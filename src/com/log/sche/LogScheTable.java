package com.log.sche;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;

import com.log.Log;
import common.component.STable;
import common.component.STableBean;

public class LogScheTable {
	private JTable jtable;

	// 构造
	public LogScheTable(Map<String, String> map, Map<String, AddLogSche> map_addLogSche) {
		try {

			Vector<String> columnName = new Vector<String>();
			columnName.add("调度标志");
			columnName.add("执行信息");
			columnName.add("执行时间");
			columnName.add("开始时间");
			columnName.add("结束时间");

			Vector<?> tableValue = LogScheDao.getInstance().getExecLogScheVector(map, map_addLogSche);
			int[] cellEditableColumn = null;
			int[] columnWidth = new int[] { 60, 500, 60, 120, 120 };
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
			Log.logError("执行结果列表构造错误:", e);
		} finally {

		}
	}

	// 双击显示调度信息
	private void mouseDoubleClick() {
		try {
			String execresult = "";
			if (jtable.getValueAt(jtable.getSelectedRow(), 3) == null)
				execresult = execresult + "开始时间  :" + "\n";
			else
				execresult = execresult + "开始时间  :" + jtable.getValueAt(jtable.getSelectedRow(), 3).toString() + "\n";
			if (jtable.getValueAt(jtable.getSelectedRow(), 4) == null)
				execresult = execresult + "结束时间  :" + "\n";
			else
				execresult = execresult + "结束时间  :" + jtable.getValueAt(jtable.getSelectedRow(), 4).toString() + "\n";

			if (jtable.getValueAt(jtable.getSelectedRow(), 2) != null)
				execresult = execresult + "执行时间  :" + jtable.getValueAt(jtable.getSelectedRow(), 2).toString() + "\n";

			else
				execresult = execresult + "执行时间    :" + "\n";

			if (jtable.getValueAt(jtable.getSelectedRow(), 0) != null)
				execresult = execresult + "调度标志  :" + jtable.getValueAt(jtable.getSelectedRow(), 0).toString() + "\n";
			else
				execresult = execresult + "调度标志  :" + "\n";

			if (jtable.getValueAt(jtable.getSelectedRow(), 1) == null)
				execresult = execresult + "执行结果  :" + "\n";
			else
				execresult = execresult + "执行结果  :" + jtable.getValueAt(jtable.getSelectedRow(), 1).toString() + "\n";

			String title = "调度日志";
			Log.showLog(title, execresult, true);
		} catch (Exception e) {
			Log.logError("执行结果列表双击错误:", e);
		} finally {
		}
	};

	public JTable getJtable() {
		return jtable;
	}

	public void setJtable(JTable jtable) {
		this.jtable = jtable;
	}

}
