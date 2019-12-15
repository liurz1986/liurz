package cn.com.usercenter.activiti;

import java.io.IOException;

import javax.sql.DataSource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 创建activiti提供的各种服务
 */

@Configuration
public class ActivitiConfig {
	private Logger logger = LoggerFactory.getLogger(ActivitiConfig.class);

	@Value("${spring.activiti.database-schema-update}")
	private String databaseSchemaUpdate;

	@Value("${spring.activiti.db-identity-used}")
	private boolean dbIdentityUsed;

	@Value("${spring.datasource.url}")
	private String dbUrls;

	@Bean
	public ProcessEngine processEngine(DataSourceTransactionManager transactionManager, DataSource dataSource)
			throws IOException {
		logger.info("==========activiti=======开始==============");
		SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();

		/*
		 * 自动部署已有的流程文件 作用相当于
		 * (据bpmn文件部署流程repositoryService.createDeployment().addClasspathResource
		 * ("singleAssignee.bpmn").deploy();)
		 */

		Resource[] resources = new PathMatchingResourcePatternResolver()
				.getResources(ResourceLoader.CLASSPATH_URL_PREFIX + "process/*.bpmn");
		configuration.setTransactionManager(transactionManager);
		// 设置数据源
		configuration.setDataSource(dataSource);
		// 是否每次都更新数据库
		// configuration.setDatabaseSchemaUpdate(databaseSchemaUpdate);
		configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
		// configuration.setAsyncExecutorActivate(false);
		configuration.setDeploymentResources(resources);
		// 设置是否使用activti自带的用户体系
		configuration.setDbIdentityUsed(dbIdentityUsed);
		return configuration.buildProcessEngine();
	}

	/**
	 * 工作流仓储服务
	 * 
	 * @param processEngine
	 * @return
	 */

	@Bean
	public RepositoryService repositoryService(ProcessEngine processEngine) {
		return processEngine.getRepositoryService();
	}

	/**
	 * 工作流运行服务
	 * 
	 * @param processEngine
	 * @return
	 */

	@Bean
	public RuntimeService runtimeService(ProcessEngine processEngine) {
		return processEngine.getRuntimeService();
	}

	/**
	 * 工作流任务服务
	 * 
	 * @param processEngine
	 * @return
	 */

	@Bean
	public TaskService taskService(ProcessEngine processEngine) {
		return processEngine.getTaskService();
	}

	/**
	 * 工作流历史数据服务
	 * 
	 * @param processEngine
	 * @return
	 */

	@Bean
	public HistoryService historyService(ProcessEngine processEngine) {
		return processEngine.getHistoryService();
	}

	/**
	 * 工作流管理服务
	 * 
	 * @param processEngine
	 * @return
	 */

	@Bean
	public ManagementService managementService(ProcessEngine processEngine) {
		return processEngine.getManagementService();
	}

	/**
	 * 工作流唯一服务
	 * 
	 * @param processEngine
	 * @return
	 */

	@Bean
	public IdentityService identityService(ProcessEngine processEngine) {
		return processEngine.getIdentityService();
	}
}
