package com.task.CustomQuery;

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
import common.component.SComboBox;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.STextArea;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlCustomQuery;
	private SLabel lDbconn;
	private SLabel lSql;
	private STextField txtDbName;
	private SButton btnDbconn;
	private SLabel lSqlrule;
	private SScrollPane scrlSql;
	private STextArea txtaSQL;
	private SScrollPane scrlRule;
	private STextArea txtaRule;
	private SComboBox cmbReturnType;
	private SLabel lReturnType;
	private String[] returnType = new String[] { "记录数", "数值", "Vector" };

	public Panel() {
	}

	// 用于定制日志
	public void hideReturnType() {
		cmbReturnType.setSelectedItem("Vector");
		cmbReturnType.setVisible(false);
		lReturnType.setVisible(false);
	}

	public JPanel getPanel() {
		pnlCustomQuery = new JPanel();
		try {
			pnlCustomQuery.setLayout(null);
			pnlCustomQuery.setBounds(0, 0, 626, 100);
			pnlCustomQuery.setPreferredSize(new java.awt.Dimension(715, 534));
			pnlCustomQuery.setSize(429, 95);
			{
				lDbconn = new SLabel("\u6570\u636e\u6e90\u8fde\u63a5");
				pnlCustomQuery.add(lDbconn);
				lDbconn.setBounds(21, 24, 70, 14);
			}
			{
				lSql = new SLabel("\u67e5\u8be2SQL");
				pnlCustomQuery.add(lSql);
				lSql.setBounds(21, 45, 70, 14);
			}
			{
				txtDbName = new STextField();
				pnlCustomQuery.add(txtDbName);
				txtDbName.setBounds(103, 21, 429, 21);
				txtDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							dbConnbtn();
					}
				});
			}
			{
				btnDbconn = new SButton("..");
				pnlCustomQuery.add(btnDbconn);
				btnDbconn.setBounds(538, 21, 22, 21);
				btnDbconn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dbConnbtn();
					}
				});
			}
			{
				lSqlrule = new SLabel("\u53c2\u6570\u6620\u5c04");
				pnlCustomQuery.add(lSqlrule);
				lSqlrule.setBounds(21, 151, 67, 14);
			}
			{
				txtaSQL = new STextArea();
				scrlSql = new SScrollPane(txtaSQL);
				pnlCustomQuery.add(scrlSql, "bottom");
				scrlSql.setBounds(103, 49, 429, 95);

			}
			{
				txtaRule = new STextArea();
				scrlRule = new SScrollPane(txtaRule);
				pnlCustomQuery.add(scrlRule, "bottom");
				scrlRule.setBounds(103, 151, 429, 95);
			}
			{
				lReturnType = new SLabel("返回类型");
				lReturnType.setBounds(21, 260, 67, 14);
				pnlCustomQuery.add(lReturnType);
				cmbReturnType = new SComboBox(returnType);
				cmbReturnType.setBounds(103, 253, 75, 21);
				pnlCustomQuery.add(cmbReturnType);
			}
		} catch (Exception e) {
			Log.logError("面板构造错误:", e);
			return pnlCustomQuery;
		} finally {
		}
		return pnlCustomQuery;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getDbName())) {
			ShowMsg.showWarn("数据源连接不能为空");
			return false;
		}
		if ("".equals(bean.getSql())) {
			ShowMsg.showWarn("定制SQL不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDbName(UtilString.isNil(txtDbName.getText()));
			bean.setSql(UtilString.isNil(txtaSQL.getText()));
			bean.setRule(UtilString.isNil(txtaRule.getText()));
			bean.setReturnType(cmbReturnType.getSelectedItem().toString());
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			ShowMsg.showWarn(e.getMessage());
			return false;
		} finally {
		}
		return true;
	}

	// 填充面板
	public void fillComp(ITask task) {
		try {
			if ("".equals(UtilString.isNil(task.getJsonStr())))
				return;
			Bean bean = (Bean) UtilJson.getJsonBean(task.getJsonStr(), Bean.class);
			txtDbName.setText(bean.getDbName());
			txtaSQL.setText(bean.getSql());
			txtaRule.setText(bean.getRule());
			cmbReturnType.setSelectedItem(UtilString.isNil(bean.getReturnType(), "数值"));
		} catch (Exception e) {
			Log.logError("面板填充控件错误:", e);
		} finally {
		}
	}

	private void dbConnbtn() {
		try {
			AppFun.getDbName(txtDbName);
		} catch (Exception e) {
			Log.logError("面板获取数据源链接错误:", e);
		} finally {
		}
	}

}
