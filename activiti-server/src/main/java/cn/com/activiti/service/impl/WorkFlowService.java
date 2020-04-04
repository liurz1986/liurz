package cn.com.activiti.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.com.activiti.entity.Activiti;
import cn.com.activiti.entity.ActivitiTask;
import cn.com.activiti.service.IWorkFlowService;

/**
 * RepositoryService: 流程仓库Service，用于管理流程仓库，例如：部署，删除，读取流程资源
 * 
 * IdentityService：身份Service，可以管理，查询用户，组之间的关系
 * 
 * RuntimeService：运行时Service，可以处理所有正在运行状态的流程实例，任务等
 * 
 * TaskService：任务Service，用于管理，查询任务，例如：签收，办理，指派等
 * 
 * HistoryService：历史Service，可以查询所有历史数据，例如：流程实例，任务，活动，变量，附件等
 * 
 * FormService：表单Service，用于读取和流程，任务相关的表单数据
 * 
 * ManagementService：引擎管理Service，和具体业务无关，主要是可以查询引擎配置，数据库，作业等
 * 
 * DynamicBpmnService：一个新增的服务，用于动态修改流程中的一些参数信息等，是引擎中的一个辅助的服务
 */
@Service
public class WorkFlowService implements IWorkFlowService {
	private Logger logger = LoggerFactory.getLogger(WorkFlowService.class);
	/**
	 * 任务管理
	 */
	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	/**
	 * 开启流程
	 */
	@Override
	public Map<String, Object> startProcess(String processDefinitionKey, Activiti vars) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			logger.info("开启流程成功：流程图id为：" + processDefinitionKey);
			// 封装数据
			Map<String, Object> params = new HashMap<String, Object>(4);
			params.put("applyUser", vars.getApplyUser());
			params.put("days", vars.getDays());
			params.put("reason", vars.getReason());
			// 开始流程 这个是查看数据库中act_re_procdef表
			// processDefinitionKey 是 bpmn 文件的名称,params参数，方便过程中跟踪
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, params);
			String processId = processInstance.getId();
			// 获取第一个待处理节点任务
			Task taskCurr = taskService.createTaskQuery().processInstanceId(processId).singleResult();
			if (null != taskCurr) {
				result.put("taskId", taskCurr.getId());
			}
			result.put("status", "success");
			result.put("processId", processId);
			logger.info("开启流程成功：流程id" + processId);
		} catch (Exception e) {
			logger.error("开启流程失败：" + e);
			result.put("status", "fail");
			result.put("message", "开启流程失败");
			return result;
		}
		return result;
	}

	/**
	 * 审批任务，并制定下一个节点审批人
	 * 
	 * @param taskId
	 *            任务id
	 * @param activiti
	 *            审批带的参数，主语是审批后，不同情况走不同分支的，需要带对应参数
	 */
	@Override
	public Map<String, Object> complete(String taskId, Activiti activiti) {
		logger.info("流程审批开始：当前任务id：" + taskId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Status", true);
		if (null == activiti) {
			result.put("Status", false);
			result.put("message", "数据为空");
			return result;
		}
		if (null == activiti.getTaskId()) {
			result.put("Status", false);
			result.put("message", "taskId不能为空");
			return result;
		}
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (null == task) {
			result.put("Status", false);
			result.put("message", "当前任务已经处理，taskid:" + activiti.getTaskId());
			return result;
		}
		String processId = task.getProcessInstanceId();
		// 审批
		try {
			// 传的参数，多个条件可以传多个值
			Map<String, Object> params = new HashMap<String, Object>(4);
			if (!StringUtils.isEmpty(activiti.getPass())) {
				params.put("pass", activiti.getPass());// 审批是不是通过
			}
			taskService.complete(activiti.getTaskId(), params);// 查看act_ru_task表

		} catch (Exception e) {
			logger.error("流程审批失败：" + e);
			result.put("status", "fail");
			result.put("message", "流程审批失败");
			return result;
		}
		try {
			// 获取当前节点
			Task taskCurr = taskService.createTaskQuery().processInstanceId(processId).singleResult();
			// 如果时最后一个节点的话，就没有下一个节点了
			if (null == taskCurr) {
				result.put("status", "success");
				result.put("message", "流程处理结束");
				return result;
			}
			String taskIdNext = taskCurr.getId();
			// 下一节点处理人
			if (!StringUtils.isEmpty(activiti.getNextAssign())) {
				taskService.setAssignee(taskIdNext, activiti.getNextAssign());
			}
			result.put("status", "success");
			result.put("message", "success");
			result.put("taskIdNext", taskIdNext);
			logger.info("流程审批结束：下一节点任务id：" + taskIdNext);
		} catch (Exception e) {
			logger.error("流程审批后设置下一个节点审批人失败：" + e);
			result.put("status", "fail");
			result.put("message", "流程审批后设置下一个节点审批人失败");
			return result;
		}
		return result;
	}

	/**
	 * 就是设置当前任务处理人(任务的认领或任务的转移)
	 * 
	 * @param taskId
	 * @param userId
	 * 
	 */
	@Override
	public void setAssignee(String taskId, String userId) {

		taskService.setAssignee(taskId, userId);
	}

	/**
	 * 
	 * 认领任务（任务有处理人了，就不能执行认领了）
	 * 
	 * @param taskId
	 *            taskId
	 * @param userId
	 *            userId
	 */
	@Override
	public void claim(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}

	/**
	 * 按组认领任务
	 * 
	 * @param taskId
	 * @param groupId
	 */
	@Override
	public void addCandidateGroup(String taskId, String groupId) {
		taskService.addCandidateGroup(taskId, groupId);

	}

	/**
	 * 获取流程的当前待处理任务的处理人 ACT_RU_TASK表 processId从act_hi_procint表中获取（proc_inst_id）
	 * 
	 * @Title: getAssignee
	 * @param processId
	 * @return String
	 */
	@Override
	public String getAssignee(String processId) {
		Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		// ACT_RU_TASK表中没有流程id对应的task，或task已经结束
		if (null == task) {
			return "";
		}
		return task.getAssignee();
	}

	/**
	 * 获取处理人的待处理所有任务 ACT_RU_TASK表
	 * 
	 * @Title: getTasksByAssignee
	 * @Description: TODO
	 * @param assignee
	 * @return List<Task>
	 */
	@Override
	public List<Map<String, String>> getTasksByAssignee(String assignee) {
		List<Map<String, String>> taskd = new ArrayList<Map<String, String>>();
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
		// ACT_RU_TASK表中没有流程id对应的task，或task已经结束
		if (null != tasks) {
			for (Task task : tasks) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("taskId", task.getId());
				map.put("taskName", task.getName());
				map.put("processId", task.getProcessInstanceId());
				taskd.add(map);
			}
		}
		return taskd;
	}

	/**
	 * 查询自己要审批的节点信息
	 */
	@Override
	public List<ActivitiTask> myApproval(String userName) {
		List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(userName).orderByTaskCreateTime().desc()
				.list();
		List<ActivitiTask> activitiTaskList = new ArrayList<>();
		for (Task task : taskList) {
			ActivitiTask activitiTask = new ActivitiTask();
			activitiTask.setId(task.getId());
			activitiTask.setName(task.getName());
			activitiTask.setCreateTime(task.getCreateTime());
			String instanceId = task.getProcessInstanceId();
			ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId)
					.singleResult();
			Activiti activiti = getActiviti(instance);
			activitiTask.setActiviti(activiti);
			activitiTaskList.add(activitiTask);
		}
		return activitiTaskList;
	}

	/**
	 * 用户的任务列表
	 * 
	 * @param userName
	 */
	@Override
	public List<Activiti> myActiviti(String userName) {
		List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().startedBy(userName).list();
		List<Activiti> activitisList = new ArrayList<>();
		for (ProcessInstance instance : instanceList) {
			Activiti activiti = getActiviti(instance);
			activitisList.add(activiti);
		}
		return activitisList;
	}

	/**
	 * 获取流程的当前任务节点
	 */
	@Override
	public Task queryTaskByProcessId(String processInstanceId) {
		// TODO Auto-generated method stub
		return taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
	}

	/**
	 * 获取当前任务
	 */
	@Override
	public Task queryTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskService.createTaskQuery().taskTenantId(taskId).singleResult();
	}

	@Override
	public Object queryVariables(String taskId, String varName) {

		return null;
	}

	/**
	 * 删除流程 （针对没有结束的流程）act_hi_procint表，是软删除，
	 * 删除后end_time、duration、delete_reason会有值。
	 * 
	 * @Title: deleteProcessInstance
	 * @Description: TODO
	 * @param processInstanceId
	 * @param deleteReason
	 * @throws Exception
	 * @return void
	 */
	@Override
	public void deleteProcessInstance(String processInstanceId, String deleteReason) throws Exception {
		runtimeService.deleteProcessInstance(processInstanceId, deleteReason);

	}

	@Override
	public Deployment deploy(String fileName, String category) {

		return null;
	}

	@Override
	public void deleteDeployment(String deploymentId) {

	}

	@Override
	public List<Deployment> deployList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessDefinition> getProcessList() throws Exception {
		return null;
		// TODO Auto-generated method stub
	}

	@Override
	public void suspendProcess(String processDefinitionId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateProcess(String processDefinitionId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollBackTask(String taskId, String userId, String reason, String groupId, int backNum)
			throws Exception {
		// TODO Auto-generated method stub

	}

	private Activiti getActiviti(ProcessInstance instance) {
		Integer days = runtimeService.getVariable(instance.getId(), "days", Integer.class);
		String reason = runtimeService.getVariable(instance.getId(), "reason", String.class);
		Activiti activiti = new Activiti();
		activiti.setApplyUser(instance.getStartUserId());
		activiti.setDays(days);
		activiti.setReason(reason);
		Date startTime = instance.getStartTime(); // activiti 6 才有
		activiti.setApplyTime(startTime);
		activiti.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
		return activiti;
	}

}
