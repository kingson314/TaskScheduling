package com.taskInterface;

import javax.swing.JPanel;

//任务面板接口
public interface ITaskPanel {
	public JPanel getPanel();
	//填充任务面板
	public void fillComp(ITask task);
	// 实例化任务对象
	public boolean fillTask(ITask task);
}
