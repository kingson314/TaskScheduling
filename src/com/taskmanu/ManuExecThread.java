package com.taskmanu;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JTable;

import com.app.AppLogView;
import com.app.AppVar;
import com.log.Log;
import com.log.common.LogCommonDao;
import com.log.common.LogCommonDialg;
import com.taskInterface.ITask;
import com.taskInterface.TaskDao;
import com.taskgroup.TaskGroup;
import com.taskgroup.TaskGroupDao;

import common.util.conver.UtilConver;
import common.util.date.UtilDate;
import common.util.string.UtilString;
import consts.Const;
import consts.VariableApp;

//手工执行线程
public class ManuExecThread extends Thread {
	private JTable jtable;
	private Long[] groupId;
	private Long[] taskId;
	private String[] params;

	// 启动线程
	public void run() {
		try {
			String maxupdateTime = "";
			String startTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
			if ("1".equals(UtilString.isNil(VariableApp.systemParamsValue.getShowExecTaskresult(), "0"))) {
				maxupdateTime = LogCommonDao.getInstance().getExecUpdateTime();
			}
			
			if (groupId == null) {// 任务
				for (int i = 0; i < taskId.length; i++) {
					String startTimeTask = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
					Long execTime = System.currentTimeMillis();
					// 执行任务
					ITask task = TaskDao.getInstance().getMapTask(taskId[i]);
					task.setNowDate(UtilConver.dateToStr(Const.fm_yyyyMMdd));
					Log.showLog(AppLogView.LogSystem, startTimeTask + ":任务[" + task.getTaskId() + "]:" + task.getTaskName() + " 开始执行...", true);
					execTask(startTimeTask, "", "-1", "", task, 0, this.params, execTime);
				}
			} else {// 任务组
				for (int j = 0; j < groupId.length; j++) {
					Long[][] taskIdOrder = TaskDao.getInstance().GetTaskIdTaskOrderFromGroupId(groupId[j]);
					TaskGroup taskGroup = TaskGroupDao.getInstance().getTaskGroupFromGroupID(groupId[j]);
					int taskGroupErrorDeal = taskGroup.getErrorDeal();
					for (int i = 0; i < taskIdOrder.length; i++) {
						Long execTime = System.currentTimeMillis();
						String startTimeTask  =UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
						// 执行任务
						ITask task = TaskDao.getInstance().getMapTask(taskIdOrder[i][0]);
						task.setNowDate(UtilConver.dateToStr(Const.fm_yyyyMMdd));
						Log.showLog(AppLogView.LogSystem, "任务组[" + taskGroup.getGroupId() + "]:" + taskGroup.getGroupName() + " 任务[" + task.getTaskId() + "]: "
								+ task.getTaskName() + " 开始执行...", true);
						if (!execTask(startTimeTask, String.valueOf(groupId[j]), "-1", String.valueOf(taskIdOrder[i][1]), task, taskGroupErrorDeal, this.params, execTime))
							break;
					}

				}
			}
			if ("1".equals(UtilString.isNil(VariableApp.systemParamsValue.getShowExecTaskresult(), "0"))) {
				LogCommonDialg inst = new LogCommonDialg(maxupdateTime);
				inst.setVisible(true);
			}
			Log.showLog(AppLogView.LogSystem, "本次手工执行顺利完成! 执行时段["+this.params[0]+","+this.params[1]+"];执行耗时[" +UtilDate.diffSecond(startTime, UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss), Const.fm_yyyyMMdd_HHmmss)+"秒] 从" +startTime +" 到 " + UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss) , true);
		} catch (Exception e) {
			Log.logError("手工执行任务错误:", e);
		} finally {
		}
	}

	// 执行任务
	private boolean execTask(String startTime, String groupId, String scheId, String taskOrder, ITask task, int taskGroupErrorDeal, String[] params, Long execTime) {
		boolean rs = true;
		try {
			String key = "-1|" + groupId + "|" + String.valueOf(task.getTaskId()) + "|" + taskOrder;
			if ("存储过程".equals(task.getTaskType()) || "形态分析".equals(task.getTaskType())|| "时点价差分析".equals(task.getTaskType())|| "时点价差极值范围比率分析".equals(task.getTaskType())) {
				AppVar.putMapTaskStartTime(task, key, System.currentTimeMillis());
				ITask newTask = (ITask) task.clone();
				newTask.manuExecTask(params);
				String endTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
				Log.taskLog(newTask, "", scheId, startTime, endTime, System.currentTimeMillis() - execTime);
				AppVar.putMapTaskStartTime(newTask, key, 0l);
			} else {
				Calendar calendar = Calendar.getInstance();
				Date begDate = UtilConver.strToDate(params[0], Const.fm_yyyyMMdd);
				Date endDate = UtilConver.strToDate(params[1], Const.fm_yyyyMMdd);
				Date tmpDate = begDate;
				while (endDate.compareTo(tmpDate) >= 0) {
					AppVar.putMapTaskStartTime(task, key, System.currentTimeMillis());
					startTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
					calendar.setTime(tmpDate);
					task.setNowDate(UtilConver.dateToStr(calendar.getTime(), Const.fm_yyyyMMdd));
					task.fireTask(startTime, groupId, scheId, taskOrder);
					String endTime = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
					String taskStatus = task.getTaskStatus();// takeLogCommon清空了状态
					Log.taskLog(task, groupId, scheId, startTime, endTime, System.currentTimeMillis() - execTime);
					AppVar.putMapTaskStartTime(task, key, 0l);
					if ("执行失败".equals(taskStatus)) {
						if (taskGroupErrorDeal == 1) {
							rs = false;
							// 错误退出
							break;
						}
					}
					calendar.add(Calendar.DAY_OF_YEAR, 1);
					tmpDate = calendar.getTime();
				}
			}
		} catch (Exception e) {
			Log.logError("手工执行任错误:", e);
		}
		return rs;
	}

	// 手工执行任务时可选参数
	public ManuExecThread(Long[] groupId, Long[] taskId, String[] optionParam) {
		this.groupId = groupId;
		this.taskId = taskId;
		this.params = optionParam;
	}

	public JTable getJtable() {
		return jtable;
	}

	public void setJtable(JTable jtable) {
		this.jtable = jtable;
	}

}
