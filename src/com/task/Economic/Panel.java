package com.task.Economic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import common.component.ShowDialog;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlMain;
	private SCheckBox chkIsEnconomicData;
	private SCheckBox chkIsEnconomicEvent;
	private SCheckBox chkIsEnconomicHoliday;
	private SCheckBox chkIsEnconomicNationalDebt;
	private SCheckBox chkIsSaveFile;
	private STextField txtSaveFilePath;
	private STextField txtUrl;
	private SLabel lUrl;
	private SLabel lDbName;
	private STextField txtDbName;
	private SButton btnDbName;
	private SButton btnSaveFilePath;
	private SLabel lSource;
	private SComboBox cmbSource;

	public Panel() {
	}

	public JPanel getPanel() {

		pnlMain = new JPanel();
		try {
			pnlMain.setBounds(0, 0, 623, 480);
			pnlMain.setLayout(null);

			{
				lUrl = new SLabel("URL");
				pnlMain.add(lUrl);
				lUrl.setBounds(21, 58, 70, 14);
				lUrl.setFont(Const.tfont);
			}
			{
				txtUrl = new STextField();
				pnlMain.add(txtUrl);
				txtUrl.setBounds(103, 51, 429, 21);
			}
			{
				lDbName = new SLabel("数据库连接");
				pnlMain.add(lDbName);
				lDbName.setBounds(21, 88, 70, 14);
				lDbName.setFont(Const.tfont);
			}
			{
				txtDbName = new STextField();
				pnlMain.add(txtDbName);
				txtDbName.setBounds(103, 81, 429, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnDbName();
					}
				});
			}
			{
				btnDbName = new SButton("..");
				pnlMain.add(btnDbName);
				btnDbName.setBounds(538, 81, 22, 21);
				btnDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDbName();
					}
				});
			}
			{
				lSource = new SLabel("网站名称");
				pnlMain.add(lSource);
				lSource.setBounds(21, 111, 70, 14);
			}
			{
				cmbSource = new SComboBox(new String[] { "福汇网", "汇通网" });
				pnlMain.add(cmbSource);
				cmbSource.setBounds(103, 111, 200, 21);
				cmbSource.addItemListener(new ItemListener() {
					public void itemStateChanged(final ItemEvent e) {
						int index = cmbSource.getSelectedIndex();
						if (index == 0) {
							setEnabled(false);
						} else
							setEnabled(true);
					}
				});
			}
			{
				chkIsEnconomicData = new SCheckBox("是否导入经济数据");
				pnlMain.add(chkIsEnconomicData);
				chkIsEnconomicData.setBounds(21, 141, 200, 21);
				chkIsEnconomicData.setSelected(true);
			}
			{
				chkIsEnconomicEvent = new SCheckBox("是否导入财经事件");
				pnlMain.add(chkIsEnconomicEvent);
				chkIsEnconomicEvent.setBounds(250, 141, 200, 21);
				chkIsEnconomicEvent.setSelected(true);
			}
			{
				chkIsEnconomicHoliday = new SCheckBox("是否导入假期预告");
				pnlMain.add(chkIsEnconomicHoliday);
				chkIsEnconomicHoliday.setBounds(21, 171, 200, 21);
				chkIsEnconomicHoliday.setSelected(true);
			}
			{
				chkIsEnconomicNationalDebt = new SCheckBox("是否导入国债发行预告");
				pnlMain.add(chkIsEnconomicNationalDebt);
				chkIsEnconomicNationalDebt.setBounds(250, 171, 200, 21);
				chkIsEnconomicNationalDebt.setSelected(true);
			}
			{
				chkIsSaveFile = new SCheckBox("是否备份网页");
				pnlMain.add(chkIsSaveFile);
				chkIsSaveFile.setBounds(21, 201, 100, 21);
				chkIsSaveFile.setSelected(true);
			}
			{
				txtSaveFilePath = new STextField();
				pnlMain.add(txtSaveFilePath);
				txtSaveFilePath.setBounds(130, 201, 400, 21);
			}
			{
				btnSaveFilePath = new SButton("..");
				pnlMain.add(btnSaveFilePath);
				btnSaveFilePath.setBounds(538, 201, 22, 21);
				btnSaveFilePath.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						txtSaveFilePath.setText(ShowDialog.openDir());
					}
				});
			}
			setEnabled(false);
		} catch (Exception e) {
			Log.logError("采集面板初始化面板错误:", e);
			return pnlMain;
		} finally {
		}
		return pnlMain;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(UtilString.isNil(bean.getUrl()))) {
			ShowMsg.showWarn("Url不能为空");
			return false;
		}
		if ("".equals(UtilString.isNil(bean.getDbName()))) {
			ShowMsg.showWarn("数据源不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setSource(UtilString.isNil(cmbSource.getSelectedItem().toString()));
			bean.setUrl(UtilString.isNil(txtUrl.getText()));
			bean.setEnconomicData(chkIsEnconomicData.isSelected());
			bean.setEnconomicEvent(chkIsEnconomicEvent.isSelected());
			bean.setEnconomicHoliday(chkIsEnconomicHoliday.isSelected());
			bean.setEnconomicNationalDebt(chkIsEnconomicNationalDebt.isSelected());
			bean.setSaveFile(chkIsSaveFile.isSelected());
			bean.setSaveFilePath(UtilString.isNil(txtSaveFilePath.getText()));
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			ShowMsg.showWarn(e.getMessage());
			return false;
		} finally {
		}
		return true;
	}

	// 填充面板
	public void fillComp(ITask task) {
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(task.getJsonStr(), Bean.class);
			txtUrl.setText(bean.getUrl());
			cmbSource.setSelectedItem(bean.getSource());
			txtDbName.setText(bean.getDbName());
			chkIsSaveFile.setSelected(bean.isSaveFile());
			chkIsEnconomicNationalDebt.setSelected(bean.isEnconomicNationalDebt());
			chkIsEnconomicData.setSelected(bean.isEnconomicData());
			chkIsEnconomicEvent.setSelected(bean.isEnconomicEvent());
			chkIsEnconomicHoliday.setSelected(bean.isEnconomicHoliday());
			txtSaveFilePath.setText(bean.getSaveFilePath());
			if (cmbSource.getSelectedIndex() == 0) {
				setEnabled(false);
			}
		} catch (Exception e) {
			Log.logError("采集面板填充控件错误:", e);
		} finally {
		}
	}

	private void btnDbName() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("采集面板获取数据源链接错误:", e);
		} finally {
		}
	}

	private void setEnabled(boolean enabled) {
		chkIsEnconomicHoliday.setEnabled(enabled);
		chkIsEnconomicNationalDebt.setEnabled(enabled);
		chkIsEnconomicEvent.setEnabled(enabled);
		chkIsSaveFile.setEnabled(enabled);
		txtSaveFilePath.setEnabled(enabled);
		btnSaveFilePath.setEnabled(enabled);

		chkIsEnconomicHoliday.setSelected(enabled);
		chkIsEnconomicNationalDebt.setSelected(enabled);
		chkIsEnconomicEvent.setSelected(enabled);
		chkIsSaveFile.setSelected(enabled);
	}
}
