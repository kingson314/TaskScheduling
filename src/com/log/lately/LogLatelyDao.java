package com.log.lately;

import java.util.Map;
import java.util.Vector;

import com.log.Log;
import com.scher.ScherDao;
import com.taskInterface.TaskDao;
import com.taskgroup.TaskGroupDao;

import common.util.Math.UtilMath;

public class LogLatelyDao {
	private static LogLatelyDao logLatelyDao = null;

	// private Connection con;

	public static LogLatelyDao getInstance() {
		if (logLatelyDao == null)
			logLatelyDao = new LogLatelyDao();
		return logLatelyDao;
	}

	// 查询来自当前内存的最近高频日志
	public Vector<?> getLogLatelyVector(Map<String, String> map, Map<String, AddLogLately> map_addLogLately) {
		Vector<Vector<String>> taskLogLately = new Vector<Vector<String>>();
		try {
			if (map == null)
				return taskLogLately;
			String key = map.get("调度标志") + "|" + map.get("任务组标志") + "|" + map.get("任务标志");
			AddLogLately addLogLately = map_addLogLately.get(key);
			if (addLogLately == null)
				return taskLogLately;

			LogLately[] logLately = addLogLately.getLogLately();
			if (logLately == null)
				return taskLogLately;

			String taskid = logLately[0].getTaskId();
			String[] taskNameType = TaskDao.getInstance().getTaskName_TypeFromTaskID(Long.valueOf(taskid));
			String scheid = logLately[0].getScheID();
			String scheName = ScherDao.getInstance().getScheNameFromScheCode(scheid);
			String groupid = logLately[0].getGroupId();
			String groupName = TaskGroupDao.getInstance().getGroupNameFromGroupId(groupid);
			String taskName = taskNameType[0];
			String tasktype = taskNameType[1];

			logLately = bubbleOrder(logLately, 2);
			int len = logLately.length;
			for (int i = 0; i < len; i++) {
				if (logLately[i] == null)
					break;
				try {
					if (UtilMath.compare(map.get("比较类型"), logLately[i].getExceTime(), Long.valueOf(map.get("执行时间")))) {
						Vector<String> rowValue = new Vector<String>();
						rowValue.add(scheid);
						rowValue.add(scheName);

						String execStatus = logLately[i].getExceStatus();
						String exexResult = logLately[i].getExceResult();
						String execTime = String.valueOf(logLately[i].getExceTime());
						String starttime = logLately[i].getStartTime();
						String endtime = logLately[i].getEndTime();
						rowValue.add(execStatus);
						rowValue.add(exexResult);
						rowValue.add(execTime);
						rowValue.add(starttime);
						rowValue.add(endtime);
						rowValue.add(groupid);
						rowValue.add(groupName);
						rowValue.add(taskid);
						rowValue.add(taskName);
						rowValue.add(tasktype);
						taskLogLately.add(rowValue);
					}
				} catch (Exception e) {
					Log.logError("查询最近执行高频日志错误:执行时间参数应为数字:", e);
					return null;
				}
			}
		} catch (Exception e) {
			Log.logError("获取最近执行高频日志表格参数SQL错误:", e);

		}
		return taskLogLately;
	}

	// 构造
	public LogLatelyDao() {
	}

	// 冒泡排序
	private LogLately[] bubbleOrder(LogLately[] array, int method) {

		for (int i = 0; i < array.length; i++) {
			for (int j = array.length - 1; j > i; j--) {
				if (method == 2) {
					if (array[i] == null)
						continue;
					if (array[j] == null)
						continue;
					if (array[i].getStartTime() == null)
						continue;
					if (array[j].getStartTime() == null)
						continue;
					if (array[i].getStartTime().compareToIgnoreCase(array[j].getStartTime()) < 0)
						swap(array, i, j);
				} else if (method == 1) {
					if (array[i] == null)
						continue;
					if (array[j] == null)
						continue;
					if (array[i].getStartTime() == null)
						continue;
					if (array[j].getStartTime() == null)
						continue;
					if (array[i].getStartTime().compareToIgnoreCase(array[j].getStartTime()) > 0)
						swap(array, i, j);
				}
			}
		}
		return array;
	}

	// 交换元素
	private void swap(LogLately[] array, int i, int j) {
		LogLately tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	// public Vector<?> getLogLatelyVector(Map<String, String> map,
	// Map<String, AddLogLately> map_addLogLately) {
	// Vector<Vector<String>> taskLogLately = new Vector<Vector<String>>();
	// try {
	// if (map == null)
	// return taskLogLately;
	// String key = map.get("调度标志") + "|" + map.get("任务组标志") + "|"
	// + map.get("任务标志");
	// AddLogLately addLogLately = map_addLogLately.get(key);
	// if (addLogLately == null)
	// return taskLogLately;
	//
	// LogLately[] logLately = addLogLately.getLogLately();
	// int index = addLogLately.getIndex();
	// if (logLately == null)
	// return taskLogLately;
	//
	// String taskid = logLately[0].getTaskId();
	// String[] taskNameType = TaskDao.getInstance()
	// .getTaskName_TypeFromTaskID(Long.valueOf(taskid));
	// String scheid = logLately[0].getScheID();
	// String scheName = ScherDao.getInstance().getScheNameFromScheID(
	// scheid);
	// String groupid = logLately[0].getGroupId();
	// String groupName = TaskGroupDao.getInstance()
	// .getGroupNameFromGroupId(groupid);
	// String taskName = taskNameType[0];
	// String tasktype = taskNameType[1];
	//
	// int len = logLately.length;
	// for (int i = index - 1; i >= 0; i--) {
	// if (logLately[i] == null)
	// break;
	//
	// try {
	// if (Fun.compare(map.get("比较类型"),
	// logLately[i].getExceTime(), Long.valueOf(map
	// .get("执行时间")))) {
	// Vector<String> rowValue = new Vector<String>();
	// rowValue.add(scheid);
	// rowValue.add(scheName);
	//
	// String execStatus = logLately[i].getExceStatus();
	// String exexResult = logLately[i].getExceResult();
	// String execTime = String.valueOf(logLately[i]
	// .getExceTime());
	// String starttime = logLately[i].getStartTime();
	// String endtime = logLately[i].getEndTime();
	// rowValue.add(execStatus);
	// rowValue.add(exexResult);
	// rowValue.add(execTime);
	// rowValue.add(starttime);
	// rowValue.add(endtime);
	// rowValue.add(groupid);
	// rowValue.add(groupName);
	// rowValue.add(taskid);
	// rowValue.add(taskName);
	// rowValue.add(tasktype);
	// taskLogLately.add(rowValue);
	// }
	// } catch (Exception e) {
	// Log.logError("查询最近执行高频日志错误:执行时间参数应为数字:", e);
	// return null;
	// }
	// }
	//
	// for (int i = len - 1; i >= index; i--) {
	// if (logLately[i] == null)
	// break;
	// try {
	// if (Fun.compare(map.get("比较类型"),
	// logLately[i].getExceTime(), Long.valueOf(map
	// .get("执行时间")))) {
	// Vector<String> rowValue = new Vector<String>();
	// rowValue.add(scheid);
	// rowValue.add(scheName);
	//
	// String execStatus = logLately[i].getExceStatus();
	// String exexResult = logLately[i].getExceResult();
	// String execTime = String.valueOf(logLately[i]
	// .getExceTime());
	// String starttime = logLately[i].getStartTime();
	// String endtime = logLately[i].getEndTime();
	// rowValue.add(execStatus);
	// rowValue.add(exexResult);
	// rowValue.add(execTime);
	// rowValue.add(starttime);
	// rowValue.add(endtime);
	// rowValue.add(groupid);
	// rowValue.add(groupName);
	// rowValue.add(taskid);
	// rowValue.add(taskName);
	// rowValue.add(tasktype);
	// taskLogLately.add(rowValue);
	// }
	// } catch (Exception e) {
	// Log.logError("查询最近执行高频日志错误:执行时间参数应为数字:", e);
	// return null;
	// }
	// }
	// // for (int i = 0; i < index; i++) {
	//
	// } catch (Exception e) {
	// Log.logError("获取最近执行高频日志表格参数SQL错误:", e);
	//
	// }
	// return taskLogLately;
	// }
}
