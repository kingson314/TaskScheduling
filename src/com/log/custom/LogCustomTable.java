package com.log.custom;

import java.util.Vector;
import javax.swing.JTable;

import com.log.Log;
import common.component.STable;
import common.component.STableBean;

public class LogCustomTable {
	private JTable jtable;

	// 构造定制日志表格
	public LogCustomTable() {
		try {
			LogCustomDao logCustomDao = new LogCustomDao();
			Vector<String> columnName = logCustomDao.getTitle();
			Vector<?> tableValue = logCustomDao.getTableValue();
			int[] cellEditableColumn = null;
			int[] columnWidth = new int[] { 150, 200 };
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
			Log.logError("列表构造错误:", e);
		}
	}

	public JTable getJtable() {
		return jtable;
	}
}