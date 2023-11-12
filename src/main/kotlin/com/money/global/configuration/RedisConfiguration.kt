package com.money.global.configuration

import org.redisson.api.*
import org.redisson.spring.data.connection.*
import org.springframework.context.annotation.*

@Configuration
class RedisConfiguration {
    @Bean
    fun redisConnectionFactory(redissonClient: RedissonClient) = RedissonConnectionFactory(redissonClient)
}