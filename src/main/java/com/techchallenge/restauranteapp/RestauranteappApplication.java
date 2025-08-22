package com.techchallenge.restauranteapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.techchallenge.restauranteapp")

public class RestauranteappApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestauranteappApplication.class, args);
	}

}
