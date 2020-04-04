package cn.com.activiti.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;

import cn.com.activiti.entity.Activiti;
import cn.com.activiti.service.IWorkFlowService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("workflow")
@Api("WorkFlowController相关的api")
public class WorkFlowController {
	private Logger logger = LoggerFactory.getLogger(WorkFlowController.class);

	@Autowired
	private IWorkFlowService workFlowService;

	/**
	 * 开启流程
	 * 
	 * @Title: startProcess
	 * @Description: TODO
	 * @param activiti
	 * @return
	 * @throws Exception
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/start
	// {"processDefinitionKey":"myProcessId"}
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public Map<String, Object> startProcess(@RequestBody Activiti activiti) throws Exception {
		return workFlowService.startProcess(activiti.getProcessDefinitionKey(), activiti);

	}

	/**
	 * 任务审批：包括设置下一个节点处理人
	 * 
	 * @Title: complete
	 * @Description: TODO
	 * @param activiti
	 * @throws Exception
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/complete
	// {"taskId":"10010","pass":"ok","nextAssign":"liurz"}
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public Map<String, Object> complete(@RequestBody Activiti activiti) throws Exception {

		return workFlowService.complete(activiti.getTaskId(), activiti);

	}

	/**
	 * 任务认领（就是设置当前任务的处理人）
	 * 
	 * @Title: claim
	 * @Description: TODO
	 * @param activiti
	 * @return
	 * @throws Exception
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/claim
	@RequestMapping(value = "/claim", method = RequestMethod.POST)
	public Map<String, Object> claim(@RequestBody Activiti activiti) throws Exception {
		Map<String, Object> resRe = new HashMap<String, Object>();
		try {
			if (StringUtils.isEmpty(activiti.getTaskId()) || StringUtils.isEmpty(activiti.getCurrentHandler())) {
				resRe.put("status", "fail");
				resRe.put("message", "taskId和认领人不能为空");
				logger.info("taskId和认领人不能为空");
				return resRe;
			}
			logger.info("任务认领开始,taskId:" + activiti.getTaskId() + "--claim user:" + activiti.getCurrentHandler());
			workFlowService.claim(activiti.getTaskId(), activiti.getCurrentHandler());
			resRe.put("status", "success");
			resRe.put("message", "任务认领成功");
			logger.info("任务认领成功");
		} catch (Exception e) {
			resRe.put("status", "fail");
			resRe.put("message", "任务认领失败");
			logger.error("任务认领失败", e);
		}
		return resRe;
	}

	/**
	 * 任务转移：就是重新设任务处理人
	 * 
	 * @Title: transferTask
	 * @Description: TODO
	 * @param taskId
	 * @param userId
	 * @return void
	 */
	// http://localhost:8080/activitServer/workflow/transferTask
	@RequestMapping(value = "/transferTask", method = RequestMethod.POST)
	public Map<String, Object> transferTask(@RequestBody Activiti activiti) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		try {
			if (StringUtils.isEmpty(activiti.getTaskId()) || StringUtils.isEmpty(activiti.getCurrentHandler())) {
				resRe.put("status", "fail");
				resRe.put("message", "taskId和认领人不能为空");
				logger.info("taskId和认领人不能为空");
				return resRe;
			}
			logger.info("任务转移开始,taskId:" + activiti.getTaskId() + "--transfer user:" + activiti.getCurrentHandler());
			workFlowService.setAssignee(activiti.getTaskId(), activiti.getCurrentHandler());
			resRe.put("status", "success");
			resRe.put("message", "任务转移成功");
			logger.info("任务认领成功");
		} catch (Exception e) {
			resRe.put("status", "fail");
			resRe.put("message", "任务转移失败");
			logger.error("任务转移失败", e);
		}
		return resRe;
	}

	/**
	 * 获取当前流程节点的处理人
	 * 
	 * @Title: getAssignee
	 * @Description: TODO
	 * @param processId
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/getAssignee?processId=5001
	@RequestMapping(value = "/getAssignee", method = RequestMethod.GET)
	public Map<String, Object> getAssignee(@RequestParam("processId") String processId) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		resRe.put("status", "success");
		resRe.put("assignee", workFlowService.getAssignee(processId));
		return resRe;
	}

	/**
	 * 代办任务--当前处理人的所有代办任务信息
	 * 
	 * @Title: getTasksByAssignee
	 * @Description: TODO
	 * @param assignee
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/getTasksByAssignee?assignee=liurz
	@RequestMapping(value = "/getTasksByAssignee", method = RequestMethod.GET)
	public Map<String, Object> getTasksByAssignee(@RequestParam("assignee") String assignee) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		resRe.put("status", "success");
		resRe.put("data", workFlowService.getTasksByAssignee(assignee));
		return resRe;
	}

	/**
	 * 用户的任务列表
	 * 
	 * @Title: myActiviti
	 * @Description: TODO
	 * @param userName
	 * @return
	 * @return List<Activiti>
	 */
	// http://localhost:8080/activitServer/workflow/myActiviti?userId=liurz
	@RequestMapping(value = "/myActiviti", method = RequestMethod.GET)
	public Map<String, Object> myActiviti(@RequestParam("userId") String userId) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		resRe.put("status", "success");
		resRe.put("data", workFlowService.myActiviti(userId));
		return resRe;
	}

	/**
	 * 删除流程（针对没有结束的流程）
	 * 
	 * @Title: deleteProcess
	 * @Description: TODO
	 * @param processInstanceId
	 * @return
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/deleteProcess
	// {"processId":"5001","reason":"test"}
	@RequestMapping(value = "/deleteProcess", method = RequestMethod.POST)
	public Map<String, Object> deleteProcess(@RequestBody Activiti activiti) {
		Map<String, Object> resRe = new HashMap<String, Object>();
		resRe.put("status", "success");
		try {
			if (StringUtils.isEmpty(activiti.getProcessId())) {
				resRe.put("status", "faile");
				resRe.put("message", "流程id不能为空");
				return resRe;
			}
			workFlowService.deleteProcessInstance(activiti.getProcessId(), activiti.getReason());
		} catch (Exception e) {
			resRe.put("status", "fail");
			resRe.put("message", "删除流程失败");
			logger.error("删除流程失败", e);
		}
		return resRe;
	}

}
