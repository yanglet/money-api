package com.money.domain.member.repository

import com.money.domain.member.entity.*
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByMemberNoAndStatus(memberNo: Long, status: MemberStatus): Member?
}