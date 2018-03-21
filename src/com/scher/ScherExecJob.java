package com.scher;

/**
 * @author: fenggq
 * @Email:
 * @Company:
 * @Action:调度执行任务
 * @DATE: Mar 8, 2012
 */
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import module.datetype.HolidayDao;
import module.datetype.NowDateDao;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;


import com.app.AppVar;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.TaskDao;
import com.taskgroup.TaskGroup;
import com.taskgroup.TaskGroupDao;
import com.threadPool.ThreadPool;

import common.util.conver.UtilConver;
import common.util.string.UtilString;
import consts.Const;
import consts.VariableApp;

public class ScherExecJob implements Job {
	// 任务调度的短信报警全局变量,记录该任务上次发送报警的时间 key:由 调度ID+"|"+ 任务组ID +"|"+任务ID+"|"
	// private final int unFinishedCount = 100000000;//
	// 如果unFinishedCount次还没执行完，则自动设为执行完成
	// 调度开始时间
	private String startTime = null;
	private final Long execTime = System.currentTimeMillis();
	private String nowDate;
	// 调度完成标志:0表示调度中，1表示已完成
	public static ConcurrentHashMap<String, Integer> MapScherFinished = new ConcurrentHashMap<String, Integer>();
	// 由于前面调度未完成，之后调度被忽略的次数
	public static ConcurrentHashMap<String, Integer> MapScherUnExecCount = new ConcurrentHashMap<String, Integer>();

	// 构造函数
	public ScherExecJob() {
		this.startTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
	}

	// 调度执行
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScherParam sp = null;
		// 调度开始时间点
		String scherExecMsg = "";
		try {
			scherExecMsg = getExecMsg(scherExecMsg, "调度开始");
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			sp = (ScherParam) dataMap.get("ScheParam");// 获取调度实例
			String nextFireTime = UtilString.isNil(sp.getNextFireTime()).trim();
			if (!nextFireTime.equals("")) {
				if (nextFireTime.compareTo(UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss)) > 0) {
					scherExecMsg = getExecMsg(scherExecMsg, "调度尚未到达下次执行时间" + nextFireTime + ",本次调度忽略");
					return;
				}
			}
			// ----------------------------------
			try {
				this.nowDate = NowDateDao.getInstance().getNowDate(sp.isNowDate(), "调度[" + sp.getSchCde() + "]");
			} catch (Exception e) {
				Log.logError(sp.getSchCde() + "调度执行任务错误:获取当前执行日期", e);
				scherExecMsg = getExecMsg(scherExecMsg, "调度执行任务错误：获取当前执行日期" + e.getMessage());
				return;
			}
			// 判断交易日，更新交易日全局变量 MapTraceDate
			Boolean IsDate = this.nowDate.equals("") ? true : HolidayDao.getInstance().isTraceDate(this.nowDate, sp.getDateType());
			// 判断特殊日期
			if (IsDate)
				IsDate = HolidayDao.getInstance().IsSpecialDate(sp.getSpecialDate(), this.nowDate, sp.getDateType());
			// 根据dateType判断是否为执行日期
			if (IsDate) {
				scherExecMsg = getExecMsg(scherExecMsg, "确认调度在执行日期内");
				String nowtime = UtilConver.dateToStr(Const.fm_HHmm);
				String execTime[][] = UtilString.getExecTime(sp.getExecTime());
				Boolean isTime = false;
				// 判断执行时段
				if (execTime != null) {
					for (int i = 0; i < execTime.length; i++) {
						if ((nowtime.compareTo(execTime[i][0]) >= 0) && (nowtime.compareTo(execTime[i][1]) <= 0)) {
							isTime = true;
						}
					}
				} else {
					isTime = true;
				}
				if (isTime) {
					scherExecMsg = getExecMsg(scherExecMsg, "确认调度在执行时段内");
					scherExecMsg = getExecMsg(scherExecMsg, "调度开始执行任务");
					// 执行调度中的任务
					execTasks(sp);
				} else {
					scherExecMsg = getExecMsg(scherExecMsg, "任务不在设置的执行时段内,本次调度忽略执行");
				}
			} else {
				scherExecMsg = getExecMsg(scherExecMsg, "任务非指定日期类型,本次调度忽略执行");
			}
			// 刷新调度状态
			refreshSche(sp, context);
		} catch (Exception e) {
			Log.logError(sp.getSchCde() + "调度执行任务错误:", e);
			scherExecMsg = getExecMsg(scherExecMsg, "调度执行任务错误" + e.getMessage());
		} finally {
			Log.takeLogSche(sp.getSchCde(), scherExecMsg, this.startTime, UtilConver.dateToStr(new Date(), Const.fm_yyyyMMdd_HHmmss), System.currentTimeMillis() - this.execTime);
		}
	}

	// 执行任务、任务组
	private void execTasks(ScherParam scher) {
		try {
			Long[][] taskId_Order = null;
			String groupId = UtilString.isNil(scher.getGroupID());
			if (groupId.equals("")) {// 任务处理
				taskId_Order = new Long[1][1];
				taskId_Order[0][0] = Long.valueOf(scher.getTaskID());
			} else {// 任务组处理
				taskId_Order = TaskDao.getInstance().GetTaskIdTaskOrderFromGroupId(Long.valueOf(groupId));
			}
			if (taskId_Order.length > 0) {
				if (Integer.valueOf(scher.getSchType()) == 0) {// 串行
					serialExecute(groupId, scher, taskId_Order);
				} else if (Integer.valueOf(scher.getSchType()) == 1) {// 并行
					paiallelExecute(groupId, scher, taskId_Order);
				}
			} else {// 没有任务时的提示处理
				Log.logInfo("任务组[" + groupId + "]:没有包含任务");
			}
		} catch (Exception e) {
			Log.logError(scher.getSchCde() + "调度执行任务-执行任务错误:", e);
		} finally {
		}
	}

	// 串行执行
	private void serialExecute(final String groupId, final ScherParam scher, final Long[][] taskId_Order) {
		// 串行keyFinished="调度标志|任务组标志||"
		final String keyFinished = scher.getSchCde() + "|" + groupId + "|" + "" + "|" + "";
		if (getFinished(keyFinished) == 0) {// 该任务组在此前调度尚未执行完成
			for (int i = 0; i < taskId_Order.length; i++) {
				ITask task = TaskDao.getInstance().getMapTask(taskId_Order[i][0]);
				if (task.getTaskId() == 0)
					return;
				task.setTaskStatus("执行成功");
				task.setTaskMsg("任务在之前调度尚未完成,忽略本次调度:" + getUnExecCount(keyFinished));
				Log.taskLog(task, groupId, scher.getSchCde(), this.startTime, UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss), System.currentTimeMillis() - this.execTime);
			}
		} else {// 该任务在此前调度执行完成
			MapScherFinished.put(keyFinished, 0);
			final String nDate = this.nowDate;
			Thread thread = new Thread() {
				public void run() {
					try {
						for (int i = 0; i < taskId_Order.length; i++) {
							String taskOrder = groupId.equals("") ? "" : String.valueOf(taskId_Order[i][1]);
							// 串行行keyTaskStartTime="调度标志|任务组标志|任务标志|任务组任务顺序"
							String keyTaskStartTime = scher.getSchCde() + "|" + groupId + "|" + String.valueOf(taskId_Order[i][0]) + "|" + taskOrder;

							ITask task = TaskDao.getInstance().getMapTask(taskId_Order[i][0]);
							AppVar.putMapTaskStartTime(task, keyTaskStartTime, System.currentTimeMillis());
							String startTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
							String scheId = scher.getSchCde();
							task.setNowDate(nDate);
							if (task.getNowDate().equals("")) {
								task.setTaskStatus("执行失败");
								task.setTaskMsg("获取当前执行日期为空!");
							} else {
								task.fireTask(startTime, groupId, scheId, taskOrder);
							}
							String endTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
							String taskStatus = task.getTaskStatus();// takeLogCommon清空了状态
							Log.taskLog(task, groupId, scheId, startTime, endTime, System.currentTimeMillis() - execTime);
							// 执行完成
							AppVar.putMapTaskStartTime(task, keyTaskStartTime, 0l);
							if (groupId != null && !groupId.equalsIgnoreCase("")) {// 当调度为串行时，任务组的错误退出，调度并行时，错误不做异常处理
								if (taskStatus.equals("执行失败")) {
									TaskGroup taskGroup = TaskGroupDao.getInstance().getTaskGroupFromGroupID(Long.valueOf(groupId));
									if (taskGroup.getErrorDeal() == 1) {
										// 错误退出
										break;
									}
								}
							}
						}
					} catch (Exception e) {
						Log.logError("[" + keyFinished + "]串行执行:", e);
					} finally {
						MapScherUnExecCount.put(keyFinished, 0);
						MapScherFinished.put(keyFinished, 1);
					}
				}
			};
			ThreadPool.getPool().submit(keyFinished, thread);
		}
	}

	// 并行执行
	private void paiallelExecute(final String groupId, final ScherParam scher, final Long[][] taskId_Order) {
		for (int i = 0; i < taskId_Order.length; i++) {
			final Long taskId = taskId_Order[i][0];
			final String taskOrder = groupId.equals("") ? "" : String.valueOf(taskId_Order[i][1]);
			// 并行keyFinished="调度标志|任务组标志|任务标志|任务组任务顺序"
			final String keyFinished = scher.getSchCde() + "|" + groupId + "|" + String.valueOf(taskId_Order[i][0]) + "|" + taskOrder;

			if (getFinished(keyFinished) == 0) {
				// 该任务在此前调度尚未执行完成
				ITask task = TaskDao.getInstance().getMapTask(taskId_Order[i][0]);
				if (task.getTaskId() == 0)
					return;
				task.setTaskStatus("执行成功");
				task.setTaskMsg("任务在之前调度尚未完成,忽略本次调度:" + getUnExecCount(keyFinished));
				Log.taskLog(task, groupId, scher.getSchCde(), this.startTime, UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss), System.currentTimeMillis() - this.execTime);
				continue;
			} else {
				// 该任务在此前调度执行完成
				MapScherFinished.put(keyFinished, 0);
				final String nDate = this.nowDate;
				Thread thread = new Thread() {
					public void run() {
						try {
							String keyTaskStartTime = keyFinished;
							ITask task = TaskDao.getInstance().getMapTask(taskId);
							AppVar.putMapTaskStartTime(task, keyTaskStartTime, System.currentTimeMillis());
							String startTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
							task.setNowDate(nDate);
							if (task.getNowDate().equals("")) {
								task.setTaskStatus("执行失败");
								task.setTaskMsg("获取历史日期为空！");
							} else {
								task.fireTask(startTime, groupId, scher.getSchCde(), taskOrder);
							}
							if (task instanceof Runnable)
								return;
							String endTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
							Log.taskLog(task, groupId, scher.getSchCde(), startTime, endTime, System.currentTimeMillis() - execTime);
							AppVar.putMapTaskStartTime(task, keyTaskStartTime, 0l);
						} catch (Exception e) {
							Log.logError("[" + keyFinished + "并行执行:", e);
						} finally {
							MapScherFinished.put(keyFinished, 1);
							MapScherUnExecCount.put(keyFinished, 0);
						}
					}
				};
				ThreadPool.getPool().submit(keyFinished, thread);
			}
		}
	}

	// 获取调度执行信息
	private String getExecMsg(String oldExecMsg, String newExecMsg) {
		StringBuilder rs = new StringBuilder();
		rs.append(oldExecMsg);
		if (rs.indexOf("1") < 0) {
			rs.append("1、" + newExecMsg + ";");
		} else if (rs.indexOf("2") < 0) {
			rs.append("\n2、" + newExecMsg + ";");
		} else if (rs.indexOf("3") < 0) {
			rs.append("\n3、" + newExecMsg + ";");
		} else if (rs.indexOf("4") < 0) {
			rs.append("\n4、" + newExecMsg + ";");
		} else if (rs.indexOf("5") < 0) {
			rs.append("\n5、" + newExecMsg + ";");
		} else if (rs.indexOf("6") < 0) {
			rs.append("\n6、" + newExecMsg + ";");
		} else if (rs.indexOf("7") < 0) {
			rs.append("\n7、" + newExecMsg + ";");
		} else if (rs.indexOf("8") < 0) {
			rs.append("\n8、" + newExecMsg + ";");
		} else if (rs.indexOf("9") < 0) {
			rs.append("\n9、" + newExecMsg + ";");
		} else if (rs.indexOf("10") < 0) {
			rs.append("\n10、" + newExecMsg + ";");
		}
		return rs.toString();
	}

	// 刷新调度信息
	private void refreshSche(ScherParam sp, JobExecutionContext context) throws Exception {
		Trigger trigger = context.getTrigger();
		String nextFireTime = UtilString.isNil(UtilConver.dateToStr(trigger.getNextFireTime(), Const.fm_yyyyMMdd_HHmmss));
		// 当调度到时间，则置为完成状态
		if (nextFireTime.equals("")) {
			JobOperater.removeJob(sp.getSchCde());// 是否需要删除，或者让quartz自动删除？
			sp.setNextFireTime("");
			sp.setStatus("结束");
			sp.setPreviousFireTime(UtilString.isNil(UtilConver.dateToStr(trigger.getPreviousFireTime(), Const.fm_yyyyMMdd_HHmmss)));
			ScherDao.getInstance().modScher(sp);// 更新 调度表
			ScherTab.getInstance().querySche();
		}
		// 自动刷新调度结果
		if (VariableApp.systemParamsValue.getAutoRefreshScheResult().equalsIgnoreCase("1")) {
			JobOperater.refreshSche(context.getScheduler(), sp);
		}
	}

	// 判断调度是否完成
	private int getFinished(String key) {
		int finished = 0;
		if (MapScherFinished.get(key) == null)
			finished = 1;
		else
			finished = MapScherFinished.get(key);
		return finished;
	}

	// 获取调度未执行次数
	private int getUnExecCount(String key) {
		int rs = 1;
		if (MapScherUnExecCount.get(key) == null) {
			MapScherUnExecCount.put(key, 1);
		} else {
			rs = MapScherUnExecCount.get(key) + 1;
			MapScherUnExecCount.put(key, rs);
		}
		return rs;
	}

}