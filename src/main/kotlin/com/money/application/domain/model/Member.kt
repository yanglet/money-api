package com.money.application.domain.model

data class Member(
    private var memberNo: Long,
    private var status: MemberStatus
) {

    companion object {
        fun withId(
            memberNo: Long,
            memberStatus: MemberStatus
        ): Member {
            require(memberNo > 0)

            return Member(memberNo, memberStatus)
        }
    }

    fun isActive(): Boolean = status == MemberStatus.ACTIVE

    fun isNonActive(): Boolean = status == MemberStatus.NON_ACTIVE

    fun getStatus() = this.status

    fun getMemberNo() = this.memberNo

}