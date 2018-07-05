package com.demo.simple_mapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.demo.simple_mapper.bean.Mapper;
import com.demo.simple_mapper.bean.SqlInfo;

/**
 * @since 2018年6月29日 上午10:30:20
 * @version 1.0.0
 * @author
 *
 */
public class JdbcManager {

	/**
	 *
	 */
	public static final String UPDATE = "update";
	/**
	 *
	 */
	public static final String SELECT = "select";

	/**
	 * 准备执行sql
	 * 
	 * @param mapper
	 * @param paramMap
	 * @throws SQLException
	 */
	public Object sqlPrepare(Mapper mapper, Map<String, Object> paramMap) throws SQLException {
		Connection conn = DBManager.getConn();
		String statement = mapper.getStatement();
		SqlInfo sqlParsing = sqlParsing(paramMap, statement);
		PreparedStatement stmt = conn.prepareStatement(sqlParsing.getSqlParsed());
		try {
			List<Object> parameterList = sqlParsing.getParameterList();
			for (int i = 0, j = 1; i < parameterList.size(); i++, j++) {
				stmt.setObject(j, parameterList.get(i));
			}
			if (SELECT.equals(mapper.getType())) {
				try {
					List selectExecute = selectExecute(mapper, stmt);
					if (mapper.isMany()) {
						return selectExecute;
					}
					if (selectExecute.size() == 1) {
						return selectExecute.get(0);
					}
					if (selectExecute.size() == 0) {
						return null;
					}
					throw new RuntimeException("返回实例数超过1个");
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} else if (UPDATE.equals(mapper.getType())) {
				return updateExecute(stmt);
			}
			return null;
		} finally {
			DBManager.close(stmt);
			DBManager.close(conn);
		}
	}

	/**
	 * @param paramMap
	 * @param statement
	 * @param sqlAttrList
	 * @return
	 */
	private SqlInfo sqlParsing(Map<String, Object> paramMap, String statement) {
		List<Object> sqlAttrList = new ArrayList<>();
		while (statement.indexOf("#{") >= 0) {
			String substring = statement.substring(statement.indexOf("#{") + 2, statement.indexOf("}"));
			statement = statement.replace("#{" + substring + "}", "?");
			Object object = paramMap.get(substring.trim());
			sqlAttrList.add(object);
		}
		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.setSqlParsed(statement);
		sqlInfo.setParameterList(sqlAttrList);
		return sqlInfo;
	}

	/**
	 * 更新语句执行
	 * 
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	public int updateExecute(PreparedStatement stmt) throws SQLException {
		return stmt.executeUpdate();
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
			Class<?> forName = mapper.getReturnType();
			Object newInstance = forName.newInstance();
			Field[] declaredFields = forName.getDeclaredFields();
			for (Field field : declaredFields) {
				resultSetMapping(rs, newInstance, field);
			}
			list.add(newInstance);
		}
		DBManager.close(rs);
		return list;
	}

	/**
	 * @param rs
	 * @param newInstance
	 * @param field
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	private void resultSetMapping(ResultSet rs, Object newInstance, Field field)
			throws SQLException, IllegalAccessException {
		Class<?> type = field.getType();
		String name = field.getName();
		if (String.class.equals(type)) {
			String string = rs.getString(name);
			field.setAccessible(true);
			field.set(newInstance, string);
		}
		if (Integer.class.equals(type)) {
			int string = rs.getInt(name);
			field.setAccessible(true);
			field.set(newInstance, string);
		}
		if (Long.class.equals(type)) {
			long long1 = rs.getLong(name);
			field.setAccessible(true);
			field.set(newInstance, long1);
		}
		if (Short.class.equals(type)) {
			short short1 = rs.getShort(name);
			field.setAccessible(true);
			field.set(newInstance, short1);
		}
		if (Byte.class.equals(type)) {
			byte byte1 = rs.getByte(name);
			field.setAccessible(true);
			field.set(newInstance, byte1);
		}
		if (Double.class.equals(type)) {
			double double1 = rs.getDouble(name);
			field.setAccessible(true);
			field.set(newInstance, double1);
		}
		if (Float.class.equals(type)) {
			float float1 = rs.getFloat(name);
			field.setAccessible(true);
			field.set(newInstance, float1);
		}
		if (Date.class.equals(type)) {
			Date date = rs.getDate(name);
			field.setAccessible(true);
			field.set(newInstance, date);
		}
		if (BigDecimal.class.equals(type)) {
			BigDecimal bigDecimal = rs.getBigDecimal(name);
			field.setAccessible(true);
			field.set(newInstance, bigDecimal);
		}
		if (Boolean.class.equals(type)) {
			boolean boolean1 = rs.getBoolean(name);
			field.setAccessible(true);
			field.set(newInstance, boolean1);
		}
	}
}
