package com.demo.simple_mapper;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
		Connection conn = null;
		Properties prop = new Properties();
		InputStream resourceAsStream = App.class.getResourceAsStream("/jdbc.properties");
		try {
			prop.load(resourceAsStream);
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException | IOException e) {
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
