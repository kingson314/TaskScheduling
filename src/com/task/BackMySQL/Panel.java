package com.task.BackMySQL;

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
import common.component.ShowDialog;
import common.component.ShowMsg;
import common.util.json.UtilJson;

public class Panel implements ITaskPanel {
	private JPanel pnlBackup;
	private SLabel lDbPath;
	private SLabel lDbconn;
	private STextField txtDbPath;
	private STextField txtDbName;
	private SButton btnDbconn;
	private STextField txtFilePath;
	private SLabel lFilePath;
	private SButton btnFilePath;
	private SButton docDBName;

	 

	public Panel() {
	}

	public JPanel getPanel() {
		pnlBackup = new JPanel();
		try {
			pnlBackup.setLayout(null);
			pnlBackup.setBounds(-13, 13, 626, 43);
			pnlBackup.setPreferredSize(new java.awt.Dimension(668, 482));
			pnlBackup.setSize(418, 95);
			
			{
				lFilePath = new SLabel("备份路径");
				pnlBackup.add(lFilePath);
				lFilePath.setBounds(30, 40, 67, 14);
			}
			{
				txtFilePath = new STextField();
				pnlBackup.add(txtFilePath);
				txtFilePath.setBounds(120, 40, 418, 22);
				txtFilePath.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							filePathbtn();
					}
				});
			}
			{
				btnFilePath = new SButton("..");
				pnlBackup.add(btnFilePath);
				btnFilePath.setBounds(544, 41, 22, 21);
				btnFilePath.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						filePathbtn();
					}
				});
			}
			
			{
				lDbPath = new SLabel("数据库安装路径");
				pnlBackup.add(lDbPath);
				lDbPath.setBounds(30, 77, 110, 14);
			}
			{
				txtDbPath = new STextField();
				pnlBackup.add(txtDbPath);
				txtDbPath.setBounds(120, 74, 418, 21);
				txtDbPath.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							dbPathbtn();
					}
				});
			}
			{
				docDBName = new SButton("..");
				pnlBackup.add(docDBName);
				docDBName.setBounds(544, 74, 22, 21);
				docDBName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dbPathbtn();
					}
				});
			}
			
			
			{
				lDbconn = new SLabel("数据库连接");
				pnlBackup.add(lDbconn);
				lDbconn.setBounds(30, 115, 70, 14);
			}
			{
				txtDbName = new STextField();
				pnlBackup.add(txtDbName);
				txtDbName.setBounds(120, 107, 418, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							dbConnbtn();
					}
				});
			}
			{
				btnDbconn = new SButton("..");
				pnlBackup.add(btnDbconn);
				btnDbconn.setBounds(544, 107, 22, 21);
				btnDbconn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dbConnbtn();
					}
				});
			}
		} catch (Exception e) {
			Log.logError("财汇禁限投库面板初始化面板错误:", e);
			return pnlBackup;
		} finally {
		}
		return pnlBackup;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getFilePath())) {
			ShowMsg.showWarn("备份路径不能为空");
			return false;
		}
		if ("".equals(bean.getDbPath())) {
			ShowMsg.showWarn("服务器安装路径不能为空");
			return false;
		}
		if ("".equals(bean.getDbName())) {
			ShowMsg.showWarn("数据库连接不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setFilePath(txtFilePath.getText() == null ? "" : txtFilePath.getText());
			bean.setDbPath(txtDbPath.getText() == null ? "" : txtDbPath.getText());
			bean.setDbName(txtDbName.getText() == null ? "" : txtDbName.getText());
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
			txtFilePath.setText(bean.getFilePath());
			txtDbPath.setText(bean.getDbPath());
			txtDbName.setText(bean.getDbName());
		} catch (Exception e) {
			ShowMsg.showWarn(e.getMessage());
		} finally {
		}
	}

	private void dbConnbtn() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			ShowMsg.showWarn(e.getMessage());
		} finally {
		}
	}

	private void dbPathbtn() {
		txtDbPath.setText(ShowDialog.openDir());
	}

	private void filePathbtn() {
		txtFilePath.setText(ShowDialog.openFile());
	}

 

}
