package cn.com.usercenter.util;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/***
 * Redis工具(用于缓存)
 * 
 * @author Administrator
 *
 */
public class RedisUtil {
	private static Logger log = LoggerFactory.getLogger(RedisUtil.class);
	protected JedisPool jedisPool;
	protected Jedis jedis;
	// 默认是连接本地
	public RedisUtil() {
		try {
			this.jedisPool = new JedisPool("127.0.0.1", 6397);
			this.jedis = jedisPool.getResource();
		} catch (Exception e) {
			log.error("connection redis failure:" + e.getStackTrace());
		}

	}

	public RedisUtil(JedisPool jedisPool) {
		
		initJedis(jedisPool);

	}

	public void initJedis(JedisPool jedisPool){
		try {
			this.jedisPool = jedisPool;
			this.jedis = jedisPool.getResource();
		} catch (Exception e) {
			log.error("connection redis failure:" + e);
		}
	}
	
	public void set(String key, String value) {
		jedis.set(key, value);
	}

	public void set(String key, String value, int expire) {
		jedis.set(key, value);
		jedis.expire(key, expire);
	}

	public void set(byte[] key, byte[] value) {
		jedis.set(key, value);
	}

	public void set(byte[] key, byte[] value, int expire) {
		jedis.set(key, value);
		jedis.expire(key, expire);
	}

	public void hmset(String key, Map<String, String> value) {

		jedis.hmset(key, value);

	}

	public void hmset(byte[] key, Map<byte[], byte[]> value) {

		jedis.hmset(key, value);

	}

	public String get(String key) {
	   
		return jedis.get(key);
	}

	public byte[] get(byte[] key) {
	  
		return jedis.get(key);
		
	}

	public void delete(String key) {

		jedis.del(key);
	}

	public void delete(byte[] key) {

		jedis.del(key);
	}

	public static void set(Jedis jedis, byte[] key, byte[] value) {

		jedis.set(key, value);

	}

	public static void set(Jedis jedis, String key, String value) {

		jedis.set(key, value);

	}

	// expire--有效时间
	public static void set(Jedis jedis, byte[] key, byte[] value, int expire) {

		jedis.set(key, value);
		jedis.expire(key, expire);

	}

	// expire--有效时间
	public static void set(Jedis jedis, String key, String value, int expire) {

		jedis.set(key, value);
		jedis.expire(key, expire);

	}

	/***
	 * 将一个Map集合存放redis
	 * 
	 * @param key
	 * @param value
	 */
	public static void hmset(Jedis jedis, String key, Map<String, String> value) {

		jedis.hmset(key, value);

	}

	/***
	 * 将一个Map集合存放redis
	 * 
	 * @param key
	 * @param value
	 */
	public static void hmset(Jedis jedis, byte[] key, Map<byte[], byte[]> value) {

		jedis.hmset(key, value);

	}

	public static String get(Jedis jedis, String key) {

		return jedis.get(key);
	}

	public static byte[] get(Jedis jedis, byte[] key) {

		return jedis.get(key);
	}

	public static Map<String, String> hgetAll(Jedis jedis, String key) {

		return jedis.hgetAll(key);
	}

	public static Map<byte[], byte[]> hgetAll(Jedis jedis, byte[] key) {

		return jedis.hgetAll(key);
	}

	// 将key由oldname重命名为newname
	public static void rename(Jedis jedis, String oldname, String newname) {

		jedis.rename(oldname, newname);
	}

	// 将key由oldname重命名为newname
	public static void rename(Jedis jedis, byte[] oldname, byte[] newname) {

		jedis.rename(oldname, newname);
	}

	// 确认�?个key是否存在
	public static boolean exists(Jedis jedis, String key) {

		return jedis.exists(key);
	}

	// 确认�?个key是否存在
	public static boolean exists(Jedis jedis, byte[] key) {

		return jedis.exists(key);
	}

	// 根据key删除数据
	public static void delete(Jedis jedis, String key) {

		jedis.del(key);
	}

	// 根据key删除数据
	public static void delete(Jedis jedis, byte[] key) {

		jedis.del(key);
	}

	/**
	 * 释放资源
	 * 
	 * @param jedis
	 */
	public static void closeResource(final Jedis jedis, final JedisPool jedisPool) {
		if (jedis != null) {
			jedis.close();
		}
		if (jedisPool != null) {
			jedisPool.close();
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param jedis
	 */
	public static void disconnect(Jedis jedis) {

		jedis.disconnect();

	}

	/**
	 * 释放资源
	 * 
	 * @param jedis
	 */
	public void closeResource() {
		if (jedis != null) {
			jedis.close();
		}
		if (jedisPool != null) {
			jedisPool.close();
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param jedis
	 */
	public void disconnect() {

		jedis.disconnect();

	}

	public static void main(String[] args) {
		JedisPool jedisPool = new JedisPool("192.168.0.105",6397);
		RedisUtil redisUtil=new RedisUtil(jedisPool);
		redisUtil.set("redisUtil","redis demo");
		System.out.println(redisUtil.get("redisUtil"));
		redisUtil.closeResource();
		redisUtil.disconnect();
	}
}
