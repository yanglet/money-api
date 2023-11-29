package com.money.adapter.out.persistence.jpa

import com.money.domain.wallet.entity.WalletJpaEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface WalletJpaRepository : JpaRepository<WalletJpaEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @QueryHints(QueryHint(name = "", value = "")) LockTimeout Setting
    @Query("""
        SELECT wallet 
        FROM WalletJpaEntity wallet
        WHERE wallet.member = :member
    """)
    fun findByMemberWithLock(member: MemberJpaEntity): WalletJpaEntity?

    fun findByMember(member: MemberJpaEntity): WalletJpaEntity?
}