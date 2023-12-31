package com.money.adapter.out.persistence.jpa

import com.money.adapter.out.persistence.jpa.common.BaseEntity
import com.money.application.domain.model.MemberStatus
import jakarta.persistence.*

@Entity
@Table(name = "MEMBER")
class MemberJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no", nullable = false)
    var memberNo: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: MemberStatus = MemberStatus.ACTIVE
) : BaseEntity() {
}