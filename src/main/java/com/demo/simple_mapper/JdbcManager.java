/**
 * Copyright(C) 2018 Zhejiang Fline Technology Co., Ltd. All rights reserved.
 *
 */
package com.demo.simple_mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.demo.simple_mapper.bean.Mapper;

/**
 * @since 2018年6月29日 上午10:30:20
 * @version 1.0.0
 * @author 
 *
 */
public class JdbcManager {

	private static Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/saizhi?useUnicode=true&characterEncoding=UTF8";
		String username = "root";
		String password = "123456";
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void main(String[] args) {
		JdbcManager jdbcManager = new JdbcManager();
		Mapper mapper = new Mapper();
		mapper.setStatement("select * from area");
		mapper.setResultType("com.demo.simple_mapper.test.Area");
		mapper.setType("select");
		jdbcManager.selectExecute(mapper);
	}

	public int updateExecute(Mapper mapper) {

		return 0;
	}

	/**
	 * 
	 */
	public List<Object> selectExecute(Mapper mapper) {
		List<Object> list = new ArrayList<>();
		try {
			Connection conn = getConn();
			PreparedStatement stmt = conn.prepareStatement(mapper.getStatement());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Class<?> forName = Class.forName(mapper.getResultType());
				Object newInstance = forName.newInstance();
				Field[] declaredFields = forName.getDeclaredFields();
				for (Field field : declaredFields) {
					Class<?> type = field.getType();
					String name = field.getName();
					if(String.class.equals(type)){
						String string = rs.getString(name);
						field.setAccessible(true);
						field.set(newInstance, string);
					}
					if(Integer.class.equals(type)){
						Integer string = rs.getInt(name);
						field.setAccessible(true);
						field.set(newInstance, string);
					}
				}
				list.add(newInstance);
			}
			DBManager.close(rs);
			DBManager.close(stmt);
			DBManager.close(conn);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.size() == 0) {
			list = null;
		}
		return list;
	}
}
