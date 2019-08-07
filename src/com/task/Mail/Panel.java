package com.task.Mail;

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
import common.component.SScrollPane;
import common.component.STextArea;
import common.component.STextField;
import common.component.ShowDialog;
import common.component.ShowMsg;
import common.util.file.UtilFile;
import common.util.json.UtilJson;

public class Panel implements ITaskPanel {

	private JPanel pnlMail;
	private STextArea txtaContent;
	private SButton btnMailFileName;
	private SButton btnMailFilePath;
	private STextField txtMailFileName;
	private SLabel l9;
	private STextField txtMailfilePath;
	private SLabel l8;
	private SLabel l5;
	private STextField txtSubject;
	private SLabel l4;
	private SButton btnMail;
	private STextField txtMail;
	private SLabel l3;
	private STextField txtAddress;
	private SLabel l7;
	private SButton btnAddress;
	private SScrollPane scrl;

	public Panel() {
	}

	public JPanel getPanel() {
		pnlMail = new JPanel();
		try {
			pnlMail.setLayout(null);
			pnlMail.setBounds(-13, 13, 626, 43);
			pnlMail.setPreferredSize(new java.awt.Dimension(642, 375));
			{
				btnMailFileName = new SButton("..");
				pnlMail.add(btnMailFileName);
				btnMailFileName.setBounds(548, 156, 22, 21);
				btnMailFileName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						mailFileNamebtn();
					}
				});
			}
			{
				btnMailFilePath = new SButton("..");
				pnlMail.add(btnMailFilePath);
				btnMailFilePath.setBounds(548, 124, 22, 21);
				btnMailFilePath.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						mailFilePathbtn();
					}
				});
			}
			{
				txtMailFileName = new STextField();
				pnlMail.add(txtMailFileName);
				txtMailFileName.setBounds(110, 156, 434, 21);
				txtMailFileName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							mailFileNamebtn();
					}
				});

			}
			{
				l9 = new SLabel("\u9644\u4ef6\u6587\u4ef6\u540d\u79f0");
				pnlMail.add(l9);
				l9.setBounds(31, 163, 73, 14);
			}
			{
				txtMailfilePath = new STextField();
				pnlMail.add(txtMailfilePath);
				txtMailfilePath.setBounds(110, 124, 434, 21);
				txtMailfilePath.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							mailFilePathbtn();
					}
				});

			}
			{
				l8 = new SLabel("\u53d1\u9001\u9644\u4ef6\u8def\u5f84");
				pnlMail.add(l8);
				l8.setBounds(31, 130, 73, 14);
			}
			{
				l5 = new SLabel("\u53d1\u9001\u5185\u5bb9");
				pnlMail.add(l5);
				l5.setBounds(31, 196, 73, 14);
			}
			{
				txtSubject = new STextField();
				pnlMail.add(txtSubject);
				txtSubject.setBounds(110, 93, 434, 21);
			}
			{
				l4 = new SLabel("\u53d1\u9001\u4e3b\u9898");
				pnlMail.add(l4);
				l4.setBounds(31, 98, 73, 14);
			}
			{
				btnMail = new SButton("..");
				pnlMail.add(btnMail);
				btnMail.setVisible(false);
				btnMail.setBounds(548, 58, 22, 21);
				btnMail.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {

					}
				});
			}
			{
				txtMail = new STextField();
				pnlMail.add(txtMail);
				txtMail.setBounds(110, 58, 434, 21);
			}
			{
				l3 = new SLabel("\u6536\u4ef6\u4eba\u90ae\u7bb1");
				pnlMail.add(l3);
				l3.setBounds(31, 65, 73, 14);
			}
			{
				txtAddress = new STextField();
				pnlMail.add(txtAddress, "North");
				txtAddress.setBounds(110, 30, 434, 21);

				txtAddress.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						// if (checkClickTime())// 双击判断
						if (e.getClickCount() == 2)
							addressbtn();// emailIdMouseDoubleClick();
					}
				});
			}
			{
				l7 = new SLabel("\u53d1\u9001\u90ae\u7bb1");
				pnlMail.add(l7, "Center");
				l7.setBounds(31, 33, 73, 14);
			}
			{
				btnAddress = new SButton("..");
				pnlMail.add(btnAddress);
				btnAddress.setBounds(548, 30, 22, 21);
				btnAddress.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						addressbtn();
					}
				});
			}
			{

				{
					txtaContent = new STextArea();
				}
				scrl = new SScrollPane(txtaContent);
				pnlMail.add(scrl, "bottom");
				scrl.setBounds(110, 200, 434, 120);
			}
		} catch (Exception e) {
			Log.logError("邮箱信息面板初始化面板错误:", e);
			return pnlMail;
		} finally {
		}
		return pnlMail;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getAddress())) {
			ShowMsg.showWarn("发送邮箱不能为空");
			return false;
		}
		if ("".equals(bean.getMail())) {
			ShowMsg.showWarn("收件人邮箱不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setAddress(txtAddress.getText() == null ? "" : txtAddress.getText());
			bean.setMail(txtMail.getText() == null ? "" : txtMail.getText());
			bean.setSubject(txtSubject.getText() == null ? "" : txtSubject.getText());
			bean.setMailFilePath(txtMailfilePath.getText() == null ? "" : UtilFile.formatFilePath(txtMailfilePath.getText()));
			bean.setMailFileName(txtMailFileName.getText() == null ? "" : txtMailFileName.getText());
			bean.setContent(txtaContent.getText() == null ? "" : txtaContent.getText());
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			// Log.logError("邮箱信息面板赋值任务对象错误:", e);
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
			txtAddress.setText(bean.getAddress() == null ? "" : bean.getAddress());
			txtMail.setText(bean.getMail() == null ? "" : bean.getMail());
			txtSubject.setText(bean.getSubject() == null ? "" : bean.getSubject());
			txtMailfilePath.setText(bean.getMailFilePath() == null ? "" : bean.getMailFilePath());
			txtMailFileName.setText(bean.getMailFileName() == null ? "" : bean.getMailFileName());
			txtaContent.setText(bean.getContent() == null ? "" : bean.getContent());
		} catch (Exception e) {
			Log.logError("邮箱信息面板填充控件错误:", e);
		} finally {
		}
	}

	private void addressbtn() {
		try {
			// MaillMessageDialog maillMessageDialog = new MaillMessageDialog();
			// int w = this.TextFieldAddress.getX() + Main.taskdialog.getX();
			// int h = this.TextFieldAddress.getY() + Main.taskdialog.getY()
			// + this.TextFieldAddress.getHeight()
			// + Main.taskdialog.jPanel_taskmsg.getHeight() + 36;
			// maillMessageDialog.setLocation(w, h);
			// maillMessageDialog.show(true);
			AppFun.getMail(txtAddress);
		} catch (Exception e) {
			Log.logError("邮箱信息面板获取数据源链接错误:", e);
		} finally {
		}
	}

	private void mailFileNamebtn() {
		String[] path = ShowDialog.openFileForPathAndFile();
		txtMailfilePath.setText(path[0]);
		txtMailFileName.setText(path[1]);
	}

	private void mailFilePathbtn() {
		txtMailfilePath.setText(ShowDialog.openDir());
	}

}
