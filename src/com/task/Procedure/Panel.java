package com.task.Procedure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import com.app.AppFun;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.SButton;
import common.component.SCheckBox;
import common.component.SComboBox;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.STextArea;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlProcedure;
	private SLabel lDbName;
	private SCheckBox chkParam;
	private SLabel lWarn;
	private STextField txtWarn;
	private STextField txtCompareValue;
	private SComboBox cmbCompareType;
	private STextField txtCompareParam;
	private STextField txtDbName;
	private SButton btnDbName;
	private SLabel lParamRule;
	private STextField txtPName;
	private SLabel lpName;
	private SScrollPane scrlpParamRule;
	private STextArea txtrParamRule;
	private SButton btnParamRule;

	public Panel() {
	}

	public JPanel getPanel() {
		pnlProcedure = new JPanel();
		try {
			pnlProcedure.setLayout(null);
			pnlProcedure.setBounds(-13, 13, 626, 43);
			pnlProcedure.setPreferredSize(new java.awt.Dimension(668, 482));
			pnlProcedure.setSize(429, 95);
			{
				lDbName = new SLabel("\u6570\u636e\u6e90\u8fde\u63a5");
				pnlProcedure.add(getBtnParamRule());
				pnlProcedure.add(lDbName);
				lDbName.setBounds(35, 111, 77, 14);
			}
			{
				txtDbName = new STextField();
				pnlProcedure.add(txtDbName);
				txtDbName.setBounds(112, 104, 429, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnDbName();
					}
				});
			}
			{
				btnDbName = new SButton("..");
				pnlProcedure.add(btnDbName);
				btnDbName.setBounds(549, 104, 22, 21);
				btnDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDbName();
					}
				});
			}
			{
				lParamRule = new SLabel("\u53c2\u6570\u6620\u5c04");
				pnlProcedure.add(lParamRule);
				lParamRule.setBounds(35, 141, 67, 14);
			}
			{
				txtPName = new STextField();
				pnlProcedure.add(txtPName);
				txtPName.setBounds(112, 68, 430, 22);

			}
			{
				lpName = new SLabel("\u5b58\u50a8\u8fc7\u7a0b\u540d\u79f0");
				pnlProcedure.add(lpName);
				lpName.setBounds(35, 76, 77, 14);
			}
			{

				pnlProcedure.add(getChkParam());
				pnlProcedure.add(getTxtCompareParam());
				pnlProcedure.add(getCmbCompareType());
				pnlProcedure.add(getTxtCompareValue());
				pnlProcedure.add(getTxtWarn());
				pnlProcedure.add(getLWarn());

				{
					txtrParamRule = new STextArea();
				}
				scrlpParamRule = new SScrollPane(txtrParamRule);
				pnlProcedure.add(scrlpParamRule, "bottom");
				scrlpParamRule.setBounds(112, 140, 431, 143);
			}
		} catch (Exception e) {
			Log.logError("存储过程面板初始化面板错误:", e);
			return pnlProcedure;
		} finally {
		}
		return pnlProcedure;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getPName())) {
			ShowMsg.showWarn("存储过程名称不能为空");
			return false;
		} else if ("".equals(bean.getPDbName())) {
			ShowMsg.showWarn("数据源连接不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setPName(UtilString.isNil(txtPName.getText()));
			bean.setPDbName(UtilString.isNil(txtDbName.getText()));
			String paramRule = UtilString.isNil(txtrParamRule.getText());
			if (!paramRule.endsWith(";"))
				paramRule = paramRule + ";";
			bean.setPParamsRule(paramRule);
			bean.setComparePapam(UtilString.isNil(txtCompareParam.getText()));
			bean.setCompareType(UtilString.isNil(cmbCompareType.getSelectedItem().toString()));
			bean.setCompareValue(UtilString.isNil(txtCompareValue.getText()));
			bean.setIfErrorWarn(chkParam.isSelected());
			bean.setWarning(UtilString.isNil(txtWarn.getText()));
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			// Log.logError("存储过程面板赋值任务对象错误:", e);
			ShowMsg.showWarn(e.getMessage());
			return false;
		} finally {
		}
		return true;
	}

	// 填充参数
	public void fillComp(ITask task) {
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(task.getJsonStr(), Bean.class);
			txtPName.setText(bean.getPName());
			txtDbName.setText(bean.getPDbName());
			txtrParamRule.setText(bean.getPParamsRule());
			chkParam.setSelected(bean.isIfErrorWarn());
			txtCompareParam.setText(bean.getComparePapam());
			txtWarn.setText(bean.getWarning());
			cmbCompareType.setSelectedItem(bean.getCompareType());
			txtCompareValue.setText(bean.getCompareValue());
			warnEnabled(chkParam.isSelected());
		} catch (Exception e) {
			Log.logError("存储过程面板填充控件错误:", e);
		} finally {
		}
	}

	private void btnDbName() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("存储过程面板获取数据源链接错误:", e);
		} finally {
		}
	}

	private SButton getBtnParamRule() {
		if (btnParamRule == null) {
			btnParamRule = new SButton("..");
			btnParamRule.setBounds(549, 141, 22, 21);
			btnParamRule.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnParamRuleActionPerformed("存储过程参数");
				}
			});
		}
		return btnParamRule;
	}

	private void btnParamRuleActionPerformed(String setName) {
		AppFun.getSet(txtrParamRule, setName);
	}

	private SCheckBox getChkParam() {
		if (chkParam == null) {
			chkParam = new SCheckBox("\u62a5\u8b66\u53c2\u6570");
			chkParam.setBounds(38, 307, 74, 18);
			chkParam.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (chkParam.isSelected()) {
						warnEnabled(true);
					} else {
						warnEnabled(false);
					}
				}
			});
		}
		return chkParam;
	}

	private void warnEnabled(boolean enabled) {
		txtCompareParam.setEnabled(enabled);
		txtCompareValue.setEnabled(enabled);
		txtWarn.setEnabled(enabled);
		cmbCompareType.setEnabled(enabled);
		lWarn.setEnabled(enabled);

	}

	private STextField getTxtCompareParam() {
		if (txtCompareParam == null) {
			txtCompareParam = new STextField();
			txtCompareParam.setBounds(112, 304, 144, 21);
			txtCompareParam.setEnabled(false);
		}
		return txtCompareParam;
	}

	private SComboBox getCmbCompareType() {
		if (cmbCompareType == null) {
			cmbCompareType = new SComboBox(Const.CompareType);
			cmbCompareType.setBounds(267, 304, 100, 21);
			cmbCompareType.setEnabled(false);
		}
		return cmbCompareType;
	}

	private STextField getTxtCompareValue() {
		if (txtCompareValue == null) {
			txtCompareValue = new STextField();
			txtCompareValue.setBounds(381, 304, 160, 21);
			txtCompareValue.setEnabled(false);
		}
		return txtCompareValue;
	}

	private STextField getTxtWarn() {
		if (txtWarn == null) {
			txtWarn = new STextField();
			txtWarn.setBounds(112, 335, 429, 21);
			txtWarn.setEnabled(false);
		}
		return txtWarn;
	}

	private SLabel getLWarn() {
		if (lWarn == null) {
			lWarn = new SLabel("\u63d0\u793a\u5185\u5bb9");
			lWarn.setBounds(38, 342, 71, 14);
			lWarn.setEnabled(false);
		}
		return lWarn;
	}
}
