package com.tedu.order.controller;

import com.jt.common.pojo.Order;
import com.jt.common.pojo.utils.vo.SysResult;
import com.tedu.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("order/manage")
public class OrderController {
	@Autowired
	private OrderService orderService;
	//根据userId查询我的所有订单数据
	@RequestMapping("query/{userId}")
	public List<Order> queryMyOrders(@PathVariable
			String userId){
		return orderService.queryMyOrders(userId);
	}
	//订单数据新增
	@RequestMapping("save")
	public SysResult saveOrder(Order order){
		try{
			orderService.saveOrder(order);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
	@RequestMapping("delete/{orderId}")
	public SysResult deleteOrder(@PathVariable
			String orderId){
		try{
			orderService.deleteOrder(orderId);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
}


































