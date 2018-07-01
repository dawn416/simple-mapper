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

import com.demo.simple_mapper.bean.Mapper;

/**
 * @since 2018年6月29日 上午10:30:20
 * @version 1.0.0
 * @author
 *
 */
public class JdbcManager {
	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
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

	/**
	 * 准备执行sql
	 * 
	 * @param mapper
	 * @param paramMap
	 * @throws SQLException
	 */
	public Object sqlPrepare(Mapper mapper, Map<String, Object> paramMap) throws SQLException {
		Connection conn = getConn();
		String statement = mapper.getStatement();
		Map<Integer, Object> sqlAttrMap = new HashMap<>();

		int i = 1;
		while (statement.indexOf("#{") >= 0) {
			String substring = statement.substring(statement.indexOf("#{") + 2, statement.indexOf("}"));
			statement = statement.replace("#{" + substring + "}", "?");
			Object object = paramMap.get(substring);
			sqlAttrMap.put(i, object);
			i++;
		}
		PreparedStatement stmt = conn.prepareStatement(statement);
		for (Entry<Integer, Object> entry : sqlAttrMap.entrySet()) {
			stmt.setObject(entry.getKey(), entry.getValue());
		}

		if ("select".equals(mapper.getType())) {
			try {
				List selectExecute = selectExecute(mapper, stmt);

				if (MyHandler.isMany.get()) {
					return selectExecute;
				} else {
					Object object = selectExecute.get(0);
					return object;
				}
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if ("update".equals(mapper.getType())) {
			return updateExecute(stmt);
		}
		DBManager.close(stmt);
		DBManager.close(conn);
		return null;
	}

	/**
	 * 更新语句执行
	 * 
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	public int updateExecute(PreparedStatement stmt) throws SQLException {
		int executeUpdate = stmt.executeUpdate();
		return executeUpdate;
	}

	/**
	 * 查询语句执行
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * 
	 */
	public List selectExecute(Mapper mapper, PreparedStatement stmt)
			throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		List list = new ArrayList();
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
		if (list.isEmpty()) {
			list = null;
		}
		return list;
	}
}
