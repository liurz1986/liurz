package cn.com.activiti_server;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.activiti_server.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ActivitiServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivitiTest {

	@Autowired
	TestService testService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;

	@Test
	public void TestStartProcess() {
		System.out.println("Start.........");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess", "1");
		System.out.println("流程启动成功，流程id:" + pi.getId());
	}

	@Test
	public void findTasksByUserId() {
		String userId = "dulingjiang";
		List<Task> resultTask = taskService.createTaskQuery().processDefinitionKey("myProcess")
				.taskCandidateOrAssigned(userId).list();
		System.out.println("任务列表：" + resultTask);
	}

}
