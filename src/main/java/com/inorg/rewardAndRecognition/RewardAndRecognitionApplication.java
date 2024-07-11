package com.inorg.rewardAndRecognition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
public class RewardAndRecognitionApplication {

	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
		return Collections.singletonMap("name", principal.getAttribute("name"));
	}

	public static void main(String[] args) {
		SpringApplication.run(RewardAndRecognitionApplication.class, args);
		System.out.println("hello world");
	}
}
