package com.monitor;

import java.util.Vector;
import javax.swing.JTable;

import com.log.Log;
import common.component.STable;
import common.component.STableBean;

public class MonitorGroupTable {
	private JTable jtable;

	// 构造监控组表格
	public MonitorGroupTable(String mName) {
		try {
			Vector<String> columnName = new Vector<String>();
			columnName.add("监控组标志");
			columnName.add("监控组名称");
			columnName.add("监控组备注");

			Vector<?> tableValue = MonitorGroupDao.getInstance()
					.getMonitorGroupVector(mName);
			int[] cellEditableColumn = null;
			int[] columnWidth = null;
			int[] columnHide = null;
			boolean isChenckHeader = false;
			boolean isReorderingAllowed = false;
			boolean isResizingAllowed = true;
			STableBean bean = new STableBean(columnName, tableValue,
					cellEditableColumn, columnWidth, columnHide,
					isChenckHeader, isReorderingAllowed, isResizingAllowed);
			STable table = new STable(bean);
			jtable = table.getJtable();
		} catch (Exception e) {
			Log.logError("监控员信息列表构造错误:", e);
		} finally {
		}
	}

	public JTable getJtable() {
		return jtable;
	}

}
