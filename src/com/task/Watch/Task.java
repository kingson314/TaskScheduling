package com.task.Watch;

import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import module.datetype.NowDateDao;
import app.AppConfig;
import app.AppStatus;

import com.app.AppFun;
import com.app.AppVar;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.TaskAbstract;

import common.component.ShortcutManager;
import common.util.conver.UtilConver;
import common.util.string.UtilString;
import consts.Const;

//守护线程任务
public class Task extends TaskAbstract {
	private static String nowDate = "00000000";

	public Task() {
		this.setTaskId(0l);
		this.setTaskName("守护任务");
		this.setInterval(0l);
		this.setWarnType("");//全部
		this.setLogType("");//普通任务日志
		this.setOverTime(0l);
		this.setMonitorGroup("");
		this.setTaskStatus("");// 不记录成功日志
		this.setTaskMsg("");
		this.setTaskOrder("-1");
		this.setJsonStr("");
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		fireTask();

	}

	private void fireTask() {
		if (!watchTaskExecTime())
			return;
		// if (!watchExecDate())
		// return;
		watchAutoLockApp();
	}

	// 守护线程监控是否在系统无输入N秒后自动锁屏
	private void watchAutoLockApp() {
		try {
			// 如果没有使用登陆模块，则不锁屏
			// if (!AppConfig.getInstance().isUseAppLog())
			// return;
			long appLockTime = Long.valueOf(UtilString.isNil(AppConfig.getInstance().getMapAppConfig().get("appAutoLockTime"), "0"));
			if (appLockTime > 0) {
				if ((System.currentTimeMillis() - ShortcutManager.AppInputTime) > appLockTime * 1000) {
					AppFun.getInstance().systemLock();
				}
			}
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("守护线程监控是否在系统无输入N秒后自动锁屏错误:", e);
		}
	}

	// 守护线程获取当前执行日期（提供了更新当前日期接口后，此功能废除）
	@SuppressWarnings("unused")
	private boolean watchExecDate() {
		boolean rs = false;
		try {
			String ndate = UtilString.isNil(NowDateDao.nowDate, UtilConver.dateToStr(Const.fm_yyyyMMdd));
			if (!nowDate.equals(ndate)) {
				nowDate = ndate;
				AppStatus.getInstance().setStatus("nowDate", "程序当前执行日期:" + nowDate.substring(0, 4) + "-" + nowDate.substring(4, 6) + "-" + nowDate.substring(6, 8));

			}
			rs = true;
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("守护线程获取当前执行日期错误:", e);
		}
		return rs;
	}

	// 守护线程执监控任务执行超时
	@SuppressWarnings("unchecked")
	private boolean watchTaskExecTime() {
		boolean rs = false;
		try {
			Iterator<?> it = AppVar.MapTaskStartTime.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Long> entry = (Entry<String, Long>) it.next();
				String key = entry.getKey();
				// System.out.println("watch:"+key);
				Long startTime = entry.getValue();

				if (startTime == 0)// 该时间为0说明上次执行已完成，不进入超时统计
					continue;
				String[] keyValue = key.split("/|");
				// 守护线程本身不做统计
				if (keyValue[2].equals("0"))
					continue;

				long currentWastedTime = (System.currentTimeMillis() - startTime) / 1000;

				ITask task = AppVar.getMapTaskForWatch(Long.valueOf(keyValue[2]));
				if (task == null)
					continue;
				long overTime = task.getOverTime() == null ? 0l : task.getOverTime();

				boolean isOver = overTime <= 0 ? false : (currentWastedTime > overTime);

				if (isOver) {
					task.setTaskStatus("执行提示");
					task.setTaskMsg("执行超时:" + (currentWastedTime - overTime) + "秒");
					Log.taskLog(task, keyValue[1], keyValue[0], UtilConver.dateToStr(new Date(startTime), Const.fm_yyyyMMdd_HHmmss), UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss), currentWastedTime);
				}
			}
			rs = true;
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("守护线程执监控任务执行超时错误:", e);
		}
		return rs;
	}

	// public static void main(String[] args) {
	// String nDate = "20122532";
	// System.out.println(nDate.substring(0, 4) + "-" + nDate.substring(4, 6)
	// + "-" + nDate.substring(6, 8));
	// }
}
