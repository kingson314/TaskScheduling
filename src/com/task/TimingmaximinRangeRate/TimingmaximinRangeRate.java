package com.task.TimingmaximinRangeRate;

/**
 * @Description: 统计某一指标在某一时间点前后价格波动极值出现在一定波动区间内的频率(用于统计出指标对价格影响波动大小)
 * @date Jul 15, 2014
 * @author:fgq
 */
public class TimingmaximinRangeRate {
	private String indicatorId;// economic_indicator.id
	private String symbol;// 货币名称
	private String range;// 波动区间;例如[10,20]
	private int count;// 该波动区间出现的次数
	private int totalCount;// 所有波动区间出现的总次数
	private double rate;// 该波动区间出现的次数占总次数的比率
	private double average;// 该波动区间内的平均波动值
	private double maxValue;// 该波动区间内的最大波动值
	private double minValue;// 该波动区间内的最小波动值
	private int minute;// 第N分钟的极值分析

	public TimingmaximinRangeRate(String indicatorId, String symbol, int totalCount,int minute) {
		this.indicatorId = indicatorId;
		this.symbol = symbol;
		this.totalCount = totalCount;
		this.minute=minute;
	}

	public String getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(String indicatorId) {
		this.indicatorId = indicatorId;
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}
}
