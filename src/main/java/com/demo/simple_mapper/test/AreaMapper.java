package com.demo.simple_mapper.test;

import java.util.List;
import java.util.Map;

import com.demo.simple_mapper.annotation.Select;

/**
 * @since 2018年6月29日 上午10:02:13
 * @version 1.0.0
 * @author
 *
 */
public interface AreaMapper {
	@Select("select * from area where id = #{id}")
	public Area selectById(Map<String, Object> map);

	public List<Area> select();

	public int updateById(Map<String, Object> map);

}
