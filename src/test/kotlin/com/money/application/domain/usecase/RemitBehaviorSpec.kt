package com.money.application.domain.usecase

import com.money.adapter.out.persistence.jpa.MemberJpaEntity
import com.money.adapter.out.persistence.jpa.MemberJpaRepository
import com.money.adapter.out.persistence.jpa.RemittanceJpaRepository
import com.money.adapter.out.persistence.jpa.WalletJpaRepository
import com.money.application.domain.model.Money
import com.money.application.domain.model.RemittanceStatus.FAIL
import com.money.application.domain.model.RemittanceStatus.SUCCESS
import com.money.application.port.`in`.dto.RemitCommand
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
class RemitBehaviorSpec(
    private val remittanceRepository: RemittanceJpaRepository,
    private val memberRepository: MemberJpaRepository,
    private val walletRepository: WalletJpaRepository,
    private val remitService: RemitService,
): BehaviorSpec({

    afterTest {
        walletRepository.deleteAll()
        memberRepository.deleteAll()
        remittanceRepository.deleteAll()
    }

    Given("1. 2명의 회원이 존재할 때") {
        val toMember = memberRepository.save(MemberJpaEntity())
        val toWalletNo = walletRepository.save(
            WalletJpaEntity(member = toMember, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember = memberRepository.save(MemberJpaEntity())
        val fromWalletNo = walletRepository.save(
            WalletJpaEntity(member = fromMember, balance = 150000, maximumBalance = 350000)
        ).walletNo

        When("from 의 잔액이 부족한 송금 요청이 올 경우") {
            remitService.remit(
                RemitCommand(
                    from = fromMember.memberNo, to = toMember.memberNo, money = Money.of(160000)
                )
            )

            Then("송금이 실패한다.") {
                val remittance = remittanceRepository.findAll()[0]
                val toWallet = walletRepository.findByIdOrNull(toWalletNo) ?: throw DataNotFoundException("")
                val fromWallet = walletRepository.findByIdOrNull(fromWalletNo) ?: throw DataNotFoundException("")

                remittance.status shouldBe FAIL
                remittance.reason shouldBe "from 의 잔액부족"
                toWallet.balance shouldBe 100000
                fromWallet.balance shouldBe 150000
            }
        }
    }

    Given("2. 2명의 회원이 존재할 때") {
        val toMember = memberRepository.save(MemberJpaEntity())
        val toWalletNo = walletRepository.save(
            WalletJpaEntity(member = toMember, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember = memberRepository.save(MemberJpaEntity())
        val fromWalletNo = walletRepository.save(
            WalletJpaEntity(member = fromMember, balance = 250000, maximumBalance = 350000)
        ).walletNo

        When("to 의 한도가 초과하는 송금 요청이 올 경우") {
            remitService.remit(
                RemitCommand(
                    from = fromMember.memberNo, to = toMember.memberNo, money = Money.of(210000)
                )
            )

            Then("송금이 실패한다.") {
                val remittance = remittanceRepository.findAll()[0]
                val toWallet = walletRepository.findByIdOrNull(toWalletNo) ?: throw DataNotFoundException("")
                val fromWallet = walletRepository.findByIdOrNull(fromWalletNo) ?: throw DataNotFoundException("")

                remittance.status shouldBe FAIL
                remittance.reason shouldBe "to 의 한도초과"
                toWallet.balance shouldBe 100000
                fromWallet.balance shouldBe 250000
            }
        }
    }

    Given("3. 2명의 회원이 존재할 때") {
        val toMember = memberRepository.save(MemberJpaEntity())
        val toWalletNo = walletRepository.save(
            WalletJpaEntity(member = toMember, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember = memberRepository.save(MemberJpaEntity())
        val fromWalletNo = walletRepository.save(
            WalletJpaEntity(member = fromMember, balance = 150000, maximumBalance = 350000)
        ).walletNo

        When("정상적인 송금 요청이 올 경우") {
            remitService.remit(
                RemitCommand(
                    from = fromMember.memberNo, to = toMember.memberNo, money = Money.of(110000)
                )
            )

            Then("송금이 성공한다.") {
                val remittance = remittanceRepository.findAll()[0]
                val toWallet = walletRepository.findByIdOrNull(toWalletNo) ?: throw DataNotFoundException("")
                val fromWallet = walletRepository.findByIdOrNull(fromWalletNo) ?: throw DataNotFoundException("")

                remittance.status shouldBe SUCCESS
                remittance.fromBalance shouldBe 40000
                remittance.toBalance shouldBe 210000
                fromWallet.balance shouldBe 40000
                toWallet.balance shouldBe 210000
            }
        }
    }

    Given("4. 4명의 회원이 존재할 때") {
        val toMember = memberRepository.save(MemberJpaEntity())
        val toWalletNo = walletRepository.save(
            WalletJpaEntity(member = toMember, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember1 = memberRepository.save(MemberJpaEntity())
        val fromWalletNo1 = walletRepository.save(
            WalletJpaEntity(member = fromMember1, balance = 100000, maximumBalance = 250000)
        ).walletNo

        val fromMember2 = memberRepository.save(MemberJpaEntity())
        val fromWalletNo2 = walletRepository.save(
            WalletJpaEntity(member = fromMember2, balance = 180000, maximumBalance = 250000)
        ).walletNo

        val fromMember3 = memberRepository.save(MemberJpaEntity())
        val fromWalletNo3 = walletRepository.save(
            WalletJpaEntity(member = fromMember3, balance = 150000, maximumBalance = 350000)
        ).walletNo

        When("3명이 1명에게 동시 송금을 할 경우") {
            val threadPool = Executors.newFixedThreadPool(3)
            val latch = CountDownLatch(3)
            val fromMemberNos = listOf(fromMember1.memberNo, fromMember2.memberNo, fromMember3.memberNo)

            for (i: Int in 1..3) {
                threadPool.submit {
                    try {
                        remitService.remit(
                            RemitCommand(
                                from = fromMemberNos[i - 1], to = toMember.memberNo, money = Money.of(10000)
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
                val toWallet = walletRepository.findByIdOrNull(toWalletNo) ?: throw DataNotFoundException("")
                val fromWallet1 = walletRepository.findByIdOrNull(fromWalletNo1) ?: throw DataNotFoundException("")
                val fromWallet2 = walletRepository.findByIdOrNull(fromWalletNo2) ?: throw DataNotFoundException("")
                val fromWallet3 = walletRepository.findByIdOrNull(fromWalletNo3) ?: throw DataNotFoundException("")

                remittances.filter { it.status == SUCCESS }.size shouldBe 3
                toWallet.balance shouldBe 130000
                fromWallet1.balance shouldBe 90000
                fromWallet2.balance shouldBe 170000
                fromWallet3.balance shouldBe 140000
            }
        }
    }
})
