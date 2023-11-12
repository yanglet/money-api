package com.money.domain.wallet.controller

import com.money.domain.wallet.service.*
import com.money.domain.wallet.service.dto.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/wallets")
class WallerController(
    private val walletService: WalletService
) {
    @PostMapping("/{memberNo}/deposit")
    fun deposit(
        @PathVariable memberNo: Long,
        @RequestBody request: WalletDepositRequest
    ) = ResponseEntity.ok(
        walletService.deposit(memberNo, request)
    )
}