package com.money.domain.wallet.service

import com.money.domain.common.service.*
import com.money.domain.member.entity.MemberStatus.ACTIVE
import com.money.domain.member.exception.*
import com.money.domain.member.repository.*
import com.money.domain.wallet.exception.*
import com.money.domain.wallet.repository.*
import com.money.domain.wallet.service.dto.*
import org.springframework.stereotype.*

@Service
class WalletService(
    private val walletRepository: WalletRepository,
    private val memberRepository: MemberRepository,

    private val distributedLockService: DistributedLockService
) {
    fun deposit(memberNo: Long, request: WalletDepositRequest) {
        distributedLockService.doDistributedLock("deposit::$memberNo") {
            val member = memberRepository.findByMemberNoAndStatus(memberNo, ACTIVE) ?: throw MemberNotFoundException("찾을 수 없는 회원입니다.")
            val wallet = walletRepository.findByMember(member) ?: throw WalletNotFoundException("찾을 수 없는 지갑입니다.")
            wallet.deposit(request.amount)
            walletRepository.flush()
        }
    }
}