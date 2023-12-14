package com.money.adapter.`in`.web

import com.money.adapter.`in`.web.dto.ReadRemittancesResponse
import com.money.adapter.`in`.web.dto.RemitRequest
import com.money.application.domain.model.Money
import com.money.application.port.`in`.ReadRemittancesUseCase
import com.money.application.port.`in`.RemitUseCase
import com.money.application.port.`in`.dto.RemitCommand
import com.money.common.WebAdapter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@WebAdapter
@RequestMapping("/v1/remittances")
class RemittanceController(
    private val remitUseCase: RemitUseCase,
    private val readRemittancesUseCase: ReadRemittancesUseCase
) {

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    fun remit(@RequestBody request: RemitRequest) {
        val command = RemitCommand(request.to, request.from, Money.of(request.amount))
        remitUseCase.remit(command)
    }

    @GetMapping("/{memberNo}")
    fun readRemittances(@PathVariable memberNo: Long): ResponseEntity<List<ReadRemittancesResponse>> {
        val result = readRemittancesUseCase.readRemittances(memberNo)

        return ResponseEntity.ok(
            result.map { ReadRemittancesResponse.from(it) }
        )
    }
}