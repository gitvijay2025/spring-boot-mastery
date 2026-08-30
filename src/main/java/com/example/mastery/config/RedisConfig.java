package com.example.mastery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;


import java.time.Duration;

@Configuration
public class RedisConfig {

    private static final BasicPolymorphicTypeValidator CACHE_TYPE_VALIDATOR =
            BasicPolymorphicTypeValidator.builder()
                    .allowIfSubType("com.example.mastery.dto.")
                    .build();

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {


        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))   // TTL — 10 min baad automatically expire
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(GenericJacksonJsonRedisSerializer.builder()
                                        .enableDefaultTyping(CACHE_TYPE_VALIDATOR)
                                        .build())
                );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }






}
