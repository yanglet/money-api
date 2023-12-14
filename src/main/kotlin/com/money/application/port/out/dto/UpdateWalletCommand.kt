package com.money.application.port.out.dto

import com.money.application.domain.model.Money
import com.money.application.domain.model.Wallet

data class UpdateWalletCommand(
    val walletNo: Long,
    val balance: Money,
    val maximumBalance: Money
) {

    companion object {
        fun from(wallet: Wallet) = UpdateWalletCommand(
            walletNo = wallet.getWalletNo(),
            balance = wallet.getBalance(),
            maximumBalance = wallet.getMaximumBalance()
        )
    }
}