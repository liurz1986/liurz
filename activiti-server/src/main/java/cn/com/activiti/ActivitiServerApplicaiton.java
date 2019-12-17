package cn.com.activiti;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication(exclude = org.activiti.spring.boot.SecurityAutoConfiguration.class)
@RestController
public class ActivitiServerApplicaiton extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ActivitiServerApplicaiton.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ActivitiServerApplicaiton.class, args);
	}

	/**
	 * 设置jackson，进行http请求和响应数据json控制,主要是设置时间格式---默认就是jackson
	 * 
	 * 
	 * @Title: jackson
	 * @Description: TODO
	 * @return
	 * @return HttpMessageConverters
	 */
	@Bean
	public HttpMessageConverters jackJsonConfigure() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		// 日期格式化 可以避免java中date返回到前端转化成时间戳
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMapper.setDateFormat(dateFormat);
		converter.setObjectMapper(objectMapper);
		List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
		// 数据格式为json
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastMediaTypes.add(MediaType.APPLICATION_JSON);
		converter.setSupportedMediaTypes(fastMediaTypes);
		return new HttpMessageConverters(converter);
	}

	// https://localhost:8080/usercenter/demo
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public Map<String, String> test() {
		Map<String, String> params = new HashMap<String, String>();// 线程不安全的
		params.put("data", "welcome to you");
		return params;
	}
}
