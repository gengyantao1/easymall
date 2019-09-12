package com.tedu.seckill.mapper;

import com.jt.common.pojo.Seckill;
import com.jt.common.pojo.Success;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SecMapper {

	List<Seckill> queryAll();

	Seckill queryOne(Integer seckillId);
	//#{seckillId} user product cart
	int updateNumber(@Param("seckillId") Long seckillId,
                     @Param("nowTime") Date nowTime);

	void saveSuccess(Success suc);

	List<Success> querySuccess(Long seckillId);

}
