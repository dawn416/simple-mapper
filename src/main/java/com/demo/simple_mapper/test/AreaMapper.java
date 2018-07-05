package com.demo.simple_mapper.test;

import java.util.Map;

import com.demo.simple_mapper.annotation.Param;
import com.demo.simple_mapper.annotation.Select;
import com.demo.simple_mapper.annotation.Update;

/**
 * @since 2018年6月29日 上午10:02:13
 * @version 1.0.0
 * @author
 *
 */
public interface AreaMapper {
	@Select("select * from area where id = #{id}")
	public Area selectById(Map<String, Object> map);

	@Select("select * from area")
	public Area select();

	@Update("update area set name = #{name} where id = #{id}")
	public int updateById(@Param("name") String name, @Param("id") int id);

}
