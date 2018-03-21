package com.taskmanu;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;


import com.log.Log;

import common.component.SBorder;
import common.component.SButton;
import common.component.SCalendar;
import common.component.SDialog;
import common.component.SLabel;
import common.component.STextField;
import common.util.conver.UtilConver;
import consts.Const;
import consts.ImageContext;

public class ManuExecParamDialog extends SDialog {
	private static final long serialVersionUID = 20120223l;
	private SLabel lBegdate;
	private SButton btnBegdate;
	private SButton btnOK;
	private SButton btnCancel;
	private JPanel pnlMain;
	private SButton btnEnddate;
	private STextField txtFundCode;
	private SLabel lFundcode;
	private STextField txtEndDate;
	private SLabel lEnddate;
	private STextField txtBegdate;

	public String[] paramValue;
	public boolean isCancle;
	private boolean hasFundCode;

	public ManuExecParamDialog(boolean vhasFundCode) {
		try {
			this.paramValue = null;
			this.isCancle = true;
			this.hasFundCode = vhasFundCode;
			initGUI();
		} catch (Exception e) {
			Log.logError("参数输入对话框对话框构造错误:", e);
		}
	}

	// 初始界面
	private void initGUI() {
		try {
			if (this.hasFundCode) {
				this.setSize(460, 198);
			} else
				this.setSize(460, 168);
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.DialogManuExecTaskParam));
			setModal(true);
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			this.setTitle("参数输入对话框");
			getContentPane().setLayout(null);
			{
				lBegdate = new SLabel("\u5f00\u59cb\u65e5\u671f");
				getContentPane().add(lBegdate);
				lBegdate.setBounds(31, 35, 57, 14);
			}
			{
				txtBegdate = new STextField();
				getContentPane().add(txtBegdate);
				txtBegdate.setBounds(94, 32, 94, 21);
				txtBegdate.setText(UtilConver.dateToStr(Const.fm_yyyyMMdd));
			}
			{
				lEnddate = new SLabel("\u7ed3\u675f\u65e5\u671f");
				getContentPane().add(lEnddate);
				lEnddate.setBounds(238, 35, 63, 14);
			}
			{
				txtEndDate = new STextField();
				getContentPane().add(txtEndDate);
				txtEndDate.setBounds(293, 32, 86, 21);
				txtEndDate.setText(UtilConver.dateToStr(Const.fm_yyyyMMdd));
			}
			if (this.hasFundCode) {
				{
					lFundcode = new SLabel("\u57fa\u91d1\u4ee3\u7801");
					getContentPane().add(lFundcode);
					lFundcode.setBounds(31, 70, 57, 14);
				}
				{

					txtFundCode = new STextField();
					getContentPane().add(txtFundCode);
					txtFundCode.setBounds(94, 67, 90, 21);

				}
			}
			{
				btnBegdate = new SButton("..");
				getContentPane().add(btnBegdate);
				btnBegdate.setBounds(190, 32, 22, 21);
				btnBegdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						new SCalendar(txtBegdate);
					}
				});
			}
			{
				btnEnddate = new SButton("..");
				getContentPane().add(btnEnddate);
				btnEnddate.setBounds(381, 32, 22, 21);
				btnEnddate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						new SCalendar(txtEndDate);
					}
				});
			}
			{
				pnlMain = new JPanel();
				FlowLayout jPanel2Layout = new FlowLayout();
				pnlMain.setLayout(jPanel2Layout);
				getContentPane().add(pnlMain);
				if (this.hasFundCode) {
					pnlMain.setBounds(1, 112, 451, 59);
				} else
					pnlMain.setBounds(1, 82, 451, 59);
				pnlMain.setBorder(SBorder.getTitledBorder());
				{
					btnOK = new SButton("\u786e  \u5b9a", ImageContext.Ok);
					pnlMain.add(btnOK);
					btnOK.setBounds(94, 130, 120, 25);
					btnOK.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnOK();
						}
					});
				}
				{
					btnCancel = new SButton("\u53d6  \u6d88", ImageContext.Exit);
					pnlMain.add(btnCancel);
					btnCancel.setBounds(238, 130, 120, 25);
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCancel();
						}
					});
				}
			}

			this.setVisible(true);

		} catch (Exception e) {
			Log.logError("参数输入对话框对话框初始化界面错误:", e);
		} finally {
		}
	}

	// 取消
	private void btnCancel() {
		try {
			this.dispose();
		} catch (Exception e) {
			Log.logError("参数输入对话框对话框退出错误:", e);
		} finally {
		}
	}

	// 确定
	private void btnOK() {
		try {
			this.isCancle = false;
			paramValue = new String[3];
			paramValue[0] = txtBegdate.getText() == null ? "" : txtBegdate.getText().trim();
			paramValue[1] = txtEndDate.getText() == null ? "" : txtEndDate.getText().trim();
			if (this.hasFundCode) {
				paramValue[2] = txtFundCode.getText() == null ? "" : txtFundCode.getText().trim();
			}
			this.dispose();
		} catch (Exception e) {
			Log.logError("参数输入对话框对话框确定错误:", e);
		} finally {
		}
	}

}
