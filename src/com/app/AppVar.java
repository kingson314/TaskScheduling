package com.app;

import java.util.concurrent.ConcurrentHashMap;

import com.taskInterface.ITask;
import com.taskInterface.TaskDao;

public class AppVar {
	/**
	 * @Description:
	 * @date May 9, 2014
	 * @author:fgq
	 */

	public static String[] TASK_TYPE = new String[] { "文件分发" };
	public static String[] TASK_TYPE_Combobox = null;
	public static String[] State = new String[] { "0", "-1" };
	public static String[] StateText = new String[] { "正常", "隐藏" };

	// public static String logType = "1"; // 日志级别 -1代表什么都不记录;0 代表只记录错误；1
	// 代表记录错误以及 错误代码；2
	// 代表只记录错误代码
	// 任务里所有的异常用1
	// public static final int[] LogType = { 0, 1, 2, 3 };

	// 任务调度的短信报警全局变量 key:由 调度ID+"|"+ 任务组ID +"|"+任务ID+"|"
	// MapTaskForWatch守护线程写日志时用到的task,与MapTask内容一样，使用此map不改变MapTask的状态，并且不须使用clone
	private static ConcurrentHashMap<Long, ITask> MapTaskForWatch = new ConcurrentHashMap<Long, ITask>();
	public static ConcurrentHashMap<String, Long> MapTaskStartTime = new ConcurrentHashMap<String, Long>();

	// 存放任务执行的开始时间
	public static void putMapTaskStartTime(ITask task, String key, long value) {
		if (task.getOverTime() > 0)
			MapTaskStartTime.put(key, value);
	}

	// 根据Id获取任务实例
	public static ITask getMapTaskForWatch(Long taskId) {
		ITask task = MapTaskForWatch.get(taskId);
		if (task == null) {
			task = TaskDao.getInstance().getMapTask(taskId);
			MapTaskForWatch.put(taskId, task);
		}
		return task;
	}

}
