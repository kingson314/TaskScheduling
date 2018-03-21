package com.taskgroup;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

import com.app.AppVar;
import com.log.Log;
import com.scher.ScherDao;
import com.scher.ScherDialog;
import com.taskInterface.TaskDao;
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
import common.util.swing.UtilComponent;
import consts.ImageContext;

public class TaskGroupTab {
	private JPanel pnlTooltaskGroup;
	private JPanel pnlTab;
	private SScrollPane scrlTaskGroup;
	private STextField txtGroupName;
	private SLabel lGroupName;
	private SButton btnGroupQuery;
	private SButton btnExecTaskGroup;
	private SButton btnAddTaskGoupSche;
	private SButton btnDelTaskGroup;
	private JTable tblTaskGroup;
	private SButton btnEditTaskGroup;
	private SButton btnAddTaskGroup;
	private SSplitPane spltTaskGroup;
	private JToolBar tbTaskGroup;
	private SLabel lState;
	private SComboBox cmbState;

	private static TaskGroupTab tab;

	public static TaskGroupTab getInstance() {
		if (tab == null)
			tab = new TaskGroupTab();
		return tab;
	}

	// 创建任务组列表
	private TaskGroupTab() {
		try {
			pnlTab = new JPanel();
			// tabMain.addTab("任务组列表", null, pnlTaskGroup, null);
			GridLayout jPanel_taskGroupLayout = new GridLayout(1, 1);
			jPanel_taskGroupLayout.setColumns(1);
			jPanel_taskGroupLayout.setHgap(5);
			jPanel_taskGroupLayout.setVgap(5);
			pnlTab.setLayout(jPanel_taskGroupLayout);
			pnlTab.setPreferredSize(new java.awt.Dimension(715, 254));
			{
				spltTaskGroup = new SSplitPane(0, 35, false);
				spltTaskGroup.setEnabled(false);
				pnlTab.add(spltTaskGroup);
				{
					pnlTooltaskGroup = new JPanel();
					GridLayout jPanel_tool_taskLayout = new GridLayout(1, 1);
					jPanel_tool_taskLayout.setColumns(1);
					jPanel_tool_taskLayout.setHgap(5);
					jPanel_tool_taskLayout.setVgap(5);
					pnlTooltaskGroup.setLayout(jPanel_tool_taskLayout);
					spltTaskGroup.add(pnlTooltaskGroup, SSplitPane.TOP);
					pnlTooltaskGroup.setPreferredSize(new java.awt.Dimension(713, 33));
					{
						tbTaskGroup = new JToolBar();
						pnlTooltaskGroup.add(tbTaskGroup);

						{
							btnAddTaskGroup = new SButton("添加任务组", ImageContext.Add);
							btnAddTaskGroup.setSize(200, 25);
							tbTaskGroup.add(btnAddTaskGroup);
							btnAddTaskGroup.setPreferredSize(new java.awt.Dimension(98, 30));
							btnAddTaskGroup.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									addTaskGroup();
								}
							});
						}
						{
							tbTaskGroup.addSeparator(new Dimension(5, 30));
						}
						{
							btnEditTaskGroup = new SButton("修改任务组", ImageContext.Mod);
							tbTaskGroup.add(btnEditTaskGroup);
							btnEditTaskGroup.setSize(60, 25);
							btnEditTaskGroup.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent evt) {
									editTaskGroup();
								}
							});
						}
						{
							tbTaskGroup.addSeparator(new Dimension(5, 30));
						}
						{
							btnDelTaskGroup = new SButton("删除任务组", ImageContext.Del);
							tbTaskGroup.add(btnDelTaskGroup);
							btnDelTaskGroup.setSize(60, 25);
							btnDelTaskGroup.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									delTaskGroup();
								}
							});
						}
						{
							tbTaskGroup.addSeparator(new Dimension(5, 30));
						}
						{
							btnAddTaskGoupSche = new SButton("添加调度", ImageContext.AddSche);
							tbTaskGroup.add(btnAddTaskGoupSche);
							btnAddTaskGoupSche.setSize(60, 25);
							btnAddTaskGoupSche.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									addSche();
								}
							});
						}
						{
							tbTaskGroup.addSeparator(new Dimension(5, 30));
						}
						{
							btnExecTaskGroup = new SButton("手工执行", ImageContext.Exec);
							tbTaskGroup.add(btnExecTaskGroup);
							btnExecTaskGroup.setSize(60, 25);
							btnExecTaskGroup.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									execTaskGroup();
								}
							});

						}
						{
							tbTaskGroup.addSeparator(new Dimension(60, 30));
						}
						{
							lGroupName = new SLabel("\u4efb\u52a1\u7ec4\u540d\u79f0");
							tbTaskGroup.add(lGroupName);
							lGroupName.setMinimumSize(new java.awt.Dimension(52, 21));
							lGroupName.setMaximumSize(new java.awt.Dimension(60, 21));
						}
						{
							txtGroupName = new STextField();
							tbTaskGroup.add(txtGroupName);
							txtGroupName.setMaximumSize(new java.awt.Dimension(120, 21));
							txtGroupName.setMinimumSize(new java.awt.Dimension(120, 21));
						}
						{
							tbTaskGroup.addSeparator(new Dimension(5, 30));
						}
						{
							lState = new SLabel("状态");
							tbTaskGroup.add(lState);
							lState.setMinimumSize(new java.awt.Dimension(32, 21));
							lState.setMaximumSize(new java.awt.Dimension(32, 21));
						}
						{
							cmbState = new SComboBox(AppVar.StateText, 5);
							tbTaskGroup.add(cmbState);
							cmbState.setMaximumSize(new java.awt.Dimension(80, 21));
							cmbState.setMinimumSize(new java.awt.Dimension(80, 21));
						}
						{
							tbTaskGroup.addSeparator(new Dimension(5, 30));
						}
						{
							btnGroupQuery = new SButton("\u67e5  \u8be2", ImageContext.Query);
							tbTaskGroup.add(btnGroupQuery);
							btnGroupQuery.setMaximumSize(new java.awt.Dimension(90, 25));
							btnGroupQuery.setMinimumSize(new java.awt.Dimension(90, 25));
							btnGroupQuery.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									try {
										btnGroupQuery.setEnabled(false);
										queryTaskGroup();
									} finally {
										btnGroupQuery.setEnabled(true);
									}
								}
							});
						}

					}

				}
				{
					tblTaskGroup = new TaskGroupTable(null).getJtable();
					scrlTaskGroup = new SScrollPane(tblTaskGroup);
					spltTaskGroup.add(scrlTaskGroup, SSplitPane.BOTTOM);
				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建任务组列表错误:", e);
		} finally {
		}
	}

	// 新增任务组
	@SuppressWarnings("deprecation")
	public void addTaskGroup() {
		try {
			TaskGroupDialog dialogTaskGroup = new TaskGroupDialog(0, -1l);
			dialogTaskGroup.show(true);
		} catch (Exception e) {
			Log.logError("主程序新增任务组错误:", e);
		} finally {
		}
	}

	// 修改任务组
	@SuppressWarnings("deprecation")
	public void editTaskGroup() {
		try {
			if (tblTaskGroup.getSelectedRow() < 0) {
				ShowMsg.showMsg("请选择任务组！");
				return;
			}
			TaskGroupDialog dialogTaskGroup = new TaskGroupDialog(1, Long.valueOf(tblTaskGroup.getValueAt(tblTaskGroup.getSelectedRow(), 2).toString()));
			dialogTaskGroup.show(true);
		} catch (Exception e) {
			Log.logError("主程序修改任务组错误:", e);
		} finally {
		}
	}

	// 任务组过滤
	public void queryTaskGroup() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("Type", "任务组过滤");
			map.put("任务组名称", txtGroupName.getText().trim());
			if (cmbState.getSelectedIndex() >= 0)
				map.put("状态", AppVar.State[cmbState.getSelectedIndex()]);
			tblTaskGroup = new TaskGroupTable(map).getJtable();
			scrlTaskGroup.setViewportView(tblTaskGroup);
		} catch (Exception e) {
			Log.logError("主程序过滤任务组错误:", e);
		} finally {
		}
	}

	// 添加调度
	@SuppressWarnings("deprecation")
	public void addSche() {
		try {
			if (tblTaskGroup.getSelectedRow() >= 0) {
				String groupId = tblTaskGroup.getValueAt(tblTaskGroup.getSelectedRow(), 2).toString();
				ScherDialog dialogSche = new ScherDialog(0, "", groupId);
				dialogSche.show(true);
			}
		} catch (Exception e) {
			Log.logError("主程序添加调度错误:", e);
		} finally {
		}
	}

	// 执行任务组
	public void execTaskGroup() {
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(tblTaskGroup);
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选执行的任务组");
				return;
			}
			Long[] groupId = null;
			String[] paramValue = null;
			boolean isCancel = false;
			boolean hasFuncode = false;// 如果是存储过程，则参数面板包含基金代码
			int len = UtilComponent.getTableSelectedCount(tblTaskGroup);
			groupId = new Long[len];
			int index = 0;
			for (int i = 0; i < tblTaskGroup.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(tblTaskGroup.getValueAt(i, 0).toString());
				if (selected) {
					groupId[index] = Long.valueOf(tblTaskGroup.getValueAt(i, 2).toString());
					index = index + 1;
				}
			}
			hasFuncode = TaskDao.getInstance().IsHasFundCode(groupId);
			/** *********判断传参********* */
			ManuExecParamDialog dialogManuExecTaskParam = new ManuExecParamDialog(hasFuncode);
			paramValue = dialogManuExecTaskParam.paramValue;
			isCancel = dialogManuExecTaskParam.isCancle;
			// 多线程执行手工任务
			if (isCancel == false) {

				Thread thread = new ManuExecThread(groupId, null, paramValue);

				String groupIds = "";
				for (int i = 0; i < groupId.length; i++) {
					groupIds = groupIds + groupId[i] + " ";
				}
				String key = "-1|" + groupIds + "|" + "|";
				ThreadPool.getPool().submit(key, thread);
			}
		} catch (Exception e) {
			Log.logError("主程序手工执行任务组错误:", e);
		} finally {
		}
	}

	// 删除任务组
	public boolean delTaskGroup() {
		boolean rs = false;
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(tblTaskGroup);
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选删除的任务组");
				return false;
			}

			int msg = ShowMsg.showConfig("确定删除任务组: " + UtilComponent.getTableSelectedRowFiled(tblTaskGroup, 2) + " ?");

			if (msg == 0) {
				for (int i = 0; i < tblTaskGroup.getRowCount(); i++) {
					Boolean selected = Boolean.valueOf(tblTaskGroup.getValueAt(i, 0).toString());
					if (selected) {
						Long groupId = Long.valueOf(tblTaskGroup.getValueAt(i, 2).toString());
						if (ScherDao.getInstance().IfTaskGroupSechExist(groupId)) {
							{
								ShowMsg.showWarn("任务组:" + groupId + " 存在调度，不能删除");
								continue;
							}
						} else {
							TaskGroupDao.getInstance().delTaskGroup(groupId);
							rs = true;
						}
					}
				}
				queryTaskGroup();
			}
		} catch (Exception e) {
			Log.logError("主程序删除任务组错误:", e);
		} finally {
		}
		return rs;
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}
}
