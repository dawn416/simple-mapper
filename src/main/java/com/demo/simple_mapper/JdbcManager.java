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

import com.demo.simple_mapper.bean.MethodInfo;
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
	public static final String INSERT = "insert";
	/**
	 *
	 */
	public static final String DELETE = "delete";
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
	 */
	public Object sqlExecuting(MethodInfo methodInfo, SqlInfo sqlParsing) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DBManager.getConn();
			stmt = conn.prepareStatement(sqlParsing.getSqlParsed());
			List<Object> parameterList = sqlParsing.getParameterList();
			for (int i = 0, j = 1; i < parameterList.size(); i++, j++) {
				stmt.setObject(j, parameterList.get(i));
			}
			System.out.println("1111111");
			if (SELECT.equals(methodInfo.getSqlType())) {
				List selectExecute = selectExecute(methodInfo, stmt);
				if (methodInfo.isMany()) {
					return selectExecute;
				}
				if (selectExecute.size() == 1) {
					return selectExecute.get(0);
				}
				if (selectExecute.size() == 0) {
					return null;
				}
				throw new RuntimeException("返回实例数超过1个");
			} else if (UPDATE.equals(methodInfo.getSqlType()) || DELETE.equals(methodInfo.getSqlType())) {
				return updateExecute(stmt);
			} else if (INSERT.equals(methodInfo.getSqlType())) {
				// TODO 若为自定义类型，要返回主键值
				return updateExecute(stmt);
			}
			return null;
		} finally {
			DBManager.close(stmt);
			DBManager.close(conn);
		}
	}

	/**
	 * 更新语句执行
	 * 
	 */
	private int updateExecute(PreparedStatement stmt) throws SQLException {
		return stmt.executeUpdate();
	}

	/**
	 * 查询语句执行
	 *
	 */
	private List selectExecute(MethodInfo methodInfo, PreparedStatement stmt) {
		List list = new ArrayList();
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
			while (rs.next()) {
				Class<?> forName = methodInfo.getResultType();
				Object newInstance = forName.newInstance();
				Field[] declaredFields = forName.getDeclaredFields();
				for (Field field : declaredFields) {
					resultSetMapping(rs, newInstance, field);
				}
				list.add(newInstance);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(rs);
		}

		return list;
	}

	/**
	 * 返回数据放入bean中
	 * 
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
			int int1 = rs.getInt(name);
			field.setAccessible(true);
			field.set(newInstance, int1);
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
