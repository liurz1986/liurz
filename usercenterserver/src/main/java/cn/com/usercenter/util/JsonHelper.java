package cn.com.usercenter.util;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 使用jackson进行json处理
 * @author Administrator
 *
 */


public class JsonHelper {
	private static Logger log=LoggerFactory.getLogger(JsonHelper.class);
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串�??
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    
    public static String objectToJson(Object data) {
    	try {
			
    		String string = MAPPER.writeValueAsString(data);
			
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.info("----objectToJson failure---");
		}
    	return null;
    }
    
    /**
     * 将json结果集转化为对象（包括Map,List等集合对象）
     * 
     * @param jsonData json数据
     * @param clazz 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
           
        	T t = MAPPER.readValue(jsonData, beanType);
        	
        	return t;
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error("----jsonToPojo failure---"+e.getMessage());
        }
        return null;
    }
    /**
     * 将json字符串转化成json�?
     * 
     * @param jsonStr json字符�?
     * 
     * @return
     */
    
    public JsonNode StringToJsonNode(String jsonStr){

    	try{
    	 return 	MAPPER.readTree(jsonStr);
    	}catch(Exception e){
    		
    		log.error("----StringToJsonNode failure---"+e.getMessage());
        }
    	return null;
    }
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
      <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    	
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("----jsonToList failure---");
		}
    	
    	return null;
    }
    /**
     * 将xml解析成json数据
     * @param xml
     * @return
     */
    public static JSONObject XmlToJSON(String xml){
    	
    	JSONObject jsonObject=null;
		try {
			jsonObject = XML.toJSONObject(xml);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        return 	jsonObject;
    }
}
