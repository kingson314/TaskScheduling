package com.log.custom;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

import com.log.Log;
import common.component.SButton;
import common.component.SScrollPane;
import common.component.SSplitPane;

import consts.ImageContext;

public class LogCustomTab {
	private SButton btnRefresh;
	private SButton btnSet;
	private JPanel pnlTab;
	private SSplitPane splt;
	private JTable tbl;
	private JToolBar tb;
	private SScrollPane scrl;
	private JPanel pnlTool;
	private static LogCustomTab tab;

	public static LogCustomTab getInstance() {
		if (tab == null)
			tab = new LogCustomTab();
		return tab;
	}

	// 创建最近高频日志列表
	private LogCustomTab() {
		pnlTab = new JPanel();
		try {
			GridLayout layout = new GridLayout(1, 1);
			layout.setColumns(1);
			layout.setHgap(5);
			layout.setVgap(5);
			pnlTab.setLayout(layout);
			pnlTab.setPreferredSize(new java.awt.Dimension(715, 254));
			{
				splt = new SSplitPane(0, 35, false);
				splt.setEnabled(false);
				pnlTab.add(splt);
				{
					pnlTool = new JPanel();
					GridLayout layoutTool = new GridLayout(1, 1);
					layoutTool.setColumns(1);
					layoutTool.setHgap(5);
					layoutTool.setVgap(5);
					pnlTool.setLayout(layoutTool);
					splt.add(pnlTool, SSplitPane.TOP);
					pnlTool.setPreferredSize(new java.awt.Dimension(713, 33));
					{

						tb = new JToolBar();
						pnlTool.add(tb);
						{
							btnSet = new SButton("配  置", ImageContext.Settings);
							tb.add(btnSet);
							btnSet.setMinimumSize(new java.awt.Dimension(100, 25));
							btnSet.setMaximumSize(new java.awt.Dimension(100, 25));
							btnSet.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									set();
								}
							});

						}

						{
							tb.addSeparator(new Dimension(5, 30));
						}
						{
							btnRefresh = new SButton("刷  新", ImageContext.Refresh);
							tb.add(btnRefresh);
							btnRefresh.setMinimumSize(new java.awt.Dimension(100, 25));
							btnRefresh.setMaximumSize(new java.awt.Dimension(100, 25));
							btnRefresh.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									refresh();
								}
							});

						}
					}

				}
				{

					scrl = new SScrollPane(null);
					splt.add(scrl, SSplitPane.BOTTOM);
					scrl.setSize(885, 605);
				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建最近高频日志列表错误:", e);
		} finally {
		}
	}

	// 刷新
	private void refresh() {
		tbl = new LogCustomTable().getJtable();
		scrl = new SScrollPane(tbl);
		splt.add(scrl, SSplitPane.BOTTOM);
		scrl.setSize(885, 605);
	}

	// 打开配置对话框

	@SuppressWarnings("deprecation")
	private void set() {
		JDialog dialog = new LogCustomSet();
		dialog.show(true);
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}
}
