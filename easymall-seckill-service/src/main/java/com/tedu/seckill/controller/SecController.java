package com.tedu.seckill.controller;

import com.jt.common.pojo.Seckill;
import com.jt.common.pojo.Success;
import com.jt.common.pojo.utils.vo.SysResult;
import com.tedu.seckill.mapper.SecMapper;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("seckill/manage")
public class SecController {
	@Autowired
	private SecMapper secMapper;
	//查询所有商品list
	@RequestMapping("list")
	public List<Seckill> queryAll(){
		return secMapper.queryAll();
	}
	//查询单个数据
	@RequestMapping("detail")
	public Seckill queryOne(Integer seckillId){
		return secMapper.queryOne(seckillId);
	}
	@Autowired
	private RabbitTemplate client;
	//生产端发送消息逻辑
	@RequestMapping("{seckillId}")
	public SysResult seckillStart(@PathVariable Integer seckillId)
	{
		//构造消息，在描述清楚的情况下,尽可能的精简消息
		//用户身份,秒杀商品
		Long  userPhone=RandomUtils.nextLong();	
		String msg=userPhone+"/"+seckillId;
		//发送消息
		client.convertAndSend("seckillEx", "seckill", msg);
		return SysResult.ok();
	}
	
	//展示成功信息给前端展示
	@RequestMapping("{seckillId}/userPhone")
	public List<Success> querySuccess(@PathVariable
			Long seckillId){
		return secMapper.querySuccess(seckillId);
	}
	
}











