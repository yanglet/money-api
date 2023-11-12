package com.money.domain.remittance.controller

import com.money.domain.remittance.service.*
import com.money.domain.remittance.service.dto.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/remittances")
class RemittanceController(
    private val remittanceService: RemittanceService
) {
    @PostMapping("/save")
    fun remit(
        @RequestBody request: RemittanceSaveRequest
    ) = ResponseEntity.ok(
        remittanceService.remit(request)
    )

    @GetMapping("/{memberNo}")
    fun readRemittancesByMemberNo(
        @PathVariable memberNo: Long
    ) = ResponseEntity.ok(
        remittanceService.readRemittancesByMemberNo(memberNo)
    )
}