package com.tedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.tedu.cart.mapper")
public class StarterCartService {
	public static void main(String[] args) {
		SpringApplication.run(StarterCartService.class, args);
	}
	//ribbon实现负载均衡访问后端服务提供者
	
	//新增商品到购物车系统,需要购物车访问商品的查询功能
	//服务调用服务
	@Bean
	@LoadBalanced
	public RestTemplate initRestTemplate(){
		return new RestTemplate();
	}
}






