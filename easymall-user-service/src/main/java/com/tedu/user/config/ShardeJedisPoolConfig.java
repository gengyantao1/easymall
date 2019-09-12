package com.tedu.user.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

@Configuration
@ConfigurationProperties(prefix="easymall.redis")
//@ConditionalOnClass({Jedis.class,ShardedJedisPool.class})
public class ShardeJedisPoolConfig {
	private List<String> nodes;//{"10.9.39.13:6379","",""}
	private Integer maxTotal;
	private Integer maxIdle;
	private Integer minIdle;
//	利用读取的属性nodes 封装List<JedisShardInfo>
//	 *利用其它属性 封装连接池的配置对象
//	 *需要初始化方法

	@Bean
	public ShardedJedisPool initShardedJedisPool(){
		//收集节点信息 nodes{"10.9.39.13:6379","",""}
		List<JedisShardInfo> info=
		new ArrayList<JedisShardInfo>();
		//for循环nodes 增强For循环
		for (String node : nodes) {
			//node="10.9.39.13|6379"
			String hostIp=node.split(":")[0];
			int port=Integer.parseInt(node.split(":")[1]);
			info.add(new JedisShardInfo(hostIp,port));
		}
		//准备构造的连接池对象需要配置对象config
		GenericObjectPoolConfig config=
			new GenericObjectPoolConfig();
		config.setMaxIdle(maxIdle);
		config.setMaxTotal(maxTotal);
		config.setMinIdle(minIdle);
		//构造连接池对象
		ShardedJedisPool pool=
				new ShardedJedisPool(config,info);
		return pool;
	}
	public List<String> getNodes() {
		return nodes;
	}
	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	
}
