package com.task.ImportPriceFromForex;

/**
 * @Description:
 * @date Mar 4, 2014
 * @author:fgq
 */
public class Price {
	public String timeServer;
	public String dateServer;
	public String timeLocal;
	public String dateLocal;
	public double open;
	public double close;
	public double high;
	public double low;
	public double ma5;
	public double ma20;
	public double ma60;
	public double kdj;
	public double volume;

	/**
	* @Description:重新初始化时最后一根线的收盘价就是下一根线的开盘价
	* @param dateServer
	* @param timeServer
	* @param close 
	* @date 2014-6-23
	* @author:fgq
	 */
	public Price(String dateServer, String timeServer,double lastClose) {
		this.timeServer = timeServer;
		this.dateServer = dateServer;
		this.timeLocal = "000000";
		this.dateLocal = "00000000";
		this.open = lastClose;
		this.close = 0;
		this.high = 0;
		this.low = 999999999;
		this.volume = 0;
	}

	public Price() {
		this.timeServer = "000000";
		this.dateServer = "00000000";
		this.timeLocal = "000000";
		this.dateLocal = "00000000";
		this.open = 0;
		this.close = 0;
		this.high = 0;
		this.low = 999999999;
		this.volume = 0;
	}

	public Price(String[] lineArr) {
		// <TICKER>,<DTYYYYMMDD>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>
		this.timeServer = lineArr[2];
		this.dateServer = lineArr[1];
		this.timeLocal = lineArr[2];
		this.dateLocal = lineArr[1];
		this.open = Double.valueOf(lineArr[3]);
		this.high = Double.valueOf(lineArr[4]);
		this.low = Double.valueOf(lineArr[5]);
		this.close = Double.valueOf(lineArr[6]);
		this.volume = Double.valueOf(lineArr[7]);
	}

	public Price(String[] lineArr, double openLast) {
		// <TICKER>,<DTYYYYMMDD>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>
		this.timeServer = lineArr[2];
		this.dateServer = lineArr[1];
		this.timeLocal = lineArr[2];
		this.dateLocal = lineArr[1];
		this.open=openLast;
		this.close = Double.valueOf(lineArr[6]);
		this.high = Double.valueOf(lineArr[4]);
		this.low = Double.valueOf(lineArr[5]);
		this.volume = Double.valueOf(lineArr[7]);
	}
}
