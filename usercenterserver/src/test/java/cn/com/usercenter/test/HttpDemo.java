package cn.com.usercenter.test;

import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
public class HttpDemo {
private static RestTemplate restTemplate=new RestTemplate();
    
    
    public void test1(){
        String url="http://localhost:8080/userCenter/services/rest/studentService/find";
    	String result=restTemplate.getForEntity(url, String.class).getBody();
    	System.out.println(result);
    } 
    
    public void test2(){
    	String url="http://localhost:8080/userCenter/services/rest/studentService/findById/3?name=xiaoming";
    	String result=restTemplate.getForEntity(url, String.class).getBody();
    	System.out.println(result);
    } 
    
    public void test3(){
    	String url="http://localhost:8080/userCenter/services/rest/studentService/post";
    	HttpHeaders headers = new HttpHeaders(); 
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("userToken", UUID.randomUUID().toString());
    	String reqJsonStr = "{\"status\":\"post\", \"data\":\"post data\"}";
    	HttpEntity<String> entity = new HttpEntity<String>(reqJsonStr,headers);
    	
    	String result=restTemplate.exchange(url,HttpMethod.POST,entity,String.class).getBody();
    	
    	System.out.println(result);
    } 
    public static void main(String[] args) {
    	HttpDemo httpDemo=new HttpDemo();
    	httpDemo.test3();
	}
}
