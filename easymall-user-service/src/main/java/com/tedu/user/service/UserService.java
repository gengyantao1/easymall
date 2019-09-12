
package com.tedu.user.service;

import java.util.UUID;

import com.jt.common.pojo.utils.MD5Util;
import com.jt.common.pojo.utils.MapperUtil;
import com.tedu.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.pojo.User;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class UserService {
	/*private static final String userSaveSuffix="user_save";
	private static final String productUpdate="product_update";
	*/
	@Autowired
	private UserMapper userMapper;

	public int checkUsername(String userName) {
		//select count(user_id)
		return userMapper.checkUsername(userName);
	}
	public void saveUser(User user) {
		//加密password
		String md5Password=
		MD5Util.md5(user.getUserPassword());
		user.setUserPassword(md5Password);
		//登录比对校验数据的userPassword也加密比对
		//补充userId
		user.setUserId(UUID.randomUUID().toString());
		userMapper.saveUser(user);
	}
//	@Autowired
//	private ShardedJedisPool pool;
	@Autowired
	private JedisCluster jedis;
	//private Jedis jedis=new Jedis("106.75.120.140",6379);
	public String login(User user) {
		
		//验证登录信息
		user.setUserPassword(MD5Util.md5(user.getUserPassword()));
		User exist=userMapper.queryExist(user);
		if(exist==null){
			//登录信息不对
			return "";
		}else{//登录成功
			//准备2个key值,userLoginLock ticket
			String userLoginLock=
				"user_login_"+exist.getUserId()+".lock";
			String ticket=
				"EM_TICKET"+System.currentTimeMillis()+exist.getUserId();
			//判断
			if(jedis.exists(userLoginLock)){
				//说明曾经有人登录过,而且没超时
				String oldTicket=jedis.get(userLoginLock);
				jedis.del(oldTicket);
			}//不存在顶替中的lock,说明是第一次登录
			jedis.setex(userLoginLock, 60*3, ticket);//超时应该
			//ticket userJson一致,并且开始存储这对key-value
			try{
				String userJson= MapperUtil.MP.writeValueAsString(exist);
				jedis.setex(ticket, 60*3, userJson);
				return ticket;
			}catch(Exception e){
				e.printStackTrace();
				return "";
			}
		}
	}
	public String queryUserJson(String ticket) {
		try{
			//判断超时时间剩余值
			Long leftTime=jedis.pttl(ticket);
			String userJson=jedis.get(ticket);
			if(userJson!=null){
				User user=MapperUtil.MP.
						readValue(userJson, User.class);
				Long leaseTime=1000*60*2l;
				if(leftTime<=leaseTime){//达到续租的条件
					//将剩余时间加上5分钟,做续租
					leftTime=leftTime+1000*60*5l;
					//做expire 对ticket和userLoginLock
					jedis.pexpire(ticket, leftTime);
					jedis.pexpire("user_login_"
					+user.getUserId()+".lock", leftTime);
				}
			}else{
				return "";
			}
			return userJson;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
















