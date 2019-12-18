package cn.com.activiti.entity;

import java.util.Date;

public class ActivitiTask {
	private String id;

	private String name;

	private Date createTime;

	private String taskId;

	private Activiti activiti;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Activiti getActiviti() {
		return activiti;
	}

	public void setActiviti(Activiti activiti) {
		this.activiti = activiti;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
