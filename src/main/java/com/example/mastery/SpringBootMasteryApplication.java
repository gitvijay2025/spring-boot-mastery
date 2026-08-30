package com.example.mastery;

import jakarta.persistence.Cacheable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching   // ✅ Redis/Cache annotations (@Cacheable, @CacheEvict) ACTIVATE karta hai
@EnableAsync     // ✅ @Async annotation ACTIVATE karta hai (background thread execution)
@SpringBootApplication
public class SpringBootMasteryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMasteryApplication.class, args);
	}

}
