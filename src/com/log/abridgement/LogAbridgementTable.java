package com.log.abridgement;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;

import com.log.Log;
import com.log.lately.LogLatelyTab;
import common.component.STable;
import common.component.STableBean;

public class LogAbridgementTable {
	private JTable jtable;

	// 构造
	public LogAbridgementTable(Map<String, String> map, Map<String, LogAbridgement> map_LogAbridgement) {
		try {
			Vector<String> columnName = new Vector<String>();
			columnName.add("调度标志");
			columnName.add("调度名称");
			columnName.add("开始时间");
			columnName.add("结束时间");
			columnName.add("失败次数");
			columnName.add("提示次数");
			columnName.add("成功次数");
			columnName.add("总次数");
			columnName.add("总耗时");
			columnName.add("平均耗时");
			columnName.add("最大耗时");
			columnName.add("最小耗时");
			columnName.add("上次执行时间");
			columnName.add("任务组标志");
			columnName.add("任务组名称");
			columnName.add("任务标志");
			columnName.add("任务名称");
			columnName.add("任务类型");

			Vector<?> tableValue = LogAbridgementDao.getInstance().getLogAbridgementVector(map, map_LogAbridgement);
			int[] cellEditableColumn = null;
			int[] columnWidth = new int[] { 60, 120, 120, 120, 60, 60, 60, 60, 60, 60, 60, 60, 120, 120, 120, 60, 120, 100 };
			int[] columnHide = null;
			boolean isChenckHeader = false;
			boolean isReorderingAllowed = false;
			boolean isResizingAllowed = true;
			STableBean bean = new STableBean(columnName, tableValue, cellEditableColumn, columnWidth, columnHide, isChenckHeader, isReorderingAllowed, isResizingAllowed);
			STable table = new STable(bean);
			jtable = table.getJtable();

			jtable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					if (evt.getClickCount() == 2)
						mouseDoubleClick();
				}
			});
		} catch (Exception e) {
			Log.logError("高频日志摘要列表构造错误:", e);
		} finally {
		}
	}

	// 双击显示最近日志明细
	private void mouseDoubleClick() {
		LogLatelyTab.getInstance().queryLogLately(this.jtable);
	}

	public JTable getJtable() {
		return jtable;
	}
}
