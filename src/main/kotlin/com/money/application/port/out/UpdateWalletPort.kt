package com.money.application.port.out

import com.money.application.domain.model.Wallet

interface UpdateWalletPort {
    fun updateWallet(wallet: Wallet)
}