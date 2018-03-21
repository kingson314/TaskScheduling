package com.task.Economic;

public class Bean {
	// 数据源
	private String dbName;
	// 数据网站名称
	private String source;
	// 财经数据来源url
	private String url;
	// 是否导入经济数据
	private boolean isEnconomicData;
	// 是否导入财经事件
	private boolean isEnconomicEvent;
	// 是否导入假期预告
	private boolean isEnconomicHoliday;
	// 是否导入国债发行预告
	private boolean isEnconomicNationalDebt;
	// 是否备份网页
	private boolean isSaveFile;
	// 备份网页文件路径
	private String saveFilePath;

	public String getSaveFilePath() {
		return saveFilePath;
	}

	public void setSaveFilePath(String saveFilePath) {
		this.saveFilePath = saveFilePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isEnconomicData() {
		return isEnconomicData;
	}

	public void setEnconomicData(boolean isEnconomicData) {
		this.isEnconomicData = isEnconomicData;
	}

	public boolean isEnconomicEvent() {
		return isEnconomicEvent;
	}

	public void setEnconomicEvent(boolean isEnconomicEvent) {
		this.isEnconomicEvent = isEnconomicEvent;
	}

	public boolean isEnconomicHoliday() {
		return isEnconomicHoliday;
	}

	public void setEnconomicHoliday(boolean isEnconomicHoliday) {
		this.isEnconomicHoliday = isEnconomicHoliday;
	}

	public boolean isEnconomicNationalDebt() {
		return isEnconomicNationalDebt;
	}

	public void setEnconomicNationalDebt(boolean isEnconomicNationalDebt) {
		this.isEnconomicNationalDebt = isEnconomicNationalDebt;
	}

	public boolean isSaveFile() {
		return isSaveFile;
	}

	public void setSaveFile(boolean isSaveFile) {
		this.isSaveFile = isSaveFile;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
