package com.taskInterface;


import com.app.Parser;
import com.log.Log;

import common.util.conver.UtilConver;
import consts.Const;
import consts.VariableApp;

public abstract class TaskAbstract implements ITask, Cloneable {

	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			Log.logError("复制对象错误:", e);
		}
		return o;
	};

	// 任务ID
	private long taskID;
	// 任务名称
	private String taskName;
	// 任务类型
	private String taskType;
	// 任务备注
	private String taskMemo;
	// 任务顺序号
	private String taskOrder;
	// 任务当前执行日期
	private String nowDate;
	// 任务状态
	private String taskStatus;
	// 任务执行结果
	private String taskMsg;
	// 报警间隔时间(秒)
	private Long Interval;
	// 日志类型
	private String logType;
	// 执行状态
	private String execStatus;
	// 任务超时阀值
	private Long overTime;
	// 监控组
	private String monitorGroup;
	// 警告类型
	private String warnType;
	// 任务明细 json字符串
	private String jsonStr;

	public TaskAbstract() {
		this.setTaskStatus("等待执行");
		this.setTaskMsg("");
	}

	public String getTaskOrder() {
		return taskOrder;
	}

	public void setTaskOrder(String taskOrder) {
		this.taskOrder = taskOrder;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public Long getTaskId() {
		return this.taskID;
	}

	public void setTaskId(Long taskId) {
		this.taskID = taskId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskMemo() {
		return taskMemo;
	}

	public void setTaskMemo(String taskMemo) {
		this.taskMemo = taskMemo;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;

	}

	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskMsg() {
		return this.taskMsg;
	}

	public void setTaskMsg(String taskMsg) {
		this.taskMsg = taskMsg;
	}

	// 根据异常设置执行结果(常用)
	public void setTaskMsg(String taskFailMsg, Exception e) {
		this.taskMsg = taskFailMsg + Log.getStrackTrace(e, VariableApp.systemParamsValue.getLogLevel());
	}

	public Long getInterval() {
		return Interval;
	}

	public void setInterval(Long interval) {
		Interval = interval;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getExecStatus() {
		return execStatus;
	}

	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}

	public Long getOverTime() {
		return overTime;
	}

	public void setOverTime(Long overTime) {
		this.overTime = overTime;
	}

	public String getMonitorGroup() {
		return monitorGroup;
	}

	public void setMonitorGroup(String monitorGroup) {
		this.monitorGroup = monitorGroup;
	}

	public String getWarnType() {
		return warnType;
	}

	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	// 获取带有宏定义日期反编译后的json字符串
	public String getParserJsonStr() throws Exception {
		String rs = "";
		java.util.Date dDate = null;
		try {
			if (this.getNowDate() == null) {
				rs = Parser.parse(jsonStr, new java.util.Date());
			} else if (this.getNowDate().equals("")) {
				rs = Parser.parse(jsonStr, new java.util.Date());
			} else {
				dDate = UtilConver.strToDate(this.getNowDate(), Const.fm_yyyyMMdd);
				rs = Parser.parse(jsonStr, dDate);
			}
		} catch (Exception e) {
			Log.logError("任务名称:" + this.getTaskName() + " --  " + this.getNowDate() + "  ===  " + dDate + "\n", e);
			throw e;
		}
		return rs;
	}

	public java.util.Date getDate() throws Exception {
		java.util.Date rs = null;
		try {
			if (this.getNowDate() == null) {
				rs = new java.util.Date();
			} else if (this.getNowDate().equals("")) {
				rs = new java.util.Date();
			} else {
				rs = UtilConver.strToDate(this.getNowDate(), Const.fm_yyyyMMdd);

			}
		} catch (Exception e) {
			Log.logError("任务名称:" + this.getTaskName() + " --  " + this.getNowDate() + "  ===  " + rs + "\n", e);
			throw e;
		}
		return rs;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	// 手工执行任务
	public void manuExecTask(String[] params) {
	}
}
