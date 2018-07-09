package com.demo.simple_mapper;

import com.demo.simple_mapper.annotation.Param;
import com.demo.simple_mapper.annotation.Select;

public interface UserMapper {
	@Select("select account from user where id = #{id}")
	public User selectById(@Param("id") int id);

}
