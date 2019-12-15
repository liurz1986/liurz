package cn.com.usercenter.elatricsearch;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.transport.TransportClient;

/**
 * SpringBoot默认支持两种技术来和ElasticSearch交互：
 * 1.Jest(默认不生效)，需要导入jest的工具包(io.searchbox.client.JestClient）
 * 2.SpringData的ElasticSearch模块（默认方式）
 * 
 * @ClassName: Client
 * @Description: TODO
 * @author lwx393577：
 * @date 2019年11月30日 下午11:49:54
 *
 */
public class Client {
	public static final String CLUSTERNAME = "elasticsearch"; // 集群模型
	public static final String INDEX = "base_kb"; // 索引名称
	public static final String TYPE = "entity";// 类型名称
	public static final String HOST = "10.110.6.43"; // 服务器地址
	public static final int PORT = 9300; // 服务端口 TCP为9300 IP为9200

	static Map<String, String> map = new HashMap<String, String>();
	// static Settings settings =
	// ImmutableSettings.settingsBuilder().put(map).put("cluster.name",
	// CLUSTERNAME)
	// .put("client.transport.sniff",true).build();

	private static TransportClient client;

	/*
	 * static { try { Class<?> clazz =
	 * Class.forName(TransportClient.class.getName()); Constructor<?>
	 * constructor = clazz.getDeclaredConstructor(Settings.class);
	 * constructor.setAccessible(true); client = (TransportClient)
	 * constructor.newInstance(settings); client.addTransportAddress(new
	 * InetSocketTransportAddress(HOST, PORT));
	 * 
	 * } catch (ClassNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (NoSuchMethodException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (SecurityException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (InstantiationException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (IllegalAccessException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IllegalArgumentException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (InvocationTargetException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * public static synchronized TransportClient geTransportClient() { return
	 * client; }
	 */

}
