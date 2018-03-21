package com.settings;

import java.util.Vector;
import javax.swing.JTable;

import com.log.Log;
import common.component.STable;
import common.component.STableBean;

public class SettingsTable {
	private JTable jtable;

	// 构造
	public SettingsTable(String sName) {
		try {
			Vector<String> columnName = new Vector<String>();
			columnName.add("配置标志");
			columnName.add("配置名称");
			columnName.add("配置值");
			columnName.add("配置说明");

			Vector<?> tableValue = SettingsDao.getInstance().getSettingsVector(sName);
			int[] cellEditableColumn = null;
			int[] columnWidth = new int[] { 50, 100, 300, 200 };
			int[] columnHide = null;
			boolean isChenckHeader = false;
			boolean isReorderingAllowed = false;
			boolean isResizingAllowed = true;
			STableBean bean = new STableBean(columnName, tableValue, cellEditableColumn, columnWidth, columnHide, isChenckHeader, isReorderingAllowed, isResizingAllowed);
			STable table = new STable(bean);
			jtable = table.getJtable();
		} catch (Exception e) {
			Log.logError("配置信息列表构造错误:", e);
		} finally {
		}
	}

	// 获取配置信息表格
	public JTable getJtable() {
		return jtable;
	}

}
