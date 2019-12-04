package cn.com.usercenter.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.com.usercenter.redis.RedisService;
import cn.com.usercenter.util.ResultDateUtil;

/**
 * 
 * 对外接口 验证用户可用性 实现：主要是通过redis缓存验证
 * 
 * x-Auth-Token数据格式：{"uuid":"xxxxx","userName":"xxxxx","createTime":"xxxx"}
 * 
 */
@RestController
@RequestMapping("/v1/rest")
public class LoginController {
    
	@Autowired
	private RedisService  redisService;
	
	/**
	 * 用户验证 通过token到redis中验证
	 * 
	 * @param token
	 * 
	 */
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public String Validate(@RequestBody String token) {
		if (isExist(token)){
			return	ResultDateUtil.successResult("validata success");
		}else{
			return	ResultDateUtil.errorResult("validata error");
		}
	}

	public boolean isExist(String token) {
		if (!StringUtils.isEmpty(token)) {
			JSONObject paramjson = JSON.parseObject(token);
			String userUuid=paramjson.getString("uuid");
			String userName=paramjson.getString("userName");
		    if(!StringUtils.isEmpty(userUuid)&&!StringUtils.isEmpty(userName)){
		    	
			    String cacheValue = redisService.get("token_"+userUuid);
					if(token.equalsIgnoreCase(cacheValue)){
						
						return true;
					}else{
						
					}
				}
		    }
       return false;
      }
	  
	  public void test(){
		  
	  }
	}
