package com.task.ImportExcel;


public class Bean {
	// 文档路径
	private String docDir;
	// 文档页面 从0开始
	private int docSheetIndex;
	// 数据库连接
	private String dbName;
	// 插入SQL
	private String insertSql;
	// 清空SQL
	private String delSql;
	// 文档中的字段规则
	private String docFieldRule;
	// 读取开始的行数
	private int startLine;
	// 忽略倒数N行
	private int ignoreLastLines;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;

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

	public int getIgnoreLastLines() {
		return ignoreLastLines;
	}

	public void setIgnoreLastLines(int ignoreLastLines) {
		this.ignoreLastLines = ignoreLastLines;
	}

	public String getDocDir() {
		return docDir;
	}

	public void setDocDir(String docDir) {
		this.docDir = docDir;
	}

	public int getDocSheetIndex() {
		return docSheetIndex;
	}

	public void setDocSheetIndex(int docSheetIndex) {
		this.docSheetIndex = docSheetIndex;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public String getDocFieldRule() {
		return docFieldRule;
	}

	public void setDocFieldRule(String docFieldRule) {
		this.docFieldRule = docFieldRule;
	}
}
