package com.money.application.domain.model

data class Money(
    private val amount: Long
) {

    companion object {
        fun of(amount: Long) = Money(amount)
    }

    fun getAmount() = this.amount

    fun isPositiveOrZero(): Boolean = amount >= 0

    fun minus(money: Money): Money {
        val amount = this.amount - money.amount
        return Money(amount)
    }

    fun plus(money: Money): Money {
        val amount = this.amount + money.amount
        return Money(amount)
    }

}