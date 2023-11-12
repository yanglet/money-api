package com.money.domain.wallet.entity

import com.money.domain.common.entity.*
import com.money.domain.member.entity.*
import com.money.domain.wallet.exception.*
import jakarta.persistence.*

@Entity
@Table(name = "WALLET")
class Wallet(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_no", nullable = false)
    var walletNo: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    var member: Member,

    @Column(name = "balance", nullable = false)
    var balance: Long,

    @Column(name = "maximum_balance", nullable = false)
    var maximumBalance: Long

) : BaseEntity() {

    fun deposit(amount: Long) {
        if (this.balance + amount > maximumBalance) {
            throw BalanceMaxedOutException("금액의 최대 한도를 초과할 수 없습니다.")
        }
        this.balance = this.balance + amount
    }

    fun withdraw(amount: Long) {
        if (this.balance - amount < 0) {
            throw BalanceInsufficientException("금액이 부족합니다.")
        }
        this.balance = this.balance - amount
    }
}