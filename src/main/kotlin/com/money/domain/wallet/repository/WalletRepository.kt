package com.money.domain.wallet.repository

import com.money.domain.member.entity.*
import com.money.domain.wallet.entity.*
import org.springframework.data.jpa.repository.JpaRepository

interface WalletRepository : JpaRepository<Wallet, Long> {
    fun findByMember(member: Member): Wallet?
}