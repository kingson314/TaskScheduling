package com.log.abridgement;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

public class LogAbridgementTab {
	private JPanel pnlTab;
	private SLabel lLogAbridgementType;
	private SLabel lTaskTypeExecLogAbridgement;
	private SLabel lRecordCountExecLogAbridgement;
	private SLabel lTaskNameExecLogAbridgement;
	private SLabel lEnddateExecLogAbridgement;
	private SLabel lScheNameExecLogAbridgement;
	private SLabel lBedateExecLogAbridgement;
	private SLabel lGroupNameExecLogAbridgement;
	private SButton btnExecLogAbridgement;
	private SButton btnEndDateExecLogAbridgement;
	private SButton btnBegDateExecLogAbridgement;
	private SButton btnQueryExecLogAbridgement;
	private STextField txtScheNameExecLogAbridgement;
	private STextField txtTaskNameExecLogAbridgement;
	private STextField txtGroupNameExecLogAbridgement;
	private STextField txtRecordCountExecLogAbridgement;
	private STextField txtBegDateExecLogAbridgement;
	private STextField txtEndDateExecLogAbridgement;
	private SComboBox cmbTaskTypeExecLogAbridgement;
	private SComboBox cmbLogAbridgementTypeExecLogAbridgement;
	private SScrollPane scrlExecLogAbridgement;
	private SSplitPane spltLogAbridgement;
	private JToolBar tb1;
	private JTable tblExecLogAbridgement;
	private JPanel pnlLogAbridgement;
	private JToolBar tb2;

	private static LogAbridgementTab tab;

	public static LogAbridgementTab getInstance() {
		if (tab == null)
			tab = new LogAbridgementTab();
		return tab;
	}

	// 创建高频日志摘要列表
	private LogAbridgementTab() {
		try {
			pnlTab = new JPanel();
			GridLayout jPanel_ExecLogAbridgementLayout = new GridLayout(1, 1);
			jPanel_ExecLogAbridgementLayout.setColumns(1);
			jPanel_ExecLogAbridgementLayout.setHgap(5);
			jPanel_ExecLogAbridgementLayout.setVgap(5);
			pnlTab.setLayout(jPanel_ExecLogAbridgementLayout);
			// tabMain.addTab("执行日志摘要", null, pnlExecLogAbridgement, null);
			{
				spltLogAbridgement = new SSplitPane(0, 70, false);
				spltLogAbridgement.setEnabled(false);
				pnlTab.add(spltLogAbridgement);
				{
					pnlLogAbridgement = new JPanel();
					GridLayout layoutLogAbridgement = new GridLayout();
					layoutLogAbridgement.setColumns(1);
					layoutLogAbridgement.setRows(2);
					layoutLogAbridgement.setHgap(5);
					layoutLogAbridgement.setVgap(5);
					pnlLogAbridgement.setLayout(layoutLogAbridgement);
					spltLogAbridgement.add(pnlLogAbridgement, SSplitPane.TOP);
					{
						tb1 = new JToolBar();
						pnlLogAbridgement.add(tb1);
						{
							lBedateExecLogAbridgement = new SLabel("开始日期");
							tb1.add(lBedateExecLogAbridgement);
							lBedateExecLogAbridgement.setMinimumSize(new java.awt.Dimension(52, 21));
							lBedateExecLogAbridgement.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							txtBegDateExecLogAbridgement = new STextField();
							tb1.add(txtBegDateExecLogAbridgement);
							txtBegDateExecLogAbridgement.setMinimumSize(new java.awt.Dimension(70, 21));
							txtBegDateExecLogAbridgement.setMaximumSize(new java.awt.Dimension(70, 21));

						}

						{
							btnBegDateExecLogAbridgement = new SButton("..");
							tb1.add(btnBegDateExecLogAbridgement);
							btnBegDateExecLogAbridgement.setMinimumSize(new java.awt.Dimension(22, 21));
							btnBegDateExecLogAbridgement.setMaximumSize(new java.awt.Dimension(22, 21));
							btnBegDateExecLogAbridgement.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									new SCalendar(txtBegDateExecLogAbridgement);
								}
							});

						}
						{
							tb1.addSeparator(new Dimension(5, 35));
						}
						{
							lScheNameExecLogAbridgement = new SLabel("调度名称");
							tb1.add(lScheNameExecLogAbridgement);
							lScheNameExecLogAbridgement.setMinimumSize(new java.awt.Dimension(52, 21));
							lScheNameExecLogAbridgement.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							txtScheNameExecLogAbridgement = new STextField();
							tb1.add(txtScheNameExecLogAbridgement);
							txtScheNameExecLogAbridgement.setMinimumSize(new java.awt.Dimension(125, 21));
							txtScheNameExecLogAbridgement.setMaximumSize(new java.awt.Dimension(125, 21));

						}
						{
							tb1.addSeparator(new Dimension(5, 35));
						}
						{
							lTaskNameExecLogAbridgement = new SLabel("任务名称");
							tb1.add(lTaskNameExecLogAbridgement);
							lTaskNameExecLogAbridgement.setMinimumSize(new java.awt.Dimension(52, 21));
							lTaskNameExecLogAbridgement.setMaximumSize(new java.awt.Dimension(52, 21));

						}
						{
							txtTaskNameExecLogAbridgement = new STextField();
							tb1.add(txtTaskNameExecLogAbridgement);
							txtTaskNameExecLogAbridgement.setMinimumSize(new java.awt.Dimension(100, 21));
							txtTaskNameExecLogAbridgement.setMaximumSize(new java.awt.Dimension(100, 21));
						}
						{
							tb1.addSeparator(new Dimension(5, 30));
						}
						{
							lGroupNameExecLogAbridgement = new SLabel("任务组名称");
							tb1.add(lGroupNameExecLogAbridgement);
							lGroupNameExecLogAbridgement.setMinimumSize(new java.awt.Dimension(60, 21));
							lGroupNameExecLogAbridgement.setMaximumSize(new java.awt.Dimension(60, 21));
						}
						{
							txtGroupNameExecLogAbridgement = new STextField();
							tb1.add(txtGroupNameExecLogAbridgement);
							txtGroupNameExecLogAbridgement.setMinimumSize(new java.awt.Dimension(100, 21));
							txtGroupNameExecLogAbridgement.setMaximumSize(new java.awt.Dimension(100, 21));
						}
						{
							tb1.addSeparator(new Dimension(5, 35));
						}
						{
							lRecordCountExecLogAbridgement = new SLabel("查询记录数");
							tb1.add(lRecordCountExecLogAbridgement);
							lRecordCountExecLogAbridgement.setMinimumSize(new java.awt.Dimension(60, 21));
							lRecordCountExecLogAbridgement.setMaximumSize(new java.awt.Dimension(60, 21));
						}
						{
							txtRecordCountExecLogAbridgement = new STextField();
							txtRecordCountExecLogAbridgement.setText(String.valueOf(100));
							tb1.add(txtRecordCountExecLogAbridgement);
							txtRecordCountExecLogAbridgement.setMinimumSize(new java.awt.Dimension(80, 21));
							txtRecordCountExecLogAbridgement.setMaximumSize(new java.awt.Dimension(80, 21));

						}
						{
							tb1.addSeparator(new Dimension(5, 35));
						}
						{
							btnExecLogAbridgement = new SButton("清空日志", ImageContext.Clear);
							tb1.add(btnExecLogAbridgement);
							btnExecLogAbridgement.setMinimumSize(new java.awt.Dimension(90, 25));
							btnExecLogAbridgement.setMaximumSize(new java.awt.Dimension(90, 25));
							btnExecLogAbridgement.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									clearExecLogAbridgement();
								}
							});
						}

					}
					tb2 = new JToolBar();
					pnlLogAbridgement.add(tb2);
					{
						{
							lEnddateExecLogAbridgement = new SLabel("结束日期");
							tb2.add(lEnddateExecLogAbridgement);
							lEnddateExecLogAbridgement.setMinimumSize(new java.awt.Dimension(52, 21));
							lEnddateExecLogAbridgement.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							txtEndDateExecLogAbridgement = new STextField();
							tb2.add(txtEndDateExecLogAbridgement);
							txtEndDateExecLogAbridgement.setMinimumSize(new java.awt.Dimension(70, 21));
							txtEndDateExecLogAbridgement.setMaximumSize(new java.awt.Dimension(70, 21));
						}
						{
							btnEndDateExecLogAbridgement = new SButton("..");
							tb2.add(btnEndDateExecLogAbridgement);
							btnEndDateExecLogAbridgement.setMinimumSize(new java.awt.Dimension(22, 21));
							btnEndDateExecLogAbridgement.setMaximumSize(new java.awt.Dimension(22, 21));
							btnEndDateExecLogAbridgement.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									new SCalendar(txtEndDateExecLogAbridgement);
								}
							});

						}
						{
							tb2.addSeparator(new Dimension(5, 30));
						}
						{
							lTaskTypeExecLogAbridgement = new SLabel("任务类型");
							tb2.add(lTaskTypeExecLogAbridgement);
							lTaskTypeExecLogAbridgement.setMinimumSize(new java.awt.Dimension(52, 21));
							lTaskTypeExecLogAbridgement.setMaximumSize(new java.awt.Dimension(52, 21));

						}
						{
							cmbTaskTypeExecLogAbridgement = new SComboBox(AppVar.TASK_TYPE_Combobox, 8);
							tb2.add(cmbTaskTypeExecLogAbridgement);

							cmbTaskTypeExecLogAbridgement.setAutoscrolls(true);
							cmbTaskTypeExecLogAbridgement.setDoubleBuffered(true);
							cmbTaskTypeExecLogAbridgement.setMinimumSize(new java.awt.Dimension(125, 21));
							cmbTaskTypeExecLogAbridgement.setMaximumSize(new java.awt.Dimension(125, 21));
						}
						{
							tb2.addSeparator(new Dimension(5, 30));
						}
						{
							lLogAbridgementType = new SLabel("查询类型");

							tb2.add(lLogAbridgementType);
							lLogAbridgementType.setMinimumSize(new java.awt.Dimension(52, 21));
							lLogAbridgementType.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							cmbLogAbridgementTypeExecLogAbridgement = new SComboBox(Const.LogAbridgementType, 8);
							tb2.add(cmbLogAbridgementTypeExecLogAbridgement);

							cmbLogAbridgementTypeExecLogAbridgement.setMinimumSize(new java.awt.Dimension(100, 21));
							cmbLogAbridgementTypeExecLogAbridgement.setMaximumSize(new java.awt.Dimension(100, 21));

							cmbLogAbridgementTypeExecLogAbridgement.addItemListener(new ItemListener() {
								public void itemStateChanged(ItemEvent evt) {
									if (cmbLogAbridgementTypeExecLogAbridgement.getSelectedIndex() == 0) {
										setComponentVisiable_LogAbridgement(false);
									} else {
										setComponentVisiable_LogAbridgement(true);
									}
								}
							});
						}
						{
							tb2.addSeparator(new Dimension(315, 30));
						}
						{
							btnQueryExecLogAbridgement = new SButton("查  询", ImageContext.Query);
							tb2.add(btnQueryExecLogAbridgement);
							btnQueryExecLogAbridgement.setMinimumSize(new java.awt.Dimension(90, 25));
							btnQueryExecLogAbridgement.setMaximumSize(new java.awt.Dimension(90, 25));
							btnQueryExecLogAbridgement.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									try {
										btnQueryExecLogAbridgement.setEnabled(false);
										queryExecLogAbridgement();
									} finally {
										btnQueryExecLogAbridgement.setEnabled(true);
									}
								}
							});

						}
					}
				}
				setComponentVisiable_LogAbridgement(false);
				{
					{
						tblExecLogAbridgement = new LogAbridgementTable(null, null).getJtable();
					}
					scrlExecLogAbridgement = new SScrollPane(tblExecLogAbridgement);
					spltLogAbridgement.add(scrlExecLogAbridgement, SSplitPane.BOTTOM);
					scrlExecLogAbridgement.setSize(885, 605);

				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建日志摘要列表错误:", e);
		} finally {
		}
	}

	// 设置高频日志摘要查询列表控件状态
	private void setComponentVisiable_LogAbridgement(boolean value) {
		try {
			txtBegDateExecLogAbridgement.setEnabled(value);
			btnBegDateExecLogAbridgement.setEnabled(value);
			txtScheNameExecLogAbridgement.setEnabled(value);
			txtTaskNameExecLogAbridgement.setEnabled(value);
			txtGroupNameExecLogAbridgement.setEnabled(value);
			txtRecordCountExecLogAbridgement.setEnabled(value);
			txtEndDateExecLogAbridgement.setEnabled(value);
			btnEndDateExecLogAbridgement.setEnabled(value);
			cmbTaskTypeExecLogAbridgement.setEnabled(value);
		} catch (Exception e) {
			Log.logError("主程序设置高频日志摘要查询列表控件状态错误:", e);
		} finally {
		}
	}

	// 查询高频日志摘要
	public void queryExecLogAbridgement() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			if (txtBegDateExecLogAbridgement.getText().trim().length() > 0)
				map.put("开始日期", txtBegDateExecLogAbridgement.getText());
			if (txtEndDateExecLogAbridgement.getText().trim().length() > 0)
				map.put("结束日期", txtEndDateExecLogAbridgement.getText());
			if (txtScheNameExecLogAbridgement.getText().trim().length() > 0)
				map.put("调度名称", txtScheNameExecLogAbridgement.getText().trim());
			if (txtTaskNameExecLogAbridgement.getText().trim().length() > 0)
				map.put("任务名称", txtTaskNameExecLogAbridgement.getText().trim());
			if (txtGroupNameExecLogAbridgement.getText().trim().length() > 0)
				map.put("任务组名称", txtGroupNameExecLogAbridgement.getText().trim());
			if (cmbTaskTypeExecLogAbridgement.getSelectedIndex() >= 0)
				map.put("任务类型", cmbTaskTypeExecLogAbridgement.getSelectedItem().toString());
			if (txtRecordCountExecLogAbridgement.getText().trim().length() > 0)
				map.put("查询记录数", txtRecordCountExecLogAbridgement.getText());
			if (cmbLogAbridgementTypeExecLogAbridgement.getSelectedIndex() >= 0)
				map.put("查询类型", cmbLogAbridgementTypeExecLogAbridgement.getSelectedItem().toString());
			tblExecLogAbridgement = new LogAbridgementTable(map, Log.MapLogAbridgement).getJtable();
			scrlExecLogAbridgement.setViewportView(tblExecLogAbridgement);
		} catch (Exception e) {
			Log.logError("主程序高频日志摘要查询错误:", e);
		} finally {
		}
	}

	// 清空高频日志摘要
	public void clearExecLogAbridgement() {
		try {
			if (cmbLogAbridgementTypeExecLogAbridgement.getSelectedIndex() == 0) {
				int msg = ShowMsg.showConfig("确定清空" + Const.LogAbridgementType[0] + "？");
				if (msg == 0) {
					Log.MapLogAbridgement.clear();
					Log.MapLogAbridgementBatch.clear();
				}
			} else if (cmbLogAbridgementTypeExecLogAbridgement.getSelectedIndex() == 1) {
				int msg = ShowMsg.showConfig("确定清空" + Const.LogAbridgementType[1] + "？");
				if (msg == 0) {
					LogAbridgementDao.getInstance().clearLogAbridgement();
				}
			}
			queryExecLogAbridgement();
		} catch (Exception e) {
			Log.logError("主程序清空高频日志摘要错误:", e);
		} finally {
		}
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}

}
