package com.taskInterface;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.app.AppVar;
import com.log.Log;
import com.scher.ScherDao;
import com.scher.ScherDialog;
import com.taskgroup.TaskGroupDetailDao;
import com.taskmanu.ManuExecParamDialog;
import com.taskmanu.ManuExecThread;
import com.threadPool.ThreadPool;

import common.component.SButton;
import common.component.SComboBox;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.SSplitPane;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.array.UtilArray;
import common.util.swing.UtilComponent;
import consts.ImageContext;

public class TaskTab {
	private JPanel pnlTaskTool;
	private JPanel pnlTab;
	private SLabel ltasktype;
	private SLabel ltaskName;
	private SButton btnTaskquery;
	private SButton btnAddSche;
	private SButton btnDelTask;
	private STextField txtTaskName;
	private SComboBox cmbTaskType;
	private SScrollPane scrlTask;
	private SButton btnEditTask;
	private SButton btnAddTask;
	private SButton btnExecTask;
	private SSplitPane spltTask;
	private JToolBar tbTask;
	private TaskTable taskTable;
	private SLabel lState;
	private SComboBox cmbState;
	private static TaskTab tab;
	public static TaskDialog dialogTask;

	public static TaskTab getInstance() {
		if (tab == null)
			tab = new TaskTab();
		return tab;
	}

	// 创建任务列表
	private TaskTab() {
		try {
			pnlTab = new JPanel();
			// tabMain.addTab("任务列表", null, pnlTask, null);
			GridLayout jmainPanelLayout = new GridLayout(1, 1);
			jmainPanelLayout.setColumns(1);
			jmainPanelLayout.setHgap(5);
			jmainPanelLayout.setVgap(5);
			pnlTab.setLayout(jmainPanelLayout);
			pnlTab.setPreferredSize(new java.awt.Dimension(715, 254));
			{
				spltTask = new SSplitPane(0, 35, false);
				spltTask.setEnabled(false);
				pnlTab.add(spltTask);
				{
					pnlTaskTool = new JPanel();
					GridLayout jPanel_tool_taskLayout = new GridLayout(1, 1);
					jPanel_tool_taskLayout.setColumns(1);
					jPanel_tool_taskLayout.setHgap(5);
					jPanel_tool_taskLayout.setVgap(5);
					pnlTaskTool.setLayout(jPanel_tool_taskLayout);
					spltTask.add(pnlTaskTool, SSplitPane.TOP);
					pnlTaskTool.setPreferredSize(new java.awt.Dimension(713, 33));
					{
						tbTask = new JToolBar();
						pnlTaskTool.add(tbTask);

						{
//							System.out.println(ImageContext.Add);
							btnAddTask = new SButton("\u6dfb\u52a0\u4efb\u52a1", ImageContext.Add);
							btnAddTask.setSize(200, 25);
							tbTask.add(btnAddTask);
							btnAddTask.setPreferredSize(new java.awt.Dimension(98, 30));
							btnAddTask.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									addTask();
								}
							});
						}
						{
							tbTask.addSeparator(new Dimension(5, 30));
						}
						{
							btnEditTask = new SButton("\u4fee\u6539\u4efb\u52a1", ImageContext.Mod);
							tbTask.add(btnEditTask);
							btnEditTask.setSize(60, 25);
							btnEditTask.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									editTask(taskTable.getJtable());
								}
							});
						}
						{
							tbTask.addSeparator(new Dimension(5, 30));
						}
						{
							btnDelTask = new SButton("\u5220\u9664\u4efb\u52a1", ImageContext.Del);
							tbTask.add(btnDelTask);
							btnDelTask.setSize(60, 25);
							btnDelTask.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									if (delTask())
										queryTask();
								}
							});
						}
						{
							tbTask.addSeparator(new Dimension(5, 30));
						}
						{
							btnAddSche = new SButton("\u6dfb\u52a0\u8c03\u5ea6", ImageContext.AddSche);
							tbTask.add(btnAddSche);
							btnAddSche.setSize(60, 25);
							btnAddSche.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									addSche();
								}
							});
						}
						{
							tbTask.addSeparator(new Dimension(5, 30));
						}
						{
							btnExecTask = new SButton("手工执行", ImageContext.Exec);
							tbTask.add(btnExecTask);
							btnExecTask.setSize(60, 25);
							btnExecTask.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									execTask();
								}
							});
						}
						{
							tbTask.addSeparator(new java.awt.Dimension(60, 30));

						}
						{
							ltasktype = new SLabel("\u4efb\u52a1\u7c7b\u578b");
							tbTask.add(ltasktype);
							ltasktype.setMinimumSize(new java.awt.Dimension(52, 21));
							ltasktype.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							cmbTaskType = new SComboBox(AppVar.TASK_TYPE_Combobox, 20);
							tbTask.add(cmbTaskType);
							cmbTaskType.setMaximumSize(new java.awt.Dimension(150, 21));
							cmbTaskType.setMinimumSize(new java.awt.Dimension(150, 21));
						}
						{
							tbTask.addSeparator(new Dimension(5, 30));
						}
						{
							ltaskName = new SLabel("\u4efb\u52a1\u540d\u79f0");
							tbTask.add(ltaskName);
							ltaskName.setMinimumSize(new java.awt.Dimension(52, 21));
							ltaskName.setMaximumSize(new java.awt.Dimension(52, 21));
							ltaskName.setHorizontalAlignment(SwingConstants.LEFT);
							ltaskName.setHorizontalTextPosition(SwingConstants.LEFT);
						}
						{
							txtTaskName = new STextField();
							tbTask.add(txtTaskName);
							txtTaskName.setMaximumSize(new java.awt.Dimension(120, 21));
							txtTaskName.setMinimumSize(new java.awt.Dimension(120, 21));
						}
						{
							tbTask.addSeparator(new Dimension(5, 30));
						}
						{
							lState = new SLabel("状态");
							tbTask.add(lState);
							lState.setMinimumSize(new java.awt.Dimension(32, 21));
							lState.setMaximumSize(new java.awt.Dimension(32, 21));
						}
						{
							cmbState = new SComboBox(AppVar.StateText, 5);
							tbTask.add(cmbState);
							cmbState.setMaximumSize(new java.awt.Dimension(80, 21));
							cmbState.setMinimumSize(new java.awt.Dimension(80, 21));
						}
						{
							tbTask.addSeparator(new Dimension(5, 30));
						}
						{
							btnTaskquery = new SButton("\u67e5  \u8be2", ImageContext.Query);
							tbTask.add(btnTaskquery);
							btnTaskquery.setMinimumSize(new java.awt.Dimension(90, 25));
							btnTaskquery.setMaximumSize(new java.awt.Dimension(90, 25));
							btnTaskquery.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									try {
										btnTaskquery.setEnabled(false);
										queryTask();
									} finally {
										btnTaskquery.setEnabled(true);
									}
								}
							});
						}

					}

				}
				{

					Map<String, String> map = new HashMap<String, String>();
					map.put("Type", "全部");
					taskTable = new TaskTable(map);
					scrlTask = new SScrollPane(taskTable.getJtable());
					scrlTask.addMouseListener(new MouseListener() {

						public void mouseClicked(MouseEvent e) {
						}

						public void mouseEntered(MouseEvent e) {
						}

						public void mouseExited(MouseEvent e) {
						}

						public void mousePressed(MouseEvent e) {
						}

						// 当table没有行时使用
						public void mouseReleased(MouseEvent e) {
							if (e.isPopupTrigger()) {
								taskTable.getPmenu().show(e.getComponent(), e.getX(), e.getY());
							}

						}
					});
					spltTask.add(scrlTask, SSplitPane.BOTTOM);
				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建任务列表错误:", e);
		} finally {
		}
	}

	// 添加任务

	@SuppressWarnings("deprecation")
	public void addTask() {
		try {
			ITask itask = new TaskSpace();
			itask.setTaskId(TaskDao.getInstance().getMaxTaskID() + 1);
			dialogTask = new TaskDialog(0, itask);
			dialogTask.show(true);
		} catch (Exception e) {
			Log.logError("主程序新增任务错误:", e);
		} finally {
		}
	}

	// 修改任务
	@SuppressWarnings("deprecation")
	public void editTask(JTable tblTask) {
		try {
			if (tblTask.getSelectedRow() < 0) {
				ShowMsg.showMsg("请选择任务！");
				return;
			}
			if (tblTask.getSelectedRow() < 0)
				return;
			ITask itask = TaskDao.getInstance().getMapTask(Long.valueOf(Long.valueOf(tblTask.getValueAt(tblTask.getSelectedRow(), 2).toString())));
			if (itask != null) {
				if (UtilArray.getArrayIndex(AppVar.TASK_TYPE_Combobox, itask.getTaskType()) == -1) {
					ShowMsg.showMsg("该任务类型为非本程序预定义类型");
					return;
				}
			}
			dialogTask = new TaskDialog(1, itask);
			dialogTask.show(true);
		} catch (Exception e) {
			Log.logError("主程修改任务序错误:", e);
		} finally {
		}
	}

	// 执行任务
	public void execTask(int flag) {
		execTask();
	}

	// 任务过滤
	public void queryTask() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("Type", "任务过滤");
			map.put("任务名称", txtTaskName.getText().trim());
			if (cmbTaskType.getSelectedIndex() >= 0)
				map.put("任务类型", cmbTaskType.getSelectedItem().toString());

			if (cmbState.getSelectedIndex() >= 0)
				map.put("状态", AppVar.State[cmbState.getSelectedIndex()]);
			taskTable = new TaskTable(map);
			scrlTask.setViewportView(taskTable.getJtable());
		} catch (Exception e) {
			Log.logError("主程序过滤任务错误:", e);
		}
	}

	// 添加调度
	@SuppressWarnings("deprecation")
	public void addSche() {
		try {
			if (taskTable.getJtable().getSelectedRow() >= 0) {
				String taskId = taskTable.getJtable().getValueAt(taskTable.getJtable().getSelectedRow(), 2).toString();
				ScherDialog dialogSche = new ScherDialog(0, taskId, "");
				dialogSche.show(true);
			}
		} catch (Exception e) {
			Log.logError("主程序添加调度错误:", e);
		} finally {
		}
	}

	// 删除任务
	public boolean delTask() {
		boolean rs = false;
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(taskTable.getJtable());
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选删除的任务");
				return false;
			}
			int msg = ShowMsg.showConfig("确定删除任务: " + UtilComponent.getTableSelectedRowFiled(taskTable.getJtable(), 2) + " ?");
			if (msg == 0) {
				for (int i = 0; i < taskTable.getJtable().getRowCount(); i++) {
					Boolean selected = Boolean.valueOf(taskTable.getJtable().getValueAt(i, 0).toString());
					if (selected) {
						Long taskid = Long.valueOf(taskTable.getJtable().getValueAt(i, 2).toString());
						if (ScherDao.getInstance().IfTaskSechExist(taskid)) {
							{
								ShowMsg.showWarn("任务:" + taskid + " 存在调度，不能删除");
								continue;
							}
						} else {
							TaskDao.getInstance().delTask(taskid);
							// 删除任务组里面的任务
							TaskGroupDetailDao.getInstance().delTaskGroupDetailWhenDelTask(taskid);
							rs = true;
						}
					}
				}
			}
		} catch (Exception e) {
			Log.logError("主程序删除任务错误:", e);
		} finally {
		}
		return rs;
	}

	// 执行任务
	public void execTask() {
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(taskTable.getJtable());
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选执行的任务");
				return;
			}

			Long[] taskId = null;
			String[] paramValue = null;
			boolean isCancel = false;
			boolean hasFuncode = false;// 如果是存储过程，则参数面板包含基金代码
			taskId = new Long[selectedCount];
			int index = 0;
			for (int i = 0; i < taskTable.getJtable().getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(taskTable.getJtable().getValueAt(i, 0).toString());
				if (selected) {
					taskId[index] = Long.valueOf(taskTable.getJtable().getValueAt(i, 2).toString());
					index = index + 1;
					if (taskTable.getJtable().getValueAt(i, 4).toString().equalsIgnoreCase("存储过程"))
						hasFuncode = true;
				}
			}
			/** *********判断传参********* */
			ManuExecParamDialog dialogManuExecTaskParam = new ManuExecParamDialog(hasFuncode);
			paramValue = dialogManuExecTaskParam.paramValue;
			isCancel = dialogManuExecTaskParam.isCancle;

			// 多线程执行手工任务
			if (isCancel == false) {
				Thread thread = new ManuExecThread(null, taskId, paramValue);
				String taskIds = "";
				for (int i = 0; i < taskId.length; i++) {
					taskIds = taskIds + taskId[i] + " ";
				}
				String key = "-1|" + "|" + taskIds + "|";
				ThreadPool.getPool().submit(key, thread);
			}
		} catch (Exception e) {
			Log.logError("主程序手工执行任务错误:", e);
		} finally {
		}
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}

	public JToolBar getTbTask() {
		return tbTask;
	}

	public TaskTable getTaskTable() {
		return taskTable;
	}

}
