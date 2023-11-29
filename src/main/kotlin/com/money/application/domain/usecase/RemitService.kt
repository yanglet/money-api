package com.money.application.domain.usecase

import com.money.application.domain.exception.BalanceInsufficientException
import com.money.application.domain.exception.BalanceMaxedOutException
import com.money.application.domain.model.*
import com.money.application.domain.model.RemittanceStatus.*
import com.money.application.port.`in`.RemitUseCase
import com.money.application.port.`in`.dto.RemitCommand
import com.money.application.port.out.CreateRemittancePort
import com.money.application.port.out.LoadMemberPort
import com.money.application.port.out.LoadWalletLockPort
import com.money.application.port.out.UpdateWalletPort
import com.money.common.UseCase
import org.springframework.transaction.annotation.Transactional

@UseCase
class RemitService(
    private val loadWalletLockPort: LoadWalletLockPort,
    private val updateWalletPort: UpdateWalletPort,
    private val createRemittancePort: CreateRemittancePort,
    private val loadMemberPort: LoadMemberPort
) : RemitUseCase {

    @Transactional
    override fun remit(command: RemitCommand) {
        val toMember = loadMemberPort.loadMember(command.to)
        val toWallet = loadWalletLockPort.loadWalletLock(command.to)

        val fromMember = loadMemberPort.loadMember(command.from)
        val fromWallet = loadWalletLockPort.loadWalletLock(command.from)

        val remittance = runCatching {
            fromWallet.withdraw(command.money)
            toWallet.deposit(command.money)
        }.fold(
            onFailure = {
                if (it is BalanceMaxedOutException) { // rollback
                    fromWallet.deposit(command.money)
                }

                Remittance.withoutId(
                    to = toMember,
                    from = fromMember,
                    amount = command.money,
                    remittanceStatus = FAIL,
                    reason = when (it) {
                        is BalanceInsufficientException -> "from 의 잔액부족"
                        is BalanceMaxedOutException -> "to 의 한도초과"
                        else -> ""
                    }
                )
            },
            onSuccess = {
                Remittance.withoutId(
                    to = toMember,
                    from = fromMember,
                    amount = command.money,
                    remittanceStatus = SUCCESS
                )
            }
        )
        updateWalletPort.updateWallet(fromWallet)
        updateWalletPort.updateWallet(toWallet)
        createRemittancePort.createRemittance(remittance)
    }

}