package com.money.domain.common.service

import com.money.global.*
import org.redisson.api.*
import org.springframework.stereotype.*
import org.springframework.transaction.annotation.*
import java.util.concurrent.*

@Service
class DistributedLockService(
    private val redissonClient: RedissonClient
) : Log {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> doDistributedLock(lockName: String, function: () -> T) {
        val lock = redissonClient.getLock(lockName)

        try {
            val available = lock.tryLock(5, 3, TimeUnit.SECONDS)
            if (available) {
                function()
            }
        } catch (e: InterruptedException) {
            log.error("InterruptedException 발생!", e)
            throw RuntimeException("InterruptedException")
        } finally {
            lock.unlock()
        }
    }
}