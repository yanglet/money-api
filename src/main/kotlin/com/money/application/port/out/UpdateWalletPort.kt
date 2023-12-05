package com.money.application.port.out

import com.money.application.port.out.dto.UpdateWalletCommand

interface UpdateWalletPort {
    fun updateWallet(command: UpdateWalletCommand)
}