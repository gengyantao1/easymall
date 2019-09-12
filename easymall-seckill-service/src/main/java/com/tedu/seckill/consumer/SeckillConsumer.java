package com.tedu.seckill.consumer;

import com.jt.common.pojo.Success;
import com.tedu.seckill.mapper.SecMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.Date;

@Component
public class SeckillConsumer {
	@Autowired
	private SecMapper secMapper;
	//实现消费逻辑的方法
	//参数就是底层连接传递过来的消息body
	//通过springboot整合配置的封装功能 
	@Autowired
	private JedisCluster cluster;
	@RabbitListener(queues="seckillQ") //可以在扫描范围内实现自动监听
	//队列实现消息的消费
	
	public void consume(String msg){
		System.out.println("seckill系统的消费端接收到了:"+msg);
		//解析消息,userPhone seckillId,利用seckillId减库存
		//update set seckill number=number-1 where
		//seckillId=参数,and starterTime<new Date new Date<endTime
		//and number >0 
		Long userPhone=Long.parseLong(msg.split("/")[0]);
		Long seckillId=Long.parseLong(msg.split("/")[1]);
		Date nowTime=new Date();
		//通过redis的逻辑,实现系统对访问数据库的权限做判断
		//避免线程安全问题 可以引入redis的list数据
		String rpop = cluster.rpop("seckillList");
		//判断rpop是否为空
		if(rpop==null){
			//秒杀完了,前面的队列请求已经秒杀完毕当前商品的所有内容
			System.out.println("当前用户秒杀失败"+userPhone);
			return;
		}
		//执行减库存
		int result=secMapper.updateNumber(seckillId,nowTime);
		if(result==0){
			//没有成功,
			System.out.println("当前用户秒杀失败"+userPhone);
			return;
		}
		//执行入库 success
		Success suc=new Success();//insert操作
		suc.setCreateTime(nowTime);
		suc.setSeckillId(seckillId);
		suc.setState(0);
		suc.setUserPhone(userPhone);
			secMapper.saveSuccess(suc);
	}
}













