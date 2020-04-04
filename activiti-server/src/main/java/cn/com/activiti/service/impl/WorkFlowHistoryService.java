package cn.com.activiti.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.activiti.service.IWorkFlowHistoryService;

@Service
public class WorkFlowHistoryService implements IWorkFlowHistoryService {
	/**
	 * 历史管理（执行完的数据）
	 */
	@Autowired
	private HistoryService historyService;

	/**
	 * 查询历史任务根据流程id 涉及到的表 ：ACT_HI_TASKINST
	 * 
	 * @Title: queryHistorTask
	 * @Description: TODO
	 * @param processInstanceId
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> queryHistoryTask(String processInstanceId) {
		List<Map<String, Object>> ress = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).orderByTaskCreateTime().asc().list();
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			map = new HashMap<String, Object>();
			map.put("taskName", historicTaskInstance.getName());
			map.put("startTime", historicTaskInstance.getStartTime());
			map.put("endTime", historicTaskInstance.getEndTime());
			map.put("taskId", historicTaskInstance.getId());
			ress.add(map);
		}
		return ress;
	}

	/**
	 * 查询历史流程信息根据流程id 涉及到的表 ：ACT_HI_ACTINST
	 */
	@Override
	public List<Map<String, Object>> queryHistoryAcitivit(String processInstanceId) {
		List<Map<String, Object>> ress = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			map = new HashMap<String, Object>();
			map.put("taskId", historicActivityInstance.getTaskId());
			map.put("startTime", historicActivityInstance.getStartTime());
			map.put("endTime", historicActivityInstance.getEndTime());
			map.put("assignee", historicActivityInstance.getAssignee());
			map.put("actId", historicActivityInstance.getActivityId());
			ress.add(map);
		}
		return ress;
	}

	/**
	 * 用户发起的流程信息
	 * 
	 * @Title: myActivitiRecord
	 * @Description: TODO
	 * @param userName
	 *            当前用户
	 * @param processDefinitionKey
	 *            流程图中流程的i
	 * @return
	 * @return List<Activiti>
	 */
	@Override
	public List<Map<String, Object>> myActivitiRecord(String userName, String processDefinitionKey) {
		List<Map<String, Object>> ress = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processDefinitionKey).startedBy(userName).orderByProcessInstanceEndTime().desc()
				.list();
		for (HistoricProcessInstance hisInstance : hisProInstance) {
			map = new HashMap<String, Object>();
			map.put("applyUser", hisInstance.getStartUserId());
			map.put("applyTime", hisInstance.getStartTime());
			map.put("processId", hisInstance.getProcessDefinitionId());
			map.put("processKey", hisInstance.getProcessDefinitionKey());
			ress.add(map);
		}
		return ress;
	}

	/**
	 * 我参议审批的流程信息（流程已经结束的情况）
	 * 
	 */
	@Override
	public List<Map<String, Object>> myApprovalRecord(String userName) {
		List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
				.involvedUser(userName).finished().orderByProcessInstanceStartTime().desc().list();

		List<Map<String, Object>> activitiList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (HistoricProcessInstance hisInstance : hisProInstance) {
			map = new HashMap<String, Object>();
			map.put("proccessId", hisInstance.getId());// 流程id
			map.put("startTime", hisInstance.getStartTime());// 开始时间
			map.put("endTime", hisInstance.getEndTime());// 结束书简
			activitiList.add(map);
		}
		return activitiList;
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

	@Override
	public HistoricProcessInstanceEntity queryProcessInstance(String processId) {

		return null;
	}

}
