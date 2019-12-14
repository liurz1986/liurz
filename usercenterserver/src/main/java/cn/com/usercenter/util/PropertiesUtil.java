package cn.com.usercenter.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	static Properties pro = null;
	static String value = null;

	static {
		InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("client.properties");
		try {
			pro = new Properties();
			pro.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {

		value = pro.getProperty(key);

		return value;
	}
}
