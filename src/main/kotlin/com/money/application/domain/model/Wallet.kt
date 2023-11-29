package com.money.application.domain.model

import com.money.application.domain.exception.BalanceInsufficientException
import com.money.application.domain.exception.BalanceMaxedOutException

data class Wallet(
    private var walletNo: Long,
    private var member: Member,
    private var balance: Money,
    private var maximumBalance: Money
) {

    companion object {
        fun of(
            walletNo: Long,
            member: Member,
            balance: Money,
            maximumBalance: Money
        ): Wallet {
            require(balance.isPositiveOrZero())
            require(maximumBalance.isPositiveOrZero())
            return Wallet(walletNo, member, balance, maximumBalance)
        }
    }

    fun getWalletNo() = this.walletNo

    fun getMember() = this.member

    fun getBalance() = this.balance

    fun getMaximumBalance() = this.maximumBalance

    private fun isGreaterThanMaximumBalance(money: Money) = money.isGreaterThan(this.maximumBalance)

    fun deposit(money: Money) {
        val afterBalance = Money.add(this.balance, money)
        if (isGreaterThanMaximumBalance(afterBalance)) {
            throw BalanceMaxedOutException("금액의 최대 한도를 초과할 수 없습니다.")
        }
        this.balance.plus(money)
    }

    fun withdraw(money: Money) {
        val afterBalance = Money.subtract(this.balance, money)
        if (afterBalance.isNegative()) {
            throw BalanceInsufficientException("잔액이 부족합니다.")
        }
        this.balance.minus(money)
    }

}