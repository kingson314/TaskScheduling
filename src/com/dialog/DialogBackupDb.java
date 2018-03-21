package com.dialog;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import com.log.Log;

import common.component.SBorder;
import common.component.SButton;
import common.component.SDialog;
import common.component.SLabel;
import common.component.SSplitPane;
import common.component.STextField;
import common.component.ShowDialog;
import common.component.ShowMsg;
import common.util.file.UtilFile;
import consts.ImageContext;

public class DialogBackupDb extends SDialog {

	private static final long serialVersionUID = 1L;
	private SButton btnExit;
	private SButton btnSet;
	private JPanel pnlBackup;
	private SSplitPane SplMain;
	private STextField txtBackupdbPath;
	private SLabel lBackup;
	private SButton btnBackPath;
	private JPanel btnTool;
	private SLabel lSrc;
	private STextField txtSrc;
	private SButton btnSrc;

	// public static void main(String[] args) {
	//
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// DialogBackupDb inst = new DialogBackupDb();
	// inst.setVisible(true);
	// }
	// });
	// }
	// 构造
	public DialogBackupDb() {
		try {
			initGUI();
		} catch (Exception e) {
			Log.logError("备份数据对话框构造错误:", e);
		} finally {
		}
	}

	// 初始化界面
	private void initGUI() {
		try {
			this.setSize(622, 404);
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.Backup));
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			setTitle("数据库备份");
			setModal(true);
			GridLayout thisLayout = new GridLayout(1, 1);
			thisLayout.setColumns(1);
			thisLayout.setHgap(5);
			thisLayout.setVgap(5);
			{
				SplMain = new SSplitPane(0, this.getHeight() - 100, false);
				getContentPane().add(SplMain);
				SplMain.setPreferredSize(new java.awt.Dimension(707, 183));
				{
					pnlBackup = new JPanel();
					pnlBackup.setLayout(null);
					pnlBackup.setPreferredSize(new java.awt.Dimension(612, 180));
					SplMain.add(pnlBackup, SSplitPane.TOP);
					{
						pnlBackup.setBorder(SBorder.getTitledBorder());

						{
							lSrc = new SLabel("源路径");
							pnlBackup.add(lSrc);
							lSrc.setBounds(55, 115, 50, 14);
						}
						{
							txtSrc = new STextField();
							pnlBackup.add(txtSrc);
							txtSrc.setBounds(111, 108, 367, 21);
							txtSrc.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									getSrcPath();
								}
							});
						}
						{
							btnSrc = new SButton("..");
							pnlBackup.add(btnSrc);
							btnSrc.setBounds(495, 108, 22, 21);
							btnSrc.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									getSrcPath();
								}
							});
						}

						{
							lBackup = new SLabel("\u5907\u4efd\u8def\u5f84");
							pnlBackup.add(lBackup);
							lBackup.setBounds(55, 145, 50, 14);
						}
						{
							txtBackupdbPath = new STextField();
							pnlBackup.add(txtBackupdbPath);
							txtBackupdbPath.setBounds(111, 138, 367, 21);
							txtBackupdbPath.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									getBackPath();
								}
							});
						}
						{
							btnBackPath = new SButton("..");
							pnlBackup.add(btnBackPath);
							btnBackPath.setBounds(495, 138, 22, 21);
							btnBackPath.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									getBackPath();
								}
							});
						}

					}

				}
				{
					btnTool = new JPanel();
					SplMain.add(btnTool, SSplitPane.BOTTOM);
					btnTool.setLayout(null);
					btnTool.setPreferredSize(new java.awt.Dimension(612, 160));
					{
						btnSet = new SButton("备  份", ImageContext.Ok);
						btnTool.add(btnSet);
						btnSet.setBounds(295, 15, 85, 31);
						btnSet.setSize(100, 25);
						btnSet.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								backup();
							}
						});
					}
					{
						btnExit = new SButton("\u9000  \u51fa", ImageContext.Exit);
						btnTool.add(btnExit);
						btnExit.setBounds(434, 15, 85, 31);
						btnExit.setSize(100, 25);
						btnExit.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								exit();
							}
						});
					}
				}
			}
		} catch (Exception e) {
			Log.logError("备份数据对话框初始化界面错误:", e);
		} finally {
		}

	}

	// 备份
	private void backup() {
		try {
			String dbNow = txtSrc.getText();
			String dbBackup = txtBackupdbPath.getText();
			if (UtilFile.copyFile(dbNow, dbBackup)) {
				ShowMsg.showMsg("备份数据成功！");
			} else {
				Log.logInfo("备份数据文件不存在！");
			}
		} catch (Exception e) {
			Log.logError("备份数据对话框备份错误:", e);
		} finally {
		}
	}

	// 退出
	private void exit() {
		try {
			this.dispose();
		} catch (Exception e) {
			Log.logError("备份数据对话框退出错误:", e);
		} finally {
		}
	}

	// 获取路径
	private void getBackPath() {
		try {
			txtBackupdbPath.setText(ShowDialog.save(this, "选择备份路径"));
		} catch (Exception e) {
			Log.logError("备份数据对话框选择备份路径错误:", e);
		} finally {
		}
	}

	// 获取路径
	private void getSrcPath() {
		try {
			txtSrc.setText(ShowDialog.openFile());
		} catch (Exception e) {
			Log.logError("备份数据对话框选择备份路径错误:", e);
		} finally {
		}
	}
}
