package com.task.ImportPrice;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.json.JSONObject;

import com.app.AppFun;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.SCheckBox;
import common.component.SComboBox;
import common.component.SLabel;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.string.UtilString;
import consts.Const;

public class Panel extends JDialog implements ITaskPanel {

	private static final long serialVersionUID = 1L;
	private JPanel pnl;
	private SLabel lDbName;
	private JTextField txtDbName;
	private JButton btnDbName;
	private STextField txtFilePath;
	private SLabel lFilePath;
	private JButton btnFilePath;
	private SComboBox cmbSymbol;
	private SLabel lSymbol;
	private SCheckBox chkPeriod1;
	private SCheckBox chkPeriod5;
	private SCheckBox chkPeriod15;
	private SCheckBox chkPeriod30;
	private SCheckBox chkPeriod60;
	private SCheckBox chkPeriod240;
	private SCheckBox chkPeriod1440;
	private SCheckBox chkDetail;


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
		pnl = new JPanel();
		try {
			getContentPane().add(pnl);
			pnl.setLayout(null);
			pnl.setBounds(-13, 13, 626, 43);
			pnl.setBorder(BorderFactory.createTitledBorder(""));
			pnl.setPreferredSize(new java.awt.Dimension(668, 482));
			pnl.setSize(429, 95);
			{
				lFilePath = new SLabel("\u6587\u4ef6\u8def\u5f84");
				pnl.add(lFilePath);
				lFilePath.setFont(Const.tfont);
				lFilePath.setBounds(21, 50, 60, 21);
			}
			{
				txtFilePath = new STextField();
				pnl.add(txtFilePath);
				txtFilePath.setBounds(103, 50, 429, 21);
				txtFilePath.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnFilePath();
					}
				});
			}
			{
				btnFilePath = new JButton();
				pnl.add(btnFilePath);
				btnFilePath.setText("..");
				btnFilePath.setBounds(538, 50, 20, 21);
				btnFilePath.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnFilePath();
					}
				});
			}

			{
				lDbName = new SLabel("\u6570\u636e\u5e93\u8fde\u63a5");
				pnl.add(lDbName);
				lDbName.setBounds(21, 80, 70, 21);
				lDbName.setFont(Const.tfont);
			}

			{
				txtDbName = new STextField();
				pnl.add(txtDbName);
				txtDbName.setBounds(103, 80, 429, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnDbName();
					}
				});
			}
			{
				btnDbName = new JButton();
				pnl.add(btnDbName);
				btnDbName.setText("..");
				btnDbName.setBounds(538, 80, 22, 21);
				btnDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDbName();
					}
				});
			}

			{
				lSymbol = new SLabel("货币名称");
				pnl.add(lSymbol);
				lSymbol.setBounds(21, 110, 70, 21);
				lSymbol.setFont(Const.tfont);
			}

			{
				cmbSymbol = new SComboBox(Const.SymbolArr);
				pnl.add(cmbSymbol);
				cmbSymbol.setBounds(103, 110, 429, 21);
			}
			{
				chkPeriod1 = new SCheckBox("1分钟");
				pnl.add(chkPeriod1);
				chkPeriod1.setBounds(103, 140, 100, 21);
			}
			{
				chkPeriod5 = new SCheckBox("5分钟");
				pnl.add(chkPeriod5);
				chkPeriod5.setBounds(250, 140, 100, 21);
				chkPeriod5.setSelected(true);
			}
			{
				chkPeriod15 = new SCheckBox("15分钟");
				pnl.add(chkPeriod15);
				chkPeriod15.setBounds(400, 140, 100, 21);
			}
			{
				chkPeriod30 = new SCheckBox("30分钟");
				pnl.add(chkPeriod30);
				chkPeriod30.setBounds(103, 170, 100, 21);
			}
			{
				chkPeriod60 = new SCheckBox("60分钟");
				pnl.add(chkPeriod60);
				chkPeriod60.setBounds(250, 170, 100, 21);
				chkPeriod60.setSelected(true);
			}
			{
				chkPeriod240 = new SCheckBox("240分钟");
				pnl.add(chkPeriod240);
				chkPeriod240.setBounds(400, 170, 250, 21);
			}
			{
				chkPeriod1440 = new SCheckBox("1440分钟");
				pnl.add(chkPeriod1440);
				chkPeriod1440.setBounds(103, 200, 100, 21);
			}
			{
				chkDetail= new SCheckBox("明细表");
				pnl.add(chkDetail);
				chkDetail.setBounds(250, 200, 100, 21);
			}
		} catch (Exception e) {
			Log.logError("面板构造错误:", e);
			return pnl;
		} finally {
		}
		return pnl;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getDbName())) {
			ShowMsg.showWarn("数据源连接不能为空");
			return false;
		} else if ("".equals(bean.getFilePath())) {
			ShowMsg.showWarn("文件路径不能为空");
			return false;
		} else if ("".equals(bean.getSymbol()) ||"全部".equals(bean.getSymbol())) {
			ShowMsg.showWarn("货币名称不能为空");
			return false;
		}
		return true;
	}

	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setFilePath(UtilString.isNil(txtFilePath.getText()));
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setSymbol(UtilString.isNil(cmbSymbol.getSelectedItem().toString()));
			bean.setPeriod1(chkPeriod1.isSelected());
			bean.setPeriod5(chkPeriod5.isSelected());
			bean.setPeriod15(chkPeriod15.isSelected());
			bean.setPeriod30(chkPeriod30.isSelected());
			bean.setPeriod60(chkPeriod60.isSelected());
			bean.setPeriod240(chkPeriod240.isSelected());
			bean.setPeriod1440(chkPeriod1440.isSelected());
			bean.setDetail(chkDetail.isSelected());
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
			cmbSymbol.setSelectedItem(bean.getSymbol());
			chkPeriod1.setSelected(bean.isPeriod1());
			chkPeriod5.setSelected(bean.isPeriod5());
			chkPeriod15.setSelected(bean.isPeriod15());
			chkPeriod30.setSelected(bean.isPeriod30());
			chkPeriod60.setSelected(bean.isPeriod60());
			chkPeriod240.setSelected(bean.isPeriod240());
			chkPeriod1440.setSelected(bean.isPeriod1440());
			chkDetail.setSelected(bean.isDetail());
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
