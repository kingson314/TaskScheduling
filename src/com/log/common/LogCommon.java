package com.log.common;

//执行日志Bean
public class LogCommon {
	// 调度标志
	private String ScheID;
	// 任务标志
	private String taskId;
	// 任务组标志
	private String groupId;
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 执行状态
	private String taskStatus;
	// 执行结果
	private String taskFailMsg;
	// 是否发送短信
	private String ifSendMsg;

	public String getIfSendMsg() {
		return ifSendMsg;
	}

	public void setIfSendMsg(String ifSendMsg) {
		this.ifSendMsg = ifSendMsg;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getScheID() {
		return ScheID;
	}

	public void setScheID(String scheID) {
		ScheID = scheID;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskFailMsg() {
		return taskFailMsg;
	}

	public void setTaskFailMsg(String taskFailMsg) {
		this.taskFailMsg = taskFailMsg;
	}
}