package com.money.adapter.out.persistence.jpa

import com.money.adapter.out.persistence.jpa.common.BaseEntity
import com.money.application.domain.model.RemittanceStatus
import jakarta.persistence.*

@Entity
@Table(name = "REMITTANCE")
class RemittanceJpaEntity(
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