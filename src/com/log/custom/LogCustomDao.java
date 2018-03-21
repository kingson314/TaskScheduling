package com.log.custom;

import java.util.List;
import java.util.Vector;

import com.settings.SettingsDao;

public class LogCustomDao {

	private List<Object> table;

	// 构造
	public LogCustomDao() {
		// 执行定制查询SQL子任务
		com.task.CustomQuery.Task task = new com.task.CustomQuery.Task();
		String jsonStr = SettingsDao.getInstance().getValue("定制日志");
		if (jsonStr.equals(""))
			return;
		task.setJsonStr(jsonStr);
		task.fireTask();
		this.table = task.getList();
	}

	// 获取查询标题
	@SuppressWarnings("unchecked")
	public Vector<String> getTitle() {
		if (this.table == null)
			return new Vector<String>();
		Vector<String> title = (Vector<String>) this.table.get(0);
		return title;
	}

	// 获取查询内容
	@SuppressWarnings("unchecked")
	public Vector<Vector<String>> getTableValue() {
		if (this.table == null)
			return new Vector<Vector<String>>();
		Vector<Vector<String>> tableValue = (Vector<Vector<String>>) this.table
				.get(1);
		return tableValue;
	}
}
