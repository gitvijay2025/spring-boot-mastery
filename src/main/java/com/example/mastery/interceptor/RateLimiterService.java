package com.example.mastery.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    private static final int MAX_REQUESTS =5 ;
    private static final Duration WINDOW = Duration.ofSeconds(10);

    public boolean isAllowed(String userId) {

        String key = "rate-limit:user:" + userId;
        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (Long.valueOf(1).equals(currentCount)) {
            redisTemplate.expire(key, WINDOW);
        }
        return currentCount != null && currentCount <= MAX_REQUESTS;
    }


}
