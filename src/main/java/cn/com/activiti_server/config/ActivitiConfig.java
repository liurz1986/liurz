package cn.com.activiti_server.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration // 声名为配置类，继承Activiti抽象配置类
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource activitiDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public SpringProcessEngineConfiguration springProcessEngineConfiguration(
			PlatformTransactionManager transactionManager, SpringAsyncExecutor springAsyncExecutor) throws IOException {

		// return baseSpringProcessEngineConfiguration(activitiDataSource(),
		// transactionManager, springAsyncExecutor);
		SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
		config.setDataSource(activitiDataSource());
		// config.setDbHistoryUsed(false);
		config.setDbIdentityUsed(false);
		config.setTransactionManager(transactionManager);
		// 添加字体
		config.setActivityFontName("宋体");
		config.setLabelFontName("宋体");
		config.setAnnotationFontName("宋体");
		// 存放数据的数据库：mysql、oracle
		config.setDatabaseType("mysql");
		// config.getJobExecutor();
		// 第一生成表要启动下面配置，以后重新生成create改为drop-create
		// 如果有表不存在就更新添加
		// config.setDatabaseSchemaUpdate("create");
		// 设置Schema为ACT，他会字段添加表
		config.setDatabaseSchema("ACT");
		return config;
	}
}