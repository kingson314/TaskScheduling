package com.scher;
/**
 * @author: fenggq
 * @Email:
 * @Company:
 * @Action: 任务调度
 * @DATE: Mar 8, 2012
 */
public class ScherParam {
	// 日期类型
	private String dateType;
	// 执行时段
	private String execTime;
	// 开始类型
	private String StartWhen;
	// 开始日期
	private String StartDate;
	// 结束类型结束日期
	private String EndDate;
	// 结束类型
	private String EndBy;
	// 每隔天数
	private String DailyN;
	// 每隔周数
	private String WeeklyN;
	// 选择的周组合
	private String Week;
	// 选择的月份组合
	private String month;
	// 每月的第N天或第几个星期N
	private String RecurMonthly;
	// 月的第N天
	private String MonthlyDay;
	// 选择频率类型
	private String RecurPrimary;
	// 调度标志
	private String schCde;
	// 调度名称
	private String schNme;
	// 调度类型
	private String schType;
	// 调度备注
	private String schComent;
	// 开始时间
	private String StartTime;
	// 结束时间
	private String EndTime;
	// 月的第N个星期N
	private String MonthlyDOW;
	// 月的第N个星期
	private String MonthlyNth;
	// 每N秒
	private String SecondN;
	// 每N分钟
	private String MinuteN;
	// 每N分钟的第N秒
	private String PerMinuteSecond;
	// 每N小时
	private String HourlyN;
	// 每N小时的第N分钟
	private String HourlyMinute;
	// 每N小时的第N分钟的第N秒
	private String HourlySecond;
	// 任务标志
	private String taskID;
	// 下次执行时间
	private String NextFireTime;
	// 上次执行时间
	private String PreviousFireTime;
	// 时间戳
	private String AddTime;
	// 调度状态
	private String status;
	// 任务组标志
	private String GroupID;
	// 调度顺序号
	private String schOrder;
	// 特使日期
	private String specialDate;
	//调度是否按当前预设执行日期执行
	private boolean nowDate;
	// 调度字符串
	private String cronStr;

	public String getCronStr() {
		return cronStr;
	}

	public void setCronStr(String cronStr) {
		this.cronStr = cronStr;
	}


	public String getGroupID() {
		return GroupID;
	}

	public void setGroupID(String groupID) {
		GroupID = groupID;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public ScherParam() {
	}

	public String getStartWhen() {
		return StartWhen;
	}

	public void setStartWhen(String startWhen) {
		StartWhen = startWhen;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public String getEndBy() {
		return EndBy;
	}

	public void setEndBy(String endBy) {
		EndBy = endBy;
	}

	public String getDailyN() {
		return DailyN;
	}

	public void setDailyN(String dailyN) {
		DailyN = dailyN;
	}

	public String getWeeklyN() {
		return WeeklyN;
	}

	public void setWeeklyN(String weeklyN) {
		WeeklyN = weeklyN;
	}

	public String getWeek() {
		return Week;
	}

	public void setWeek(String week) {
		Week = week;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getRecurMonthly() {
		return RecurMonthly;
	}

	public void setRecurMonthly(String recurMonthly) {
		RecurMonthly = recurMonthly;
	}

	public String getMonthlyDay() {
		return MonthlyDay;
	}

	public void setMonthlyDay(String monthlyDay) {
		MonthlyDay = monthlyDay;
	}

	public String getRecurPrimary() {
		return RecurPrimary;
	}

	public void setRecurPrimary(String recurPrimary) {
		RecurPrimary = recurPrimary;
	}

	public String getSchCde() {
		return schCde;
	}

	public void setSchCde(String schCde) {
		this.schCde = schCde;
	}

	public String getSchNme() {
		return schNme;
	}

	public void setSchNme(String schNme) {
		this.schNme = schNme;
	}

	public String getSchType() {
		return schType;
	}

	public void setSchType(String schType) {
		this.schType = schType;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public String getMonthlyDOW() {
		return MonthlyDOW;
	}

	public void setMonthlyDOW(String monthlyDOW) {
		MonthlyDOW = monthlyDOW;
	}

	public String getMonthlyNth() {
		return MonthlyNth;
	}

	public void setMonthlyNth(String monthlyNth) {
		MonthlyNth = monthlyNth;
	}

	public String getSecondN() {
		return SecondN;
	}

	public void setSecondN(String secondN) {
		SecondN = secondN;
	}

	public String getMinuteN() {
		return MinuteN;
	}

	public void setMinuteN(String minuteN) {
		MinuteN = minuteN;
	}

	public String getPerMinuteSecond() {
		return PerMinuteSecond;
	}

	public void setPerMinuteSecond(String perMinuteSecond) {
		PerMinuteSecond = perMinuteSecond;
	}

	public String getHourlyN() {
		return HourlyN;
	}

	public void setHourlyN(String hourlyN) {
		HourlyN = hourlyN;
	}

	public String getHourlyMinute() {
		return HourlyMinute;
	}

	public void setHourlyMinute(String hourlyMinute) {
		HourlyMinute = hourlyMinute;
	}

	public String getHourlySecond() {
		return HourlySecond;
	}

	public void setHourlySecond(String hourlySecond) {
		HourlySecond = hourlySecond;
	}

	public String getSchComent() {
		return schComent;
	}

	public void setSchComent(String schComent) {
		this.schComent = schComent;
	}

	public String getNextFireTime() {
		return NextFireTime;
	}

	public void setNextFireTime(String nextFireTime) {
		NextFireTime = nextFireTime;
	}

	public String getPreviousFireTime() {
		return PreviousFireTime;
	}

	public void setPreviousFireTime(String previousFireTime) {
		PreviousFireTime = previousFireTime;
	}

	public String getAddTime() {
		return AddTime;
	}

	public void setAddTime(String addTime) {
		AddTime = addTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	public String getSchOrder() {
		return schOrder;
	}

	public void setSchOrder(String schOrder) {
		this.schOrder = schOrder;
	}

	public String getSpecialDate() {
		return specialDate;
	}

	public void setSpecialDate(String specialDate) {
		this.specialDate = specialDate;
	}

	public boolean isNowDate() {
		return nowDate;
	}

	public void setNowDate(boolean nowDate) {
		this.nowDate = nowDate;
	}

}
