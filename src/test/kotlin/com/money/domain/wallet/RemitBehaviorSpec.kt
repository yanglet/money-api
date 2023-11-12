package com.money.domain.wallet

import com.money.domain.member.entity.*
import com.money.domain.member.repository.*
import com.money.domain.remittance.entity.*
import com.money.domain.remittance.repository.*
import com.money.domain.remittance.service.*
import com.money.domain.remittance.service.dto.*
import com.money.domain.wallet.entity.*
import com.money.domain.wallet.exception.*
import com.money.domain.wallet.repository.*
import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import org.springframework.boot.test.context.*
import org.springframework.data.repository.*
import org.springframework.test.context.*
import java.util.concurrent.*

@SpringBootTest
@TestPropertySource(properties = ["spring.profiles.active = test"])
class RemitBehaviorSpec(
    private val remittanceRepository: RemittanceRepository,
    private val remittanceService: RemittanceService,

    private val memberRepository: MemberRepository,

    private val walletRepository: WalletRepository,
): BehaviorSpec({

    afterTest {
        walletRepository.deleteAll()
        memberRepository.deleteAll()
        remittanceRepository.deleteAll()
    }

    Given("1. 2명의 회원이 존재할 때") {
        val toMember = memberRepository.save(Member())
        val toWalletNo = walletRepository.save(
            Wallet(member = toMember, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember = memberRepository.save(Member())
        val fromWalletNo = walletRepository.save(
            Wallet(member = fromMember, balance = 150000, maximumBalance = 350000)
        ).walletNo

        When("from 의 잔액이 부족한 송금 요청이 올 경우") {
            remittanceService.remit(
                RemittanceSaveRequest(
                    from = fromMember.memberNo, to = toMember.memberNo, amount = 160000
                )
            )

            Then("송금이 실패한다.") {
                val remittance = remittanceRepository.findAll()[0]
                val toWallet = walletRepository.findByIdOrNull(toWalletNo) ?: throw WalletNotFoundException("")
                val fromWallet = walletRepository.findByIdOrNull(fromWalletNo) ?: throw WalletNotFoundException("")

                remittance.status shouldBe RemittanceStatus.FAIL
                remittance.reason shouldBe "from 의 잔액부족"
                toWallet.balance shouldBe 100000
                fromWallet.balance shouldBe 150000
            }
        }
    }

    Given("2. 2명의 회원이 존재할 때") {
        val toMember = memberRepository.save(Member())
        val toWalletNo = walletRepository.save(
            Wallet(member = toMember, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember = memberRepository.save(Member())
        val fromWalletNo = walletRepository.save(
            Wallet(member = fromMember, balance = 250000, maximumBalance = 350000)
        ).walletNo

        When("to 의 한도가 초과하는 송금 요청이 올 경우") {
            remittanceService.remit(
                RemittanceSaveRequest(
                    from = fromMember.memberNo, to = toMember.memberNo, amount = 210000
                )
            )

            Then("송금이 실패한다.") {
                val remittance = remittanceRepository.findAll()[0]
                val toWallet = walletRepository.findByIdOrNull(toWalletNo) ?: throw WalletNotFoundException("")
                val fromWallet = walletRepository.findByIdOrNull(fromWalletNo) ?: throw WalletNotFoundException("")

                remittance.status shouldBe RemittanceStatus.FAIL
                remittance.reason shouldBe "to 의 한도초과"
                toWallet.balance shouldBe 100000
                fromWallet.balance shouldBe 250000
            }
        }
    }

    Given("3. 2명의 회원이 존재할 때") {
        val toMember = memberRepository.save(Member())
        val toWalletNo = walletRepository.save(
            Wallet(member = toMember, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember = memberRepository.save(Member())
        val fromWalletNo = walletRepository.save(
            Wallet(member = fromMember, balance = 150000, maximumBalance = 350000)
        ).walletNo

        When("정상적인 송금 요청이 올 경우") {
            remittanceService.remit(
                RemittanceSaveRequest(
                    from = fromMember.memberNo, to = toMember.memberNo, amount = 110000
                )
            )

            Then("송금이 성공한다.") {
                val remittance = remittanceRepository.findAll()[0]
                val toWallet = walletRepository.findByIdOrNull(toWalletNo) ?: throw WalletNotFoundException("")
                val fromWallet = walletRepository.findByIdOrNull(fromWalletNo) ?: throw WalletNotFoundException("")

                remittance.status shouldBe RemittanceStatus.SUCCESS
                remittance.fromBalance shouldBe 40000
                remittance.toBalance shouldBe 210000
                fromWallet.balance shouldBe 40000
                toWallet.balance shouldBe 210000
            }
        }
    }

    Given("4. 4명의 회원이 존재할 때") {
        val toMember = memberRepository.save(Member())
        val toWalletNo = walletRepository.save(
            Wallet(member = toMember, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember1 = memberRepository.save(Member())
        val fromWalletNo1 = walletRepository.save(
            Wallet(member = fromMember1, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember2 = memberRepository.save(Member())
        val fromWalletNo2 = walletRepository.save(
            Wallet(member = fromMember2, balance = 180000, maximumBalance = 250000)
        ).walletNo

        val fromMember3 = memberRepository.save(Member())
        val fromWalletNo3 = walletRepository.save(
            Wallet(member = fromMember3, balance = 150000, maximumBalance = 350000)
        ).walletNo

        When("3명이 1명에게 동시 송금을 할 경우") {
            val threadPool = Executors.newFixedThreadPool(3)
            val latch = CountDownLatch(3)
            val fromMemberNos = listOf(fromMember1.memberNo, fromMember2.memberNo, fromMember3.memberNo)

            for (i: Int in 1..3) {
                threadPool.submit {
                    try {
                        remittanceService.remit(
                            RemittanceSaveRequest(
                                from = fromMemberNos[i - 1], to = toMember.memberNo, amount = 10000
                            )
                        )
                    } finally {
                        latch.countDown()
                    }
                }
            }

            latch.await()

            Then("송금이 성공한다.") {
                val remittances = remittanceRepository.findAll()
                val toWallet = walletRepository.findByIdOrNull(toWalletNo) ?: throw WalletNotFoundException("")
                val fromWallet1 = walletRepository.findByIdOrNull(fromWalletNo1) ?: throw WalletNotFoundException("")
                val fromWallet2 = walletRepository.findByIdOrNull(fromWalletNo2) ?: throw WalletNotFoundException("")
                val fromWallet3 = walletRepository.findByIdOrNull(fromWalletNo3) ?: throw WalletNotFoundException("")

                remittances.filter { it.status == RemittanceStatus.SUCCESS }.size shouldBe 3
                toWallet.balance shouldBe 130000
                fromWallet1.balance shouldBe 90000
                fromWallet2.balance shouldBe 170000
                fromWallet3.balance shouldBe 140000
            }
        }
    }
})