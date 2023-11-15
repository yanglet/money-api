package com.money.application.domain

import com.money.application.domain.exception.BalanceMaxedOutException
import com.money.application.domain.model.Member
import com.money.application.domain.model.MemberStatus
import com.money.application.domain.model.Money
import com.money.application.domain.model.Wallet
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class WalletBehaviorSpec : BehaviorSpec({

    Given("deposit") {
        val member = Member.of(
            memberNo = 1,
            MemberStatus.ACTIVE
        )
        val wallet = Wallet.of(
            walletNo = 1,
            member = member,
            balance = Money.of(10000),
            maximumBalance = Money.of(15000)
        )

        When("maximumBalance 를 초과하면") {
            val amount = Money.of(10000)

            Then("BalanceMaxedOutException 예외가 발생한다.") {
                shouldThrow<BalanceMaxedOutException> { wallet.deposit(amount) }
            }
        }

        When("maximumBalance 를 초과하지 않으면") {
            val amount = Money.of(1000)

            Then("성공한다.") {
                wallet.deposit(amount)

                wallet.getBalance().getAmount() shouldBe 11000
            }
        }
    }

})