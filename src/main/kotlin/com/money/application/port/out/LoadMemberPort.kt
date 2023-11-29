package com.money.application.port.out

import com.money.application.domain.model.Member

interface LoadMemberPort {
    fun loadMember(memberNo: Long): Member
}