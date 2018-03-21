package com.monitor;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTable;
import app.AppCon;

import com.log.Log;
import common.component.DialogTableModel;
import common.component.SLabel;
import common.component.STextField;
import common.component.ShowMsg;

import consts.ImageContext;

public class MonitorDialog extends DialogTableModel {
	private static final long serialVersionUID = 1L;
	private SLabel lMName;
	private SLabel lMTel;
	private SLabel lMMailAddreass;
	private STextField txtMName;
	private STextField txtMTel;
	private STextField txtMMailAddress;
	private JPanel pnlParam;
	private int Addmod;
	private SLabel lMCode;
	private STextField txtMCode;

	// public static void main(String[] args) {
	//
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// MonitorDialog inst = new MonitorDialog();
	// inst.setVisible(true);
	// }
	// });
	// }

	// 构造
	public MonitorDialog() {
		super(0);
		try {
			super.setTitle("监控员信息");
			this.setSize(626, 375);
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.Monitor));
			super.jTable = getTable("监控员面板", "select * from " + AppCon.TN_Monitor);
			super.ini();
			textEnabled(false);

		} catch (Exception e) {
			Log.logError("监控员信息对话框构造错误:", e);
		} finally {
		}
	}

	// 获取监控员表格
	private JTable getTable(String type, String sql) {
		final JTable table = new MonitorTable(type, sql).getJtable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				txtMCode.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
				txtMName.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				txtMTel.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
				txtMMailAddress.setText(table.getValueAt(table.getSelectedRow(), 4).toString());

				textEnabled(false);
			}
		});
		return table;
	}

	// 刷新表格
	private void refreshTable() {
		super.jTable = getTable("监控员面板", "select * from  " + AppCon.TN_Monitor);
		super.scrlTable.setViewportView(super.jTable);
	}

	// 新增监控员
	@Override
	protected void add() {
		try {
			textEnabled(true);
			textClear();
			super.add();
			Addmod = 0;
		} catch (Exception e) {
			Log.logError("监控员信息对话框新增错误:", e);

		} finally {
		}
	}

	// 修改监控员
	@Override
	protected boolean mod() {
		boolean rs = false;
		try {
			if (txtMCode.getText().length() < 1) {
				ShowMsg.showMsg("请选择需要修改的记录！");
				return rs;
			}
			if (super.mod()) {
				textEnabled(true);
				Addmod = 1;
				txtMCode.setEnabled(false);
				rs = true;

			}
		} catch (Exception e) {
			Log.logError("监控员信息对话框修改错误:", e);
			return false;
		} finally {
		}
		return rs;
	}

	// 取消
	@Override
	protected void cancle() {
		try {
			textEnabled(false);
			super.cancle();
			textClear();
		} catch (Exception e) {
			Log.logError("监控员信息对话框取消错误:", e);
		} finally {
		}
	}

	// 删除监控员
	@Override
	protected boolean del() {
		try {
			if (txtMCode.getText().length() < 1) {
				ShowMsg.showMsg("请选择需要删除的记录！");
				return false;
			}
			textEnabled(false);
			if (super.del() == true) {
				MonitorDao.getInstance().delMonitor(super.jTable.getValueAt(super.jTable.getSelectedRow(), 1).toString());
				refreshTable();
				textClear();
			}
		} catch (Exception e) {
			Log.logError("监控员信息对话框删除错误:", e);
			return false;
		} finally {
		}
		return true;
	}

	// 退出
	@Override
	protected void exit() {
		try {
			super.exit();
		} catch (Exception e) {
			Log.logError("监控员信息对话框退出错误:", e);
		} finally {
		}
	}

	// 保存监控员
	@Override
	protected void post() {
		try {
			if (txtMCode.getText().length() < 1) {
				ShowMsg.showMsg("请输入监控员标志！");
				return;
			}

			Monitor monitor = new Monitor();
			monitor.setMCode(txtMCode.getText() == null ? "" : txtMCode.getText());
			monitor.setMName(txtMName.getText() == null ? "" : txtMName.getText());
			monitor.setMTel(txtMTel.getText() == null ? "" : txtMTel.getText());
			monitor.setMMailAddress(txtMMailAddress.getText() == null ? "" : txtMMailAddress.getText());

			if (Addmod == 0) {
				if (MonitorDao.getInstance().ifExistMonitor(monitor.getMCode())) {
					ShowMsg.showWarn("监控员标志已存在！");
					return;
				}
				MonitorDao.getInstance().addMonitor(monitor);
			} else if (Addmod == 1) {
				MonitorDao.getInstance().modMonitor(monitor);
			}
			textEnabled(false);
			super.post();
			refreshTable();
		} catch (Exception e) {
			Log.logError("监控员信息对话框保存错误:", e);
		} finally {
		}
	}

	// 获取参数面板
	@Override
	protected JPanel GetParamPanel() {
		pnlParam = new JPanel();
		pnlParam.setLayout(null);
		try {
			lMCode = new SLabel("监控员标志");
			lMCode.setBounds(50, 20, 100, 20);
			pnlParam.add(lMCode);

			txtMCode = new STextField("");
			txtMCode.setBounds(150, 20, 150, 20);
			pnlParam.add(txtMCode);

			lMName = new SLabel("监控员名称");
			lMName.setBounds(350, 20, 100, 20);
			pnlParam.add(lMName);

			txtMName = new STextField("");
			txtMName.setBounds(450, 20, 150, 20);
			pnlParam.add(txtMName);

			lMTel = new SLabel("监控员电话");
			lMTel.setBounds(50, 50, 100, 20);
			pnlParam.add(lMTel);

			txtMTel = new STextField("");
			txtMTel.setBounds(150, 50, 150, 20);
			pnlParam.add(txtMTel);

			lMMailAddreass = new SLabel("监控员邮箱地址");
			lMMailAddreass.setBounds(350, 50, 100, 20);
			pnlParam.add(lMMailAddreass);

			txtMMailAddress = new STextField("");
			txtMMailAddress.setBounds(450, 50, 150, 20);
			pnlParam.add(txtMMailAddress);
		} catch (Exception e) {
			Log.logError("监控员信息对话框初始化界面获取jPanelParam错误:", e);
		} finally {
		}
		return pnlParam;
	}

	// 状态控制
	private void textEnabled(boolean flag) {
		txtMCode.setEnabled(flag);
		txtMName.setEnabled(flag);
		txtMMailAddress.setEnabled(flag);
		txtMTel.setEnabled(flag);
	}

	// 清空txt
	private void textClear() {
		txtMCode.setText("");
		txtMName.setText("");
		txtMMailAddress.setText("");
		txtMTel.setText("");
	}

}
