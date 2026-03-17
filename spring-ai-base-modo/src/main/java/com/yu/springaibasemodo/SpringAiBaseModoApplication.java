package com.yu.springaibasemodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.yu")
public class SpringAiBaseModoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiBaseModoApplication.class, args);
	}

}
