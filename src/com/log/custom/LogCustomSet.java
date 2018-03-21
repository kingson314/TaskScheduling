package com.log.custom;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;


import com.log.Log;
import com.settings.Settings;
import com.settings.SettingsDao;
import com.taskInterface.ITask;
import common.component.SButton;

import consts.ImageContext;

public class LogCustomSet extends JDialog {

	private static final long serialVersionUID = -8698934447915973542L;
	private com.task.CustomQuery.Panel pnlSet;

	// 构造
	public LogCustomSet() {
		super();
		setModal(true);
		setAlwaysOnTop(false);
		this.setBounds(0, 0, 600, 360);
		int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
		int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
		this.setLocation(w, h);
		this.setLayout(null);

		pnlSet = new com.task.CustomQuery.Panel();
		JPanel panel = pnlSet.getPanel();
		panel.setBounds(0, 0, 600, 300);
		pnlSet.hideReturnType();
		this.add(panel);
		this.fillComp();
		this.add(getTool());
	}

	// 获取工具栏面板
	private JPanel getTool() {
		JPanel pnlTool = new JPanel();
		pnlTool.setBounds(0, 300, 600, 60);
		{
			SButton SButton_ok = new SButton("确   定", ImageContext.Ok);
			pnlTool.add(SButton_ok);
			SButton_ok.setPreferredSize(new java.awt.Dimension(120, 25));
			SButton_ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnOk();
				}
			});
		}
		{
			SButton SButton_cancel = new SButton("取  消", ImageContext.Exit);
			pnlTool.add(SButton_cancel);
			SButton_cancel.setPreferredSize(new java.awt.Dimension(121, 25));
			SButton_cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnCancel();
				}
			});
		}
		return pnlTool;
	}

	// 填充面板
	private void fillComp() {
		try {
			com.task.CustomQuery.Task task = new com.task.CustomQuery.Task();
			task.setJsonStr(SettingsDao.getInstance().getValue("定制日志"));
			pnlSet.fillComp(task);
		} catch (Exception e) {
			Log.logError("面板填充控件错误:", e);
		} finally {
		}
	}

	// 确定
	private void btnOk() {
		String setValue = SettingsDao.getInstance().getValue("定制日志");
		ITask task = new com.task.CustomQuery.Task();
		pnlSet.fillTask(task);
		Settings settings = new Settings();
		settings.setSetName("定制日志");
		settings.setSetValue(task.getJsonStr());
		settings.setSetMemo("定制日志查询");
		if ("".equals(setValue)) {
			SettingsDao.getInstance().addSettings(settings);
		} else {
			settings.setId(SettingsDao.getInstance().getId("定制日志"));
			SettingsDao.getInstance().modSettings(settings);
		}
		btnCancel();
	}

	// 取消
	private void btnCancel() {
		try {
			this.dispose();
		} catch (Exception e) {
			Log.logError("定制日志面板退出错误:", e);
		} finally {
		}
	}
}
