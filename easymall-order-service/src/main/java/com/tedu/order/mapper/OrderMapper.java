package com.tedu.order.mapper;

import com.jt.common.pojo.Order;

import java.util.List;

public interface OrderMapper {

	List<Order> queryOrders(String userId);

	void addOrder(Order order);

	void deleteOrder(String orderId);

}
