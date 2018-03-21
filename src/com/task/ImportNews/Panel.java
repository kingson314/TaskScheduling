package com.task.ImportNews;

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
	private JLabel lDelSql;
	private JTextField txtDbName;
	private JButton btnDbName;
	private JLabel lSql;
	private JScrollPane scrlDel;
	private JTextArea txtaDelsql;
	private JTextField tfFilePath;
	private JTextField tfSeparate;
	private JLabel lblSeparate;
	private JLabel lblFilePath;
	private JButton btnFilePath;
	private JScrollPane scrlInsert;
	private JTextArea txtaInsertSql;

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
				lDbName = new JLabel();
				pnl.add(lDbName);
				lDbName.setText("\u6570\u636e\u5e93\u8fde\u63a5");
				lDbName.setBounds(21, 88, 70, 14);
				lDbName.setFont(Const.tfont);
			}
			{
				lDelSql = new JLabel();
				pnl.add(lDelSql);
				lDelSql.setText("\u5220\u9664SQL");
				lDelSql.setBounds(21, 115, 70, 14);
				lDelSql.setFont(Const.tfont);
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
				lSql = new JLabel();
				pnl.add(lSql);
				lSql.setText("\u63d2\u5165SQL");
				lSql.setBounds(21, 221, 67, 14);
				lSql.setFont(Const.tfont);
			}
			{
				scrlDel = new JScrollPane();
				pnl.add(scrlDel, "bottom");
				scrlDel.setBounds(103, 115, 429, 95);
				{
					txtaDelsql = new JTextArea();
					scrlDel.setViewportView(txtaDelsql);
					txtaDelsql.setFont(Const.tfont);
					txtaDelsql.setLineWrap(true);
				}
			}
			{
				scrlInsert = new JScrollPane();
				pnl.add(scrlInsert, "bottom");
				scrlInsert.setBounds(103, 221, 429, 95);
				{
					txtaInsertSql = new JTextArea();
					scrlInsert.setViewportView(txtaInsertSql);
					txtaInsertSql.setFont(Const.tfont);
					txtaInsertSql.setLineWrap(true);
				}
			}

			pnl.add(getBtnFilePath());
			pnl.add(getLblSeparate());
			pnl.add(getTfSeparate());
			pnl.add(getTfFilePath());
			pnl.add(getLblFilePath());
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
			bean.setFilePath(UtilString.isNil(tfFilePath.getText()));
			bean.setSeparate(UtilString.isNil(tfSeparate.getText()));
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setDelSql(UtilString.isNil(txtaDelsql.getText()));
			bean.setInsertSql(UtilString.isNil(txtaInsertSql.getText()));
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
			Bean dtObj = (Bean) JSONObject.toBean(JSONObject.fromObject(task.getJsonStr()), Bean.class);
			tfFilePath.setText(dtObj.getFilePath());
			tfSeparate.setText(dtObj.getSeparate());
			txtDbName.setText(dtObj.getDbName());
			txtaDelsql.setText(dtObj.getDelSql());
			txtaInsertSql.setText(dtObj.getInsertSql());
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

	private JButton getBtnFilePath() {
		if (btnFilePath == null) {
			btnFilePath = new JButton();
			btnFilePath.setText("..");
			btnFilePath.setBounds(538, 29, 20, 21);
			btnFilePath.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnFilePath();
				}
			});
		}
		return btnFilePath;
	}

	private JLabel getLblFilePath() {
		if (lblFilePath == null) {
			lblFilePath = new JLabel();
			lblFilePath.setText("\u6587\u4ef6\u8def\u5f84");
			lblFilePath.setFont(Const.tfont);
			lblFilePath.setBounds(21, 36, 60, 14);
		}
		return lblFilePath;
	}

	private JLabel getLblSeparate() {
		if (lblSeparate == null) {
			lblSeparate = new JLabel();
			lblSeparate.setText("\u5b57\u6bb5\u95f4\u9694\u7b26");
			lblSeparate.setFont(Const.tfont);
			lblSeparate.setBounds(21, 62, 70, 14);
		}
		return lblSeparate;
	}

	private JTextField getTfSeparate() {
		if (tfSeparate == null) {
			tfSeparate = new JTextField();
			tfSeparate.setFont(Const.tfont);
			tfSeparate.setBounds(103, 55, 113, 21);
		}
		return tfSeparate;
	}

	private void btnFilePath() {
		try {
			JFileChooser file = new JFileChooser();
			file.setDialogTitle("选择文件路径");
			file.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int val = file.showOpenDialog(this);
			if (val == JFileChooser.APPROVE_OPTION) {
				tfFilePath.setText(file.getSelectedFile().getAbsolutePath());
			}
		} catch (Exception e) {
			Log.logError("获取文件路径错误:", e);
		} finally {
		}
	}

	private JTextField getTfFilePath() {
		if (tfFilePath == null) {
			tfFilePath = new JTextField();
			tfFilePath.setBounds(103, 29, 429, 21);
			tfFilePath.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2)
						btnFilePath();
				}
			});
		}
		return tfFilePath;
	}

}
