package com.money.adapter.`in`.web

import com.money.adapter.`in`.web.dto.DepositRequest
import com.money.application.domain.model.Money
import com.money.application.port.`in`.DepositPort
import com.money.application.port.`in`.dto.DepositCommand
import com.money.common.WebAdapter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@WebAdapter
@RequestMapping("/v1/wallets")
class WalletController(
    private val depositPort: DepositPort
) {

    @PostMapping("/{memberNo}/deposit")
    fun deposit(
        @PathVariable memberNo: Long,
        @RequestBody request: DepositRequest
    ) {
        val command = DepositCommand(Money.of(request.amount))
        depositPort.deposit(memberNo, command)
    }

}