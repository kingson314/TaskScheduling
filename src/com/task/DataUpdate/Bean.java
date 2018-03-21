package com.task.DataUpdate;


public class Bean {
	// 源数据库连接
	private String srcDbName;
	// 源表查询SQL
	private String srcSql;
	// 目的数据库连接
	private String dstDbName;
	// 目标插入SQL
	private String dstSql;
	// 参数映射规则
	private String ruleSql;
	// 更新类型
	private String updateType;
	// 最大值SQL
	private String maxValueSql;

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getSrcDbName() {
		return this.srcDbName;
	}

	public void setSrcDbName(String srcDbName) {
		this.srcDbName = srcDbName;
	}

	public String getSrcSql() {
		return this.srcSql;
	}

	public void setSrcSql(String srcSql) {
		this.srcSql = srcSql;
	}

	public String getDstDbName() {
		return this.dstDbName;
	}

	public void setDstDbName(String dstDbName) {
		this.dstDbName = dstDbName;
	}

	public String getDstSql() {
		return this.dstSql;
	}

	public void setDstSql(String dstSql) {
		this.dstSql = dstSql;
	}

	public String getMaxValueSql() {
		return maxValueSql;
	}

	public void setMaxValueSql(String maxValueSql) {
		this.maxValueSql = maxValueSql;
	}

	public String getRuleSql() {
		return ruleSql;
	}

	public void setRuleSql(String ruleSql) {
		this.ruleSql = ruleSql;
	}
}
