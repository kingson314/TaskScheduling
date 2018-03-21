package com.task.ExportExcel;

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

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlExcel;
	private SLabel lDocName;
	private SLabel lDocTitle;
	private SLabel lDbconn;
	private SLabel lSql;
	private STextField txtDocName;
	private STextField txtDoctitle;
	private STextField txtDbName;
	private SButton btnDbconn;
	private SLabel lSqlrule;
	private STextField txtDocPath;
	private SLabel lDocPath;
	private SButton btnDocPath;
	private SButton btnDocName;
	private SScrollPane scrl1;
	private STextArea txtaSQL;
	private SScrollPane scrl2;
	private STextArea txtaSqlRule;

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
		pnlExcel = new JPanel();
		try {
			// getContentPane().add(jPanelExcel);
			pnlExcel.setLayout(null);
			pnlExcel.setBounds(-13, 13, 626, 43);
			pnlExcel.setPreferredSize(new java.awt.Dimension(668, 482));
			pnlExcel.setSize(429, 95);
			{
				lDocName = new SLabel("\u6587\u6863\u540d\u79f0");
				pnlExcel.add(lDocName);
				lDocName.setBounds(30, 77, 70, 14);
			}
			{
				lDocTitle = new SLabel("\u6587\u6863\u6807\u9898");
				pnlExcel.add(lDocTitle);
				lDocTitle.setBounds(30, 110, 70, 14);
			}
			{
				lDbconn = new SLabel("数据库连接");
				pnlExcel.add(lDbconn);
				lDbconn.setBounds(30, 149, 70, 14);
			}
			{
				lSql = new SLabel("\u67e5\u8be2SQL");
				pnlExcel.add(lSql);
				lSql.setBounds(30, 186, 70, 14);
			}
			{
				txtDocName = new STextField();
				pnlExcel.add(txtDocName);
				txtDocName.setBounds(109, 74, 429, 21);
				txtDocName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							docNamebtn();
					}
				});
			}
			{
				txtDoctitle = new STextField();
				pnlExcel.add(txtDoctitle);
				txtDoctitle.setBounds(109, 107, 429, 21);
			}
			{
				txtDbName = new STextField();
				pnlExcel.add(txtDbName);
				txtDbName.setBounds(109, 146, 429, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							dbConnbtn();
					}
				});
			}
			{
				btnDbconn = new SButton("..");
				pnlExcel.add(btnDbconn);
				btnDbconn.setBounds(544, 146, 22, 21);
				btnDbconn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dbConnbtn();
					}
				});
			}
			{
				lSqlrule = new SLabel("\u53c2\u6570\u6620\u5c04");
				pnlExcel.add(lSqlrule);
				lSqlrule.setBounds(36, 292, 67, 14);
			}
			{
				txtDocPath = new STextField();
				pnlExcel.add(txtDocPath);
				txtDocPath.setBounds(109, 40, 429, 22);
				txtDocPath.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							docPathbtn();
					}
				});
			}
			{
				lDocPath = new SLabel("\u6587\u6863\u8def\u5f84");
				pnlExcel.add(lDocPath);
				lDocPath.setBounds(30, 40, 67, 14);
			}
			{
				btnDocPath = new SButton("..");
				pnlExcel.add(btnDocPath);
				btnDocPath.setBounds(544, 41, 22, 21);
				btnDocPath.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						docPathbtn();
					}
				});
			}
			{
				btnDocName = new SButton("..");
				pnlExcel.add(btnDocName);
				btnDocName.setBounds(544, 74, 22, 21);
				btnDocName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						docNamebtn();
					}
				});
			}
			{

				{
					txtaSQL = new STextArea();
				}
				scrl1 = new SScrollPane(txtaSQL);
				pnlExcel.add(scrl1, "bottom");
				scrl1.setBounds(109, 190, 429, 95);
			}
			{
				{
					txtaSqlRule = new STextArea();
				}
				scrl2 = new SScrollPane(txtaSqlRule);
				pnlExcel.add(scrl2, "bottom");
				scrl2.setBounds(109, 292, 429, 95);
			}
		} catch (Exception e) {
			Log.logError("财汇禁限投库面板初始化面板错误:", e);
			return pnlExcel;
		} finally {
		}
		return pnlExcel;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getDocPath())) {
			ShowMsg.showWarn("文档路径不能为空");
			return false;
		}
		if ("".equals(bean.getDocName())) {
			ShowMsg.showWarn("文档名称不能为空");
			return false;
		}
		if ("".equals(bean.getDbName())) {
			ShowMsg.showWarn("数据库连接不能为空");
			return false;
		}
		if ("".equals(bean.getSql())) {
			ShowMsg.showWarn("查询SQL不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDocPath(txtDocPath.getText() == null ? "" : txtDocPath.getText());
			bean.setDocName(txtDocName.getText() == null ? "" : txtDocName.getText());
			bean.setTitle(txtDoctitle.getText() == null ? "" : txtDoctitle.getText());
			bean.setDbName(txtDbName.getText() == null ? "" : txtDbName.getText());
			bean.setSql(txtaSQL.getText() == null ? "" : txtaSQL.getText());
			bean.setSqlRule(txtaSqlRule.getText() == null ? "" : txtaSqlRule.getText());

			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			// Log.logError("财汇禁限投库面板赋值任务对象错误:", e);
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
			txtDocPath.setText(bean.getDocPath());
			txtDocName.setText(bean.getDocName());
			txtDoctitle.setText(bean.getTitle());
			txtDbName.setText(bean.getDbName());
			txtaSQL.setText(bean.getSql());
			txtaSqlRule.setText(bean.getSqlRule());
		} catch (Exception e) {
			Log.logError("财汇禁限投库面板填充控件错误:", e);
		} finally {
		}
	}

	private void dbConnbtn() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("财汇禁限投库面板获取数据源链接错误:", e);
		} finally {
		}
	}

	private void docPathbtn() {
		mouseDoubleClick(txtDocPath);
	}

	private void docNamebtn() {
		mouseDoubleClick(txtDocName);
	}

	private void mouseDoubleClick(STextField TextField) {

		try {

			if (TextField.equals(txtDocPath)) {
				txtDocPath.setText(ShowDialog.openDir());
			} else if (TextField.equals(txtDocName)) {
				String[] path = ShowDialog.openFileForPathAndFile();
				txtDocPath.setText(path[0]);
				txtDocName.setText(path[1]);
			}
		} catch (Exception e) {
			Log.logError("财汇禁限投库面板获取路径错误:", e);
		} finally {
		}
	}

}
