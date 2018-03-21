package com.log.sche;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

import com.log.Log;
import common.component.SButton;
import common.component.SComboBox;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.SSplitPane;
import common.component.STextField;
import common.component.ShowMsg;

import consts.Const;
import consts.ImageContext;

public class LogScheTab {
	private JPanel pnlTab;
	private SLabel lExecTimeLogSche;
	private SLabel lScheIdLogSche;
	private SButton btnLogSchequery;
	private STextField txtScheIdLogSche;
	private STextField txtExecTimeLogSche;
	private SComboBox cmbCompareTypeLogSche;
	private JToolBar tbLogSche;
	private SScrollPane scrlLogSche;
	private SSplitPane spltLogSche;
	private JTable tblLogSche;
	private JPanel pnlLogSche;
	private static LogScheTab tab;

	public static LogScheTab getInstance() {
		if (tab == null)
			tab = new LogScheTab();
		return tab;
	}

	// 创建最近调度日志列表
	private LogScheTab() {
		try {
			pnlTab = new JPanel();
			GridLayout jPanel_LogScheLayout = new GridLayout(1, 1);
			jPanel_LogScheLayout.setColumns(1);
			jPanel_LogScheLayout.setHgap(5);
			jPanel_LogScheLayout.setVgap(5);
			pnlTab.setLayout(jPanel_LogScheLayout);
			{
				spltLogSche = new SSplitPane(0, 35, false);
				pnlTab.add(spltLogSche);
				{
					pnlLogSche = new JPanel();
					GridLayout layoutLogSche = new GridLayout(1, 1);
					layoutLogSche.setColumns(1);
					layoutLogSche.setHgap(5);
					layoutLogSche.setVgap(5);
					pnlLogSche.setLayout(layoutLogSche);
					spltLogSche.add(pnlLogSche, SSplitPane.TOP);
					pnlLogSche.setPreferredSize(new java.awt.Dimension(713, 33));
					{
						tbLogSche = new JToolBar();
						pnlLogSche.add(tbLogSche);
						{
							lScheIdLogSche = new SLabel("调度标志");
							tbLogSche.add(lScheIdLogSche);
							lScheIdLogSche.setMinimumSize(new java.awt.Dimension(52, 21));
							lScheIdLogSche.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							txtScheIdLogSche = new STextField();
							tbLogSche.add(txtScheIdLogSche);
							txtScheIdLogSche.setMinimumSize(new java.awt.Dimension(125, 21));
							txtScheIdLogSche.setMaximumSize(new java.awt.Dimension(125, 21));
						}
						{
							tbLogSche.addSeparator(new Dimension(5, 30));
						}
						{
							lExecTimeLogSche = new SLabel("执行时间");
							tbLogSche.add(lExecTimeLogSche);
							lExecTimeLogSche.setMinimumSize(new java.awt.Dimension(52, 21));
							lExecTimeLogSche.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							cmbCompareTypeLogSche = new SComboBox(Const.CompareType);
							tbLogSche.add(cmbCompareTypeLogSche);
							cmbCompareTypeLogSche.setMinimumSize(new java.awt.Dimension(80, 21));
							cmbCompareTypeLogSche.setMaximumSize(new java.awt.Dimension(80, 21));
						}
						{
							tbLogSche.addSeparator(new Dimension(1, 30));
						}
						{
							txtExecTimeLogSche = new STextField();
							txtExecTimeLogSche.setText("0");
							tbLogSche.add(txtExecTimeLogSche);
							txtExecTimeLogSche.setMinimumSize(new java.awt.Dimension(80, 21));
							txtExecTimeLogSche.setMaximumSize(new java.awt.Dimension(80, 21));

						}
						{
							tbLogSche.addSeparator(new Dimension(5, 30));
						}
						{
							btnLogSchequery = new SButton("查  询", ImageContext.Query);
							tbLogSche.add(btnLogSchequery);
							btnLogSchequery.setMinimumSize(new java.awt.Dimension(90, 25));
							btnLogSchequery.setMaximumSize(new java.awt.Dimension(90, 25));
							btnLogSchequery.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									try {
										btnLogSchequery.setEnabled(false);
										queryLogSche();
									} finally {
										btnLogSchequery.setEnabled(true);
									}
								}
							});

						}
					}
				}
				{
					tblLogSche = new LogScheTable(null, null).getJtable();
					scrlLogSche = new SScrollPane(tblLogSche);
					spltLogSche.add(scrlLogSche, SSplitPane.BOTTOM);
					scrlLogSche.setSize(885, 605);
				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建最近调度日志列表错误:", e);
		} finally {
		}
	}

	// 查询高频最近调度日志
	public void queryLogSche() {
		try {
			if (txtScheIdLogSche.getText().trim().length() == 0) {
				ShowMsg.showMsg("请输入调度标志");
			}
			Map<String, String> map = null;

			map = new HashMap<String, String>();
			if (txtScheIdLogSche.getText() == null) {
				map.put("调度标志", "");
			} else if (txtScheIdLogSche.getText().trim().length() >= 0)
				map.put("调度标志", txtScheIdLogSche.getText().trim());

			if (cmbCompareTypeLogSche.getSelectedIndex() >= 0)
				map.put("比较类型", cmbCompareTypeLogSche.getSelectedItem().toString());
			else
				map.put("比较类型", "");
			if (txtExecTimeLogSche.getText() == null)
				map.put("执行时间", "");
			else
				map.put("执行时间", txtExecTimeLogSche.getText().trim());

			tblLogSche = new LogScheTable(map, Log.MapAddLogSche).getJtable();
			scrlLogSche.setViewportView(tblLogSche);
		} catch (Exception e) {
			Log.logError("主程序最近高频日志查询错误:", e);
		} finally {
		}
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}
}
