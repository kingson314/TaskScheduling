package com.settings;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.log.Log;
import common.component.DialogTableModel;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.STextArea;
import common.component.STextField;
import common.component.ShowMsg;

import consts.ImageContext;

public class SettingsDialog extends DialogTableModel {
	private static final long serialVersionUID = 1L;
	private SLabel lSetName;
	private SLabel lSetValue;
	private SLabel lId;
	private SLabel lSetMemo;
	private JPanel pnlParam;
	private STextField txtSetName;
	private STextField txtId;
	private STextArea txtaSetMemo;
	private STextArea txtaSetValue;
	private SScrollPane scrlValue;
	private SScrollPane scrlMemo;

	private int Addmod;

	// 获取表格
	private JTable getTable() {
		final JTable table = new SettingsTable("").getJtable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				txtId.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				txtSetName.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
				txtaSetValue.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
				txtaSetMemo.setText(table.getValueAt(table.getSelectedRow(), 3).toString());
				textEnabled(false);
			}
		});
		return table;
	}

	// 刷新表格
	private void refreshTable() {
		super.jTable = getTable();
		super.scrlTable.setViewportView(super.jTable);
	}

	// 构造
	public SettingsDialog() {
		super(0);
		try {
			super.setTitle("配置信息");
			this.setSize(626, 375);
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.Settings));
			super.jTable = getTable();
			super.ini();
			textEnabled(false);
		} catch (Exception e) {
			Log.logError("配置信息对话框构造错误:", e);
		} finally {
		}
	}

	// 新增
	@Override
	protected void add() {
		try {
			textEnabled(true);
			textClear();
			super.add();
			Addmod = 0;
		} catch (Exception e) {
			Log.logError("配置信息对话框新增错误:", e);
		} finally {
		}
	}

	// 修改
	@Override
	protected boolean mod() {
		boolean rs = false;
		try {
			if (txtId.getText().length() < 1) {
				ShowMsg.showMsg("请选择需要修改的记录！");
				return rs;
			}
			if (super.mod()) {
				textEnabled(true);
				Addmod = 1;
				txtId.setEditable(false);
				rs = true;
			}
		} catch (Exception e) {
			Log.logError("配置信息对话框修改错误:", e);
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
			Log.logError("配置信息对话框取消错误:", e);
		} finally {
		}
	}

	// 删除
	@Override
	protected boolean del() {
		try {
			if (txtId.getText().length() < 1) {
				ShowMsg.showMsg("请选择需要删除的记录！");
				return false;
			}
			textEnabled(false);
			if (super.del() == true) {
				SettingsDao.getInstance().delSettings(Integer.valueOf(txtId.getText().trim()));
				refreshTable();
				textClear();
			}
		} catch (Exception e) {
			Log.logError("配置信息对话框删除错误:", e);
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
			Log.logError("配置信息对话框退出错误:", e);
		} finally {
		}
	}

	// 保存
	@Override
	protected void post() {
		try {
			Settings settings = new Settings();
			settings.setSetName(txtSetName.getText());
			settings.setSetValue(txtaSetValue.getText());
			settings.setSetMemo(txtaSetMemo.getText());
			if (Addmod == 0) {
				SettingsDao.getInstance().addSettings(settings);
			} else if (Addmod == 1) {
				settings.setId(Integer.valueOf(txtId.getText()));
				SettingsDao.getInstance().modSettings(settings);
			}
			textEnabled(false);
			super.post();
			refreshTable();
		} catch (Exception e) {
			Log.logError("配置信息对话框保存错误:", e);
		} finally {
		}
	}

	// 获取参数面板
	@Override
	protected JPanel GetParamPanel() {
		pnlParam = new JPanel();
		try {
			pnlParam.setLayout(null);

			lId = new SLabel("配置标志");
			lId.setBounds(50, 20, 100, 20);
			pnlParam.add(lId);
			txtId = new STextField("");
			txtId.setBounds(150, 20, 250, 20);
			txtId.setEditable(false);
			pnlParam.add(txtId);

			lSetName = new SLabel("配置名称");
			lSetName.setBounds(450, 20, 100, 20);
			pnlParam.add(lSetName);
			txtSetName = new STextField("");
			txtSetName.setBounds(500, 20, 250, 20);
			pnlParam.add(txtSetName);

			lSetValue = new SLabel("配置值");
			lSetValue.setBounds(50, 50, 100, 20);
			pnlParam.add(lSetValue);
			{
				txtaSetValue = new STextArea();
				scrlValue = new SScrollPane(txtaSetValue);
				pnlParam.add(scrlValue, "bottom");
				scrlValue.setBounds(150, 50, 250, 38);
			}

			lSetMemo = new SLabel("配置说明");
			lSetMemo.setBounds(450, 50, 100, 20);
			pnlParam.add(lSetMemo);
			{
				txtaSetMemo = new STextArea();
				scrlMemo = new SScrollPane(txtaSetMemo);
				pnlParam.add(scrlMemo, "bottom");
				scrlMemo.setBounds(500, 50, 250, 38);
			}
		} catch (Exception e) {
			Log.logError("配置信息对话框初始化jPanelParam错误:", e);
		} finally {
		}
		return pnlParam;
	}

	// 状态控制
	private void textEnabled(boolean flag) {
		txtSetName.setEditable(flag);
		txtaSetValue.setEditable(flag);
		txtaSetMemo.setEditable(flag);
	}

	// 清空txt
	private void textClear() {
		txtSetName.setText("");
		txtId.setText("");
		txtaSetValue.setText("");
		txtaSetMemo.setText("");
	}
}
