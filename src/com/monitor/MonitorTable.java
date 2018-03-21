package com.monitor;

import java.util.Vector;
import javax.swing.JTable;

import com.log.Log;
import common.component.STable;
import common.component.STableBean;

public class MonitorTable {
	private JTable jtable;

	// 构造监控员表格
	public MonitorTable(String type, String sql) {
		try {
			Vector<String> columnName = new Vector<String>();
			columnName.add("全选");
			columnName.add("监控员标志");
			columnName.add("监控员名称");
			columnName.add("监控员电话");
			columnName.add("监控员邮箱地址");

			Vector<?> tableValue = MonitorDao.getInstance().getMonitorVector(sql);
			int[] cellEditableColumn = new int[] { 0 };
			int[] columnWidth = new int[] { 50, 150, 150, 150, 300 };
			int[] columnHide = null;
			if (type.equals("监控员面板")) {
				columnHide = new int[] { 0 };
			} else if (type.equals("监控组面板")) {
				columnHide = new int[] { 3, 4 };
			}

			boolean isChenckHeader = true;
			boolean isReorderingAllowed = false;
			boolean isResizingAllowed = true;
			STableBean bean = new STableBean(columnName, tableValue, cellEditableColumn, columnWidth, columnHide, isChenckHeader, isReorderingAllowed, isResizingAllowed);
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
