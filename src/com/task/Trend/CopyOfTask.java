package com.task.Trend;
//package com.ts.task.Trend;
//
//import java.sql.Connection;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import net.sf.json.JSONObject;
//import app.common.consts.ConstApp;
//import app.common.dbconnection.DbConnection;
//import app.common.dbconnection.DbConnectionDao;
//
//import com.ts.taskInterface.TaskAbstract;
//import common.util.date.UtilDate;
//import common.util.jdbc.UtilJDBCManager;
//import common.util.jdbc.UtilSql;
//import common.util.string.UtilString;
//
////FeatureStatistics
//public class CopyOfTask extends TaskAbstract {
//	private Connection con;
//	private Bean bean;
//	private String sql5 = "select dateServer,timeServer,dateLocal,timeLocal,open,close,high,low,ma5,ma20,ma60,kdj,id,type "
//			+ "from gbpjpy5  where dateServer||' '||timeServer between ? and ? order by dateServer ,timeServer ";
//
//	public CopyOfTask() {
//		this.setTaskStatus("等待执行");
//		this.setTaskFailMsg("");
//	}
//
//	public void fireTask() {
//		try {
//			bean = (Bean) JSONObject.toBean(JSONObject.fromObject(this.getJsonStr()), Bean.class);
//			DbConnection dbConn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
//			if (dbConn == null) {
//				this.setTaskStatus("执行失败");
//				this.setTaskFailMsg("获取数据库连接错误");
//				return;
//			}
//			con = UtilJDBCManager.getConnection(dbConn);
//
//			String sql60 = "select dateServer,timeServer,dateLocal,timeLocal,open,close,high,low,ma5,ma20,ma60,kdj,id,type from gbpjpy60 order by dateServer ,timeServer ";
//			ArrayList<HashMap<String, String>> list60 =
//
//			UtilSql.executeSql(con, sql60, new Object[0]);
//			for (int i = 0; i < list60.size(); i++) {
//
//				// 使用前一根K线，供判断是否破坏趋势
//				String upType = upSymbolType(list60,
//
//				i);
//				upFeaturekline(list60, i, upType);
//				String downType = downSymbolType
//
//				(list60, i);
//				downFeaturekline(list60, i, downType);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private List<HashMap<String, String>> getList5(String
//
//	dateTime60) throws Exception {
//		String dateTimeDuration = UtilDate.addMinute(dateTime60,
//
//		bean.getDuration(), ConstApp.fmDateTime3);
//		List<HashMap<String, String>> list5 = UtilSql.executeSql(con,
//
//		sql5, new Object[] { dateTime60, dateTimeDuration });
//		return list5;
//	}
//
//	// 分析上涨形态时，统计达到最大最小值以及形态破坏的5分钟K线
//	private void upFeaturekline(List<HashMap<String, String>> list60, int
//
//	index60, String type) throws Exception {
//		if (type.equals(""))
//			return;
//		String dateTime60 = list60.get(index60 - 1).get
//
//		("DATESERVER") + " " + list60.get(index60 - 1).get("TIMESERVER");
//		List<HashMap<String, String>> list5 = getList5
//
//		(dateTime60);
//		// 以下数值均为形态未破坏前的数值
//		Map<String, String> mapFeaturekline = new
//
//		HashMap<String, String>();
//		mapFeaturekline.put("TYPE", type);
//		String id60 = list60.get(index60).get("ID");
//		mapFeaturekline.put("ID60", id60);
//
//		double highest5 = 0;
//		double lowest5 = 20120814;
//		int indexMax = 0;
//		int indexMin = 0;
//		int indexBad = 0;
//		for (int i = 0; i < list5.size(); i++) {
//			String dateTime5 = list5.get(i).get
//
//			("DATESERVER") + list5.get(i).get("TIMESERVER");
//			if (dateTime5.compareTo(dateTime60) <= 0)
//				continue;
//			// 趋势方向最大值
//			if (Double.valueOf(list5.get(i).get("HIGH")) >
//
//			highest5) {
//				highest5 = Double.valueOf(list5.get
//
//				(i).get("HIGH"));
//				indexMax = i;
//			}
//			// 趋势相反方向最大值
//			if (Double.valueOf(list5.get(i).get("LOW")) <
//
//			lowest5) {
//				lowest5 = Double.valueOf(list5.get
//
//				(i).get("LOW"));
//				indexMin = i;
//			}
//			if (type.equals("a")) {
//				// 判断形态是否被破坏(涨势中，最低价小于60 线的开盘价【最低价】)
//				if (Double.valueOf(list5.get(i).get
//
//				("LOW")) < Double.valueOf(list60.get(index60 - 1).get("OPEN"))) {
//					indexBad = i;
//					break;
//				}
//			} else if (type.equals("b")) {
//				// 判断形态是否被破坏(涨势中，最低价小于60线的开盘价【最低价】)
//				if (Double.valueOf(list5.get(i).get
//
//				("LOW")) < Double.valueOf(list60.get(index60 - 2).get("OPEN"))) {
//					indexBad = i;
//					break;
//				}
//			}
//		}
//		mapFeaturekline.put("MAXID", list5.get(indexMax).get
//
//		("ID"));
//		mapFeaturekline.put("MINID", list5.get(indexMin).get
//
//		("ID"));
//		mapFeaturekline.put("BADID", list5.get(indexBad).get
//
//		("ID"));
//		updateFeatureKLine(mapFeaturekline);
//
//		double valueMax = 0;
//		double valueMin = 0;
//		double valueMaxMin = 0;
//		double valueMaxSuper = 0;
//		long timeMax = 0;
//		long timeMin = 0;
//		long timeBad = 0;
//		if (indexMax > 0) {
//			valueMax = Double.valueOf(UtilString.isNil(list5.get
//
//			(indexMax).get("HIGH"))) - Double.valueOf(UtilString.isNil(list60.get
//
//			(index60).get("OPEN")));
//			valueMaxSuper = Double.valueOf(UtilString.isNil
//
//			(list5.get(indexMax).get("HIGH"))) - Double.valueOf(UtilString.isNil
//
//			(list60.get(index60).get("LOW")));
//			timeMax = UtilDate.diffMinute(list60.get
//
//			(index60).get("DATESERVER") + " " + list60.get(index60).get
//
//			("TIMESERVER"), list5.get(indexMax).get
//
//			("DATESERVER") + " " + list5.get(indexMax).get
//
//			("TIMESERVER"), ConstApp.fmDateTime3);
//		}
//
//		if (indexMin > 0) {
//			valueMin = Double.valueOf(UtilString.isNil(list60.get
//
//			(index60).get("OPEN"))) - Double.valueOf(UtilString.isNil(list5.get
//
//			(indexMin).get("LOW")));
//			timeMin = UtilDate.diffMinute(list60.get
//
//			(index60).get("DATESERVER") + " " + list60.get(index60).get
//
//			("TIMESERVER"), list5.get(indexMin).get
//
//			("DATESERVER") + " " + list5.get(indexMin).get("TIMESERVER"),
//
//			ConstApp.fmDateTime3);
//		}
//		valueMaxMin = valueMax - valueMin;
//		if (indexBad > 0) {
//			timeBad = UtilDate.diffMinute(list60.get
//
//			(index60).get("DATESERVER") + " " + list60.get(index60).get
//
//			("TIMESERVER"), list5.get(indexBad).get
//
//			("DATESERVER") + " " + list5.get(indexBad).get
//
//			("TIMESERVER"), ConstApp.fmDateTime3);
//		}
//
//		Map<String, String> mapFeaturestatistics = new
//
//		HashMap<String, String>();
//		mapFeaturestatistics.put("ID60", id60);
//		mapFeaturestatistics.put("VALUEMAX", String.valueOf
//
//		(Math.abs(valueMax)));
//		mapFeaturestatistics.put("VALUEMIN", String.valueOf
//
//		(Math.abs(valueMin)));
//		mapFeaturestatistics.put("VALUEMAXMIN", String.valueOf
//
//		(Math.abs(valueMaxMin)));
//		mapFeaturestatistics.put("VALUEMAXSUPER",
//
//		String.valueOf(Math.abs(valueMaxSuper)));
//		mapFeaturestatistics.put("TIMEMAX", String.valueOf
//
//		(Math.abs(timeMax)));
//		mapFeaturestatistics.put("TIMEMIN", String.valueOf
//
//		(Math.abs(timeMin)));
//		mapFeaturestatistics.put("TIMEBAD", String.valueOf
//
//		(Math.abs(timeBad)));
//		updateFeatureStatistics(mapFeaturestatistics);
//	}
//
//	// 分析下跌形态时，统计达到最大最小值以及形态破坏的5分钟K线
//	private void downFeaturekline(List<HashMap<String, String>>
//
//	list60, int index60, String type) throws Exception {
//		if (type.equals(""))
//			return;
//		String dateTime60 = list60.get(index60 - 1).get
//
//		("DATESERVER") + " " + list60.get(index60 - 1).get("TIMESERVER");
//		List<HashMap<String, String>> list5 = getList5
//
//		(dateTime60);
//		// 以下数值均为形态未破坏前的数值
//		Map<String, String> mapFeatureKLine = new
//
//		HashMap<String, String>();
//		mapFeatureKLine.put("TYPE", type);
//		String id60 = list60.get(index60).get("ID");
//		mapFeatureKLine.put("ID60", id60);
//
//		double highest5 = 0;
//		double lowest5 = 20120814;
//		int indexMax = 0;
//		int indexMin = 0;
//		int indexBad = 0;
//		for (int i = 0; i < list5.size(); i++) {
//			String dateTime5 = list5.get(i).get
//
//			("DATESERVER") + list5.get(i).get("TIMESERVER");
//			if (dateTime5.compareTo(dateTime60) <= 0)
//
//				continue;
//			// 趋势相反方向最大值
//			if (Double.valueOf(list5.get(i).get("HIGH")) >
//
//			highest5) {
//				highest5 = Double.valueOf(list5.get
//
//				(i).get("HIGH"));
//				indexMin = i;
//			}
//			// 趋势方向最大值
//			if (Double.valueOf(list5.get(i).get("LOW")) <
//
//			lowest5) {
//				lowest5 = Double.valueOf(list5.get
//
//				(i).get("LOW"));
//				indexMax = i;
//			}
//			if (type.equals("A")) {
//				// 判断形态是否被破坏(涨势中，最低价小于60 线的开盘价【最低价】)
//				if (Double.valueOf(list5.get(i).get("LOW")) > Double.valueOf(list60.get(index60 - 1).get("OPEN"))) {
//					indexBad = i;
//					break;
//				}
//			} else if (type.equals("B")) {
//				// 判断形态是否被破坏(涨势中，最低价小于60线的开盘价【最低价】)
//				if (Double.valueOf(list5.get(i).get("LOW")) > Double.valueOf(list60.get(index60 - 2).get("OPEN"))) {
//					indexBad = i;
//					break;
//				}
//			}
//		}
//		mapFeatureKLine.put("MINID", list5.get(indexMin).get
//
//		("ID"));
//		mapFeatureKLine.put("MAXID", list5.get(indexMax).get
//
//		("ID"));
//		mapFeatureKLine.put("BADID", list5.get(indexBad).get
//
//		("ID"));
//		updateFeatureKLine(mapFeatureKLine);
//
//		double valueMax = 0;
//		double valueMin = 0;
//		double valueMaxMin = 0;
//		double valueMaxSuper = 0;
//		long timeMax = 0;
//		long timeMin = 0;
//		long timeBad = 0;
//		if (indexMax > 0) {
//			valueMax = -Double.valueOf(UtilString.isNil
//
//			(list60.get(index60).get("OPEN"))) + Double.valueOf(UtilString.isNil(list5.get
//
//			(indexMax).get("LOW")));
//			valueMaxSuper = Double.valueOf(UtilString.isNil
//
//			(list60.get(index60).get("HIGH"))) - Double.valueOf(UtilString.isNil(list5.get
//
//			(indexMax).get("LOW")));
//			timeMax = UtilDate.diffMinute(list60.get
//
//			(index60).get("DATESERVER") + " " + list60.get(index60).get
//
//			("TIMESERVER"), list5.get(indexMax).get
//
//			("DATESERVER") + " " + list5.get(indexMax).get
//
//			("TIMESERVER"), ConstApp.fmDateTime3);
//		}
//		if (indexMin > 0) {
//			valueMin = +Double.valueOf(UtilString.isNil
//
//			(list60.get(index60).get("OPEN"))) - Double.valueOf(UtilString.isNil(list5.get
//
//			(indexMin).get("HIGH")));
//			timeMin = UtilDate.diffMinute(list60.get
//
//			(index60).get("DATESERVER") + " " + list60.get(index60).get
//
//			("TIMESERVER"), list5.get(indexMin).get
//
//			("DATESERVER") + " " + list5.get(indexMin).get("TIMESERVER"),
//
//			ConstApp.fmDateTime3);
//		}
//		valueMaxMin = valueMax - valueMin;
//		if (indexBad > 0) {
//			timeBad = UtilDate.diffMinute(list60.get
//
//			(index60).get("DATESERVER") + " " + list60.get(index60).get
//
//			("TIMESERVER"), list5.get(indexBad).get
//
//			("DATESERVER") + " " + list5.get(indexBad).get("TIMESERVER"),
//
//			ConstApp.fmDateTime3);
//		}
//
//		Map<String, String> mapFeaturestatistics = new
//
//		HashMap<String, String>();
//		mapFeaturestatistics.put("ID60", id60);
//		mapFeaturestatistics.put("VALUEMAX", String.valueOf
//
//		(Math.abs(valueMax)));
//		mapFeaturestatistics.put("VALUEMIN", String.valueOf
//
//		(Math.abs(valueMin)));
//		mapFeaturestatistics.put("VALUEMAXMIN", String.valueOf
//
//		(Math.abs(valueMaxMin)));
//		mapFeaturestatistics.put("VALUEMAXSUPER",
//
//		String.valueOf(Math.abs(valueMaxSuper)));
//		mapFeaturestatistics.put("TIMEMAX", String.valueOf
//
//		(Math.abs(timeMax)));
//		mapFeaturestatistics.put("TIMEMIN", String.valueOf
//
//		(Math.abs(timeMin)));
//		mapFeaturestatistics.put("TIMEBAD", String.valueOf
//
//		(Math.abs(timeBad)));
//		updateFeatureStatistics(mapFeaturestatistics);
//	}
//
//	private String downSymbolType(List<HashMap<String, String>>
//
//	list60, int index) {
//		String type = "";
//		// 破位式
//		if (index < 1)
//			return "";
//		// 前一根是阳线，本根K线最低价比前一阴线最低价低，记录本根K线
//		if (Double.valueOf(list60.get(index - 1).get("CLOSE")) >
//
//		Double.valueOf(list60.get(index - 1).get("OPEN"))) {// 阳线
//			if (Double.valueOf(list60.get(index).get
//
//			("LOW")) > Double.valueOf(list60.get(index - 1).get("LOW"))) {
//				type = "C";
//				updateSymbolType(type, Integer.valueOf
//
//				(list60.get(index).get("ID")));
//			}
//		}
//		// 一降式
//		if (index < 2)
//			return "";
//		// 前2根是阳线，前一根是阴线，前一根的收盘价比前二根开盘价低，记录本根K线
//		if (Double.valueOf(list60.get(index - 2).get("CLOSE"))
//
//		> Double.valueOf(list60.get(index - 2).get("OPEN"))) {// 阳线
//			if (Double.valueOf(list60.get(index - 1).get
//
//			("CLOSE")) < Double.valueOf(list60.get(index - 1).get("OPEN"))) {// 阴线
//				// 前一根的收盘价比前二根开盘价高
//				if (Double.valueOf(list60.get(index -
//
//				1).get("CLOSE")) < Double.valueOf(list60.get(index - 2).get("OPEN"))) {
//					type = "A";
//					updateSymbolType(type,
//
//					Integer.valueOf(list60.get(index).get("ID")));
//				}
//			}
//		}
//		// 二降式
//		if (index < 3)
//			return "";
//		// 前3根是阳线，前二根是阴线，前二根线收盘价比前3根开盘价高，前一根的收盘价比前二根收盘价低，前一根收盘价比前3根开盘价低，记录本根K线
//		if (Double.valueOf(list60.get(index - 3).get("CLOSE"))
//
//		> Double.valueOf(list60.get(index - 3).get("OPEN"))) {
//			if (Double.valueOf(list60.get(index - 2).get
//
//			("CLOSE")) < Double.valueOf(list60.get(index - 2).get("OPEN"))) {// 阴线
//				if (Double.valueOf(list60.get(index -
//
//				2).get("CLOSE")) > Double.valueOf(list60.get(index - 3).get("OPEN"))) {
//					// 前一根的收盘价比前二根开盘价低
//					if (Double.valueOf(list60.get
//
//					(index - 1).get("CLOSE")) < Double.valueOf(list60.get(index - 2).get
//
//					("OPEN"))) {
//						type = "B";
//						updateSymbolType(type,
//
//						Integer.valueOf(list60.get(index).get("ID")));
//					}
//				}
//			}
//		}
//		return type;
//	}
//
//	private String upSymbolType(List<HashMap<String, String>>
//
//	list60, int index) {
//		String type = "";
//
//		// 破位式
//		if (index < 1)
//			return "";
//		// 前一根是阴线，本根K线最高价比前一阴线最高价高，记录本根K线
//		if (Double.valueOf(list60.get(index - 1).get("CLOSE"))
//
//		< Double.valueOf(list60.get(index - 1).get("OPEN"))) {// 阴线
//			if (Double.valueOf(list60.get(index).get
//
//			("HIGH")) > Double.valueOf(list60.get(index - 1).get("HIGH"))) {
//				type = "c";
//				updateSymbolType(type, Integer.valueOf
//
//				(list60.get(index).get("ID")));
//			}
//		}
//
//		// 一升式
//		if (index < 2)
//			return "";
//		// 前2根是阴线，前一根是阳线，前一根的收盘价比前二根开盘价高，记录本根K线
//		if (Double.valueOf(list60.get(index - 2).get("CLOSE"))
//
//		< Double.valueOf(list60.get(index - 2).get("OPEN"))) {
//			// 阳线
//			if (Double.valueOf(list60.get(index - 1).get
//
//			("CLOSE")) > Double.valueOf(list60.get(index - 1).get("OPEN"))) {
//				// 前一根的收盘价比前二根开盘价高
//				if (Double.valueOf(list60.get(index -
//
//				1).get("CLOSE")) > Double.valueOf(list60.get(index - 2).get("OPEN"))) {
//					type = "a";
//					updateSymbolType(type,
//
//					Integer.valueOf(list60.get(index).get("ID")));
//				}
//			}
//		}
//
//		// 二升式
//		if (index < 3)
//			return "";
//		// 前3根是阴线，前二根是阳线，前二根线收盘价比前3根开盘价低，前一根的收盘价比前二根收盘价高，前一根收盘价比前3根开盘价高，记录本根K线
//		if (Double.valueOf(list60.get(index - 3).get("CLOSE"))
//
//		< Double.valueOf(list60.get(index - 3).get("OPEN"))) {
//			// 阳线
//			if (Double.valueOf(list60.get(index - 2).get
//
//			("CLOSE")) > Double.valueOf(list60.get(index - 2).get("OPEN"))) {
//				if (Double.valueOf(list60.get(index -
//
//				2).get("CLOSE")) < Double.valueOf(list60.get(index - 3).get("OPEN"))) {
//					// 前一根的收盘价比前二根开盘价高
//					if (Double.valueOf(list60.get
//
//					(index - 1).get("CLOSE")) > Double.valueOf(list60.get(index - 2).get
//
//					("OPEN"))) {
//						type = "b";
//						updateSymbolType(type,
//
//						Integer.valueOf(list60.get(index).get("ID")));
//					}
//				}
//			}
//		}
//		return type;
//	}
//
//	public void fireTask(String startTime, String groupId, String
//
//	scheCod, String taskOrder) {
//		fireTask();
//
//	}
//
//	/**
//	 * @fun :更新图形表中的形态类型
//	 * @param type
//	 *            ：形态类型
//	 * @param id
//	 *            ：k线的id号
//	 */
//	private void updateSymbolType(String type, int id) {
//		Object[] param = new Object[2];
//		param[0] = type;
//		param[1] = id;
//		String sql = "update gbpjpy60 set type=? where id=? ";
//		try {
//			UtilSql.executeUpdate(con, sql, param);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void updateFeatureKLine(Map<String, String> mapParam) {
//		Object[] param = new Object[6];
//		param[0] = "gbpjpy";
//		param[1] = mapParam.get("ID60");
//		param[2] = mapParam.get("MAXID");
//		param[3] = mapParam.get("MINID");
//		param[4] = mapParam.get("BADID");
//		param[5] = mapParam.get("TYPE");
//		String sql = "insert into featurekline (symbol, id60, id5max,id5min, id5bad,type) values" + "(?,?,?,?,?,?)";
//		try {
//			UtilSql.executeUpdate(con, sql, param);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void updateFeatureStatistics(Map<String, String>
//
//	mapParam) {
//		Object[] param = new Object[9];
//		param[0] = "GBPJPY";
//		param[1] = mapParam.get("ID60");
//		param[2] = mapParam.get("VALUEMAX");
//		param[3] = mapParam.get("VALUEMIN");
//		param[4] = mapParam.get("VALUEMAXMIN");
//		param[5] = mapParam.get("VALUEMAXSUPER");
//		param[6] = mapParam.get("TIMEMAX");
//		param[7] = mapParam.get("TIMEMIN");
//		param[8] = mapParam.get("TIMEBAD");
//
//		String sql = "insert into featurestatistics(SYMBOL,ID60,VALUEMAX,VALUEMIN,VALUEMAXSUPER,VALUEMAXMIN," + "TIMEMAX,TIMEMIN,TIMEBAD) values" +
//
//		"(?,?,?,?,?,?,?,?,?)";
//		try {
//			UtilSql.executeUpdate(con, sql, param);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public static void main(String[] args) {
//		CopyOfTask fs = new CopyOfTask();
//		fs.fireTask();
//		// String date1 = "2012.12.05 15:30:31";
//		// String date2 = "2012.12.05 15:35:32";
//		// try {
//		// System.out.println(Fun.diffMinute(date1, date2,
//		// ConstApp.fmDateTime3));
//		// } catch (ParseException e) {
//		// e.printStackTrace();
//		// }
//		System.out.println("Done");
//	}
//}
