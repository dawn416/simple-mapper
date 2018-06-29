package com.demo.simple_mapper;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
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
	public static Connection createConn() {
		// try {
		// Class.forName(App.DRIVER);
		// return DriverManager.getConnection(App.URL, App.USER, App.PASS);
		// } catch (Exception e) {
		// e.printStackTrace();
		// System.out.println("获取数据库连接失败");
		return null;
		// }
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
