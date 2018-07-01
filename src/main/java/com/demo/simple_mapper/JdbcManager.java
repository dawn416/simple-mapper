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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

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

	@Test
	public void test1() {
		JdbcManager jdbcManager = new JdbcManager();
		Mapper mapper = new Mapper();
		mapper.setStatement("select * from area where id = #{id}");
		mapper.setResultType("com.demo.simple_mapper.test.Area");
		mapper.setType("select");
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1);
		jdbcManager.selectExecute(mapper, map);
	}

	@Test
	public void test(String statement, Map map, Map<String, Object> paramMap) {
		int i = 1;
		while (statement.indexOf("#{") >= 0) {
			String substring = statement.substring(statement.indexOf("#{") + 2, statement.indexOf("}"));
			statement = statement.replace("#{" + substring + "}", "?");
			Object object = paramMap.get(substring);
			map.put(i, object);
		}

	}

	public int updateExecute(Mapper mapper) {

		return 0;
	}

	/**
	 * 
	 */
	public Object selectExecute(Mapper mapper, Map<String, Object> map2) {
		List<Object> list = new ArrayList<>();

		Map<String, Object> paramMap = map2;
		try {
			Connection conn = getConn();
			String statement = mapper.getStatement();
			Map<Integer, Object> map = new HashMap<>();
			int i = 1;
			// test(statement, map, paramMap);
			// System.out.println(statement);
			while (statement.indexOf("#{") >= 0) {
				String substring = statement.substring(statement.indexOf("#{") + 2, statement.indexOf("}"));
				statement = statement.replace("#{" + substring + "}", "?");
				Object object = paramMap.get(substring);
				map.put(i, object);
				i++;
			}
			System.out.println(statement);
			PreparedStatement stmt = conn.prepareStatement(statement);
			System.out.println(map.size());
			for (Entry<Integer, Object> entry : map.entrySet()) {
				stmt.setObject(entry.getKey(), entry.getValue());
				System.out.println("param setting");
			}
			if ("select".equals(mapper.getType())) {
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					Class<?> forName = Class.forName(mapper.getResultType());
					Object newInstance = forName.newInstance();
					Field[] declaredFields = forName.getDeclaredFields();
					for (Field field : declaredFields) {
						Class<?> type = field.getType();
						String name = field.getName();
						if (String.class.equals(type)) {
							String string = rs.getString(name);
							field.setAccessible(true);
							field.set(newInstance, string);
						}
						if (Integer.class.equals(type)) {
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
			} else if ("update".equals(mapper.getType())) {
				System.out.println("update");
				int executeUpdate = stmt.executeUpdate();
				System.out.println("result is " + executeUpdate);
				DBManager.close(stmt);
				DBManager.close(conn);
				return executeUpdate;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.size() == 0) {
			list = null;
		}
		return list;
	}
}
