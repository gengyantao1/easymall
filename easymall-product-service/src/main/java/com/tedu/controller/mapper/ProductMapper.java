package com.tedu.controller.mapper;

import com.jt.common.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface ProductMapper {
    int queryTotal();

    List<Product> queryPage(@Param("start")int start, @Param("rows")Integer rows);

    Product queryById(String productId);

    void saveProduct(Product product);

    void updateProduct(Product product);

}
