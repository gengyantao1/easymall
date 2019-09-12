package com.tedu.search.controller;

import com.jt.common.pojo.Product;
import com.tedu.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {
	@Autowired
	private SearchService searchService;
	//创建索引
	@RequestMapping("createIndex")
	public String createIndex(){
		searchService.createIndex();
		return "success";
	}
	
	//搜索功能,根据页面传递的参数,封装分页结果
	@RequestMapping("/search/manage/query")
	public List<Product> queryProduct(Integer page,
                                      Integer rows, String query){
		return searchService.query(query,page,rows);
	}
	
	
	
	
	
	
	
	
	
}
