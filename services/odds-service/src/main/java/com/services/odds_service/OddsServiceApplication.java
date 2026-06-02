package com.services.odds_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OddsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OddsServiceApplication.class, args);
	}

}
