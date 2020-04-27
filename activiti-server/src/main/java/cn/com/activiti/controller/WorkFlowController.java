package cn.com.activiti.controller;

import java.util.List;
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

import cn.com.activiti.entity.WorkFlowVo;
import cn.com.activiti.service.IWorkFlowService;
import cn.com.activiti.util.Constants;
import cn.com.activiti.util.ResponseVo;
import io.swagger.annotations.Api;

/**
 * 流程处理
 * 
 * @ClassName: WorkFlowController
 * @Description: TODO
 * @author lwx393577：
 * @date 2020年4月26日 下午9:49:33
 *
 */
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
	 * @param WorkFlowVo
	 * @return
	 * @throws Exception
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/start
	// 参数：{"processDefinitionKey":"resouresApply","workFlowParams":{"approve":"liurz"}}
	// 返回值：{"status": "success","message": null,"errorDetail": null, "items":
	// {"processId": "22508", "taskId": "22513" }}
	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public ResponseVo startProcess(@RequestBody WorkFlowVo workFlowVo) throws Exception {
		ResponseVo result = new ResponseVo();
		try {
			return workFlowService.startProcess(workFlowVo);
		} catch (Exception e) {
			logger.error("开启流程失败：" + e);
			result.setErrorDetail(e.getMessage());
			result.setMessage("开启流程失败");
			result.setStatus(Constants.FAIL);
			return result;
		}
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
	// 参数：{"taskId":"22516","workFlowParams":{"approve":"liurz"}}
	// 返回值：{"status":"success","message":null,"errorDetail":null,"items":{"processId":"22508","taskId":"22516"}}
	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	public ResponseVo complete(@RequestBody WorkFlowVo workFlowVo) throws Exception {
		ResponseVo result = new ResponseVo();
		try {
			return workFlowService.complete(workFlowVo);
		} catch (Exception e) {
			logger.error("任务审批失败：" + e);
			result.setErrorDetail(e.getMessage());
			result.setMessage("任务审批失败");
			result.setStatus(Constants.FAIL);
			return result;
		}

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
	public ResponseVo claim(@RequestBody WorkFlowVo workFlowVo) throws Exception {
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(workFlowVo.getTaskId()) || StringUtils.isEmpty(workFlowVo.getClaimant())) {
				logger.info("taskId和认领人不能为空");
				result.setMessage("taskId或认领人不能为空");
				result.setStatus(Constants.FAIL);
				return result;
			}
			logger.info("任务认领开始,taskId:" + workFlowVo.getTaskId() + "--claim user:" + workFlowVo.getClaimant());
			workFlowService.claim(workFlowVo.getTaskId(), workFlowVo.getClaimant());
			result.setMessage("任务认领成功");
			result.setStatus(Constants.SUCCESS);
			logger.info("任务认领成功");
			return result;
		} catch (Exception e) {
			result.setMessage("任务认领失败");
			result.setErrorDetail(e.getMessage());
			result.setStatus(Constants.FAIL);
			logger.error("任务认领失败", e);
			return result;
		}
	}

	/**
	 * 任务转移(所谓的转单)：就是重新设任务处理人
	 * 
	 * @Title: transferTask
	 * @Description: TODO
	 * @param taskId
	 * @param userId
	 * @return void
	 */
	// http://localhost:8080/activitServer/workflow/transferTask
	@RequestMapping(value = "/transferTask", method = RequestMethod.POST)
	public ResponseVo transferTask(@RequestBody WorkFlowVo workFlowVo) {
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(workFlowVo.getTaskId()) || StringUtils.isEmpty(workFlowVo.getAssginne())) {
				result.setMessage("taskId或转移人不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("taskId或转移人不能为空");
				return result;
			}
			logger.info("任务转移开始,taskId:" + workFlowVo.getTaskId() + "--transfer user:" + workFlowVo.getAssginne());
			workFlowService.setAssignee(workFlowVo.getTaskId(), workFlowVo.getAssginne());
			result.setMessage("任务转移成功");
			result.setStatus(Constants.SUCCESS);
			logger.info("任务认领成功");
		} catch (Exception e) {
			result.setMessage("任务转移失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("任务转移失败", e);
		}
		return result;
	}

	/**
	 * 获取流程当前节点的任务信息
	 * 
	 * @Title: getAssignee
	 * @Description: TODO
	 * @param processId
	 * @return Map<String,Object>
	 */
	// http://localhost:8080/activitServer/workflow/getCurrentRunTask?processId=5001
	@RequestMapping(value = "/getCurrentRunTask", method = RequestMethod.GET)
	public ResponseVo getCurrentRunTask(@RequestParam("processId") String processId) {
		logger.info("---proccessId--", processId);
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(processId)) {
				result.setMessage("流程id不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("t流程id不能为空");
				return result;
			}
			Map<String, Object> items = workFlowService.getCurrentRunTask(processId);
			result.setMessage("获取流程的当前任务信息");
			result.setStatus(Constants.SUCCESS);
			result.setItems(items);
			logger.info("获取流程的当前任务信息成功");
		} catch (Exception e) {
			result.setMessage("获取流程的当前任务信息失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("获取流程的当前任务信息失败", e);
		}
		return result;
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
	public ResponseVo getTasksByAssignee(@RequestParam("assignee") String assignee) {
		logger.info("===getTasksByAssignee start,assignee {} ", assignee);
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(assignee)) {
				result.setMessage("当前处理人不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("当前处理人不能为空");
				return result;
			}
			List<Map<String, String>> taskes = workFlowService.getTasksByAssignee(assignee);
			result.setMessage("代办任务获取成功");
			result.setStatus(Constants.SUCCESS);
			result.setItems(taskes);
			logger.info("代办任务获取成功");
		} catch (Exception e) {
			result.setMessage("代办任务获取失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("代办任务获取失败", e);
		}
		return result;
	}

	/**
	 * 获取任务信息
	 * 
	 * @Title: queryTaskById
	 * @Description: TODO
	 * @param taskId
	 * @return
	 * @return ResponseVo
	 */
	@RequestMapping(value = "/queryTaskById", method = RequestMethod.GET)
	public ResponseVo queryTaskById(@RequestParam("taskId") String taskId) {
		logger.info("===queryTaskById start,taskId {} ", taskId);
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(taskId)) {
				result.setMessage("taskId不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("taskId不能为空");
				return result;
			}
			Map<String, Object> taske = workFlowService.queryTaskById(taskId);
			result.setMessage("获取任务信息成功");
			result.setStatus(Constants.SUCCESS);
			result.setItems(taske);
			logger.info("获取任务信息成功");
		} catch (Exception e) {
			result.setMessage("获取任务信息失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("获取任务信息失败", e);
		}
		return result;
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
	// {"processId":"5001","remark":"删除原因"}
	@RequestMapping(value = "/deleteProcess", method = RequestMethod.POST)
	public ResponseVo deleteProcess(@RequestBody WorkFlowVo workFlowVo) {
		logger.info("===deleteProcess,processId:{} ", workFlowVo.getProcessId());
		ResponseVo result = new ResponseVo();
		try {
			if (StringUtils.isEmpty(workFlowVo.getProcessId())) {
				result.setMessage("流程Id不能为空");
				result.setStatus(Constants.FAIL);
				logger.info("流程Id不能为空");
				return result;
			}
			workFlowService.deleteProcessInstance(workFlowVo.getProcessId(), workFlowVo.getRemark());
			result.setMessage("删除流程成功");
			result.setStatus(Constants.SUCCESS);
			logger.info("删除流程成功");
		} catch (Exception e) {
			result.setMessage("删除流程失败");
			result.setStatus(Constants.FAIL);
			result.setErrorDetail(e.getMessage());
			logger.error("删除流程失败", e);
		}
		return result;
	}

}
