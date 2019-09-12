package com.tedu.cart.controller;


import com.jt.common.pojo.Cart;
import com.jt.common.pojo.utils.vo.SysResult;
import com.tedu.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cart/manage")
public class CartController {
	@Autowired
	private CartService cartService;
	//根据userId查询我的购物车商品数据
	@RequestMapping("query")
	public List<Cart> queryMyCart(String userId){
		return cartService.queryMyCart(userId);
	}
	//购物车数据新增
	@RequestMapping("save")
	public SysResult saveCart(Cart cart){
		try{//cart name price image
			//userId productId num传递的
			//调用业务层代码实现insert封装
			cartService.saveCart(cart);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
	//更新num
	@RequestMapping("")
	public SysResult updateNum(Cart cart){
		try{
			cartService.updateNum(cart);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
	//删除购物车商品
	@RequestMapping("delete")
	public SysResult deleteCart(Cart cart){
		try{
			//userId productId
			cartService.deleteCart(cart);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
}










