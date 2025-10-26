package com.cts.gatedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatedemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatedemoApplication.class, args);
	}

}
