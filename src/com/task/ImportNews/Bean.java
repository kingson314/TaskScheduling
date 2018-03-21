package com.task.ImportNews;

public class Bean {

	private String filePath;
	private String separate;
	private String DbName;
	private String delSql;
	private String insertSql;

	public String getDbName() {
		return DbName;
	}

	public void setDbName(String dbName) {
		DbName = dbName;
	}

	public String getSeparate() {
		return separate;
	}

	public void setSeparate(String separate) {
		this.separate = separate;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getDelSql() {
		return delSql;
	}

	public void setDelSql(String delSql) {
		this.delSql = delSql;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
