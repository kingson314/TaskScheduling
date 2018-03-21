package com.task.Procedure;

public class Bean {
	// 存储过程名称
	private String pName;
	// [参数名,参数数据类型，参数输入输出类型，参数值]
	private String pParamsRule;
	// 数据库连接
	private String pDbName;
	// 是否错误警告
	private boolean ifErrorWarn;
	// 比较的参数
	private String comparePapam;
	// 比较类型
	private String compareType;
	// 比较值
	private String compareValue;
	// 警告信息
	private String warning;

	public String getComparePapam() {
		return comparePapam;
	}

	public void setComparePapam(String comparePapam) {
		this.comparePapam = comparePapam;
	}

	public String getCompareType() {
		return compareType;
	}

	public void setCompareType(String compareType) {
		this.compareType = compareType;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getPName() {
		return pName;
	}

	public void setPName(String name) {
		pName = name;
	}

	public String getPParamsRule() {
		return pParamsRule;
	}

	public void setPParamsRule(String paramsRule) {
		pParamsRule = paramsRule;
	}

	public String getPDbName() {
		return pDbName;
	}

	public void setPDbName(String dbName) {
		pDbName = dbName;
	}

	public String getCompareValue() {
		return compareValue;
	}

	public void setCompareValue(String compareValue) {
		this.compareValue = compareValue;
	}

	public boolean isIfErrorWarn() {
		return ifErrorWarn;
	}

	public void setIfErrorWarn(boolean ifErrorWarn) {
		this.ifErrorWarn = ifErrorWarn;
	}

}
