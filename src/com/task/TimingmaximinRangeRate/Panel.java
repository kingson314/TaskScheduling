package com.task.TimingmaximinRangeRate;

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
import common.component.SComboBox;
import common.component.SLabel;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import config.dictionary.DictionaryDao;
import consts.Const;

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlMain;
	private SLabel lDbName;
	private STextField txtDbName;
	private SButton btnDbName;
	private SLabel lSymbol;
	private SComboBox cmbSymbol;
	private SLabel lRange;
	private SComboBox cmbRange;
	private SLabel lMinute;
	private SComboBox cmbMinute;

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
				lDbName.setBounds(21, 24, 70, 14);
			}
			{
				txtDbName = new STextField();
				pnlMain.add(txtDbName);
				txtDbName.setBounds(110, 21, 429, 21);
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
				lSymbol = new SLabel("货币名称");
				pnlMain.add(lSymbol);
				lSymbol.setBounds(21, 54, 90, 14);
			}
			{
				cmbSymbol = new SComboBox(Const.SymbolArr);
				pnlMain.add(cmbSymbol);
				cmbSymbol.setBounds(110, 51, 429, 21);
			}
			{
				lRange = new SLabel("极值范围");
				pnlMain.add(lRange);
				lRange.setBounds(21, 84, 90, 14);
			}
			{
				cmbRange = new SComboBox(DictionaryDao.getInstance().getDicionary("ArrayRange"));
				cmbRange.setEditable(true);
				pnlMain.add(cmbRange);
				cmbRange.setBounds(110, 81, 429, 21);
			}
			{
				lMinute = new SLabel("第N分钟");
				pnlMain.add(lMinute);
				lMinute.setBounds(21, 114, 90, 14);
			}
			{
				cmbMinute = new SComboBox(Const.minutePointArr);
				cmbMinute.setEditable(true);
				pnlMain.add(cmbMinute);
				cmbMinute.setBounds(110, 111, 429, 21);
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
		if ("".equals(bean.getSymbol())) {
			ShowMsg.showWarn("货币名称不能为空");
			return false;
		}

		if ("".equals(bean.getRange())) {
			ShowMsg.showWarn("极值波动范围不能为空");
			return false;
		}
		if ("".equals(bean.getMinute())) {
			ShowMsg.showWarn("分钟不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setSymbol(UtilString.isNil(cmbSymbol.getSelectedItem().toString()));
			bean.setRange(UtilString.isNil(cmbRange.getSelectedItem().toString()));
			bean.setMinute(Integer.valueOf(UtilString.isNil(cmbMinute.getSelectedItem().toString(), "30")));
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
			cmbSymbol.setSelectedItem(bean.getSymbol());
			cmbRange.setSelectedItem(bean.getRange().replace("\"", ""));
			cmbMinute.setSelectedItem(bean.getMinute());
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
