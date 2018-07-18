package com.demo.simple_mapper;

import java.util.List;

import com.demo.simple_mapper.annotation.Param;
import com.demo.simple_mapper.annotation.Select;

public interface UserMapper {
	@Select("select account from user where id = #{id}")
	public List<User> selectByUserId(@Param("id") int id);

}
