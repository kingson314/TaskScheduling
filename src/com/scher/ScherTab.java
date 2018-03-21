package com.scher;

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
import common.util.swing.UtilComponent;
import consts.Const;
import consts.ImageContext;

/**
 * @author: fenggq
 * @Email:
 * @Company:
 * @Action:调度显示列表
 * @DATE: Mar 8, 2012
 */
public class ScherTab {

	private SComboBox cmbScheStatus;
	private SButton btnEditSche;
	private SButton btnStartSche;
	private SButton btnStopSche;
	private SButton btnRefreshSche;
	private SButton btnDelSche;
	private SScrollPane scrlSche;
	private STextField txtScheName;
	private JPanel pnlTab;
	private JTable tblSche;
	private SButton btnScheQuery;
	private static ScherTab tab;

	public static ScherTab getInstance() {
		if (tab == null)
			tab = new ScherTab();
		return tab;
	}

	// 构造
	private ScherTab() {
		pnlTab = new JPanel();
		try {
			GridLayout jPanel_scheLayout = new GridLayout(1, 1);
			jPanel_scheLayout.setColumns(1);
			jPanel_scheLayout.setHgap(5);
			jPanel_scheLayout.setVgap(5);
			pnlTab.setLayout(jPanel_scheLayout);
			// tabMain.addTab("调度列表", null, pnlSche, null);
			{
				SSplitPane spltSche = new SSplitPane(0, 35, false);
				pnlTab.add(spltSche);
				spltSche.setEnabled(false);
				{
					JPanel pnlToolSche = new JPanel();
					GridLayout layoutScheTool = new GridLayout(1, 1);
					layoutScheTool.setColumns(1);
					layoutScheTool.setHgap(5);
					layoutScheTool.setVgap(5);
					pnlToolSche.setLayout(layoutScheTool);
					spltSche.add(pnlToolSche, SSplitPane.TOP);
					pnlToolSche.setPreferredSize(new java.awt.Dimension(713, 33));
					pnlToolSche.setSize(885, 33);
					{
						JToolBar tbSche = new JToolBar();
						pnlToolSche.add(tbSche);
						tbSche.setSize(885, 25);
						tbSche.setPreferredSize(new java.awt.Dimension(880, 34));
						{
							btnStartSche = new SButton("\u542f\u52a8\u8c03\u5ea6", ImageContext.Start);
							tbSche.add(btnStartSche);
							btnStartSche.setSize(60, 25);
							btnStartSche.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									startSche();
								}
							});
						}
						{
							tbSche.addSeparator(new Dimension(5, 30));
						}
						{
							btnStopSche = new SButton("\u505c\u6b62\u8c03\u5ea6", ImageContext.Stop);
							tbSche.add(btnStopSche);
							btnStopSche.setSize(60, 25);
							btnStopSche.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									stopSche();
								}
							});
						}
						{
							tbSche.addSeparator(new Dimension(5, 30));
						}

						{
							btnEditSche = new SButton("编辑调度", ImageContext.Mod);
							tbSche.add(btnEditSche);
							btnEditSche.setSize(60, 25);
							btnEditSche.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									editSche();
									;
								}
							});
						}
						{
							tbSche.addSeparator(new Dimension(5, 30));
						}

						{
							btnDelSche = new SButton("\u5220\u9664\u8c03\u5ea6", ImageContext.Del);
							tbSche.add(btnDelSche);
							btnDelSche.setSize(60, 25);
							btnDelSche.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									delSche();
								}
							});
						}
						{
							tbSche.addSeparator(new Dimension(5, 30));
						}

						{
							btnRefreshSche = new SButton("刷新调度状态", ImageContext.Refresh);
							tbSche.add(btnRefreshSche);
							btnRefreshSche.setSize(60, 25);
							btnRefreshSche.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									try {
										btnRefreshSche.setEnabled(false);
										JobOperater.refreshSche();
									} finally {
										btnRefreshSche.setEnabled(true);
									}
								}
							});
						}
						{
							tbSche.addSeparator(new Dimension(60, 30));
						}

						{
							SLabel lscheName = new SLabel("调度名称");
							tbSche.add(lscheName);
							lscheName.setMaximumSize(new java.awt.Dimension(52, 21));
							lscheName.setMinimumSize(new java.awt.Dimension(52, 21));
						}
						{
							txtScheName = new STextField();
							tbSche.add(txtScheName);
							txtScheName.setMinimumSize(new java.awt.Dimension(120, 21));
							txtScheName.setMaximumSize(new java.awt.Dimension(120, 21));
						}
						{
							tbSche.addSeparator(new Dimension(5, 30));
						}
						{
							SLabel lschestatus = new SLabel("调度状态");
							tbSche.add(lschestatus);
							lschestatus.setMaximumSize(new java.awt.Dimension(52, 21));
							lschestatus.setMinimumSize(new java.awt.Dimension(52, 21));
						}
						{
							cmbScheStatus = new SComboBox(Const.ScheStatus);
							tbSche.add(cmbScheStatus);
							cmbScheStatus.setMinimumSize(new java.awt.Dimension(80, 21));
							cmbScheStatus.setMaximumSize(new java.awt.Dimension(80, 21));
						}
						{
							tbSche.addSeparator(new Dimension(5, 30));
						}
						{
							btnScheQuery = new SButton("\u67e5  \u8be2", ImageContext.Query);
							tbSche.add(btnScheQuery);
							btnScheQuery.setMinimumSize(new java.awt.Dimension(90, 25));
							btnScheQuery.setMaximumSize(new java.awt.Dimension(90, 25));
							btnScheQuery.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									try {
										btnScheQuery.setEnabled(false);
										querySche();
									} finally {
										btnScheQuery.setEnabled(true);
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
						tblSche = new ScherTable(map).getJtable();
						if (tblSche.getRowCount() > 0) {
							scheButton(true);
						} else {
							scheButton(false);
						}
					}
					scrlSche = new SScrollPane(tblSche);
					spltSche.add(scrlSche, SSplitPane.BOTTOM);

				}
			}
		} catch (Exception e) {
			Log.logError("主程序创建调度列表错误:", e);
		} finally {
		}
	}

	// 调度过滤
	public void querySche() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("Type", "调度过滤");
			map.put("调度名称", txtScheName.getText() == null ? "" : txtScheName.getText().trim());
			if (cmbScheStatus.getSelectedIndex() >= 0)
				map.put("调度状态", cmbScheStatus.getSelectedItem().toString() == null ? "" : cmbScheStatus.getSelectedItem().toString().trim());
			tblSche = new ScherTable(map).getJtable();
			scrlSche.setViewportView(tblSche);

			if (tblSche.getRowCount() > 0) {
				scheButton(true);
			} else {
				scheButton(false);
			}
		} catch (Exception e) {
			Log.logError("主程序过滤调度错误:", e);
		} finally {
		}
	}

	// 设置调度列表按钮状态
	private void scheButton(boolean bool) {
		try {
			btnEditSche.setEnabled(bool);
			btnStartSche.setEnabled(bool);
			btnStopSche.setEnabled(bool);
			btnRefreshSche.setEnabled(bool);
			btnDelSche.setEnabled(bool);
		} catch (Exception e) {
			Log.logError("主程序设置调度列表按钮状态错误:", e);
		} finally {
		}
	}

	// 修改调度

	@SuppressWarnings("deprecation")
	public void editSche(JTable tblSche) {
		try {
			if (tblSche.getSelectedRow() >= 0) {
				String taskId = tblSche.getValueAt(tblSche.getSelectedRow(), 5).toString();
				String groupId = tblSche.getValueAt(tblSche.getSelectedRow(), 4).toString();
				ScherDialog dialogSche = new ScherDialog(1, taskId, groupId);
				dialogSche.show(true);
			}
		} catch (Exception e) {
			Log.logError("主程序修改调度错误:", e);
		} finally {
		}
	}

	// 编辑调度
	public void editSche() {
		editSche(tblSche);
	}

	// 删除调度
	public boolean delSche() {
		boolean rs = false;
		try {
			int selectedCount = UtilComponent.getTableSelectedCount(tblSche);
			if (selectedCount == 0) {
				ShowMsg.showMsg("请勾选删除的调度");
				return false;
			}

			int msg = ShowMsg.showConfig("确定删除调度: " + UtilComponent.getTableSelectedRowFiled(tblSche, 2) + " ?");
			if (msg == 0) {
				for (int i = 0; i < tblSche.getRowCount(); i++) {
					Boolean selected = Boolean.valueOf(tblSche.getValueAt(i, 0).toString());
					if (selected) {
						String scheId = tblSche.getValueAt(i, 2).toString();
						if (scheId.equals("0")) {
							ShowMsg.showWarn("不允许删除守护调度！");
							continue;
						}
						JobOperater.removeJob(scheId);
						ScherDao.getInstance().delSche(Long.valueOf(scheId));
						rs = true;
					}
				}
				querySche();
			}
		} catch (Exception e) {
			Log.logError("主程序新增调度错误:", e);
		} finally {
		}
		return rs;
	}

	// 启动调度
	public void startSche() {
		try {
			int selectcount = 0;
			for (int i = 0; i < tblSche.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(tblSche.getValueAt(i, 0).toString());
				if (selected) {
					selectcount += 1;
					String scheCode = tblSche.getValueAt(i, 2).toString();
					JobOperater.resumeTrigger(scheCode);
				}
			}
			if (selectcount > 0) {
				JobOperater.refreshSche();
			} else {
				ShowMsg.showMsg("请勾选启动的调度");
			}
		} catch (Exception e) {
			Log.logError("主程序启动调度错误:", e);
		} finally {
		}
	}

	// 停止调度
	public void stopSche() {
		try {
			int selectcount = 0;
			for (int i = 0; i < tblSche.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(tblSche.getValueAt(i, 0).toString());
				if (selected) {
					selectcount += 1;
					String scheCode = tblSche.getValueAt(i, 2).toString();
					JobOperater.pauseTrigger(scheCode);
				}
			}
			if (selectcount > 0) {
				JobOperater.refreshSche();
			} else {
				ShowMsg.showMsg("请勾选停止的调度");
			}
		} catch (Exception e) {
			Log.logError("主程序停止调度错误:", e);
		} finally {
		}
	}

	public JPanel getPnlTab() {
		return pnlTab;
	}

	public JTable getTblSche() {
		return tblSche;
	}

}
