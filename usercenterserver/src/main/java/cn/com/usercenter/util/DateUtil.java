package cn.com.usercenter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    
	public static String ToString(Date date){
		
		SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return simple.format(date);
	}
	
    public static Date ToDate(String datestr){
		
		SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=null;
		try {
			date = simple.parse(datestr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
}
