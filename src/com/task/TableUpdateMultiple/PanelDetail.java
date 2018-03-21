package com.task.TableUpdateMultiple;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.log.Log;
import common.component.SCheckBox;

import consts.Const;

public class PanelDetail extends JPanel {
	private static final long serialVersionUID = -530288896474717106L;

	private JPanel pnlDifferent;
	private JLabel lSrcCompareFields;
	private JPanel pnlNotExist;
	private JLabel lDstCompareFields;
	private JScrollPane scrlpInsertSql;
	private JLabel lSrcSelectSql;
	private JLabel lUpdatetSql;
	private JScrollPane scrlpSrcSelectSql;
	private JLabel lDstCompareKey;
	private JLabel lSrcCompareKey;
	private JScrollPane scrlpUpdateSql;
	private JScrollPane scrlpDstSelectSql;
	private JLabel lInsertSql;
	private JLabel lDstSelectSql;
	public JTextArea txtrDstSelectSql;
	public JTextArea txtrInsertSql;
	public JTextArea txtrSrcSelectSql;
	public JTextArea txtrUpdatetSql;
	public JCheckBox chkInsert;
	public SCheckBox rbtnUpdate;
	public JRadioButton rbtnInsert;
	public JTextField txtDstCompareFields;
	public JTextField txtSrcCompareFields;
	public JTextField txtDstCompareKey;
	public JTextField txtSrcCompareKey;

	public PanelDetail() {
		super();
		try {
			this.setLayout(null);
			this.setBounds(-13, 13, 626, 43);
			this.setPreferredSize(new java.awt.Dimension(607, 452));
			this.setSize(429, 95);
			{
				lUpdatetSql = new JLabel();
				this.add(lUpdatetSql);
				lUpdatetSql.setText("\u66f4\u65b0SQL");
				lUpdatetSql.setBounds(31, 282, 90, 14);
				lUpdatetSql.setFont(Const.tfont);
			}
			{
				scrlpSrcSelectSql = new JScrollPane();
				this.add(scrlpSrcSelectSql, "bottom");
				scrlpSrcSelectSql.setBounds(128, 5, 439, 50);

				txtrSrcSelectSql = new JTextArea();
				txtrSrcSelectSql.setText("");
				txtrSrcSelectSql.setEditable(true);
				txtrSrcSelectSql.setFont(Const.tfont);
				txtrSrcSelectSql.setLineWrap(true);
				txtrSrcSelectSql.setMinimumSize(new java.awt.Dimension(3, 3));
				txtrSrcSelectSql.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
				scrlpSrcSelectSql.setViewportView(txtrSrcSelectSql);
			}
			{
				lSrcSelectSql = new JLabel();
				this.add(lSrcSelectSql);
				lSrcSelectSql.setText("\u6e90\u8868SQL");
				lSrcSelectSql.setFont(Const.tfont);
				lSrcSelectSql.setBounds(31, 5, 90, 14);
			}

			{
				scrlpUpdateSql = new JScrollPane();
				this.add(scrlpUpdateSql, "bottom");
				scrlpUpdateSql.setBounds(128, 279, 439, 50);
				{
					txtrUpdatetSql = new JTextArea();
					txtrUpdatetSql.setText("");
					txtrUpdatetSql.setEditable(true);
					scrlpUpdateSql.setViewportView(txtrUpdatetSql);
					txtrUpdatetSql.setFont(Const.tfont);
					txtrUpdatetSql.setLineWrap(true);
					txtrUpdatetSql.setMinimumSize(new java.awt.Dimension(3, 3));
					txtrUpdatetSql.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
				}
			}
			{
				scrlpInsertSql = new JScrollPane();
				this.add(scrlpInsertSql, "bottom");
				scrlpInsertSql.setBounds(128, 225, 439, 50);
				{
					txtrInsertSql = new JTextArea();
					txtrInsertSql.setText("");
					txtrInsertSql.setEditable(true);
					scrlpInsertSql.setViewportView(txtrInsertSql);
					txtrInsertSql.setFont(Const.tfont);
					txtrInsertSql.setLineWrap(true);
					txtrInsertSql.setMinimumSize(new java.awt.Dimension(3, 3));
					txtrInsertSql.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
				}

			}
			this.add(getLDstSelectSql());
			this.add(getLInsertSql());

			{
				scrlpDstSelectSql = new JScrollPane();
				this.add(scrlpDstSelectSql);
				scrlpDstSelectSql.setBounds(128, 61, 439, 50);
				scrlpDstSelectSql.setViewportView(getTxtrtDstSelectSql());
			}
			{
				lSrcCompareKey = new JLabel();
				this.add(lSrcCompareKey);
				lSrcCompareKey.setText("\u6e90\u8868\u5173\u952e\u5b57");
				lSrcCompareKey.setBounds(31, 126, 73, 14);
				lSrcCompareKey.setFont(Const.tfont);
			}
			{
				lDstCompareKey = new JLabel();
				this.add(lDstCompareKey);
				lDstCompareKey.setText("\u76ee\u6807\u8868\u5173\u952e\u5b57");
				lDstCompareKey.setBounds(314, 126, 82, 14);
				lDstCompareKey.setFont(Const.tfont);
			}
			{
				lSrcCompareFields = new JLabel();
				this.add(lSrcCompareFields);
				lSrcCompareFields.setText("\u6e90\u8868\u6bd4\u8f83\u5b57\u6bb5");
				lSrcCompareFields.setBounds(31, 152, 73, 14);
				lSrcCompareFields.setFont(Const.tfont);
			}
			{
				lDstCompareFields = new JLabel();
				this.add(lDstCompareFields);
				lDstCompareFields.setText("\u76ee\u6807\u8868\u6bd4\u8f83\u5b57\u6bb5");
				lDstCompareFields.setBounds(314, 152, 88, 14);
				lDstCompareFields.setFont(Const.tfont);
			}
			{
				txtSrcCompareKey = new JTextField();
				this.add(txtSrcCompareKey);
				txtSrcCompareKey.setBounds(128, 119, 160, 21);
				txtSrcCompareKey.setFont(Const.tfont);
			}
			{
				txtDstCompareKey = new JTextField();
				this.add(txtDstCompareKey);
				txtDstCompareKey.setBounds(408, 119, 160, 21);
				txtDstCompareKey.setFont(Const.tfont);
			}
			{
				txtSrcCompareFields = new JTextField();
				this.add(txtSrcCompareFields);
				txtSrcCompareFields.setBounds(128, 145, 160, 21);
				txtSrcCompareFields.setFont(Const.tfont);
			}
			{
				txtDstCompareFields = new JTextField();
				this.add(txtDstCompareFields);
				txtDstCompareFields.setBounds(408, 145, 160, 21);
				txtDstCompareFields.setFont(Const.tfont);
			}
			{
				pnlDifferent = new JPanel();
				this.add(pnlDifferent);
				GridLayout pnlDifferentLayout = new GridLayout(1, 1);
				pnlDifferentLayout.setColumns(1);
				pnlDifferentLayout.setHgap(5);
				pnlDifferentLayout.setVgap(5);
				pnlDifferent.setLayout(pnlDifferentLayout);
				pnlDifferent.setBounds(31, 178, 257, 40);
				pnlDifferent.setBorder(BorderFactory.createTitledBorder(null, "\u4e0d\u4e00\u6837\u65f6", TitledBorder.LEADING, TitledBorder.TOP, Const.tfont, new java.awt.Color(0, 0, 0)));
				pnlDifferent.add(getRbtnUpdate());
				pnlDifferent.add(getRbtnInsert());
			}
			{
				pnlNotExist = new JPanel();
				this.add(pnlNotExist);
				GridLayout jPanel1Layout = new GridLayout(1, 1);
				jPanel1Layout.setColumns(1);
				jPanel1Layout.setHgap(5);
				jPanel1Layout.setVgap(5);
				pnlNotExist.setBorder(BorderFactory.createTitledBorder(null, "\u4e0d\u5b58\u5728\u65f6", TitledBorder.LEADING, TitledBorder.TOP, Const.tfont, new java.awt.Color(0, 0, 0)));
				pnlNotExist.setLayout(jPanel1Layout);
				pnlNotExist.setBounds(314, 178, 254, 40);
				pnlNotExist.add(getChkInsert());
			}

		} catch (Exception e) {
			Log.logError("", e);
		}
	}

	private JLabel getLInsertSql() {
		if (lInsertSql == null) {
			lInsertSql = new JLabel();
			lInsertSql.setText("\u63d2\u5165SQL");
			lInsertSql.setFont(Const.tfont);
			lInsertSql.setBounds(32, 225, 90, 14);
		}
		return lInsertSql;
	}

	private JTextArea getTxtrtDstSelectSql() {
		if (txtrDstSelectSql == null) {
			txtrDstSelectSql = new JTextArea();
			txtrDstSelectSql.setFont(Const.tfont);
			txtrDstSelectSql.setLineWrap(true);
			txtrDstSelectSql.setText("");
			txtrDstSelectSql.setEditable(true);
			txtrDstSelectSql.setMinimumSize(new java.awt.Dimension(3, 3));
			txtrDstSelectSql.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
		}
		return txtrDstSelectSql;
	}

	private JLabel getLDstSelectSql() {
		if (lDstSelectSql == null) {
			lDstSelectSql = new JLabel();
			lDstSelectSql.setText("\u76ee\u6807\u8868SQL");
			lDstSelectSql.setFont(Const.tfont);
			lDstSelectSql.setBounds(31, 61, 90, 14);
		}
		return lDstSelectSql;
	}

	private JRadioButton getRbtnInsert() {
		if (rbtnInsert == null) {
			rbtnInsert = new JRadioButton();
			rbtnInsert.setText("Insert");
			rbtnInsert.setSelected(false);
			rbtnInsert.setFont(Const.tfont);
			rbtnInsert.setVisible(false);
			rbtnInsert.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// Fun.oppositeVaule(rbtnInsert, rbtnUpdate);

				}
			});
		}
		return rbtnInsert;
	}

	private SCheckBox getRbtnUpdate() {
		if (rbtnUpdate == null) {
			rbtnUpdate = new SCheckBox("Update");
			rbtnUpdate.setSelected(true);
			rbtnUpdate.setFont(Const.tfont);
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

	private JCheckBox getChkInsert() {
		if (chkInsert == null) {
			chkInsert = new JCheckBox();
			chkInsert.setText("Insert");
			chkInsert.setSelected(true);
			chkInsert.setFont(Const.tfont);
		}
		return chkInsert;
	}

}
