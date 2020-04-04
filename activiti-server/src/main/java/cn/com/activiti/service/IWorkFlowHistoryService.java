package cn.com.activiti.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

/**
 * 历史数据
 * 
 * @ClassName: IWorkFlowHistoryService
 * @author lwx393577：
 * @date 2020年4月4日 下午5:13:51
 *
 */
public interface IWorkFlowHistoryService {
	/**
	 * 当前流程处理历史任务信息
	 * 
	 * @Title: queryHistorTask
	 * @Description: TODO
	 * @param processInstanceId
	 * @return
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> queryHistoryTask(String processInstanceId);

	/**
	 * 当前流程处理历史流程信息
	 * 
	 * @Title: queryHistoryAcitivit
	 * @Description: TODO
	 * @param processInstanceId
	 * @return
	 * @return List<HistoricActivityInstance>
	 */
	public List<Map<String, Object>> queryHistoryAcitivit(String processInstanceId);

	/**
	 * 我参议审批的流程信息
	 * 
	 * @Title: myApprovalRecord
	 * @Description: TODO
	 * @param userName
	 * @return List<Activiti>
	 */
	public List<Map<String, Object>> myApprovalRecord(String userName);

	/**
	 * 用户发起的流程信息
	 * 
	 * @Title: myVacRecord
	 * @Description: TODO
	 * @param userNamemyActivitiRecord
	 * @param processDefinitionKey
	 *            流程图定义的id
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> myActivitiRecord(String userName, String processDefinitionKey);

	/**
	 * 通过流程id 查询流程
	 *
	 * @param processId
	 * @return
	 */
	HistoricProcessInstanceEntity queryProcessInstance(String processInstanceId);

}
