package com.task.FileCheck;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.SButton;
import common.component.SCheckBox;
import common.component.SLabel;
import common.component.SMenuItem;
import common.component.SScrollPane;
import common.component.STextArea;
import common.component.STextField;
import common.component.ShowDialog;
import common.component.ShowMsg;
import common.util.file.UtilFile;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.ImageContext;

public class Panel implements ITaskPanel {
	private JPanel pnlFile;
	private JPanel pnlFilescr;
	private SLabel l9;
	private STextField txtFileCheckDir;
	private SLabel l2;
	private SLabel l1;
	private SLabel lScrfile;
	private STextField txtScrfile;
	private SLabel l10;
	private STextField txtFileCheckName;
	private STextArea txtaFileCheckNotExistWarning;
	private SScrollPane scrl1;
	private SCheckBox chkIffileCheckExistWarning;
	private SCheckBox chkIffileCheckNotExistWarning;
	private STextArea txtaFileCheckExistWarning;
	private SScrollPane scrlHsSql;
	private SButton btnScrpath;
	private SButton btnScrname;

	// public static void main(String[] args) {
	// Panel inis = new Panel();
	//
	// inis.add(inis.getPanel());
	// inis.setBounds(0, 0, 650, 480);
	// int w = (Toolkit.getDefaultToolkit().getScreenSize().width - inis
	// .getWidth()) / 2;
	// int h = (Toolkit.getDefaultToolkit().getScreenSize().height - inis
	// .getHeight()) / 2;
	// inis.setLocation(w, h);
	// inis.setVisible(true);
	// }

	public Panel() {
		// this.setSize(615, 480);
		// this.add(getPanel());

	}

	public JPanel getPanel() {

		pnlFile = new JPanel();
		try {
			// getContentPane().add(jPanelFile);
			pnlFile.setBounds(0, 0, 623, 480);
			pnlFile.setLayout(null);

			{
				pnlFilescr = new JPanel();
				pnlFile.add(getSScrollPanel_hsSql(), "bottom");
				pnlFile.add(getchk_iffileCheckNotExistWarning());
				pnlFile.add(getchk_iffileCheckExistWarning());
				pnlFile.add(getSScrollPane1(), "bottom");
				pnlFile.add(getSLabel1());
				pnlFile.add(getSLabel2());
				pnlFile.add(pnlFilescr);
				pnlFilescr.setLayout(null);
				pnlFilescr.setBounds(0, 0, 607, 111);
				{
					l9 = new SLabel("\u6587\u4ef6\u76ee\u5f55");
					pnlFilescr.add(l9);
					l9.setBounds(36, 44, 67, 14);
				}
				{
					txtFileCheckDir = new STextField();
					pnlFilescr.add(txtFileCheckDir);
					txtFileCheckDir.setBounds(111, 41, 420, 21);
					txtFileCheckDir.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							// if (checkClickTime())// 双击判断
							if (e.getClickCount() == 2)
								scrPathbtn();// mouseDoubleClick(TextField_srcfilepath);
						}
					});

				}
				{
					lScrfile = new SLabel("\u6587\u4ef6\u6e90");
					pnlFilescr.add(lScrfile);
					lScrfile.setBounds(310, 23, 55, 14);
					lScrfile.setVisible(false);
				}
				{
					txtScrfile = new STextField();
					pnlFilescr.add(txtScrfile);
					txtScrfile.setBounds(387, 16, 100, 21);
					txtScrfile.setVisible(false);
				}
				{
					l10 = new SLabel("\u6587\u4ef6\u540d\u79f0");
					pnlFilescr.add(l10);
					l10.setBounds(36, 70, 55, 14);
				}
				{
					txtFileCheckName = new STextField();
					pnlFilescr.add(txtFileCheckName);
					txtFileCheckName.setBounds(111, 67, 420, 21);
					txtFileCheckName.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							// if (checkClickTime())// 双击判断
							if (e.getClickCount() == 2)
								scrbtn();// mouseDoubleClick(TextField_srcfilename);
						}
					});

				}
				{
					btnScrpath = new SButton("..");
					pnlFilescr.add(btnScrpath);
					btnScrpath.setBounds(537, 41, 22, 21);
					btnScrpath.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							scrPathbtn();
						}
					});
				}
				{
					btnScrname = new SButton("..");
					pnlFilescr.add(btnScrname);
					btnScrname.setBounds(537, 67, 22, 21);
					btnScrname.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							scrbtn();
						}
					});
				}
			}
			addPupuMenu();
		} catch (Exception e) {
			Log.logError("文件检查面板初始化面板错误:", e);
			return pnlFile;
		} finally {
		}
		return pnlFile;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getFileCheckDir())) {
			ShowMsg.showWarn("文件目录不能为空");
			return false;
		}
		if ("".equals(bean.getFileCheckName())) {
			ShowMsg.showWarn("文件名称不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setFileCheckDir(UtilFile.formatFilePath(txtFileCheckDir.getText().trim()));
			bean.setFileCheckName(txtFileCheckName.getText().trim());
			bean.setFileCheckExistWarning(txtaFileCheckExistWarning.getText());
			bean.setFileCheckNotExistWarning(txtaFileCheckNotExistWarning.getText());
			bean.setIfFileCheckExistWarning(chkIffileCheckExistWarning.isSelected());
			bean.setIfFileCheckNotExistWarning(chkIffileCheckNotExistWarning.isSelected());
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			// Log.logError("文件检查面板赋值任务对象错误:", e);
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
			txtFileCheckDir.setText(bean.getFileCheckDir() == null ? "" : bean.getFileCheckDir());
			txtFileCheckName.setText(bean.getFileCheckName() == null ? "" : bean.getFileCheckName());
			txtaFileCheckNotExistWarning.setText(bean.getFileCheckNotExistWarning() == null ? "" : bean.getFileCheckNotExistWarning());
			txtaFileCheckExistWarning.setText(bean.getFileCheckExistWarning() == null ? "" : bean.getFileCheckExistWarning());
			chkIffileCheckExistWarning.setSelected(bean.getIfFileCheckExistWarning() == null ? false : bean.getIfFileCheckExistWarning());
			chkIffileCheckNotExistWarning.setSelected(bean.getIfFileCheckNotExistWarning() == null ? false : bean.getIfFileCheckNotExistWarning());
		} catch (Exception e) {
			Log.logError("文件检查面板填充控件错误:", e);
		} finally {
		}
	}

	private void scrbtn() {
		mouseDoubleClick(txtFileCheckName);
	}

	private void scrPathbtn() {
		mouseDoubleClick(txtFileCheckDir);
	}

	private void mouseDoubleClick(STextField TextField) {
		try {

			if (TextField.equals(txtFileCheckDir)) {
				txtFileCheckDir.setText(ShowDialog.openDir());
			} else if (TextField.equals(txtFileCheckName)) {
				String[] path = ShowDialog.openFileForPathAndFile();
				txtFileCheckDir.setText(path[0]);
				txtFileCheckName.setText(path[1]);
			}
		} catch (Exception e) {
			Log.logError("文件检查面板获取路径错误:", e);
		} finally {
		}

	}

	private SScrollPane getSScrollPanel_hsSql() {
		if (scrlHsSql == null) {
			txtaFileCheckExistWarning = new STextArea();
			scrlHsSql = new SScrollPane(txtaFileCheckExistWarning);
			scrlHsSql.setBounds(111, 265, 415, 80);
		}
		return scrlHsSql;
	}

	private SCheckBox getchk_iffileCheckNotExistWarning() {
		if (chkIffileCheckNotExistWarning == null) {
			chkIffileCheckNotExistWarning = new SCheckBox("\u6587\u4ef6\u4e0d\u5b58\u5728\u63d0\u793a");
			chkIffileCheckNotExistWarning.setBounds(34, 112, 134, 14);
		}
		return chkIffileCheckNotExistWarning;
	}

	private SCheckBox getchk_iffileCheckExistWarning() {
		if (chkIffileCheckExistWarning == null) {
			chkIffileCheckExistWarning = new SCheckBox("\u6587\u4ef6\u5b58\u5728\u63d0\u793a");
			chkIffileCheckExistWarning.setBounds(34, 236, 128, 14);
		}
		return chkIffileCheckExistWarning;
	}

	private SScrollPane getSScrollPane1() {
		if (scrl1 == null) {
			txtaFileCheckNotExistWarning = new STextArea();
			scrl1 = new SScrollPane(txtaFileCheckNotExistWarning);
			scrl1.setBounds(111, 138, 415, 80);
		}
		return scrl1;
	}

	private SLabel getSLabel1() {
		if (l1 == null) {
			l1 = new SLabel("\u63d0\u793a\u4fe1\u606f");
			l1.setBounds(36, 138, 69, 14);
		}
		return l1;
	}

	private SLabel getSLabel2() {
		if (l2 == null) {
			l2 = new SLabel("\u63d0\u793a\u4fe1\u606f");
			l2.setBounds(36, 262, 69, 14);
		}
		return l2;
	}

	private void addPupuMenu() {
		try {
			final JPopupMenu ppmenu = new JPopupMenu();
			txtaFileCheckExistWarning.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger())
						ppmenu.show(e.getComponent(), e.getX(), e.getY());
				}
			});

			final SMenuItem itemsAddFileName = new SMenuItem("添加文件名称", ImageContext.FileName);
			itemsAddFileName.addActionListener(new ActionListener() {// 浮动菜单
						// 退出按钮事件
						public void actionPerformed(ActionEvent arg0) {
							UtilString.addString(txtaFileCheckExistWarning, txtFileCheckName.getText());
						}
					});
			ppmenu.add(itemsAddFileName);
			final SMenuItem itemsAddFilpath = new SMenuItem("添加文件路径", ImageContext.FilePath);
			itemsAddFilpath.addActionListener(new ActionListener() {// 浮动菜单
						// 退出按钮事件
						public void actionPerformed(ActionEvent arg0) {
							UtilString.addString(txtaFileCheckExistWarning, txtFileCheckDir.getText() + txtFileCheckName.getText());
						}
					});
			ppmenu.add(itemsAddFilpath);

			final JPopupMenu ppmenu1 = new JPopupMenu();
			txtaFileCheckNotExistWarning.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger())
						ppmenu1.show(e.getComponent(), e.getX(), e.getY());
				}
			});

			final SMenuItem itemsAddFileName1 = new SMenuItem("添加文件名称", ImageContext.FileName);
			itemsAddFileName1.addActionListener(new ActionListener() {// 浮动菜单
						// 退出按钮事件
						public void actionPerformed(ActionEvent arg0) {
							UtilString.addString(txtaFileCheckNotExistWarning, txtFileCheckName.getText());
						}
					});
			ppmenu1.add(itemsAddFileName1);
			final SMenuItem itemsAddFilpath1 = new SMenuItem("添加文件路径", ImageContext.FilePath);
			itemsAddFilpath1.addActionListener(new ActionListener() {// 浮动菜单
						// 退出按钮事件
						public void actionPerformed(ActionEvent arg0) {
							UtilString.addString(txtaFileCheckNotExistWarning, txtFileCheckDir.getText() + txtFileCheckName.getText());
						}
					});
			ppmenu1.add(itemsAddFilpath1);
		} catch (Exception e) {
			Log.logError("文件检查面板添加浮动菜单错误:", e);
		} finally {
		}
	}
}
