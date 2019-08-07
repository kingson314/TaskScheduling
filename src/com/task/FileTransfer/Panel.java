package com.task.FileTransfer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.app.AppFun;
import com.app.Parser;
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
import common.util.file.UtilFile;
import common.util.json.UtilJson;
import consts.Const;

public class Panel implements ITaskPanel {
	private JPanel pnlFile;
	private JPanel pnlPlugin;
	private SCheckBox chkEnableplugin;
	private SLabel l15;
	private STextField txtConsoleparm;
	private SLabel l16;
	private STextField txtPluginname;
	private SLabel l17;
	private STextField txtPluginpath;
	private SButton btnPptah;
	private SButton btnPpname;
	private JPanel pnlFiledest;
	private SLabel l11;
	private SComboBox cmbDesfiletype;
	private SLabel l12;
	private STextField txtDestfilepath;
	private SLabel l13;
	private STextField txtFiledest;
	private SLabel l14;
	private STextField txtDestfilename;
	private SButton btnDestpath;
	private SButton btnDestname;
	private JPanel pnlFilescr;
	private SLabel l8;
	private SCheckBox chkIsCreateOkFile;
	private SComboBox cmbScrfiletype;
	private SLabel l9;
	private STextField txtSrcfilepath;
	private SCheckBox chkDelsrcfile;
	private SLabel lScrfile;
	private STextField txtScrfile;
	private SLabel l10;
	private STextField txtSrcfilename;
	private SButton btnScrpath;
	private SButton btnScrname;
	private JPanel pnlTaskOption;
	private SCheckBox chkOverwite;
	private SCheckBox chkLocked;

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
		// this.setSize(615, 437);
		// this.add(getPanel());

	}

	public JPanel getPanel() {

		pnlFile = new JPanel();
		try {
			// getContentPane().add(jPanelFile);
			pnlFile.setBounds(0, 0, 623, 480);
			pnlFile.setLayout(null);
			pnlFile.setPreferredSize(new java.awt.Dimension(607, 407));
			{
				pnlTaskOption = new JPanel();
				pnlFile.add(pnlTaskOption);
				pnlTaskOption.setLayout(null);
				pnlTaskOption.setBorder(BorderFactory.createTitledBorder(null, "\u4efb\u52a1\u9009\u9879", TitledBorder.LEADING, TitledBorder.TOP, Const.tfont));
				pnlTaskOption.setBounds(0, 0, 615, 55);
				{
					chkOverwite = new SCheckBox("\u662f\u5426\u8986\u76d6\u4e0a\u6b21\u62f7\u8d1d\u6587\u4ef6");
					pnlTaskOption.add(chkOverwite);
					chkOverwite.setBounds(310, 20, 146, 18);
				}
				{
					chkLocked = new SCheckBox("\u662f\u5426\u9501\u5b9a\u6587\u4ef6");
					pnlTaskOption.add(chkLocked);
					chkLocked.setBounds(30, 20, 121, 18);
				}
			}

			{
				pnlPlugin = new JPanel();
				pnlFile.add(pnlPlugin);
				pnlPlugin.setLayout(null);
				pnlPlugin.setBounds(0, 276, 615, 137);
				pnlPlugin.setBorder(BorderFactory.createTitledBorder(null, "\u5916\u90e8\u7a0b\u5e8f", TitledBorder.LEADING, TitledBorder.TOP, Const.tfont));
				{
					chkEnableplugin = new SCheckBox("\u662f\u5426\u8c03\u7528\u5916\u90e8\u7a0b\u5e8f");
					pnlPlugin.add(chkEnableplugin, "Center");
					chkEnableplugin.setBounds(30, 21, 131, 25);
				}
				{
					l15 = new SLabel("\u547d\u4ee4\u53c2\u6570");
					pnlPlugin.add(l15, "North");
					l15.setBounds(30, 52, 48, 19);
				}
				{
					txtConsoleparm = new STextField();
					pnlPlugin.add(txtConsoleparm, "West");
					txtConsoleparm.setBounds(107, 49, 419, 21);
				}
				{
					l16 = new SLabel("\u7a0b\u5e8f\u540d\u79f0");
					pnlPlugin.add(l16, "East");
					l16.setBounds(30, 81, 48, 19);
				}
				{
					txtPluginname = new STextField();
					pnlPlugin.add(txtPluginname, "South");
					txtPluginname.setBounds(107, 79, 419, 21);
					txtPluginname.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							// if (checkClickTime())// 双击判断
							if (e.getClickCount() == 2)
								plugbtn();// mouseDoubleClick(TextField_pluginname);
						}
					});
				}
				{
					l17 = new SLabel("\u7a0b\u5e8f\u76ee\u5f55");
					pnlPlugin.add(l17, "South");
					l17.setBounds(31, 110, 48, 19);
				}
				{
					txtPluginpath = new STextField();
					pnlPlugin.add(txtPluginpath, "South");
					txtPluginpath.setBounds(107, 107, 419, 21);
					txtPluginpath.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							// if (checkClickTime())// 双击判断
							if (e.getClickCount() == 2)
								plugPathbtn();// mouseDoubleClick(TextField_pluginpath);
						}
					});
				}
				{
					btnPptah = new SButton("..");
					pnlPlugin.add(btnPptah, "South");
					btnPptah.setBounds(537, 107, 22, 21);
					btnPptah.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							plugPathbtn();
						}
					});
				}
				{
					btnPpname = new SButton("..");
					pnlPlugin.add(btnPpname, "South");
					btnPpname.setBounds(537, 79, 22, 21);
					btnPpname.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							plugbtn();
						}
					});
				}
			}
			{
				pnlFiledest = new JPanel();
				pnlFile.add(pnlFiledest);
				pnlFiledest.setLayout(null);
				pnlFiledest.setBounds(0, 162, 615, 112);
				pnlFiledest.setBorder(BorderFactory.createTitledBorder(null, "\u76ee\u7684\u6587\u4ef6\u9009\u9879", TitledBorder.LEADING, TitledBorder.TOP, Const.tfont));
				{
					l11 = new SLabel("\u76ee\u7684\u6587\u4ef6\u7c7b\u578b");
					pnlFiledest.add(l11);
					l11.setBounds(30, 26, 81, 14);
				}
				{
					cmbDesfiletype = new SComboBox(new String[] { "FILE", "FTP" });
					pnlFiledest.add(cmbDesfiletype);
					cmbDesfiletype.setBounds(109, 19, 100, 21);
				}
				{
					l12 = new SLabel("\u76ee\u7684\u6587\u4ef6\u76ee\u5f55");
					pnlFiledest.add(l12);
					l12.setBounds(30, 52, 81, 14);
				}
				{
					txtDestfilepath = new STextField();
					pnlFiledest.add(txtDestfilepath);
					txtDestfilepath.setBounds(109, 45, 420, 21);
					txtDestfilepath.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							// if (checkClickTime())// 双击判断
							if (e.getClickCount() == 2)
								destPathbtn();// mouseDoubleClick(TextField_destfilepath);
						}
					});
				}
				{
					l13 = new SLabel("\u6587\u4ef6\u76ee\u7684");
					pnlFiledest.add(l13);
					l13.setBounds(310, 26, 70, 14);
					l13.setVisible(false);
				}
				{
					txtFiledest = new STextField();
					pnlFiledest.add(txtFiledest);
					txtFiledest.setBounds(387, 19, 100, 21);
					txtFiledest.setSize(100, 21);
					txtFiledest.setVisible(false);
				}
				{
					l14 = new SLabel("\u76ee\u7684\u6587\u4ef6\u540d\u79f0");
					pnlFiledest.add(l14);
					l14.setBounds(30, 78, 76, 14);
				}
				{
					txtDestfilename = new STextField();
					pnlFiledest.add(txtDestfilename);
					txtDestfilename.setBounds(109, 71, 420, 21);
					txtDestfilename.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							// if (checkClickTime())// 双击判断
							if (e.getClickCount() == 2)
								destbtn();// mouseDoubleClick(TextField_destfilename);
						}
					});
				}
				{
					btnDestpath = new SButton("..");
					pnlFiledest.add(btnDestpath);
					btnDestpath.setBounds(537, 45, 22, 21);
					btnDestpath.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							destPathbtn();
						}
					});
				}
				{
					btnDestname = new SButton("..");
					pnlFiledest.add(btnDestname);
					pnlFiledest.add(getJchk_IsCreateOkFile());
					btnDestname.setBounds(537, 71, 22, 21);
					btnDestname.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							destbtn();
						}
					});
				}
			}
			{
				pnlFilescr = new JPanel();
				pnlFile.add(pnlFilescr);
				pnlFilescr.setLayout(null);
				pnlFilescr.setBounds(0, 56, 615, 106);
				pnlFilescr.setBorder(BorderFactory.createTitledBorder(null, "\u6e90\u6587\u4ef6\u4fe1\u606f", TitledBorder.LEADING, TitledBorder.TOP, Const.tfont));
				{
					l8 = new SLabel("\u6e90\u6587\u4ef6\u7c7b\u578b");
					pnlFilescr.add(l8);
					l8.setBounds(30, 23, 67, 14);
				}
				{
					chkDelsrcfile = new SCheckBox("\u662f\u5426\u5220\u9664\u6e90\u6587\u4ef6");
					pnlFilescr.add(chkDelsrcfile);
					chkDelsrcfile.setBounds(310, 23, 143, 14);
				}
				{
					cmbScrfiletype = new SComboBox(new String[] { "FILE", "FTP" });
					pnlFilescr.add(cmbScrfiletype);
					cmbScrfiletype.setBounds(109, 16, 100, 21);
					cmbScrfiletype.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent evt) {
							if (cmbScrfiletype.getSelectedItem().toString().trim().equalsIgnoreCase("ftp"))
								enableDeleteSrcFile(false);
							else {
								enableDeleteSrcFile(true);
							}
						}
					});
					if (cmbScrfiletype.getSelectedItem().toString().trim().equalsIgnoreCase("ftp"))
						enableDeleteSrcFile(false);
					else {
						enableDeleteSrcFile(true);
					}
				}
				{
					l9 = new SLabel("\u6e90\u6587\u4ef6\u76ee\u5f55");
					pnlFilescr.add(l9);
					l9.setBounds(30, 48, 73, 14);
				}
				{
					txtSrcfilepath = new STextField();
					pnlFilescr.add(txtSrcfilepath);
					txtSrcfilepath.setBounds(109, 41, 422, 21);
					txtSrcfilepath.setSize(420, 21);
					txtSrcfilepath.addMouseListener(new MouseAdapter() {
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
					txtScrfile.setBounds(387, 16, 10, 21);
					txtScrfile.setSize(100, 21);
					txtScrfile.setVisible(false);
				}
				{
					l10 = new SLabel("\u6e90\u6587\u4ef6\u540d\u79f0");
					pnlFilescr.add(l10);
					l10.setBounds(30, 74, 73, 14);
				}
				{
					txtSrcfilename = new STextField();
					pnlFilescr.add(txtSrcfilename);
					txtSrcfilename.setBounds(109, 67, 420, 21);
					txtSrcfilename.addMouseListener(new MouseAdapter() {
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

		} catch (Exception e) {
			Log.logError("文件分发面板初始化面板错误:", e);
			return pnlFile;
		} finally {
		}
		return pnlFile;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getSrcFilePath())) {
			ShowMsg.showWarn("源文件目录不能为空");
			return false;
		}
		if ("".equals(bean.getDestFilePath())) {
			ShowMsg.showWarn("目的文件目录不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setLocked(chkLocked.isSelected());
			bean.setOverwrite(chkOverwite.isSelected());
			bean.setDelSrcFile(chkDelsrcfile.isSelected());
			bean.setSrcFileType(cmbScrfiletype.getSelectedItem().toString());
			bean.setSrcFilePath(UtilFile.formatFilePath(txtSrcfilepath.getText().trim()));
			bean.setSrcFileName(txtSrcfilename.getText().trim());
			bean.setDestFileType(cmbDesfiletype.getSelectedItem().toString());
			bean.setDestFilePath(UtilFile.formatFilePath(txtDestfilepath.getText().trim()));
			if (txtSrcfilename.getText().trim().indexOf("*") >= 0) {
				ShowMsg.showMsg("源文件为过滤类型，目的文件将赋值为空");
				txtDestfilename.setText("");
			}

			bean.setDestFileName(txtDestfilename.getText().trim());

			bean.setEnablePlugin(chkEnableplugin.isSelected());
			bean.setConsoleParam(txtConsoleparm.getText().trim());
			bean.setPluginName(txtPluginname.getText().trim());
			bean.setPluginPath(UtilFile.formatFilePath(txtPluginpath.getText().trim()));
			bean.setIfCreateOkFile(chkIsCreateOkFile.isSelected());

			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			// Log.logError("文件分发面板赋值任务对象错误:", e);
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
			if (bean.getLocked() == null)
				chkLocked.setSelected(false);
			else
				chkLocked.setSelected(bean.getLocked() == true ? true : false);

			if (bean.getOverwrite() != null)
				chkOverwite.setSelected(bean.getOverwrite());

			if (bean.getDelSrcFile() == null)
				chkDelsrcfile.setSelected(false);
			else
				chkDelsrcfile.setSelected(bean.getDelSrcFile() == true ? true : false);

			cmbScrfiletype.setSelectedItem((bean.getSrcFileType() == null ? "" : bean.getSrcFileType()));
			txtSrcfilepath.setText(Parser.removeSlash(bean.getSrcFilePath()) == null ? "" : Parser.removeSlash(bean.getSrcFilePath()));
			txtSrcfilename.setText(bean.getSrcFileName() == null ? "" : bean.getSrcFileName());

			cmbDesfiletype.setSelectedItem(bean.getDestFileType() == null ? "" : bean.getDestFileType());
			txtDestfilepath.setText(Parser.removeSlash(bean.getDestFilePath()) == null ? "" : Parser.removeSlash(bean.getDestFilePath()));
			txtDestfilename.setText(bean.getDestFileName() == null ? "" : bean.getDestFileName());

			if (bean.getEnablePlugin() == null)
				chkEnableplugin.setSelected(false);
			else
				chkEnableplugin.setSelected(bean.getEnablePlugin() == true ? true : false);
			txtConsoleparm.setText(Parser.removeSlash(bean.getConsoleParam()) == null ? "" : Parser.removeSlash(bean.getConsoleParam()));
			txtPluginname.setText(bean.getPluginName() == null ? "" : bean.getPluginName());
			txtPluginpath.setText(Parser.removeSlash(bean.getPluginPath()) == null ? "" : Parser.removeSlash(bean.getPluginPath()));

			if (bean.getIfCreateOkFile() == null)
				chkIsCreateOkFile.setSelected(false);
			else
				chkIsCreateOkFile.setSelected(bean.getIfCreateOkFile() == true ? true : false);
		} catch (Exception e) {
			Log.logError("文件分发面板填充控件错误:", e);
		} finally {
		}
	}

	private void scrbtn() {
		mouseDoubleClick(txtSrcfilename);
	}

	private void scrPathbtn() {
		mouseDoubleClick(txtSrcfilepath);
	}

	private void destbtn() {
		mouseDoubleClick(txtDestfilename);
	}

	private void destPathbtn() {
		mouseDoubleClick(txtDestfilepath);
	}

	private void plugPathbtn() {
		mouseDoubleClick(txtPluginpath);
	}

	private void plugbtn() {
		mouseDoubleClick(txtPluginname);
	}

	private void mouseDoubleClick(STextField TextField) {
		try {
			if (cmbScrfiletype.getSelectedIndex() == 0) {
				if (TextField.equals(txtSrcfilepath)) {
					txtSrcfilepath.setText(ShowDialog.openDir());
				} else if (TextField.equals(txtSrcfilename)) {
					String[] path = ShowDialog.openFileForPathAndFile();
					txtSrcfilepath.setText(path[0]);
					txtSrcfilename.setText(path[1]);
				}
			} else if (cmbScrfiletype.getSelectedIndex() == 1 && TextField.equals(txtSrcfilepath)) {
				AppFun.getftp(txtSrcfilepath);
			}

			if (cmbDesfiletype.getSelectedIndex() == 0) {
				if (TextField.equals(txtDestfilepath)) {
					txtDestfilepath.setText(ShowDialog.openDir());
				} else if (TextField.equals(txtDestfilename)) {
					String[] path = ShowDialog.openFileForPathAndFile();
					txtDestfilepath.setText(path[0]);
					txtDestfilename.setText(path[1]);
				}
			} else if (cmbDesfiletype.getSelectedIndex() == 1 && TextField.equals(txtDestfilepath)) {
				AppFun.getftp(txtDestfilepath);

			}
			if (TextField.equals(txtPluginpath)) {
				txtPluginpath.setText(ShowDialog.openDir());

			} else if (TextField.equals(txtPluginname)) {
				String[] path = ShowDialog.openFileForPathAndFile();
				txtPluginpath.setText(path[0]);
				txtPluginname.setText(path[1]);
			}
		} catch (Exception e) {
			Log.logError("文件分发面板获取路径错误:", e);
		} finally {
		}
	}

	private SCheckBox getJchk_IsCreateOkFile() {
		if (chkIsCreateOkFile == null) {
			chkIsCreateOkFile = new SCheckBox("\u662f\u5426\u521b\u5efaOK\u6587\u4ef6");
			chkIsCreateOkFile.setBounds(310, 22, 154, 18);
		}
		return chkIsCreateOkFile;
	}

	private void enableDeleteSrcFile(boolean enable) {
		chkDelsrcfile.setEnabled(enable);
		if (!chkDelsrcfile.isEnabled())
			chkDelsrcfile.setSelected(false);

	}
}
