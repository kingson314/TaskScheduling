package com.task.ImportOrders;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.json.JSONObject;

import com.app.AppFun;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.SComboBox;
import common.component.ShowMsg;
import common.util.string.UtilString;
import consts.Const;

public class Panel extends JDialog implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlMain;
	private JLabel lDbName;
	private JTextField txtDbName;
	private JButton btnDbName;
	private JTextField txtFilePath;
	private JLabel lFilePath;
	private JButton btnFilePath;
	private JLabel lAccountName;
	private String[] accountNameArr = new String[] {"2014036866","2013015433","2013015432"};
	private SComboBox cmbAccountName;

	public static void main(String[] args) {
		Panel inis = new Panel();

		inis.add(inis.getPanel());
		inis.setBounds(0, 0, 623, 606);
		int w = (Toolkit.getDefaultToolkit().getScreenSize().width - inis.getWidth()) / 2;
		int h = (Toolkit.getDefaultToolkit().getScreenSize().height - inis.getHeight()) / 2;
		inis.setLocation(w, h);
		inis.setVisible(true);
	}

	public Panel() {
		this.setSize(615, 480);
		this.add(getPanel());
	}

	public JPanel getPanel() {
		pnlMain = new JPanel();
		try {
			getContentPane().add(pnlMain);
			pnlMain.setLayout(null);
			pnlMain.setBounds(-13, 13, 626, 43);
			pnlMain.setBorder(BorderFactory.createTitledBorder(""));
			pnlMain.setPreferredSize(new java.awt.Dimension(668, 482));
			pnlMain.setSize(429, 95);
			{
				lFilePath = new JLabel();
				lFilePath.setText("\u6587\u4ef6\u8def\u5f84");
				lFilePath.setFont(Const.tfont);
				lFilePath.setBounds(21, 36, 60, 14);
				pnlMain.add(lFilePath);
			}
			{
				txtFilePath = new JTextField();
				txtFilePath.setBounds(103, 29, 429, 21);
				pnlMain.add(txtFilePath);
				txtFilePath.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnFilePath();
					}
				});
			}
			{
				btnFilePath = new JButton();
				btnFilePath.setText("..");
				btnFilePath.setBounds(538, 29, 20, 21);
				pnlMain.add(btnFilePath);
				btnFilePath.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnFilePath();
					}
				});
			}
			{
				lAccountName = new JLabel();
				lAccountName.setText("账号名称");
				lAccountName.setFont(Const.tfont);
				lAccountName.setBounds(21, 66, 60, 14);
				pnlMain.add(lAccountName);
			}
			{
				cmbAccountName = new SComboBox(accountNameArr);
				cmbAccountName.setBounds(103, 59, 200, 21);
				pnlMain.add(cmbAccountName);
			}
			{
				lDbName = new JLabel();
				pnlMain.add(lDbName);
				lDbName.setText("\u6570\u636e\u5e93\u8fde\u63a5");
				lDbName.setBounds(21, 96, 70, 14);
				lDbName.setFont(Const.tfont);
			}

			{
				txtDbName = new JTextField();
				pnlMain.add(txtDbName);
				txtDbName.setBounds(103, 89, 429, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnDbName();
					}
				});
			}
			{
				btnDbName = new JButton();
				pnlMain.add(btnDbName);
				btnDbName.setText("..");
				btnDbName.setBounds(538, 89, 22, 21);
				btnDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDbName();
					}
				});
			}

		} catch (Exception e) {
			Log.logError("面板构造错误:", e);
			return pnlMain;
		} finally {
		}
		return pnlMain;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getDbName())) {
			ShowMsg.showWarn("数据源连接不能为空");
			return false;
		} else if ("".equals(bean.getFilePath())) {
			ShowMsg.showWarn("文件路径不能为空");
			return false;
		}
		return true;
	}

	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setFilePath(UtilString.isNil(txtFilePath.getText()));
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setAccountName( cmbAccountName.getSelectedItem().toString());
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(JSONObject.fromObject(bean).toString());
		} catch (Exception e) {
			Log.logError("面板赋值任务对象错误:", e);
			return false;
		} finally {
		}
		return true;
	}

	public void fillComp(ITask task) {
		try {
			Bean bean = (Bean) JSONObject.toBean(JSONObject.fromObject(task.getJsonStr()), Bean.class);
			txtFilePath.setText(bean.getFilePath());
			txtDbName.setText(bean.getDbName());
			cmbAccountName.setSelectedItem(bean.getAccountName());
		} catch (Exception e) {
			Log.logError("面板填充控件错误:", e);
		} finally {
		}
	}

	private void btnDbName() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("面板获取数据源链接错误:", e);
		} finally {
		}
	}

	private void btnFilePath() {
		try {
			JFileChooser file = new JFileChooser();
			file.setDialogTitle("选择文件路径");
			file.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int val = file.showOpenDialog(this);
			if (val == JFileChooser.APPROVE_OPTION) {
				txtFilePath.setText(file.getSelectedFile().getAbsolutePath());
			}
		} catch (Exception e) {
			Log.logError("获取文件路径错误:", e);
		} finally {
		}
	}

}
