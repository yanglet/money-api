package com.money.application.domain.usecase

import com.money.application.port.`in`.DepositPort
import com.money.application.port.`in`.dto.DepositCommand
import com.money.application.port.out.LoadWalletLockPort
import com.money.application.port.out.UpdateWalletPort
import com.money.common.UseCase
import org.springframework.transaction.annotation.Transactional

@UseCase
class DepositUseCase(
    private val loadWalletLockPort: LoadWalletLockPort,
    private val updateWalletPort: UpdateWalletPort
) : DepositPort {

    @Transactional
    override fun deposit(memberNo: Long, command: DepositCommand) {
        val wallet = loadWalletLockPort.loadWalletLock(memberNo)
        wallet.deposit(command.amount)
        updateWalletPort.updateWallet(wallet)
    }

}