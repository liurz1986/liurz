package cn.com.usercenter.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import cn.com.usercenter.util.JdbcUtill;

public class DataTest {

	public static void test() {
		String sql = "insert t_user(id,name,password,createtime,endtime,uuid) values(?,?,?,?,?,?)";
		Connection conn = JdbcUtill.getConnection();
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			Date date = new Date();
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dataStr = dateFormate.format(date);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.YEAR, 1);// 过去一年(-1),当前的一年后（1）
			Date m = c.getTime();
			String year = dateFormate.format(m);
			pre.setObject(1, 11);
			pre.setObject(2, "test1");
			pre.setObject(3, "test1");
			pre.setObject(4, dataStr);
			pre.setObject(5, year);
			pre.setObject(6, UUID.randomUUID().toString());

			pre.execute();
			pre.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    //删除文件夹下所用内容。delete删除的目录（文件夹）下面是没有子文件的；删除的文件（非文件夹）
	public static void test1(File file) {
		if (file.exists()) {// 文件或目录存在
			if (file.isDirectory() && file.listFiles().length > 0) {// 文件是目录并且有子目录
				// 目录下的子文件
				for (File childrenfile : file.listFiles()) {
					test1(childrenfile);
				}

			} else {// 文件不是目录删除
				file.delete();
			}
		}
	}
    
	public static void stream_writer(){
		try {
			FileWriter fileWriter=new FileWriter(new File("D:/usr/file/test.txt"));
			BufferedWriter buffer=new BufferedWriter(fileWriter);
			buffer.write("create new data");
			buffer.flush();
			buffer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void stream_read(){
		try {
			FileReader fileReader=new FileReader(new File("D:/usr/file/test.txt"));
			BufferedReader buffer=new BufferedReader(fileReader);
			//读取一行数据
			String res=buffer.readLine();
			while(null!=res){
				System.out.println(res);
				res=buffer.readLine();
			}
			buffer.close();
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		// DataTest.test();
		File file = new File("D:/usr/file");
		//DataTest.test1(file);
		//file.delete();
		DataTest.stream_read();
	}
}
