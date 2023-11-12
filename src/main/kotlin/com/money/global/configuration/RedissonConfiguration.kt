package com.money.global.configuration

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfiguration(
    @Value("\${redis.host}") private val host: String,
    @Value("\${redis.port}") private val port: String
) {
    companion object {
        const val REDISSON_HOST_PREFIX = "redis://"
    }

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer().address = "$REDISSON_HOST_PREFIX$host:$port"
        return Redisson.create(config)
    }
}