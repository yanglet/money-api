package com.money.adapter.`in`.web

import com.money.adapter.`in`.web.dto.RemitRequest
import com.money.application.domain.model.Money
import com.money.application.port.`in`.RemitUseCase
import com.money.application.port.`in`.dto.RemitCommand
import com.money.common.WebAdapter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@WebAdapter
@RequestMapping("/v1/remittances")
class RemittanceController(
    private val remitUseCase: RemitUseCase
) {

    @PostMapping("/save")
    fun remit(request: RemitRequest) {
        val command = RemitCommand(request.to, request.from, Money.of(request.amount))
        remitUseCase.remit(command)
    }

}