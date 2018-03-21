package com.log.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.log.Log;
import common.component.STable;
import common.component.STableBean;

public class LogCommonTable {
	private JTable jtable;

	// 构造
	public LogCommonTable(Map<String, String> map) {
		try {
			Vector<String> columnName = new Vector<String>();
			columnName.add("调度标志");
			columnName.add("调度名称");
			columnName.add("开始时间");
			columnName.add("结束时间");
			columnName.add("执行状态");
			columnName.add("执行结果");
			columnName.add("任务组标志");
			columnName.add("任务组名称");
			columnName.add("任务标志");
			columnName.add("任务名称");
			columnName.add("任务类型");

			Vector<?> tableValue = LogCommonDao.getInstance().getLogCommonVector(map);
			int[] cellEditableColumn = null;
			int[] columnWidth = new int[] { 60, 120, 120, 120, 60, 340, 60, 120, 50, 120, 100 };
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
			DefaultTableCellRenderer dcr = new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table, Object value,

				boolean isSelected, boolean hasFocus, int row, int column) {
					Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					if (table.getValueAt(row, column) != null) {
						if (table.getValueAt(row, column).equals("执行失败") || table.getValueAt(row, column).equals("执行警告") || table.getValueAt(row, column).equals("执行提示")) {
							cell.setBackground(Color.yellow);
						} else
							cell.setBackground(Color.WHITE);
					}
					return cell;
				}
			};

			jtable.getColumn(columnName.get(4)).setCellRenderer(dcr);
		} catch (Exception e) {
			Log.logError("执行结果列表构造错误:", e);
		} finally {

		}
	}

	// 双击显示执行日志
	private void mouseDoubleClick() {
		try {
			String execresult = "";
			if (jtable.getValueAt(jtable.getSelectedRow(), 2) == null)
				execresult = execresult + "开始时间  :" + "\n";
			else
				execresult = execresult + "开始时间  :" + jtable.getValueAt(jtable.getSelectedRow(), 2).toString() + "\n";
			if (jtable.getValueAt(jtable.getSelectedRow(), 3) == null)
				execresult = execresult + "结束时间  :" + "\n";
			else
				execresult = execresult + "结束时间  :" + jtable.getValueAt(jtable.getSelectedRow(), 3).toString() + "\n";
			if (jtable.getSelectedRowCount() > 0)
				execresult = execresult + "调度标志  :" + jtable.getValueAt(jtable.getSelectedRow(), 0).toString() + "\n";

			else
				execresult = execresult + "调度标志  :" + "\n";
			if (jtable.getValueAt(jtable.getSelectedRow(), 1) == null)
				execresult = execresult + "调度名称  :" + "\n";
			else
				execresult = execresult + "调度名称  :" + jtable.getValueAt(jtable.getSelectedRow(), 1).toString() + "\n";
			if (jtable.getValueAt(jtable.getSelectedRow(), 6) == null)
				execresult = execresult + "任务组标志:" + "\n";
			else
				execresult = execresult + "任务组标志:" + jtable.getValueAt(jtable.getSelectedRow(), 6).toString() + "\n";
			if (jtable.getValueAt(jtable.getSelectedRow(), 7) == null)
				execresult = execresult + "任务组名称:" + "\n";
			else
				execresult = execresult + "任务组名称:" + jtable.getValueAt(jtable.getSelectedRow(), 7).toString() + "\n";
			String taskId = "";
			if (jtable.getValueAt(jtable.getSelectedRow(), 8) == null)
				execresult = execresult + "任务标志  :" + "\n";
			else {
				taskId = jtable.getValueAt(jtable.getSelectedRow(), 8).toString();
				execresult = execresult + "任务标志  :" + taskId + "\n";
			}
			String taskName = "";
			if (jtable.getValueAt(jtable.getSelectedRow(), 9) == null)
				execresult = execresult + "任务名称  :" + "\n";
			else {
				taskName = jtable.getValueAt(jtable.getSelectedRow(), 9).toString();
				execresult = execresult + "任务名称  :" + taskName + "\n";
			}
			if (jtable.getValueAt(jtable.getSelectedRow(), 10) == null)
				execresult = execresult + "任务类型  :" + "\n";
			else
				execresult = execresult + "任务类型  :" + jtable.getValueAt(jtable.getSelectedRow(), 10).toString() + "\n";
			if (jtable.getValueAt(jtable.getSelectedRow(), 4) == null)
				execresult = execresult + "执行状态  :" + "\n";
			else
				execresult = execresult + "执行状态  :" + jtable.getValueAt(jtable.getSelectedRow(), 4).toString() + "\n";
			if (jtable.getValueAt(jtable.getSelectedRow(), 5) == null)
				execresult = execresult + "执行结果  :" + "\n";
			else
				execresult = execresult + "执行结果  :" + jtable.getValueAt(jtable.getSelectedRow(), 5).toString() + "\n";
			String title = taskName + "[" + taskId + "]";
			Log.showLog(title, execresult, true);
		} catch (Exception e) {
			Log.logError("执行结果列表双击错误:", e);
		} finally {
		}
	};

	public JTable getJtable() {
		return jtable;
	}

}
