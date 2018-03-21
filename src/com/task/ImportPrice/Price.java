package com.task.ImportPrice;

/**
 * @Description:
 * @date Mar 4, 2014
 * @author:fgq
 */
public class Price {
	private long bars;
	private String timeServer;
	private String dateServer;
	private String timeLocal;
	private String dateLocal;
	private double ask;
	private double bid;
	private double open;
	private double close;
	private double high;
	private double low;
	private double ma5;
	private double ma20;
	private double ma60;
	private double kdj;
	private double volume;

	public Price(String[] lineArr) {
		bars = Long.valueOf(lineArr[0]);
		timeServer = lineArr[1];
		dateServer = lineArr[2];
		timeLocal = lineArr[3];
		dateLocal = lineArr[4];
		ask = Double.valueOf(lineArr[5]);
		bid = Double.valueOf(lineArr[6]);
		open = Double.valueOf(lineArr[7]);
		close = Double.valueOf(lineArr[8]);
		high = Double.valueOf(lineArr[9]);
		low = Double.valueOf(lineArr[10]);
		ma5 = Double.valueOf(lineArr[11]);
		ma20 = Double.valueOf(lineArr[12]);
		ma60 = Double.valueOf(lineArr[13]);
		kdj = Double.valueOf(lineArr[14]);
		volume = Double.valueOf(lineArr[15]);
	}

	public long getBars() {
		return bars;
	}

	public void setBars(long bars) {
		this.bars = bars;
	}

	public String getTimeServer() {
		return timeServer;
	}

	public void setTimeServer(String timeServer) {
		this.timeServer = timeServer;
	}

	public String getDateServer() {
		return dateServer;
	}

	public void setDateServer(String dateServer) {
		this.dateServer = dateServer;
	}

	public String getTimeLocal() {
		return timeLocal;
	}

	public void setTimeLocal(String timeLocal) {
		this.timeLocal = timeLocal;
	}

	public String getDateLocal() {
		return dateLocal;
	}

	public void setDateLocal(String dateLocal) {
		this.dateLocal = dateLocal;
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
	}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getMa5() {
		return ma5;
	}

	public void setMa5(double ma5) {
		this.ma5 = ma5;
	}

	public double getMa20() {
		return ma20;
	}

	public void setMa20(double ma20) {
		this.ma20 = ma20;
	}

	public double getMa60() {
		return ma60;
	}

	public void setMa60(double ma60) {
		this.ma60 = ma60;
	}

	public double getKdj() {
		return kdj;
	}

	public void setKdj(double kdj) {
		this.kdj = kdj;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}
}
