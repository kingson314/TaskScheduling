package com.taskInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;


import com.app.AppLogView;
import com.app.AppMain;
import com.log.Log;

import common.component.SMenuItem;
import common.component.STable;
import common.component.STableBean;
import common.component.ShowDialog;
import common.component.ShowMsg;
import common.util.conver.UtilConver;
import common.util.log.UtilLog;
import common.util.string.UtilString;
import common.util.swing.UtilComponent;
import consts.ImageContext;

public class TaskTable {
	private JTable jtable;

	// 根据map参数构造table
	public TaskTable(Map<String, String> map) {
		try {
			Vector<String> columnName = new Vector<String>();
			columnName.add("");
			columnName.add("任务序号");
			columnName.add("任务标志");
			columnName.add("任务名称");
			columnName.add("任务类型");
			columnName.add("日志类型");
			columnName.add("监控组");
			columnName.add("报警时间间隔(秒)");
			columnName.add("报警类型");
			columnName.add("任务超时阀值(秒)");
			columnName.add("任务说明");
			columnName.add("任务组任务顺序号");
			Vector<?> tableValue = TaskDao.getInstance().getTaskVector(map);
			int[] cellEditableColumn = new int[] { 0 };
			int[] columnWidth = new int[] { 35, 60, 60, 300, 120, 100, 100, 100, 100, 100, 400 };
			int[] columnHide = new int[] { 11 };
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
			Log.logError("任务列表构造错误:", e);
		} finally {
		}

	}

	// 浮动菜单
	public JPopupMenu getPmenu() {
		JPopupMenu mTask = new JPopupMenu();
		try {
			final SMenuItem miExecTask = new SMenuItem("执行任务", ImageContext.Exec);
			miExecTask.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							TaskTab.getInstance().execTask(1);
						}
					});

			mTask.add(miExecTask);

			final SMenuItem miViewLog = new SMenuItem("查看日志", ImageContext.LogView);
			miViewLog.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							viewLog();
						}
					});

			mTask.add(miViewLog);
			mTask.addSeparator();

			final SMenuItem miCopyTask = new SMenuItem("复制任务", ImageContext.Copy);
			miCopyTask.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							copyTask();
						}
					});
			mTask.add(miCopyTask);

			final SMenuItem itemsExportTask = new SMenuItem("导出任务", ImageContext.Export);
			itemsExportTask.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							exportTask();
						}
					});
			mTask.add(itemsExportTask);
			final SMenuItem miImportTask = new SMenuItem("导入任务", ImageContext.Import);
			miImportTask.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							importTask();
						}
					});
			mTask.add(miImportTask);
			mTask.addSeparator();
			final SMenuItem miUpdateOrder = new SMenuItem("更新编号", ImageContext.Refresh);
			miUpdateOrder.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							updateOrder();
						}
					});
			mTask.add(miUpdateOrder);
			mTask.addSeparator();
			final SMenuItem miHideTask = new SMenuItem("隐藏任务", ImageContext.Mod);
			miHideTask.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							modeState(-1);
						}
					});

			mTask.add(miHideTask);

			final SMenuItem miShowTask = new SMenuItem("显示任务", ImageContext.Mod);
			miShowTask.addActionListener(new ActionListener() {// 浮动菜单
						public void actionPerformed(ActionEvent arg0) {
							modeState(0);
						}
					});
			mTask.add(miShowTask);
		} catch (Exception e) {
			Log.logError("任务列表刷新错误:", e);
		} finally {
		}
		return mTask;
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
					TaskDao.getInstance().modState(taskId, state);
				}
			}
		} catch (Exception e) {
			UtilLog.logError("更新错误:", e);
		} finally {
		}
	}

	// 导出任务事件
	private void exportTask() {
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(this.jtable);
			if (selectedCount == 0) {
				JOptionPane.showMessageDialog(null, "请勾选导出的任务", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			String[] filter = { "xml" };
			String path = ShowDialog.save(AppMain.appMain, "选保存文件路径", "任务xml", filter, "tasks.xml");
			if (!path.equals("")) {
				exportXml(path);
				ShowMsg.showMsg("导出任务成功");
			}

		} catch (Exception e) {
			Log.logError("导出任务错误:", e);
		}
	}

	// 导出任务到xml
	private void exportXml(String path) throws Exception {
		List<Map<String, Object>> taskList = new ArrayList<Map<String, Object>>();
		String unExportTaskId = "";
		for (int i = 0; i < this.jtable.getRowCount(); i++) {
			Boolean selected = Boolean.valueOf(this.jtable.getValueAt(i, 0).toString());
			if (selected) {
				Long taskId = Long.valueOf(this.jtable.getValueAt(i, 2).toString());
				ITask task = TaskDao.getInstance().getMapTask(taskId);
				try {
					Map<String, Object> map = UtilConver.beanToMap(task);
					taskList.add(map);
				} catch (Exception e) {
					unExportTaskId += String.valueOf(taskId) + ",";
					Log.logError("导出任务" + taskId + "错误：", e);
					continue;
				}
			}
		}
		if (unExportTaskId.endsWith(","))
			unExportTaskId = unExportTaskId.substring(0, unExportTaskId.length() - 1);
		if (!"".equals(unExportTaskId))
			Log.logWarn("未导出任务标志:" + unExportTaskId);
		UtilConver.listToXmlFile(taskList, "TaskList", path);
	}

	// 导入任务
	private void importTask() {
		try {
			String[] filter = { "xml" };
			String path = ShowDialog.open(AppMain.appMain, "选择导入文件路径", "任务xml", filter, 0);
			if (!path.equals("")) {
				List<Map<String, Object>> list = UtilConver.xmlFileToList(path);
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = (Map<String, Object>) list.get(i);
					TaskSpace task = (TaskSpace) UtilConver.mapToBean(map, TaskSpace.class);
					task.setTaskId(TaskDao.getInstance().getMaxTaskID() + 1);
					task.setTaskOrder(String.valueOf(task.getTaskId()));
					TaskDao.getInstance().addTask(task);
				}
				TaskTab.getInstance().queryTask();
				ShowMsg.showMsg("导入任务成功");
			}
		} catch (Exception e) {
			Log.logError("导入任务错误:", e);
		}
	}

	// 复制任务
	private void copyTask() {
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(this.jtable);
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选复制的任务");
				return;
			}

			for (int i = 0; i < this.jtable.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(this.jtable.getValueAt(i, 0).toString());
				if (selected) {
					Long taskId = Long.valueOf(this.jtable.getValueAt(i, 2).toString());
					// 须复制一个
					ITask task = (ITask) TaskDao.getInstance().getMapTask(taskId).clone();
					task.setTaskId(TaskDao.getInstance().getMaxTaskID() + 1);
					task.setTaskOrder(String.valueOf(task.getTaskId()));
					TaskDao.getInstance().addTask(task);
				}
			}
			TaskTab.getInstance().queryTask();
		} catch (Exception e) {

		}
	}

	// 更新任务顺序号
	private void updateOrder() {
		int maxOrderLen = TaskDao.getInstance().getMaxOrderLen();
		for (int i = 0; i < this.jtable.getRowCount(); i++) {
			String curOrder = this.jtable.getValueAt(i, 1).toString();
			if (curOrder.length() >= maxOrderLen)
				continue;
			long id = Long.valueOf(this.jtable.getValueAt(i, 2).toString());
			String order = UtilString.expandOrder(curOrder, maxOrderLen);
			TaskDao.getInstance().modOrder(id, order);
		}
		TaskTab.getInstance().queryTask();
	}

	// 双击编辑任务
	private void mouseDoubleClick() {
		try {
			TaskTab.getInstance().editTask(this.jtable);
		} catch (Exception e) {
			Log.logError("任务列表双击错误:", e);
		} finally {
		}
	};

	// 查看任务日志
	private void viewLog() {
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(this.jtable);
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选任务");
				return;
			}
			for (int i = 0; i < this.jtable.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(this.jtable.getValueAt(i, 0).toString());
				if (selected) {
					String taskName = jtable.getValueAt(i, 3).toString();
					String taskId = jtable.getValueAt(i, 2).toString();
					String title = taskName + "[" + taskId + "]";
					AppLogView.getInstance().addTab(title);
				}
			}
		} catch (Exception e) {
			Log.logError("查看任务日志错误:", e);
		} finally {
		}
	};

	public JTable getJtable() {
		return this.jtable;
	}

	// public static void main(String[] arg) {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("Type", "全部");
	// TaskTable ts = new TaskTable(map);
	// JFrame frame = new JFrame("sjh");
	// frame.setLayout(null);
	// frame.setSize(1100, 600);
	// SScrollPane src = new SScrollPane(ts.jtable);
	// src.setBounds(0, 0, 1000, 200);
	// frame.add(src);
	// frame.setVisible(true);
	// }

}
