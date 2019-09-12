package com.tedu.controller;

import com.jt.common.pojo.Product;
import com.jt.common.pojo.utils.vo.EasyUIResult;
import com.jt.common.pojo.utils.vo.SysResult;
import com.tedu.controller.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/manage")
public class ProductController {
    @Autowired
    private ProductService productService;
    //商品的分页查询
    @RequestMapping(value="pageManage",method= RequestMethod.GET)
    public EasyUIResult productPageQuery(Integer page,
                                         Integer rows){
        //控制层直接调用业务层方法获取返回结果
        EasyUIResult result=productService.
                pageQuery(page,rows);
        return result;
    }
    //商品的单个查询
    @RequestMapping("{haha}/{productId}")
    //在路径中如果某个阶段的字符串想要接收成为参数变量的值
    //需要在springmvc使用{变量名称}
    //url:/product/manage/1/2 haha=1 productId=2
    public Product queryById(@PathVariable String haha
            , @PathVariable String productId){
        //haha,productId 在请求url中以 ?key=value
        //post 请求体 key=value
        System.out.println("haha:"+haha);
        System.out.println("productId:"+productId);
        Product product=productService.queryById(productId);
        return product;
    }
    //商品的新增
    @RequestMapping("save")
    public SysResult saveProduct(Product product){
        //成功与失败
        try{
            productService.saveProduct(product);//缺少了id
            //表示成功 SysResult (200,"",null)
            return SysResult.ok();//{"status":200,"msg":"ok","data":null}
        }catch(Exception e){
            //e.message
            e.printStackTrace();
            //201,e.message,null
            return SysResult.build(201, e.getMessage(), null);
            //js data.msg data.status ,data.data
        }
    }
    //商品的更改
    @RequestMapping("update")
    public SysResult updateProduct(Product product){
        //包含了所有的商品字段属性值
        try{
            productService.updateProduct(product);
            return SysResult.ok();
        }catch(Exception e){
            e.printStackTrace();
            return SysResult.build(201, e.getMessage(), null);
        }
    }

}
