package com.threadPool;

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

public class ThreadTable {
	private JTable jtable;

	// 获取表格列名
	private Vector<String> getColumnName() {
		Vector<String> columnName = new Vector<String>();
		String[] keyName = new String[] { "全选", "调度标志", "任务组标志", "任务标志", "任务组任务顺序", "多线程任务标志" };

		for (int i = 0; i < keyName.length; i++) {
			columnName.add(keyName[i]);
		}
		columnName.add("线程状态");
		return columnName;
	}

	// 根据表格参数构造线程表格
	public ThreadTable(Map<String, String> map) {
		try {
			Vector<String> columnName = getColumnName();
			Vector<?> tableValue = ThreadDao.getInstance().getThreadVector(map);
			int[] cellEditableColumn = new int[] { 0 };
			int[] columnWidth = new int[] { 50, 100, 100, 100, 100, 100, 100 };
			int[] columnHide = null;
			boolean isChenckHeader = true;
			boolean isReorderingAllowed = false;
			boolean isResizingAllowed = true;
			STableBean bean = new STableBean(columnName, tableValue, cellEditableColumn, columnWidth, columnHide, isChenckHeader, isReorderingAllowed, isResizingAllowed);

			STable table = new STable(bean);
			// table.setPmenu(getPmenu());
			this.jtable = table.getJtable();
			this.jtable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					if (evt.getClickCount() == 2) {
						mouseDoubleClick();
					}
				}
			});

			DefaultTableCellRenderer dcr = new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component cell = null;

					cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

					cell.setBackground(Color.white);
					if (column == 6) {
						if (table.getValueAt(row, column) != null) {
							if (table.getValueAt(row, column).equals("完成")) {
								cell.setBackground(Color.green);
							} else if (table.getValueAt(row, column).equals("取消")) {
								cell.setBackground(Color.LIGHT_GRAY);
							} else if (table.getValueAt(row, column).equals("运行中")) {
								cell.setBackground(Color.yellow);
							} else
								cell.setBackground(Color.white);
						}
					}
					return cell;
				}
			};
			this.jtable.getColumn(columnName.get(6)).setCellRenderer(dcr);
		} catch (Exception e) {
			Log.logError("线程列表构造错误:", e);
		} finally {
		}

	}

	// private JPopupMenu getPmenu() {
	// JPopupMenu ppmenu_task = new JPopupMenu();
	// try {
	// final SMenuItem itemsExecTask = new SMenuItem("停止线程");
	// itemsExecTask.setFont(Constants.tfont);
	// itemsExecTask.setIcon(new ImageIcon(ImageContext.EXEUSCHE));
	// itemsExecTask.addActionListener(new ActionListener() {// 浮动菜单
	// public void actionPerformed(ActionEvent arg0) {
	// stopThread();
	// }
	// });
	//
	// ppmenu_task.add(itemsExecTask);
	// final SMenuItem itemsSeparator = new SMenuItem("————");
	// itemsSeparator.setFont(Constants.tfont);
	// ppmenu_task.add(itemsSeparator);
	//
	// } catch (Exception e) {
	// Log.logError("线程列表刷新错误:", e);
	// } finally {
	// }
	// return ppmenu_task;
	// }

	// 双击事件
	private void mouseDoubleClick() {
		try {

		} catch (Exception e) {
			Log.logError("线程列表双击错误:", e);
		} finally {
		}
	};

	// 获取线程表格
	public JTable getJtable() {
		return this.jtable;
	}

	// public static void main(String[] arg) {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("Type", "全部");
	// ThreadTable ts = new ThreadTable(map);
	// JFrame frame = new JFrame("sjh");
	// frame.setLayout(null);
	// frame.setSize(1100, 600);
	// SScrollPane src = new SScrollPane(ts.jtable);
	// src.setBounds(0, 0, 1000, 200);
	// frame.add(src);
	// frame.setVisible(true);
	// }

}
