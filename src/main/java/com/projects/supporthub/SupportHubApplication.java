package com.projects.supporthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SupportHubApplication 
{
	public static void main(String[] args) {
		SpringApplication.run(SupportHubApplication.class, args);
	}
}