package com.monitor;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import app.AppCon;

import com.log.Log;
import common.component.SBorder;
import common.component.SButton;
import common.component.SDialog;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.SSplitPane;
import common.component.STextField;
import common.component.ShowMsg;

import consts.Const;
import consts.ImageContext;

public class MonitorGroupDialog extends SDialog {
	private static final long serialVersionUID = 1L;
	private SSplitPane splMg;
	private SSplitPane splTool;
	private SScrollPane scrlMg;
	private SButton btnMod;
	private SButton btnExit;
	private SButton btnDel;
	private JPanel pnlIn;
	private SButton btnDelDetail;
	private JSeparator jSeparator1;
	private SButton btnAddDetail;
	private JToolBar tbM;
	private SButton btnCancle;
	private SButton btnPost;
	private SButton btnAdd;
	private JPanel pnlTool;
	private JPanel pnlParam;
	private SSplitPane splInm;
	private STextField txtMgMemo;
	private STextField txtMgName;
	private STextField txtMgCode;
	private SLabel lMgMemo;
	private SLabel lMgName;
	private SLabel lMgCode;
	private SSplitPane splM;
	private SSplitPane splMain;

	private JTable tblMg;
	private SScrollPane scrlOut;
	private SScrollPane scrlIn;
	private int Addmod;
	private MonitorTable tblIn;
	private MonitorTable tblOut;

	// public static void main(String[] args) {
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// MonitorGroupDialog inst = new MonitorGroupDialog();
	// inst.setVisible(true);
	// }
	// }
	// 获取监控组表格
	private JTable getMgtable() {
		JTable mgTable = new MonitorGroupTable("").getJtable();
		mgTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				mClicked(evt);
			}
		});
		return mgTable;
	}

	// 构造
	public MonitorGroupDialog() {
		super();
		try {
			super.setTitle("监控组信息");
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.MonitorGroup));
			this.tblMg = getMgtable();
			this.tblIn = new MonitorTable("监控组面板", "select * from " + AppCon.TN_Monitor + "  where 1=2");
			this.tblOut = new MonitorTable("监控组面板", "select * from  " + AppCon.TN_Monitor + "   where 1=2");
			init();

			enableText(false);
		} catch (Exception e) {
			Log.logError("监控组对话框构造错误:", e);
		} finally {
		}
	}

	// 点击事件
	public void mClicked(MouseEvent evt) {
		txtMgCode.setText(this.tblMg.getValueAt(this.tblMg.getSelectedRow(), 0).toString());
		txtMgName.setText(this.tblMg.getValueAt(this.tblMg.getSelectedRow(), 1).toString());
		txtMgMemo.setText(this.tblMg.getValueAt(this.tblMg.getSelectedRow(), 2).toString());
		refreshMinotorTable(txtMgCode.getText());
	}

	// 刷新监控员表
	private void refreshMinotorTable(String mgCode) {
		tblIn = new MonitorTable("监控组面板", "select a.* from  " + AppCon.TN_Monitor + "    a , " + AppCon.TN_MonitorGroup + "    b, " + "  " + AppCon.TN_MonitorGroupDetail
				+ "    c where a.mcode=c.mcode" + " and b.mgCode=c.mgCode and b.mgCode='" + mgCode + "'");
		scrlIn.setViewportView(tblIn.getJtable());
		String outSql = "select * from  " + AppCon.TN_Monitor + "  where mcode not in(select mcode from  " + AppCon.TN_MonitorGroupDetail + "  where mgCode ='" + mgCode + "')";
		tblOut = new MonitorTable("监控组面板", outSql);
		scrlOut.setViewportView(tblOut.getJtable());
	}

	// 初始化
	protected void init() {
		try {
			initGUI();
			btnAdd.setEnabled(true);
			btnMod.setEnabled(true);
			btnDel.setEnabled(true);
			btnPost.setEnabled(false);
			btnCancle.setEnabled(false);
			btnExit.setEnabled(true);
			tblMg.setEnabled(true);
		} catch (Exception e) {
			Log.logError("监控组对话框初始化错误:", e);
		} finally {
		}
	}

	// 初始化界面
	private void initGUI() {
		try {
			setModal(true);
			setAlwaysOnTop(false);
			GridLayout thisLayout = new GridLayout(1, 2);
			thisLayout.setColumns(2);
			thisLayout.setHgap(5);
			thisLayout.setVgap(5);
			getContentPane().setLayout(thisLayout);
			this.setBounds(0, 0, 1123, 586);
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			{
				splMain = new SSplitPane(1, 0.65, false);
				splMain.setEnabled(false);
				getContentPane().add(splMain);
				{
					splM = new SSplitPane(0, 0.52, false);
					splMain.add(splM, SSplitPane.RIGHT);
					splM.setPreferredSize(new java.awt.Dimension(612, 557));
				}
				{
					splMg = new SSplitPane(0, 0.7, false);
					splMg.setEnabled(false);
					splMain.add(splMg, SSplitPane.LEFT);
					splMg.setPreferredSize(new java.awt.Dimension(462, 557));
					scrlMg = new SScrollPane(tblMg);
					splMg.add(scrlMg, SSplitPane.TOP);
					scrlMg.setPreferredSize(new java.awt.Dimension(460, 321));

					{
						splTool = new SSplitPane(0, 0.65, false);
						splTool.setEnabled(false);
						splMg.add(splTool, SSplitPane.BOTTOM);
						{
							pnlParam = new JPanel();
							splTool.add(pnlParam, SSplitPane.TOP);
							pnlParam.setLayout(null);
							pnlParam.setPreferredSize(new java.awt.Dimension(458, 134));
							{
								lMgCode = new SLabel("\u76d1\u63a7\u7ec4\u6807\u5fd7");
								pnlParam.add(lMgCode);
								lMgCode.setBounds(98, 19, 75, 18);
							}
							{
								lMgName = new SLabel("\u76d1\u63a7\u7ec4\u540d\u79f0");
								pnlParam.add(lMgName);
								lMgName.setBounds(435, 19, 75, 18);
							}
							{
								lMgMemo = new SLabel("\u76d1\u63a7\u7ec4\u5907\u6ce8");
								pnlParam.add(lMgMemo);
								lMgMemo.setBounds(98, 60, 76, 18);
							}
							{
								txtMgCode = new STextField();
								pnlParam.add(txtMgCode);
								txtMgCode.setBounds(177, 16, 150, 21);
								txtMgCode.setFont(Const.tfont);
							}
							{
								txtMgName = new STextField();
								pnlParam.add(txtMgName);
								txtMgName.setBounds(512, 16, 150, 21);
								txtMgName.setFont(Const.tfont);
							}
							{
								txtMgMemo = new STextField();
								pnlParam.add(txtMgMemo);
								txtMgMemo.setBounds(177, 57, 150, 21);
								txtMgMemo.setFont(Const.tfont);
							}
						}
						{
							pnlTool = new JPanel();
							pnlTool.setLayout(null);
							splTool.add(pnlTool, SSplitPane.BOTTOM);
							pnlTool.setPreferredSize(new java.awt.Dimension(458, 151));
							{
								btnAdd = new SButton("\u6dfb  \u52a0", ImageContext.Add);
								btnAdd.setIcon(new ImageIcon());
								pnlTool.add(btnAdd);
								btnAdd.setBounds(43, 10, 100, 25);
								btnAdd.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										add();
									}
								});
							}
							{
								btnMod = new SButton("\u4fee  \u6539", ImageContext.Mod);
								pnlTool.add(btnMod);
								btnMod.setBounds(149, 10, 100, 25);
								btnMod.setFont(Const.tfont);
								btnMod.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										mod();
									}
								});
							}
							{
								btnPost = new SButton("\u63d0  \u4ea4", ImageContext.Post);
								pnlTool.add(btnPost);
								btnPost.setBounds(255, 10, 100, 25);
								btnPost.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										post();
									}
								});
							}
							{
								btnCancle = new SButton("\u53d6  \u6d88", ImageContext.Cancel);
								pnlTool.add(btnCancle);
								btnCancle.setBounds(361, 10, 100, 25);
								btnCancle.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										cancle();
									}
								});
							}
							{
								btnDel = new SButton("\u5220  \u9664", ImageContext.Del);
								pnlTool.add(btnDel);
								btnDel.setBounds(467, 10, 100, 25);
								btnDel.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										del();
									}
								});
							}
							{
								btnExit = new SButton("\u9000  \u51fa", ImageContext.Exit);
								pnlTool.add(btnExit);
								btnExit.setBounds(589, 10, 100, 25);
								btnExit.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										exit();
									}
								});
							}
						}

					}
				}
				{
					splInm = new SSplitPane(0, 0.87, false);
					splM.add(splInm, SSplitPane.TOP);
					splInm.setPreferredSize(new java.awt.Dimension(642, 220));
					splInm.setEnabled(false);
					{
						tbM = new JToolBar();
						tbM.setEnabled(false);
						splInm.add(tbM, SSplitPane.BOTTOM);
						splInm.add(getJPanelIn(), SSplitPane.LEFT);
						tbM.setPreferredSize(new java.awt.Dimension(640, 83));
						{
							btnAddDetail = new SButton("\u6dfb \u52a0", ImageContext.Add);
							tbM.add(btnAddDetail);
							btnAddDetail.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									addMonitorDetail();

								}
							});
						}
						{
							jSeparator1 = new JSeparator();
							tbM.add(jSeparator1);
							jSeparator1.setMaximumSize(new java.awt.Dimension(20, 32767));
						}
						{
							btnDelDetail = new SButton("\u5220 \u9664", ImageContext.Del);
							tbM.add(btnDelDetail);
							btnDelDetail.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									delMonitorDetail();

								}
							});
						}
					}
					{
						scrlOut = new SScrollPane(this.tblOut.getJtable());
						splM.add(scrlOut, SSplitPane.BOTTOM);
						scrlOut.setPreferredSize(new java.awt.Dimension(642, 295));
					}
				}
			}
		} catch (Exception e) {
			Log.logError("监控组对话框初始化界面错误:", e);
		} finally {
		}
	}

	// 删除监控组监控员
	private void delMonitorDetail() {
		MonitorGroupDetail mgd = new MonitorGroupDetail();
		mgd.setMgCode(tblMg.getValueAt(tblMg.getSelectedRow(), 0).toString());
		for (int i = 0; i < this.tblIn.getJtable().getRowCount(); i++) {
			Boolean selected = Boolean.valueOf(this.tblIn.getJtable().getValueAt(i, 0).toString());
			if (selected) {
				mgd.setMCode(this.tblIn.getJtable().getValueAt(i, 1).toString());
				MonitorGroupDetailDao.getInstance().delMonitorGroupDetail(mgd);
			}
		}
		refreshMinotorTable(mgd.getMgCode());
	}

	// 添加监控组监控员
	private void addMonitorDetail() {
		MonitorGroupDetail mgd = new MonitorGroupDetail();
		mgd.setMgCode(tblMg.getValueAt(tblMg.getSelectedRow(), 0).toString());
		for (int i = 0; i < this.tblOut.getJtable().getRowCount(); i++) {
			Boolean selected = Boolean.valueOf(this.tblOut.getJtable().getValueAt(i, 0).toString());
			if (selected) {
				mgd.setMCode(this.tblOut.getJtable().getValueAt(i, 1).toString());
				MonitorGroupDetailDao.getInstance().addMonitorGroupDetail(mgd);
			}

		}
		refreshMinotorTable(mgd.getMgCode());

	}

	// 添加监控组
	protected void add() {
		try {
			btnAdd.setEnabled(false);
			btnMod.setEnabled(false);
			btnDel.setEnabled(false);
			btnPost.setEnabled(true);
			btnCancle.setEnabled(true);
			btnExit.setEnabled(false);
			tblMg.setEnabled(false);
			textClear();
			enableText(true);
			this.Addmod = 0;
		} catch (Exception e) {
			Log.logError("监控组对话框新增事件错误:", e);
		} finally {
		}
	}

	// 修改监控组
	protected boolean mod() {
		boolean rs = true;
		try {
			if (tblMg.getSelectedRowCount() <= 0 || txtMgCode.getText().length() < 1) {
				ShowMsg.showMsg("请选择需要修改的记录！");
				return false;
			}
			btnAdd.setEnabled(false);
			btnMod.setEnabled(false);
			btnDel.setEnabled(false);
			btnPost.setEnabled(true);
			btnCancle.setEnabled(true);
			btnExit.setEnabled(false);
			tblMg.setEnabled(false);
			this.Addmod = 1;
			enableText(true);
			txtMgCode.setEnabled(false);
		} catch (Exception e) {
			Log.logError("监控组对话框修改错误:", e);
			return false;
		} finally {
		}
		return rs;
	}

	// 取消
	protected void cancle() {
		btnAdd.setEnabled(true);
		btnMod.setEnabled(true);
		btnDel.setEnabled(true);
		btnPost.setEnabled(false);
		btnCancle.setEnabled(false);
		btnExit.setEnabled(true);
		tblMg.setEnabled(true);
		enableText(false);
		textClear();
	}

	// 删除监控组
	protected boolean del() {
		try {
			if (txtMgCode.getText().length() < 1) {
				ShowMsg.showMsg("请选择需要删除的记录！");
				return false;
			}
			int i = ShowMsg.showConfig(" 确定删除该记录? ");
			if (i != 0) {
				return false;
			}
			btnAdd.setEnabled(true);
			btnMod.setEnabled(true);
			btnDel.setEnabled(true);
			btnPost.setEnabled(false);
			btnCancle.setEnabled(false);
			btnExit.setEnabled(true);
			tblMg.setEnabled(true);
			MonitorGroupDao.getInstance().delMonitorGroup(txtMgCode.getText().trim());
			MonitorGroupDetailDao.getInstance().delMonitorGroupDetail(txtMgCode.getText().trim());
			tblMg = getMgtable();
			scrlMg.setViewportView(tblMg);
		} catch (Exception e) {
			Log.logError("监控组对话框删除错误:", e);
			return false;
		} finally {
		}
		return true;
	}

	// 保存监控组
	protected void post() {

		MonitorGroup mg = getMg();
		if (this.Addmod == 0) {
			if (txtMgCode.getText().length() < 1) {
				ShowMsg.showMsg("请输入监控组标志！");
				return;
			}

			if (MonitorGroupDao.getInstance().ifExistMonitorGroup(mg.getMgCode())) {
				ShowMsg.showWarn("监控组标志已存在！");
				return;
			}
			MonitorGroupDao.getInstance().addMonitorGroup(mg);
		} else if (this.Addmod == 1) {
			MonitorGroupDao.getInstance().modMonitorGroup(mg);
		}
		tblMg = getMgtable();
		scrlMg.setViewportView(tblMg);
		btnAdd.setEnabled(true);
		btnMod.setEnabled(true);
		btnDel.setEnabled(true);
		btnPost.setEnabled(false);
		btnCancle.setEnabled(false);
		btnExit.setEnabled(true);
		tblMg.setEnabled(true);
		enableText(false);
	}

	// 清空txt
	private void textClear() {
		txtMgCode.setText("");
		txtMgName.setText("");
		txtMgMemo.setText("");
	}

	// 状态控制
	private void enableText(boolean enabled) {
		txtMgCode.setEnabled(enabled);
		txtMgName.setEnabled(enabled);
		txtMgMemo.setEnabled(enabled);
	}

	private MonitorGroup getMg() {
		MonitorGroup mg = new MonitorGroup();
		mg.setMgCode(txtMgCode.getText() == null ? "" : txtMgCode.getText().trim());
		mg.setMgName(txtMgName.getText() == null ? "" : txtMgName.getText().trim());
		mg.setMgMemo(txtMgMemo.getText() == null ? "" : txtMgMemo.getText().trim());
		return mg;
	}

	// 退出
	protected void exit() {
		this.dispose();
	}

	private JPanel getJPanelIn() {
		if (pnlIn == null) {
			pnlIn = new JPanel();
			GridLayout jPanelInLayout = new GridLayout(1, 1);
			jPanelInLayout.setColumns(1);
			jPanelInLayout.setHgap(5);
			jPanelInLayout.setVgap(5);
			pnlIn.setLayout(jPanelInLayout);
			pnlIn.setBorder(SBorder.getTitledBorder());
			{
				scrlIn = new SScrollPane(this.tblIn.getJtable());
				pnlIn.add(scrlIn);
			}
		}
		return pnlIn;
	}

}
