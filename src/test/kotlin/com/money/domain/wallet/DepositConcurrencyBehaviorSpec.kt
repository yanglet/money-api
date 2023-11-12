package com.money.domain.wallet

import com.money.domain.member.entity.*
import com.money.domain.member.repository.*
import com.money.domain.wallet.entity.*
import com.money.domain.wallet.exception.*
import com.money.domain.wallet.repository.*
import com.money.domain.wallet.service.*
import com.money.domain.wallet.service.dto.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.*
import org.springframework.test.context.*
import java.util.concurrent.*

@SpringBootTest
@TestPropertySource(properties = ["spring.profiles.active = test"])
class DepositConcurrencyBehaviorSpec(
    private val memberRepository: MemberRepository,

    private val walletRepository: WalletRepository,
    private val walletService: WalletService
) : BehaviorSpec({

    Given("지갑 금액이 0원 일 때") {
        val member = memberRepository.save(Member())
        val walletNo = walletRepository.save(
            Wallet(member = member, balance = 0L, maximumBalance = 100000L)
        ).walletNo

        When("1000원씩 10번의 충전을 동시에 시도할 경우") {
            val threadPool = Executors.newFixedThreadPool(10)
            val latch = CountDownLatch(10)

            for (i: Int in 1..10) {
                threadPool.submit {
                    try {
                        walletService.deposit(
                            member.memberNo,
                            WalletDepositRequest(amount = 1000L)
                        )
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()

            Then("보유 금액은 10000원이 된다.") {
                val wallet = walletRepository.findByIdOrNull(walletNo) ?: throw WalletNotFoundException("")

                wallet.balance shouldBe 10000L
            }
        }
    }
})