package com.inorg.rewardAndRecognition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@EntityScan(basePackages = "com.inorg.rewardAndRecognition.userPortal.entity")
public class RewardAndRecognitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewardAndRecognitionApplication.class, args);
		System.out.println("hello world");
	}


}
