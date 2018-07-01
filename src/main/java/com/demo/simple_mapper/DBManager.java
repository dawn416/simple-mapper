package com.demo.simple_mapper;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * <h1>DBManager</h1>
 * <h1>根据配置信息，维持连接对象的管理</h1>
 * 
 */
public class DBManager {

	/**
	 * 创建一个新的数据库连接
	 * 
	 * @return java.sql.Connection
	 */
	public static Connection getConn() {
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

	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		close(rs);
		close(stmt);
		close(conn);
	}

	public static void close(ResultSet rs, Connection conn) {
		close(rs);
		close(conn);
	}

	public static void close(Connection x) {
		if (x == null) {
			return;
		}
		try {
			x.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("close connection error");
		}
	}

	public static void close(Statement x) {
		if (x == null) {
			return;
		}
		try {
			x.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("close statement error");
		}
	}

	public static void close(ResultSet x) {
		if (x == null) {
			return;
		}
		try {
			x.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("close result set error");
		}
	}

	public static void close(Closeable x) {
		if (x == null) {
			return;
		}
		try {
			x.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("close error");
		}
	}

}
