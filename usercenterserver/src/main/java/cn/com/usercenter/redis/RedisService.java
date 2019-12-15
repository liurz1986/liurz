package cn.com.usercenter.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.com.usercenter.util.RedisUtil;
import redis.clients.jedis.JedisPool;

@Component
public class RedisService extends RedisUtil{

	@Value("${redis.ip}")
	private String redisIp;// 注入redis ip

	@Value("${redis.port}")
	private int redisPort;// 注入redis ip
	
	
	
	public RedisService(){
		JedisPool pool=new JedisPool(redisIp,redisPort);
		initJedis(pool);
	}
	
}
