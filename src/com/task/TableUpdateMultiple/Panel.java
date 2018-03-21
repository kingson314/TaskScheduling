package com.task.TableUpdateMultiple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.app.AppFun;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.component.STabbedPane;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

public class Panel extends JDialog implements ITaskPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pnlMain;
	private JLabel lSrcDbName;
	private STabbedPane tabDetai;
	private JLabel lDstDbName;
	private STextField txtSrcDbName;
	private STextField txtDstDbName;
	private JButton btnSrcDbName;
	private JButton btnDstDbName;
	private JPanel pnlCon;
	private JSplitPane spltMain;
	private PanelDetail[] pnlDetail;
	private final int pnlDetailCount = 2;

	public Panel() {
	}

	public JPanel getPanel() {
		pnlMain = new JPanel();
		pnlMain.add(getSpltMain());
		return pnlMain;
	}

	private boolean paramValidate(Bean bean) {
		if ("".equals(bean.getSrcDbName())) {
			ShowMsg.showWarn("源数据库连接不能为空");
			return false;
		}
		if ("".equals(bean.getDstDbName())) {
			ShowMsg.showWarn("目的数据库连接不能为空");
			return false;
		}
		return true;
	}

	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setDstDbName(UtilString.isNil(txtDstDbName.getText()));
			bean.setSrcDbName(UtilString.isNil(txtSrcDbName.getText()));

			List<BeanDetail> beanDetailList = new ArrayList<BeanDetail>();
			for (int i = 0; i < pnlDetailCount; i++) {
				BeanDetail beanDetail = new BeanDetail();
				beanDetail.setDstCompareFields(UtilString.isNil(pnlDetail[i].txtDstCompareFields.getText()));
				beanDetail.setDstCompareKey(UtilString.isNil(pnlDetail[i].txtDstCompareKey.getText()));

				beanDetail.setDstInsertSql(UtilString.isNil(pnlDetail[i].txtrInsertSql.getText()));
				beanDetail.setDstSelectSql(UtilString.isNil(pnlDetail[i].txtrDstSelectSql.getText()));
				beanDetail.setDstUpdateSql(UtilString.isNil(pnlDetail[i].txtrUpdatetSql.getText()));
				beanDetail.setSrcCompareFields(UtilString.isNil(pnlDetail[i].txtSrcCompareFields.getText()));
				beanDetail.setSrcCompareKey(UtilString.isNil(pnlDetail[i].txtSrcCompareKey.getText()));

				beanDetail.setSrcSelectSql(UtilString.isNil(pnlDetail[i].txtrSrcSelectSql.getText()));
				beanDetail.setIfInsertWhenDifferent(pnlDetail[i].rbtnInsert.isSelected());
				beanDetail.setIfInsertWhenNotExist(pnlDetail[i].chkInsert.isSelected());
				beanDetail.setIfUpdateWhenDifferent(pnlDetail[i].rbtnUpdate.isSelected());
				beanDetailList.add(beanDetail);
			}
			bean.setListBeanDetail(beanDetailList);
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			ShowMsg.showWarn(e.getMessage());
			return false;
			// Log.logError("数据表更新面板赋值任务对象错误:", e);
		} finally {
		}
		return true;
	}

	public void fillComp(ITask task) {
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(task.getJsonStr(), Bean.class);
			txtSrcDbName.setText(bean.getSrcDbName());
			txtDstDbName.setText(bean.getDstDbName());

			List<BeanDetail> beanDetailList = bean.getListBeanDetail();
			for (int i = 0; i < beanDetailList.size(); i++) {
				String jsonStr = UtilJson.objToJson(beanDetailList.get(i));
				BeanDetail beanDetail = (BeanDetail) UtilJson.getJsonBean(jsonStr, BeanDetail.class);
				pnlDetail[i].txtDstCompareFields.setText(beanDetail.getDstCompareFields());
				pnlDetail[i].txtDstCompareKey.setText(beanDetail.getDstCompareKey());
				pnlDetail[i].txtrInsertSql.setText(beanDetail.getDstInsertSql());
				pnlDetail[i].txtrDstSelectSql.setText(beanDetail.getDstSelectSql());
				pnlDetail[i].txtrUpdatetSql.setText(beanDetail.getDstUpdateSql());
				pnlDetail[i].txtrSrcSelectSql.setText(beanDetail.getSrcSelectSql());
				pnlDetail[i].txtSrcCompareFields.setText(beanDetail.getSrcCompareFields());
				pnlDetail[i].txtSrcCompareKey.setText(beanDetail.getSrcCompareKey());
				pnlDetail[i].rbtnInsert.setSelected(beanDetail.isIfInsertWhenDifferent());
				pnlDetail[i].rbtnUpdate.setSelected(beanDetail.isIfUpdateWhenDifferent());
				pnlDetail[i].chkInsert.setSelected(beanDetail.isIfInsertWhenNotExist());
			}
		} catch (Exception e) {
			Log.logError("数据表更新面板填充控件错误:", e);
		} finally {
		}
	}

	private void btnSrcDbNameActionPerformed() {
		try {
			AppFun.getDbName(txtSrcDbName);
		} catch (Exception e) {
			Log.logError("数据表更新面板获取源数据库链接错误:", e);
		} finally {
		}
	}

	private void btnDstDbNameActionPerformed() {
		try {
			AppFun.getDbName(txtDstDbName);
		} catch (Exception e) {
			Log.logError("数据表更新面板获取目标数据库链接错误:", e);
		} finally {
		}
	}

	private JSplitPane getSpltMain() {
		if (spltMain == null) {
			spltMain = new JSplitPane();
			spltMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
			spltMain.setDividerSize(1);
			spltMain.add(getPnlCon(), JSplitPane.TOP);
			spltMain.add(getTabDetai(), JSplitPane.BOTTOM);
		}
		return spltMain;
	}

	private JPanel getPnlCon() {
		if (pnlCon == null) {
			pnlCon = new JPanel();
			pnlCon.setBounds(7, 3, 595, 59);
			pnlCon.setLayout(null);
			pnlCon.setBorder(null);
			pnlCon.setPreferredSize(new java.awt.Dimension(592, 59));
			{
				btnDstDbName = new JButton("..");
				pnlCon.add(btnDstDbName);
				btnDstDbName.setFont(Const.tfontBtn);
				btnDstDbName.setBounds(539, 33, 22, 21);
				btnDstDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnDstDbNameActionPerformed();
					}
				});
			}

			{
				btnSrcDbName = new JButton("..");
				pnlCon.add(btnSrcDbName);
				btnSrcDbName.setFont(Const.tfontBtn);
				btnSrcDbName.setBounds(539, 7, 22, 21);
				btnSrcDbName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnSrcDbNameActionPerformed();
					}
				});
			}

			{
				txtDstDbName = new STextField();
				pnlCon.add(txtDstDbName);
				txtDstDbName.setBounds(122, 33, 411, 21);
				txtDstDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnDstDbNameActionPerformed();
					}
				});
			}
			{
				txtSrcDbName = new STextField();
				pnlCon.add(txtSrcDbName);
				txtSrcDbName.setBounds(122, 7, 411, 21);
				txtSrcDbName.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)
							btnSrcDbNameActionPerformed();
					}
				});
			}
			{
				lDstDbName = new JLabel("目标数据库连接");
				pnlCon.add(lDstDbName);
				lDstDbName.setFont(Const.tfont);
				lDstDbName.setBounds(25, 40, 91, 14);
			}
			{
				lSrcDbName = new JLabel("源数据库连接");
				pnlCon.add(lSrcDbName);
				lSrcDbName.setBounds(25, 14, 91, 14);
				lSrcDbName.setFont(Const.tfont);
			}
		}
		return pnlCon;
	}

	private STabbedPane getTabDetai() {
		if (tabDetai == null) {
			tabDetai = new STabbedPane();
			tabDetai.setPreferredSize(new java.awt.Dimension(626, 359));
			pnlDetail = new PanelDetail[pnlDetailCount];
			for (int i = 0; i < pnlDetailCount; i++) {
				pnlDetail[i] = new PanelDetail();
				String title = "Table" + String.valueOf(i + 1);
				tabDetai.addTab(title, null, pnlDetail[i], null);
			}
		}
		return tabDetai;
	}

}
