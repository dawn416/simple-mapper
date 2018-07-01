/**
 * Copyright(C) 2018 Zhejiang Fline Technology Co., Ltd. All rights reserved.
 *
 */
package com.demo.simple_mapper.test;

import java.util.List;
import java.util.Map;

/**
 * @since 2018年6月29日 上午10:02:13
 * @version 1.0.0
 * @author
 *
 */
public interface AreaMapper {
	public List<Area> selectById(Map<String, Object> map);

	public int updateById(Map<String, Object> map);

}
