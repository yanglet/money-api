package com.money.application.domain.usecase

import com.money.application.port.`in`.ReadRemittancesUseCase
import com.money.application.port.out.LoadRemittancesPort
import com.money.common.UseCase
import org.springframework.cache.annotation.Cacheable
import org.springframework.transaction.annotation.Transactional

@UseCase
class ReadRemittancesService(
    private val loadRemittancesPort: LoadRemittancesPort
) : ReadRemittancesUseCase {

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = ["Remittance"], key = "#memberNo", cacheManager = "customCacheManager")
    override fun readRemittances(memberNo: Long) = loadRemittancesPort.readRemittances(memberNo)
}