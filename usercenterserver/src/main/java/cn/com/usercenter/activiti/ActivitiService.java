package cn.com.usercenter.activiti;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

public interface ActivitiService {
	/**
	 * 启动流程
	 *
	 * @param processDefinitionKey
	 *            流程定义id
	 * 
	 * @param paramsMap
	 *            参数
	 * 
	 * @return
	 */
	boolean startProcess(String processDefinitionKey, Activiti activiti) throws Exception;

	/**
	 * 完成任务
	 *
	 * @param taskId
	 *            任务id
	 * @param paramsMap
	 *            任务携带变量
	 */
	void complete(String taskId, Map<String, Object> paramsMap);

	/**
	 * 审批
	 * 
	 * @Title: passApproval
	 * @Description: TODO
	 * @param userName
	 * @param activitiTask
	 * @return
	 * @return Boolean
	 */
	public Boolean passApproval(String userName, ActivitiTask activitiTask);

	/**
	 * 完成任务
	 *
	 * @param taskId
	 */
	public void complete(String taskId);

	/**
	 * 通过流程id 查询任务
	 *
	 * @param processInstanceId
	 * @return
	 */
	Task queryTaskByProcessId(String processInstanceId);

	/**
	 * 通过任务id，查询任务信息
	 *
	 * @param taskId
	 * @return
	 */
	Task queryTaskById(String taskId);

	/**
	 * 设置任务认领组
	 *
	 * @param taskId
	 * @param groupId
	 */
	void addCandidateGroup(String taskId, String groupId);

	/**
	 * 任务认领
	 * 
	 * @Title: setAssignee
	 * @Description: TODO
	 * @param taskId
	 * @param userId
	 * @return void
	 */
	void setAssignee(String taskId, String userId);

	/**
	 * 认领任务:setAssignee和claim两个的区别是在认领任务时，claim会检查该任务是否已经被认领，
	 * 如果被认领则会抛出ActivitiTaskAlreadyClaimedException
	 * ,而setAssignee不会进行这样的检查，其他方面两个方法效果一致。
	 * setOwner是在代理任务时使用，代表着任务的归属者，而这时，setAssignee代表的是代理办理者，
	 * 
	 * @param taskId
	 *            任务id
	 * @param userId
	 *            认领人id
	 */
	void claim(String taskId, String userId);

	/**
	 * setOwner是在代理任务时使用，代表着任务的归属者
	 * 
	 * @Title: setOwner
	 * @Description: TODO
	 * @param taskId
	 * @param userId
	 * @return void
	 */
	void setOwner(String taskId, String userId);

	/**
	 * 设置认证用户，用于定义流程启动人
	 *
	 * @param userId
	 */
	void setAuthUser(String userId);

	/**
	 * 查看定义的流程图
	 *
	 * @param processDefinitionId
	 * @return
	 */
	byte[] definitionImage(String processDefinitionId) throws IOException;

	/**
	 * 查看流程进度图
	 *
	 * @param pProcessInstanceId
	 * @return
	 * @throws Exception
	 */
	byte[] getProcessImage(String pProcessInstanceId) throws Exception;

	/**
	 * 通过任务和变量名称获取变量
	 *
	 * @param taskId
	 * @param varName
	 * @return
	 */
	public Object queryVariables(String taskId, String varName);

	/**
	 * 通过流程id 查询流程
	 *
	 * @param processId
	 * @return
	 */
	HistoricProcessInstanceEntity queryProcessInstance(String processId);

	/**
	 * 删除流程
	 *
	 * @param processInstanceId
	 * @param deleteReason
	 * @return
	 * @throws Exception
	 */
	void deleteProcessInstance(String processInstanceId, String deleteReason) throws Exception;

	/**
	 * 流程部署 此方法为手动部署，传入/resource/processes目录下的流程问卷名称即可
	 *
	 * @param fileName
	 *            文件名
	 * @param category
	 *            流程分类
	 */
	Deployment deploy(String fileName, String category);

	/**
	 * 删除部署的流程，级联删除流程实例
	 *
	 * @param deploymentId
	 *            流程部署ID
	 */
	void deleteDeployment(String deploymentId);

	/**
	 * 流程部署列表
	 *
	 * @return
	 */
	List<Deployment> deployList();

	/**
	 * 获取部署流程列表
	 *
	 * @return
	 */
	List<ProcessDefinition> getProcessList() throws Exception;

	/**
	 * 挂起流程
	 *
	 * @param processDefinitionId
	 * @throws Exception
	 */
	void suspendProcess(String processDefinitionId) throws Exception;

	/**
	 * 激活流程
	 *
	 * @param processDefinitionId
	 * @throws Exception
	 */
	void activateProcess(String processDefinitionId) throws Exception;

	/**
	 * 任务回退
	 *
	 * @param taskId
	 *            当前任务id
	 * @param userId
	 *            用户id
	 * @param reason
	 *            理由
	 * @param groupId
	 *            分组id
	 * @param backNum
	 *            退回数
	 * @throws Exception
	 */
	public void rollBackTask(String taskId, String userId, String reason, String groupId, int backNum) throws Exception;

	/**
	 * 查询需要自己审批的任务
	 * 
	 * @Title: myAudit
	 * @Description: TODO
	 * @param userName
	 * @return
	 * @return List<Task>
	 */
	public List<ActivitiTask> myApproval(String userName);

	/**
	 * 查询已完成的请假记录
	 * 
	 * @Title: myVacRecord
	 * @Description: TODO
	 * @param userName
	 * @return
	 * @return List<Task>
	 */
	public List<Activiti> myActivitiRecord(String userName);

	/**
	 * 我审批的记录列表
	 * 
	 * @Title: myApprovalRecord
	 * @Description: TODO
	 * @param userName
	 * @return
	 * @return List<Activiti>
	 */
	public List<Activiti> myApprovalRecord(String userName);

	/**
	 * 
	 * @Title: myActiviti
	 * @Description: TODO
	 * @param userName
	 * @return
	 * @return List<Activiti>
	 */
	public List<Activiti> myActiviti(String userName);
}
