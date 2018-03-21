package com.log;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import common.component.SMenuItem;
import common.component.SScrollPane;

import consts.Const;
import consts.ImageContext;


public class LogDisplay {
	private SScrollPane scrlLog;
	private JTextPane txtaLog;

	// 创建日志显示栏
	public LogDisplay() {
		try {
			{
				txtaLog = new JTextPane();
				txtaLog.setFont(Const.tfont);
				// txtaLog.setEditable(false);
			}
			{
				scrlLog = new SScrollPane(txtaLog);
				scrlLog.setSize(890, 300);
			}
			addPupuMenu();
		} catch (Exception e) {
			Log.logError("主程序创建日志显示栏错误:", e);
		} finally {
		}
	}

	public void append(String str, Color color, int fontSize) {
		if (txtaLog.getDocument().getLength() > 10000) {
			txtaLog.setText("");
		}
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, color);
		StyleConstants.setFontSize(attrSet, fontSize);
		insert(str, attrSet);
		txtaLog.setCaretPosition(txtaLog.getDocument().getLength());// 日志文件移到最后一行
		// txtaLog.repaint(); // 重画日志文件
	}

	private void insert(String str, SimpleAttributeSet attrSet) {
		Document doc = txtaLog.getDocument();
		try {
			if (doc.getLength() > 0)
				str = "\n" + str;
			doc.insertString(doc.getLength(), str, attrSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JTextPane getTxtaLog() {
		return txtaLog;
	}

	public SScrollPane getScrlLog() {
		return scrlLog;
	}

	// 创建浮动菜单栏
	private void addPupuMenu() {
		try {
			final JPopupMenu pmLog = new JPopupMenu();
			txtaLog.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger())
						pmLog.show(e.getComponent(), e.getX(), e.getY());
				}
			});
			final SMenuItem miClear = new SMenuItem("清空日志", ImageContext.Clear);
			miClear.addActionListener(new ActionListener() {// 浮动菜单
						// 退出按钮事件
						public void actionPerformed(ActionEvent arg0) {
							txtaLog.setText("");
						}
					});
			pmLog.add(miClear);
		} catch (Exception e) {
			Log.logError("菜单错误:", e);
		} finally {
		}
	}
}
