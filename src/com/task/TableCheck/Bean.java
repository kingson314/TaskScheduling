package com.task.TableCheck;


public class Bean {
	// 数据库连接
	private String tcDbName;
	// 是否使用记录数限制警告
	private Boolean tcIfNumLimitedWarn;
	// 限制记录数
	private Long tcnumLimited;
	// 是否使用记录数提示
	private Boolean tcIfNumWarn;
	// 查询SQL
	private String tcSQL;
	// 参数映射
	private String tcParamsRule;
	// 比较类型
	private String compareType;
	// 记录数限制警告信息
	private String tcNumLimitedWarning;
	// 记录数提示信息
	private String tcNumWarning;

	public String getTcNumLimitedWarning() {
		return tcNumLimitedWarning;
	}

	public void setTcNumLimitedWarning(String tcNumLimitedWarning) {
		this.tcNumLimitedWarning = tcNumLimitedWarning;
	}

	public String getTcNumWarning() {
		return tcNumWarning;
	}

	public void setTcNumWarning(String tcNumWarning) {
		this.tcNumWarning = tcNumWarning;
	}

	public String getTcDbName() {
		return tcDbName;
	}

	public void setTcDbName(String tcDbName) {
		this.tcDbName = tcDbName;
	}

	public Boolean getTcIfNumLimitedWarn() {
		return tcIfNumLimitedWarn;
	}

	public void setTcIfNumLimitedWarn(Boolean tcIfNumLimitedWarn) {
		this.tcIfNumLimitedWarn = tcIfNumLimitedWarn;
	}

	public Long getTcnumLimited() {
		return tcnumLimited;
	}

	public void setTcnumLimited(Long tcnumLimited) {
		this.tcnumLimited = tcnumLimited;
	}

	public Boolean getTcIfNumWarn() {
		return tcIfNumWarn;
	}

	public void setTcIfNumWarn(Boolean tcIfNumWarn) {
		this.tcIfNumWarn = tcIfNumWarn;
	}

	public String getTcSQL() {
		return tcSQL;
	}

	public void setTcSQL(String tcSQL) {
		this.tcSQL = tcSQL;
	}

	public String getTcParamsRule() {
		return tcParamsRule;
	}

	public void setTcParamsRule(String tcParamsRule) {
		this.tcParamsRule = tcParamsRule;
	}

	public String getCompareType() {
		return compareType;
	}

	public void setCompareType(String compareType) {
		this.compareType = compareType;
	}
}
