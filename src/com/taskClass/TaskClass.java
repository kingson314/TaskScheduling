package com.taskClass;

//子任务类Bean
public class TaskClass {
	// 任务类型
	private String taskType;
	// 子任务jar路径
	private String jarPath;
	// 子任务Task包路径
	private String taskPath;
	// 子任务Panel包路径
	private String panelPath;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getJarPath() {
		return jarPath;
	}

	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}

	public String getTaskPath() {
		return taskPath;
	}

	public void setTaskPath(String taskPath) {
		this.taskPath = taskPath;
	}

	public String getPanelPath() {
		return panelPath;
	}

	public void setPanelPath(String panelPath) {
		this.panelPath = panelPath;
	}

}
