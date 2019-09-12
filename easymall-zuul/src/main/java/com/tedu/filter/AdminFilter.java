package com.tedu.filter;

import com.jt.common.pojo.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Component
public class AdminFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		
		return true;
	}
	@Autowired
	private RestTemplate restTemplate;
	@Override
	public Object run() {
		RequestContext context=RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		StringBuffer url=request.getRequestURL();
		System.out.println(url);
		
		//通过cookie判断是否登录
		try{
			String ticket = CookieUtils.getCookieValue(request, "EM_TICKET");
			/*if(false){
				if(StringUtils.isNotEmpty(ticket)){
					SysResult result=restTemplate.getForObject("http://userservice/user/manage/query/"+ticket, SysResult.class);
					String userJson=(String) result.getData();
					if(StringUtils.isNotEmpty(userJson)){
						MapperUtil.MP.readValue(userJson,User.class);
					}
				}
			}*/
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return url;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}
