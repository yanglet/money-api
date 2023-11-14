package com.money.adapter.out.persistence.jpa

import com.money.application.domain.model.MemberStatus
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long> {
    fun findByMemberNoAndStatus(memberNo: Long, status: MemberStatus): MemberJpaEntity?
}