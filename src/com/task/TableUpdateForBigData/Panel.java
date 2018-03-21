package com.task.TableUpdateForBigData;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.app.AppFun;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.SButton;
import common.component.SCheckBox;
import common.component.SLabel;
import common.component.SRadioButton;
import common.component.SScrollPane;
import common.component.STextArea;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

public class Panel implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlTableUpdate;
	private SLabel lSrcDbName;
	private SLabel lSrcCompareFields;
	private SCheckBox chkInsert;
	private JPanel pnlNotExist;
	private SCheckBox rbtnUpdate;
	private SRadioButton rbtnInsert;
	private JPanel pnlDifferent;
	private STextField txtDstCompareFields;
	private STextField txtSrcCompareFields;
	private STextField txtDstKey;
	private STextField txtSrcKey;
	private SLabel lDstCompareFields;
	private SScrollPane scrlpInsertSql;
	private SLabel lInsertSql;
	private SLabel lDstDbName;
	private SLabel lSrcSelectSql;
	private SLabel lUpdatetSql;
	private STextField txtSrcDbName;
	private SLabel lDstSelectSql;
	private STextArea txtrDstSelectSql;
	private SScrollPane scrlpDstSelectSql;
	private STextArea txtrInsertSql;
	private SButton btnSrcDbName;
	private STextField txtDstDbName;
	private SButton btnDstDbName;
	private STextArea txtrSrcSelectSql;
	private SScrollPane scrlpSrcSelectSql;
	private SLabel lDstCompareKey;
	private SLabel lSrcCompareKey;
	private STextArea txtrUpdatetSql;
	private SScrollPane scrlpUpdateSql;

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
		// getContentPane().add(getPanel(), BorderLayout.NORTH);
		// this.setSize(615, 480);
	}

	public JPanel getPanel() {
		pnlTableUpdate = new JPanel();
		try {
			pnlTableUpdate.setLayout(null);
			pnlTableUpdate.setBounds(0, 13, 429, 95);
			{
				lSrcDbName = new SLabel("源数据库连接");
				pnlTableUpdate.add(lSrcDbName);
				lSrcDbName.setBounds(30, 15, 91, 14);
			}
			{
				lUpdatetSql = new SLabel("\u66f4\u65b0SQL");
				pnlTableUpdate.add(lUpdatetSql);
				lUpdatetSql.setBounds(30, 345, 90, 14);
			}
			{
				txtSrcDbName = new STextField();
				pnlTableUpdate.add(txtSrcDbName);
				txtSrcDbName.setBounds(127, 8, 411, 21);
				txtSrcDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnSrcDbNameActionPerformed();
					}
				});
			}
			{
				btnSrcDbName = new SButton("..");
				pnlTableUpdate.add(btnSrcDbName);
				btnSrcDbName.setBounds(544, 8, 22, 21);
				btnSrcDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnSrcDbNameActionPerformed();
					}
				});
			}
			{
				btnDstDbName = new SButton("..");
				pnlTableUpdate.add(btnDstDbName);
				btnDstDbName.setBounds(545, 34, 22, 21);
				btnDstDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDstDbNameActionPerformed();
					}
				});
			}
			{
				txtDstDbName = new STextField();
				pnlTableUpdate.add(txtDstDbName);
				txtDstDbName.setBounds(128, 34, 411, 21);
				txtDstDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnDstDbNameActionPerformed();
					}
				});
			}
			{
				lDstDbName = new SLabel("目标数据库连接");
				pnlTableUpdate.add(lDstDbName);
				lDstDbName.setBounds(30, 41, 91, 14);
			}
			{

				txtrSrcSelectSql = new STextArea();
				scrlpSrcSelectSql = new SScrollPane(txtrSrcSelectSql);
				pnlTableUpdate.add(scrlpSrcSelectSql, "bottom");
				scrlpSrcSelectSql.setBounds(127, 68, 439, 50);
			}
			{
				lSrcSelectSql = new SLabel("\u6e90\u8868SQL");
				pnlTableUpdate.add(lSrcSelectSql);
				lSrcSelectSql.setBounds(30, 68, 90, 14);
			}

			{

				{
					txtrUpdatetSql = new STextArea();
				}
				scrlpUpdateSql = new SScrollPane(txtrUpdatetSql);
				pnlTableUpdate.add(scrlpUpdateSql, "bottom");
				scrlpUpdateSql.setBounds(127, 342, 439, 50);
			}
			{

				{
					txtrInsertSql = new STextArea();
				}
				scrlpInsertSql = new SScrollPane(txtrInsertSql);
				pnlTableUpdate.add(scrlpInsertSql, "bottom");
				scrlpInsertSql.setBounds(127, 288, 439, 50);

			}
			pnlTableUpdate.add(getLDstSelectSql());
			pnlTableUpdate.add(getLInsertSql());
			pnlTableUpdate.add(getScrlpDstSelectSql());
			pnlTableUpdate.add(getLSrcCompareKey());
			pnlTableUpdate.add(getLDstCompareKey());
			pnlTableUpdate.add(getLSrcCompareFields());
			pnlTableUpdate.add(getLDstCompareFields());
			pnlTableUpdate.add(getTxtSrcCompareKey());
			pnlTableUpdate.add(getTxtDstCompareKey());
			pnlTableUpdate.add(getTxtSrcCompareFields());
			pnlTableUpdate.add(getTxtDstCompareFields());
			pnlTableUpdate.add(getPnlDifferent());
			pnlTableUpdate.add(getPnlNotExist());
		} catch (Exception e) {
			Log.logError("数据同步面板初始化面板错误:", e);
			return pnlTableUpdate;
		} finally {
		}
		return pnlTableUpdate;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getSrcDbName())) {
			ShowMsg.showWarn("源数据库连接不能为空");
			return false;
		}
		if ("".equals(bean.getDstDbName())) {
			ShowMsg.showWarn("目的数据库连接不能为空");
			return false;
		}
		if ("".equals(bean.getSrcSelectSql())) {
			ShowMsg.showWarn("源表SQL不能为空");
			return false;
		}
		if ("".equals(bean.getSrcKey())) {
			ShowMsg.showWarn("源表关键字不能为空");
			return false;
		}
		if ("".equals(bean.getDstKey())) {
			ShowMsg.showWarn("目标表关键字不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDstKey(UtilString.isNil(txtDstKey.getText()));
			bean.setDstDbName(UtilString.isNil(txtDstDbName.getText()));
			bean.setDstInsertSql(UtilString.isNil(txtrInsertSql.getText()));
			bean.setDstUpdateSql(UtilString.isNil(txtrUpdatetSql.getText()));
			bean.setSrcKey(UtilString.isNil(txtSrcKey.getText()));
			bean.setSrcDbName(UtilString.isNil(txtSrcDbName.getText()));
			bean.setSrcSelectSql(UtilString.isNil(txtrSrcSelectSql.getText()));
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			ShowMsg.showWarn(e.getMessage());
			return false;
			// Log.logError("面板赋值任务对象错误:", e);
		} finally {
		}
		return true;
	}

	// 填充界面
	public void fillComp(ITask task) {
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(task.getJsonStr(), Bean.class);
			txtDstKey.setText(bean.getDstKey());
			txtDstDbName.setText(bean.getDstDbName());
			txtrInsertSql.setText(bean.getDstInsertSql());
			txtrUpdatetSql.setText(bean.getDstUpdateSql());
			txtrSrcSelectSql.setText(bean.getSrcSelectSql());
			txtSrcDbName.setText(bean.getSrcDbName());
			txtSrcKey.setText(bean.getSrcKey());
		} catch (Exception e) {
			Log.logError("面板填充控件错误:", e);
		} finally {
		}
	}

	private void btnSrcDbNameActionPerformed() {
		try {
			AppFun.getDbName(txtSrcDbName);
		} catch (Exception e) {
			Log.logError("面板获取源数据库链接错误:", e);
		} finally {
		}
	}

	private void btnDstDbNameActionPerformed() {
		try {
			AppFun.getDbName(txtDstDbName);
		} catch (Exception e) {
			Log.logError("面板获取目标数据库链接错误:", e);
		} finally {
		}
	}

	private SLabel getLInsertSql() {
		if (lInsertSql == null) {
			lInsertSql = new SLabel("\u63d2\u5165SQL");
			lInsertSql.setBounds(31, 288, 90, 14);
		}
		return lInsertSql;
	}

	private SScrollPane getScrlpDstSelectSql() {
		if (scrlpDstSelectSql == null) {
			txtrDstSelectSql = new STextArea();
			scrlpDstSelectSql = new SScrollPane(txtrDstSelectSql);
			scrlpDstSelectSql.setBounds(127, 124, 439, 50);
		}
		return scrlpDstSelectSql;
	}

	private SLabel getLDstSelectSql() {
		if (lDstSelectSql == null) {
			lDstSelectSql = new SLabel("\u76ee\u6807\u8868SQL");
			lDstSelectSql.setBounds(30, 124, 90, 14);
		}
		return lDstSelectSql;
	}

	private SLabel getLSrcCompareKey() {
		if (lSrcCompareKey == null) {
			lSrcCompareKey = new SLabel("\u6e90\u8868\u5173\u952e\u5b57");
			lSrcCompareKey.setBounds(30, 189, 73, 14);
		}
		return lSrcCompareKey;
	}

	private SLabel getLDstCompareKey() {
		if (lDstCompareKey == null) {
			lDstCompareKey = new SLabel("\u76ee\u6807\u8868\u5173\u952e\u5b57");
			lDstCompareKey.setBounds(313, 189, 82, 14);
		}
		return lDstCompareKey;
	}

	private SLabel getLSrcCompareFields() {
		if (lSrcCompareFields == null) {
			lSrcCompareFields = new SLabel("\u6e90\u8868\u6bd4\u8f83\u5b57\u6bb5");
			lSrcCompareFields.setBounds(30, 215, 73, 14);
		}
		return lSrcCompareFields;
	}

	private SLabel getLDstCompareFields() {
		if (lDstCompareFields == null) {
			lDstCompareFields = new SLabel("\u76ee\u6807\u8868\u6bd4\u8f83\u5b57\u6bb5");
			lDstCompareFields.setBounds(313, 215, 88, 14);
		}
		return lDstCompareFields;
	}

	private STextField getTxtSrcCompareKey() {
		if (txtSrcKey == null) {
			txtSrcKey = new STextField();
			txtSrcKey.setBounds(127, 182, 160, 21);
		}
		return txtSrcKey;
	}

	private STextField getTxtDstCompareKey() {
		if (txtDstKey == null) {
			txtDstKey = new STextField();
			txtDstKey.setBounds(407, 182, 160, 21);
		}
		return txtDstKey;
	}

	private STextField getTxtSrcCompareFields() {
		if (txtSrcCompareFields == null) {
			txtSrcCompareFields = new STextField();
			txtSrcCompareFields.setBounds(127, 208, 160, 21);
		}
		return txtSrcCompareFields;
	}

	private STextField getTxtDstCompareFields() {
		if (txtDstCompareFields == null) {
			txtDstCompareFields = new STextField();
			txtDstCompareFields.setBounds(407, 208, 160, 21);
		}
		return txtDstCompareFields;
	}

	private JPanel getPnlDifferent() {
		if (pnlDifferent == null) {
			pnlDifferent = new JPanel();
			GridLayout pnlDifferentLayout = new GridLayout(1, 1);
			pnlDifferentLayout.setColumns(1);
			pnlDifferentLayout.setHgap(5);
			pnlDifferentLayout.setVgap(5);
			pnlDifferent.setLayout(pnlDifferentLayout);
			pnlDifferent.setBounds(30, 241, 257, 40);
			pnlDifferent.setBorder(BorderFactory.createTitledBorder(null, "\u4e0d\u4e00\u6837\u65f6", TitledBorder.LEADING, TitledBorder.TOP, Const.tfont, new java.awt.Color(0, 0, 0)));
			pnlDifferent.add(getRbtnUpdate());
			pnlDifferent.add(getRbtnInsert());
		}
		return pnlDifferent;
	}

	private SRadioButton getRbtnInsert() {
		if (rbtnInsert == null) {
			rbtnInsert = new SRadioButton("Insert");
			rbtnInsert.setSelected(false);
			rbtnInsert.setVisible(false);
			// rbtnInsert.addActionListener(new ActionListener() {
			// public void actionPerformed(ActionEvent evt) {
			// // Fun.oppositeVaule(rbtnInsert, rbtnUpdate);
			//
			// }
			// });
		}
		return rbtnInsert;
	}

	private SCheckBox getRbtnUpdate() {
		if (rbtnUpdate == null) {
			rbtnUpdate = new SCheckBox("Update");
			rbtnUpdate.setSelected(true);
			rbtnUpdate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// Fun.oppositeVaule(rbtnUpdate, rbtnInsert);
					// rbtnUpdate.setSelected(true);
					rbtnInsert.setSelected(false);
				}
			});
		}
		return rbtnUpdate;
	}

	private JPanel getPnlNotExist() {
		if (pnlNotExist == null) {
			pnlNotExist = new JPanel();
			GridLayout jPanel1Layout = new GridLayout(1, 1);
			jPanel1Layout.setColumns(1);
			jPanel1Layout.setHgap(5);
			jPanel1Layout.setVgap(5);
			pnlNotExist.setBorder(BorderFactory.createTitledBorder(null, "\u4e0d\u5b58\u5728\u65f6", TitledBorder.LEADING, TitledBorder.TOP, Const.tfont, new java.awt.Color(0, 0, 0)));
			pnlNotExist.setLayout(jPanel1Layout);
			pnlNotExist.setBounds(313, 241, 254, 40);
			pnlNotExist.add(getChkInsert());
		}
		return pnlNotExist;
	}

	private SCheckBox getChkInsert() {
		if (chkInsert == null) {
			chkInsert = new SCheckBox("Insert");
			chkInsert.setSelected(true);
		}
		return chkInsert;
	}

}
