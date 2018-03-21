package com.log.lately;

//最近日志Bean
public class LogLately {
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
	private String exceStatus;
	// 执行结果
	private String exceResult;
	// 执行时间
	private Long exceTime;

	public String getScheID() {
		return ScheID;
	}

	public void setScheID(String scheID) {
		ScheID = scheID;
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

	public String getExceStatus() {
		return exceStatus;
	}

	public void setExceStatus(String exceStatus) {
		this.exceStatus = exceStatus;
	}

	public String getExceResult() {
		return exceResult;
	}

	public void setExceResult(String exceResult) {
		this.exceResult = exceResult;
	}

	public Long getExceTime() {
		return exceTime;
	}

	public void setExceTime(Long exceTime) {
		this.exceTime = exceTime;
	}

}
