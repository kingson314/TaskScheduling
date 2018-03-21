package com.log.sche;

//调度日志Bean
public class LogSche {
	// 调度标志
	private String scheId;
	// 执行信息
	private String execMsg;
	// 开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	// 执行时间
	private Long execTime;

	public Long getExecTime() {
		return execTime;
	}

	public void setExecTime(Long execTime) {
		this.execTime = execTime;
	}

	public String getScheId() {
		return scheId;
	}

	public void setScheId(String scheId) {
		this.scheId = scheId;
	}

	public String getExecMsg() {
		return execMsg;
	}

	public void setExecMsg(String execMsg) {
		this.execMsg = execMsg;
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

}
