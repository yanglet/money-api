package com.money.application.domain.model

import com.money.application.domain.model.RemittanceStatus.*

data class Remittance(
    private var remittanceNo: Long?,
    private var to: Member,
    private var from: Member,
    private var money: Money,
    private var status: RemittanceStatus,
    private var reason: String?
) {

    companion object {
        fun withId(
            remittanceNo: Long,
            to: Member,
            from: Member,
            amount: Money,
            remittanceStatus: RemittanceStatus,
            reason: String? = null
        ): Remittance {
            require(remittanceNo > 0)
            require(to.isActive())
            require(from.isActive())
            require(amount.isPositive())

            return Remittance(remittanceNo, to, from, amount, remittanceStatus, reason)
        }

        fun withoutId(
            to: Member,
            from: Member,
            amount: Money,
            remittanceStatus: RemittanceStatus,
            reason: String? = null
        ): Remittance {
            require(to.isActive())
            require(from.isActive())
            require(amount.isPositive())

            return Remittance(null, to, from, amount, remittanceStatus, reason)
        }
    }

    fun isSuccess() = this.status == SUCCESS

    fun isFail() = this.status == FAIL

    fun getRemittanceNo() = this.remittanceNo

    fun getTo() = this.to

    fun getFrom() = this.from

    fun getMoney() = this.money

    fun getStatus() = this.status

    fun getReason() = this.reason

}
