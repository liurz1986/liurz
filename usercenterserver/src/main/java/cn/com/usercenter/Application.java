package cn.com.usercenter;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
// @EnableDiscoveryClient
@RestController
public class Application extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// https://localhost:8080/usercenter/demo
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public Map<String, String> test() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", "welcome to you");
		params.put("status", "success");
		params.put（"key","values"）;
        params.put("status1", "success");
		return params;
	}

}
