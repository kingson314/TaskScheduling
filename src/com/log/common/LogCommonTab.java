package com.log.common;

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
import common.component.SButton;
import common.component.SCalendar;
import common.component.SComboBox;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.SSplitPane;
import common.component.STextField;
import common.component.ShowMsg;

import consts.Const;
import consts.ImageContext;

public class LogCommonTab {
	private STextField txtRecordCount;
	private SButton btnEndDate;
	private STextField txtScheNameLogCommon;
	private SButton btnBegDate;
	private STextField txtTaskNameLogCommon;
	private STextField txtGroupNameLogCommon;
	private STextField txtLogCommon;
	private STextField txtEndDate;
	private STextField txtBegDate;
	private SComboBox cmbTaskTypeLogCommon;
	private SComboBox cmbExecStatus;
	private SComboBox cmbIfSendMsg;
	private SScrollPane scrlScheResult;
	private SSplitPane spltScheResult;
	private JToolBar tb1;
	private JTable tblLogCommon;
	private SButton btnLogCommonQuery;
	private SButton btnScheResult;
	private SLabel lIfSendMsg;
	private SLabel lScheName;
	private SLabel lTaskName;
	private SLabel lEnddate;
	private SLabel lBedate;
	private SLabel lLogCommon;
	private SLabel lExecStatus;
	private SLabel lTaskType;
	private SLabel lGroupName;
	private JPanel pnlTab;
	private SLabel lRecordCount;
	private JPanel pnlScheResult;
	private JToolBar tb2;

	private static LogCommonTab tab;

	public static LogCommonTab getInstance() {
		if (tab == null)
			tab = new LogCommonTab();
		return tab;
	}

	// 创建执行日志列表
	private LogCommonTab() {
		try {
			pnlTab = new JPanel();
			GridLayout jPanel_scheResultLayout = new GridLayout(1, 1);
			jPanel_scheResultLayout.setColumns(1);
			jPanel_scheResultLayout.setHgap(5);
			jPanel_scheResultLayout.setVgap(5);
			pnlTab.setLayout(jPanel_scheResultLayout);
			// tabMain.addTab("执行日志", null, pnlLogCommon, null);
			{
				spltScheResult = new SSplitPane(0, 70, false);
				spltScheResult.setEnabled(false);
				pnlTab.add(spltScheResult);
				{
					pnlScheResult = new JPanel();
					GridLayout layoutLogAbridgement = new GridLayout();
					layoutLogAbridgement.setColumns(1);
					layoutLogAbridgement.setRows(2);
					layoutLogAbridgement.setHgap(5);
					layoutLogAbridgement.setVgap(5);
					pnlScheResult.setLayout(layoutLogAbridgement);
					spltScheResult.add(pnlScheResult, SSplitPane.TOP);
					{
						tb1 = new JToolBar();
						pnlScheResult.add(tb1);
						{
							{
								lBedate = new SLabel("开始日期");
								tb1.add(lBedate);
								lBedate.setMinimumSize(new java.awt.Dimension(52, 21));
								lBedate.setMaximumSize(new java.awt.Dimension(52, 21));
							}
							{
								txtBegDate = new STextField();
								tb1.add(txtBegDate);
								txtBegDate.setMinimumSize(new java.awt.Dimension(70, 21));
								txtBegDate.setMaximumSize(new java.awt.Dimension(70, 21));
							}
							{
								btnBegDate = new SButton("..");
								tb1.add(btnBegDate);
								btnBegDate.setMinimumSize(new java.awt.Dimension(22, 21));
								btnBegDate.setMaximumSize(new java.awt.Dimension(22, 21));
								btnBegDate.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										new SCalendar(txtBegDate);
									}
								});

							}
							{
								tb1.addSeparator(new Dimension(5, 30));
							}
							{
								lScheName = new SLabel("调度名称");
								tb1.add(lScheName);
								lScheName.setMinimumSize(new java.awt.Dimension(52, 21));
								lScheName.setMaximumSize(new java.awt.Dimension(52, 21));
							}
							{
								txtScheNameLogCommon = new STextField();
								tb1.add(txtScheNameLogCommon);
								txtScheNameLogCommon.setMinimumSize(new java.awt.Dimension(125, 21));
								txtScheNameLogCommon.setMaximumSize(new java.awt.Dimension(125, 21));
							}
							{
								tb1.addSeparator(new Dimension(5, 30));
							}
							{
								lTaskName = new SLabel("任务名称");
								tb1.add(lTaskName);
								lTaskName.setMinimumSize(new java.awt.Dimension(52, 21));
								lTaskName.setMaximumSize(new java.awt.Dimension(52, 21));

							}
							{
								txtTaskNameLogCommon = new STextField();
								tb1.add(txtTaskNameLogCommon);
								txtTaskNameLogCommon.setMinimumSize(new java.awt.Dimension(100, 21));
								txtTaskNameLogCommon.setMaximumSize(new java.awt.Dimension(100, 21));
							}
							{
								tb1.addSeparator(new Dimension(5, 30));
							}
							{
								lGroupName = new SLabel("任务组名称");
								tb1.add(lGroupName);
								lGroupName.setMinimumSize(new java.awt.Dimension(60, 21));
								lGroupName.setMaximumSize(new java.awt.Dimension(60, 21));
							}
							{
								txtGroupNameLogCommon = new STextField();
								tb1.add(txtGroupNameLogCommon);
								txtGroupNameLogCommon.setMinimumSize(new java.awt.Dimension(100, 21));
								txtGroupNameLogCommon.setMaximumSize(new java.awt.Dimension(100, 21));
							}
							{
								tb1.addSeparator(new Dimension(5, 30));
							}
							{
								lRecordCount = new SLabel("查询记录数");
								tb1.add(lRecordCount);
								lRecordCount.setMinimumSize(new java.awt.Dimension(60, 21));
								lRecordCount.setMaximumSize(new java.awt.Dimension(60, 21));
							}
							{
								txtRecordCount = new STextField();
								txtRecordCount.setText(String.valueOf(100));
								tb1.add(txtRecordCount);
								txtRecordCount.setMinimumSize(new java.awt.Dimension(80, 21));
								txtRecordCount.setMaximumSize(new java.awt.Dimension(80, 21));
							}
							{
								tb1.addSeparator(new Dimension(5, 30));
							}
							{
								btnScheResult = new SButton("清空日志", ImageContext.Clear);
								tb1.add(btnScheResult);
								btnScheResult.setMinimumSize(new java.awt.Dimension(90, 25));
								btnScheResult.setMaximumSize(new java.awt.Dimension(90, 25));
								btnScheResult.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										clearLogCommon();
										queryLogCommon();
									}
								});
							}
						}
						// 第二行
						tb2 = new JToolBar();
						pnlScheResult.add(tb2);
						{
							{
								lEnddate = new SLabel("结束日期");
								tb2.add(lEnddate);
								lEnddate.setMinimumSize(new java.awt.Dimension(52, 21));
								lEnddate.setMaximumSize(new java.awt.Dimension(52, 21));
							}
							{
								txtEndDate = new STextField();
								tb2.add(txtEndDate);
								txtEndDate.setMinimumSize(new java.awt.Dimension(70, 21));
								txtEndDate.setMaximumSize(new java.awt.Dimension(70, 21));
							}
							{
								btnEndDate = new SButton("..");
								tb2.add(btnEndDate);
								btnEndDate.setMinimumSize(new java.awt.Dimension(22, 21));
								btnEndDate.setMaximumSize(new java.awt.Dimension(22, 21));
								btnEndDate.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										new SCalendar(txtEndDate);
									}
								});

							}
							{
								tb2.addSeparator(new Dimension(5, 30));
							}
							{
								lTaskType = new SLabel("任务类型");
								lTaskType.setMinimumSize(new java.awt.Dimension(52, 21));
								lTaskType.setMaximumSize(new java.awt.Dimension(52, 21));
								tb2.add(lTaskType);
							}
							{
								cmbTaskTypeLogCommon = new SComboBox(AppVar.TASK_TYPE_Combobox, 20);
								tb2.add(cmbTaskTypeLogCommon);
								cmbTaskTypeLogCommon.setMinimumSize(new java.awt.Dimension(125, 21));
								cmbTaskTypeLogCommon.setMaximumSize(new java.awt.Dimension(125, 21));
								cmbTaskTypeLogCommon.setAutoscrolls(true);
								cmbTaskTypeLogCommon.setDoubleBuffered(true);
								cmbTaskTypeLogCommon.setPreferredSize(new java.awt.Dimension(24, 21));
								cmbTaskTypeLogCommon.setMaximumRowCount(20);
							}
							{
								tb2.addSeparator(new Dimension(5, 30));
							}
							{
								lExecStatus = new SLabel("执行状态");
								tb2.add(lExecStatus);
								lExecStatus.setMinimumSize(new java.awt.Dimension(52, 21));
								lExecStatus.setMaximumSize(new java.awt.Dimension(52, 21));
							}
							{
								cmbExecStatus = new SComboBox(Const.ExecStatus, 8);
								tb2.add(cmbExecStatus);
								cmbExecStatus.setMinimumSize(new java.awt.Dimension(100, 21));
								cmbExecStatus.setMaximumSize(new java.awt.Dimension(100, 21));
								cmbExecStatus.setAutoscrolls(true);
								cmbExecStatus.setDoubleBuffered(true);
							}
							{
								tb2.addSeparator(new Dimension(5, 30));
							}
							{
								lLogCommon = new SLabel("执行结果");
								tb2.add(lLogCommon);
								lLogCommon.setMinimumSize(new java.awt.Dimension(60, 21));
								lLogCommon.setMaximumSize(new java.awt.Dimension(60, 21));
							}
							{
								txtLogCommon = new STextField();
								tb2.add(txtLogCommon);
								txtLogCommon.setMinimumSize(new java.awt.Dimension(100, 21));
								txtLogCommon.setMaximumSize(new java.awt.Dimension(100, 21));
							}
							{
								tb2.addSeparator(new Dimension(5, 30));
							}
							{
								lIfSendMsg = new SLabel("发送短信");
								tb2.add(lIfSendMsg);
								lIfSendMsg.setMinimumSize(new java.awt.Dimension(60, 21));
								lIfSendMsg.setMaximumSize(new java.awt.Dimension(60, 21));
							}
							{
								cmbIfSendMsg = new SComboBox(Const.IfSendMsg);
								tb2.add(cmbIfSendMsg);
								cmbIfSendMsg.setMinimumSize(new java.awt.Dimension(80, 21));
								cmbIfSendMsg.setMaximumSize(new java.awt.Dimension(80, 21));
								cmbIfSendMsg.setAutoscrolls(true);
								cmbIfSendMsg.setDoubleBuffered(true);
							}
							{
								tb2.addSeparator(new Dimension(5, 30));
							}
							{
								btnLogCommonQuery = new SButton("查  询", ImageContext.Query);
								tb2.add(btnLogCommonQuery);
								btnLogCommonQuery.setMinimumSize(new java.awt.Dimension(90, 25));
								btnLogCommonQuery.setMaximumSize(new java.awt.Dimension(90, 25));
								btnLogCommonQuery.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										try {
											btnLogCommonQuery.setEnabled(false);
											queryLogCommon();
										} finally {
											btnLogCommonQuery.setEnabled(true);
										}
									}
								});

							}
						}
					}
				}
				{
					tblLogCommon = new LogCommonTable(null).getJtable();
					scrlScheResult = new SScrollPane(tblLogCommon);
					spltScheResult.add(scrlScheResult, SSplitPane.BOTTOM);
				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建执行结果列表错误:", e);
		} finally {
		}
	}

	// 查询执行结果
	public void queryLogCommon() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			if (txtBegDate.getText().trim().length() > 0)
				map.put("开始日期", txtBegDate.getText());
			if (txtEndDate.getText().trim().length() > 0)
				map.put("结束日期", txtEndDate.getText());
			if (txtScheNameLogCommon.getText().trim().length() > 0)
				map.put("调度名称", txtScheNameLogCommon.getText().trim());
			if (txtTaskNameLogCommon.getText().trim().length() > 0)
				map.put("任务名称", txtTaskNameLogCommon.getText().trim());
			if (txtGroupNameLogCommon.getText().trim().length() > 0)
				map.put("任务组名称", txtGroupNameLogCommon.getText().trim());
			if (cmbTaskTypeLogCommon.getSelectedIndex() >= 0)
				map.put("任务类型", cmbTaskTypeLogCommon.getSelectedItem().toString());
			if (cmbExecStatus.getSelectedIndex() >= 0)
				map.put("执行状态", cmbExecStatus.getSelectedItem().toString());
			if (txtLogCommon.getText().trim().length() > 0)
				map.put("执行结果", txtLogCommon.getText());
			if (txtRecordCount.getText().trim().length() > 0)
				map.put("查询记录数", txtRecordCount.getText());
			if (cmbIfSendMsg.getSelectedIndex() > 0)
				map.put("是否发送短信", cmbIfSendMsg.getSelectedItem().toString());
			tblLogCommon = new LogCommonTable(map).getJtable();
			scrlScheResult.setViewportView(tblLogCommon);
		} catch (Exception e) {
			Log.logError("主程序执行结果查询错误:", e);
		} finally {
		}
	}

	// 清空执行结果
	public void clearLogCommon() {
		try {
			int msg = ShowMsg.showConfig("确定清空任务执行日志？");
			if (msg != 0) {
				return;
			}
			LogCommonDao.getInstance().clearLogCommon();

		} catch (Exception e) {
			Log.logError("主程序清空执行结果错误:", e);
		} finally {
		}
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}
}
