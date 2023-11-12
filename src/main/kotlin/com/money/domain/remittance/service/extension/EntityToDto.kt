package com.money.domain.remittance.service.extension

import com.money.domain.remittance.entity.*
import com.money.domain.remittance.service.dto.*

fun Remittance.toReadResponse() = RemittanceReadResponse(
    remittanceNo = this.remittanceNo,
    to = this.to,
    from = this.from,
    toBalance = this.toBalance,
    fromBalance = this.fromBalance,
    amount = this.amount,
    status = this.status.toString(),
    reason = this.reason,
    insertDate = this.insertDate,
    updateDate = this.updateDate
)