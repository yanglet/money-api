package com.money.application.domain.model

data class Money(
    private var amount: Long
) {

    companion object {
        fun of(amount: Long) = Money(amount)

        fun subtract(a: Money, b: Money) = of(a.amount - b.amount)

        fun add(a: Money, b: Money) = of(a.amount + b.amount)
    }

    fun getAmount() = this.amount

    fun isPositiveOrZero(): Boolean = amount >= 0

    fun isPositive(): Boolean = amount > 0

    fun isNegative(): Boolean = amount < 0

    fun isGreaterThan(money: Money): Boolean = this.amount > money.amount

    fun minus(money: Money) {
        this.amount -= money.amount
    }

    fun plus(money: Money) {
        this.amount += money.amount
    }

}