package com.task.ExportExcel;

public class Bean {
	// 文档路径
	private String docPath;
	// 文档名称
	private String docName;
	// 文档标题
	private String title;
	// 数据库连接
	private String dbName;
	// 查询SQL
	private String sql;
	// 参数映射规则
	private String sqlRule;

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getSqlRule() {
		return sqlRule;
	}

	public void setSqlRule(String sqlRule) {
		this.sqlRule = sqlRule;
	}
}
