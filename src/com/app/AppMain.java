package com.app;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import app.AppConfig;
import app.AppStatus;
import app.AppToolBar;
import app.AppTree;

import common.component.SSplitPane;
import common.component.STabbedPane;

import consts.ImageContext;

/**
 * @info 主程序
 * 
 * @author fgq 20120831
 * 
 */
public class AppMain extends JFrame {
	private static final long serialVersionUID = 20111030;
	private SSplitPane spltView;
	private SSplitPane spltTab;
	private JTabbedPane tabView;
	private SSplitPane spltMain;
	private STabbedPane logView;
	public static AppMain appMain;

	// 构造函数，执行参数初始化、界面初始化
	public AppMain() {
		try {
			AppFun.getInstance().setGlobalShortCut();
			this.setSize(900, 700);
			this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.Sys));
			this.setLayout(new BorderLayout());
			this.setTitle(AppConfig.getInstance().getMapAppConfig().get("appTitle"));
			this.setSize(1200, 800);
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			// 添加窗体退出事件
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent evt) {
					AppFun.getInstance().systemClosing();
				}
			});

			// 菜单
			this.setJMenuBar(AppMenu.getInstance().getMb());
			// 主界面分割窗体
			spltMain = new SSplitPane(0, 30, false);
			spltMain.setEnabled(false);
			{
				// 工具栏
				{
					spltMain.add(AppToolBar.getInstance().getToolBar(), SSplitPane.TOP);
				}
				// 视图
				{
					spltView = new SSplitPane(1, 200, true);
					// 导航Jtree
					{
						spltView.add(AppTree.getInstance().getTreeView(), SSplitPane.LEFT);
					}

					// 页面
					{
						spltTab = new SSplitPane(0, 0.6, true);
						{
							// 列表视图
							this.tabView = AppTableView.getInstance().getTab();
							spltTab.add(this.tabView, SSplitPane.TOP);
						}

						// 日志信息显示
						{
							this.logView = AppLogView.getInstance().getTab();
							spltTab.add(this.logView, SSplitPane.BOTTOM);

						}
						spltView.add(spltTab, SSplitPane.RIGHT);
					}

				}
				spltMain.add(spltView, SSplitPane.BOTTOM);
			}
			this.add(spltMain, BorderLayout.CENTER);
			// 状态栏
			this.add(AppStatus.getInstance().getStatusbar(), BorderLayout.PAGE_END);
			this.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {}
	}

	// 启动失败时，限制程序部分功能，现已不使用
	public void disabled(int type) {
		// for (int i = 0; i < TaskTab.getInstance().getTbTask()
		// .getComponentCount() - 1; i++) {
		// TaskTab.getInstance().getTbTask().getComponent(i).setEnabled(false);
		// }
		// TaskTab.getInstance().getTblTask().setEnabled(false);
		//
		// // AppMenu.getInstance().getMTool().setEnabled(false);
		// // AppMenu.getInstance().getMHelp().setEnabled(false);
		//
		// AppMenu.getInstance().getMiFtp().setEnabled(false);
		// AppMenu.getInstance().getMiSettings().setEnabled(false);
		// AppMenu.getInstance().getMiMail().setEnabled(false);
		// AppMenu.getInstance().getMiMonitor().setEnabled(false);
		// AppMenu.getInstance().getMiMonitorGroup().setEnabled(false);
		// String text = "";
		// if (type == 0) {
		// AppMenu.getInstance().getMiSys().setEnabled(false);
		// AppMenu.getInstance().getMiDbConn().setEnabled(false);
		// text = "请检查/xml目录下的systemConfig.xml文件配置是否正确。";
		// } else if (type == 1) {
		// text = "检查并重新设置如下参数:\n" + "系统参数——当前执行日期数据库连接\n"
		// + "数据源配置——使用到的数据库连接(能否正常连接)\n" + "重启程序...\n";
		// }
		// Log.showLog(AppLogView.LogSystem, text, true);
		// AppLogView.getInstance().getLogDisplay(AppLogView.LogSystem)
		// .getTxtaLog().setFont(
		// new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 14));
		// AppLogView.getInstance().getLogDisplay("系统日志").getTxtaLog()
		// .setForeground(Color.red);
	}

	public JTabbedPane getTabMain() {
		return tabView;
	}

}
