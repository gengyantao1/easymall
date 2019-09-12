package com.tedu.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jt.common.pojo.utils.CookieUtils;
import com.jt.common.pojo.utils.vo.SysResult;
import com.tedu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jt.common.pojo.User;


@RestController
@RequestMapping("user/manage")
public class UserController {
	@Autowired
	private UserService userService;
	//校验用户名是否存在
	@RequestMapping("checkUserName")
	public SysResult checkUsername(String userName){
		//控制层判断反回数据
		int exist=userService.checkUsername(userName);
		if(exist==0){//不存在,可用
			return SysResult.ok();//status=200
		}else{//存在,不可用
			return SysResult.build(201, "", null);
		}
	}
	//注册表单提交
	@RequestMapping("save")
	public SysResult saveUser(User user){
		//判断成功失败结构
		try{
			userService.saveUser(user);
			return SysResult.ok();
		}catch(Exception e){
			e.printStackTrace();
			return SysResult.build(201, "", null);
		}
	}
	//根据接口文件实现登录的校验,通过正确登录的
	//用户存储在redis中
	@RequestMapping("login")
	public SysResult login(User user,
			HttpServletRequest req,
			HttpServletResponse res){
		//调用业务层 获取redis中存储的key值 ticket
		String ticket=userService.login(user);
		//判断失败和成功
		if("".equals(ticket)){//没有正确生成redis的
		//key 说明登录失败的
			return SysResult.build(201, "", null);
		}else{
		//正确存储了数据到redis并且登录时成功的
			//cookie存放一个值, EM_TICKET
			//CookieUtils
			CookieUtils.setCookie(req, res,
					"EM_TICKET", ticket);
			return SysResult.ok();
		}
	}
	
	//用户登录状态获取
	@RequestMapping("query/{ticket}")
	public SysResult queryUserJson(@PathVariable
			String ticket){
		String userJson=
			userService.queryUserJson(ticket);
		//如果redis数据不存在,返回时null还是""
		if(userJson==null){
			//登录状态不存在,不是ticket时错的,就是超时了
			return SysResult.build(201, "", null);
		}else{
			//登录状态合法
			//function(data){
			//  data.status 判断200还是非200
			//  data.msg 获取交流信息
			//  data.data==userJson
			//  data.data.userName==eeee}
			return SysResult.build(200, "ok", userJson);
		}
	}
}













