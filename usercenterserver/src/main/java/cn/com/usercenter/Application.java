package cn.com.usercenter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
//@EnableDiscoveryClient
@RestController
public class Application extends SpringBootServletInitializer
{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }
	//https://localhost:8080/usercenter/demo
	@RequestMapping(value="/demo",method=RequestMethod.GET)
	public Map<String,String>  test(){
		Map<String,String> params=new HashMap<String,String>();
		params.put("data", "welcome to you");
		params.put("status", "success");
		params.put（"key","values"）;
		return params;
	}
	
}
