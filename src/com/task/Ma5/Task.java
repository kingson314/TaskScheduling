package com.task.Ma5;

import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;
import net.sf.json.JSONObject;

import com.log.Log;
import com.taskInterface.TaskAbstract;

import common.util.conver.UtilConver;
import common.util.date.UtilDate;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.string.UtilString;
import consts.Const;

//FeatureStatistics
public class Task extends TaskAbstract {
	private Connection con;
	private Bean bean;
	private String sql5 = "select dateServer,timeServer,dateLocal,timeLocal,open,close,high,low,ma5,ma20,ma60,kdj,id " + "from gbpjpy5_detail  where dateServer>=? and dateServer<= ?"
			+ " and dateServer||' '||timeServer > ? and  dateServer||' '||timeServer <=? order by dateServer ,timeServer ";
	private String begDate;
	private String endDate;

	/**
	 * 此任务类型目前只能手工执行
	 */
	public void manuExecTask(String[] params) {
		this.begDate = params[0].replaceAll("-", ".");
		this.endDate = params[1].replaceAll("-", ".");
		this.fireTask();
	}

	public void fireTask() {
		try {
			bean = (Bean) JSONObject.toBean(JSONObject.fromObject(this.getJsonStr()), Bean.class);
			DbConnection dbConn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbConn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbConn);
			init();
			String sql60 = "select dateServer,timeServer,dateLocal,timeLocal,open,close,high,low,ma5,ma20,ma60,kdj,id,type from gbpjpy60  where  dateserver>=? and dateserver<=? order by dateServer ,timeServer ";
			ArrayList<HashMap<String, String>> list60 = UtilSql.executeSql(this.con, sql60, new Object[] { this.begDate, this.endDate });
			String runDate = "";
			for (int i = 0; i < list60.size(); i++) { // 使用前一根K线，供判断是否破坏趋势
				String upType = upSymbolType(list60, i);
				upFeaturekline(list60, i, upType);
				String downType = downSymbolType(list60, i);
				downFeaturekline(list60, i, downType);
				if (!runDate.equals(list60.get(i).get("DATESERVER"))) {
					runDate = list60.get(i).get("DATESERVER");
					Log.logInfo(this.getTaskName() + "[" + this.getTaskId() + "] 当前执行日期:" + runDate);
				}
			}
			this.setTaskStatus("执行成功");
			this.setTaskMsg("形态分析完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() throws Exception {
		String sql = "delete  featurestatistics  where id60 in(select id from gbpjpy60 where dateserver>=? and dateserver<=?) ";
		UtilSql.executeUpdate(this.con, sql, new Object[] { this.begDate, this.endDate });
		sql = " delete  featurekline  where id60 in(select id from gbpjpy60 where dateserver>=? and dateserver<=?)";
		UtilSql.executeUpdate(this.con, sql, new Object[] { this.begDate, this.endDate });
		sql = "update gbpjpy60 set type=''  where dateserver>=? and dateserver<=? ";
		UtilSql.executeUpdate(this.con, sql, new Object[] { this.begDate, this.endDate });
	}

	private List<HashMap<String, String>> getList5(String dateTime60) throws Exception {
		String dateTimeDuration = UtilDate.addMinute(dateTime60, bean.getDuration(), Const.fm_yyyyMMdd_HHmmss);
		// 开始日期,结束日期 必须用此二日期用于索引
		String begdate = UtilConver.dateToStr(UtilConver.strToDate(dateTime60, Const.fm_yyyyMMdd_HHmmss), Const.fm_yyyyMMdd);
		String endDate = UtilConver.dateToStr(UtilConver.strToDate(dateTimeDuration, Const.fm_yyyyMMdd_HHmmss), Const.fm_yyyyMMdd);
		List<HashMap<String, String>> list5 = UtilSql.executeSql(con, sql5, new Object[] { begdate, endDate, dateTime60, dateTimeDuration });
		return list5;
	}

	private long getTimeDiff(String beginTime, HashMap<String, String> map5) throws ParseException {
		if ("".equals(beginTime))
			return 0;
		return UtilDate.diffMinute(beginTime, map5.get("DATESERVER") + " " + map5.get("TIMESERVER"), Const.fm_yyyyMMdd_HHmmss);
	}

	// 分析上涨形态时，统计达到最大最小值以及形态破坏的5分钟K线
	private void upFeaturekline(List<HashMap<String, String>> list60, int index60, String type) throws Exception {
		if (type.equals(""))
			return;
		String dateTime60 = list60.get(index60 - 1).get("DATESERVER") + " " + list60.get(index60 - 1).get("TIMESERVER");
		// System.out.println(list60.get(index60).get("ID")+" "+dateTime60);
		List<HashMap<String, String>> list5 = getList5(dateTime60);
		if (list5.size() <= 0)
			return;
		// 以下数值均为形态未破坏前的数值
		Map<String, String> mapFeaturekline = new HashMap<String, String>();
		mapFeaturekline.put("TYPE", type);
		String id60 = list60.get(index60).get("ID");
		mapFeaturekline.put("ID60", id60);

		double max5 = 0;
		// 小于前一根最高价时方更新最低价，否则取前一根最高价
		double beginValue = Double.valueOf(list60.get(index60 - 1).get("HIGH"));// 行情突破开始值
		double min5 = beginValue;
		int indexMax = -1;
		int indexMin = -1;
		int indexBad = -1;
		String beginTime = "";// 行情突破时间
		int beginIndex = 0;
		for (int i = 0; i < list5.size(); i++) {
			if (type.equals("5")) {
				if (Double.valueOf(list5.get(i).get("CLOSE")) > beginValue) {
					beginTime = list5.get(i).get("DATESERVER") + " " + list5.get(i).get("TIMESERVER");
					beginIndex = i;
					break;
				}
			}
		}

		for (int i = beginIndex + 1; i < list5.size(); i++) {
			if (type.equals("5")) {
				// System.out.println((list5.get(i).get("TIMESERVER"))+"
				// "+list5.get(i).get("LOW")+" "+list60.get(index60 -
				// 1).get("OPEN"));
				// 判断形态是否被破坏(涨势中，最低价小于60 线的开盘价【最低价】)
				if (Double.valueOf(list5.get(i).get("CLOSE")) < Double.valueOf(list60.get(index60 - 1).get("LOW"))) {
					indexBad = i;
					break;
				}
			}
			// 趋势方向最大值
			if (Double.valueOf(list5.get(i).get("CLOSE")) > max5) {
				max5 = Double.valueOf(list5.get(i).get("CLOSE"));
				indexMax = i;
			}
			// 趋势相反方向最大值
			if (Double.valueOf(list5.get(i).get("CLOSE")) < min5) {
				min5 = Double.valueOf(list5.get(i).get("CLOSE"));
				indexMin = i;
			}
		}
		if (indexMax >= 0)
			mapFeaturekline.put("MAXID", list5.get(indexMax).get("ID"));
		if (indexMin >= 0)
			mapFeaturekline.put("MINID", list5.get(indexMin).get("ID"));
		if (indexBad >= 0)
			mapFeaturekline.put("BADID", list5.get(indexBad).get("ID"));
		updateFeatureKLine(mapFeaturekline);

		double valueMax = 0;
		double valueMin = 0;
		double valueMaxMin = 0;
		double valueMaxSuper = 0;
		long timeMax = 0;
		long timeMin = 0;
		long timeBad = 0;
		if (indexMax >= 0) {
			valueMax = max5 - beginValue;
			valueMaxSuper = max5 - Double.valueOf(UtilString.isNil(list60.get(index60 - 1).get("LOW")));
			timeMax = getTimeDiff(beginTime, list5.get(indexMax));
		}

		if (indexMin >= 0) {
			valueMin = min5 - beginValue;
			timeMin = getTimeDiff(beginTime, list5.get(indexMin));
		}
		valueMaxMin = valueMax - valueMin;
		if (indexBad >= 0) {
			timeBad = getTimeDiff(beginTime, list5.get(indexBad));
		}

		Map<String, String> mapFeaturestatistics = new HashMap<String, String>();
		mapFeaturestatistics.put("ID60", id60);
		mapFeaturestatistics.put("VALUEMAX", String.valueOf(valueMax));
		mapFeaturestatistics.put("VALUEMIN", String.valueOf(valueMin));
		mapFeaturestatistics.put("VALUEMAXMIN", String.valueOf(valueMaxMin));
		mapFeaturestatistics.put("VALUEMAXSUPER", String.valueOf(valueMaxSuper));
		mapFeaturestatistics.put("TIMEMAX", String.valueOf(timeMax));
		mapFeaturestatistics.put("TIMEMIN", String.valueOf(timeMin));
		mapFeaturestatistics.put("TIMEBAD", String.valueOf(timeBad));
		updateFeatureStatistics(mapFeaturestatistics);
	}

	// 分析下跌形态时，统计达到最大最小值以及形态破坏的5分钟K线
	private void downFeaturekline(List<HashMap<String, String>> list60, int index60, String type) throws Exception {
		if (type.equals(""))
			return;
		String dateTime60 = list60.get(index60 - 1).get("DATESERVER") + " " + list60.get(index60 - 1).get("TIMESERVER");
		List<HashMap<String, String>> list5 = getList5(dateTime60);
		if (list5.size() <= 0)
			return;
		// 以下数值均为形态未破坏前的数值
		Map<String, String> mapFeatureKLine = new HashMap<String, String>();
		mapFeatureKLine.put("TYPE", type);
		String id60 = list60.get(index60).get("ID");
		mapFeatureKLine.put("ID60", id60);
		double beginValue = Double.valueOf(list60.get(index60 - 1).get("LOW"));
		double max5 = beginValue;
		double min5 = beginValue;
		int indexMax = -1;
		int indexMin = -1;
		int indexBad = -1;
		String beginTime = "";// 行情突破时间
		int beginIndex = 0;
		for (int i = 0; i < list5.size(); i++) {
			if (type.equals("6")) {
				if (Double.valueOf(list5.get(i).get("CLOSE")) < beginValue) {
					beginTime = list5.get(i).get("DATESERVER") + " " + list5.get(i).get("TIMESERVER");
					beginIndex = i;
					break;
				}
			}
		}

		for (int i = beginIndex + 1; i < list5.size(); i++) {
			if (type.equals("6")) {
				// 判断形态是否被破坏(涨势中，最低价小于60 线的开盘价【最低价】)
				if (Double.valueOf(list5.get(i).get("CLOSE")) > Double.valueOf(list60.get(index60 - 1).get("HIGH"))) {
					indexBad = i;
					break;
				}
			}
			// 趋势方向最大值
			if (Double.valueOf(list5.get(i).get("CLOSE")) < max5) {
				max5 = Double.valueOf(list5.get(i).get("CLOSE"));
				indexMax = i;
			}
			// 趋势相反方向最大值
			if (Double.valueOf(list5.get(i).get("CLOSE")) > min5) {
				min5 = Double.valueOf(list5.get(i).get("CLOSE"));
				indexMin = i;
			}
		}
		if (indexMax >= 0)
			mapFeatureKLine.put("MAXID", list5.get(indexMax).get("ID"));
		if (indexMin >= 0)
			mapFeatureKLine.put("MINID", list5.get(indexMin).get("ID"));
		if (indexBad >= 0)
			mapFeatureKLine.put("BADID", list5.get(indexBad).get("ID"));
		updateFeatureKLine(mapFeatureKLine);

		double valueMax = 0;
		double valueMin = 0;
		double valueMaxMin = 0;
		double valueMaxSuper = 0;
		long timeMax = 0;
		long timeMin = 0;
		long timeBad = 0;
		if (indexMax >= 0) {
			valueMax = beginValue - max5;
			valueMaxSuper = Double.valueOf(UtilString.isNil(list60.get(index60 - 1).get("HIGH"))) - max5;
			timeMax = getTimeDiff(beginTime, list5.get(indexMax));
		}
		if (indexMin >= 0) {
			valueMin = beginValue - min5;
			timeMin = getTimeDiff(beginTime, list5.get(indexMin));
		}
		valueMaxMin = valueMax - valueMin;
		if (indexBad >= 0) {
			timeBad = getTimeDiff(beginTime, list5.get(indexBad));
		}

		Map<String, String> mapFeaturestatistics = new HashMap<String, String>();
		mapFeaturestatistics.put("ID60", id60);
		mapFeaturestatistics.put("VALUEMAX", String.valueOf(valueMax));
		mapFeaturestatistics.put("VALUEMIN", String.valueOf(valueMin));
		mapFeaturestatistics.put("VALUEMAXMIN", String.valueOf(valueMaxMin));
		mapFeaturestatistics.put("VALUEMAXSUPER", String.valueOf(valueMaxSuper));
		mapFeaturestatistics.put("TIMEMAX", String.valueOf(timeMax));
		mapFeaturestatistics.put("TIMEMIN", String.valueOf(timeMin));
		mapFeaturestatistics.put("TIMEBAD", String.valueOf(timeBad));
		updateFeatureStatistics(mapFeaturestatistics);
	}

	private String downSymbolType(List<HashMap<String, String>> list60, int index) {
		String type = "";
		// 破位式
		if (index < 1)
			return "";
		// 前一根是阳线，本根K线最低价比前一阴线最低价低，记录本根K线
		if (Double.valueOf(list60.get(index - 1).get("CLOSE")) > Double.valueOf(list60.get(index - 1).get("OPEN"))) {// 阳线
			if (Double.valueOf(list60.get(index).get("LOW")) < Double.valueOf(list60.get(index - 1).get("LOW"))) {
				type = "6";
				updateSymbolType(type, Integer.valueOf(list60.get(index).get("ID")));
			}
		}
		return type;
	}

	private String upSymbolType(List<HashMap<String, String>> list60, int index) {
		String type = "";
		// 破位式
		if (index < 1)
			return "";
		// 前一根是阴线，本根K线最高价比前一阴线最高价高，记录本根K线
		if (Double.valueOf(list60.get(index - 1).get("CLOSE")) < Double.valueOf(list60.get(index - 1).get("OPEN"))) {// 阴线
			if (Double.valueOf(list60.get(index).get("HIGH")) > Double.valueOf(list60.get(index - 1).get("HIGH"))) {
				type = "5";
				updateSymbolType(type, Integer.valueOf(list60.get(index).get("ID")));
			}
		}
		return type;
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		fireTask();
	}

	/**
	 * @fun :更新图形表中的形态类型
	 * @param type
	 *            ：形态类型
	 * @param id
	 *            ：k线的id号
	 */
	private void updateSymbolType(String type, int id) {
		Object[] param = new Object[2];
		param[0] = type;
		param[1] = id;
		String sql = "update gbpjpy60 set type=? where id=? ";
		try {
			UtilSql.executeUpdate(con, sql, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateFeatureKLine(Map<String, String> mapParam) {
		Object[] param = new Object[6];
		param[0] = "gbpjpy";
		param[1] = mapParam.get("ID60");
		param[2] = mapParam.get("MAXID");
		param[3] = mapParam.get("MINID");
		param[4] = mapParam.get("BADID");
		param[5] = mapParam.get("TYPE");
		String sql = "insert into featurekline (symbol, id60, id5max,id5min, id5bad,type)values(?,?,?,?,?,?)";
		try {
			UtilSql.executeUpdate(con, sql, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateFeatureStatistics(Map<String, String> mapParam) {
		Object[] param = new Object[9];
		param[0] = "GBPJPY";
		param[1] = mapParam.get("ID60");
		param[2] = mapParam.get("VALUEMAX");
		param[3] = mapParam.get("VALUEMIN");
		param[4] = mapParam.get("VALUEMAXMIN");
		param[5] = mapParam.get("VALUEMAXSUPER");
		param[6] = mapParam.get("TIMEMAX");
		param[7] = mapParam.get("TIMEMIN");
		param[8] = mapParam.get("TIMEBAD");
		String sql = "insert into featurestatistics(SYMBOL,ID60,VALUEMAX,VALUEMIN,VALUEMAXMIN,VALUEMAXSUPER,TIMEMAX,TIMEMIN,TIMEBAD) values(?,?,?,?,?,?,?,?,?)";
		try {
			UtilSql.executeUpdate(con, sql, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Task fs = new Task();
		fs.fireTask();
		// String date1 = "2012.12.05 15:30:31";
		// String date2 = "2012.12.05 15:35:32";
		// try {
		// System.out.println(Fun.diffMinute(date1, date2,
		// ConstApp.fmDateTime3));
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		System.out.println("Done");
	}
}
