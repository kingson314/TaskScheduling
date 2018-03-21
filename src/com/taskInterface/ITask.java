package com.taskInterface;

//任务接口
public interface ITask {
	public Object clone();

	public String getTaskOrder();

	public void setTaskOrder(String taskOrder);

	public String getNowDate();

	public void setNowDate(String nowDate);

	public Long getTaskId();

	public void setTaskId(Long taskId);

	public String getTaskName();

	public void setTaskName(String taskName);

	public String getTaskType();

	public void setTaskType(String tasktype);

	public String getTaskMemo();

	public void setTaskMemo(String taskMemo);

	public String getTaskStatus();

	public void setTaskStatus(String taskStatus);

	public String getTaskMsg();

	public void setTaskMsg(String taskFailMsg);

	public void setTaskMsg(String taskFailMsg, Exception e);

	public Long getInterval();

	public void setInterval(Long interval);

	public String getLogType();

	public void setLogType(String logType);

	public String getExecStatus();

	public void setExecStatus(String execStatus);

	public Long getOverTime();

	public void setOverTime(Long overTime);

	public String getMonitorGroup();

	public void setMonitorGroup(String monitorGroup);

	public String getWarnType();

	public void setWarnType(String warnType);

	public String getJsonStr();

	public String getParserJsonStr() throws Exception;

	public void setJsonStr(String jsonStr);

	public void fireTask(final String startTime, final String groupId,
			final String scheCod, final String taskOrder);// 任务中多线程时用到

	public void manuExecTask(String[] params);

}
