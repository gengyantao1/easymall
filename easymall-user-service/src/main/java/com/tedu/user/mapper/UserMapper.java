package com.tedu.user.mapper;

import com.jt.common.pojo.User;

public interface UserMapper {

	int checkUsername(String userName);

	void saveUser(User user);

	User queryExist(User user);

}
