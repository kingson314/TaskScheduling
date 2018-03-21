package com.task.TimingmaximinRangeRate;

public class Bean {
    private String dbName;
    private String symbol;
    // 经济数据发布日期
    private String begDate;
    // 经济数据发布日期
    private String endDate;

    private String range;

    private int minute;// 极值所在的前后第几分钟

    public String getDbName() {
	return dbName;
    }

    public void setDbName(String dbName) {
	this.dbName = dbName;
    }

    public String getBegDate() {
	return begDate;
    }

    public void setBegDate(String begDate) {
	this.begDate = begDate;
    }

    public String getEndDate() {
	return endDate;
    }

    public void setEndDate(String endDate) {
	this.endDate = endDate;
    }

    public String getSymbol() {
	return symbol;
    }

    public void setSymbol(String symbol) {
	this.symbol = symbol;
    }

    public String getRange() {
	return range;
    }

    public void setRange(String range) {
	this.range = range;
    }

    public int getMinute() {
	return minute;
    }

    public void setMinute(int minute) {
	this.minute = minute;
    }

}
