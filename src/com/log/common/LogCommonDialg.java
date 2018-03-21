package com.log.common;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;

import com.log.Log;
import common.component.SDialog;
import common.component.SScrollPane;

import consts.ImageContext;

public class LogCommonDialg extends SDialog {
	private SScrollPane SScrollPane;

	private static final long serialVersionUID = 1L;
	private JTable jTable;

	// public static void main(String[] args) {
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// String maxUpdateTime = LogCommonDao.getInstance()
	// .getExecUpdateTime();
	// LogCommonDialg inst = new LogCommonDialg(maxUpdateTime);
	// inst.setVisible(true);
	// }
	// });
	//
	// }
	// 构造
	public LogCommonDialg(String maxUpdateTime) {
		super();
		try {
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(
					ImageContext.Sche));
			this.setTitle("执行结果 ");
			Map<String, String> map = new HashMap<String, String>();
			map.put("最大更新时间", String.valueOf(maxUpdateTime));
			this.jTable = new LogCommonTable(map).getJtable();
			String key[] = new String[1];
			key[0] = String.valueOf(maxUpdateTime);
			initGUI();
		} catch (Exception e) {
			Log.logError("执行结果对话框构造错误:", e);
		} finally {
		}
	}

	// 初始化界面
	private void initGUI() {
		try {
			setModal(true);
			setAlwaysOnTop(false);
			GridLayout thisLayout = new GridLayout(1, 1);
			thisLayout.setColumns(1);
			thisLayout.setHgap(5);
			thisLayout.setVgap(5);
			getContentPane().setLayout(thisLayout);
			this.setBounds(0, 0, 900, 300);
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this
					.getWidth()) / 2;
			int h = 60 + (Toolkit.getDefaultToolkit().getScreenSize().height - this
					.getHeight()) / 2;
			this.setLocation(w, h);
			this.addWindowListener(new WindowAdapter() {// 添加窗体退出事件
						public void windowClosing(java.awt.event.WindowEvent evt) {
							dispose();
						}
					});
			{
				SScrollPane = new SScrollPane(jTable);
				getContentPane().add(SScrollPane);
			}
			jTable.getTableHeader().getColumnModel().getColumn(0)
					.setMaxWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(0)
					.setMinWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(0).setWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(0)
					.setPreferredWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(1)
					.setMaxWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(1)
					.setMinWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(1).setWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(1)
					.setPreferredWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(9)
					.setMaxWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(9)
					.setMinWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(9).setWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(9)
					.setPreferredWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(10).setMaxWidth(
					0);
			jTable.getTableHeader().getColumnModel().getColumn(10).setMinWidth(
					0);
			jTable.getTableHeader().getColumnModel().getColumn(10).setWidth(0);
			jTable.getTableHeader().getColumnModel().getColumn(10)
					.setPreferredWidth(0);
		} catch (Exception e) {
			Log.logError("执行结果对话框初始化界面错误:", e);
		} finally {
		}
	}

}
