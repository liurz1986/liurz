package cn.com.usercenter.activiti;

import java.util.Date;

public class Activiti {

	private Integer days;// 时间

	private String reason;// 申请流程的理由

	private String applyUser;// 流程的申请人

	private String nextAssign;// 下一个节点处理人

	private Date applyTime;// 申请的时间

	private String applyStatus;// 申请的状态

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	public String getNextAssign() {
		return nextAssign;
	}

	public void setNextAssign(String nextAssign) {
		this.nextAssign = nextAssign;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getApplyStatus() {
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}

}
