package com.log.abridgement;

//日志摘要类Bean
public class LogAbridgement {
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

	// 执行成功的次数
	private Long successTimes;

	// 执行失败的次数
	private Long failedTimes;
	// 执行报警次数
	private Long warnedTimes;

	// 总执行次数
	private Long totalTimes;

	// 总执行时间值
	private Long totalExecTime;

	// 最大执行时间值
	private Long maxExecTime;

	// 最小执行时间值
	private Long minExecTime;

	// 平均执行时间值
	private double avgExecTime;

	// 上次执行时间
	private String LastExceTime;

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

	public Long getSuccessTimes() {
		return successTimes;
	}

	public void setSuccessTimes(Long successTimes) {
		this.successTimes = successTimes;
	}

	public Long getFailedTimes() {
		return failedTimes;
	}

	public void setFailedTimes(Long failedTimes) {
		this.failedTimes = failedTimes;
	}

	public Long getWarnedTimes() {
		return warnedTimes;
	}

	public void setWarnedTimes(Long warnedTimes) {
		this.warnedTimes = warnedTimes;
	}

	public Long getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(Long totalTimes) {
		this.totalTimes = totalTimes;
	}

	public Long getMaxExecTime() {
		return maxExecTime;
	}

	public void setMaxExecTime(Long maxExecTime) {
		this.maxExecTime = maxExecTime;
	}

	public Long getMinExecTime() {
		return minExecTime;
	}

	public void setMinExecTime(Long minExecTime) {
		this.minExecTime = minExecTime;
	}

	public double getAvgExecTime() {
		return avgExecTime;
	}

	public void setAvgExecTime(double avgExecTime) {
		this.avgExecTime = avgExecTime;
	}

	public String getLastExceTime() {
		return LastExceTime;
	}

	public void setLastExceTime(String lastExceTime) {
		LastExceTime = lastExceTime;
	}

	public Long getTotalExecTime() {
		return totalExecTime;
	}

	public void setTotalExecTime(Long totalExecTime) {
		this.totalExecTime = totalExecTime;
	}

}
