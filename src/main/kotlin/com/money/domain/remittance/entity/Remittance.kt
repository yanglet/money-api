package com.money.domain.remittance.entity

import com.money.domain.common.entity.*
import jakarta.persistence.*

@Entity
@Table(name = "REMITTANCE")
class Remittance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "remittance_no", nullable = false)
    var remittanceNo: Long = 0,

    @Column(name = "to_member_no", nullable = false)
    var to: Long,

    @Column(name = "from_member_no", nullable = false)
    var from: Long,

    @Column(name = "to_balance", nullable = false)
    var toBalance: Long,

    @Column(name = "from_balance", nullable = false)
    var fromBalance: Long,

    @Column(name = "amount", nullable = false)
    var amount: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: RemittanceStatus,

    @Column(name = "reason")
    var reason: String? = null

) : BaseEntity() {
}