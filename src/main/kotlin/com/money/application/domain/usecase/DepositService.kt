package com.money.application.domain.usecase

import com.money.application.port.`in`.DepositUseCase
import com.money.application.port.`in`.dto.DepositCommand
import com.money.application.port.out.LoadWalletLockPort
import com.money.application.port.out.UpdateWalletPort
import com.money.application.port.out.dto.UpdateWalletCommand
import com.money.common.UseCase
import org.springframework.transaction.annotation.Transactional

@UseCase
class DepositService(
    private val loadWalletLockPort: LoadWalletLockPort,
    private val updateWalletPort: UpdateWalletPort
) : DepositUseCase {

    @Transactional
    override fun deposit(memberNo: Long, command: DepositCommand) {
        val wallet = loadWalletLockPort.loadWalletLock(memberNo)
        wallet.deposit(command.money)
        updateWalletPort.updateWallet(UpdateWalletCommand.from(wallet))
    }

}