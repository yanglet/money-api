package com.money.domain.wallet.entity

import com.money.adapter.out.persistence.jpa.MemberJpaEntity
import com.money.adapter.out.persistence.jpa.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "WALLET")
class WalletJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_no", nullable = false)
    var walletNo: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    var member: MemberJpaEntity,

    @Column(name = "balance", nullable = false)
    var balance: Long,

    @Column(name = "maximum_balance", nullable = false)
    var maximumBalance: Long

) : BaseEntity() {

    fun update(balance: Long, maximumBalance: Long) {
        this.balance = balance
        this.maximumBalance = maximumBalance
    }

}