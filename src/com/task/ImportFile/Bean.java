package com.task.ImportFile;

public class Bean {

	private String filePath;
	private String separate;
	private String DbName;
	private String keySql;
	private String keyColumnIndex;
	private String updateSql;
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


	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}



	public String getKeySql() {
		return keySql;
	}

	public void setKeySql(String keySql) {
		this.keySql = keySql;
	}

	public String getKeyColumnIndex() {
		return keyColumnIndex;
	}

	public void setKeyColumnIndex(String keyColumnIndex) {
		this.keyColumnIndex = keyColumnIndex;
	}

}
