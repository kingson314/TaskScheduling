package com.task.Watch;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;
import javax.swing.JTable;

import com.app.AppVar;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.TaskDao;

import common.component.STable;
import common.component.STableBean;
import common.util.conver.UtilConver;
import consts.Const;

//用于显示任务执行的时间，暂无展示
public class ExecTimeTable {
	private JTable jtable;

	// 表构造
	public ExecTimeTable() {
		try {
			int[] cellEditableColumn = null;
			int[] columnWidth = null;
			int[] columnHide = null;
			boolean isChenckHeader = true;
			boolean isReorderingAllowed = false;
			boolean isResizingAllowed = true;
			STableBean bean = new STableBean(getColumnName(), getTableValue(), cellEditableColumn, columnWidth, columnHide, isChenckHeader, isReorderingAllowed, isResizingAllowed);

			STable table = new STable(bean);
			jtable = table.getJtable();

		} catch (Exception e) {
			Log.logError("列表构造错误:", e);
		} finally {
		}
	}

	public JTable getJtable() {
		return jtable;
	}

	// 表值
	@SuppressWarnings("unchecked")
	private Vector<Vector<String>> getTableValue() {
		Vector<Vector<String>> tableValue = new Vector<Vector<String>>();
		Iterator<?> it = AppVar.MapTaskStartTime.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Long> entry = (Entry<String, Long>) it.next();
			String key = entry.getKey();
			Long startTime = entry.getValue();

			if (startTime == 0)// 该时间为0说明上次执行已完成，不进入超时统计
				continue;
			String[] keyValue = key.split("/|");
			// 守护线程本身不做统计
			if (keyValue[2].equals("0"))
				continue;
			Vector<String> rowValue = new Vector<String>();
			for (int i = 0; i < keyValue.length; i++) {
				rowValue.add(keyValue[i]);
			}

			long currentWastedTime = (System.currentTimeMillis() - startTime) / 1000;

			ITask task = TaskDao.getInstance().getMapTask(Long.valueOf(keyValue[2]));
			if (task == null)
				continue;
			long overTime = task.getOverTime() == null ? 0l : task.getOverTime();
			rowValue.add(String.valueOf(currentWastedTime));
			rowValue.add(String.valueOf(overTime));
			boolean isOver = overTime <= 0 ? false : (currentWastedTime > overTime);
			rowValue.add(isOver ? "是" : "否");
			rowValue.add(UtilConver.dateToStr(new Date(startTime), Const.fm_yyyyMMdd_HHmmss));
			rowValue.add(UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss));
			tableValue.add(rowValue);
		}
		return tableValue;
	}

	// 表头
	private Vector<String> getColumnName() {
		Vector<String> columnName = new Vector<String>();
		String[] keyName = new String[] { "调度标志", "任务组标志", "任务标志", "任务组任务顺序", "多线程任务标志" };

		for (int i = 0; i < keyName.length; i++) {
			columnName.add(keyName[i]);
		}
		columnName.add("当前耗时");
		columnName.add("超时时间");
		columnName.add("是否超时");
		columnName.add("开始时间");
		columnName.add("当前时间");
		return columnName;
	}
}
