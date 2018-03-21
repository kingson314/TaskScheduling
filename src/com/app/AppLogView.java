package com.app;

import java.util.HashMap;
import java.util.Map;


import com.log.LogDisplay;
import common.component.STabbedPane;

import consts.Const;
import consts.ImageContext;

/**
 * @info 程序日志视图
 * 
 * @author fgq 20120831
 * 
 */
public class AppLogView {
	public static final String LogSystem = "系统日志";
	private static AppLogView appLogView = null;
	private Map<String, LogDisplay> mapLogDisplay = new HashMap<String, LogDisplay>();
	private STabbedPane tab;

	public static AppLogView getInstance() {
		if (appLogView == null)
			appLogView = new AppLogView();
		return appLogView;
	}

	// 构造
	private AppLogView() {
		tab = new STabbedPane(new String[] { LogSystem });
		tab.setFont(Const.tfont);
		tab.addTab(LogSystem, ImageContext.LogSys, getLogDisplay(LogSystem).getScrlLog(), LogSystem, false);

	}

	// 添加页面
	public void addTab(String title) {
		boolean has = false;
		for (int i = 0; i < tab.getTabCount(); i++) {
			if (title.equals(tab.getTitleAt(i))) {
				tab.setSelectedIndex(i);
				has = true;
				break;
			}
		}
		if (!has) {
			String image = "";
			if (title.equals(LogSystem))
				image = ImageContext.LogSys;
			else {
				image = ImageContext.LogTask;
			}
			tab.addTab(title, image, getLogDisplay(title).getScrlLog(), title, false);
			tab.setSelectedIndex(tab.getTabCount() - 1);
		}

	}

	// 获取当前页面
	private String getSelectTab() {
		int index = tab.getSelectedIndex();
		if (index < 0)
			return null;
		return tab.getTitleAt(index);
	}

	public STabbedPane getTab() {
		return tab;
	}

	// 根据标题获取日志显示面板
	public LogDisplay getLogDisplay(String title) {
		LogDisplay logDisplay = mapLogDisplay.get(title);
		if (logDisplay == null) {
			logDisplay = new LogDisplay();
			mapLogDisplay.put(title, logDisplay);
		}
		return logDisplay;
	}

	// 根据标题获取当前日志显示面板
	public LogDisplay getNowLogDisplay(String title) {
		return getLogDisplay(getSelectTab());
	}

}
