package com.tedu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
@MapperScan("com.tedu.seckill.mapper")
public class StartSeckillService {
	public static void main(String[] args) {
		SpringApplication.run(StartSeckillService.class, args);
	}
	//当前easymall系统使用的交换机队列和绑定关系
	//都在启动类的配置类中实现,队列和交换机的使用
	//springboot的整合配置中是以对象存在
	//声明一个队列 seckillQ
	//org.springframework.amqp.core
	@Bean
	public Queue declareQueue(){
		return new Queue("seckillQ", true, false, false, null);
	}
	//声明交换机
	@Bean
	public DirectExchange exDeclare(){
		return new DirectExchange("seckillEx");
	}
	//声明的只是内存对象,在连接使用rabbitmq之前不会调用
	//懒加载
	//需要绑定队列与交换机的关系
	@Bean
	public Binding bind(){
		return BindingBuilder.
				bind(declareQueue()).to(exDeclare())
				.with("seckill");
		//channel.queueBind(exname,queuename,routingkey)
	}
	
	@Autowired
	private RabbitTemplate client;
	@RequestMapping("send")
	public String sendMsg(String msg){
		//直接调用方法
		client.convertAndSend
		("seckillEx", "seckill", "你好啊,seckill秒杀系统");
		return "success";
	}
}

















