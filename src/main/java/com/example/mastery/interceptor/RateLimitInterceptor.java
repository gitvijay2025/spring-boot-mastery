package com.example.mastery.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@AllArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String userId = request.getHeader("X-User-Id");
        if (userId == null || userId.isBlank()) {
            userId = request.getRemoteAddr();
        }

        if (!rateLimiterService.isAllowed(userId)) {
            response.setStatus(429);
           // response.setContentType("text/plain");
          //  response.getWriter().write("Rate limit exceeded");
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Rate limit exceeded, please try again later\"}");
            return false;
        }

        return true;
    }
}
