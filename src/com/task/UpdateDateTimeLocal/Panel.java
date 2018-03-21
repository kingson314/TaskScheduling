package com.task.UpdateDateTimeLocal;

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
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlMain;
	private SLabel lDbName;
	private STextField txtDbName;
	private SButton btnDbName;
	private STextField txtTimeDiffDaylightSaving;
	private SLabel lTimeDiffDaylightSaving;
	private STextField txtTimeDiffWinterTime;
	private SLabel lTimeDiffWinterTime;
	private SLabel lMinute;
	private SLabel lMinute1;
	private SComboBox cmbSymbol;
	private SLabel lSymbol;
	private SCheckBox chkPeriod1;
	private SCheckBox chkPeriod5;
	private SCheckBox chkPeriod15;
	private SCheckBox chkPeriod30;
	private SCheckBox chkPeriod60;
	private SCheckBox chkPeriod240;
	private SCheckBox chkPeriod1440;
	private SCheckBox chkDetail;

	public Panel() {
	}

	public JPanel getPanel() {
		pnlMain = new JPanel();
		try {
			pnlMain.setLayout(null);
			pnlMain.setBounds(-13, 13, 626, 43);
			pnlMain.setPreferredSize(new java.awt.Dimension(668, 482));
			pnlMain.setSize(429, 95);
			{
				lDbName = new SLabel("\u6570\u636e\u6e90\u8fde\u63a5");
				pnlMain.add(lDbName);
				lDbName.setBounds(21, 50, 70, 21);
			}
			{
				txtDbName = new STextField();
				pnlMain.add(txtDbName);
				txtDbName.setBounds(103, 50, 429, 21);
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
				btnDbName.setBounds(538, 50, 22, 21);
				btnDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dbConnbtn();
					}
				});
			}

			{
				lTimeDiffDaylightSaving = new SLabel("夏令时时间差");
				pnlMain.add(lTimeDiffDaylightSaving);
				lTimeDiffDaylightSaving.setBounds(21, 80, 90, 21);
			}
			{
				txtTimeDiffDaylightSaving = new STextField();
				pnlMain.add(txtTimeDiffDaylightSaving);
				txtTimeDiffDaylightSaving.setBounds(103, 80, 90, 21);
			}
			{
				lMinute = new SLabel("分钟");
				pnlMain.add(lMinute);
				lMinute.setBounds(210, 80, 50, 14);
			}
			{
				lTimeDiffWinterTime = new SLabel("冬令时时间差");
				pnlMain.add(lTimeDiffWinterTime);
				lTimeDiffWinterTime.setBounds(280, 80, 80, 21);
			}
			{
				txtTimeDiffWinterTime = new STextField();
				pnlMain.add(txtTimeDiffWinterTime);
				txtTimeDiffWinterTime.setBounds(360, 80, 100, 21);
			}
			{
				lMinute1 = new SLabel("分钟");
				pnlMain.add(lMinute1);
				lMinute1.setBounds(470, 80, 50, 21);
			}

			{
				lSymbol = new SLabel("货币名称");
				pnlMain.add(lSymbol);
				lSymbol.setBounds(21, 110, 70, 21);
				lSymbol.setFont(Const.tfont);
			}

			{
				cmbSymbol = new SComboBox(Const.SymbolArr);
				pnlMain.add(cmbSymbol);
				cmbSymbol.setBounds(103, 110, 429, 21);
			}
			{
				chkPeriod1 = new SCheckBox("1分钟");
				pnlMain.add(chkPeriod1);
				chkPeriod1.setBounds(103, 140, 100, 21);
			}
			{
				chkPeriod5 = new SCheckBox("5分钟");
				pnlMain.add(chkPeriod5);
				chkPeriod5.setBounds(250, 140, 100, 21);
				chkPeriod5.setSelected(true);
			}
			{
				chkPeriod15 = new SCheckBox("15分钟");
				pnlMain.add(chkPeriod15);
				chkPeriod15.setBounds(400, 140, 100, 21);
			}
			{
				chkPeriod30 = new SCheckBox("30分钟");
				pnlMain.add(chkPeriod30);
				chkPeriod30.setBounds(103, 170, 100, 21);
			}
			{
				chkPeriod60 = new SCheckBox("60分钟");
				pnlMain.add(chkPeriod60);
				chkPeriod60.setBounds(250, 170, 100, 21);
				chkPeriod60.setSelected(true);
			}
			{
				chkPeriod240 = new SCheckBox("240分钟");
				pnlMain.add(chkPeriod240);
				chkPeriod240.setBounds(400, 170, 250, 21);
			}
			{
				chkPeriod1440 = new SCheckBox("1440分钟");
				pnlMain.add(chkPeriod1440);
				chkPeriod1440.setBounds(103, 200, 100, 21);
			}
			{
				chkDetail = new SCheckBox("明细表");
				pnlMain.add(chkDetail);
				chkDetail.setBounds(250, 200, 100, 21);
			}
		} catch (Exception e) {
			Log.logError("面板构造错误:", e);
			return pnlMain;
		}
		return pnlMain;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getDbName())) {
			ShowMsg.showWarn("数据源连接不能为空");
			return false;
		}
		if ("".equals(bean.getTimeDiffDaylightSaving())) {
			ShowMsg.showWarn("夏令时时间差不能为空");
			return false;
		}
		if ("".equals(bean.getTimeDiffWinterTime())) {
			ShowMsg.showWarn("冬令时时间差不能为空");
			return false;
		} else if ("".equals(bean.getSymbol())||"全部".equals(bean.getSymbol())) {
			ShowMsg.showWarn("货币名称不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setTimeDiffDaylightSaving(Integer.valueOf(UtilString.isNil(txtTimeDiffDaylightSaving.getText())));
			bean.setTimeDiffWinterTime(Integer.valueOf(UtilString.isNil(txtTimeDiffWinterTime.getText())));
			bean.setSymbol(UtilString.isNil(cmbSymbol.getSelectedItem().toString()));
			bean.setPeriod1(chkPeriod1.isSelected());
			bean.setPeriod5(chkPeriod5.isSelected());
			bean.setPeriod15(chkPeriod15.isSelected());
			bean.setPeriod30(chkPeriod30.isSelected());
			bean.setPeriod60(chkPeriod60.isSelected());
			bean.setPeriod240(chkPeriod240.isSelected());
			bean.setPeriod1440(chkPeriod1440.isSelected());
			bean.setDetail(chkDetail.isSelected());
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			ShowMsg.showWarn(e.getMessage());
			return false;
		}
		return true;
	}

	// 填充面板
	public void fillComp(ITask task) {
		try {
			if (task.getJsonStr().equals(""))
				return;
			Bean bean = (Bean) UtilJson.getJsonBean(task.getJsonStr(), Bean.class);
			txtDbName.setText(bean.getDbName());
			txtTimeDiffDaylightSaving.setText(String.valueOf(bean.getTimeDiffDaylightSaving()));
			txtTimeDiffWinterTime.setText(String.valueOf(bean.getTimeDiffWinterTime()));
			cmbSymbol.setSelectedItem(bean.getSymbol());
			chkPeriod1.setSelected(bean.isPeriod1());
			chkPeriod5.setSelected(bean.isPeriod5());
			chkPeriod15.setSelected(bean.isPeriod15());
			chkPeriod30.setSelected(bean.isPeriod30());
			chkPeriod60.setSelected(bean.isPeriod60());
			chkPeriod240.setSelected(bean.isPeriod240());
			chkPeriod1440.setSelected(bean.isPeriod1440());
			chkDetail.setSelected(bean.isDetail());
		} catch (Exception e) {
			Log.logError("面板填充控件错误:", e);
		}
	}

	private void dbConnbtn() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("面板获取数据源链接错误:", e);
		}
	}

}
