package com.task.TimingValue;

public class Bean {
    private String dbName;
    // 持续时间
    private int duration;
    // 经济数据发布日期
    private String begDate;
    // 经济数据发布日期
    private String endDate;

    public String getDbName() {
	return dbName;
    }

    public void setDbName(String dbName) {
	this.dbName = dbName;
    }

    public int getDuration() {
	return duration;
    }

    public void setDuration(int duration) {
	this.duration = duration;
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

}
