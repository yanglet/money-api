package com.money.adapter.`in`.web.dto

data class RemitRequest(
    val to: Long,
    val from: Long,
    val amount: Long
)