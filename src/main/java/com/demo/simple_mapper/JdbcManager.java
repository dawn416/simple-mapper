package com.demo.simple_mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
			System.out.println(sqlParsing.getSqlParsed());
			System.out.println(sqlParsing.getParameterList());
			stmt = conn.prepareStatement(sqlParsing.getSqlParsed());
			List<Object> parameterList = sqlParsing.getParameterList();
			for (int i = 0, j = 1; i < parameterList.size(); i++, j++) {
				stmt.setObject(j, parameterList.get(i));
			}
			if (SELECT.equals(methodInfo.getSqlType())) {
				List selectExecute = selectExecute(methodInfo, stmt);
				if (methodInfo.isMany()) {
					return selectExecute;
				}
				if (selectExecute.size() == 1) {
					return selectExecute.get(0);
				}
				if (selectExecute.isEmpty()) {
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
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			List<String> columnlist = new ArrayList<>();
			for (int i = 0; i < columnCount; i++) {
				String columnName = rsmd.getColumnName(i + 1);
				columnlist.add(columnName);
			}
			Class<?> forName = methodInfo.getResultType();
			Field[] declaredFields = forName.getDeclaredFields();
			while (rs.next()) {
				Object newInstance = forName.newInstance();
				for (String columnName : columnlist) {
					Object object = rs.getObject(columnName);
					for (Field field : declaredFields) {
						if (columnName.equals(field.getName())) {
							field.setAccessible(true);
							field.set(newInstance, object);
						}
					}
				}
				list.add(newInstance);
			}
		} catch (InstantiationException | IllegalAccessException | SecurityException | SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(rs);
		}
		return list;
	}

}
