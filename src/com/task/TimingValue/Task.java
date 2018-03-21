package com.task.TimingValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.taskInterface.TaskAbstract;

import common.util.conver.UtilConver;
import common.util.date.UtilDate;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

/**
 * @Description:某一时间点前后价差轨迹 只支持手工执行
 * @date Jul 10, 2014
 * @author:fgq
 */
public class Task extends TaskAbstract {
	private Bean bean;
	private Connection con;
	private static HashMap<String, Integer> MapSymbolDigits = null;// 货币的小数点位数
	private final String sa_timingvalue = " sa_timingvalue ";
	private final String sa_timingmaximin = " sa_timingmaximin ";
	// 获取在指定日期范围内的并且在指标与货币关联表中设置的指标ID
	private String sqlEconomicData = "select distinct a.id,a.indicator,a.publishDate, a.publishTime, a.indicatorId, a.predictedresult ,a.compareresult,c.symbol,d.indicatoreffect "
			+ "from economic_data a, relate_indicator_symbol b,config_symbol c,economic_indicator d "
			+ "where a.indicatorId = b.indicatorId and a.indicatorId = d.id  and b.symbolid=c.id and a.publishDate between ? and ? "
			+ "order by a.id,c.symbol,a.publishDate, a.publishTime";
	private String sqlTimingValue = "insert into "
			+ sa_timingvalue
			+ "(symbol,priceType,economicDataId,valueMinuteBefore1,valueMinuteBefore2,valueMinuteBefore3,valueMinuteBefore4,valueMinuteBefore5,"
			+ "valueMinuteAfter1,valueMinuteAfter2, valueMinuteAfter3,valueMinuteAfter4,valueMinuteAfter5,valueMinuteAfter6,valueMinuteAfter7,valueMinuteAfter8,valueMinuteAfter9,valueMinuteAfter10,"
			+ "valueMinuteAfter11,valueMinuteAfter12,valueMinuteAfter13,valueMinuteAfter14,valueMinuteAfter15,valueMinuteAfter16,valueMinuteAfter17,valueMinuteAfter18,valueMinuteAfter19,valueMinuteAfter20,"
			+ "valueMinuteAfter21,valueMinuteAfter22,valueMinuteAfter23,valueMinuteAfter24,valueMinuteAfter25,valueMinuteAfter26,valueMinuteAfter27,valueMinuteAfter28,valueMinuteAfter29,valueMinuteAfter30,"
			+ "valueMinuteAfter35,valueMinuteAfter40,valueMinuteAfter45,valueMinuteAfter50,valueMinuteAfter55,valueMinuteAfter60,valueMinuteAfter70,valueMinuteAfter80,valueMinuteAfter90,valueMinuteAfter100,"
			+ "valueMinuteAfter110,valueMinuteAfter120)" + "values(?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?)";
	private String sqlTimingmaximin = "insert into "
			+ sa_timingmaximin
			+ "(symbol,maximinType,economicDataId,maximinMinuteBefore1,maximinMinuteBefore2,maximinMinuteBefore3,maximinMinuteBefore4,maximinMinuteBefore5,"
			+ "maximinMinuteAfter1,maximinMinuteAfter2, maximinMinuteAfter3,maximinMinuteAfter4,maximinMinuteAfter5,maximinMinuteAfter6,maximinMinuteAfter7,maximinMinuteAfter8,maximinMinuteAfter9,maximinMinuteAfter10,"
			+ "maximinMinuteAfter11,maximinMinuteAfter12,maximinMinuteAfter13,maximinMinuteAfter14,maximinMinuteAfter15,maximinMinuteAfter16,maximinMinuteAfter17,maximinMinuteAfter18,maximinMinuteAfter19,maximinMinuteAfter20,"
			+ "maximinMinuteAfter21,maximinMinuteAfter22,maximinMinuteAfter23,maximinMinuteAfter24,maximinMinuteAfter25,maximinMinuteAfter26,maximinMinuteAfter27,maximinMinuteAfter28,"
			+ "maximinMinuteAfter29,maximinMinuteAfter30,"
			+ "maximinMinuteAfter35,maximinMinuteAfter40,maximinMinuteAfter45,maximinMinuteAfter50,maximinMinuteAfter55,maximinMinuteAfter60,maximinMinuteAfter70,maximinMinuteAfter80,"
			+ "maximinMinuteAfter90,maximinMinuteAfter100,maximinMinuteAfter110,maximinMinuteAfter120)" + "VALUES(?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?)";

	private String sqlTimingmaximin30 = "insert into sa_timingmaximin30(symbol,economicDataId,maxbefore,minbefore,extremumbefore,maxafter,minafter,extremumafter,trendbefore,trendmaxafter,trendminafter,trendextremumafter,statisticalResultBefore,statisticalResultMaxAfter,statisticalResultMinAfter,statisticalResultExtremumAfter)"
			+ "VALUES(?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?)";
	// 先默认使用1分钟数据
	private long period = 1;

	public void manuExecTask(String[] params) {
		this.bean = (Bean) UtilJson.getJsonBean(this.getJsonStr(), Bean.class);
		bean.setBegDate(params[0]);
		bean.setEndDate(params[1]);
		exec();
	}

	// @Description:
	// -1、手动关联指标ID与货币ID
	// 1、手动把economic_data表关联上economic_indicator的id
	// 2、查询economic_data中关于该指标id的在查询日期区间内的所有记录，获取发布日期与发布时间
	// 3、循环economic_data获取的记录
	// 4、判断symbol_timingvalue表中该指标以及该发布日期、发布时间是否存在记录
	// 5、遍历symbol_value5_detail表
	// 6、计算该指标下时间点的峰值并插入symbol_timingvalue表中
	public void exec() {
		PreparedStatement psTimingValue = null;
		PreparedStatement psTimingMaximin = null;
		PreparedStatement psTimingMaximin30 = null;
		ResultSet rsEconomicData = null;
		PreparedStatement psEconomicData = null;
		try {
			DbConnection dbConn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbConn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbConn);
			psEconomicData = con.prepareStatement(sqlEconomicData, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			psEconomicData.setString(1, bean.getBegDate());
			psEconomicData.setString(2, bean.getEndDate());
			rsEconomicData = psEconomicData.executeQuery();

			psTimingValue = con.prepareStatement(sqlTimingValue);
			psTimingMaximin = con.prepareStatement(sqlTimingmaximin);
			psTimingMaximin30 = con.prepareStatement(sqlTimingmaximin30);
			int cnt = 0;
			String symbol = "";
			String economicDataId = "";
			String publishDate = "";
			String publishTime = "";
			String publisDateTime = "";
			String indicatoreffect = "";
			String compareResult = "";
			String predictedresult = "";
			while (rsEconomicData.next()) {
				try {
					economicDataId = rsEconomicData.getString("ID");
					symbol = rsEconomicData.getString("SYMBOL");
					publishDate = rsEconomicData.getString("PUBLISHDATE");
					publishTime = rsEconomicData.getString("PUBLISHTIME") + "00";
					publisDateTime = publishDate + " " + publishTime;
					indicatoreffect = rsEconomicData.getString("INDICATOREFFECT");
					compareResult = rsEconomicData.getString("COMPARERESULT");
					predictedresult = rsEconomicData.getString("PREDICTEDRESULT");
					if (this.isExistTimingValue(con, symbol, economicDataId)) {
						continue;
					}

					TimingValue timingValueHigh = new TimingValue(symbol, economicDataId, 2);// 最高价时点价差
					TimingValue timingValueLow = new TimingValue(symbol, economicDataId, 3);// 最低价时点价差

					TimingMaximin30 timingMaxmin30 = new TimingMaximin30(symbol, economicDataId);

					TimingMaximin timingMax = new TimingMaximin(symbol, economicDataId, 0);// 价差的最大值
					TimingMaximin timingMin = new TimingMaximin(symbol, economicDataId, 1);// 价差的最小
					double publishCloseValue = getPublishCloseValue(con, symbol, publishDate, publishTime);// 发布时间点的收盘价
					if (publishCloseValue == 0) {
						continue;
					}
					String dateTime = "";
					List<HashMap<String, String>> listDetail = this.getListPriceDetai(con, symbol, publisDateTime);
					double valueHigh = 0;
					double valueLow = 0;
					double vlaueMax = 0;
					double valueMin = 999999999;
					for (int i = 0; i < listDetail.size(); i++) {
						dateTime = listDetail.get(i).get("DATELOCAL") + " " + listDetail.get(i).get("TIMELOCAL");
						if (timingValueHigh.getValueMinuteBefore5() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= -5) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteBefore5(valueHigh);
							timingValueLow.setValueMinuteBefore5(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteBefore5(vlaueMax);
							timingMin.setMaximinMinuteBefore5(valueMin);
						} else if (timingValueHigh.getValueMinuteBefore4() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= -4) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteBefore4(valueHigh);
							timingValueLow.setValueMinuteBefore4(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteBefore4(vlaueMax);
							timingMin.setMaximinMinuteBefore4(valueMin);
						} else if (timingValueHigh.getValueMinuteBefore3() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= -3) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteBefore3(valueHigh);
							timingValueLow.setValueMinuteBefore3(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteBefore3(vlaueMax);
							timingMin.setMaximinMinuteBefore3(valueMin);
						} else if (timingValueHigh.getValueMinuteBefore2() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= -2) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteBefore2(valueHigh);
							timingValueLow.setValueMinuteBefore2(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteBefore2(vlaueMax);
							timingMin.setMaximinMinuteBefore2(valueMin);
						} else if (timingValueHigh.getValueMinuteBefore1() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= -1) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteBefore1(valueHigh);
							timingValueLow.setValueMinuteBefore1(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteBefore1(vlaueMax);
							timingMin.setMaximinMinuteBefore1(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter1() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 1) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter1(valueHigh);
							timingValueLow.setValueMinuteAfter1(valueLow);

							// 重新初始化计算经济数据公布后的最大最小值
							vlaueMax = 0;
							valueMin = 999999999;
							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter1(vlaueMax);
							timingMin.setMaximinMinuteAfter1(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter2() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 2) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter2(valueHigh);
							timingValueLow.setValueMinuteAfter2(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter2(vlaueMax);
							timingMin.setMaximinMinuteAfter2(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter3() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 3) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter3(valueHigh);
							timingValueLow.setValueMinuteAfter3(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter3(vlaueMax);
							timingMin.setMaximinMinuteAfter3(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter4() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 4) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter4(valueHigh);
							timingValueLow.setValueMinuteAfter4(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter4(vlaueMax);
							timingMin.setMaximinMinuteAfter4(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter5() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 5) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter5(valueHigh);
							timingValueLow.setValueMinuteAfter5(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter5(vlaueMax);
							timingMin.setMaximinMinuteAfter5(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter6() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 6) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter6(valueHigh);
							timingValueLow.setValueMinuteAfter6(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter6(vlaueMax);
							timingMin.setMaximinMinuteAfter6(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter7() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 7) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter7(valueHigh);
							timingValueLow.setValueMinuteAfter7(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter7(vlaueMax);
							timingMin.setMaximinMinuteAfter7(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter8() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 8) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter8(valueHigh);
							timingValueLow.setValueMinuteAfter8(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter8(vlaueMax);
							timingMin.setMaximinMinuteAfter8(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter9() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 9) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter9(valueHigh);
							timingValueLow.setValueMinuteAfter9(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter9(vlaueMax);
							timingMin.setMaximinMinuteAfter9(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter10() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 10) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter10(valueHigh);
							timingValueLow.setValueMinuteAfter10(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter10(vlaueMax);
							timingMin.setMaximinMinuteAfter10(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter11() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 11) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter11(valueHigh);
							timingValueLow.setValueMinuteAfter11(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter11(vlaueMax);
							timingMin.setMaximinMinuteAfter11(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter12() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 12) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter12(valueHigh);
							timingValueLow.setValueMinuteAfter12(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter12(vlaueMax);
							timingMin.setMaximinMinuteAfter12(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter13() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 13) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter13(valueHigh);
							timingValueLow.setValueMinuteAfter13(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter13(vlaueMax);
							timingMin.setMaximinMinuteAfter13(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter14() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 14) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter14(valueHigh);
							timingValueLow.setValueMinuteAfter14(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter14(vlaueMax);
							timingMin.setMaximinMinuteAfter14(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter15() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 15) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter15(valueHigh);
							timingValueLow.setValueMinuteAfter15(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter15(vlaueMax);
							timingMin.setMaximinMinuteAfter15(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter16() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 16) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter16(valueHigh);
							timingValueLow.setValueMinuteAfter16(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter16(vlaueMax);
							timingMin.setMaximinMinuteAfter16(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter17() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 17) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter17(valueHigh);
							timingValueLow.setValueMinuteAfter17(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter17(vlaueMax);
							timingMin.setMaximinMinuteAfter17(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter18() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 18) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter18(valueHigh);
							timingValueLow.setValueMinuteAfter18(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter18(vlaueMax);
							timingMin.setMaximinMinuteAfter18(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter19() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 19) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter19(valueHigh);
							timingValueLow.setValueMinuteAfter19(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter19(vlaueMax);
							timingMin.setMaximinMinuteAfter19(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter20() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 20) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter20(valueHigh);
							timingValueLow.setValueMinuteAfter20(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter20(vlaueMax);
							timingMin.setMaximinMinuteAfter20(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter21() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 21) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter21(valueHigh);
							timingValueLow.setValueMinuteAfter21(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter21(vlaueMax);
							timingMin.setMaximinMinuteAfter21(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter22() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 22) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter22(valueHigh);
							timingValueLow.setValueMinuteAfter22(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter22(vlaueMax);
							timingMin.setMaximinMinuteAfter22(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter23() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 23) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter23(valueHigh);
							timingValueLow.setValueMinuteAfter23(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter23(vlaueMax);
							timingMin.setMaximinMinuteAfter23(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter24() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 24) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter24(valueHigh);
							timingValueLow.setValueMinuteAfter24(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter24(vlaueMax);
							timingMin.setMaximinMinuteAfter24(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter25() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 25) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter25(valueHigh);
							timingValueLow.setValueMinuteAfter25(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter25(vlaueMax);
							timingMin.setMaximinMinuteAfter25(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter26() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 26) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter26(valueHigh);
							timingValueLow.setValueMinuteAfter26(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter26(vlaueMax);
							timingMin.setMaximinMinuteAfter26(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter27() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 27) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter27(valueHigh);
							timingValueLow.setValueMinuteAfter27(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter27(vlaueMax);
							timingMin.setMaximinMinuteAfter27(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter28() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 28) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter28(valueHigh);
							timingValueLow.setValueMinuteAfter28(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter28(vlaueMax);
							timingMin.setMaximinMinuteAfter28(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter29() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 29) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter29(valueHigh);
							timingValueLow.setValueMinuteAfter29(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter29(vlaueMax);
							timingMin.setMaximinMinuteAfter29(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter30() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 30) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter30(valueHigh);
							timingValueLow.setValueMinuteAfter30(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter30(vlaueMax);
							timingMin.setMaximinMinuteAfter30(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter35() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 35) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter35(valueHigh);
							timingValueLow.setValueMinuteAfter35(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter35(vlaueMax);
							timingMin.setMaximinMinuteAfter35(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter40() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 40) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter40(valueHigh);
							timingValueLow.setValueMinuteAfter40(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter40(vlaueMax);
							timingMin.setMaximinMinuteAfter40(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter45() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 45) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter45(valueHigh);
							timingValueLow.setValueMinuteAfter45(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter45(vlaueMax);
							timingMin.setMaximinMinuteAfter45(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter50() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 50) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter50(valueHigh);
							timingValueLow.setValueMinuteAfter50(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter50(vlaueMax);
							timingMin.setMaximinMinuteAfter50(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter55() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 55) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter55(valueHigh);
							timingValueLow.setValueMinuteAfter55(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter55(vlaueMax);
							timingMin.setMaximinMinuteAfter55(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter60() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 60) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter60(valueHigh);
							timingValueLow.setValueMinuteAfter60(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter60(vlaueMax);
							timingMin.setMaximinMinuteAfter60(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter70() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 70) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter70(valueHigh);
							timingValueLow.setValueMinuteAfter70(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter70(vlaueMax);
							timingMin.setMaximinMinuteAfter70(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter80() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 80) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter80(valueHigh);
							timingValueLow.setValueMinuteAfter80(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter80(vlaueMax);
							timingMin.setMaximinMinuteAfter80(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter90() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 90) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter90(valueHigh);
							timingValueLow.setValueMinuteAfter90(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter90(vlaueMax);
							timingMin.setMaximinMinuteAfter90(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter100() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 100) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter100(valueHigh);
							timingValueLow.setValueMinuteAfter100(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter100(vlaueMax);
							timingMin.setMaximinMinuteAfter100(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter110() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 110) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter110(valueHigh);
							timingValueLow.setValueMinuteAfter110(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter110(vlaueMax);
							timingMin.setMaximinMinuteAfter110(valueMin);
						} else if (timingValueHigh.getValueMinuteAfter120() == 0 && UtilDate.diffMinute(publisDateTime, dateTime, Const.fm_yyyyMMdd_HHmmss) >= 120) {
							valueHigh = getValueDiff(listDetail, symbol, i, publishCloseValue, 2);
							valueLow = getValueDiff(listDetail, symbol, i, publishCloseValue, 3);
							timingValueHigh.setValueMinuteAfter120(valueHigh);
							timingValueLow.setValueMinuteAfter120(valueLow);

							vlaueMax = Math.max(vlaueMax, valueHigh);
							valueMin = Math.min(valueMin, valueLow);
							timingMax.setMaximinMinuteAfter120(vlaueMax);
							timingMin.setMaximinMinuteAfter120(valueMin);
						}
					}
					if (listDetail.size() > 0) {
						this.addBatchForTimingValue(psTimingValue, timingValueHigh);
						this.addBatchForTimingValue(psTimingValue, timingValueLow);

						this.addBatchForTimingMaximin(psTimingMaximin, timingMax);
						this.addBatchForTimingMaximin(psTimingMaximin, timingMin);

						timingMaxmin30.setMaxBefore(timingMax.getMaximinMinuteBefore1());
						timingMaxmin30.setMinBefore(timingMin.getMaximinMinuteBefore1());
						timingMaxmin30.setExtremumBefore(Math.abs(timingMaxmin30.getMaxBefore()) > Math.abs(timingMaxmin30.getMinBefore()) ? timingMaxmin30.getMaxBefore()
								: timingMaxmin30.getMinBefore());
						timingMaxmin30.setMaxAfter(timingMax.getMaximinMinuteAfter30());
						timingMaxmin30.setMinAfter(timingMin.getMaximinMinuteAfter30());
						timingMaxmin30.setExtremumAfter(Math.abs(timingMaxmin30.getMaxAfter()) > Math.abs(timingMaxmin30.getMinAfter()) ? timingMaxmin30.getMaxAfter()
								: timingMaxmin30.getMinAfter());
						timingMaxmin30.setTrendBefore(timingMaxmin30.getExtremumBefore() > 0 ? "跌" : "涨");
						String statisticalResultBefore = this.getStatisticalResultBefore(symbol, indicatoreffect, predictedresult, timingMaxmin30.getTrendBefore());
						timingMaxmin30.setStatisticalResultBefore(statisticalResultBefore);

						timingMaxmin30.setTrendExtremumAfter(timingMaxmin30.getExtremumAfter() > 0 ? "涨" : "跌");
						timingMaxmin30.setTrendMaxAfter(timingMaxmin30.getMaxAfter() > 0 ? "涨" : "跌");
						timingMaxmin30.setTrendMinAfter(timingMaxmin30.getMinAfter() > 0 ? "涨" : "跌");

						String statisticalResultMaxAfter = this.getStatisticalResultAfter(symbol, indicatoreffect, compareResult, timingMaxmin30.getTrendMaxAfter());
						timingMaxmin30.setStatisticalResultMaxAfter(statisticalResultMaxAfter);
						String statisticalResultMinAfter = this.getStatisticalResultAfter(symbol, indicatoreffect, compareResult, timingMaxmin30.getTrendMinAfter());
						timingMaxmin30.setStatisticalResultMinAfter(statisticalResultMinAfter);
						String statisticalResultExtremumAfter = this.getStatisticalResultAfter(symbol, indicatoreffect, compareResult, timingMaxmin30.getTrendExtremumAfter());
						timingMaxmin30.setStatisticalResultExtremumAfter(statisticalResultExtremumAfter);
						// System.out.println("indicator:" +
						// rsEconomicData.getString("INDICATOR"));
						// System.out.println("economicDataId:" +
						// economicDataId);
						// System.out.println("publishDate:" + publishDate);
						// System.out.println("publishTime:" + publishTime);
						// System.out.println("indicatoreffect:" +
						// indicatoreffect);
						// System.out.println("compareResult:" + compareResult);
						// System.out.println("predictedresult:" +
						// predictedresult);
						// System.out.println("trendBefore:" +
						// timingMaxmin30.getTrendBefore());
						// System.out.println("trendAfter:" +
						// timingMaxmin30.getTrendAfter());
						// System.out.println("isFitBefore:" +
						// statisticalResultBefore);
						// System.out.println("isFitAfter:" +
						// statisticalResultAfter);
						// System.out.println("=======================================");
						this.addBatchForTimingMaximin30(psTimingMaximin30, timingMaxmin30);
					}
					cnt += 1;
					if (cnt != 0 && cnt % 1000 == 0) {
						try {
							psTimingValue.executeBatch();
							psTimingMaximin.executeBatch();
							psTimingMaximin30.executeBatch();
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						} finally {
							psTimingValue.clearBatch();
							psTimingMaximin.clearBatch();
							psTimingMaximin30.clearBatch();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			psTimingValue.executeBatch();
			psTimingMaximin.executeBatch();
			psTimingMaximin30.executeBatch();
			this.setTaskStatus("执行成功");
			this.setTaskMsg("数据分析完成");
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("", e);
		} finally {
			UtilSql.close(rsEconomicData, psEconomicData, psTimingValue, con);
		}
	}

	/**
	 * @Description:获取货币的小数点位数
	 * @param symbol
	 * @return int
	 * @date Jul 10, 2014
	 * @author:fgq
	 */
	private int getSymbolDigits(String symbol) {
		if (MapSymbolDigits == null)
			MapSymbolDigits = new HashMap<String, Integer>();
		if (MapSymbolDigits.get(symbol) == null || MapSymbolDigits.get(symbol) == 0) {
			String sql = "select * from config_symbol where state=0 and symbol='" + symbol + "'";
			Map<String, String> map = UtilSql.QueryA(this.con, sql);
			int digits = Integer.valueOf(map.get("DIGITS"));
			MapSymbolDigits.put(symbol, digits);
		}
		return MapSymbolDigits.get(symbol);
	}

	private double getValueDiff(List<HashMap<String, String>> listDetail, String symbol, int index, double publishCloseValue, int priceType) {
		int digits = getSymbolDigits(symbol);
		double valueDiff = 0;
		if (priceType == 2) {
			valueDiff = (Double.valueOf(listDetail.get(index).get("HIGH")) - publishCloseValue) * Math.pow(10, digits - 1);// 减掉一位，貌似是券商精确了多一位
		} else if (priceType == 3) {
			valueDiff = (Double.valueOf(listDetail.get(index).get("LOW")) - publishCloseValue) * Math.pow(10, digits - 1);
		}
		return valueDiff;
	}

	/**
	 * @Description:判断是否已存在改时点价差的数据
	 * @param con
	 * @param symbol
	 * @param indicatorId
	 * @param dateLocal
	 * @param timeLocal
	 * @return
	 * @throws Exception boolean
	 * @date 2014-6-2
	 * @author:fgq
	 */
	private boolean isExistTimingValue(Connection con, String symbol, String economicDataId) throws Exception {
		String sql = "select count(1) from " + sa_timingvalue + " where  symbol=? and economicDataId=?  ";
		return UtilSql.isExist(con, sql, new Object[] { symbol, economicDataId });
	}

	/**
	 * @Description:参数化插入时点价差表PS
	 * @param ps
	 * @param tp
	 * @throws Exception void
	 * @date 2014-6-2
	 * @author:fgq
	 */
	private void addBatchForTimingValue(PreparedStatement ps, TimingValue tp) throws Exception {
		Object[] params = new Object[] { tp.getSymbol(), tp.getPriceType(), tp.getEconomicDataId(), tp.getValueMinuteBefore1(), tp.getValueMinuteBefore2(),
				tp.getValueMinuteBefore3(), tp.getValueMinuteBefore4(), tp.getValueMinuteBefore5(), tp.getValueMinuteAfter1(), tp.getValueMinuteAfter2(),
				tp.getValueMinuteAfter3(), tp.getValueMinuteAfter4(), tp.getValueMinuteAfter5(), tp.getValueMinuteAfter6(), tp.getValueMinuteAfter7(), tp.getValueMinuteAfter8(),
				tp.getValueMinuteAfter9(), tp.getValueMinuteAfter10(), tp.getValueMinuteAfter11(), tp.getValueMinuteAfter12(), tp.getValueMinuteAfter13(),
				tp.getValueMinuteAfter14(), tp.getValueMinuteAfter15(), tp.getValueMinuteAfter16(), tp.getValueMinuteAfter17(), tp.getValueMinuteAfter18(),
				tp.getValueMinuteAfter19(), tp.getValueMinuteAfter20(), tp.getValueMinuteAfter21(), tp.getValueMinuteAfter22(), tp.getValueMinuteAfter23(),
				tp.getValueMinuteAfter24(), tp.getValueMinuteAfter25(), tp.getValueMinuteAfter26(), tp.getValueMinuteAfter27(), tp.getValueMinuteAfter28(),
				tp.getValueMinuteAfter29(), tp.getValueMinuteAfter30(), tp.getValueMinuteAfter35(), tp.getValueMinuteAfter40(), tp.getValueMinuteAfter45(),
				tp.getValueMinuteAfter50(), tp.getValueMinuteAfter55(), tp.getValueMinuteAfter60(), tp.getValueMinuteAfter70(), tp.getValueMinuteAfter80(),
				tp.getValueMinuteAfter90(), tp.getValueMinuteAfter100(), tp.getValueMinuteAfter110(), tp.getValueMinuteAfter120() };
		for (int i = 0; i < params.length; i++) {
			ps.setObject(i + 1, params[i]);
		}
		ps.addBatch();
	}

	private void addBatchForTimingMaximin(PreparedStatement ps, TimingMaximin tp) throws Exception {
		Object[] params = new Object[] { tp.getSymbol(), tp.getMaximinType(), tp.getEconomicDataId(), tp.getMaximinMinuteBefore1(), tp.getMaximinMinuteBefore2(),
				tp.getMaximinMinuteBefore3(), tp.getMaximinMinuteBefore4(), tp.getMaximinMinuteBefore5(), tp.getMaximinMinuteAfter1(), tp.getMaximinMinuteAfter2(),
				tp.getMaximinMinuteAfter3(), tp.getMaximinMinuteAfter4(), tp.getMaximinMinuteAfter5(), tp.getMaximinMinuteAfter6(), tp.getMaximinMinuteAfter7(),
				tp.getMaximinMinuteAfter8(), tp.getMaximinMinuteAfter9(), tp.getMaximinMinuteAfter10(), tp.getMaximinMinuteAfter11(), tp.getMaximinMinuteAfter12(),
				tp.getMaximinMinuteAfter13(), tp.getMaximinMinuteAfter14(), tp.getMaximinMinuteAfter15(), tp.getMaximinMinuteAfter16(), tp.getMaximinMinuteAfter17(),
				tp.getMaximinMinuteAfter18(), tp.getMaximinMinuteAfter19(), tp.getMaximinMinuteAfter20(), tp.getMaximinMinuteAfter21(), tp.getMaximinMinuteAfter22(),
				tp.getMaximinMinuteAfter23(), tp.getMaximinMinuteAfter24(), tp.getMaximinMinuteAfter25(), tp.getMaximinMinuteAfter26(), tp.getMaximinMinuteAfter27(),
				tp.getMaximinMinuteAfter28(), tp.getMaximinMinuteAfter29(), tp.getMaximinMinuteAfter30(), tp.getMaximinMinuteAfter35(), tp.getMaximinMinuteAfter40(),
				tp.getMaximinMinuteAfter45(), tp.getMaximinMinuteAfter50(), tp.getMaximinMinuteAfter55(), tp.getMaximinMinuteAfter60(), tp.getMaximinMinuteAfter70(),
				tp.getMaximinMinuteAfter80(), tp.getMaximinMinuteAfter90(), tp.getMaximinMinuteAfter100(), tp.getMaximinMinuteAfter110(), tp.getMaximinMinuteAfter120() };
		for (int i = 0; i < params.length; i++) {
			ps.setObject(i + 1, params[i]);
		}
		ps.addBatch();
	}

	// symbol,datelocal,timelocal,indicatorid,maxbefore,minbefore,extremumbefore,maxafter,minafter,extremumafter,trendbefore,trendafter,isfitbefore,isfitafter
	private void addBatchForTimingMaximin30(PreparedStatement ps, TimingMaximin30 tp) throws Exception {
		Object[] params = new Object[] { tp.getSymbol(), tp.getEconomicDataId(), tp.getMaxBefore(), tp.getMinBefore(), tp.getExtremumBefore(), tp.getMaxAfter(), tp.getMinAfter(),
				tp.getExtremumAfter(), tp.getTrendBefore(), tp.getTrendMaxAfter(), tp.getTrendMinAfter(), tp.getTrendExtremumAfter(), tp.getStatisticalResultBefore(),
				tp.getStatisticalResultMaxAfter(), tp.getStatisticalResultMinAfter(), tp.getStatisticalResultExtremumAfter() };
		for (int i = 0; i < params.length; i++) {
			ps.setObject(i + 1, params[i]);
		}
		ps.addBatch();
	}

	/**
	 * @Description:获取经济数据发布时间点的1分钟价格表的收盘价
	 * @param con
	 * @param dateLocal
	 * @param timeLocal
	 * @return
	 * @throws Exception double
	 * @date 2014-6-2
	 * @author:fgq
	 */
	private double getPublishCloseValue(Connection con, String symbol, String dateLocal, String timeLocal) throws Exception {
		String tableName = " price_" + symbol + this.period;
		String sql = "select close " + "from " + tableName + "  where dateLocal='" + dateLocal + "' and timeLocal <='" + timeLocal + "' order by timeLocal desc ";
		// System.out.println(sql);
		HashMap<String, String> mapRs = UtilSql.QueryA(con, sql, new Object[] {});
		if (mapRs == null) {
			return 0;
		} else if (mapRs.get("CLOSE") == null) {
			return 0;
		} else {
			return Double.valueOf(mapRs.get("CLOSE"));
		}
	}

	/**
	 * @Description:获取1分钟价格表中在发布时间前5分钟到120分钟的价格数据
	 * @param con
	 * @param dateTime
	 * @return
	 * @throws Exception
	 *             List<HashMap<String,String>>
	 * @date 2014-6-2
	 * @author:fgq
	 */
	private List<HashMap<String, String>> getListPriceDetai(Connection con, String symbol, String dateTime) throws Exception {
		String tableName = " price_" + symbol + this.period;
		String sql5 = "select dateLocal,timeLocal,open,close,high,low " + "from " + tableName + "  where dateLocal>=? and dateLocal<= ?"
				+ " and concat(dateLocal,' ',timeLocal) > ? and  concat(dateLocal,' ',timeLocal) <=? order by dateLocal ,timeLocal ";

		String begTime = UtilDate.addMinute(dateTime, -6, Const.fm_yyyyMMdd_HHmmss);
		String endTime = UtilDate.addMinute(dateTime, bean.getDuration(), Const.fm_yyyyMMdd_HHmmss);
		// 开始日期,结束日期 必须用此二日期用于索引
		String begdate = UtilConver.dateToStr(UtilConver.strToDate(dateTime, Const.fm_yyyyMMdd_HHmmss), Const.fm_yyyyMMdd);
		String endDate = UtilConver.dateToStr(UtilConver.strToDate(endTime, Const.fm_yyyyMMdd_HHmmss), Const.fm_yyyyMMdd);
		System.out.println("getListDetai：" + "select dateLocal,timeLocal,open,close,high,low " + "from " + tableName + "  where dateLocal>='" + begdate + "' and dateLocal<= '"
				+ endDate + "'" + " and concat(dateLocal,' ',timeLocal) > '" + begTime + "' and  concat(dateLocal,' ',timeLocal) <='" + endTime + "' order by dateLocal ,timeLocal ");
		return UtilSql.executeSql(con, sql5, new Object[] { begdate, endDate, begTime, endTime });
	}

	/**
	 * @Description:
	 * @param type
	 *            :0表示小涨大跌；1表示大涨小跌
	 * @param predictedresult
	 * @param trendBefore
	 * @return int
	 * @date Sep 3, 2014
	 * @author:fgq
	 */
	private String getStatisticalResultBefore(int type, String predictedresult, String trendBefore) {
		String rs = "未知";
		if (type == 0) {
			if ("涨".equals(trendBefore)) {
				if ("预测值大于前值".equals(predictedresult)) {
					rs = "不符合";
				} else if ("预测值小于前值".equals(predictedresult)) {
					rs = "符合";
				} else if ("预测值等于前值".equals(predictedresult)) {
					rs = "等值无趋势";
				}
			} else if ("跌".equals(trendBefore)) {
				if ("预测值大于前值".equals(predictedresult)) {
					rs = "符合";
				} else if ("预测值小于前值".equals(predictedresult)) {
					rs = "不符合";
				} else if ("预测值等于前值".equals(predictedresult)) {
					rs = "等值无趋势";
				}
			}
		} else if (type == 1) {
			if ("涨".equals(trendBefore)) {
				if ("预测值大于前值".equals(predictedresult)) {
					rs = "符合";
				} else if ("预测值小于前值".equals(predictedresult)) {
					rs = "不符合";
				} else if ("预测值等于前值".equals(predictedresult)) {
					rs = "等值无趋势";
				}
			} else if ("跌".equals(trendBefore)) {
				if ("预测值大于前值".equals(predictedresult)) {
					rs = "不符合";
				} else if ("预测值小于前值".equals(predictedresult)) {
					rs = "符合";
				} else if ("预测值等于前值".equals(predictedresult)) {
					rs = "等值无趋势";
				}
			}
		}
		return rs;
	}

	/**
	 * @Description:
	 * @param symbol
	 * @param indicatoreffect
	 *            :参考Const.Indicatoreffect
	 * @param predictedresult
	 *            :参考Const.Predictedresult
	 * @param trendBefore
	 * @date Sep 2, 2014
	 * @author:fgq
	 */
	private String getStatisticalResultBefore(String symbol, String indicatoreffect, String predictedresult, String trendBefore) {
		String rs = "未知";
		if ("".equals(UtilString.isNil(indicatoreffect))) {
			return "指标影响为空";
		}
		if ("".equals(UtilString.isNil(predictedresult))) {
			return "预测结果为空";
		}

		if ("".equals(UtilString.isNil(trendBefore))) {
			return "趋势为空";
		}
		if ("XAUUSD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			}
		} else if ("EURUSD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值>预测值=利好欧元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好欧元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			}
		} else if ("GBPUSD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值>预测值=利好英镑".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好英镑".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			}
		} else if ("AUDUSD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值>预测值=利好澳元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好澳元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			}
		} else if ("USDJPY".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值>预测值=利好日元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好日元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			}
		} else if ("USDCAD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值>预测值=利好加元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好加元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			}
		} else if ("USDCHF".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值>预测值=利好瑞郎".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(0, predictedresult, trendBefore);
			} else if ("公布值<预测值=利好瑞郎".equals(indicatoreffect)) {
				return this.getStatisticalResultBefore(1, predictedresult, trendBefore);
			}
		}
		return rs;
	}

	/**
	 * @Description:
	 * @param type
	 *            :0表示小涨大跌；1表示大涨小跌
	 * @param compareResult
	 * @param trendAfter
	 * @return int
	 * @date Sep 3, 2014
	 * @author:fgq
	 */
	private String getStatisticalResultAfter(int type, String compareResult, String trendAfter) {
		String rs = "未知";
		if (type == 0) {
			if ("涨".equals(trendAfter)) {
				if ("大于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("等于前值".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("大于预测值;".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于预测值;".equals(compareResult)) {
					rs = "符合";
				} else if ("等于预测值;".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("大于预测值;大于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("大于预测值;等于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("大于预测值;小于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("等于预测值;大于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("等于预测值;等于前值".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("等于预测值;小于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("小于预测值;大于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("小于预测值;等于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("小于预测值;小于前值".equals(compareResult)) {
					rs = "符合";
				}
			} else if ("跌".equals(trendAfter)) {
				if ("大于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("小于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("等于前值".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("大于预测值;".equals(compareResult)) {
					rs = "符合";
				} else if ("小于预测值;".equals(compareResult)) {
					rs = "不符合";
				} else if ("等于预测值;".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("大于预测值;大于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("大于预测值;等于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("大于预测值;小于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("等于预测值;大于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("等于预测值;等于前值".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("等于预测值;小于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于预测值;大于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于预测值;等于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于预测值;小于前值".equals(compareResult)) {
					rs = "不符合";
				}
			}
		} else if (type == 1) {
			if ("跌".equals(trendAfter)) {
				if ("大于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("等于前值".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("大于预测值;".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于预测值;".equals(compareResult)) {
					rs = "符合";
				} else if ("等于预测值;".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("大于预测值;大于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("大于预测值;等于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("大于预测值;小于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("等于预测值;大于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("等于预测值;等于前值".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("等于预测值;小于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("小于预测值;大于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("小于预测值;等于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("小于预测值;小于前值".equals(compareResult)) {
					rs = "符合";
				}
			} else if ("涨".equals(trendAfter)) {
				if ("大于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("小于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("等于前值".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("大于预测值;".equals(compareResult)) {
					rs = "符合";
				} else if ("小于预测值;".equals(compareResult)) {
					rs = "不符合";
				} else if ("等于预测值;".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("大于预测值;大于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("大于预测值;等于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("大于预测值;小于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("等于预测值;大于前值".equals(compareResult)) {
					rs = "符合";
				} else if ("等于预测值;等于前值".equals(compareResult)) {
					rs = "等值无趋势";
				} else if ("等于预测值;小于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于预测值;大于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于预测值;等于前值".equals(compareResult)) {
					rs = "不符合";
				} else if ("小于预测值;小于前值".equals(compareResult)) {
					rs = "不符合";
				}
			}

		}
		return rs;
	}

	/**
	 * @Description:
	 * @param symbol
	 * @param indicatoreffect
	 *            :参考Const.Indicatoreffect
	 * @param compareResult
	 *            :参考Const.CompareResult
	 * @param trendAfter
	 * @return int ：0表示不符合，1表示符合，-1表示未知，2表示等值无趋势判断
	 * @date Sep 2, 2014
	 * @author:fgq
	 */
	private String getStatisticalResultAfter(String symbol, String indicatoreffect, String compareResult, String trendAfter) {
		String rs = "未知";
		if ("".equals(UtilString.isNil(indicatoreffect))) {
			return "指标影响为空";
		}
		if ("".equals(UtilString.isNil(compareResult))) {
			return "比较结果为空";
		}
		if ("".equals(UtilString.isNil(trendAfter))) {
			return "趋势为空";
		}
		if ("XAUUSD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			}
		} else if ("EURUSD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值>预测值=利好欧元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值<预测值=利好欧元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			}
		} else if ("GBPUSD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值>预测值=利好英镑".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值<预测值=利好英镑".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			}
		} else if ("AUDUSD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值>预测值=利好澳元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值<预测值=利好澳元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			}
		} else if ("USDJPY".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值>预测值=利好日元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值<预测值=利好日元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			}
		} else if ("USDCAD".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值>预测值=利好加元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值<预测值=利好加元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			}
		} else if ("USDCHF".equalsIgnoreCase(symbol)) {
			if ("公布值>预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			} else if ("公布值<预测值=利好美元".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值>预测值=利好瑞郎".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(0, compareResult, trendAfter);
			} else if ("公布值<预测值=利好瑞郎".equals(indicatoreffect)) {
				return this.getStatisticalResultAfter(1, compareResult, trendAfter);
			}
		}
		return rs;
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
	}

}
