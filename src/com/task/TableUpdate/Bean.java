package com.task.TableUpdate;


public class Bean {
	// 源数据库连接
	private String srcDbName;
	// 源表查询SQL
	private String srcSelectSql;
	// 源表比较关键字
	private String srcCompareKey;
	// 源表比较字段
	private String srcCompareFields;
	// 目标数据连接
	private String dstDbName;
	// 目标表查询SQL
	private String dstSelectSql;
	// 目标表比较关键字
	private String dstCompareKey;
	// 目标表比较字段
	private String dstCompareFields;
	// 插入SQL
	private String dstInsertSql;
	// 更新SQL
	private String dstUpdateSql;
	// 当比较不同时是否插入
	private boolean ifInsertWhenDifferent;
	// 当比较不同时是否更新
	private boolean ifUpdateWhenDifferent;
	// 当不存在时是否插入
	private boolean ifInsertWhenNotExist;

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

	public String getDstSelectSql() {
		return dstSelectSql;
	}

	public void setDstSelectSql(String dstSelectSql) {
		this.dstSelectSql = dstSelectSql;
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

	public String getSrcCompareKey() {
		return srcCompareKey;
	}

	public void setSrcCompareKey(String srcCompareKey) {
		this.srcCompareKey = srcCompareKey;
	}

	public String getSrcCompareFields() {
		return srcCompareFields;
	}

	public void setSrcCompareFields(String srcCompareFields) {
		this.srcCompareFields = srcCompareFields;
	}

	public String getDstCompareKey() {
		return dstCompareKey;
	}

	public void setDstCompareKey(String dstCompareKey) {
		this.dstCompareKey = dstCompareKey;
	}

	public String getDstCompareFields() {
		return dstCompareFields;
	}

	public void setDstCompareFields(String dstCompareFields) {
		this.dstCompareFields = dstCompareFields;
	}

	public boolean isIfInsertWhenDifferent() {
		return ifInsertWhenDifferent;
	}

	public void setIfInsertWhenDifferent(boolean ifInsertWhenDifferent) {
		this.ifInsertWhenDifferent = ifInsertWhenDifferent;
	}

	public boolean isIfUpdateWhenDifferent() {
		return ifUpdateWhenDifferent;
	}

	public void setIfUpdateWhenDifferent(boolean ifUpdateWhenDifferent) {
		this.ifUpdateWhenDifferent = ifUpdateWhenDifferent;
	}

	public boolean isIfInsertWhenNotExist() {
		return ifInsertWhenNotExist;
	}

	public void setIfInsertWhenNotExist(boolean ifInsertWhenNotExist) {
		this.ifInsertWhenNotExist = ifInsertWhenNotExist;
	}
}
