package com.task.TableUpdateForBigData;

public class Bean {
	// 源数据库连接
	private String srcDbName;
	// 源表查询SQL
	private String srcSelectSql;
	// 源表关键字(具备排序能力)
	private String srcKey;
	// 目标数据连接
	private String dstDbName;
	// 目标表关键字
	private String dstKey;
	// 插入SQL
	private String dstInsertSql;
	// 更新SQL
	private String dstUpdateSql;

	public String getSrcDbName() {
		return this.srcDbName;
	}

	public void setSrcDbName(String srcDbName) {
		this.srcDbName = srcDbName;

	}

	public String getDstDbName() {
		return this.dstDbName;
	}

	public void setDstDbName(String dstDbName) {
		this.dstDbName = dstDbName;
	}

	public String getSrcSelectSql() {
		return srcSelectSql;
	}

	public void setSrcSelectSql(String srcSelectSql) {
		this.srcSelectSql = srcSelectSql;
	}

	public String getDstInsertSql() {
		return dstInsertSql;
	}

	public void setDstInsertSql(String dstInsertSql) {
		this.dstInsertSql = dstInsertSql;
	}

	public String getDstUpdateSql() {
		return dstUpdateSql;
	}

	public void setDstUpdateSql(String dstUpdateSql) {
		this.dstUpdateSql = dstUpdateSql;
	}

	public String getSrcKey() {
		return srcKey;
	}

	public void setSrcKey(String srcKey) {
		this.srcKey = srcKey;
	}

	public String getDstKey() {
		return dstKey;
	}

	public void setDstKey(String dstKey) {
		this.dstKey = dstKey;
	}

}
