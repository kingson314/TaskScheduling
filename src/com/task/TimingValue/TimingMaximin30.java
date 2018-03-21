package com.task.TimingValue;

/**
 * @Description:
 * @date 2014-8-30
 * @author:fgq
 */
public class TimingMaximin30 {
	private String id;
	private String symbol;
	// economic_data.id
	private String economicDataId;
	private double maxBefore;
	private double minBefore;
	private double extremumBefore;
	private double maxAfter;
	private double minAfter;
	private double extremumAfter;
	private String trendBefore;
	private String trendMaxAfter;
	private String trendMinAfter;
	private String trendExtremumAfter;
	private String statisticalResultBefore;
	private String statisticalResultMaxAfter;
	private String statisticalResultMinAfter;
	private String statisticalResultExtremumAfter;

	public TimingMaximin30(String symbol, String economicDataId) {
		this.symbol = symbol;
		this.economicDataId = economicDataId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEconomicDataId() {
		return economicDataId;
	}

	public void setEconomicDataId(String economicDataId) {
		this.economicDataId = economicDataId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getMaxBefore() {
		return maxBefore;
	}

	public void setMaxBefore(double maxBefore) {
		this.maxBefore = maxBefore;
	}

	public double getMinBefore() {
		return minBefore;
	}

	public void setMinBefore(double minBefore) {
		this.minBefore = minBefore;
	}

	public double getExtremumBefore() {
		return extremumBefore;
	}

	public void setExtremumBefore(double extremumBefore) {
		this.extremumBefore = extremumBefore;
	}

	public double getMaxAfter() {
		return maxAfter;
	}

	public void setMaxAfter(double maxAfter) {
		this.maxAfter = maxAfter;
	}

	public double getMinAfter() {
		return minAfter;
	}

	public void setMinAfter(double minAfter) {
		this.minAfter = minAfter;
	}

	public double getExtremumAfter() {
		return extremumAfter;
	}

	public void setExtremumAfter(double extremumAfter) {
		this.extremumAfter = extremumAfter;
	}

	public String getTrendBefore() {
		return trendBefore;
	}

	public void setTrendBefore(String trendBefore) {
		this.trendBefore = trendBefore;
	}

	public String getStatisticalResultBefore() {
		return statisticalResultBefore;
	}

	public void setStatisticalResultBefore(String statisticalResultBefore) {
		this.statisticalResultBefore = statisticalResultBefore;
	}

	public String getTrendMaxAfter() {
		return trendMaxAfter;
	}

	public void setTrendMaxAfter(String trendMaxAfter) {
		this.trendMaxAfter = trendMaxAfter;
	}

	public String getTrendMinAfter() {
		return trendMinAfter;
	}

	public void setTrendMinAfter(String trendMinAfter) {
		this.trendMinAfter = trendMinAfter;
	}

	public String getTrendExtremumAfter() {
		return trendExtremumAfter;
	}

	public void setTrendExtremumAfter(String trendExtremumAfter) {
		this.trendExtremumAfter = trendExtremumAfter;
	}

	public String getStatisticalResultMaxAfter() {
		return statisticalResultMaxAfter;
	}

	public void setStatisticalResultMaxAfter(String statisticalResultMaxAfter) {
		this.statisticalResultMaxAfter = statisticalResultMaxAfter;
	}

	public String getStatisticalResultMinAfter() {
		return statisticalResultMinAfter;
	}

	public void setStatisticalResultMinAfter(String statisticalResultMinAfter) {
		this.statisticalResultMinAfter = statisticalResultMinAfter;
	}

	public String getStatisticalResultExtremumAfter() {
		return statisticalResultExtremumAfter;
	}

	public void setStatisticalResultExtremumAfter(String statisticalResultExtremumAfter) {
		this.statisticalResultExtremumAfter = statisticalResultExtremumAfter;
	}

}
