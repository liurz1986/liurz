package cn.com.activiti.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.activiti.service.IWorkFlowHistoryService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("workflowhis")
@Api("WorkFlowHistoryController相关的api")
public class WorkFlowHistoryController {
	private Logger logger = LoggerFactory.getLogger(WorkFlowHistoryController.class);
	@Autowired
	private IWorkFlowHistoryService workFlowHistoryService;

	/**
	 * 我审批过的流程已经结束的记录
	 * 
	 * @Title: myApprovalRecord
	 * @Description: TODO
	 * @param userName
	 * @param processDefinitionKey
	 * @return List<Activiti>
	 */
	// http://localhost:8080/activitServer/workflowhis/myApprovalRecord?assignee=liurz
	@RequestMapping(value = "/myApprovalRecord", method = RequestMethod.GET)
	public Map<String, Object> myApprovalRecord(@RequestParam("assignee") String assignee) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		resRe.put("status", "success");
		resRe.put("data", workFlowHistoryService.myApprovalRecord(assignee));
		return resRe;
	}

	/**
	 * 查询流程已经处理的节点信息根据流程id 涉及到的表 ：ACT_HI_TASKINST
	 * 
	 * @Title: queryHistorTask
	 * @Description: TODO
	 * @param processInstanceId
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflowhis/queryHistorTask?processInstanceId=5001
	@RequestMapping(value = "/queryHistorTask", method = RequestMethod.GET)
	public Map<String, Object> queryHistorTask(@RequestParam("processInstanceId") String processInstanceId) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		resRe.put("status", "success");
		resRe.put("data", workFlowHistoryService.queryHistoryTask(processInstanceId));
		return resRe;
	}

	/**
	 * 查询历史流程信息根据流程id 涉及到的表 ：ACT_HI_ACTINST
	 * 
	 * @Title: queryHistoryAcitivit
	 * @Description: TODO
	 * @param processInstanceId
	 * @return
	 * @return List<Map<String,Object>>
	 */
	// http://localhost:8080/activitServer/workflowhis/queryHistoryAcitivit?processInstanceId=5001
	@RequestMapping(value = "/queryHistoryAcitivit", method = RequestMethod.GET)
	public Map<String, Object> queryHistoryAcitivit(@RequestParam("processInstanceId") String processInstanceId) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		resRe.put("status", "success");
		resRe.put("data", workFlowHistoryService.queryHistoryAcitivit(processInstanceId));
		return resRe;
	}

	/**
	 * 用户发起的流程信息
	 * 
	 * @Title: myActivitiRecord
	 * @Description: TODO
	 * @param userName
	 * @param processDefinitionKey
	 * @return
	 * @return Map<String,Object>
	 */
	@RequestMapping(value = "/myActivitiRecord", method = RequestMethod.GET)
	public Map<String, Object> myActivitiRecord(@RequestParam("userName") String userName,
			@RequestParam("processDefinitionKey") String processDefinitionKey) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		resRe.put("status", "success");
		resRe.put("data", workFlowHistoryService.myActivitiRecord(userName, processDefinitionKey));
		return resRe;
	}
}
