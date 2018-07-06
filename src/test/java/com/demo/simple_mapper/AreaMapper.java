package com.demo.simple_mapper;

import java.util.List;

import com.demo.simple_mapper.annotation.Delete;
import com.demo.simple_mapper.annotation.Insert;
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
	public Area selectById(@Param("id") int id);

	@Select("select * from area")
	public List<Area> select();

	@Update("update area set name = #{name} where id = #{id}")
	public int updateById(@Param("name") String name, @Param("id") int id);

	@Delete("delete from area where id = #{id}")
	public int deleteById(@Param("id") int id);

	@Insert("insert into area (name) values (#{name})")
	public int insert(@Param("name") String name);

}
