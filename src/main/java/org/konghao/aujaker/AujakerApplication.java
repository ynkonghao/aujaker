package org.konghao.aujaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AujakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AujakerApplication.class, args);
	}
}
