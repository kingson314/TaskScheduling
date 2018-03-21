package com.task.ImportExcel;

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
import common.util.json.UtilJson;
import common.util.string.UtilString;

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;

	private JPanel pnlMain;
	private SLabel lFileDir;
	private SLabel lRule;
	private STextField txtFileDir;
	private SButton btnFileDir;
	private SButton btnDbName;
	private SLabel lDbName;
	private SScrollPane scrlDelSql;
	private STextArea txtaDelSql;
	private SLabel lDelSql;
	private SLabel lInsertSql;
	private SScrollPane scrlRule;
	private STextArea txtaRule;
	private SScrollPane scrlInsertSql;
	private STextField txtIgnoreLastLines;
	private SLabel lIgnoreLastLines;
	private STextField txtStartLine;
	private SLabel lStartLine;
	private STextField txtSheetIndex;
	private SLabel lSheetIndex;
	private STextArea txtaInsertSql;
	private STextField txtDbName;

	// public static void main(String[] args) {
	// Panel inis = new Panel();
	// inis.add(inis.getPanel());
	// inis.setBounds(0, 0, 623, 606);
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
		pnlMain = new JPanel();
		try {
			// getContentPane().add(pnlMain);
			pnlMain.setLayout(null);
			pnlMain.setBounds(0, 0, 626, 100);
			pnlMain.setPreferredSize(new java.awt.Dimension(637, 502));
			pnlMain.setSize(429, 95);
			{
				lFileDir = new SLabel("\u6587\u4ef6\u8def\u5f84");
				pnlMain.add(lFileDir);
				lFileDir.setBounds(30, 24, 91, 14);
			}
			{
				lRule = new SLabel("\u6620\u5c04\u89c4\u5219");
				pnlMain.add(lRule);
				lRule.setBounds(31, 224, 90, 14);
			}
			{
				txtFileDir = new STextField();
				pnlMain.add(txtFileDir);
				txtFileDir.setBounds(127, 21, 411, 21);
				txtFileDir.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnFileDir();
					}
				});
			}
			{
				btnFileDir = new SButton("..");
				pnlMain.add(btnFileDir);
				btnFileDir.setBounds(544, 21, 22, 21);
				btnFileDir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnFileDir();
					}
				});
			}
			{
				btnDbName = new SButton("..");
				pnlMain.add(btnDbName);
				btnDbName.setBounds(545, 76, 22, 21);
				btnDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDbName();
					}
				});
			}
			{
				txtDbName = new STextField();
				pnlMain.add(txtDbName);
				txtDbName.setBounds(128, 76, 411, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnDbName();
					}
				});
			}
			{
				lDbName = new SLabel("\u6570\u636e\u5e93\u8fde\u63a5");
				pnlMain.add(lDbName);
				lDbName.setBounds(31, 76, 91, 14);
			}
			{

				txtaDelSql = new STextArea();
				scrlDelSql = new SScrollPane(txtaDelSql);
				pnlMain.add(scrlDelSql, "bottom");
				scrlDelSql.setBounds(127, 103, 412, 95);
			}
			{
				lDelSql = new SLabel("\u6e05\u9664SQL");
				pnlMain.add(lDelSql);
				lDelSql.setBounds(31, 105, 90, 14);
			}

			{
				lInsertSql = new SLabel("\u76ee\u6807SQL");
				pnlMain.add(lInsertSql);
				lInsertSql.setBounds(31, 308, 84, 14);
			}
			{

				{
					txtaRule = new STextArea();
				}
				scrlRule = new SScrollPane(txtaRule);
				pnlMain.add(scrlRule, "bottom");
				scrlRule.setBounds(127, 206, 412, 95);
			}
			{

				pnlMain.add(getLSheetIndex());
				pnlMain.add(getTxtSheetIndex());
				pnlMain.add(getLStartLine());
				pnlMain.add(getTxtStartLine());
				pnlMain.add(getLIgnoreLastLines());
				pnlMain.add(getTxtIgnoreLastLines());

				{
					txtaInsertSql = new STextArea();
				}
				scrlInsertSql = new SScrollPane(txtaInsertSql);
				pnlMain.add(scrlInsertSql, "bottom");
				scrlInsertSql.setBounds(127, 307, 412, 95);
			}
		} catch (Exception e) {
			Log.logError("导入Excel面板初始化面板错误:", e);
			return pnlMain;
		} finally {
		}
		return pnlMain;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getDocDir())) {
			ShowMsg.showWarn("文件路径不能为空");
			return false;
		}
		if ("".equals(bean.getDbName())) {
			ShowMsg.showWarn("目的数据库连接不能为空");
			return false;
		}
		if ("".equals(bean.getDocFieldRule())) {
			ShowMsg.showWarn("映射规则不能为空");
			return false;
		}
		if ("".equals(bean.getInsertSql())) {
			ShowMsg.showWarn("目标SQL不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDocDir(UtilString.isNil(txtFileDir.getText()));
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setDelSql(UtilString.isNil(txtaDelSql.getText()));
			String rule = UtilString.isNil(txtaRule.getText()).trim();
			if (!rule.endsWith(";"))
				rule = rule + ";";
			bean.setDocFieldRule(rule);
			bean.setInsertSql(UtilString.isNil(txtaInsertSql.getText()));
			bean.setDocSheetIndex(Integer.valueOf(UtilString.isNil(txtSheetIndex.getText(), "0")));
			bean.setStartLine(Integer.valueOf(UtilString.isNil(txtStartLine.getText(), "2")));
			bean.setIgnoreLastLines(Integer.valueOf(UtilString.isNil(txtIgnoreLastLines.getText(), "0")));
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			// Log.logError("导入Excel面板赋值任务对象错误:", e);
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
			txtFileDir.setText(UtilString.isNil(bean.getDocDir()));
			txtDbName.setText(UtilString.isNil(bean.getDbName()));
			txtaDelSql.setText(UtilString.isNil(bean.getDelSql()));
			txtaRule.setText(UtilString.isNil(bean.getDocFieldRule()));
			txtaInsertSql.setText(UtilString.isNil(bean.getInsertSql()));
			txtSheetIndex.setText(UtilString.isNil(String.valueOf(bean.getDocSheetIndex()), "0"));
			txtStartLine.setText(UtilString.isNil(String.valueOf(bean.getStartLine()), "2"));
			txtIgnoreLastLines.setText(UtilString.isNil(String.valueOf(bean.getIgnoreLastLines()), "0"));
		} catch (Exception e) {
			Log.logError("导入Excel面板填充控件错误:", e);
		} finally {
		}
	}

	private void btnFileDir() {
		try {
			txtFileDir.setText(ShowDialog.openFile());
		} catch (Exception e) {
			Log.logError("导入Excel面板获取文件路径错误:", e);
		} finally {
		}
	}

	private void btnDbName() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("导入Excel面板获取数据源链接错误:", e);
		} finally {
		}
	}

	private SLabel getLSheetIndex() {
		if (lSheetIndex == null) {
			lSheetIndex = new SLabel("\u6587\u6863\u9875\u7801");
			lSheetIndex.setBounds(128, 54, 52, 14);
		}
		return lSheetIndex;
	}

	private STextField getTxtSheetIndex() {
		if (txtSheetIndex == null) {
			txtSheetIndex = new STextField();
			txtSheetIndex.setText("0");
			txtSheetIndex.setBounds(183, 47, 60, 21);
		}
		return txtSheetIndex;
	}

	private SLabel getLStartLine() {
		if (lStartLine == null) {
			lStartLine = new SLabel("\u5f00\u59cb\u884c\u53f7");
			lStartLine.setBounds(270, 54, 52, 14);
		}
		return lStartLine;
	}

	private STextField getTxtStartLine() {
		if (txtStartLine == null) {
			txtStartLine = new STextField();
			txtStartLine.setText("2");
			txtStartLine.setBounds(325, 47, 60, 21);
		}
		return txtStartLine;
	}

	private SLabel getLIgnoreLastLines() {
		if (lIgnoreLastLines == null) {
			lIgnoreLastLines = new SLabel("\u5ffd\u7565\u5012\u6570N\u884c");
			lIgnoreLastLines.setBounds(408, 54, 74, 14);
		}
		return lIgnoreLastLines;
	}

	private STextField getTxtIgnoreLastLines() {
		if (txtIgnoreLastLines == null) {
			txtIgnoreLastLines = new STextField();
			txtIgnoreLastLines.setText("0");
			txtIgnoreLastLines.setBounds(483, 47, 52, 21);
		}
		return txtIgnoreLastLines;
	}

}
