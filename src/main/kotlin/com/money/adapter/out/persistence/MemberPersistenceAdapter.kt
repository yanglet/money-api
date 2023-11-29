package com.money.adapter.out.persistence

import com.money.adapter.out.persistence.jpa.MemberJpaRepository
import com.money.application.domain.model.Member
import com.money.application.port.out.LoadMemberPort
import com.money.common.PersistenceAdapter
import com.money.common.exception.DataNotFoundException
import org.springframework.data.repository.findByIdOrNull

@PersistenceAdapter
class MemberPersistenceAdapter(
    private val memberJpaRepository: MemberJpaRepository
) : LoadMemberPort {

    override fun loadMember(memberNo: Long): Member {
        val memberJpaEntity = memberJpaRepository.findByIdOrNull(memberNo) ?: throw DataNotFoundException("찾을 수 없는 회원입니다.")

        return Member.of(
            memberNo = memberJpaEntity.memberNo,
            memberStatus = memberJpaEntity.status
        )
    }

}