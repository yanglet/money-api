package com.money.domain.remittance.service

import com.money.domain.common.service.*
import com.money.domain.member.entity.MemberStatus.ACTIVE
import com.money.domain.member.exception.*
import com.money.domain.member.repository.*
import com.money.domain.remittance.entity.*
import com.money.domain.remittance.repository.*
import com.money.domain.remittance.service.dto.*
import com.money.domain.remittance.service.extension.*
import com.money.domain.wallet.exception.*
import com.money.domain.wallet.repository.*
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.*
import org.springframework.stereotype.*
import org.springframework.transaction.annotation.Transactional

@Service
class RemittanceService(
    private val remittanceRepository: RemittanceRepository,
    private val memberRepository: MemberRepository,
    private val walletRepository: WalletRepository,
    private val distributedLockService: DistributedLockService
) {
    @CacheEvict(cacheNames = ["Remittance"], key = "#request.from", cacheManager = "customCacheManager")
    fun remit(request: RemittanceSaveRequest) {
        distributedLockService.doDistributedLock("remit::${request.to}") {
            val fromMember = memberRepository.findByMemberNoAndStatus(request.from, ACTIVE) ?: throw MemberNotFoundException("찾을 수 없는 회원입니다.")
            val fromWallet = walletRepository.findByMember(fromMember) ?: throw WalletNotFoundException("찾을 수 없는 지갑입니다.")

            val toMember = memberRepository.findByMemberNoAndStatus(request.to, ACTIVE) ?: throw MemberNotFoundException("찾을 수 없는 회원입니다.")
            val toWallet = walletRepository.findByMember(toMember) ?: throw WalletNotFoundException("찾을 수 없는 지갑입니다.")

            val remittance = runCatching {
                fromWallet.withdraw(request.amount)
                toWallet.deposit(request.amount)
            }.fold(
                onFailure = {
                    if (it is BalanceMaxedOutException) {
                        fromWallet.deposit(request.amount)
                    }

                    Remittance(
                        to = toMember.memberNo,
                        from = fromMember.memberNo,
                        toBalance = toWallet.balance,
                        fromBalance = fromWallet.balance,
                        amount = request.amount,
                        status = RemittanceStatus.FAIL,
                        reason = when (it) {
                            is BalanceInsufficientException -> "from 의 잔액부족"
                            is BalanceMaxedOutException -> "to 의 한도초과"
                            else -> ""
                        }
                    )
                },
                onSuccess = {
                    Remittance(
                        to = toMember.memberNo,
                        from = fromMember.memberNo,
                        toBalance = toWallet.balance,
                        fromBalance = fromWallet.balance,
                        amount = request.amount,
                        status = RemittanceStatus.SUCCESS
                    )
                }
            )
            remittanceRepository.saveAndFlush(remittance)
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ["Remittance"], key = "#memberNo", cacheManager = "customCacheManager")
    fun readRemittancesByMemberNo(memberNo: Long): Iterable<RemittanceReadResponse> {
        val from = memberRepository.findByIdOrNull(memberNo) ?: throw MemberNotFoundException("찾을 수 없는 회원입니다.")
        val remittances = remittanceRepository.findByFrom(from.memberNo)
        return remittances.map { it.toReadResponse() }
    }
}