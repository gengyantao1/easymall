package com.tedu.user.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

//@Component
public class RedisUtils {
	@Autowired(required=false)
	private ShardedJedisPool pool;
	//easymall set setnx,expire,exists,get ,hmset hmget
	
	public void saveAndUpdate(String key,String value){
		ShardedJedis sJedis=pool.getResource();
		try{
			sJedis.set(key, value);
		}catch(Exception e){
			//TODO 日志
		}finally{
			pool.returnResource(sJedis);
		}
	}
}







