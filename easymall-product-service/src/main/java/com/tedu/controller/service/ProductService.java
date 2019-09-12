package com.tedu.controller.service;

import com.jt.common.pojo.Product;
import com.jt.common.pojo.utils.MapperUtil;
import com.jt.common.pojo.utils.vo.EasyUIResult;
import com.tedu.controller.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;
    public EasyUIResult pageQuery(Integer page, Integer rows) {
        //1,准备好一个返回的对象
        EasyUIResult result=new EasyUIResult();
        //2,封装total,查询商品总数
        int total=productMapper.queryTotal();
        //select count(product_id)
        result.setTotal(total);//js 计算分页数量
        // total%rows==0? total/rows:(total/rows+1)
        //3,封装另一个数据rows EasyUIResuolt属性
        int start=(page-1)*rows;
        List<Product> pList=productMapper.queryPage
                (start,rows);
        result.setRows(pList);
        return result;
    }
    /*@Autowired
    private JedisCluster cluster;*/
    private Jedis cluster =new Jedis("106.75.120.140",6379);
    public Product queryById(String productId) {
        //判断是否存在锁,如果有直接获取数据库,没有在执行后续逻辑
        String productUpdateLock
                ="product_update_"+productId
                +".lock";
        if(cluster.exists(productUpdateLock)){
            return productMapper.queryById(productId);
        }
        //引入缓存逻辑 redis jedisCluster
        //准备一个操作缓存的key值:业务逻辑+参数相关
        String productQueryKey="product_query_"+productId;
        //判断redis缓存中是否存在商品数据
        try{
            if(cluster.exists(productQueryKey)){
                //缓存命中,直接使用
                //不知道缓存的数据结构 String pJson
                String pJson=cluster.get(productQueryKey);
                return MapperUtil.MP.readValue(
                        pJson, Product.class);
            }else{//缓存未命中,数据库压力
                //读取数据库数据
                Product p=productMapper.queryById(productId);
                //将数据库读取的数据放到缓存一份
                String pJson=MapperUtil.MP.writeValueAsString(p);
                cluster.setex(productQueryKey, 60*60*24*2,
                        pJson);
                return p;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
    public void saveProduct(Product product) {
        //product 缺少productId补齐数据
        String productId= UUID.randomUUID().toString();
        product.setProductId(productId);
        //TODO 引入缓存的逻辑
        productMapper.saveProduct(product);
        //insert into t_product (表格字段) values
        //(#{各种参数属性值})
    }
    public void updateProduct(Product product) {
        //1 生成锁
        String productUpdateLock
                ="product_update_"+product.getProductId()
                +".lock";//应该存储
        cluster.set(productUpdateLock, "");
        //2 删除缓存
        String productQueryKey="product_query_"
                +product.getProductId();
        cluster.del(productQueryKey);
        //3 更新数据库
        productMapper.updateProduct(product);
        //4 释放锁
        cluster.del(productUpdateLock);
    }

}
