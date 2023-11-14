package com.money.application.domain.model

data class Wallet(
    private val walletNo: Long,
    private val member: Member,
    private val balance: Money,
    private val maximumBalance: Money
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

    fun deposit(money: Money) {
        this.balance.plus(money)
    }

}