package com.task.UpdateDateTimeLocal;

public class Bean {
	private String dbName;
	private String symbol;
	private int timeDiffDaylightSaving;// 夏令时时间差（分钟）
	private int timeDiffWinterTime;// 冬令时时间差（分钟）
	private boolean isDetail;
	private boolean isPeriod1;
	private boolean isPeriod5;
	private boolean isPeriod15;
	private boolean isPeriod30;
	private boolean isPeriod60;
	private boolean isPeriod240;
	private boolean isPeriod1440;

	public int getTimeDiffDaylightSaving() {
		return timeDiffDaylightSaving;
	}

	public void setTimeDiffDaylightSaving(int timeDiffDaylightSaving) {
		this.timeDiffDaylightSaving = timeDiffDaylightSaving;
	}

	public int getTimeDiffWinterTime() {
		return timeDiffWinterTime;
	}

	public void setTimeDiffWinterTime(int timeDiffWinterTime) {
		this.timeDiffWinterTime = timeDiffWinterTime;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public boolean isDetail() {
		return isDetail;
	}

	public void setDetail(boolean isDetail) {
		this.isDetail = isDetail;
	}

	public boolean isPeriod1() {
		return isPeriod1;
	}

	public void setPeriod1(boolean isPeriod1) {
		this.isPeriod1 = isPeriod1;
	}

	public boolean isPeriod5() {
		return isPeriod5;
	}

	public void setPeriod5(boolean isPeriod5) {
		this.isPeriod5 = isPeriod5;
	}

	public boolean isPeriod15() {
		return isPeriod15;
	}

	public void setPeriod15(boolean isPeriod15) {
		this.isPeriod15 = isPeriod15;
	}

	public boolean isPeriod30() {
		return isPeriod30;
	}

	public void setPeriod30(boolean isPeriod30) {
		this.isPeriod30 = isPeriod30;
	}

	public boolean isPeriod60() {
		return isPeriod60;
	}

	public void setPeriod60(boolean isPeriod60) {
		this.isPeriod60 = isPeriod60;
	}

	public boolean isPeriod240() {
		return isPeriod240;
	}

	public void setPeriod240(boolean isPeriod240) {
		this.isPeriod240 = isPeriod240;
	}

	public boolean isPeriod1440() {
		return isPeriod1440;
	}

	public void setPeriod1440(boolean isPeriod1440) {
		this.isPeriod1440 = isPeriod1440;
	}
}
