package com.task.TableCheck;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import com.app.AppFun;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.SBorder;
import common.component.SButton;
import common.component.SComboBox;
import common.component.SLabel;
import common.component.SRadioButton;
import common.component.SScrollPane;
import common.component.STextArea;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import consts.Const;

public class Panel implements ITaskPanel {
	private JPanel pnlMain;
	private SLabel lDbconn;
	private SLabel lSql;
	private JPanel pnl1;
	private STextField txtDbName;
	private SButton btnDbName;
	private SLabel lSqlrule;
	private SScrollPane scrl1;
	private STextArea txtaSQL;
	private SScrollPane scrl2;
	private STextArea txtaSqlRule;
	private STextArea txta2;
	private STextField txtTcNumLimited;
	private SLabel l;
	private SComboBox cmbCompareType;
	private SRadioButton chkTcIfNumWarn;
	private STextArea txtaTNumWarning;
	private SScrollPane scrl4;
	private JPanel pnl2;
	private SRadioButton rbtnTcIfNumLimitedWarn;
	private STextArea txtaTcNumLimitedWarning;
	private SScrollPane scrl3;

	// public static void main(String[] args) {
	// Panel inis = new Panel();
	//
	// inis.add(inis.getPanel());
	// inis.setBounds(0, 0, 623, 606);
	// int w = (Toolkit.getDefaultToolkit().getScreenSize().width - inis
	// .getWidth()) / 2;
	// int h = (Toolkit.getDefaultToolkit().getScreenSize().height - inis
	// .getHeight()) / 2;
	// inis.setLocation(w, h);
	// inis.setVisible(true);
	// }

	public Panel() {
		// this.setSize(615, 480);
		// this.add(getPanel());
	}

	public JPanel getPanel() {
		pnlMain = new JPanel();
		try {
			pnlMain.setLayout(null);
			pnlMain.setBounds(-13, 13, 626, 43);
			pnlMain.setPreferredSize(new java.awt.Dimension(668, 482));
			pnlMain.setSize(429, 95);
			{
				lDbconn = new SLabel("\u6570\u636e\u6e90\u8fde\u63a5");
				pnlMain.add(lDbconn);
				lDbconn.setBounds(21, 24, 70, 14);
			}
			{
				lSql = new SLabel("\u67e5\u8be2SQL");
				pnlMain.add(lSql);
				lSql.setBounds(21, 45, 70, 14);
			}
			{
				txtDbName = new STextField();
				pnlMain.add(txtDbName);
				txtDbName.setBounds(103, 21, 429, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							dbConnbtn();
					}
				});
			}
			{
				btnDbName = new SButton("..");
				pnlMain.add(btnDbName);
				btnDbName.setBounds(538, 21, 22, 21);
				btnDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dbConnbtn();
					}
				});
			}
			{
				lSqlrule = new SLabel("\u53c2\u6570\u6620\u5c04");
				pnlMain.add(lSqlrule);
				lSqlrule.setBounds(21, 151, 67, 14);
			}
			{
				txtaSQL = new STextArea();
				scrl1 = new SScrollPane(txtaSQL);
				pnlMain.add(scrl1, "bottom");
				scrl1.setBounds(103, 49, 429, 95);

			}
			{
				txtaSqlRule = new STextArea();
				scrl2 = new SScrollPane(txtaSqlRule);
				pnlMain.add(scrl2, "bottom");
				scrl2.setBounds(103, 151, 429, 95);
			}
			{
				pnlMain.add(getJPanel1());
				pnlMain.add(getJPanel2());
			}
		} catch (Exception e) {
			Log.logError("数据表检查面板构造错误:", e);
			return pnlMain;
		} finally {
		}
		return pnlMain;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getTcDbName())) {
			ShowMsg.showWarn("数据源连接不能为空");
			return false;
		}
		if ("".equals(bean.getTcSQL())) {
			ShowMsg.showWarn("查询SQL不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setTcIfNumLimitedWarn(rbtnTcIfNumLimitedWarn.isSelected());
			bean.setTcIfNumWarn(chkTcIfNumWarn.isSelected());
			try {
				bean.setTcnumLimited(Long.valueOf(txtTcNumLimited.getText() == null || txtTcNumLimited.getText().trim().equals("") ? "0" : txtTcNumLimited.getText()));
			} catch (Exception e) {
				ShowMsg.showMsg("请输入数字！");
				return false;
			}
			bean.setTcDbName(txtDbName.getText() == null ? "" : txtDbName.getText());
			bean.setTcSQL(txtaSQL.getText() == null ? "" : txtaSQL.getText());
			bean.setTcParamsRule(txtaSqlRule.getText() == null ? "" : txtaSqlRule.getText());
			bean.setTcNumLimitedWarning(txtaTcNumLimitedWarning.getText() == null ? "" : txtaTcNumLimitedWarning.getText());
			bean.setTcNumWarning(txtaTNumWarning.getText() == null ? "" : txtaTNumWarning.getText());
			bean.setCompareType(cmbCompareType.getSelectedItem().toString());
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			// Log.logError("数据表检查面板赋值任务对象错误:", e);
			ShowMsg.showWarn(e.getMessage());
			return false;
		} finally {
		}
		return true;
	}

	// 填充面板

	// 填充面板
	public void fillComp(ITask task) {
		try {
			if (task.getJsonStr().equals(""))
				return;
			Bean bean = (Bean) UtilJson.getJsonBean(task.getJsonStr(), Bean.class);
			if (bean.getTcIfNumLimitedWarn() == null)
				rbtnTcIfNumLimitedWarn.setSelected(false);
			else
				rbtnTcIfNumLimitedWarn.setSelected(bean.getTcIfNumLimitedWarn() == true ? true : false);
			if (bean.getTcIfNumWarn() == null)
				chkTcIfNumWarn.setSelected(false);
			else
				chkTcIfNumWarn.setSelected(bean.getTcIfNumWarn() == true ? true : false);
			txtTcNumLimited.setText(String.valueOf(bean.getTcnumLimited()));
			txtDbName.setText(bean.getTcDbName());
			txtaSQL.setText(bean.getTcSQL());
			txtaSqlRule.setText(bean.getTcParamsRule());
			txtaTcNumLimitedWarning.setText(bean.getTcNumLimitedWarning());
			txtaTNumWarning.setText(bean.getTcNumWarning());
			if (rbtnTcIfNumLimitedWarn.isSelected()) {
				txtTcNumLimited.setEnabled(true);
				txtaTcNumLimitedWarning.setEnabled(true);
			} else {
				txtTcNumLimited.setEnabled(false);
				txtaTcNumLimitedWarning.setEnabled(false);
			}
			if (chkTcIfNumWarn.isSelected()) {
				txtaTNumWarning.setEnabled(true);
			} else {
				txtaTNumWarning.setEnabled(false);
			}
			cmbCompareType.setSelectedItem(bean.getCompareType() == null ? "小于" : bean.getCompareType());
		} catch (Exception e) {
			Log.logError("数据表检查面板填充控件错误:", e);
		} finally {
		}
	}

	private void dbConnbtn() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("数据表检查面板获取数据源链接错误:", e);
		} finally {
		}
	}

	private JPanel getJPanel1() {
		if (pnl1 == null) {
			pnl1 = new JPanel();
			pnl1.setBounds(14, 323, 588, 58);
			pnl1.setLayout(null);
			pnl1.setBorder(SBorder.getTitledBorder());
			pnl1.add(getSScrollPane3(), "bottom");
			pnl1.add(getTextFieldtcNumLimited());
			pnl1.add(getSCheckBox_tcIfNumLimitedWarn());
			pnl1.add(getCmbCompareType());
			pnl1.add(getL());
		}
		return pnl1;
	}

	private SScrollPane getSScrollPane3() {
		if (scrl3 == null) {
			scrl3 = new SScrollPane(getTextArea2());
			scrl3.setBounds(291, 5, 228, 48);
			scrl3.setBorder(SBorder.getTitledBorder());
			scrl3.setViewportView(getTextArea_tcNumLimitedWarning());
		}
		return scrl3;
	}

	private STextArea getTextArea2() {
		if (txta2 == null) {
			txta2 = new STextArea();
		}
		return txta2;
	}

	private STextArea getTextArea_tcNumLimitedWarning() {
		if (txtaTcNumLimitedWarning == null) {
			txtaTcNumLimitedWarning = new STextArea();
			txtaTcNumLimitedWarning.setEnabled(false);
			txtaTcNumLimitedWarning.setLineWrap(true);
			txtaTcNumLimitedWarning.setMinimumSize(new java.awt.Dimension(3, 3));
			txtaTcNumLimitedWarning.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

		}
		return txtaTcNumLimitedWarning;
	}

	private STextField getTextFieldtcNumLimited() {
		if (txtTcNumLimited == null) {
			txtTcNumLimited = new STextField();
			txtTcNumLimited.setEnabled(false);
			txtTcNumLimited.setBounds(126, 16, 96, 21);
		}
		return txtTcNumLimited;
	}

	private SRadioButton getSCheckBox_tcIfNumLimitedWarn() {
		if (rbtnTcIfNumLimitedWarn == null) {
			rbtnTcIfNumLimitedWarn = new SRadioButton("");
			rbtnTcIfNumLimitedWarn.setSelected(false);
			rbtnTcIfNumLimitedWarn.setBounds(21, 19, 18, 18);
			rbtnTcIfNumLimitedWarn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (rbtnTcIfNumLimitedWarn.isSelected()) {
						txtTcNumLimited.setEnabled(true);
						txtaTcNumLimitedWarning.setEnabled(true);
						chkTcIfNumWarn.setSelected(false);
						txtaTNumWarning.setEnabled(false);
					}
				}
			});
		}
		return rbtnTcIfNumLimitedWarn;
	}

	private JPanel getJPanel2() {
		if (pnl2 == null) {
			pnl2 = new JPanel();
			pnl2.setBounds(14, 253, 588, 58);
			pnl2.setLayout(null);
			pnl2.setBorder(SBorder.getTitledBorder());
			pnl2.add(getSCheckBox_tcIfNumWarn());
			pnl2.add(getSScrollPane4(), "bottom");
		}
		return pnl2;
	}

	private SScrollPane getSScrollPane4() {
		if (scrl4 == null) {
			scrl4 = new SScrollPane(getTextArea_tcNumWarning());
			scrl4.setBorder(SBorder.getTitledBorder());
			scrl4.setBounds(145, 5, 374, 48);
		}
		return scrl4;
	}

	private STextArea getTextArea_tcNumWarning() {
		if (txtaTNumWarning == null) {
			txtaTNumWarning = new STextArea();
			txtaTNumWarning.setLineWrap(true);
			txtaTNumWarning.setMinimumSize(new java.awt.Dimension(3, 3));
			txtaTNumWarning.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
			txtaTNumWarning.setEnabled(true);
		}
		return txtaTNumWarning;
	}

	private SRadioButton getSCheckBox_tcIfNumWarn() {
		if (chkTcIfNumWarn == null) {
			chkTcIfNumWarn = new SRadioButton("\u63d0\u793a\u6570\u636e\u8868\u8bb0\u5f55\u6570");
			chkTcIfNumWarn.setBounds(21, 23, 124, 22);
			chkTcIfNumWarn.setSelected(true);
			chkTcIfNumWarn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (chkTcIfNumWarn.isSelected()) {
						txtaTNumWarning.setEnabled(true);
						rbtnTcIfNumLimitedWarn.setSelected(false);
						txtTcNumLimited.setEnabled(false);
						txtaTcNumLimitedWarning.setEnabled(false);
					}
				}
			});
		}
		return chkTcIfNumWarn;
	}

	private SComboBox getCmbCompareType() {
		if (cmbCompareType == null) {
			cmbCompareType = new SComboBox(Const.CompareType);
			cmbCompareType.setBounds(45, 16, 75, 21);
		}
		return cmbCompareType;
	}

	private SLabel getL() {
		if (l == null) {
			l = new SLabel("\u6761\u8bb0\u5f55\u63d0\u793a");
			l.setBounds(226, 18, 62, 16);
		}
		return l;
	}

}
