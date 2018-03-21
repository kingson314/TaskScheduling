package com.threadPool;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;


import com.log.Log;

import common.component.SButton;
import common.component.SComboBox;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.SSplitPane;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.swing.UtilComponent;
import consts.ImageContext;

public class ThreadTab {
	private final static String[] ThreadStatus = { "", "完成", "取消", "运行中" };
	private JTable tblThread;
	private SScrollPane scrlThread;
	private SComboBox cmbThreadStatus;
	private STextField txtScheCde;
	private JPanel pnlTab;
	private SSplitPane spltThread;
	private JPanel pnlTool;
	private JToolBar tbThread;
	private SButton btnThreadquery;
	private static ThreadTab tab;

	public static ThreadTab getInstance() {
		if (tab == null)
			tab = new ThreadTab();
		return tab;
	}

	// 构造
	private ThreadTab() {
		pnlTab = new JPanel();
		try {
			GridLayout layoutThreadTab = new GridLayout(1, 1);
			layoutThreadTab.setColumns(1);
			layoutThreadTab.setHgap(5);
			layoutThreadTab.setVgap(5);
			pnlTab.setLayout(layoutThreadTab);
			pnlTab.setPreferredSize(new java.awt.Dimension(715, 254));
			{
				spltThread = new SSplitPane(0, 35, false);
				spltThread.setEnabled(false);
				pnlTab.add(spltThread);
				{
					pnlTool = new JPanel();
					GridLayout layoutTool = new GridLayout(1, 1);
					layoutTool.setColumns(1);
					layoutTool.setHgap(5);
					layoutTool.setVgap(5);
					pnlTool.setLayout(layoutTool);
					spltThread.add(pnlTool, SSplitPane.TOP);
					pnlTool.setPreferredSize(new java.awt.Dimension(713, 33));
					{
						tbThread = new JToolBar();
						pnlTool.add(tbThread);

						{
							SButton btnAddThread = new SButton("停止线程", ImageContext.Stop);
							btnAddThread.setMaximumSize(new java.awt.Dimension(80, 25));
							btnAddThread.setMinimumSize(new java.awt.Dimension(80, 25));
							tbThread.add(btnAddThread);
							btnAddThread.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									stopThread();
								}
							});
						}

						{
							tbThread.addSeparator(new Dimension(60, 30));
						}
						{
							SLabel lThreadStatus = new SLabel("线程状态");
							tbThread.add(lThreadStatus);
							lThreadStatus.setMinimumSize(new java.awt.Dimension(52, 21));
							lThreadStatus.setMaximumSize(new java.awt.Dimension(52, 21));
						}
						{
							cmbThreadStatus = new SComboBox(ThreadStatus);
							tbThread.add(cmbThreadStatus);
							cmbThreadStatus.setMaximumSize(new java.awt.Dimension(125, 21));
							cmbThreadStatus.setMinimumSize(new java.awt.Dimension(125, 21));
						}
						{
							tbThread.addSeparator(new Dimension(60, 30));
						}
						{
							SLabel lScheCod = new SLabel("调度标志");
							tbThread.add(lScheCod);
							lScheCod.setMinimumSize(new java.awt.Dimension(52, 21));
							lScheCod.setMaximumSize(new java.awt.Dimension(52, 21));
							lScheCod.setHorizontalAlignment(SwingConstants.RIGHT);
							lScheCod.setHorizontalTextPosition(SwingConstants.RIGHT);
						}
						{
							txtScheCde = new STextField();
							tbThread.add(txtScheCde);
							txtScheCde.setMaximumSize(new java.awt.Dimension(120, 21));
							txtScheCde.setMinimumSize(new java.awt.Dimension(120, 21));
						}
						{
							tbThread.addSeparator(new Dimension(60, 30));
						}
						{
							btnThreadquery = new SButton("查  询", ImageContext.Query);
							tbThread.add(btnThreadquery);
							btnThreadquery.setMinimumSize(new java.awt.Dimension(90, 25));
							btnThreadquery.setMaximumSize(new java.awt.Dimension(90, 25));
							btnThreadquery.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									try {
										btnThreadquery.setEnabled(false);
										queryThread();
									} finally {
										btnThreadquery.setEnabled(true);
									}
								}
							});
						}

					}

				}
				{
					{
						Map<String, String> map = new HashMap<String, String>();
						map.put("Type", "全部");
						tblThread = new ThreadTable(map).getJtable();

					}
					scrlThread = new SScrollPane(tblThread);
					spltThread.add(scrlThread, SSplitPane.BOTTOM);

				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建任务列表错误:", e);
		} finally {
		}
	}

	// 查询线程
	public void queryThread() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("线程状态", cmbThreadStatus.getSelectedItem().toString());
		map.put("调度标志", txtScheCde.getText().trim());
		tblThread = new ThreadTable(map).getJtable();
		scrlThread.setViewportView(tblThread);
	}

	// 停止线程，刷新线程表格
	public void stopThread() {
		stopThread(this.tblThread);
		queryThread();
	}

	// 停止线程
	private void stopThread(JTable jtable) {
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(jtable);
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选线程");
				return;
			}

			for (int i = 0; i < jtable.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(jtable.getValueAt(i, 0).toString());
				if (selected) {
					String scheCod = jtable.getValueAt(i, 1).toString();
					String groupId = jtable.getValueAt(i, 2).toString();
					String taskId = jtable.getValueAt(i, 3).toString();
					String taskOrder = jtable.getValueAt(i, 4).toString();
					String taskThreadIndex = jtable.getValueAt(i, 5).toString();
					String key = "";
					if (taskThreadIndex.equals(""))
						key = scheCod + "|" + groupId + "|" + taskId + "|" + taskOrder;
					else
						key = scheCod + "|" + groupId + "|" + taskId + "|" + taskOrder + "|" + taskThreadIndex;
					Future<?> future = ThreadPool.MapFuture.get(key);
					future.cancel(true);
					// System.out.println("stopThread:" + key);
					// Fun.MapTaskStartTime.put(key, 0l);
				}
			}
		} catch (Exception e) {

		}
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}

}
