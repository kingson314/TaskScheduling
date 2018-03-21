package com.task.BackMySQL;

public class Bean {
	// 文档路径
	private String filePath;

	// 数据库连接
	private String dbName;
	// 数据库安装路径
	private String dbPath;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getDbPath() {
		return dbPath;
	}
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	 
}
