package com.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}
	
	//https://github.com/Dotridge/springcloudrepo
	

	@RestController
	class MyRestController{
		@RequestMapping("/say")
		public String sayHello(){
			
			System.out.println("hiiiiiiiiiiiiii");
			return "Hi This is spring boot application!";
			
		}
	}
	
}
