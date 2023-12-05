package com.money.adapter.out.persistence

import com.money.adapter.out.persistence.jpa.MemberJpaRepository
import com.money.adapter.out.persistence.jpa.WalletJpaRepository
import com.money.application.domain.model.Member
import com.money.application.domain.model.MemberStatus.ACTIVE
import com.money.application.domain.model.Money
import com.money.application.domain.model.Wallet
import com.money.application.port.out.LoadWalletLockPort
import com.money.application.port.out.UpdateWalletPort
import com.money.application.port.out.dto.UpdateWalletCommand
import com.money.common.PersistenceAdapter
import com.money.common.exception.DataNotFoundException
import org.springframework.data.repository.findByIdOrNull

@PersistenceAdapter
class WalletPersistenceAdapter(
    private val walletJpaRepository: WalletJpaRepository,
    private val memberJpaRepository: MemberJpaRepository
) : LoadWalletLockPort, UpdateWalletPort {

    override fun loadWalletLock(memberNo: Long): Wallet {
        val memberJpaEntity = memberJpaRepository.findByMemberNoAndStatus(memberNo, ACTIVE) ?: throw DataNotFoundException("찾을 수 없는 회원입니다.")
        val walletJpaEntity = walletJpaRepository.findByMemberWithLock(memberJpaEntity) ?: throw DataNotFoundException("찾을 수 없는 지갑입니다.")

        return Wallet.withId(
            walletNo = walletJpaEntity.walletNo,
            member = Member.withId(memberJpaEntity.memberNo, memberJpaEntity.status),
            balance = Money.of(walletJpaEntity.balance),
            maximumBalance = Money.of(walletJpaEntity.maximumBalance)
        )
    }

    override fun updateWallet(command: UpdateWalletCommand) {
        val walletJpaEntity = walletJpaRepository.findByIdOrNull(command.walletNo) ?: throw DataNotFoundException("찾을 수 없는 지갑입니다.")
        walletJpaEntity.update(
            balance = command.balance.getAmount(),
            maximumBalance = command.maximumBalance.getAmount()
        )
    }

}