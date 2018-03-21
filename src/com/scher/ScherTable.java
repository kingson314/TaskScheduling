package com.scher;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


import com.log.Log;
import com.taskInterface.TaskDao;

import common.component.SMenuItem;
import common.component.STable;
import common.component.STableBean;
import common.util.string.UtilString;
import consts.ImageContext;

/**
 * @author: fenggq
 * @Email:
 * @Company:
 * @Action:调度表格
 * @DATE: Mar 8, 2012
 */
public class ScherTable {
	private JTable jtable;

	public ScherTable(Map<String, String> map) {
		try {
			Vector<String> columnName = new Vector<String>();
			columnName.add("");
			columnName.add("调度编号");
			columnName.add("调度标志");
			columnName.add("调度名称");
			columnName.add("任务组标志");
			columnName.add("任务标志");
			columnName.add("上次完成时间");
			columnName.add("下次执行时间");
			columnName.add("调度状态");
			columnName.add("调度类型");
			columnName.add("日期类型");
			columnName.add("执行时段");
			columnName.add("调度说明");
			Vector<?> tableValue = ScherDao.getInstance().getScheVector(map);
			int[] cellEditableColumn = new int[] { 0 };
			int[] columnWidth = new int[] { 30, 60, 60, 300, 70, 60, 120, 120, 60, 60, 110, 250, 300 };
			int[] columnHide = null;
			boolean isChenckHeader = true;
			boolean isReorderingAllowed = false;
			boolean isResizingAllowed = true;
			STableBean bean = new STableBean(columnName, tableValue, cellEditableColumn, columnWidth, columnHide, isChenckHeader, isReorderingAllowed, isResizingAllowed);

			STable table = new STable(bean);
			table.setPmenu(getPmenu());
			jtable = table.getJtable();
			jtable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) { // if
					if (e.getClickCount() == 2)
						mouseDoubleClick();
				}
			});

			DefaultTableCellRenderer dcr = new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component cell = null;

					cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

					cell.setBackground(Color.white);
					if (column == 8) {
						if (table.getValueAt(row, column) != null) {
							if (table.getValueAt(row, column).equals("正常")) {
								cell.setBackground(Color.green);
							} else if (table.getValueAt(row, column).equals("结束")) {
								cell.setBackground(Color.LIGHT_GRAY);
							} else if (table.getValueAt(row, column).equals("停止")) {
								cell.setBackground(Color.yellow);
							} else
								cell.setBackground(Color.white);
						}
					}
					return cell;
				}
			};
			jtable.getColumn(columnName.get(8)).setCellRenderer(dcr);

		} catch (Exception e) {
			Log.logError("调度列表构造错误:", e);
		} finally {
		}
	}

	// 浮动菜单

	private JPopupMenu getPmenu() {
		final JPopupMenu pmSche = new JPopupMenu();
		try {

			final SMenuItem miGetStatus = new SMenuItem("查看执行状态", ImageContext.ViewStatus);
			miGetStatus.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							showFinished();
						}
					});

			pmSche.add(miGetStatus);

			final SMenuItem miResetStatus = new SMenuItem("重置执行状态", ImageContext.ModStatus);
			miResetStatus.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							reSetFinished();
						}
					});

			pmSche.add(miResetStatus);
			pmSche.addSeparator();
			final SMenuItem miUpdateOrder = new SMenuItem("更新编号", ImageContext.Refresh);
			miUpdateOrder.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							updateOrder();
						}
					});
			pmSche.add(miUpdateOrder);
		} catch (Exception e) {
			Log.logError("调度列表刷新错误:", e);
		} finally {
		}
		return pmSche;
	}

	// 获取调度执行结束状态
	private String getFinishedKey() {
		if (this.jtable.getSelectedRowCount() <= 0)
			return null;
		String scheId = this.jtable.getValueAt(this.jtable.getSelectedRow(), 2).toString();
		ScherParam scher = ScherDao.getInstance().getScheParamsFromSchCde(Long.valueOf(scheId));

		Long[][] taskId_Order = null;
		String groupId = null;
		if (scher.getGroupID() == null) {// 任务处理
			taskId_Order = new Long[1][1];
			taskId_Order[0][0] = Long.valueOf(scher.getTaskID());
			groupId = "";
		} else if (scher.getGroupID().length() <= 0) {// 任务处理
			taskId_Order = new Long[1][1];
			taskId_Order[0][0] = Long.valueOf(scher.getTaskID());
			groupId = "";
		} else {// 任务组处理
			groupId = scher.getGroupID();
			taskId_Order = TaskDao.getInstance().GetTaskIdTaskOrderFromGroupId(Long.valueOf(groupId));
		}
		String key = "";
		if (taskId_Order.length > 0) {
			if (Integer.valueOf(scher.getSchType()) == 0) {// 调度串行
				key = scher.getSchCde() + "|" + groupId + "|" + "|";
			} else if (Integer.valueOf(scher.getSchType()) == 1) {// 调度并发

				final String GroupId = groupId;
				final String ScheCod = scher.getSchCde();
				for (int i = 0; i < taskId_Order.length; i++) {
					String tOrder = "";
					if (GroupId.equals("")) {
					} else {
						tOrder = String.valueOf(taskId_Order[i][1]);
					}
					final String TaskOrder = tOrder;
					key = ScheCod + "|" + GroupId + "|" + String.valueOf(taskId_Order[i][0]) + "|" + TaskOrder;
				}
			}
		}

		return key;

	}

	// 显示调度是否完成
	private void showFinished() {
		String key = getFinishedKey();
		Integer finished = -1;
		if (ScherExecJob.MapScherFinished.get(key) == null)
			finished = 1;
		else
			finished = ScherExecJob.MapScherFinished.get(key);
		if (finished != null)
			Log.logInfo(key + ": " + finished);
		else
			Log.logInfo(key + ": " + "调度尚未启动");

	}

	// 重置调度执行为已完成状态
	private void reSetFinished() {
		String key = getFinishedKey();
		Integer finished = -1;
		System.out.println(ScherExecJob.MapScherFinished.get(key));
		if (ScherExecJob.MapScherFinished.get(key) == null)
			finished = 1;
		else
			finished = ScherExecJob.MapScherFinished.get(key);
		System.out.println(finished);
		if (finished == null)
			Log.logInfo(key + ": " + "调度尚未启动");
		else if (finished != 1)
			ScherExecJob.MapScherFinished.put(key, 1);
	}

	// 双击编辑调度

	private void mouseDoubleClick() {
		try {
			ScherTab.getInstance().editSche(this.jtable);
		} catch (Exception e) {
			Log.logError("调度列表双击错误:", e);
		} finally {
		}
	};

	// 更新调度顺序号
	private void updateOrder() {
		int maxOrderLen = ScherDao.getInstance().getMaxOrderLen();
		for (int i = 0; i < this.jtable.getRowCount(); i++) {
			String curOrder = this.jtable.getValueAt(i, 1).toString();
			if (curOrder.length() >= maxOrderLen)
				continue;
			long id = Long.valueOf(this.jtable.getValueAt(i, 2).toString());
			String order = UtilString.expandOrder(curOrder, maxOrderLen);
			ScherDao.getInstance().modOrder(id, order);
		}
		ScherTab.getInstance().querySche();
	}

	// 获取调度表格
	public JTable getJtable() {
		return jtable;
	}

	// public static void main(String[] arg) {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("Type", "全部");
	// JTable jtable = new ScherTable(map).getJtable();
	// JFrame frame = new JFrame("sjh");
	// frame.setLayout(null);
	// frame.setSize(1100, 600);
	// SScrollPane src = new SScrollPane(jtable);
	// src.setBounds(0, 0, 1000, 200);
	// frame.add(src);
	// frame.setVisible(true);
	// }

}
