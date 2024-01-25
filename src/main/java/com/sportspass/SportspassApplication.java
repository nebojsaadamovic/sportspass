package com.sportspass;

import com.sportspass.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



@SpringBootApplication(scanBasePackages = { "com.sportspass" })
public class SportspassApplication  {

	@Autowired
	private JwtUtil jwtUtil;

	public static void main(String[] args) {
		SpringApplication.run(SportspassApplication.class, args);	}





}
