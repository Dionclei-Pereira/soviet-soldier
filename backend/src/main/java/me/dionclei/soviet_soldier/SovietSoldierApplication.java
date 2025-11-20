package me.dionclei.soviet_soldier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class SovietSoldierApplication {

	public static void main(String[] args) {
		SpringApplication.run(SovietSoldierApplication.class, args);
	}

}
