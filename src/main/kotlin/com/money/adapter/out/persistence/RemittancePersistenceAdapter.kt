package com.money.adapter.out.persistence

import com.money.adapter.out.persistence.jpa.MemberJpaRepository
import com.money.adapter.out.persistence.jpa.RemittanceJpaEntity
import com.money.adapter.out.persistence.jpa.RemittanceJpaRepository
import com.money.adapter.out.persistence.jpa.WalletJpaRepository
import com.money.application.domain.model.Member
import com.money.application.domain.model.MemberStatus.ACTIVE
import com.money.application.domain.model.Money
import com.money.application.domain.model.Remittance
import com.money.application.port.out.CreateRemittancePort
import com.money.application.port.out.LoadRemittancesPort
import com.money.common.PersistenceAdapter
import com.money.common.exception.DataNotFoundException
import org.springframework.data.repository.findByIdOrNull

@PersistenceAdapter
class RemittancePersistenceAdapter(
    private val remittanceJpaRepository: RemittanceJpaRepository,
    private val walletJpaRepository: WalletJpaRepository,
    private val memberJpaRepository: MemberJpaRepository
) : CreateRemittancePort, LoadRemittancesPort {

    override fun createRemittance(remittance: Remittance) {
        val toMemberJpaEntity = memberJpaRepository.findByMemberNoAndStatus(remittance.getTo().getMemberNo(), ACTIVE) ?: throw DataNotFoundException("찾을 수 없는 회원입니다.")
        val toWalletJpaEntity = walletJpaRepository.findByMember(toMemberJpaEntity) ?: throw DataNotFoundException("찾을 수 없는 지갑입니다.")

        val fromMemberJpaEntity = memberJpaRepository.findByMemberNoAndStatus(remittance.getFrom().getMemberNo(), ACTIVE) ?: throw DataNotFoundException("찾을 수 없는 회원입니다.")
        val fromWalletJpaEntity = walletJpaRepository.findByMember(fromMemberJpaEntity) ?: throw DataNotFoundException("찾을 수 없는 지갑입니다.")

        val remittanceJpaEntity = RemittanceJpaEntity(
            to = remittance.getTo().getMemberNo(),
            from = remittance.getFrom().getMemberNo(),
            toBalance = toWalletJpaEntity.balance,
            fromBalance = fromWalletJpaEntity.balance,
            amount = remittance.getMoney().getAmount(),
            status = remittance.getStatus(),
            reason = remittance.getReason()
        )
        remittanceJpaRepository.save(remittanceJpaEntity)
    }

    override fun readRemittances(memberNo: Long): List<Remittance> {
        memberJpaRepository.findByIdOrNull(memberNo) ?: throw DataNotFoundException("찾을 수 없는 회원입니다.")
        val remittanceJpaEntities = remittanceJpaRepository.findByFrom(memberNo)

        return remittanceJpaEntities.map {
            Remittance.withId(
                remittanceNo = it.remittanceNo,
                to = Member.withId(it.to, ACTIVE),
                from = Member.withId(it.from, ACTIVE),
                amount = Money.of(it.amount),
                remittanceStatus = it.status,
                reason = it.reason
            )
        }
    }

}