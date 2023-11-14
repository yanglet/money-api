package com.money.application.domain.model

data class Member(
    private val memberNo: Long,
    private val status: MemberStatus
) {

    companion object {
        fun of(
            memberNo: Long,
            memberStatus: MemberStatus
        ): Member {
            return Member(memberNo, memberStatus)
        }
    }

    fun isActive(): Boolean = status == MemberStatus.ACTIVE

    fun isNonActive(): Boolean = status == MemberStatus.NON_ACTIVE

    fun getStatus() = this.status

    fun getMemberNo() = this.memberNo

}