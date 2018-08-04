package com.mengyunzhi.coretest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mengyunzhi.*")
public class CoretestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoretestApplication.class, args);
	}
}
