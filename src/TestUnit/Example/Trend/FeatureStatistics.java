//package TestUnit.Example.Trend;
//
//import java.sql.Connection;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.ts.common.app.Fun;
//import com.ts.common.app.JDBCManager;
//import com.ts.common.app.Sql;
//import com.ts.common.dbconnection.DbConnection;
//import com.ts.common.dbconnection.DbConnectionDao;
//import com.ts.taskInterface.TaskAbstract;
//
//public class FeatureStatistics extends TaskAbstract {
//	private Connection con;
//	private Bean bean;
//
//	public FeatureStatistics() {
//		this.setTaskStatus("等待执行");
//		this.setTaskFailMsg("");
//	}
//
//	public void fireTask() {
// try {
// // Bean bean = (Bean) JSONObject.toBean(JSONObject.fromObject(this
// // .getJsonStr()), Bean.class);
// bean = new Bean();
// bean.setDbName("ts");
// DbConnection dbconn = DbConnectionDao.getInstance().getMapDbConn(
// bean.getDbName());
//
// con = JDBCManager.getConnection(dbconn);
//
// String sql5 = "select
// dateServer,timeServer,dateLocal,timeLocal,open,close,high,low,ma5,ma20,ma60,kdj,id,type
// from gbpjpy5 order by dateServer ,timeServer ";
// String sql60 = "select
// dateServer,timeServer,dateLocal,timeLocal,open,close,high,low,ma5,ma20,ma60,kdj,id,type
// from gbpjpy60 order by dateServer ,timeServer ";
//
// // String featureStatisticsSql = " insert into featurestatistics "
// //
// +"(symbol,id60,highest5,lowest5,openlowest5,openhighest5,lowesthighest5,timehighest5,timelowest5,elapsedtimehighest5,elapsedtimelowest5)"
// // + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
// Object[] param60 = null;
// Object[] param5 = null;
// ArrayList<HashMap<String, String>> list5 = Sql.executeSql(con,
// sql5, param5);
// ArrayList<HashMap<String, String>> list60 = Sql.executeSql(con,
// sql60, param60);
// for (int i = 0; i < list60.size(); i++) {
// // System.out.println(list60.get(i).getClose());
// String upType = upSymbolType(list60, i);
// // 使用前一根K线，供判断是否破坏趋势
// upFeaturekline(list5, list60, i, upType);
// String downType = downSymbolType(list60, i);
// downFeaturekline(list5, list60, i, downType);
// }
// // 插入分析统计表
// // Sql.executeUpdateByListParam(con, featureStatisticsSql,
// // listParams);
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
//
//	// 分析上涨形态时，统计达到最大最小值以及形态破坏的5分钟K线
//	private void upFeaturekline(List<HashMap<String, String>> list5,
//			List<HashMap<String, String>> list60, int index60, String type)
//			throws ParseException {
//		if (type.equals(""))
//			return;
//		// 以下数值均为形态未破坏前的数值
//		Map<String, String> mapFeaturekline = new HashMap<String, String>();
//		Map<String, String> mapFeaturestatistics = new HashMap<String, String>();
//		mapFeaturekline.put("TYPE", type);
//		String dateTime60 = list60.get(index60 - 1).get("DATESERVER")
//				+ list60.get(index60 - 1).get("TIMESERVER");
//		String id60 = list60.get(index60).get("ID");
//		mapFeaturekline.put("ID60", id60);
//		mapFeaturestatistics.put("ID60", id60);
//
//		double highest5 = 0;
//		double lowest5 = 20120814;
//		int indexMax = 0;
//		int indexMin = 0;
//		int indexBad = 0;
//		for (int i = 0; i < list5.size(); i++) {
//			String dateTime5 = list5.get(i).get("DATESERVER")
//					+ list5.get(i).get("TIMESERVER");
//			if (dateTime5.compareTo(dateTime60) <= 0)
//				continue;
//			// 趋势方向最大值
//			if (Double.valueOf(list5.get(i).get("HIGH")) > highest5) {
//				highest5 = Double.valueOf(list5.get(i).get("HIGH"));
//				indexMax = i;
//			}
//			// 趋势相反方向最大值
//			if (Double.valueOf(list5.get(i).get("LOW")) < lowest5) {
//				lowest5 = Double.valueOf(list5.get(i).get("LOW"));
//				indexMin = i;
//			}
//			if (type.equals("a")) {
//				// 判断形态是否被破坏(涨势中，最低价小于60 线的开盘价【最低价】)
//				if (Double.valueOf(list5.get(i).get("LOW")) < Double
//						.valueOf(list60.get(index60 - 1).get("OPEN"))) {
//					indexBad = i;
//					break;
//				}
//			} else if (type.equals("b")) {
//				// 判断形态是否被破坏(涨势中，最低价小于60线的开盘价【最低价】)
//				if (Double.valueOf(list5.get(i).get("LOW")) < Double
//						.valueOf(list60.get(index60 - 2).get("OPEN"))) {
//					indexBad = i;
//					break;
//				}
//			}
//		}
//		mapFeaturekline.put("MAXID", list5.get(indexMax).get("ID"));
//		mapFeaturekline.put("MINID", list5.get(indexMin).get("ID"));
//		mapFeaturekline.put("BADID", list5.get(indexBad).get("ID"));
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
//			valueMax = Double.valueOf(Fun
//					.isNil(list5.get(indexMax).get("HIGH")))
//					- Double
//							.valueOf(Fun.isNil(list60.get(index60).get("OPEN")));
//			valueMaxSuper = Double.valueOf(Fun.isNil(list5.get(indexMax).get(
//					"HIGH")))
//					- Double.valueOf(Fun.isNil(list60.get(index60).get("LOW")));
//			timeMax = Fun.diffMinute(list60.get(index60).get("DATESERVER")
//					+ " " + list60.get(index60).get("TIMESERVER"), list5.get(
//					indexMax).get("DATESERVER")
//					+ " " + list5.get(indexMax).get("TIMESERVER"),
//					"yyyy.MM.dd HH:mm:ss");
//		}
//
//		if (indexMin > 0) {
//			valueMin = Double
//					.valueOf(Fun.isNil(list5.get(indexMin).get("LOW")))
//					- Double
//							.valueOf(Fun.isNil(list60.get(index60).get("OPEN")));
//
//			timeMin = Fun.diffMinute(list60.get(index60).get("DATESERVER")
//					+ " " + list60.get(index60).get("TIMESERVER"), list5.get(
//					indexMin).get("DATESERVER")
//					+ " " + list5.get(indexMin).get("TIMESERVER"),
//					"yyyy.MM.dd HH:mm:ss");
//		}
//		valueMaxMin = valueMax - valueMin;
//		if (indexBad > 0) {
//			timeBad = Fun.diffMinute(list60.get(index60).get("DATESERVER")
//					+ " " + list60.get(index60).get("TIMESERVER"), list5.get(
//					indexBad).get("DATESERVER")
//					+ " " + list5.get(indexBad).get("TIMESERVER"),
//					"yyyy.MM.dd HH:mm:ss");
//		}
//		mapFeaturestatistics.put("VALUEMAX", String.valueOf(valueMax));
//		mapFeaturestatistics.put("VALUEMIN", String.valueOf(valueMin));
//		mapFeaturestatistics.put("VALUEMAXMIN", String.valueOf(valueMaxMin));
//		mapFeaturestatistics
//				.put("VALUEMAXSUPER", String.valueOf(valueMaxSuper));
//		mapFeaturestatistics.put("TIMEMAX", String.valueOf(timeMax));
//		mapFeaturestatistics.put("TIMEMIN", String.valueOf(timeMin));
//		mapFeaturestatistics.put("TIMEBAD", String.valueOf(timeBad));
//		updateFeaturestatistics(mapFeaturestatistics);
//	}
//
//	// 分析下跌形态时，统计达到最大最小值以及形态破坏的5分钟K线
//	private void downFeaturekline(List<HashMap<String, String>> list5,
//			List<HashMap<String, String>> list60, int index60, String type)
//			throws ParseException {
//		if (type.equals(""))
//			return;
//		// 以下数值均为形态未破坏前的数值
//		Map<String, String> mapFeatureKLine = new HashMap<String, String>();
//		Map<String, String> mapFeaturestatistics = new HashMap<String, String>();
//		mapFeatureKLine.put("TYPE", type);
//		String dateTime60 = list60.get(index60 - 1).get("DATESERVER")
//				+ list60.get(index60 - 1).get("TIMESERVER");
//
//		String id60 = list60.get(index60).get("ID");
//		mapFeatureKLine.put("ID60", id60);
//		mapFeaturestatistics.put("ID60", id60);
//		double highest5 = 0;
//		double lowest5 = 20120814;
//		int indexMax = 0;
//		int indexMin = 0;
//		int indexBad = 0;
//		for (int i = 0; i < list5.size(); i++) {
//			String dateTime5 = list5.get(i).get("DATESERVER")
//					+ list5.get(i).get("TIMESERVER");
//			if (dateTime5.compareTo(dateTime60) <= 0)
//				continue;
//			// 趋势相反方向最大值
//			if (Double.valueOf(list5.get(i).get("HIGH")) > highest5) {
//				highest5 = Double.valueOf(list5.get(i).get("HIGH"));
//				indexMin = i;
//			}
//			// 趋势方向最大值
//			if (Double.valueOf(list5.get(i).get("LOW")) < lowest5) {
//				lowest5 = Double.valueOf(list5.get(i).get("LOW"));
//				indexMax = i;
//			}
//			if (type.equals("A")) {
//				// 判断形态是否被破坏(涨势中，最低价小于60 线的开盘价【最低价】)
//				if (Double.valueOf(list5.get(i).get("LOW")) > Double
//						.valueOf(list60.get(index60 - 1).get("OPEN"))) {
//					indexBad = i;
//					break;
//				}
//			} else if (type.equals("B")) {
//				// 判断形态是否被破坏(涨势中，最低价小于60线的开盘价【最低价】)
//				if (Double.valueOf(list5.get(i).get("LOW")) > Double
//						.valueOf(list60.get(index60 - 2).get("OPEN"))) {
//					indexBad = i;
//					break;
//				}
//			}
//		}
//		mapFeatureKLine.put("MINID", list5.get(indexMin).get("ID"));
//		mapFeatureKLine.put("MAXID", list5.get(indexMax).get("ID"));
//		mapFeatureKLine.put("BADID", list5.get(indexBad).get("ID"));
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
//			valueMax = -Double.valueOf(Fun
//					.isNil(list5.get(indexMax).get("LOW")))
//					+ Double
//							.valueOf(Fun.isNil(list60.get(index60).get("OPEN")));
//			valueMaxSuper = -Double.valueOf(Fun.isNil(list5.get(indexMax).get(
//					"LOW")))
//					+ Double
//							.valueOf(Fun.isNil(list60.get(index60).get("HIGH")));
//			timeMax = Fun.diffMinute(list60.get(index60).get("DATESERVER")
//					+ " " + list60.get(index60).get("TIMESERVER"), list5.get(
//					indexMax).get("DATESERVER")
//					+ " " + list5.get(indexMax).get("TIMESERVER"),
//					"yyyy.MM.dd HH:mm:ss");
//		}
//		if (indexMin > 0) {
//			valueMin = -Double.valueOf(Fun.isNil(list5.get(indexMin)
//					.get("HIGH")))
//					+ Double
//							.valueOf(Fun.isNil(list60.get(index60).get("OPEN")));
//			timeMin = Fun.diffMinute(list60.get(index60).get("DATESERVER")
//					+ " " + list60.get(index60).get("TIMESERVER"), list5.get(
//					indexMin).get("DATESERVER")
//					+ " " + list5.get(indexMin).get("TIMESERVER"),
//					"yyyy.MM.dd HH:mm:ss");
//		}
//		valueMaxMin = valueMax - valueMin;
//		if (indexBad > 0) {
//			timeBad = Fun.diffMinute(list60.get(index60).get("DATESERVER")
//					+ " " + list60.get(index60).get("TIMESERVER"), list5.get(
//					indexBad).get("DATESERVER")
//					+ " " + list5.get(indexBad).get("TIMESERVER"),
//					"yyyy.MM.dd HH:mm:ss");
//		}
//		mapFeaturestatistics.put("VALUEMAX", String.valueOf(valueMax));
//		mapFeaturestatistics.put("VALUEMIN", String.valueOf(valueMin));
//		mapFeaturestatistics.put("VALUEMAXMIN", String.valueOf(valueMaxMin));
//		mapFeaturestatistics
//				.put("VALUEMAXSUPER", String.valueOf(valueMaxSuper));
//		mapFeaturestatistics.put("TIMEMAX", String.valueOf(timeMax));
//		mapFeaturestatistics.put("TIMEMIN", String.valueOf(timeMin));
//		mapFeaturestatistics.put("TIMEBAD", String.valueOf(timeBad));
//		updateFeaturestatistics(mapFeaturestatistics);
//	}
//
//	private String downSymbolType(List<HashMap<String, String>> list60,
// int index) {
// String type = "";
// if (index < 2)
// return type;
//
// // 前2根是阳线，前一根是阴线，前一根的收盘价比前二根开盘价低，记录本根K线
// // 阳线
// if (Double.valueOf(list60.get(index - 2).get("CLOSE")) > Double
// .valueOf(list60.get(index - 2).get("OPEN"))) {
// // 阴线
// if (Double.valueOf(list60.get(index - 1).get("CLOSE")) < Double
// .valueOf(list60.get(index - 1).get("OPEN"))) {
// // 前一根的收盘价比前二根开盘价高
// if (Double.valueOf(list60.get(index - 1).get("CLOSE")) < Double
// .valueOf(list60.get(index - 2).get("OPEN"))) {
// type = "A";
// updateSymbolType(type, Integer.valueOf(list60.get(index)
// .get("ID")));
// }
// }
// }
//
// if (index < 3)
// return type;
// // 前3根是阳线，前二根是阴线，前二根线收盘价比前3根开盘价高，前一根的收盘价比前二根收盘价低，前一根收盘价比前3根开盘价低，记录本根K线
// if (Double.valueOf(list60.get(index - 3).get("CLOSE")) > Double
// .valueOf(list60.get(index - 3).get("OPEN"))) {
// // 阴线
// if (Double.valueOf(list60.get(index - 2).get("CLOSE")) < Double
// .valueOf(list60.get(index - 2).get("OPEN"))) {
// if (Double.valueOf(list60.get(index - 2).get("CLOSE")) > Double
// .valueOf(list60.get(index - 3).get("OPEN"))) {
// // 前一根的收盘价比前二根开盘价低
// if (Double.valueOf(list60.get(index - 1).get("CLOSE")) < Double
// .valueOf(list60.get(index - 2).get("OPEN"))) {
// // if (list.get(index - 1).get("CLOSE") < list
// // .get(index - 3).get("OPEN")) {
// type = "B";
// updateSymbolType(type, Integer.valueOf(list60
// .get(index).get("ID")));
// // }
// }
// }
// }
// }
//
// if (index < 3)
// return type;
// //
// 前3根是阴线，前二根是阳线，前二根线收盘价比前3根开盘价低，前一根的收盘价比前二根收盘价高，前一根收盘价比前3根开盘价低，记录本根K线收盘价比前3根开盘价高
//
// return type;
// }
//
//	private String upSymbolType(List<HashMap<String, String>> list60, int index)
// {
// String type = "";
// if (index < 2)
// return type;
// // 前2根是阴线，前一根是阳线，前一根的收盘价比前二根开盘价高，记录本根K线
// // 阴线
// if (Double.valueOf(list60.get(index - 2).get("CLOSE")) < Double
// .valueOf(list60.get(index - 2).get("OPEN"))) {
// // 阳线
// if (Double.valueOf(list60.get(index - 1).get("CLOSE")) > Double
// .valueOf(list60.get(index - 1).get("OPEN"))) {
// // 前一根的收盘价比前二根开盘价高
// if (Double.valueOf(list60.get(index - 1).get("CLOSE")) > Double
// .valueOf(list60.get(index - 2).get("OPEN"))) {
// type = "a";
// updateSymbolType(type, Integer.valueOf(list60.get(index)
// .get("ID")));
// }
// }
// }
// if (index < 3)
// return type;
// // 前3根是阴线，前二根是阳线，前二根线收盘价比前3根开盘价低，前一根的收盘价比前二根收盘价高，前一根收盘价比前3根开盘价高，记录本根K线
// if (Double.valueOf(list60.get(index - 3).get("CLOSE")) < Double
// .valueOf(list60.get(index - 3).get("OPEN"))) {
// // 阳线
// if (Double.valueOf(list60.get(index - 2).get("CLOSE")) > Double
// .valueOf(list60.get(index - 2).get("OPEN"))) {
// if (Double.valueOf(list60.get(index - 2).get("CLOSE")) < Double
// .valueOf(list60.get(index - 3).get("OPEN"))) {
// // 前一根的收盘价比前二根开盘价高
// if (Double.valueOf(list60.get(index - 1).get("CLOSE")) > Double
// .valueOf(list60.get(index - 2).get("OPEN"))) {
// // if (list.get(index - 1).get("CLOSE") > list
// // .get(index - 3).get("OPEN")) {
// type = "b";
// updateSymbolType(type, Integer.valueOf(list60
// .get(index).get("ID")));
// // }
// }
// }
// }
// }
// if (index < 3)
// return type;
// //
// 前3根是阴线，前二根是阳线，前二根线收盘价比前3根开盘价低，前一根的收盘价比前二根收盘价高，前一根收盘价比前3根开盘价低，记录本根K线收盘价比前3根开盘价高
//
// return type;
// }
//
//	private void updateSymbolType(String type, int id) {
//		Object[] param = new Object[2];
//		param[0] = type;
//		param[1] = id;
//		String sql = "update gbpjpy60 set type=? where id=? ";
//		try {
//			Sql.executeUpdate(con, sql, param);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void updateFeatureKLine(Map<String, String> mapParam) {
// Object[] param = new Object[6];
// param[0] = "gbpjpy";
// param[1] = mapParam.get("ID60");
// param[2] = mapParam.get("MAXID");
// param[3] = mapParam.get("MINID");
// param[4] = mapParam.get("BADID");
// param[5] = mapParam.get("TYPE");
// String sql = "insert into featurekline (symbol, id60, id5max,id5min,
// id5bad,type) values"
// + "(?,?,?,?,?,?)";
// try {
// Sql.executeUpdate(con, sql, param);
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
//
//	private void updateFeaturestatistics(Map<String, String> mapParam) {
// Object[] param = new Object[9];
// param[0] = "GBPJPY";
// param[1] = mapParam.get("ID60");
// param[2] = mapParam.get("VALUEMAX");
// param[3] = mapParam.get("VALUEMIN");
// param[4] = mapParam.get("VALUEMAXMIN");
// param[5] = mapParam.get("VALUEMAXSUPER");
// param[6] = mapParam.get("TIMEMAX");
// param[7] = mapParam.get("TIMEMIN");
// param[8] = mapParam.get("TIMEBAD");
//
// String sql = "insert into
// featurestatistics(SYMBOL,ID60,VALUEMAX,VALUEMIN,VALUEMAXSUPER,VALUEMAXMIN,"
// + "TIMEMAX,TIMEMIN,TIMEBAD) values" + "(?,?,?,?,?,?,?,?,?)";
// try {
// Sql.executeUpdate(con, sql, param);
// } catch (Exception e) {
// e.printStackTrace();
// }
//
// }
//
//	public void fireTask(String startTime, String groupId, String scheCod,
//			String taskOrder) {
//		fireTask();
//
//	}
//
//	public static void main(String[] args) {
//		FeatureStatistics fs = new FeatureStatistics();
//		fs.fireTask();
//		// String date1 = "2012.12.05 15:30:31";
//		// String date2 = "2012.12.05 15:35:32";
//		// try {
//		// System.out.println(Fun.diffMinute(date1, date2,
//		// "yyyy.MM.dd HH:mm:ss"));
//		// } catch (ParseException e) {
//		// e.printStackTrace();
//		// }
//		System.out.println("Done");
//	}
//}
