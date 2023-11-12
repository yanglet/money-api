package com.money.global.configuration

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.jsontype.*
import com.fasterxml.jackson.datatype.jsr310.*
import org.springframework.cache.annotation.*
import org.springframework.context.annotation.*
import org.springframework.data.redis.cache.*
import org.springframework.data.redis.connection.*
import org.springframework.data.redis.serializer.*
import java.time.*

@Configuration
@EnableCaching
class CacheConfiguration : CachingConfigurer {

    @Bean
    fun customCacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
        val objectMapper = ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(
                JavaTimeModule()
            )
            .activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                    .allowIfBaseType(Any::class.java)
                    .build(), ObjectMapper.DefaultTyping.EVERYTHING
            )

        val config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofDays(1))
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer(objectMapper)
                )
            )

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(config)
            .build()
    }
}