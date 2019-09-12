package com.tedu.search.service;

import com.jt.common.pojo.Product;
import com.jt.common.pojo.utils.MapperUtil;
import com.tedu.search.mapper.SearchMapper;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
	@Autowired
	private SearchMapper searchMapper;
	@Autowired
	private TransportClient client;
	public void createIndex() {
		//准备好一个es中的索引文件
		//先判断空索引/没索引 索引不存在
		IndicesAdminClient admin = client.admin().indices();
		IndicesExistsRequestBuilder request = 
				admin.prepareExists("emprod");
		IndicesExistsResponse response = request.get();
		if(!response.isExists()){
			//说明不存在
			admin.prepareCreate("emprod").get();
		}
		//向索引中添加数据
		List<Product> pList=searchMapper.queryAll();
		for (Product p : pList) {
			try{
				//转化成json
				String pJson= MapperUtil
					.MP.writeValueAsString(p);
				IndexRequestBuilder request1 = client.prepareIndex("emprod", 
						"product", p.getProductId());
				//json放到request1的请求参数中
				request1.setSource(pJson).get();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public List<Product> query(String keyWord, Integer page, Integer rows) {
		/* 1 构建一个查询对象
		 * 2 获取分页的查询结果
		 * 3 hits 中source是数据 pJson
		 * 4 转化成product对象
		 * 5 封装list返回
		 */
		List<Product> pList=new ArrayList<Product>();
		MatchQueryBuilder query 
		= QueryBuilders.matchQuery("productName", keyWord);
		SearchRequestBuilder request = client.prepareSearch("emprod")
		.setQuery(query)
		.setFrom((page-1)*rows).setSize(rows);
		//发送请求获取响应数据
		SearchResponse response = request.get();
		SearchHits topHits = response.getHits();
		//封装总数 topHits.totalHit() rows
		SearchHit[] hits = topHits.hits();
		for (SearchHit hit : hits) {
			//从hit中获取source数据 json
			try{//获取json
				String pJson = hit.getSourceAsString();
				Product product = MapperUtil
						.MP.readValue(pJson, 
								Product.class);
				pList.add(product);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return pList;
	}

	
	
	
	
	
	
	
	
	
}
