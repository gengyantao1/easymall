package com.tedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tedu.user.mapper")
public class StarterUserEasymall {
	public static void main(String[] args) {
		SpringApplication.run(StarterUserEasymall.class, args);
	}
}
