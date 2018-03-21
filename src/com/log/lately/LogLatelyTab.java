package com.log.lately;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

import com.app.AppTableView;
import com.log.Log;
import common.component.SButton;
import common.component.SComboBox;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.SSplitPane;
import common.component.STextField;

import consts.Const;
import consts.ImageContext;

public class LogLatelyTab {
	private SButton btnLogLatelyQuery;
	private JPanel pnlTab;
	private SLabel lExecType;
	private SLabel lTaskId;
	private SLabel lGroupId;
	private STextField txtExecTime;
	private SComboBox cmbCompareType;
	private SSplitPane spltLogLately;
	private JTable tblLogLately;
	private JToolBar tbLogLately;
	private STextField txtScheId;
	private STextField txtTaskId;
	private STextField txtGroupId;
	private SScrollPane scrlLoglate;
	private JPanel pnlTool;
	private static LogLatelyTab tab;

	public static LogLatelyTab getInstance() {
		if (tab == null)
			tab = new LogLatelyTab();
		return tab;
	}

	// 创建最近高频日志列表
	private LogLatelyTab() {
		pnlTab = new JPanel();
		try {
			GridLayout layoutLogLatelyTab = new GridLayout(1, 1);
			layoutLogLatelyTab.setColumns(1);
			layoutLogLatelyTab.setHgap(5);
			layoutLogLatelyTab.setVgap(5);
			pnlTab.setLayout(layoutLogLatelyTab);
			pnlTab.setPreferredSize(new java.awt.Dimension(715, 254));
			{
				spltLogLately = new SSplitPane(0, 35, false);
				spltLogLately.setEnabled(false);
				pnlTab.add(spltLogLately);
				{
					pnlTool = new JPanel();
					GridLayout layoutTool = new GridLayout(1, 1);
					layoutTool.setColumns(1);
					layoutTool.setHgap(5);
					layoutTool.setVgap(5);
					pnlTool.setLayout(layoutTool);
					spltLogLately.add(pnlTool, SSplitPane.TOP);
					pnlTool.setPreferredSize(new java.awt.Dimension(713, 33));
					{

						tbLogLately = new JToolBar();
						pnlTool.add(tbLogLately);
						{
							SLabel lScheID = new SLabel("调度标志");
							tbLogLately.add(lScheID);
							lScheID.setLocation(new java.awt.Point(0, 0));
							lScheID.setMinimumSize(new java.awt.Dimension(52, 21));
							lScheID.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							txtScheId = new STextField();
							tbLogLately.add(txtScheId);
							txtScheId.setMinimumSize(new java.awt.Dimension(125, 21));
							txtScheId.setMaximumSize(new java.awt.Dimension(125, 21));

						}
						{
							tbLogLately.addSeparator(new Dimension(5, 30));
						}
						{
							lTaskId = new SLabel("任务标志");
							tbLogLately.add(lTaskId);
							lTaskId.setMinimumSize(new java.awt.Dimension(52, 21));
							lTaskId.setMaximumSize(new java.awt.Dimension(52, 21));

						}
						{
							txtTaskId = new STextField();
							tbLogLately.add(txtTaskId);
							txtTaskId.setMinimumSize(new java.awt.Dimension(100, 21));
							txtTaskId.setMaximumSize(new java.awt.Dimension(100, 21));
						}
						{
							tbLogLately.addSeparator(new Dimension(5, 30));
						}
						{
							lGroupId = new SLabel("任务组标志");
							tbLogLately.add(lGroupId);
							lGroupId.setMinimumSize(new java.awt.Dimension(60, 21));
							lGroupId.setMaximumSize(new java.awt.Dimension(60, 21));
						}
						{
							txtGroupId = new STextField();
							tbLogLately.add(txtGroupId);
							txtGroupId.setMinimumSize(new java.awt.Dimension(100, 21));
							txtGroupId.setMaximumSize(new java.awt.Dimension(100, 21));

						}
						{
							tbLogLately.addSeparator(new Dimension(5, 30));
						}

						{
							lExecType = new SLabel("执行时间");
							tbLogLately.add(lExecType);
							lExecType.setMinimumSize(new java.awt.Dimension(60, 21));
							lExecType.setMaximumSize(new java.awt.Dimension(60, 21));
						}
						{
							cmbCompareType = new SComboBox(Const.CompareType);
							tbLogLately.add(cmbCompareType);
							cmbCompareType.setMinimumSize(new java.awt.Dimension(80, 21));
							cmbCompareType.setMaximumSize(new java.awt.Dimension(80, 21));
						}
						{
							tbLogLately.addSeparator(new Dimension(1, 30));
						}
						{
							txtExecTime = new STextField();
							txtExecTime.setText("0");
							tbLogLately.add(txtExecTime);
							txtExecTime.setMinimumSize(new java.awt.Dimension(100, 21));
							txtExecTime.setMaximumSize(new java.awt.Dimension(100, 21));

						}
						{
							tbLogLately.addSeparator(new Dimension(5, 30));
						}
						{
							btnLogLatelyQuery = new SButton("刷  新", ImageContext.Refresh);
							tbLogLately.add(btnLogLatelyQuery);
							btnLogLatelyQuery.setMinimumSize(new java.awt.Dimension(100, 25));
							btnLogLatelyQuery.setMaximumSize(new java.awt.Dimension(100, 25));
							btnLogLatelyQuery.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									try {
										btnLogLatelyQuery.setEnabled(false);
										queryLogLately();
									} finally {
										btnLogLatelyQuery.setEnabled(true);
									}
								}
							});

						}
					}

				}
				{
					tblLogLately = new LogLatelyTable(null, null).getJtable();
					scrlLoglate = new SScrollPane(tblLogLately);
					spltLogLately.add(scrlLoglate, SSplitPane.BOTTOM);
					scrlLoglate.setSize(885, 605);
				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建最近高频日志列表错误:", e);
		} finally {
		}
	}

	// 查询高频最近日志
	public void queryLogLately() {
		try {
			Map<String, String> map = null;
			map = new HashMap<String, String>();
			if (txtScheId.getText() == null) {
				map.put("调度标志", "");
			} else if (txtScheId.getText().trim().length() >= 0)
				map.put("调度标志", txtScheId.getText().trim());
			if (txtTaskId.getText() == null) {
				map.put("任务标志", "");
			} else if (txtTaskId.getText().trim().length() >= 0)
				map.put("任务标志", txtTaskId.getText().trim());
			if (txtGroupId.getText() == null) {
				map.put("任务组标志", "");
			} else if (txtGroupId.getText().trim().length() >= 0)
				map.put("任务组标志", txtGroupId.getText().trim());
			if (cmbCompareType.getSelectedIndex() >= 0)
				map.put("比较类型", cmbCompareType.getSelectedItem().toString());
			else
				map.put("比较类型", "");
			if (txtExecTime.getText() == null)
				map.put("执行时间", "");
			else
				map.put("执行时间", txtExecTime.getText().trim());

			tblLogLately = new LogLatelyTable(map, Log.MapAddLogLately).getJtable();
			scrlLoglate.setViewportView(tblLogLately);
		} catch (Exception e) {
			Log.logError("主程序最近高频日志查询错误:", e);
		} finally {
		}
	}

	// 根据Logdgement查询高频最近调度日志
	public void queryLogLately(JTable table) {
		if (table.getSelectedColumnCount() == 0)
			return;
		AppTableView.getInstance().addTab("最近高频日志");
		Map<String, String> map = new HashMap<String, String>();
		map.put("调度标志", table.getValueAt(table.getSelectedRow(), 0).toString());
		map.put("任务标志", table.getValueAt(table.getSelectedRow(), 15).toString());
		map.put("任务组标志", table.getValueAt(table.getSelectedRow(), 13).toString());
		map.put("比较类型", "大于");
		map.put("执行时间", "-1");

		txtScheId.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
		txtTaskId.setText(table.getValueAt(table.getSelectedRow(), 15).toString());
		txtGroupId.setText(table.getValueAt(table.getSelectedRow(), 13).toString());
		tblLogLately = new LogLatelyTable(map, Log.MapAddLogLately).getJtable();
		scrlLoglate.setViewportView(tblLogLately);
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}
}
