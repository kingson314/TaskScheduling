package com.app;


import com.log.abridgement.LogAbridgementTab;
import com.log.common.LogCommonTab;
import com.log.custom.LogCustomTab;
import com.log.lately.LogLatelyTab;
import com.log.sche.LogScheTab;
import com.scher.ScherTab;
import com.taskInterface.TaskTab;
import com.taskgroup.TaskGroupTab;
import com.threadPool.ThreadTab;
import common.component.STabbedPane;

import consts.Const;
import consts.ImageContext;

/**
 * @info 程序表格视图
 * 
 * @author fgq 20120831
 * 
 */
public class AppTableView {
	private static STabbedPane tab = new STabbedPane(null);
	private static AppTableView appTableView = null;

	public static AppTableView getInstance() {
		if (appTableView == null)
			appTableView = new AppTableView();
		return appTableView;
	}

	// 构造，添加默认的3个列表
	private AppTableView() {
		tab.setFont(Const.tfont);
		tab.addTab("任务组列表", ImageContext.TabTaskGroup, TaskGroupTab.getInstance().getPnlTab(), "任务组列表", false);
		tab.addTab("任务列表", ImageContext.TabTask, TaskTab.getInstance().getPnlTab(), "任务列表", false);
		tab.addTab("调度列表", ImageContext.TabSche, ScherTab.getInstance().getPnlTab(), "调度列表", false);
	}

	// 添加列表
	public void addTab(String title) {
		boolean has = tab.hasTab(title);
		if (!has) {
			if (title.equals("任务列表"))
				tab.addTab(title, ImageContext.TabTask, TaskTab.getInstance().getPnlTab(), title, false);
			else if (title.equals("任务组列表"))
				tab.addTab(title, ImageContext.TabTaskGroup, TaskGroupTab.getInstance().getPnlTab(), title, false);
			else if (title.equals("调度列表"))
				tab.addTab(title, ImageContext.TabSche, ScherTab.getInstance().getPnlTab(), title, false);
			else if (title.equals("执行日志"))
				tab.addTab(title, ImageContext.TabLogCommon, LogCommonTab.getInstance().getPnlTab(), title, false);
			else if (title.equals("执行日志摘要"))
				tab.addTab(title, ImageContext.TabLogAbridgement, LogAbridgementTab.getInstance().getPnlTab(), title, false);
			else if (title.equals("最近高频日志"))
				tab.addTab(title, ImageContext.TabLogLately, LogLatelyTab.getInstance().getPnlTab(), title, false);
			else if (title.equals("最近调度日志"))
				tab.addTab(title, ImageContext.TabLogSche, LogScheTab.getInstance().getPnlTab(), title, false);
			else if (title.equals("定制日志"))
				tab.addTab(title, ImageContext.TabThread, LogCustomTab.getInstance().getPnlTab(), title, false);
			else if (title.equals("线程列表"))
				tab.addTab(title, ImageContext.TabThread, ThreadTab.getInstance().getPnlTab(), title, false);

			tab.setSelectedIndex(tab.getTabCount() - 1);
		}
	}

	public STabbedPane getTab() {
		return tab;
	}

}
