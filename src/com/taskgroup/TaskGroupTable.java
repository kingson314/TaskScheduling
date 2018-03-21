package com.taskgroup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JTable;


import com.log.Log;

import common.component.SMenuItem;
import common.component.STable;
import common.component.STableBean;
import common.component.ShowMsg;
import common.util.log.UtilLog;
import common.util.string.UtilString;
import common.util.swing.UtilComponent;
import consts.ImageContext;

//任务组表格
public class TaskGroupTable {
	private JTable jtable;

	// 根据参数map构造table
	public TaskGroupTable(Map<String, String> map) {
		try {
			Vector<String> columnName = new Vector<String>();
			columnName.add("");
			columnName.add("任务组编号");
			columnName.add("任务组标志");
			columnName.add("任务组名称");
			columnName.add("异常处理");
			columnName.add("执行类型");
			columnName.add("任务组说明");
			Vector<?> tableValue = TaskGroupDao.getInstance().getTaskGroupVector(map);
			int[] cellEditableColumn = new int[] { 0 };
			int[] columnWidth = new int[] { 30, 70, 70, 300, 80, 80, 400 };
			int[] columnHide = null;
			boolean isChenckHeader = true;
			boolean isReorderingAllowed = false;
			boolean isResizingAllowed = true;
			STableBean bean = new STableBean(columnName, tableValue, cellEditableColumn, columnWidth, columnHide, isChenckHeader, isReorderingAllowed, isResizingAllowed);

			STable table = new STable(bean);
			table.setPmenu(getPmenu());
			jtable = table.getJtable();

			this.jtable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					if (evt.getClickCount() == 2) {
						mouseDoubleClick();
					}
				}
			});

		} catch (Exception e) {
			Log.logError("任务组列表构造错误:", e);
		} finally {
		}
	}

	// 浮动菜单
	private JPopupMenu getPmenu() {
		JPopupMenu mTaskGroup = new JPopupMenu();
		try {
			final SMenuItem miExecTaskGroup = new SMenuItem("执行任务组", ImageContext.Exec);
			miExecTaskGroup.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							TaskGroupTab.getInstance().execTaskGroup();
						}
					});
			mTaskGroup.add(miExecTaskGroup);
			mTaskGroup.addSeparator();
			final SMenuItem miUpdateOrder = new SMenuItem("更新编号", ImageContext.Refresh);
			miUpdateOrder.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							updateOrder();
						}
					});
			mTaskGroup.add(miUpdateOrder);

			mTaskGroup.addSeparator();
			final SMenuItem miHideTaskGroup = new SMenuItem("隐藏任务组", ImageContext.Mod);
			miHideTaskGroup.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							modeState(-1);
						}
					});

			mTaskGroup.add(miHideTaskGroup);

			final SMenuItem miShowTaskGroup = new SMenuItem("显示任务组", ImageContext.Mod);
			miShowTaskGroup.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							modeState(0);
						}
					});
			mTaskGroup.add(miShowTaskGroup);
		} catch (Exception e) {
			Log.logError("错误:", e);
		} finally {
		}
		return mTaskGroup;
	}

	// 更新状态
	private void modeState(int state) {
		try {
			if (this.jtable == null) {
				ShowMsg.showMsg("请勾选记录");
				return;
			}
			int selectedCount = UtilComponent.getTableSelectedCount(this.jtable);
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选记录");
				return;
			}

			for (int i = 0; i < this.jtable.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(this.jtable.getValueAt(i, 0).toString());
				if (selected) {
					int taskId = Integer.valueOf(this.jtable.getValueAt(i, 2).toString());
					TaskGroupDao.getInstance().modState(taskId, state);
				}
			}
		} catch (Exception e) {
			UtilLog.logError("更新错误:", e);
		} finally {
		}
	}

	// 更新任务顺序号
	private void updateOrder() {
		int maxOrderLen = TaskGroupDao.getInstance().getMaxOrderLen();
		for (int i = 0; i < this.jtable.getRowCount(); i++) {
			String curOrder = this.jtable.getValueAt(i, 1).toString();
			if (curOrder.length() >= maxOrderLen)
				continue;
			long id = Long.valueOf(this.jtable.getValueAt(i, 2).toString());
			String order = UtilString.expandOrder(curOrder, maxOrderLen);
			TaskGroupDao.getInstance().modOrder(id, order);
		}
		TaskGroupTab.getInstance().queryTaskGroup();
	}

	// 双击编辑任务组
	private void mouseDoubleClick() {
		TaskGroupTab.getInstance().editTaskGroup();
	};

	public JTable getJtable() {
		return jtable;
	}

	// public static void main(String[] arg) {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("Type", "全部");
	// JTable jtable = new TaskGroupTable(map).getJtable();
	// JFrame frame = new JFrame("sjh");
	// frame.setLayout(null);
	// frame.setSize(1100, 600);
	// SScrollPane src = new SScrollPane(jtable);
	// src.setBounds(0, 0, 1000, 200);
	// frame.add(src);
	// frame.setVisible(true);
	// }
}
