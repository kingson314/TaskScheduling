package com.task.DataUpdate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

public class Panel implements ITaskPanel {
	private JPanel pnlDataUpdate;
	private SLabel lSrcDbName;
	private SScrollPane scrlSrcSql;
	private SLabel l1;
	private SLabel lDstDbName;
	private SLabel lrule;
	private SLabel lDstSql;
	private STextField txtSrcDbName;
	private SLabel lMaxValue;
	private STextArea txtaMaxValueSql;
	private SScrollPane scrlMaxValue;
	private SComboBox cmbUpdateType;
	private SLabel lUpdateType;
	private STextArea txtaSrcSql;
	private SButton btnSrcDbName;
	private STextField txtDstDbName;
	private SButton btnDstDbName;
	private STextArea txtaRuleSql;
	private SScrollPane scrlDelSql;
	private STextArea txtaDstSql;
	private SScrollPane scrlDstSql;

	private String srcSql = "";
	private String dstSql = "";
	private String delSql = "";

	// public static void main(String[] args) {
	// Panel inis = new Panel();
	//
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
		pnlDataUpdate = new JPanel();
		try {
			// getContentPane().add(jPanelDataUpdate);
			pnlDataUpdate.setLayout(null);
			pnlDataUpdate.setBounds(-13, 13, 626, 43);
			pnlDataUpdate.setPreferredSize(new java.awt.Dimension(668, 482));
			pnlDataUpdate.setSize(429, 95);
			{
				lSrcDbName = new SLabel("源数据库连接");
				pnlDataUpdate.add(getSLabel1());
				pnlDataUpdate.add(getLupdateType());
				pnlDataUpdate.add(getCmb_updateType());
				pnlDataUpdate.add(getSScrollPane_MaxValue(), "bottom");
				pnlDataUpdate.add(getSLabel_MaxValue());
				pnlDataUpdate.add(lSrcDbName);
				lSrcDbName.setBounds(30, 21, 91, 14);
			}
			{
				lDstSql = new SLabel("\u76ee\u6807SQL");
				pnlDataUpdate.add(lDstSql);
				lDstSql.setBounds(30, 330, 90, 14);
			}
			{
				txtSrcDbName = new STextField();
				pnlDataUpdate.add(txtSrcDbName);
				txtSrcDbName.setBounds(127, 18, 411, 21);
				txtSrcDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btn_SrcDbName();
					}
				});
			}
			{
				btnSrcDbName = new SButton("..");
				pnlDataUpdate.add(btnSrcDbName);
				btnSrcDbName.setBounds(544, 18, 22, 21);
				btnSrcDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btn_SrcDbName();
					}
				});
			}
			{
				btnDstDbName = new SButton("..");
				pnlDataUpdate.add(btnDstDbName);
				btnDstDbName.setBounds(545, 49, 22, 21);
				btnDstDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btn_DstDbName();
					}
				});
			}
			{
				txtDstDbName = new STextField();
				pnlDataUpdate.add(txtDstDbName);
				txtDstDbName.setBounds(128, 49, 411, 21);
				txtDstDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btn_DstDbName();
					}
				});
			}
			{
				lDstDbName = new SLabel("目标数据库连接");
				pnlDataUpdate.add(lDstDbName);
				lDstDbName.setBounds(30, 52, 91, 14);
			}
			{

				txtaRuleSql = new STextArea();
				txtaRuleSql.setText(delSql);
				scrlDelSql = new SScrollPane(txtaRuleSql);
				pnlDataUpdate.add(scrlDelSql, "bottom");
				scrlDelSql.setBounds(127, 111, 412, 65);
			}
			{
				lrule = new SLabel("\u589e\u91cf\u89c4\u5219");
				pnlDataUpdate.add(lrule);
				lrule.setBounds(30, 113, 90, 14);
			}

			{
				{
					txtaDstSql = new STextArea();
					txtaDstSql.setText(dstSql);
				}
				scrlDstSql = new SScrollPane(txtaDstSql);
				pnlDataUpdate.add(scrlDstSql, "bottom");
				scrlDstSql.setBounds(127, 327, 412, 72);
			}
			{

				{
					txtaSrcSql = new STextArea();
					txtaSrcSql.setText(srcSql);
				}
				scrlSrcSql = new SScrollPane(txtaSrcSql);
				pnlDataUpdate.add(scrlSrcSql, "bottom");
				scrlSrcSql.setBounds(127, 250, 412, 72);
			}
		} catch (Exception e) {
			Log.logError("数据同步面板初始化面板错误:", e);
			return pnlDataUpdate;
		} finally {
		}
		return pnlDataUpdate;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getDstDbName())) {
			ShowMsg.showWarn("目的数据库连接不能为空");
			return false;
		}
		if ("".equals(bean.getSrcDbName())) {
			ShowMsg.showWarn("源数据库连接不能为空");
			return false;
		}
		if ("".equals(bean.getSrcSql())) {
			ShowMsg.showWarn("源表SQL不能为空");
			return false;
		}
		if ("".equals(bean.getDstSql())) {
			ShowMsg.showWarn("目标SQL不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setSrcDbName(txtSrcDbName.getText() == null ? ""
					: txtSrcDbName.getText());
			bean.setDstDbName(txtDstDbName.getText() == null ? ""
					: txtDstDbName.getText());
			bean.setUpdateType(cmbUpdateType.getSelectedItem().toString());
			bean.setRuleSql(txtaRuleSql.getText() == null ? "" : txtaRuleSql
					.getText());
			bean.setSrcSql(txtaSrcSql.getText() == null ? "" : txtaSrcSql
					.getText());
			bean.setDstSql(txtaDstSql.getText() == null ? "" : txtaDstSql
					.getText());
			bean.setMaxValueSql(txtaMaxValueSql.getText() == null ? ""
					: txtaMaxValueSql.getText());

			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			// Log.logError("数据同步面板赋值任务对象错误:", e);
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
			txtSrcDbName.setText(bean.getSrcDbName());
			txtDstDbName.setText(bean.getDstDbName());
			cmbUpdateType.setSelectedItem(bean.getUpdateType());
			txtaRuleSql.setText(bean.getRuleSql());
			txtaSrcSql.setText(bean.getSrcSql());
			txtaDstSql.setText(bean.getDstSql());
			txtaMaxValueSql.setText(bean.getMaxValueSql());
		} catch (Exception e) {
			Log.logError("数据同步面板填充控件错误:", e);
		} finally {
		}
	}

	private void btn_SrcDbName() {
		try {
			AppFun.getDbName(txtSrcDbName);
		} catch (Exception e) {
			Log.logError("数据同步面板获取源数据库链接错误:", e);
		} finally {
		}
	}

	private void btn_DstDbName() {
		try {
			AppFun.getDbName(txtDstDbName);
		} catch (Exception e) {
			Log.logError("数据同步面板获取目标数据库链接错误:", e);
		} finally {
		}
	}

	private SLabel getSLabel1() {
		if (l1 == null) {
			l1 = new SLabel("\u6e90\u8868SQL");
			l1.setBounds(30, 252, 90, 14);
		}
		return l1;
	}

	private SLabel getLupdateType() {
		if (lUpdateType == null) {
			lUpdateType = new SLabel("\u66f4\u65b0\u7c7b\u578b");
			lUpdateType.setBounds(30, 83, 83, 14);
		}
		return lUpdateType;
	}

	private SComboBox getCmb_updateType() {
		if (cmbUpdateType == null) {
			cmbUpdateType = new SComboBox(new String[] { "增量更新", "全表更新" });
			cmbUpdateType.setSelectedIndex(0);
			cmbUpdateType.setBounds(128, 80, 115, 21);
			cmbUpdateType.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (cmbUpdateType.getSelectedIndex() == 0) {
						lrule.setText("增量规则");
					} else
						lrule.setText("全表规则");
				}
			});
		}
		return cmbUpdateType;
	}

	private SScrollPane getSScrollPane_MaxValue() {
		if (scrlMaxValue == null) {
			txtaMaxValueSql = new STextArea();
			txtaMaxValueSql.setText(delSql);
			scrlMaxValue = new SScrollPane(txtaMaxValueSql);
			scrlMaxValue.setBounds(127, 180, 412, 65);
		}
		return scrlMaxValue;
	}

	private SLabel getSLabel_MaxValue() {
		if (lMaxValue == null) {
			lMaxValue = new SLabel("\u6700\u5927\u503cSQL");
			lMaxValue.setBounds(30, 182, 90, 14);
		}
		return lMaxValue;
	}
}
