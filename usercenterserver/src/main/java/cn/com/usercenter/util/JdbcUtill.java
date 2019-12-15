package cn.com.usercenter.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcUtill {
	private static Logger log = LoggerFactory.getLogger(JdbcUtill.class);
	private static String url = "jdbc:mysql://localhost:3306/iquoter?"
			+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
	private static Connection connection;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("动态加载mysql驱动失败："+e.getMessage());
		}
	}

	public static Connection getConnection() {
		if (null == connection) {
			try {
				connection = DriverManager.getConnection(url);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("获取conneciton失败："+e.getMessage());
				return null;
			}
		} 
		return connection;
	}
}
