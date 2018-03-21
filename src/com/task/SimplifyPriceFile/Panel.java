package com.task.SimplifyPriceFile;

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

import net.sf.json.JSONObject;

import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.SLabel;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.string.UtilString;
import consts.Const;

public class Panel extends JDialog implements ITaskPanel {

	private static final long serialVersionUID = 1L;
	private JPanel pnl;
	private STextField tfFilePath;
	private STextField tfSeparate;
	private SLabel lSeparate;
	private SLabel lFilePath;
	private JButton btnFilePath;

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

			pnl.add(getBtnFilePath());
			pnl.add(getLblSeparate());
			pnl.add(getTfSeparate());
			pnl.add(getTfFilePath());
			pnl.add(getLblFilePath());
		} catch (Exception e) {
			Log.logError("数据表检查面板构造错误:", e);
			return pnl;
		} finally {
		}
		return pnl;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getFilePath())) {
			ShowMsg.showWarn("文件路径不能为空");
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
			tfFilePath.setText(bean.getFilePath());
			tfSeparate.setText(bean.getSeparate());
		} catch (Exception e) {
			Log.logError("数据表检查面板填充控件错误:", e);
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

	private SLabel getLblFilePath() {
		if (lFilePath == null) {
			lFilePath = new SLabel("\u6587\u4ef6\u8def\u5f84");
			lFilePath.setFont(Const.tfont);
			lFilePath.setBounds(21, 36, 60, 14);
		}
		return lFilePath;
	}

	private SLabel getLblSeparate() {
		if (lSeparate == null) {
			lSeparate = new SLabel("\u5b57\u6bb5\u95f4\u9694\u7b26");
			lSeparate.setFont(Const.tfont);
			lSeparate.setBounds(21, 62, 70, 14);
		}
		return lSeparate;
	}

	private STextField getTfSeparate() {
		if (tfSeparate == null) {
			tfSeparate = new STextField();
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

	private STextField getTfFilePath() {
		if (tfFilePath == null) {
			tfFilePath = new STextField();
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
