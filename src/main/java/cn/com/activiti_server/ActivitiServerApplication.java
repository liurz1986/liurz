package cn.com.activiti_server;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Activiti服务----流程数据 记录保存在mysql数据库中
 * 
 * @ClassName: ActivitiServerApplication
 * @Description: TODO
 * @author lwx393577：
 * @date 2019年12月10日 下午10:23:59
 *
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivitiServerApplication.class, args);
	}
}