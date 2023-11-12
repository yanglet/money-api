package com.money.global.init

import com.money.domain.member.entity.*
import com.money.domain.member.repository.*
import com.money.domain.wallet.entity.*
import com.money.domain.wallet.repository.*
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ApplicationReadyEventListener(
    private val memberRepository: MemberRepository,
    private val walletRepository: WalletRepository
) {
    @Transactional
    @EventListener(ApplicationReadyEvent::class)
    fun init() {
        for (i in 1 .. 10) {
            when (i % 3) {
                1 -> {
                    val member = memberRepository.save(Member(status = MemberStatus.NON_ACTIVE))
                    walletRepository.save(
                        Wallet(member = member, balance = 10000L * i, maximumBalance = 100000L * i)
                    )
                }
                else -> {
                    val member = memberRepository.save(Member())
                    walletRepository.save(
                        Wallet(member = member, balance = 10000L * i, maximumBalance = 100000L * i)
                    )
                }
            }
        }
    }
}