package cn.com.usercenter.util;

import com.alibaba.fastjson.JSONObject;

public class ResultDateUtil {

	public static String successResult(Object obj) {
		JSONObject json = new JSONObject();
		json.put("result", "success");
		json.put("data", obj);
		return json.toString();
	}

	public static String errorResult(Object obj) {
		JSONObject json = new JSONObject();
		json.put("result", "error");
		json.put("data", obj);
		return json.toString();
	}
	
	
}
