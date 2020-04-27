package cn.com.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.com.activiti.entity.WorkFlowVo;
import cn.com.activiti.service.IWorkFlowService;
import cn.com.activiti.util.Constants;
import cn.com.activiti.util.ResponseVo;

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
	 * 开启流程:同时设置第一个节点的处理人
	 */
	@Override
	public ResponseVo startProcess(WorkFlowVo workFlowVo) throws Exception {
		ResponseVo result = new ResponseVo();
		Map<String, Object> items = new HashMap<String, Object>();
		logger.info("开启流程,params:{}", workFlowVo);
		if (StringUtils.isEmpty(workFlowVo.getProcessDefinitionKey())) {
			result.setStatus(Constants.FAIL);
			result.setMessage("流程图的id不能为空");
			return result;25001 25006
		}
		// 开始流程 这个是查看数据库中act_re_procdef表
		// processDefinitionKey 是 bpmn 文件的名称,params参数，方便过程中跟踪
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(workFlowVo.getProcessDefinitionKey(),
				workFlowVo.getWorkFlowParams());
		String processId = processInstance.getId();
		// 获取第一个待处理节点任务
		Task taskCurr = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		String taskId = "";
		if (null != taskCurr) {
			taskId = taskCurr.getId();
			items.put("taskId", taskCurr.getId());
		}
		items.put("processId", processId);
		result.setItems(items);
		result.setStatus(Constants.SUCCESS);
		logger.info("开启流程成功：流程id {},taskId {}", processId, taskId);
		return result;
	}

	/**
	 * 审批任务，并制定下一个节点审批人
	 * 
	 * @param workFlowVo
	 *            :其中WorkFlowParams传递了下一个节点的审批人、以及审批时分支判断条件参数
	 */
	@Override
	public ResponseVo complete(WorkFlowVo workFlowVo) {
		logger.info("流程审批开始：当前任务id：" + workFlowVo.getTaskId());
		ResponseVo result = new ResponseVo();
		Map<String, Object> items = new HashMap<String, Object>();
		result.setStatus(Constants.SUCCESS);
		String taskId = workFlowVo.getTaskId();
		if (null == taskId) {
			result.setStatus(Constants.FAIL);
			result.setMessage("taskId不能为空");
			return result;
		}
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (null == task) {
			result.setStatus(Constants.FAIL);
			result.setMessage("当前任务已经处理，taskid:" + taskId);
			return result;
		}
		String processId = task.getProcessInstanceId();
		// 审批
		taskService.complete(taskId, workFlowVo.getWorkFlowParams());// 查看act_ru_task表
		Task taskCurr = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		if (null == taskCurr) {
			result.setMessage("流程结束");
			result.setProccessStatus("0"); // 流程结束
			logger.info("流程审批结束：流程已经结束");
			return result;
		}
		String taskIdNext = taskCurr.getId();
		items.put("processId", processId);
		items.put("taskId", taskIdNext);
		result.setItems(items);
		logger.info("流程审批结束：下一节点任务id：" + taskIdNext);
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
	 * 获取流程的当前待处理任务ACT_RU_TASK表 processId从act_hi_procint表中获取（proc_inst_id）
	 * 
	 * @Title: getAssignee
	 * @param processId
	 * @return String
	 */
	@Override
	public Map<String, Object> getCurrentRunTask(String processId) {
		Map<String, Object> items = new HashMap<String, Object>();
		Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
		// ACT_RU_TASK表中没有流程id对应的task，或task已经结束
		if (null != task) {
			items.put("taskId", task.getId());
			items.put("taskName", task.getName());
			items.put("taskRefKey", task.getTaskDefinitionKey());
			items.put("taskCreateTime", task.getCreateTime());
			items.put("taskAssignee", task.getAssignee());
			items.put("processId", task.getProcessInstanceId());
		}
		return items;
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
				map.put("taskRefKey", task.getTaskDefinitionKey());
				map.put("processId", task.getProcessInstanceId());
				taskd.add(map);
			}
		}
		return taskd;
	}

	/**
	 * 获取当前任务
	 */
	@Override
	public Map<String, Object> queryTaskById(String taskId) {
		Map<String, Object> items = new HashMap<String, Object>();
		Task task = taskService.createTaskQuery().taskTenantId(taskId).singleResult();
		if (null != task) {
			items.put("taskId", task.getId());
			items.put("taskName", task.getName());
			items.put("taskRefKey", task.getTaskDefinitionKey());
			items.put("taskCreateTime", task.getCreateTime());
			items.put("taskAssignee", task.getAssignee());
			items.put("processId", task.getProcessInstanceId());
		}
		return items;
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

}
