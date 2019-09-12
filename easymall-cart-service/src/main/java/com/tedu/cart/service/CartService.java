package com.tedu.cart.service;

import com.jt.common.pojo.Cart;
import com.jt.common.pojo.Product;
import com.tedu.cart.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CartService {
	@Autowired
	private CartMapper cartMapper;
	public List<Cart> queryMyCart(String userId) {
		return cartMapper.queryMyCart(userId);
	}
	@Autowired
	private RestTemplate client;
	public void saveCart(Cart cart) {
		/*接收参数后需要判断当前购物车数据是否是新增还是更新
		 *1,userId productId 查询已存在的cart
		 *2,判断已存在的cart是否为空
		 *	2.1 为空说明数据不存在 insert
		 *	2.2 非空说明购物车数据已经存在,将旧num和新增num
		 *		叠加,更新数据库的num数量
		 *
		 *
		 *
		 */
		Cart exist=cartMapper.queryExist(cart);//userId productId
		if(exist==null){
			//TODO 新增,需要补齐 ribbon的负载均衡访问
			//需要定义接口访问文件 productservice已经存在一个
			//根据id查询商品信息的方法 /product/manage/item/{productId}
			//根据接口文件准备一个请求地址
			String url="http://productservice/product"
					+ "/manage/item/"+cart.getProductId();
			Product p=client.getForObject(url, Product.class);
			//通过p补充cart对象
			cart.setProductName(p.getProductName());
			cart.setProductImage(p.getProductImgurl());
			cart.setProductPrice(p.getProductPrice());
			//调用insert方法
			cartMapper.saveCart(cart);
		}else{
			//非空,将exist的num和cart 的num做叠加
			cart.setNum(cart.getNum()+exist.getNum());
			cartMapper.updateNum(cart);
			//update t_cart set num=#{num} 
			//where user_id and product_id
		}
	}
	public void updateNum(Cart cart) {
		cartMapper.updateNum(cart);
	}
	public void deleteCart(Cart cart) {
		cartMapper.deleteCart(cart);
		
	}

}
