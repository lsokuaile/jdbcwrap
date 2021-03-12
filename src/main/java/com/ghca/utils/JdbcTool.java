package com.ghca.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
/*
	描述：这个工具类是oracle工具类的封装
	思路：通过输出流将jdbc.properties文件读入到这个文件中，将这个文件进行线程绑定，
		使同一个事物使用同一个连接对象，保证了资源的不浪费性
*/
public class JdbcTool {
	//输出流
	private static InputStream inputStream = JdbcTool.class.getResourceAsStream("/jdbc.properties");
	//配置文件
	private static Properties properties = new Properties();
	//线程
	private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

	//加载驱动 创建连接
	public static Connection getConnection() throws ClassNotFoundException, IOException, SQLException {
		properties.load(inputStream);
		Class.forName(properties.getProperty("oracle.driver"));
		Connection connection = connectionThreadLocal.get();
		if (connection == null) {
			connection = DriverManager.getConnection(properties.getProperty("oracle.url"), properties.getProperty("oracle.username"), properties.getProperty("oracle.password"));
			connectionThreadLocal.set(connection);
		}
		return connection;
	}

	public static void close(PreparedStatement preparedStatement, Connection connection, ResultSet resultSet) {
		if (connection != null) {
			try {
				connection.close();
				connectionThreadLocal.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public static void close(PreparedStatement preparedStatement, ResultSet resultSet) {

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(PreparedStatement preparedStatement) {

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				connectionThreadLocal.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: getAllTableNameForMySql
	 * @Description: 获取数据库下所有表
	 * @param database
	 * @return
	 * @return: List<String>
	 */
	public static List<String> getAllTableNameForMySql(Connection conn, String database) {
		List<String> list = new ArrayList<String>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
//			ps = conn.prepareStatement("use " + database);
//			ps.execute();
			// // select table_name from information_schema.tables where table_schema='test' and table_type='base table';

//			ps = conn.prepareStatement("select table_name from information_schema.tables where table_schema='"+database+"' and table_type='base table';");
//			rs = ps.executeQuery();
			// 模拟sql
			ps = conn.prepareStatement("select student_id, student_name from student");
			rs = ps.executeQuery();

//			Statement statement = conn.createStatement();
//			rs = statement.executeQuery("select table_name from information_schema.tables where table_schema='"+database+"' and table_type='base table';");

			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}