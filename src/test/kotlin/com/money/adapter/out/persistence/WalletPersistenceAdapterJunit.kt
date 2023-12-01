package com.money.adapter.out.persistence

import com.money.adapter.out.persistence.jpa.MemberJpaEntity
import com.money.adapter.out.persistence.jpa.MemberJpaRepository
import com.money.adapter.out.persistence.jpa.WalletJpaRepository
import com.money.application.domain.model.Member
import com.money.application.domain.model.MemberStatus.ACTIVE
import com.money.application.domain.model.MemberStatus.NON_ACTIVE
import com.money.application.domain.model.Money
import com.money.application.domain.model.Wallet
import com.money.common.exception.DataNotFoundException
import com.money.domain.wallet.entity.WalletJpaEntity
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
@Import(WalletPersistenceAdapter::class)
class WalletPersistenceAdapterJunit(
    @Autowired private val entityManager: EntityManager,
    @Autowired private val memberJpaRepository: MemberJpaRepository,
    @Autowired private val walletJpaRepository: WalletJpaRepository,
    @Autowired private val walletPersistenceAdapter: WalletPersistenceAdapter
) {

    @AfterEach
    fun afterEach() {
        walletJpaRepository.deleteAll()
        memberJpaRepository.deleteAll()
    }

    @Test
    fun `loadWalletLock 성공`() {
        val memberJpaEntity = memberJpaRepository.save(MemberJpaEntity(status = ACTIVE))
        val walletJpaEntity = walletJpaRepository.save(WalletJpaEntity(member = memberJpaEntity, balance = 10000, maximumBalance = 100000))

        val wallet = walletPersistenceAdapter.loadWalletLock(memberNo = memberJpaEntity.memberNo)

        entityManager.getLockMode(walletJpaEntity) shouldBe LockModeType.PESSIMISTIC_WRITE
        wallet.getWalletNo() shouldBe walletJpaEntity.walletNo
    }

    @Test
    fun `loadWalletLock 실패 - 찾을 수 없는 회원입니다`() {
        val memberJpaEntity = memberJpaRepository.save(MemberJpaEntity(status = NON_ACTIVE))

        val dataNotFoundException = assertThrows<DataNotFoundException> {
            walletPersistenceAdapter.loadWalletLock(memberNo = memberJpaEntity.memberNo)
        }
        dataNotFoundException.message shouldBe "찾을 수 없는 회원입니다."
    }

    @Test
    fun `loadWalletLock 실패 - 찾을 수 없는 지갑입니다`() {
        val memberJpaEntity = memberJpaRepository.save(MemberJpaEntity(status = ACTIVE))

        val dataNotFoundException = assertThrows<DataNotFoundException> {
            walletPersistenceAdapter.loadWalletLock(memberNo = memberJpaEntity.memberNo)
        }
        dataNotFoundException.message shouldBe "찾을 수 없는 지갑입니다."
    }

    @Test
    fun `updateWallet 성공`() {
        val memberJpaEntity = memberJpaRepository.save(MemberJpaEntity(status = ACTIVE))
        val walletJpaEntity = walletJpaRepository.save(WalletJpaEntity(member = memberJpaEntity, balance = 10000, maximumBalance = 100000))

        val updatedBalance = Money.of(walletJpaEntity.balance + 10000)
        val updatedWallet = Wallet.withId(
            walletNo = walletJpaEntity.walletNo,
            member = Member.withId(memberNo = memberJpaEntity.memberNo, memberStatus = memberJpaEntity.status),
            balance = updatedBalance,
            maximumBalance = Money.of(walletJpaEntity.maximumBalance)
        )

        walletPersistenceAdapter.updateWallet(updatedWallet)

        val result = walletJpaRepository.findByIdOrNull(walletJpaEntity.walletNo) ?: throw DataNotFoundException("")

        result.walletNo shouldBe updatedWallet.getWalletNo()
        result.balance shouldBe updatedWallet.getBalance().getAmount()
        result.maximumBalance shouldBe updatedWallet.getMaximumBalance().getAmount()
    }

}