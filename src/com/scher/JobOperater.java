package com.scher;

import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerKey.triggerKey;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.log.Log;

import common.util.conver.UtilConver;
import consts.Const;

/**
 * @author: fenggq
 * @Email:
 * @Company:
 * @Action: 调度操作
 * @DATE: Mar 8, 2012
 */
public class JobOperater {
	// 移除任务
	public static boolean removeJob(String schCde) {
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			{
				String triggerName = schCde;
				String triggerGroupName = schCde;
				String jobName = schCde;
				String jobGroupName = schCde;
				try {
					scheduler.pauseTrigger(triggerKey(triggerName, triggerGroupName));// 停止触发器
				} catch (SchedulerException e) {
					Log.logError("调度(" + schCde + ")停止触发器[" + triggerName + "," + triggerGroupName + "]错误:", e);
					return false;
				}
				try {
					scheduler.unscheduleJob(triggerKey(triggerName, triggerGroupName));// 移除触发器
				} catch (SchedulerException e) {
					Log.logError("调度(" + schCde + ")移除触发器[" + triggerName + "," + triggerGroupName + "]错误:", e);
					return false;
				}
				try {
					scheduler.deleteJob(jobKey(jobName, jobGroupName));// 删除任务
				} catch (SchedulerException e) {
					Log.logError("调度(" + schCde + ")删除任务[" + jobName + "," + jobGroupName + "]错误:", e);
					return false;
				}
				return true;
			}

		} catch (Exception e) {
			Log.logError("停止调度(" + schCde + ")任务发生错误:", e);
			return false;
		}

	}

	// 恢复调度
	public static boolean resumeJob(String schCde, String jobCde) {
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();

			Trigger trigger = scheduler.getTrigger(triggerKey(jobCde, schCde));
			scheduler.rescheduleJob(triggerKey(jobCde, schCde), trigger);
			// System.out.println("恢复调度(" + schCde + ")任务成功!");
			return true;
		} catch (Exception e) {
			Log.logError("恢复调度(" + schCde + ")任务发生错误:", e);
			return false;
		}

	}

	// 停止使用相关的触发器
	public static void pauseTrigger(String schCde) {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = null;
		try {
			scheduler = schedulerFactory.getScheduler();
			String triggerName = schCde;
			String triggerGroupName = schCde;
			TriggerState ts = scheduler.getTriggerState(triggerKey(triggerName, triggerGroupName));
			if (!ts.equals("PAUSED"))
				scheduler.pauseTrigger(triggerKey(triggerName, triggerGroupName));
		} catch (SchedulerException e) {
			Log.logError("暂停调度(" + schCde + ")发生错误:", e);
		}
	}

	// 暂停所有的触发器
	public static void pauseTriggers(GroupMatcher<TriggerKey> groupmatcher) throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.pauseTriggers(groupmatcher);
	}

	// 关闭调度
	public static void shutDown() throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.shutdown();
	}

	// 恢复相关的trigger
	public static void resumeTrigger(String schCde) {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = null;
		try {
			scheduler = schedulerFactory.getScheduler();
			String triggerName = schCde;
			String triggerGroupName = schCde;
			TriggerState ts = scheduler.getTriggerState(triggerKey(triggerName, triggerGroupName));
			if (ts.toString().equals("PAUSED"))
				scheduler.resumeTrigger(triggerKey(triggerName, triggerGroupName));
			else if (ts.toString().equals("NONE")) {
				ScherParam scheParam = ScherDao.getInstance().getScheParamsFromSchCde(Long.valueOf(schCde));
				new ScherRunAction(scheParam).run();
			}
		} catch (Exception e) {
			Log.logError("恢复调度(" + schCde + ")发生错误:", e);
		}
	}

	// 获取守护调度实例
	public static ScherParam getWatchSche() {
		ScherParam sp = new ScherParam();
		sp.setStartWhen("now");
		sp.setAddTime("");
		sp.setStatus("");
		sp.setDateType("全部");
		sp.setExecTime("全部");
		sp.setStartDate("");
		sp.setEndDate("none");
		sp.setEndBy("");
		sp.setDailyN("");
		sp.setWeeklyN("");
		sp.setWeek("");
		sp.setMonth("");
		sp.setRecurMonthly("");
		sp.setMonthlyDay("");
		sp.setRecurPrimary("persecond");
		sp.setSchCde("-1");
		sp.setSchNme("守护调度");
		sp.setSchType("0");
		sp.setSchComent("");
		sp.setStartTime("");
		sp.setEndTime("");
		sp.setMonthlyDOW("");
		sp.setMonthlyNth("");
		sp.setSecondN("1");
		sp.setMinuteN("");
		sp.setPerMinuteSecond("");
		sp.setHourlyN("");
		sp.setHourlyMinute("");
		sp.setHourlySecond("");
		sp.setTaskID("-1");
		sp.setNextFireTime("");
		sp.setPreviousFireTime("");
		sp.setGroupID("");
		sp.setSchOrder("00");
		sp.setSpecialDate("全部");
		return sp;
	}

	// 恢复所有的触发器
	public static void resumeTriggers(GroupMatcher<TriggerKey> groupmatcher) throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.resumeTriggers(groupmatcher);
	}

	// 恢复相关的job任务
	public static void resumeJob(JobKey jobkey) throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.pauseJob(jobkey);
	}

	// 恢复所有的任务
	public static void resumeJobs(GroupMatcher<JobKey> matcher) throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.resumeJobs(matcher);
	}

	// 刷新调度信息
	public static void refreshSche(Scheduler scheduler, ScherParam sp) {
		try {
			// if(Fun.isNil(sp.getStatus()).equals("停止"))return;
			Trigger trigger = scheduler.getTrigger(new TriggerKey(sp.getSchCde(), sp.getSchCde()));
			if (trigger != null) {
				if (trigger.getPreviousFireTime() == null) {
					sp.setPreviousFireTime("");
				} else if (trigger.getPreviousFireTime() == null) {
					sp.setPreviousFireTime("");
				} else {
					sp.setPreviousFireTime(UtilConver.dateToStr(trigger.getPreviousFireTime(), Const.fm_yyyyMMdd_HHmmss));
				}
				if (trigger.getNextFireTime() == null) {
					sp.setNextFireTime("");
				} else if (trigger.getNextFireTime() == null) {
					sp.setNextFireTime("");
				} else {
					sp.setNextFireTime(UtilConver.dateToStr(trigger.getNextFireTime(), Const.fm_yyyyMMdd_HHmmss));
				}
			}

			TriggerState ts = scheduler.getTriggerState(new TriggerKey(sp.getSchCde(), sp.getSchCde()));
			if (ts.toString().equals("NONE")) {
				sp.setStatus("结束");
			} else if (ts.toString().equals("NORMAL")) {
				sp.setStatus("正常");
			} else if (ts.toString().equals("PAUSED")) {
				sp.setStatus("停止");
			} else if (ts.toString().equals("COMPLETE")) {
				sp.setStatus("完成");
			} else if (ts.toString().equals("ERROR")) {
				sp.setStatus("出错");
			} else if (ts.toString().equals("BLOCKED")) {
				sp.setStatus("阻塞");
			}

			ScherDao.getInstance().modScher(sp);// 更新 调度表
			ScherTab.getInstance().querySche();
		} catch (Exception e) {
			Log.logError("刷新调度状态错误", e);
		}
	}

	// 刷新调度状态
	public static void refreshSche() {
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			ScherParam[] sp = ScherDao.getInstance().getScherTime();
			if (sp != null) {
				for (int i = 0; i < sp.length; i++) {
					JobOperater.refreshSche(scheduler, sp[i]);
				}
			}
		} catch (SchedulerException e) {
			Log.logError("主程序刷新调度错误:", e);
		} catch (Exception e) {
			Log.logError("主程序刷新调度错误1:", e);
		}
	}
	/*
	 * public static boolean cancelJob(String schCde, String jobCde) { try {
	 * SchedulerFactory schedulerFactory = new StdSchedulerFactory(); Scheduler
	 * scheduler = schedulerFactory.getScheduler(); // String
	 * triggerGroups[]=scheduler.getTriggerGroupNames(); // List<String>
	 * triggerGroups = scheduler.getTriggerGroupNames();
	 *//***********************************************************************
		 * *获取Job的触发器实体 触发器的名称和组名格式分别为: triggerName: schCde+"|"+UUID
		 * triggerGroup: schCde
		 **********************************************************************/
	/*
	 * System.out.println("cancelJob:" + schCde + " " + jobCde); // Trigger
	 * tg=scheduler.getTrigger(jobCde, schCde); Trigger tg =
	 * scheduler.getTrigger(triggerKey(jobCde, schCde)); //
	 * scheduler.getTriggerState(triggerKey(jobCde, schCde)); //
	 * scheduler.deleteJob(jobCde, schCde); scheduler.deleteJob(jobKey(jobCde,
	 * schCde)); System.out.println(tg.getCalendarName()); // if
	 * (!tg.getCalendarName().equals(null))
	 * scheduler.deleteCalendar(tg.getCalendarName());
	 * System.out.println("停止Quartz任务成功!");
	 * DataDriverLog.log.error("停止Quartz任务成功!"); return true; } catch (Exception
	 * cancelJobE) { cancelJobE.printStackTrace();
	 * System.out.println("停止Quartz任务发生错误:" +
	 * cancelJobStaticFun.getStrackTrace(e, StaticFun.logType));
	 * DataDriverLog.log .error("停止Quartz任务发生错误:" +
	 * cancelJobStaticFun.getStrackTrace(e, StaticFun.logType)); return false; } }
	 */
}
