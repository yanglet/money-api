package com.money.application.port.out

import com.money.application.domain.model.Wallet

interface LoadWalletLockPort {
    fun loadWalletLock(memberNo: Long): Wallet
}