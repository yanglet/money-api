package com.money.application.domain

import com.money.application.domain.model.Money
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class MoneyAnnotationSpec : AnnotationSpec() {

    @Test
    fun isPositiveOrZero() {
        val money = Money.of(10000)

        money.isPositiveOrZero() shouldBe true
    }

    @Test
    fun isPositive() {
        val money = Money.of(10000)
        val zeroMoney = Money.of(0)

        money.isPositive() shouldBe true
        zeroMoney.isPositive() shouldBe false
    }

    @Test
    fun isNegative() {
        val money = Money.of(10000)

        money.isNegative() shouldBe false
    }

    @Test
    fun isGreaterThan() {
        val money = Money.of(10000)
        val greaterMoney = Money.of(20000)

        greaterMoney.isGreaterThan(money) shouldBe true
    }

    @Test
    fun minus() {
        val money = Money.of(10000)
        val greaterMoney = Money.of(20000)

        greaterMoney.minus(money)

        greaterMoney.getAmount() shouldBe 10000
    }

    @Test
    fun plus() {
        val money = Money.of(10000)
        val greaterMoney = Money.of(20000)

        money.plus(greaterMoney)

        money.getAmount() shouldBe 30000
    }

}