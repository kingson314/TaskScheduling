package com.task.Ccgp;

public class Bean {
	// 数据源
	private String dbName;
	 //爬取第一页到第pageIndex页
	private int pageIndex;
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	 
}
