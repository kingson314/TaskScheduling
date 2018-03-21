package com.log.sche;

import java.util.Map;
import java.util.Vector;

import com.log.Log;

import common.util.Math.UtilMath;

public class LogScheDao {
	public static LogScheDao logScheDao = null;

	public static LogScheDao getInstance() {
		if (logScheDao == null)
			logScheDao = new LogScheDao();
		return logScheDao;
	}

	// 查询来自当前内存的最近调度日志
	public Vector<?> getExecLogScheVector(Map<String, String> map, Map<String, AddLogSche> map_addLogSche) {
		Vector<Vector<String>> taskLogSche = new Vector<Vector<String>>();
		try {
			if (map == null)
				return taskLogSche;
			String key = map.get("调度标志");
			AddLogSche addLogSche = map_addLogSche.get(key);
			if (addLogSche == null)
				return taskLogSche;

			LogSche[] logSche = addLogSche.getLogSche();
			if (logSche == null)
				return taskLogSche;

			int len = logSche.length;
			logSche = bubbleOrder(logSche, 2);
			for (int i = 0; i < len; i++) {
				if (logSche[i] == null)
					break;
				try {
					if (UtilMath.compare(map.get("比较类型"), logSche[i].getExecTime(), Long.valueOf(map.get("执行时间")))) {
						String scheid = logSche[i].getScheId();
						String execMsg = logSche[i].getExecMsg();
						String startTime = logSche[i].getStartTime();
						String endTime = logSche[i].getEndTime();
						String execTime = String.valueOf(logSche[i].getExecTime());
						Vector<String> rowValue = new Vector<String>();
						rowValue.add(scheid);
						rowValue.add(execMsg);
						rowValue.add(execTime);
						rowValue.add(startTime);
						rowValue.add(endTime);
						taskLogSche.add(rowValue);
					}
				} catch (Exception e) {
					Log.logError("查询最近调度日志错误:执行时间参数应为数字:", e);
					return null;
				}
			}

		} catch (Exception e) {
			Log.logError("获取最近调度日志表格参数SQL错误:", e);

		}
		return taskLogSche;
	}

	// 冒泡法
	private LogSche[] bubbleOrder(LogSche[] array, int method) {

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

	// 元素交换
	private void swap(LogSche[] array, int i, int j) {
		LogSche tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
	// public Vector<?> getExecLogScheVector(Map<String, String> map,
	// Map<String, AddLogSche> map_addLogSche) {
	// Vector<Vector<String>> taskLogSche = new Vector<Vector<String>>();
	// try {
	// if (map == null)
	// return taskLogSche;
	// String key = map.get("调度标志");
	// AddLogSche addLogSche = map_addLogSche.get(key);
	// if (addLogSche == null)
	// return taskLogSche;
	//
	// LogSche[] logSche = addLogSche.getLogSche();
	// int index = addLogSche.getIndex();
	// if (logSche == null)
	// return taskLogSche;
	//
	// int len = logSche.length;
	//
	// for (int i = index - 1; i >= 0; i--) {
	// if (logSche[i] == null)
	// break;
	//
	// try {
	// if (Fun.compare(map.get("比较类型"), logSche[i].getExecTime(),
	// Long.valueOf(map.get("执行时间")))) {
	//
	// String scheid = logSche[i].getScheId();
	// String execMsg = logSche[i].getExecMsg();
	// String startTime = logSche[i].getStartTime();
	// String endTime = logSche[i].getEndTime();
	// String execTime = String.valueOf(logSche[i]
	// .getExecTime());
	// Vector<String> rowValue = new Vector<String>();
	// rowValue.add(scheid);
	// rowValue.add(execMsg);
	// rowValue.add(execTime);
	// rowValue.add(startTime);
	// rowValue.add(endTime);
	// taskLogSche.add(rowValue);
	// }
	// } catch (Exception e) {
	// Log.logError("查询最近调度日志错误:执行时间参数应为数字:", e);
	// return null;
	// }
	// }
	// for (int i = len - 1; i >= index; i--) {
	// if (logSche[i] == null)
	// break;
	// try {
	// if (Fun.compare(map.get("比较类型"), logSche[i].getExecTime(),
	// Long.valueOf(map.get("执行时间")))) {
	//
	// String scheid = logSche[i].getScheId();
	// String execMsg = logSche[i].getExecMsg();
	// String startTime = logSche[i].getStartTime();
	// String endTime = logSche[i].getEndTime();
	// String execTime = String.valueOf(logSche[i]
	// .getExecTime());
	// Vector<String> rowValue = new Vector<String>();
	// rowValue.add(scheid);
	// rowValue.add(execMsg);
	// rowValue.add(execTime);
	// rowValue.add(startTime);
	// rowValue.add(endTime);
	// taskLogSche.add(rowValue);
	// }
	// } catch (Exception e) {
	// Log.logError("查询最近调度日志错误:执行时间参数应为数字:", e);
	// return null;
	// }
	// }
	// // for (int i = 0; i < index; i++) {
	//
	// } catch (Exception e) {
	// Log.logError("获取最近调度日志表格参数SQL错误:", e);
	//
	// }
	// return taskLogSche;
	// }
}
