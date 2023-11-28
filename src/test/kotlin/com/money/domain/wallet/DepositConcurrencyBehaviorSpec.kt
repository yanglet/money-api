package com.money.domain.wallet

import com.money.adapter.out.persistence.jpa.MemberJpaEntity
import com.money.adapter.out.persistence.jpa.MemberJpaRepository
import com.money.adapter.out.persistence.jpa.WalletJpaRepository
import com.money.application.domain.model.Money
import com.money.application.domain.usecase.DepositService
import com.money.application.port.`in`.dto.DepositCommand
import com.money.common.exception.DataNotFoundException
import com.money.domain.wallet.entity.WalletJpaEntity
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@TestPropertySource(properties = ["spring.profiles.active = test"])
class DepositConcurrencyBehaviorSpec(
    private val memberRepository: MemberJpaRepository,

    private val walletRepository: WalletJpaRepository,
    private val walletService: DepositService
) : BehaviorSpec({

    Given("지갑 금액이 0원 일 때") {
        val member = memberRepository.save(MemberJpaEntity())
        val walletNo = walletRepository.save(
            WalletJpaEntity(member = member, balance = 0L, maximumBalance = 100000L)
        ).walletNo

        When("1000원씩 10번의 충전을 동시에 시도할 경우") {
            val threadPool = Executors.newFixedThreadPool(10)
            val latch = CountDownLatch(10)

            for (i: Int in 1..10) {
                threadPool.submit {
                    try {
                        walletService.deposit(
                            member.memberNo,
                            DepositCommand(Money.of(1000))
                        )
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()

            Then("보유 금액은 10000원이 된다.") {
                val wallet = walletRepository.findByIdOrNull(walletNo) ?: throw DataNotFoundException("")

                wallet.balance shouldBe 10000L
            }
        }
    }
})