package com.tedu.search.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.List;

/**
 * 通过初始化的属性读取,
 * 生成框架维护的transportclient对象
 * @author tedu
 *
 */
@Configuration
@ConfigurationProperties(prefix="es")
public class ESConfig {
	private List<String> nodes;
	/*
	 * 初始化方法
	 */
	@Bean
	public TransportClient initTransport(){
		TransportClient client = 
		new PreBuiltTransportClient(Settings.EMPTY);
		//解析nodes {"ip:port","",""}
		try{
			for (String node : nodes) {
				//node="10.9.104.184:9300"
				String host=node.split(":")[0];
				int port=Integer.parseInt(node.split(":")[1]);
				InetAddress esA1 = 
					InetAddress.getByName(host);
				TransportAddress addresT1=
					new InetSocketTransportAddress(esA1, port);//扩展功能
				client.addTransportAddress(addresT1);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return client;
}
	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}
	
}
















