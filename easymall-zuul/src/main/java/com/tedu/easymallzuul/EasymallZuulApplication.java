package com.tedu.easymallzuul;

import com.netflix.zuul.ZuulFilter;
import com.tedu.filter.AdminFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication

public class EasymallZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasymallZuulApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate initZuulRestTemplate(){
        return new RestTemplate();
    }
    @Bean
    @ConditionalOnMissingBean(AdminFilter.class)
    public ZuulFilter admin(){
        return new AdminFilter();
    }
}

