/**
 * Copyright(C) 2018 Zhejiang Fline Technology Co., Ltd. All rights reserved.
 *
 */
package com.demo.simple_mapper;

import java.util.List;

/**
 * @since 2018年7月12日 上午8:43:38
 * @version 1.0.0
 * @author 黎明
 *
 */
public class InterfacePojo implements AreaMapper, UserMapper {

	@Override
	public Area selectById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Area> select() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateById(String name, int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> selectByUserId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
