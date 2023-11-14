package com.money.application.domain.model

data class Money(
    private var amount: Long
) {

    companion object {
        fun of(amount: Long) = Money(amount)
    }

    fun getAmount() = this.amount

    fun isPositiveOrZero(): Boolean = amount >= 0

    fun isGreaterThan(money: Money): Boolean = this.amount > money.amount

    fun minus(money: Money) {
        this.amount -= money.amount
    }

    fun plus(money: Money) {
        this.amount += money.amount
    }

}