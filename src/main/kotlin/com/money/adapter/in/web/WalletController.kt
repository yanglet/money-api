package com.money.adapter.`in`.web

import com.money.adapter.`in`.web.dto.DepositRequest
import com.money.application.domain.model.Money
import com.money.application.port.`in`.DepositUseCase
import com.money.application.port.`in`.dto.DepositCommand
import com.money.common.WebAdapter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@WebAdapter
@RequestMapping("/v1/wallets")
class WalletController(
    private val depositUseCase: DepositUseCase
) {

    @PostMapping("/{memberNo}/deposit")
    @ResponseStatus(HttpStatus.OK)
    fun deposit(
        @PathVariable memberNo: Long,
        @RequestBody request: DepositRequest
    ) {
        val command = DepositCommand(Money.of(request.amount))
        depositUseCase.deposit(memberNo, command)
    }
}