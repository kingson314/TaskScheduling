package com.scher;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import java.util.Date;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.DailyCalendar;


import com.efunds.tydw.common.util.DateAction;
import com.efunds.tydw.sys.util.StringUtils;
import com.log.Log;

import common.util.conver.UtilConver;
import common.util.string.UtilString;
import consts.Const;

/**
 * @author: fenggq
 * @Email:
 * @Company:
 * @Action:获取传递参数，组合成Cron表达式，然后执行调度
 * @DATE: Mar 8, 2012
 */
public class ScherRunAction {
	private ScherParam sp;

	public ScherRunAction(ScherParam scherParam) {
		this.sp = scherParam;
	}

	public void run() {
		try {
			setCronStr();
			startScher();
		} catch (Exception e) {
			Log.logError("启动调度" + sp.getSchCde() + "发生错误:", e);
		}
	}

	// 设置非一次执行的调度字符串
	private void setCronStr() {
		String Week = sp.getWeek();
		String month = sp.getMonth();
		try {
			String cronStr = "";
			// 转换成 秒 分 时 的格式
			sp.setStartTime(UtilString.isNil(sp.getStartTime(), UtilConver.dateToStr(new Date(), Const.fm_HHmmss)));
			sp.setStartDate(UtilString.isNil(sp.getStartDate(), UtilConver.dateToStr(new Date(), Const.fm_yyyyMMdd)));

			String sTime = DateAction.getDat(sp.getStartTime(), Const.fm_HHmmss, "ss mm HH");

			String[] sTimes = sTime.split("/s");

			if (sTime.equals("") || sTime == null) {
				sTime = "0 00 00";// 则表示为0点运行
			}

			if (sp.getRecurPrimary().equals("persecond")) {
				cronStr += 0 + "/" + sp.getSecondN() + " * * * * ?";
			} else if (sp.getRecurPrimary().equals("perminute")) {
				cronStr += (StringUtils.isBlank(sp.getPerMinuteSecond()) ? sTimes[0] : sp.getPerMinuteSecond()) + " " + 0 + "/" + sp.getMinuteN() + " * * * ?";
			} else if (sp.getRecurPrimary().equals("hourly")) {
				cronStr += (StringUtils.isBlank(sp.getHourlySecond()) ? sTimes[0] : sp.getHourlySecond()) + " " + (StringUtils.isBlank(sp.getHourlyMinute()) ? sTimes[1] : sp.getHourlyMinute()) + " "
						+ sTimes[2] + "/" + sp.getHourlyN() + " * * ?";
			} else if (sp.getRecurPrimary().equals("dailyday")) {
				cronStr += sTime + " " + "* * ?";
			} else if (sp.getRecurPrimary().equals("weekly")) {
				Week = Week.replaceAll(" ", "");
				// 不支持每隔几周
				cronStr += sTime + " ? * " + Week;// + "/" + sp.getWeeklyN();
			} else if (sp.getRecurPrimary().equals("monthly")) {
				month = month.replaceAll(" ", "");
				if (sp.getRecurMonthly().equals("monthlyday")) {
					cronStr += sTime + " " + sp.getMonthlyDay() + " " + month + " ?";
				} else {
					if (sp.getRecurMonthly().equals("monthlynth")) {
						cronStr += sTime + " ?  " + month + " " + sp.getMonthlyDOW() + "#" + sp.getMonthlyNth();
					}
				}
			}
			// 设置
			sp.setCronStr(cronStr);
			sp.setEndBy(UtilString.isNil(sp.getEndBy(), ""));
			sp.setEndTime(UtilString.isNil(sp.getEndTime(), ""));
		} catch (Exception e) {
			Log.logError(sp.getSchCde() + "设置CronStr发生错误:", e);
		}
	}

	// 启动调度
	private void startScher() throws SchedulerException {
		try {
			String schCde = sp.getSchCde();
			String jobName = schCde;
			String jobGroup = schCde;
			String triggerName = schCde;
			String triggerGroup = schCde;
			String cronExpress = sp.getCronStr();

			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();

			JobDetail jobDetail = null;
			CronTrigger cronTrigger = null;
			try {
				jobDetail = newJob(ScherExecJob.class).withIdentity(jobName, jobGroup).build();
				// 传入调度参数，供JobAction使用！
				JobDataMap dataMap = jobDetail.getJobDataMap();
				dataMap.put("ScheParam", sp);
			} catch (Exception e) {
				Log.logError("调度" + sp.getSchCde() + "初始化任务信息发生错误:", e);
			}

			try {
				scheduler.deleteJob(jobKey(jobName, jobGroup));
			} catch (Exception e) {
				Log.logError("调度" + sp.getSchCde() + "删除任务:[" + jobName + "," + jobGroup + "]错误:", e);
				return;
			}
			TriggerBuilder<Trigger> triggerBuilder = newTrigger();
			triggerBuilder.withIdentity(triggerName, triggerGroup);
			if (!"once".equals(sp.getRecurPrimary())) {// 不是只执行一次则使用以下代码
				// 设置开始
				triggerBuilder.startAt(UtilConver.strToDate(sp.getStartDate() + " " + sp.getStartTime(), Const.fm_yyyyMMdd_HHmmss));
				// 设置结束
				if (!sp.getEndBy().equals("")) {
					triggerBuilder.endAt(UtilConver.strToDate(sp.getEndBy() + " " + sp.getEndTime(), Const.fm_yyyyMMdd_HHmmss));
				}

				// 设置时段
				if (!sp.getExecTime().equals("全部")) {
					String timeZone = sp.getExecTime().replaceAll(";", ",");
					timeZone = timeZone.replaceAll("/[", "");
					timeZone = timeZone.replaceAll("/]", "");
					String[] tz = timeZone.split(",");
					if (tz.length > 2) {
						scheduler.addCalendar(triggerName, getTwoTimeZoneCal(tz), true, true);
					} else if (tz.length == 2) {
						scheduler.addCalendar(triggerName, getOneTimeZoneCal(tz), true, true);
					}
					// 设置频率
					cronTrigger = triggerBuilder.modifiedByCalendar(triggerName).withSchedule(cronSchedule(cronExpress).withMisfireHandlingInstructionDoNothing()).build();

				} else {
					// 设置频率
					cronTrigger = triggerBuilder.withSchedule(cronSchedule(cronExpress).withMisfireHandlingInstructionDoNothing()).build();

				}
				scheduler.scheduleJob(jobDetail, cronTrigger);
			} else {// 只执行一次
				triggerBuilder.startAt(UtilConver.strToDate(sp.getStartDate() + " " + sp.getStartTime(), Const.fm_yyyyMMdd_HHmmss));
				SimpleTrigger simpleTrigger = triggerBuilder.withSchedule(simpleSchedule().withIntervalInSeconds(1).withRepeatCount(0)).build();
				scheduler.scheduleJob(jobDetail, simpleTrigger);
			}
			// 启动调度
			scheduler.start();
		} catch (Exception e) {
			Log.logError("调度" + sp.getSchCde() + "生成调度任务发生错误:", e);
		}
	}

	/**
	 * 
	 * 调度本身只支持两个以下的时段控制
	 * 
	 */
	// 获取单个调度时段
	private DailyCalendar getOneTimeZoneCal(String[] args) {
		DailyCalendar cal = new DailyCalendar(args[0], args[1]);
		cal.setInvertTimeRange(true);
		return cal;
	}

	// 获取双调度时段
	private DailyCalendar getTwoTimeZoneCal(String[] args) {
		DailyCalendar cal1 = new DailyCalendar(args[0], args[3]);
		cal1.setInvertTimeRange(true);
		DailyCalendar cal2 = new DailyCalendar(cal1, args[1], args[2]);
		return cal2;
	}

	// public static void main(String[] args) throws SchedulerException {
	// ScherParam sp = new ScherParam();
	// sp.setSchCde("100");
	// sp.setStartDate("2012-01-01");
	// sp.setStartTime("00:00:00");
	// sp.setEndBy("");
	// sp.setEndTime("");
	// sp.setExecTime("[14:06,18:00]");
	// sp.setSecondN("5");
	// sp.setCronStr(0 + "/" + sp.getSecondN() + " * * * * ?");
	// new ScherRunner().taskCron(sp, 0);
	// }
}
