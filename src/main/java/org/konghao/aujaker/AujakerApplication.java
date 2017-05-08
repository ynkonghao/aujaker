package org.konghao.aujaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class AujakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AujakerApplication.class, args);
	}
}
