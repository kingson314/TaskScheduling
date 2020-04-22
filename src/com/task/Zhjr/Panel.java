package com.task.Zhjr;

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
import common.component.SLabel;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;

import consts.Const;

public class Panel implements ITaskPanel {
	private JPanel pnlMain;
	private STextField txtCookie;
	private SLabel lCookie;
	private SLabel lDbName;
	private STextField txtDbName;
	private SButton btnDbName;

	public Panel() {
	}

	public JPanel getPanel() {

		pnlMain = new JPanel();
		try {
			pnlMain.setBounds(0, 0, 623, 480);
			pnlMain.setLayout(null);

			{
				lCookie = new SLabel("Cookie");
				pnlMain.add(lCookie);
				lCookie.setBounds(21, 58, 70, 14);
				lCookie.setFont(Const.tfont);
			}
			{
				txtCookie = new STextField();
				pnlMain.add(txtCookie);
				txtCookie.setBounds(103, 51, 429, 21);
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
		} catch (Exception e) {
			Log.logError("采集面板初始化面板错误:", e);
			return pnlMain;
		} finally {
		}
		return pnlMain;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
//		if ("".equals(UtilString.isNil(bean.getCookie()))) {
//			ShowMsg.showWarn("Cookie不能为空");
//			return false;
//		}
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
			bean.setCookie(UtilString.isNil(txtCookie.getText().replace("%", "$$$")));
			 
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
			txtCookie.setText(bean.getCookie());
			txtDbName.setText(bean.getDbName());
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
}
