package cn.com.usercenter.activiti;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

public class ActivitiServiceImpl implements ActivitiService {
	private Logger logger = LoggerFactory.getLogger(ActivitiServiceImpl.class);
	/**
	 * 任务管理
	 */
	@Autowired
	private TaskService taskService;

	/**
	 * 历史管理（执行完的数据）
	 */
	@Autowired
	private HistoryService historyService;

	/**
	 * 管理流程定义
	 */
	@Autowired
	private RepositoryService repositoryService;

	/**
	 * 组织机构管理
	 */
	@Autowired
	private IdentityService identityService;

	@Autowired
	private RuntimeService runtimeService;

	private static final String PROCESS_DEFINE_KEY = "vacationProcess";

	@Override
	public boolean startProcess(String processDefinitionKey, Activiti vars) throws Exception {
		try {
			// 开始流程 这个是查看数据库中act_re_procdef表
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
			String processId = processInstance.getId();
			// 查询当前任务
			Task currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			String taskId1 = currentTask.getId();
			// 申明任务人，默认处理人为申请人
			// taskService.claim(currentTask.getId(), userName);
			taskService.setAssignee(taskId1, vars.getApplyUser());
			Map<String, Object> params = new HashMap<String, Object>(4);
			params.put("applyUser", vars.getApplyUser());
			params.put("days", vars.getDays());
			params.put("reason", vars.getReason());
			// 在此方法中，Vaction 是申请时的具体信息，在完成“申请请假”任务时，可以将这些信息设置成参数。
			// 完成第一步申请
			taskService.complete(currentTask.getId(), params);

			// 到了下一个任务， 应该在此处指派任务由谁来处理
			// 重新获取当前任务
			Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
			String taskId2 = task.getId();
			// 设置节点处理人
			taskService.setAssignee(taskId2, vars.getNextAssign());
			logger.error("开启流程成功：当前任务id：" + taskId2 + "节点任务处理人：" + vars.getNextAssign());
		} catch (Exception e) {
			logger.error("开启流程失败：" + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void complete(String taskId, Map<String, Object> paramsMap) {

		taskService.complete(taskId, paramsMap);// 查看act_ru_task表
	}

	@Override
	public void complete(String taskId) {
		// TODO Auto-generated method stub
		taskService.complete(taskId);// 查看act_ru_task表
	}

	@Override
	public Task queryTaskByProcessId(String processInstanceId) {
		// TODO Auto-generated method stub
		return taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
	}

	@Override
	public Task queryTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskService.createTaskQuery().taskTenantId(taskId).singleResult();
	}

	@Override
	public void addCandidateGroup(String taskId, String groupId) {
		taskService.addCandidateGroup(taskId, groupId);

	}

	@Override
	public void claim(String taskId, String userId) {
		taskService.claim(taskId, userId);
	}

	@Override
	public void setAuthUser(String userId) {

	}

	@Override
	public byte[] definitionImage(String processDefinitionId) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getProcessImage(String pProcessInstanceId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryVariables(String taskId, String varName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoricProcessInstanceEntity queryProcessInstance(String processId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteProcessInstance(String processInstanceId, String deleteReason) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Deployment deploy(String fileName, String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDeployment(String deploymentId) {
		// TODO Auto-generated method stub

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

	@Override
	public List<Activiti> myActivitiRecord(String userName) {
		List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(PROCESS_DEFINE_KEY).startedBy(userName).finished().orderByProcessInstanceEndTime()
				.desc().list();

		List<Activiti> activitiList = new ArrayList<>();
		for (HistoricProcessInstance hisInstance : hisProInstance) {
			Activiti activiti = new Activiti();
			activiti.setApplyUser(hisInstance.getStartUserId());
			activiti.setApplyTime(hisInstance.getStartTime());
			activiti.setApplyStatus("申请结束");
			List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(hisInstance.getId()).list();
			setVars(activiti, varInstanceList);
			activitiList.add(activiti);
		}
		return activitiList;
	}

	@Override
	public List<Activiti> myApprovalRecord(String userName) {
		List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(PROCESS_DEFINE_KEY).involvedUser(userName).finished()
				.orderByProcessInstanceEndTime().desc().list();

		List<String> auditTaskNameList = new ArrayList<>();
		auditTaskNameList.add("经理审批");
		auditTaskNameList.add("总监审批");
		List<Activiti> activitiList = new ArrayList<>();
		for (HistoricProcessInstance hisInstance : hisProInstance) {

			Activiti activiti = new Activiti();
			activiti.setApplyUser(hisInstance.getStartUserId());
			activiti.setApplyStatus("申请结束");
			activiti.setApplyTime(hisInstance.getStartTime());
			List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(hisInstance.getId()).list();
			setVars(activiti, varInstanceList);
			activitiList.add(activiti);
		}
		return activitiList;
	}

	@Override
	public void setAssignee(String taskId, String userId) {

		taskService.setAssignee(taskId, userId);
	}

	@Override
	public void setOwner(String taskId, String userId) {

		taskService.setOwner(taskId, userId);

	}

	@Override
	public Boolean passApproval(String userName, ActivitiTask activitiTask) {
		// 节点处理人
		taskService.setAssignee(activitiTask.getTaskId(), userName);
		// 审批
		taskService.complete(activitiTask.getTaskId());
		return true;
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
	 * 将历史参数列表设置到实体中去
	 * 
	 * @param entity
	 *            实体
	 * @param varInstanceList
	 *            历史参数列表
	 */
	public static <T> void setVars(T entity, List<HistoricVariableInstance> varInstanceList) {
		Class<?> tClass = entity.getClass();
		try {
			for (HistoricVariableInstance varInstance : varInstanceList) {
				Field field = tClass.getDeclaredField(varInstance.getVariableName());
				if (field == null) {
					continue;
				}
				field.setAccessible(true);
				field.set(entity, varInstance.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * dfasdfsa
	 * 
	 * @Title: main
	 * @Description: TODO
	 * @return void
	 */
	public void main() {

	}

	public void test() {
		System.out.println("ddsafs");
		Integer d = 1;
		Integer h = d * 300;
		System.out.println(h);

	}
}
