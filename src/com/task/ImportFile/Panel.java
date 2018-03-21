package com.task.ImportFile;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.sf.json.JSONObject;

import com.app.AppFun;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.ShowMsg;
import common.util.string.UtilString;
import consts.Const;

public class Panel extends JDialog implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnl;
	private JLabel lDbName;
	private JLabel lUpdateSql;
	private JTextField txtDbName;
	private JScrollPane scrlKey;
	private JTextArea txtaKeySql;
	private JLabel lKeySql;
	private JButton btnDbName;
	private JLabel lInsertSql;
	private JScrollPane scrlUpdate;
	private JTextArea txtaUpdateSql;
	private JTextField txtFilePath;
	private JTextField txtSeparate;
	private JLabel lSeparate;
	private JLabel lFilePath;
	private JButton btnFilePath;
	private JScrollPane scrlInsert;
	private JTextArea txtaInsertSql;
	private JTextField txtKeyColumnIndex;
	private JLabel lKeyColumnIndex;

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
				lFilePath = new JLabel();
				lFilePath.setText("文件路径");
				lFilePath.setFont(Const.tfont);
				lFilePath.setBounds(21, 36, 60, 14);
				pnl.add(lFilePath);
			}
			{
				txtFilePath = new JTextField();
				txtFilePath.setBounds(103, 29, 429, 21);
				txtFilePath.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnFilePath();
					}
				});
				pnl.add(txtFilePath);
			}
			{
				btnFilePath = new JButton();
				btnFilePath.setText("..");
				btnFilePath.setBounds(538, 29, 20, 21);
				btnFilePath.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnFilePath();
					}
				});
				pnl.add(btnFilePath);
			}
			{
				lSeparate = new JLabel();
				lSeparate.setText("字段间隔符");
				lSeparate.setFont(Const.tfont);
				lSeparate.setBounds(21, 62, 70, 14);
				pnl.add(lSeparate);
			}
			{
				txtSeparate = new JTextField();
				txtSeparate.setFont(Const.tfont);
				txtSeparate.setBounds(103, 55, 70, 21);
				pnl.add(txtSeparate);
			}

			{
				lKeyColumnIndex = new JLabel();
				lKeyColumnIndex.setText("关键字对应列");
				lKeyColumnIndex.setFont(Const.tfont);
				lKeyColumnIndex.setBounds(211, 62, 75, 14);
				pnl.add(lKeyColumnIndex);
			}
			{
				txtKeyColumnIndex = new JTextField();
				txtKeyColumnIndex.setFont(Const.tfont);
				txtKeyColumnIndex.setBounds(291, 55, 100, 21);
				pnl.add(txtKeyColumnIndex);
			}
			{
				lDbName = new JLabel();
				pnl.add(lDbName);
				lDbName.setText("数据库连接");
				lDbName.setBounds(21, 88, 70, 14);
				lDbName.setFont(Const.tfont);
			}
			{
				lKeySql = new JLabel();
				lKeySql.setText("\u4e3b\u952e\u5224\u65adSQL");
				lKeySql.setBounds(21, 112, 90, 17);
				lKeySql.setFont(Const.tfont);
				pnl.add(lKeySql);
			}
			{
				txtaKeySql = new JTextArea();
				txtaKeySql.setFont(Const.tfont);
				txtaKeySql.setLineWrap(true);
			}
			{
				scrlKey = new JScrollPane();
				scrlKey.setBounds(103, 112, 429, 58);
				scrlKey.setViewportView(txtaKeySql);
				pnl.add(scrlKey, "bottom");
			}
			{
				lUpdateSql = new JLabel();
				pnl.add(lUpdateSql);
				lUpdateSql.setText("更新SQL");
				lUpdateSql.setBounds(21, 181, 70, 14);
				lUpdateSql.setFont(Const.tfont);
			}
			{
				txtDbName = new JTextField();
				pnl.add(txtDbName);
				txtDbName.setBounds(103, 81, 429, 21);
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
				btnDbName.setBounds(538, 81, 22, 21);
				btnDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDbName();
					}
				});
			}
			{
				lInsertSql = new JLabel();
				pnl.add(lInsertSql);
				lInsertSql.setText("插入SQL");
				lInsertSql.setBounds(21, 288, 67, 14);
				lInsertSql.setFont(Const.tfont);
			}
			{
				scrlUpdate = new JScrollPane();
				pnl.add(scrlUpdate, "bottom");
				scrlUpdate.setBounds(103, 181, 429, 95);
				{
					txtaUpdateSql = new JTextArea();
					scrlUpdate.setViewportView(txtaUpdateSql);
					txtaUpdateSql.setFont(Const.tfont);
					txtaUpdateSql.setLineWrap(true);
					txtaUpdateSql.setPreferredSize(new java.awt.Dimension(426, 92));
				}
			}
			{
				scrlInsert = new JScrollPane();
				pnl.add(scrlInsert, "bottom");
				scrlInsert.setBounds(103, 288, 429, 95);
				{
					txtaInsertSql = new JTextArea();
					scrlInsert.setViewportView(txtaInsertSql);
					txtaInsertSql.setFont(Const.tfont);
					txtaInsertSql.setLineWrap(true);
				}
			}

		} catch (Exception e) {
			Log.logError("数据表检查面板构造错误:", e);
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
		} else if ("".equals(bean.getInsertSql())) {
			ShowMsg.showWarn("插入SQL不能为空");
			return false;
		} else if ("".equals(bean.getSeparate())) {
			ShowMsg.showWarn("分割字符串不能为空");
			return false;
		}
		return true;
	}

	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setFilePath(UtilString.isNil(txtFilePath.getText()));
			bean.setSeparate(UtilString.isNil(txtSeparate.getText()));
			bean.setKeySql(UtilString.isNil(txtaKeySql.getText()));
			bean.setKeyColumnIndex(UtilString.isNil(txtKeyColumnIndex.getText(), "0"));
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setUpdateSql(UtilString.isNil(txtaUpdateSql.getText()));
			bean.setInsertSql(UtilString.isNil(txtaInsertSql.getText()));
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(JSONObject.fromObject(bean).toString());
		} catch (Exception e) {
			Log.logError("数据表检查面板赋值任务对象错误:", e);
			return false;
		} finally {
		}
		return true;
	}

	public void fillComp(ITask task) {
		try {
			Bean bean = (Bean) JSONObject.toBean(JSONObject.fromObject(task.getJsonStr()), Bean.class);
			txtFilePath.setText(bean.getFilePath());
			txtSeparate.setText(bean.getSeparate());
			txtaKeySql.setText(bean.getKeySql());
			txtKeyColumnIndex.setText(String.valueOf(bean.getKeyColumnIndex()));
			txtDbName.setText(bean.getDbName());
			txtaUpdateSql.setText(bean.getUpdateSql());
			txtaInsertSql.setText(bean.getInsertSql());
		} catch (Exception e) {
			Log.logError("数据表检查面板填充控件错误:", e);
		} finally {
		}
	}

	private void btnDbName() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("数据表检查面板获取数据源链接错误:", e);
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
