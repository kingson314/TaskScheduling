package com.log;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import module.mail.MailDao;
import module.mail.MailSend;
import module.mail.MailSender;
import module.systemparam.SystemParamsValue;


import com.app.AppLogView;
import com.app.MsgAlter;
import com.log.abridgement.LogAbridgement;
import com.log.abridgement.LogAbridgementDao;
import com.log.common.LogCommon;
import com.log.common.LogCommonDao;
import com.log.lately.AddLogLately;
import com.log.lately.LogLately;
import com.log.sche.AddLogSche;
import com.log.sche.LogSche;
import com.monitor.MonitorGroupDetailDao;
import com.taskInterface.ITask;

import common.util.conver.UtilConver;
import common.util.log.UtilLog;
import common.util.string.UtilString;
import consts.Const;
import consts.VariableApp;

/**
 * 记录日志
 * 
 * @author fgq 20120815
 * 
 */
public class Log {

	// public static final String LogSystem = "System";// 系统日志文件名称
	public static String LogPath = System.getProperty("user.dir") + "/log/";// 日志路径
	// 高频任务摘要key:由 调度ID+"|"+ 任务组ID +"|"+任务ID
	public static ConcurrentHashMap<String, LogAbridgement> MapLogAbridgement = new ConcurrentHashMap<String, LogAbridgement>();
	public static ConcurrentHashMap<String, String> MapLogAbridgementDate = new ConcurrentHashMap<String, String>();// yyyyMMdd
	public static ConcurrentHashMap<String, String> MapLogAbridgementBatchDate = new ConcurrentHashMap<String, String>();// yyyyMMdd
	public static ConcurrentHashMap<String, LogAbridgement> MapLogAbridgementBatch = new ConcurrentHashMap<String, LogAbridgement>();
	public static ConcurrentHashMap<String, AddLogLately> MapAddLogLately = new ConcurrentHashMap<String, AddLogLately>();
	public static ConcurrentHashMap<String, AddLogSche> MapAddLogSche = new ConcurrentHashMap<String, AddLogSche>();
	// 任务调度的短信报警全局变量 key:由 调度ID+"|"+ 任务组ID +"|"+任务ID+"|"
	public static ConcurrentHashMap<String, Long> MapLastlistenTime = new ConcurrentHashMap<String, Long>();
	public static final String[] TaskLogType = new String[] { "普通任务日志", "高频任务日志摘要" };

	public static String MonitorMailSubject = "易方达投研数据中心——任务调度监控信息";


	// 日志级别从高到低:ERROR[0]、WARN[1]、INFO[2]、DEBUG[3]
	// 错误级别
	public static void logError(String message, Exception e) {
		UtilLog.logError(message, e);
		showLog(AppLogView.LogSystem, message + getStrackTrace(e, VariableApp.systemParamsValue.getLogLevel()), true);
	}

	// 警告级别级别
	public static void logWarn(String message) {
		UtilLog.logWarn(message);
		showLog(AppLogView.LogSystem, message, true);
	}

	// 消息级别
	public static void logInfo(String message) {
		UtilLog.logInfo(message);
		showLog(AppLogView.LogSystem, message, true);
	}

	// 调试级别
	public static void logDebug(String message) {
		UtilLog.logDebug(message);
		showLog(AppLogView.LogSystem, message, true);
	}

	// 获取异常代码
	public static String getStrackTrace(Exception e, String logType) {
		e.printStackTrace();
		String stackTraceMsg = "";
		if (logType == null)
			logType = "1";
		if (logType.equals("-1")) {
			return stackTraceMsg;
		} else if (logType.equals("0")) {
			stackTraceMsg = e.getMessage() + "\n";
			return stackTraceMsg;
		} else if (logType.equals("1")) {
			stackTraceMsg = e.getMessage() + "\n";
			stackTraceMsg = stackTraceMsg + "errorList:";
			StackTraceElement[] stackTrace = e.getStackTrace();
			for (StackTraceElement element : stackTrace) {
				stackTraceMsg = stackTraceMsg + element.toString() + "\n";
			}
			return stackTraceMsg;
		}
		if (logType.equals("2")) {
			StackTraceElement[] stackTrace = e.getStackTrace();
			for (StackTraceElement element : stackTrace) {
				stackTraceMsg = stackTraceMsg + element.toString() + "\n";
			}
			return stackTraceMsg;
		} else
			return stackTraceMsg;
	}

	// 高频日志摘要汇总信息，每天重置，用于查询当前
	private synchronized static void takeLogAbridgement(ITask task, String groupId, String scheID, String startTime, String endTime, Long ExecTime) {
		if (scheID.trim().equals("-1"))
			return;
		String taskId = String.valueOf(task.getTaskId());
		String key = scheID + "|" + groupId + "|" + taskId;
		LogAbridgement newLogAbridgement = new LogAbridgement();
		try {
			String taskStatus = task.getTaskStatus();
			newLogAbridgement.setScheID(scheID == null ? "" : scheID);
			newLogAbridgement.setGroupId(groupId == null ? "" : groupId);
			newLogAbridgement.setTaskId(String.valueOf(task.getTaskId()));
			newLogAbridgement.setEndTime(endTime == null ? "" : endTime);
			// 本次执行时间 不是上一次
			newLogAbridgement.setLastExceTime(startTime);

			LogAbridgement oldLogAbridgement = MapLogAbridgement.get(key);
			if (oldLogAbridgement != null) {
				newLogAbridgement.setStartTime(oldLogAbridgement.getStartTime());
				newLogAbridgement.setSuccessTimes(oldLogAbridgement.getSuccessTimes() == null ? 0 : oldLogAbridgement.getSuccessTimes());
				newLogAbridgement.setFailedTimes(oldLogAbridgement.getFailedTimes() == null ? 0 : oldLogAbridgement.getFailedTimes());
				newLogAbridgement.setWarnedTimes(oldLogAbridgement.getWarnedTimes() == null ? 0 : oldLogAbridgement.getWarnedTimes());

				if (taskStatus.equalsIgnoreCase("执行成功")) {
					newLogAbridgement.setSuccessTimes(oldLogAbridgement.getSuccessTimes() == null ? 0 : oldLogAbridgement.getSuccessTimes() + 1);
				} else if (taskStatus.equalsIgnoreCase("执行失败")) {
					newLogAbridgement.setFailedTimes(oldLogAbridgement.getFailedTimes() == null ? 0 : oldLogAbridgement.getFailedTimes() + 1);
				} else if (taskStatus.equalsIgnoreCase("执行提示")) {
					newLogAbridgement.setWarnedTimes(oldLogAbridgement.getWarnedTimes() == null ? 0 : oldLogAbridgement.getWarnedTimes() + 1);
				}
				newLogAbridgement.setTotalTimes(oldLogAbridgement.getTotalTimes() == null ? 0 : oldLogAbridgement.getTotalTimes() + 1);
				newLogAbridgement.setTotalExecTime(oldLogAbridgement.getTotalExecTime() + ExecTime);
				if (ExecTime > oldLogAbridgement.getMaxExecTime())
					newLogAbridgement.setMaxExecTime(ExecTime);
				else
					newLogAbridgement.setMaxExecTime(oldLogAbridgement.getMaxExecTime());
				if (ExecTime < oldLogAbridgement.getMinExecTime())
					newLogAbridgement.setMinExecTime(ExecTime);
				else
					newLogAbridgement.setMinExecTime(oldLogAbridgement.getMinExecTime());
				newLogAbridgement.setAvgExecTime(newLogAbridgement.getTotalExecTime() / newLogAbridgement.getTotalTimes());
			} else {
				// 这个开始时间的含义为第一次执行时间
				newLogAbridgement.setStartTime(startTime == null ? "" : startTime);
				newLogAbridgement.setSuccessTimes(0l);
				newLogAbridgement.setFailedTimes(0l);
				newLogAbridgement.setWarnedTimes(0l);
				newLogAbridgement.setTotalTimes(0l);
				newLogAbridgement.setMinExecTime(0l);
				newLogAbridgement.setMaxExecTime(0l);
				newLogAbridgement.setTotalTimes(0l);
				newLogAbridgement.setAvgExecTime(0.0);
				if (taskStatus.equalsIgnoreCase("执行成功")) {
					newLogAbridgement.setSuccessTimes(1l);
				} else if (taskStatus.equalsIgnoreCase("执行失败")) {
					newLogAbridgement.setFailedTimes(1l);
				} else if (taskStatus.equalsIgnoreCase("执行提示")) {
					newLogAbridgement.setWarnedTimes(1l);
				}

				newLogAbridgement.setTotalTimes(1l);
				newLogAbridgement.setTotalExecTime(ExecTime);
				newLogAbridgement.setMaxExecTime(ExecTime);
				newLogAbridgement.setMinExecTime(ExecTime);
				newLogAbridgement.setAvgExecTime(Double.valueOf(ExecTime));
			}
		} catch (Exception e) {
			logError("记录高频日志摘要错误:", e);
		} finally {
			try {
				if (!UtilConver.dateToStr(Const.fm_yyyyMMdd).equals(MapLogAbridgementDate.get(key))) {
					// 重置该key
					MapLogAbridgement.remove(key);
					MapLogAbridgementDate.put(key, UtilConver.dateToStr(new Date(), Const.fm_yyyyMMdd));
				} else
					MapLogAbridgement.put(key, newLogAbridgement);
			} catch (Exception e) {
				logError("记录高频日志摘要存库错误:", e);
			} finally {
			}
		}
	}

	// 最近日志，每N条写库，保证记录的都是当前n条的信息，而不是汇总的信息,用于写库
	private synchronized static void takeLogAbridgementBatch(ITask task, String groupId, String scheID, String startTime, String endTime, Long ExecTime) {
		if (scheID.trim().equals("-1"))
			return;
		String taskId = String.valueOf(task.getTaskId());
		String key = scheID + "|" + groupId + "|" + taskId;
		LogAbridgement newLogAbridgement = new LogAbridgement();
		try {
			String taskStatus = task.getTaskStatus();
			newLogAbridgement.setScheID(scheID == null ? "" : scheID);
			newLogAbridgement.setGroupId(groupId == null ? "" : groupId);
			newLogAbridgement.setTaskId(String.valueOf(task.getTaskId()));
			newLogAbridgement.setEndTime(endTime == null ? "" : endTime);
			// 本次执行时间 不是上一次
			newLogAbridgement.setLastExceTime(startTime);

			LogAbridgement oldLogAbridgement = MapLogAbridgementBatch.get(key);
			if (oldLogAbridgement != null) {
				newLogAbridgement.setStartTime(oldLogAbridgement.getStartTime());
				newLogAbridgement.setSuccessTimes(oldLogAbridgement.getSuccessTimes() == null ? 0 : oldLogAbridgement.getSuccessTimes());
				newLogAbridgement.setFailedTimes(oldLogAbridgement.getFailedTimes() == null ? 0 : oldLogAbridgement.getFailedTimes());
				newLogAbridgement.setWarnedTimes(oldLogAbridgement.getWarnedTimes() == null ? 0 : oldLogAbridgement.getWarnedTimes());
				if (taskStatus.equalsIgnoreCase("执行成功")) {
					newLogAbridgement.setSuccessTimes(oldLogAbridgement.getSuccessTimes() == null ? 0 : oldLogAbridgement.getSuccessTimes() + 1);
				} else if (taskStatus.equalsIgnoreCase("执行失败")) {
					newLogAbridgement.setFailedTimes(oldLogAbridgement.getFailedTimes() == null ? 0 : oldLogAbridgement.getFailedTimes() + 1);
				} else if (taskStatus.equalsIgnoreCase("执行提示")) {
					newLogAbridgement.setWarnedTimes(oldLogAbridgement.getWarnedTimes() == null ? 0 : oldLogAbridgement.getWarnedTimes() + 1);
				}
				newLogAbridgement.setTotalTimes(oldLogAbridgement.getTotalTimes() == null ? 0 : oldLogAbridgement.getTotalTimes() + 1);
				newLogAbridgement.setTotalExecTime(oldLogAbridgement.getTotalExecTime() + ExecTime);
				if (ExecTime > oldLogAbridgement.getMaxExecTime())
					newLogAbridgement.setMaxExecTime(ExecTime);
				else
					newLogAbridgement.setMaxExecTime(oldLogAbridgement.getMaxExecTime());
				if (ExecTime < oldLogAbridgement.getMinExecTime())
					newLogAbridgement.setMinExecTime(ExecTime);
				else
					newLogAbridgement.setMinExecTime(oldLogAbridgement.getMinExecTime());
				double avg = newLogAbridgement.getTotalExecTime() / newLogAbridgement.getTotalTimes();
				newLogAbridgement.setAvgExecTime(avg);
			} else {
				// 这个开始时间的含义为第一次执行时间
				newLogAbridgement.setStartTime(startTime == null ? "" : startTime);
				newLogAbridgement.setSuccessTimes(0l);
				newLogAbridgement.setFailedTimes(0l);
				newLogAbridgement.setWarnedTimes(0l);
				newLogAbridgement.setTotalTimes(0l);
				newLogAbridgement.setMinExecTime(0l);
				newLogAbridgement.setMaxExecTime(0l);
				newLogAbridgement.setTotalTimes(0l);
				newLogAbridgement.setAvgExecTime(0.0);
				if (taskStatus.equalsIgnoreCase("执行成功")) {
					newLogAbridgement.setSuccessTimes(1l);
				} else if (taskStatus.equalsIgnoreCase("执行失败")) {
					newLogAbridgement.setFailedTimes(1l);
				} else if (taskStatus.equalsIgnoreCase("执行提示")) {
					newLogAbridgement.setWarnedTimes(1l);
				}

				newLogAbridgement.setTotalTimes(1l);
				newLogAbridgement.setTotalExecTime(ExecTime);
				newLogAbridgement.setMaxExecTime(ExecTime);
				newLogAbridgement.setMinExecTime(ExecTime);
				newLogAbridgement.setAvgExecTime(Double.valueOf(ExecTime));
			}
		} catch (Exception e) {
			logError("记录高频日志摘要错误:", e);
		} finally {
			try {
				if (!UtilConver.dateToStr(Const.fm_yyyyMMdd).equals(MapLogAbridgementBatchDate.get(key))) {
					// 写库
					LogAbridgementDao.getInstance().addLogAbridgement(newLogAbridgement);
					// 重置该key
					MapLogAbridgementBatch.remove(key);
					MapLogAbridgementBatchDate.put(key, UtilConver.dateToStr(new Date(), Const.fm_yyyyMMdd));
				}
				if (newLogAbridgement.getTotalTimes() % Long.valueOf(VariableApp.systemParamsValue.getLogAbridgement_count()) == 0) {
					// 写库
					LogAbridgementDao.getInstance().addLogAbridgement(newLogAbridgement);
					MapLogAbridgementBatch.remove(key);
				} else
					MapLogAbridgementBatch.put(key, newLogAbridgement);
			} catch (Exception e) {
				logError("记录高频日志摘要存库错误:", e);
			} finally {
			}
		}
	}

	// 最近任务日志
	private synchronized static void takeLogLately(ITask task, String groupId, String scheID, String startTime, String endTime, Long ExecTime) {
		if (scheID.trim().equals("-1"))
			return;
		String taskId = String.valueOf(task.getTaskId());
		String key = scheID + "|" + groupId + "|" + taskId;
		LogLately logLately = new LogLately();
		try {
			String taskStatus = task.getTaskStatus();
			logLately.setScheID(scheID == null ? "" : scheID);
			logLately.setGroupId(groupId == null ? "" : groupId);
			logLately.setTaskId(String.valueOf(task.getTaskId()));
			logLately.setStartTime(startTime == null ? "" : startTime);
			logLately.setEndTime(endTime == null ? "" : endTime);
			logLately.setExceTime(ExecTime);
			logLately.setExceStatus(taskStatus);
			logLately.setExceResult(task.getTaskMsg());
			AddLogLately addLogLately = MapAddLogLately.get(key);
			if (addLogLately == null) {
				addLogLately = new AddLogLately(Integer.valueOf(VariableApp.systemParamsValue.getLogLately_count()));
			}
			addLogLately.addLog(logLately);
			MapAddLogLately.put(key, addLogLately);
		} catch (Exception e) {
			logError("记录最近高频日志错误:", e);
		} finally {

		}
	}

	// 最近调度日志
	public synchronized static void takeLogSche(String scheID, String execMsg, String startTime, String endTime, Long execTime) {
		String key = scheID;
		LogSche logSche = new LogSche();
		try {
			logSche.setScheId(scheID == null ? "" : scheID);
			logSche.setExecMsg(execMsg == null ? "" : execMsg);
			logSche.setStartTime(startTime == null ? "" : startTime);
			logSche.setEndTime(endTime == null ? "" : endTime);
			logSche.setExecTime(execTime == null ? 0 : execTime);
			AddLogSche addLogSche = MapAddLogSche.get(key);
			if (addLogSche == null) {
				addLogSche = new AddLogSche(Integer.valueOf(VariableApp.systemParamsValue.getLogSche_count()));
			}
			addLogSche.addLog(logSche);
			MapAddLogSche.put(key, addLogSche);
		} catch (Exception e) {
			logError("记录调度日志错误:", e);
		} finally {

		}
	}

	// 普通任务日志、错误或提示的高频日志、手工高频日志
	private synchronized static void takeLogCommon(ITask task, String groupId, String scheId, String startTime, String endTime, Long ExecTime) {
		String taskStatus = task.getTaskStatus();
		String taskFailMsg = task.getTaskMsg();
		// ------------------普通任务日志 -------------------
		// 更新任务执行结果信息(持久化 + 刷新视图)
		final LogCommon taskResultEntry = new LogCommon();
		taskResultEntry.setScheID(scheId);
		taskResultEntry.setGroupId(groupId);
		taskResultEntry.setTaskId(String.valueOf(task.getTaskId()));
		taskResultEntry.setStartTime(startTime);
		taskResultEntry.setEndTime(endTime);
		taskResultEntry.setTaskStatus(taskStatus);
		taskResultEntry.setIfSendMsg("否");
		taskResultEntry.setTaskFailMsg(taskFailMsg);

		StringBuilder sb = new StringBuilder();
		sb.append("执行状态:  " + taskStatus + "\n");
		sb.append("开始时间:  " + startTime + "\n");
		sb.append("结束时间:  " + endTime + "\n");
		sb.append("调度标志:  " + (scheId.equals("-1") ? "手工" : scheId) + "\n");
		sb.append("任务组标志:" + String.valueOf(groupId) + "\n");
		String taskName = task.getTaskName();
		String taskId = String.valueOf(task.getTaskId());
		sb.append("任务标志:  " + String.valueOf(task.getTaskId()) + "\n");
		sb.append("任务名称:  " + taskName + "\n");

		if (taskStatus.equalsIgnoreCase("执行失败")) {
			sb.append("失败原因:  " + taskFailMsg.trim() + "\n");
		} else {
			sb.append("执行结果:  " + taskFailMsg.trim() + "\n");
		}
		sb.append("\n");
		taskResultEntry.setIfSendMsg(warn(task, groupId, scheId, startTime, endTime, ExecTime));
		LogCommonDao.getInstance().addLogCommon(taskResultEntry);
		// 使用了静态变量ScheExecJob.MapTask 后，须清空这两个字段
		String title = taskName + "[" + taskId + "]";
		showLog(title, sb.toString(), false);
		// 如果是手工执行/执行失败/执行提示,普通日志类型，则系统日志与任务日志都写前台显示
		if (scheId.equals("-1") || taskStatus.equals("执行失败") || taskStatus.equals("执行提示")|| "普通任务日志".equals(task.getLogType())) {
			showLog(AppLogView.LogSystem, sb.toString(), true);
		}
		task.setTaskStatus("");
		task.setTaskMsg("");
	}

	// 任务日志
	public synchronized static void taskLog(ITask task, String groupId, String scheID, String startTime, String endTime, Long ExecTime) {
		try {
			if (UtilString.isNil(task.getTaskStatus()).length() < 1)
				return;
			if (task.getLogType().equals(TaskLogType[1])) {
				// 高频日志摘要
				takeLogAbridgement(task, groupId, scheID, startTime, endTime, ExecTime);
				takeLogAbridgementBatch(task, groupId, scheID, startTime, endTime, ExecTime);
				// 最近执行日志
				takeLogLately(task, groupId, scheID, startTime, endTime, ExecTime);
				// 如果任务为非执行成功，还要继续执行普通任务日志
				if (!scheID.equals("-1") && task.getTaskStatus().equals("执行成功")) {
					task.setTaskMsg("");
					task.setTaskStatus("");
					return;
				}
			}
			// 记录普通日志
			takeLogCommon(task, groupId, scheID, startTime, endTime, ExecTime);
		} catch (Exception e) {
			logError("记录执行结果日志错误:", e);
		} finally {
		}
	}

	// 发送报警
	private synchronized static String warn(ITask task, String groupId, String scheID, String startTime, String endTime, Long ExecTime) {
		String ifSendMsg = "否";
		if (task.getMonitorGroup().length() > 0) {
			if (task.getTaskStatus().equalsIgnoreCase("执行失败") || task.getTaskStatus().equalsIgnoreCase("执行提示")) {// 发送报警

				String key = scheID + "|" + groupId + "|" + String.valueOf(task.getTaskId());
				// 初始化ScheMap_LastlistenTime
				if (MapLastlistenTime.get(key) == null)
					MapLastlistenTime.put(key, 0l);

				// 按指定间隔时间执行
				if ((System.currentTimeMillis() - MapLastlistenTime.get(key)) > (task.getInterval() * 1000)) {
					MapLastlistenTime.put(key, System.currentTimeMillis());
					// 警告信信息
					StringBuilder sb_msg = new StringBuilder();
					sb_msg.append("开始时间:" + startTime + "\n");
					sb_msg.append("调度标志:" + scheID + "\n");
					sb_msg.append("任务标志:" + String.valueOf(task.getTaskId()) + "\n");
					sb_msg.append("任务名称:  " + task.getTaskName() + "\n");
					sb_msg.append("投研数据中心");

					if (task.getWarnType().equals(Const.WarnType[0]) || task.getWarnType().equals(Const.WarnType[1])) {
						if (Integer.valueOf(VariableApp.systemParamsValue.getAutoSend().equals("") ? "0" : VariableApp.systemParamsValue.getAutoSend()) == 1) {
							String tel = MonitorGroupDetailDao.getInstance().getMTel(task.getMonitorGroup());
							if (!tel.equals("")) {
								SystemParamsValue sysParamValue = (SystemParamsValue) VariableApp.systemParamsValue.clone();
								sysParamValue.setTel(tel);
								String msg = "";
								int index = task.getTaskMsg().indexOf("errorList:");
								if (index > 0) {
									msg = task.getTaskMsg().substring(0, index) + sb_msg.toString();
								} else {
									msg = task.getTaskMsg() + "\n" + sb_msg.toString();
								}
								new MsgAlter(sysParamValue, msg).run();
								ifSendMsg = "是";

							}
						}
					}
					if (task.getWarnType().equals(Const.WarnType[0]) || task.getWarnType().equals(Const.WarnType[2])) {
						MailSender mailsender = MailDao.getInstance().getMailSenderByAddress(VariableApp.systemParamsValue.getMonitorMailAdress());
						if (mailsender != null) {
							String[] mMail = MonitorGroupDetailDao.getInstance().getMMailAddress(task.getMonitorGroup());
							if (mMail != null) {
								for (int i = 0; i < mMail.length; i++) {
									if (!mMail[i].equals("")) {
										String msg = task.getTaskMsg() + "\n" + sb_msg.toString();
										new MailSend(mailsender, mMail[i], null, MonitorMailSubject, msg);
									}
								}
							}

						}
					}
				}
			}
		}
		return ifSendMsg;
	}

	// 前台显示日志
	public synchronized static void showLog(String title, String log, boolean isShowNow) {
		try {
			if (!isShowNow) {
				if (!AppLogView.getInstance().getTab().hasTab(title)) {
					// 如果不马上显示，而且页面上也不包含此页，则不写日志，要看日志可查询执行日志
					return;
				}
			} else {
				AppLogView.getInstance().addTab(title);
			}
			while (log.endsWith("\n")) {
				log = log.substring(0, log.lastIndexOf("\n"));
			}
			log = log + "\n============================================================";
			if (log.indexOf("执行失败") >= 0) {
				AppLogView.getInstance().getLogDisplay(title).append(log, Color.red, 12);
			} else if (log.indexOf("执行提示") >= 0) {
				AppLogView.getInstance().getLogDisplay(title).append(log, Color.BLUE, 12);
			} else {
				AppLogView.getInstance().getLogDisplay(title).append(log, Color.black, 12);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 编写文件日志
	public synchronized static void WriteLog(String fileName, String log, int LogLevel) {// 写日志
		if (LogLevel != 0)
			return;// 只记录日志级别为0的日志
		String sFilePath = LogPath + UtilConver.dateToStr(Const.fm_yyyyMMdd) + "/";
		File fpath = new File(sFilePath);
		if (fpath.exists() == false)
			fpath.mkdirs();

		String fName = sFilePath + fileName + ".log";
		File f = new File(fName);
		File fPath = new File(f.getParent());
		if (fPath.exists() == false)
			fPath.mkdirs();
		if (f.exists() == false) // 文件不存在则创建一个
		{
			try {
				f.createNewFile();
			} catch (IOException e) {

				logError("WriteFile错误:", e);
			}
		}
		try {
			String content = "";
			String tmpline = "";
			BufferedReader fReader = new BufferedReader(new FileReader(fName)); // 读取文件原有内容
			while ((tmpline = fReader.readLine()) != null) {
				content += tmpline + "\n";
			}
			fReader.close();
			String dateTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmssSSS);
			content += dateTime + ":" + log;
			BufferedWriter fWriter = new BufferedWriter(new FileWriter(fName));
			fWriter.write(content);
			fWriter.flush();
			fWriter.close();
			// 每20k新增一个文件
			if ((double) (f.length() / 1024) > 20) {
				int i = 0;
				while (true) {
					String tmpname = sFilePath + fileName + (i) + ".log";// i转换字符
					File tmpf = new File(tmpname);
					if (tmpf.exists() == false) {
						f.renameTo(tmpf);
						break;
					}
					i = i + 1;
				}
			}
		} catch (IOException e) {
			logError("WriteLog IO错误:", e);
		} catch (Exception e) {
			logError("WriteLog 错误:", e);
		}
	}

}
