package com.money.domain.remittance.service.dto

import java.time.*

data class RemittanceReadResponse(
    val remittanceNo: Long,
    val to: Long,
    val from: Long,
    val toBalance: Long,
    val fromBalance: Long,
    val amount: Long,
    val status: String,
    val reason: String?,
    val insertDate: LocalDateTime,
    val updateDate : LocalDateTime
)