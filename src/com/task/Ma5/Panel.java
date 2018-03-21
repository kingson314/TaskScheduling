package com.task.Ma5;

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

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlMain;
	private SLabel lDbName;
	private STextField txtDbName;
	private SButton btnDbName;
	private STextField txtDuration;

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
				lDbName = new SLabel("行情时长(分钟)");
				pnlMain.add(lDbName);
				lDbName.setBounds(21, 54, 90, 14);
			}
			{
				txtDuration = new STextField();
				pnlMain.add(txtDuration);
				txtDuration.setBounds(110, 51, 429, 21);

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
		if ("".equals(bean.getDuration())) {
			ShowMsg.showWarn("数据源连接不能为空");
			return false;
		}

		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setDuration(Integer.valueOf(UtilString.isNil(txtDuration.getText())));
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
			txtDuration.setText(String.valueOf(bean.getDuration()));
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
