//package com.task.Economic;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import module.dbconnection.DbConnection;
//import module.dbconnection.DbConnectionDao;
//
//import org.htmlparser.Node;
//import org.htmlparser.NodeFilter;
//import org.htmlparser.Parser;
//import org.htmlparser.filters.AndFilter;
//import org.htmlparser.filters.HasAttributeFilter;
//import org.htmlparser.filters.OrFilter;
//import org.htmlparser.filters.TagNameFilter;
//import org.htmlparser.util.NodeList;
//import org.htmlparser.util.ParserException;
//
//import com.task.Economic.entity.EconomicData;
//import com.task.Economic.entity.EconomicHoliday;
//import com.task.Economic.entity.EconomicNationalDebt;
//import com.taskInterface.TaskAbstract;
//import common.util.UtilFun;
//import common.util.UtilGuid;
//import common.util.conver.UtilConver;
//import common.util.date.UtilDate;
//import common.util.file.UtilFile;
//import common.util.jdbc.UtilJDBCManager;
//import common.util.jdbc.UtilSql;
//import common.util.json.UtilJson;
//import common.util.string.UtilString;
//
//import consts.Const;
//
///**
// * @Description:针对汇通网 http://www.fx678.com/indexs/index.shtml 导入财经相关数据
// * @date Feb 11, 2014
// * @author:fgq
// * @modify : 20140310 增加福汇财经数据导入
// */
//public class Task extends TaskAbstract {
//	private Connection con;
//	private PreparedStatement psInsert;
//	private PreparedStatement psUpdate;
//	private PreparedStatement psDelete;
//	// 程序刚启动时获取当天的经济数据Sql
//	private String sqlGetEconomicDataPublishedValue = "select  publishdate,publishtime,country,indicator,importance,previousvalue,predictedvalue,publishedvalue from economic_data where publishdate=? and source=? ";
//	// 汇通使用该变量
//	private static String publishDateHT = "";
//	// 汇通使用该变量,经济数据使用内存判断发布值是否更新
//	private static Map<String, EconomicData> mapEconomicDataHT;
//	// 福汇使用该变量
//	private static String publishDateFH = "";
//	// 福汇使用该变量,经济数据使用内存判断发布值是否更新
//	private static Map<String, EconomicData> mapEconomicDataFH;
//	// 福汇最近未发布时间
//	private static List<String> listUnPublisTimeFH = new ArrayList<String>();
//	// 还有未发布指标
//	private static boolean hasUnPublishIndicatorFH = true;
//	private static boolean hasUnPublishIndicatorHT = true;
//	// 汇通最近未发布时间（未实现）
//	private static List<String> listUnPublisTimeHT = new ArrayList<String>();
//	// 更新经济数据发布值sql
//	private String sqlUpdateEconomicDataPublishedValue = "update economic_data set publishedValue=?,compareresult=?,modifydate=sysdate where publishdate=? and indicator=? and source=?  ";
//	// 插入经济数据记录sql
//	private String sqlInsertEconomicData = "insert into economic_data(id,publishdate,publishtime,country,indicator,importance,previousvalue,predictedvalue,publishedvalue,source,compareresult,predictedresult)values('"+UtilGuid.getGuid()+"',?,?,?,?,?,?,?,?,?,?,?)";
//	// private String sqlSelectEconomicData = "select
//	// id,publishdate,publishtime,country,indicator,importance,previousvalue,predictedvalue,publishedvalue,source,compareresult
//	// from economic_data where publishDate=? and source=?";
//	private String sqlDeleteEconomicData = "delete from economic_data where publishDate=? and source=? ";
//	// 获取财经事件记录sql
//	private String sqlGetEconomicEvent = "select count(1) from economic_event where occurDate=? and occurTime=? and country=? and site=? and event=?";
//	// 插入财经事件记录sql
//	private String sqlInsertEconomicEvent = "insert  into economic_event(id,occurDate,occurTime,country,site,importance,event)values('"+UtilGuid.getGuid()+"',?,?,?,?,?,?)";
//	// 获取假期预告记录Sql
//	private String sqlGetEconomicHoliday = "select count(1) from economic_holiday where occurDate=? and occurTime=? and country=? and site=? and event=?";
//	// 插入假期预告记录sql
//	private String sqlInsertEconomicHoliday = "insert  into economic_holiday(id,occurDate,occurTime,country,site,event)values('"+UtilGuid.getGuid()+"',?,?,?,?,?)";
//	// 获取国债预告记录sql
//	private String sqlGetEconomicNationalDebt = "select count(1) from economic_NationalDebt where occurDate=? and occurTime=? and country=? and site=? and event=?";
//	// 插入国债预告记录sql
//	private String sqlInsertEconomicNationalDebt = "insert  into economic_NationalDebt(id,occurDate,occurTime,country,site,importance,event)values('"+UtilGuid.getGuid()+"',?,?,?,?,?,?)";
//	private static String[] countryArr = null;
//	// key =source+"|"+publishDate ; value=0表示未曾匹配，value=1表示已匹配
//	private static Map<String, Integer> mapMatch;
//	// 手动执行时需执行删除该日期段的数据，否则会出现数据重复录入
//	public void fireTask() {
//		DbConnection dbconn = null;
//		String curDate = "";
//		try {
//			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
//			dbconn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
//			if (dbconn == null) {
//				this.setTaskStatus("执行失败");
//				this.setTaskMsg("获取数据库连接错误");
//				return;
//			}
//			curDate = UtilConver.dateToStr(this.getDate(), Const.fm_yyyyMMdd);
//			con = UtilJDBCManager.getConnection(dbconn);
//			if ("福汇网".equals(bean.getSource())) {
//				if (!publishDateFH.equals(curDate)) {
//					hasUnPublishIndicatorFH = true;
//				}
//				if (!hasUnPublishIndicatorFH) {
//					this.setTaskStatus("执行成功");
//					this.setTaskMsg(bean.getSource() + ":" + "财经数据已全部发布[" + curDate + "]");
//					return;
//				}
//				// 调度保护：当指标中侦查中的指标未到达时点时，获取网页的函数自动跑空，只要是防止过度访问网站。
//				if (listUnPublisTimeFH.size() > 0) {
//					// 取list中的第一个，即最近的未发布时间
//					if (UtilConver.dateToStr("HH:mm").compareTo(listUnPublisTimeFH.get(0)) < 0) {
//						this.setTaskStatus("执行成功");
//						this.setTaskMsg(bean.getSource() + ":" + UtilConver.dateToStr(Const.fm_HHmmss) + "未到达最近的未发布时间：" + listUnPublisTimeFH.get(0));
//						return;
//					} else {
//						listUnPublisTimeFH.clear();
//					}
//				}
//				// ///////////////////////////////////////////
//				if (mapEconomicDataFH == null) {
//					mapEconomicDataFH = new HashMap<String, EconomicData>();
//					List<HashMap<String, Object>> list = UtilSql.QueryM(con, sqlGetEconomicDataPublishedValue, new Object[] { curDate, 1 });
//					if (list != null) {
//						for (Map<String, Object> map : list) {
//							EconomicData economicData = (EconomicData) UtilConver.convertMap(map, EconomicData.class);
//							economicData.setSource(1);
//							String key = economicData.getIndicator() + economicData.getPublishTime() + economicData.getSource();
//							mapEconomicDataFH.put(key, economicData);
//						}
//					}
//				}
//				if (!publishDateFH.equals(curDate)) {
//					mapEconomicDataFH.clear();
//					deleteEconomicData(curDate, 1);
//					publishDateFH = curDate;
//				}
//				this.psInsert = con.prepareStatement(sqlInsertEconomicData);
//				this.psUpdate = con.prepareStatement(sqlUpdateEconomicDataPublishedValue);
//				//2015-07-01 网上下载数据失效了
//			//	if (curDate.equals(UtilConver.dateToStr(Const.fm_yyyyMMdd))) {// 获取当天时从网站获取
//			//			String url = "http://www.dailyfx.com.hk/calendar/index.html";
//			//			getEconomicDataFhRealTime(url);
//			//	}else
//				{
//					File inFile = new File(bean.getUrl());
//				    this.formatEconomicDataFileFH(inFile);
//					this.getEconomicDataFH(bean.getUrl());
//				}
//				this.psInsert.executeBatch();
//				this.psInsert.clearBatch();
//				this.psUpdate.executeBatch();
//				this.psUpdate.clearBatch();
//				math(curDate, 1);
//			} else if ("汇通网".equals(bean.getSource())) {
//				if (bean.isEnconomicData()) {
//					if (!publishDateHT.equals(curDate)) {
//						hasUnPublishIndicatorHT = true;
//					}
//					if (!hasUnPublishIndicatorHT) {
//						this.setTaskStatus("执行成功");
//						this.setTaskMsg(bean.getSource() + ":" + "财经数据已全部发布[" + curDate + "]");
//						return;
//					}
//					// 调度保护：当指标中侦查中的指标未到达时点时，获取网页的函数自动跑空，只要是防止过度访问网站。
//					if (listUnPublisTimeHT.size() > 0) {
//						// 取list中的第一个，即最近的未发布时间
//						if (UtilConver.dateToStr("HH:mm").compareTo(listUnPublisTimeHT.get(0)) < 0) {
//							this.setTaskStatus("执行成功");
//							this.setTaskMsg(bean.getSource() + ":" + UtilConver.dateToStr(Const.fm_HHmmss) + "未到达最近的未发布时间：" + listUnPublisTimeHT.get(0));
//							return;
//						} else {
//							listUnPublisTimeHT.clear();
//						}
//					}
//					// ///////////////////////////////////////////
//					if (mapEconomicDataHT == null) {
//						mapEconomicDataHT = new HashMap<String, EconomicData>();
//						List<HashMap<String, Object>> list = UtilSql.QueryM(con, sqlGetEconomicDataPublishedValue, new Object[] { curDate, 0 });
//						if (list != null) {
//							for (Map<String, Object> map : list) {
//								EconomicData economicData = (EconomicData) UtilConver.convertMap(map, EconomicData.class);
//								economicData.setSource(0);
//								String key = economicData.getIndicator() + economicData.getPublishTime() + economicData.getSource();
//								mapEconomicDataHT.put(key, economicData);
//							}
//						}
//					}
//					if (!publishDateHT.equals(curDate)) {
//						mapEconomicDataHT.clear();
//						deleteEconomicData(curDate, 0);
//						publishDateHT = curDate;
//					}
//					this.psInsert = con.prepareStatement(sqlInsertEconomicData);
//					this.psUpdate = con.prepareStatement(sqlUpdateEconomicDataPublishedValue);
//					this.getEconomicDataHT(bean.getUrl(), curDate);
//					this.psInsert.executeBatch();
//					this.psInsert.clearBatch();
//					this.psUpdate.executeBatch();
//					this.psUpdate.clearBatch();
//					math(curDate, 0);
//				}
//				if (bean.isEnconomicEvent()) {
//					this.psInsert = con.prepareStatement(sqlInsertEconomicEvent);
//					this.getEconomicEvent(bean.getUrl(), curDate);
//					this.psInsert.executeBatch();
//					this.psInsert.clearBatch();
//				}
//				if (bean.isEnconomicHoliday()) {
//					this.psInsert = con.prepareStatement(sqlInsertEconomicHoliday);
//					this.getEconomicHoliday(bean.getUrl(), curDate);
//					this.psInsert.executeBatch();
//					this.psInsert.clearBatch();
//				}
//				if (bean.isEnconomicNationalDebt()) {
//					this.psInsert = con.prepareStatement(sqlInsertEconomicNationalDebt);
//					this.getEconomicNationalDebt(bean.getUrl(), curDate);
//					this.psInsert.executeBatch();
//					this.psInsert.clearBatch();
//				}
//				if (bean.isSaveFile()) {
//					this.getHtmlFile(bean);
//				}
//			}
//			this.setTaskStatus("执行成功");
//			this.setTaskMsg(bean.getSource() + "数据采集执行成功[" + curDate + "]");
//		} catch (Exception e) {
//			this.setTaskStatus("执行失败");
//			this.setTaskMsg("执行错误[" + curDate + "]:", e);
//		} finally {
//			UtilSql.close(this.con, this.psInsert, this.psUpdate);
//		}
//	}
//
//	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
//		fireTask();
//	}
//
//	public List<String> getListUnPublisTimeFh() {
//		if (listUnPublisTimeFH == null)
//			listUnPublisTimeFH = new ArrayList<String>();
//		return listUnPublisTimeFH;
//	}
//
//	/**
//	 * @Description:删除当前的经济数据
//	 * @param date
//	 * @param source
//	 * @throws SQLException void
//	 * @date Mar 28, 2014
//	 * @author:fgq
//	 */
//	private void deleteEconomicData(String date, int source) throws SQLException {
//		this.psDelete = con.prepareStatement(sqlDeleteEconomicData);
//		this.psDelete.setString(1, date);
//		this.psDelete.setInt(2, source);
//		this.psDelete.addBatch();
//		this.psDelete.executeBatch();
//		this.psDelete.clearBatch();
//	}
//
//	// 获取汇通各国经济数据指标(手工维护，一次性)
//	public void getEconomicIndicator(String url) throws ParserException {
//		// url =
//		// "http://www.fx678.com/indexchart/flash/economyData/indexchart.aspx?id=1232";
//		String[] arrExceptStr = new String[] { "&nbsp;" };
//		Parser parser = new Parser(url);
//		parser.setEncoding("GB2312");
//		NodeFilter[] nodeFilter = new NodeFilter[1];
//		nodeFilter[0] = new TagNameFilter("a");
//		OrFilter andFilter = new OrFilter(nodeFilter);
//		NodeList nodeList = parser.parse(andFilter);
//		Node[] nodes = nodeList.toNodeArray();
//		String line = null;
//		// 每次换时间时出现新一批数据
//		for (int i = 0; i < nodes.length; i++) {
//			line = nodes[i].toPlainTextString();
//			if (line != null && !"".equals(line.trim())) {
//				// 过滤掉不需要的字符
//				for (String exceptStr : arrExceptStr) {
//					line = line.replace(exceptStr, "");
//				}
//				System.out.println(line);
//			}
//		}
//	}
//
//	// 获取汇通经济数据
//	private void getEconomicDataHT(String url, String date) throws Exception {
//		String[] arrExceptStr = new String[] { "&nbsp;" };
//		Parser parser = new Parser(url);
//		parser.setEncoding("GB2312");
//		NodeFilter[] nodeFilter = new NodeFilter[1];
//		nodeFilter[0] = new TagNameFilter("td");
//		// nodeFilter[0]=new NodeClassFilter(Span.class);
//		// nodeFilter[1] = new HasAttributeFilter("class", "TableBlock");
//		AndFilter andFilter = new AndFilter(nodeFilter);
//		// NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
//		NodeList nodeList = parser.parse(andFilter);
//		Node[] nodes = nodeList.toNodeArray();
//		String line = null;
//		String time = "";
//		String country = "";
//		// 每次换时间时出现新一批数据
//		EconomicData economicData = new EconomicData();
//		int newDataLineCount = 0;
//		for (int i = 0; i < nodes.length; i++) {
//			line = nodes[i].toPlainTextString();
//			if (line != null && !"".equals(line.trim())) {
//				// 过滤掉不需要的字符
//				for (String exceptStr : arrExceptStr) {
//					line = line.replace(exceptStr, "");
//					// System.out.println(line);
//				}
//				// 判断是否以HH:mm时间开头
//				if (UtilDate.isTime(line)) {
//					newDataLineCount = 0;
//					System.out.println(line);
//					time = line;
//					country = "";
//					// System.out.println(time+" "+ country);
//				} else {
//					newDataLineCount += 1;
//					if (newDataLineCount == 1) {
//						country = line;
//					} else if (newDataLineCount == 2) {
//						continue;
//					} else if ((newDataLineCount - 3) % 5 == 0) {
//						if ("---".equals(line)) {
//							newDataLineCount -= 1;
//							continue;
//						}
//						economicData = new EconomicData();
//						economicData.setPublishDate(date.replace("-", "").replace(":", ""));
//						economicData.setPublishTime(time.replace(":", ""));
//						economicData.setCountry(country);
//						for (int index = 0; index < Const.EscapeWord.length; index++) {
//							line = line.replace(Const.EscapeWord[index][0], Const.EscapeWord[index][1]);
//						}
//						// 记录未发布的时间
//						if ("侦查中".equals(line.trim())) {
//							if (!"".equals(UtilString.isNil(economicData.getPublishTime()))) {
//								listUnPublisTimeHT.add(economicData.getPublishTime());
//							}
//						}
//						economicData.setIndicator(line.toUpperCase());
//					} else if ((newDataLineCount - 3) % 5 == 1) {
//						economicData.setImportance(line);
//					} else if ((newDataLineCount - 3) % 5 == 2) {
//						economicData.setPreviousValue(line);
//					} else if ((newDataLineCount - 3) % 5 == 3) {
//						economicData.setPredictedValue(line);
//					} else if ((newDataLineCount - 3) % 5 == 4) {
//						economicData.setPublishedValue(line);
//						String key = economicData.getIndicator() + economicData.getPublishTime() + economicData.getSource();
//						EconomicData tmpEconomicData = mapEconomicDataHT.get(key);
//						if (tmpEconomicData != null) {
//							if (!economicData.getPublishedValue().equals(tmpEconomicData.getPublishedValue())) {
//								this.psUpdate.setString(1, economicData.getPublishedValue());
//								this.psUpdate.setString(2, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
//								this.psUpdate.setString(3, economicData.getPublishDate());
//								this.psUpdate.setString(4, economicData.getIndicator());
//								this.psUpdate.setInt(5, economicData.getSource());
//								this.psUpdate.addBatch();
//								mapEconomicDataHT.put(key, economicData);
//							}
//						} else {
//							this.psInsert.setString(1, economicData.getPublishDate());
//							this.psInsert.setString(2, economicData.getPublishTime());
//							this.psInsert.setString(3, economicData.getCountry());
//							this.psInsert.setString(4, economicData.getIndicator());
//							this.psInsert.setString(5, economicData.getImportance());
//							this.psInsert.setString(6, economicData.getPreviousValue());
//							this.psInsert.setString(7, economicData.getPredictedValue());
//							this.psInsert.setString(8, economicData.getPublishedValue());
//							this.psInsert.setInt(9, economicData.getSource());
//							this.psInsert.setString(10, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
//							this.psInsert.setString(11, this.getcompareValue(economicData.getPredictedValue(), economicData.getPreviousValue()));
//							this.psInsert.addBatch();
//							mapEconomicDataHT.put(key, economicData);
//						}
//					}
//				}
//			}
//		}
//		if (listUnPublisTimeHT.size() == 0)
//			hasUnPublishIndicatorHT = false;
//	}
//
//	//格式化文件 ：主要针对福汇经济数据中 有历史数据的经济数据
//	private void  formatEconomicDataFileFH(File inFile) throws Exception{
//		StringBuffer sb=new StringBuffer();
//		BufferedReader buffReader = null;
//		String sline = "";
//		buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
//		boolean his=false;
//		while ((sline = buffReader.readLine()) != null) {
//			if("".equals(UtilString.isNil(sline.trim()))){
//				his=true;
//				continue;
//			}
//			if(his==false){
//				sb.append(sline).append("\r\n");
//			}else{
//				int idx=sb.lastIndexOf("\r\n");
//				sb.delete(idx,idx+"\r\n".length()) .append("\t");
//				sb.append(sline.trim()).append("\r\n");
//				his=false;
//			}
//		}
//		String content=sb.toString();
//		BufferedWriter fWriter = new BufferedWriter(new FileWriter(inFile.getAbsoluteFile()));
//		fWriter.write(content);
//		fWriter.flush();
//		fWriter.close();
//	}
//	// 通过txt文件获取福汇经济数据
//	private void getEconomicDataFH(String filePath) throws Exception {
//		File inFile =new File(filePath);
//		if(!UtilFile.exists(inFile.getParent()+"/bak/"+inFile.getName())){
//			UtilFile.copyFile(filePath, inFile.getParent()+"/bak/"+inFile.getName());
//		}
//		BufferedReader buffReader = null;
//		String sline = "";
//		String[] lineArr = null;
//		EconomicData economicData = null;
//		buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile),"utf-8"));
//		String publishDate=inFile.getName().substring(0,inFile.getName().indexOf(".")).replaceAll("-","");
//		String time="";
//		while ((sline = buffReader.readLine()) != null) {
//			System.out.println(sline.replace("?", "").replace("\t", " "));
//			lineArr = sline.replace("?", "").replace("\t", " ").replace("  ", " ").split(" ");
//			economicData = new EconomicData();
//			economicData.setPublishDate(publishDate);
//			economicData.setCountry("");
//			economicData.setSource(1);
//			String line="";
//			if (lineArr[0].indexOf("/")>0 && lineArr[1].indexOf(":")>0) {
//				time=lineArr[1].replace(":", "");
//				if(UtilDate.isTime(lineArr[1])){
//					economicData.setPublishTime(time);
//					line = lineArr[3];
//					economicData.setImportance(lineArr[4]);
//					economicData.setPreviousValue(lineArr[5]);
//					if(lineArr.length>7){
//						economicData.setPredictedValue(lineArr[6]);
//						economicData.setPublishedValue(lineArr[7]);
//					}else{
//						economicData.setPublishedValue(lineArr[6]);
//					}
//				}else{
//					line = lineArr[2];
//					economicData.setImportance(lineArr[3]);
//					economicData.setPreviousValue(lineArr[4]);
//					if(lineArr.length>7){
//						economicData.setPredictedValue(lineArr[5]);
//						economicData.setPublishedValue(lineArr[6]);
//					}else if(lineArr.length>6){
//						economicData.setPublishedValue(lineArr[5]);
//					}
//				}
//				
//			} else {
//				economicData.setPublishTime(time);
//				line = lineArr[0];
//				economicData.setImportance(lineArr[1]);
//				economicData.setPreviousValue(lineArr[2]);
//				if(lineArr.length>4){
//					economicData.setPredictedValue(lineArr[3]);
//					economicData.setPublishedValue(lineArr[4]);
//				}else{
//					economicData.setPublishedValue(lineArr[3]);
//				}
//			}
//			for (int index = 0; index < Const.EscapeWord.length; index++) {
//				line = line.replace(Const.EscapeWord[index][0], Const.EscapeWord[index][1]);
//			}
//			economicData.setIndicator(line.toUpperCase());
//			String key = economicData.getIndicator() + economicData.getPublishTime() + economicData.getSource();
//			EconomicData tmpEconomicData = mapEconomicDataFH.get(key);
//			if (tmpEconomicData != null) {
//				if (!economicData.getPublishedValue().equals(tmpEconomicData.getPublishedValue())) {
//					this.psUpdate.setString(1, economicData.getPublishedValue());
//					this.psUpdate.setString(2, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
//					this.psUpdate.setString(3, economicData.getPublishDate());
//					this.psUpdate.setString(4, economicData.getIndicator());
//					this.psUpdate.setInt(5, economicData.getSource());
//					this.psUpdate.addBatch();
//					mapEconomicDataFH.put(key, economicData);
//				}
//			} else {
//				this.psInsert.setString(1, economicData.getPublishDate());
//				this.psInsert.setString(2, economicData.getPublishTime());
//				this.psInsert.setString(3, economicData.getCountry());
//				this.psInsert.setString(4, economicData.getIndicator());
//				this.psInsert.setString(5, economicData.getImportance());
//				this.psInsert.setString(6, economicData.getPreviousValue());
//				this.psInsert.setString(7, economicData.getPredictedValue());
//				this.psInsert.setString(8, economicData.getPublishedValue());
//				this.psInsert.setInt(9, economicData.getSource());
//				this.psInsert.setString(10, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
//				this.psInsert.setString(11, this.getcompareValue(economicData.getPredictedValue(), economicData.getPreviousValue()));
//				this.psInsert.addBatch();
//				mapEconomicDataFH.put(key, economicData);
//			}
//		}
//	}
//	
//	// 通过txt文件获取福汇经济数据  改版前的标准格式，需要先执行formatEconomicDataFileFH_bak
//	@SuppressWarnings("unused")
//	private void getEconomicDataFH_bak(String filePath) throws Exception {
//		File inFile =new File(filePath);
//		if(!UtilFile.exists(inFile.getParent()+"/bak/"+inFile.getName())){
//			UtilFile.copyFile(filePath, inFile.getParent()+"/bak/"+inFile.getName());
//		}
//		BufferedReader buffReader = null;
//		String sline = "";
//		String[] lineArr = null;
//		EconomicData economicData = null;
//		buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile),"utf-8"));
//		while ((sline = buffReader.readLine()) != null) {
//			System.out.println(sline.replace("?", "").replace("\t", " "));
//			lineArr = sline.replace("?", "").replace("\t", " ").split(" ");
//			economicData = new EconomicData();
//			if (lineArr[0].length() > 10) {
//				economicData.setPublishDate(lineArr[0].substring(1).replace("-", "").replace(":", ""));
//			} else {
//				economicData.setPublishDate(lineArr[0].replace("-", "").replace(":", ""));
//			}
//			economicData.setCountry("");
//			economicData.setSource(1);
//			economicData.setPublishTime(lineArr[1].replace(":", ""));
//			String line = lineArr[2];
//			for (int index = 0; index < Const.EscapeWord.length; index++) {
//				line = line.replace(Const.EscapeWord[index][0], Const.EscapeWord[index][1]);
//			}
//			economicData.setIndicator(line.toUpperCase());
//			economicData.setImportance(lineArr[3]);
//			economicData.setPreviousValue(lineArr[4]);
//			economicData.setPredictedValue(lineArr[5]);
//			economicData.setPublishedValue(lineArr[6]);
//			String key = economicData.getIndicator() + economicData.getPublishTime() + economicData.getSource();
//			EconomicData tmpEconomicData = mapEconomicDataFH.get(key);
//			if (tmpEconomicData != null) {
//				if (!economicData.getPublishedValue().equals(tmpEconomicData.getPublishedValue())) {
//					this.psUpdate.setString(1, economicData.getPublishedValue());
//					this.psUpdate.setString(2, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
//					this.psUpdate.setString(3, economicData.getPublishDate());
//					this.psUpdate.setString(4, economicData.getIndicator());
//					this.psUpdate.setInt(5, economicData.getSource());
//					this.psUpdate.addBatch();
//					mapEconomicDataFH.put(key, economicData);
//				}
//			} else {
//				this.psInsert.setString(1, economicData.getPublishDate());
//				this.psInsert.setString(2, economicData.getPublishTime());
//				this.psInsert.setString(3, economicData.getCountry());
//				this.psInsert.setString(4, economicData.getIndicator());
//				this.psInsert.setString(5, economicData.getImportance());
//				this.psInsert.setString(6, economicData.getPreviousValue());
//				this.psInsert.setString(7, economicData.getPredictedValue());
//				this.psInsert.setString(8, economicData.getPublishedValue());
//				this.psInsert.setInt(9, economicData.getSource());
//				this.psInsert.setString(10, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
//				this.psInsert.setString(11, this.getcompareValue(economicData.getPredictedValue(), economicData.getPreviousValue()));
//				this.psInsert.addBatch();
//				mapEconomicDataFH.put(key, economicData);
//			}
//		}
//	}
//	// 获取汇通财经事件
//	private void getEconomicEvent(String url, String date) throws Exception {
//		boolean isEvent = false;// 由于财经事件与假期使用了同样的class,故在此要分段截取
//		String[] arrExceptStr = new String[] { "&nbsp;" };
//		String[] arrIngoreStr = new String[] { "时间", "国家", "地点", "事件", "重要性", "[]" };
//		String importance = "低中高";
//		boolean ingored = false;
//		Parser parser = new Parser(url);
//		parser.setEncoding("GB2312");
//
//		NodeFilter[] arrNodeFilter = new NodeFilter[4];
//		arrNodeFilter[0] = new HasAttributeFilter("class", "w_60");
//		arrNodeFilter[1] = new HasAttributeFilter("class", "w_65");
//		arrNodeFilter[2] = new HasAttributeFilter("class", "w_525 text_l");
//		arrNodeFilter[3] = new HasAttributeFilter("class", "title");
//
//		OrFilter orFilter = new OrFilter(arrNodeFilter);
//		NodeList nodeList = parser.parse(orFilter);
//		Node[] nodes = nodeList.toNodeArray();
//		String line = null;
//		// 每次换时间时出现新一批数据
//		EconomicNationalDebt economicEvent = new EconomicNationalDebt();
//		int newDataLineCount = 0;
//		for (int i = 0; i < nodes.length; i++) {
//			line = nodes[i].toPlainTextString();
//			if (line != null && !"".equals(line.trim())) {
//				// 过滤掉不需要的字符
//				for (String exceptStr : arrExceptStr) {
//					line = line.replace(exceptStr, "");
//				}
//				for (String ingoreStr : arrIngoreStr) {
//					ingored = false;
//					if (ingoreStr.equals(line.trim())) {
//						ingored = true;
//						break;
//					}
//				}
//				System.out.println(line);
//				if (ingored)
//					continue;
//				if (line.equals("财经大事")) {
//					isEvent = true;
//					continue;
//				}
//				if (line.equals("国债发行预告"))
//					break;
//				if (!isEvent)
//					continue;
//
//				if ((newDataLineCount) % 5 == 0) {
//					economicEvent = new EconomicNationalDebt();
//					economicEvent.setOccurDate(date.replace("-", ""));
//					economicEvent.setOccurTime(line.substring(5).replace(":", ""));
//				} else if ((newDataLineCount) % 5 == 1) {
//					economicEvent.setCountry(line);
//				} else if ((newDataLineCount) % 5 == 2) {
//					economicEvent.setSite(line);
//				} else if ((newDataLineCount) % 5 == 3) {
//					// 有可能没有重要性这一列
//					if (importance.indexOf(line) >= 0) {
//						economicEvent.setImportance(line);
//					} else {
//						economicEvent.setEvent(line);
//						Object[] params = new Object[] { economicEvent.getOccurDate(), economicEvent.getOccurTime(), economicEvent.getCountry(), economicEvent.getSite(), economicEvent.getEvent() };
//						if (!UtilSql.isExist(this.con, sqlGetEconomicEvent, params)) {
//							this.psInsert.setString(1, economicEvent.getOccurDate());
//							this.psInsert.setString(2, economicEvent.getOccurTime());
//							this.psInsert.setString(3, economicEvent.getCountry());
//							this.psInsert.setString(4, economicEvent.getSite());
//							this.psInsert.setString(5, economicEvent.getImportance());
//							this.psInsert.setString(6, economicEvent.getEvent());
//							this.psInsert.addBatch();
//						}
//						newDataLineCount = 0;
//						continue;
//					}
//				} else if ((newDataLineCount) % 5 == 4) {
//					economicEvent.setEvent(line);
//					Object[] params = new Object[] { economicEvent.getOccurDate(), economicEvent.getOccurTime(), economicEvent.getCountry(), economicEvent.getSite(), economicEvent.getEvent() };
//					if (!UtilSql.isExist(this.con, sqlGetEconomicEvent, params)) {
//						this.psInsert.setString(1, economicEvent.getOccurDate());
//						this.psInsert.setString(2, economicEvent.getOccurTime());
//						this.psInsert.setString(3, economicEvent.getCountry());
//						this.psInsert.setString(4, economicEvent.getSite());
//						this.psInsert.setString(5, economicEvent.getImportance());
//						this.psInsert.setString(6, economicEvent.getEvent());
//						this.psInsert.addBatch();
//					}
//				}
//				newDataLineCount += 1;
//			}
//		}
//	}
//
//	// 获取汇通国债发行预告
//	private void getEconomicNationalDebt(String url, String date) throws Exception {
//		boolean isNationalDebt = false;// 由于财经事件与假期使用了同样的class,故在此要分段截取
//		String[] arrExceptStr = new String[] { "&nbsp;" };
//		String[] arrIngoreStr = new String[] { "时间", "国家", "地点", "事件", "重要性", "[]" };
//		String importance = "低中高";
//		boolean ingored = false;
//		Parser parser = new Parser(url);
//		parser.setEncoding("GB2312");
//
//		NodeFilter[] arrNodeFilter = new NodeFilter[4];
//		arrNodeFilter[0] = new HasAttributeFilter("class", "w_60");
//		arrNodeFilter[1] = new HasAttributeFilter("class", "w_65");
//		arrNodeFilter[2] = new HasAttributeFilter("class", "w_525 text_l");
//		arrNodeFilter[3] = new HasAttributeFilter("class", "title");
//
//		OrFilter orFilter = new OrFilter(arrNodeFilter);
//		NodeList nodeList = parser.parse(orFilter);
//		Node[] nodes = nodeList.toNodeArray();
//		String line = null;
//		// 每次换时间时出现新一批数据
//		EconomicNationalDebt economicNationalDebt = new EconomicNationalDebt();
//		int newDataLineCount = 0;
//		for (int i = 0; i < nodes.length; i++) {
//			line = nodes[i].toPlainTextString();
//			if (line != null && !"".equals(line.trim())) {
//				// 过滤掉不需要的字符
//				for (String exceptStr : arrExceptStr) {
//					line = line.replace(exceptStr, "");
//				}
//				for (String ingoreStr : arrIngoreStr) {
//					ingored = false;
//					if (ingoreStr.equals(line.trim())) {
//						ingored = true;
//						break;
//					}
//				}
//				System.out.println(line);
//				if (ingored)
//					continue;
//				if (line.equals("国债发行预告")) {
//					isNationalDebt = true;
//					continue;
//				}
//				if (!isNationalDebt)
//					continue;
//
//				if ((newDataLineCount) % 5 == 0) {
//					economicNationalDebt = new EconomicNationalDebt();
//					economicNationalDebt.setOccurDate(date.replace("-", ""));
//					economicNationalDebt.setOccurTime(line.substring(5).replace(":", ""));
//				} else if ((newDataLineCount) % 5 == 1) {
//					economicNationalDebt.setCountry(line);
//				} else if ((newDataLineCount) % 5 == 2) {
//					economicNationalDebt.setSite(line);
//				} else if ((newDataLineCount) % 5 == 3) {
//					// 有可能没有重要性这一列
//					if (importance.indexOf(line) >= 0) {
//						economicNationalDebt.setImportance(line);
//					} else {
//						economicNationalDebt.setEvent(line);
//						Object[] params = new Object[] { economicNationalDebt.getOccurDate(), economicNationalDebt.getOccurTime(), economicNationalDebt.getCountry(), economicNationalDebt.getSite(),
//								economicNationalDebt.getEvent() };
//						if (!UtilSql.isExist(this.con, sqlGetEconomicNationalDebt, params)) {
//							this.psInsert.setString(1, economicNationalDebt.getOccurDate());
//							this.psInsert.setString(2, economicNationalDebt.getOccurTime());
//							this.psInsert.setString(3, economicNationalDebt.getCountry());
//							this.psInsert.setString(4, economicNationalDebt.getSite());
//							this.psInsert.setString(5, economicNationalDebt.getImportance());
//							this.psInsert.setString(6, economicNationalDebt.getEvent());
//							this.psInsert.addBatch();
//						}
//						newDataLineCount = 0;
//						continue;
//					}
//				} else if ((newDataLineCount) % 5 == 4) {
//					economicNationalDebt.setEvent(line);
//					Object[] params = new Object[] { economicNationalDebt.getOccurDate(), economicNationalDebt.getOccurTime(), economicNationalDebt.getCountry(), economicNationalDebt.getSite(),
//							economicNationalDebt.getEvent() };
//					if (!UtilSql.isExist(this.con, sqlGetEconomicNationalDebt, params)) {
//						this.psInsert.setString(1, economicNationalDebt.getOccurDate());
//						this.psInsert.setString(2, economicNationalDebt.getOccurTime());
//						this.psInsert.setString(3, economicNationalDebt.getCountry());
//						this.psInsert.setString(4, economicNationalDebt.getSite());
//						this.psInsert.setString(5, economicNationalDebt.getImportance());
//						this.psInsert.setString(6, economicNationalDebt.getEvent());
//						this.psInsert.addBatch();
//					}
//				}
//				newDataLineCount += 1;
//			}
//		}
//	}
//
//	// 获取汇通假期预告
//	private void getEconomicHoliday(String url, String date) throws Exception {
//		boolean isHoliday = false;// 由于财经事件与假期使用了同样的class,故在此要分段截取
//		String[] arrExceptStr = new String[] { "&nbsp;" };
//		String[] arrIngoreStr = new String[] { "时间", "国家", "地点", "事件", "重要性", "[]" };
//		boolean ingored = false;
//		Parser parser = new Parser(url);
//		parser.setEncoding("GB2312");
//
//		NodeFilter[] arrNodeFilter = new NodeFilter[4];
//		arrNodeFilter[0] = new HasAttributeFilter("class", "w_60");
//		arrNodeFilter[1] = new HasAttributeFilter("class", "w_65");
//		arrNodeFilter[2] = new HasAttributeFilter("class", "w_525 text_l");
//		arrNodeFilter[3] = new HasAttributeFilter("class", "title");
//
//		OrFilter orFilter = new OrFilter(arrNodeFilter);
//		NodeList nodeList = parser.parse(orFilter);
//		Node[] nodes = nodeList.toNodeArray();
//		String line = null;
//		// 每次换时间时出现新一批数据
//		EconomicHoliday economicHoliday = new EconomicHoliday();
//		List<EconomicHoliday> listEconomicHoliday = new ArrayList<EconomicHoliday>();
//		int newDataLineCount = 0;
//		for (int i = 0; i < nodes.length; i++) {
//			line = nodes[i].toPlainTextString();
//			if (line != null && !"".equals(line.trim())) {
//				// 过滤掉不需要的字符
//				for (String exceptStr : arrExceptStr) {
//					line = line.replace(exceptStr, "");
//				}
//				for (String ingoreStr : arrIngoreStr) {
//					ingored = false;
//					if (ingoreStr.equals(line.trim())) {
//						ingored = true;
//						break;
//					}
//				}
//				if (ingored)
//					continue;
//				System.out.println(line);
//				if (line.equals("假期预告")) {
//					isHoliday = true;
//					continue;
//				}
//				if (!isHoliday)
//					continue;
//				if (line.equals("财经大事")) {
//					break;
//				}
//
//				if ((newDataLineCount) % 4 == 0) {
//					economicHoliday = new EconomicHoliday();
//					economicHoliday.setOccurDate(date.replace("-", ""));
//					economicHoliday.setOccurTime(line.substring(5).replace(":", ""));
//				} else if ((newDataLineCount) % 4 == 1) {
//					economicHoliday.setCountry(line);
//				} else if ((newDataLineCount) % 4 == 2) {
//					economicHoliday.setSite(line);
//				} else if ((newDataLineCount) % 4 == 3) {
//					economicHoliday.setEvent(line);
//					Object[] params = new Object[] { economicHoliday.getOccurDate(), economicHoliday.getOccurTime(), economicHoliday.getCountry(), economicHoliday.getSite(), economicHoliday.getEvent() };
//					if (!UtilSql.isExist(this.con, sqlGetEconomicHoliday, params)) {
//						this.psInsert.setString(1, economicHoliday.getOccurDate());
//						this.psInsert.setString(2, economicHoliday.getOccurTime());
//						this.psInsert.setString(3, economicHoliday.getCountry());
//						this.psInsert.setString(4, economicHoliday.getSite());
//						this.psInsert.setString(5, economicHoliday.getEvent());
//						this.psInsert.addBatch();
//					}
//				}
//				newDataLineCount += 1;
//			}
//		}
//		System.out.println(UtilJson.objToJson(listEconomicHoliday));
//	}
//
//	// 备份html文件
//	private void getHtmlFile(Bean bean) throws IOException {
//		String html;
//		html = getHTML(bean.getUrl(), "GB2312");
//		UtilFile.writeFile(bean.getSaveFilePath() + "/" + bean.getUrl().substring(bean.getUrl().lastIndexOf("/") + 1), html);
//	}
//
//	// 下载html内容
//	private String getHTML(String pageURL, String encoding) throws IOException {
//		StringBuilder pageHTML = new StringBuilder();
//		URL url = new URL(pageURL);
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestProperty("User-Agent", "MSIE 7.0");
//		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
//		String line = null;
//		while ((line = br.readLine()) != null) {
//			pageHTML.append(line);
//			pageHTML.append("\r\n");
//		}
//		connection.disconnect();
//		return pageHTML.toString();
//	}
//
//	// 获取该指标是否匹配过 0表示未曾匹配，1表示已经匹配过
//	private int getMatch(String key) {
//		if (mapMatch == null) {
//			mapMatch = new HashMap<String, Integer>();
//			return 0;
//		} else if (mapMatch.get(key) == null) {
//			return 0;
//		} else
//			return mapMatch.get(key);
//	}
//
//	// 匹配当天导入数据的国家与指标
//	private void math(String date, int source) throws Exception {
//		String key = source + "|" + date;
//		// 已经匹配过的日期则不再匹配
//		if (getMatch(key) == 1) {
//			return;
//		}
//		String sql = "select a.id,a.indicator  from economic_data  a where  indicatorid is null and source=" + source + " and a.publishdate='" + date + "'";
//		List<HashMap<String, String>> list = UtilSql.queryM(con, sql);
//		if (list == null)
//			return;
//		for (HashMap<String, String> map : list) {
//			match(map.get("ID"), map.get("INDICATOR"), source);
//		}
//		mapMatch.put(key, 1);
//	}
//
//	// 根据economic_data中的指标名称匹配economic_indicator里的指标id和国家
//	private void match(String economic_data_id, String economic_data_indicator, int source) throws Exception {
//		if (source == -1)
//			return;
//		List<HashMap<String, Object>> list = getCountryAndIndicatorId(economic_data_indicator, source);
//		Map<String, Object> map = null;
//		if (list == null || list.size() == 0) {
//			return;
//		} else if (list.size() > 1) {
//			// ShowMsg.showWarn("出现重复匹配指标，请查证：source[" + source + "];indicator["
//			// + economic_data_indicator+"]");
//			System.out.println("出现重复匹配指标，请查证：source[" + source + "];indicator[" + economic_data_indicator + "]");
//			// 当出现重复匹配时，取字数最多的优先匹配
//			int len = 0;
//			for (int i = 0; i < list.size(); i++) {
//				if (UtilString.isNil(list.get(i).get("INDICATOR")).length() > len) {
//					map = list.get(i);
//					len = UtilString.isNil(list.get(i).get("INDICATOR")).length();
//				}
//			}
//		} else if (list.size() == 1) {
//			map = list.get(0);
//		}
//		if (map != null) {
//			String indicatorId = map.get("ID").toString();
//			String country = map.get("COUNTRY").toString();
//			if (!"".equals(indicatorId)) {
//				String sql = "update economic_data set indicatorId='" + indicatorId + "' , country='" + country + "' where id='" + economic_data_id + "'";
//				System.out.println(sql);
//				UtilSql.executeUpdate(con, sql, new Object[] {});
//			}
//		}
//	}
//
//	/**
//	 * @Description:获取匹配的指标与国家
//	 * @param economic_data_indicator
//	 * @param source
//	 * @return List<HashMap<String,Object>>
//	 * @date Mar 27, 2014
//	 * @author:fgq
//	 */
//	private List<HashMap<String, Object>> getCountryAndIndicatorId(String economic_data_indicator, int source) {
//		String sql = "";
//		if (countryArr == null)
//			getCountryArr();
//		String country = UtilFun.getCountry(economic_data_indicator, countryArr);
//		if (source == 0) {// 汇通
//			sql = "select id,country,indicatorHt as indicator from economic_indicator where instr('" + economic_data_indicator + "',trim(indicatorHt))>0";
//		} else if (source == 1) {// 福汇
//			sql = "select id,country,indicatorFh as indicator from economic_indicator where instr('" + economic_data_indicator + "',trim(indicatorFh))>0";
//		}
//		if (!"".equals(country)) {
//			sql += "  and country='" + country + "'";
//		}
//		System.out.println(sql);
//		return UtilSql.QueryM(con, sql);
//	}
//
//	/**
//	 * @Description:获取国家
//	 * @date Mar 27, 2014
//	 * @author:fgq
//	 */
//	public void getCountryArr() {
//		String sql = "select name,value  from config_dictionary  where groupname='COUNTRY' and state=0 order by ord";
//		List<HashMap<String, Object>> list = UtilSql.QueryM(con, sql);
//		countryArr = new String[list.size()];
//		for (int i = 0; i < list.size(); i++) {
//			countryArr[i] = list.get(i).get("VALUE").toString();
//		}
//	}
//
//	/**
//	 * @Description:去掉指标值中的非数字字符
//	 * @param indicatorValue
//	 * @return double
//	 * @date Mar 27, 2014
//	 * @author:fgq
//	 */
//	private Double getNumericValue(String indicatorValue) {
//		double rs = 0;
//		indicatorValue=UtilString.isNil(indicatorValue);
//		for (String unit : Const.UnitIndicator) {
//			indicatorValue = indicatorValue.replace(unit, "");
//		}
//		try {
//			rs = Double.valueOf(indicatorValue);
//		} catch (Exception e) {
//			return null;
//		}
//		return rs;
//	}
//
//	/**
//	 * @Description:获取公布值与预测值、前值的比较结果
//	 * @param publishedValue
//	 * @param predictedValue
//	 * @param previousValue
//	 * @return String
//	 * @date Mar 28, 2014
//	 * @author:fgq
//	 */
//	private String getcompareValue(String publishedValue, String predictedValue, String previousValue) {
//		StringBuilder rs = new StringBuilder();
//		Double publishedVal = getNumericValue(publishedValue);
//		Double predictedVal = getNumericValue(predictedValue);
//		Double previousVal = getNumericValue(previousValue);
//		if (publishedVal == null)
//			return "";
//		if (predictedVal != null) {
//			if (publishedVal > predictedVal) {
//				rs.append("大于预测值;");
//			} else if ((double) publishedVal < (double) predictedVal) {
//				rs.append("小于预测值;");
//			} else if (((double) publishedVal) == ((double) predictedVal)) {
//				rs.append("等于预测值;");
//			}
//		}
//		if (previousVal != null) {
//			if ((double) publishedVal > (double) previousVal) {
//				rs.append("大于前值");
//			} else if ((double) publishedVal < (double) previousVal) {
//				rs.append("小于前值");
//			} else if ((double) publishedVal == (double) previousVal) {
//				rs.append("等于前值");
//			}
//		}
//		return rs.toString();
//	}
//
//	/**
//	 * @Description:获取预测值、前值的比较结果
//	 * @param predictedValue
//	 * @param previousValue
//	 * @return String
//	 * @date 2014-8-31
//	 * @author:fgq
//	 */
//	private String getcompareValue(String predictedValue, String previousValue) {
//		StringBuilder rs = new StringBuilder();
//		Double predictedVal = getNumericValue(predictedValue);
//		Double previousVal = getNumericValue(previousValue);
//		if (predictedVal == null)
//			return "";
//		if (previousVal == null)
//			return "";
//		if (predictedVal > previousVal) {
//			rs.append("预测值大于前值");
//		} else if (predictedVal < previousVal) {
//			rs.append("预测值小于前值");
//		} else {
//			rs.append("预测值等于前值");
//		}
//		return rs.toString();
//	}
//
//	// 判断是否为yyyy-MM-dd格式
//	private static boolean isDate(String arg) {
//		boolean rs = false;
//		try {
//			if (arg.indexOf("-") < 0)
//				return false;
//			UtilConver.strToDate(arg, "yyyy-MM-dd");
//			rs = true;
//		} catch (Exception e) {
//		}
//		return rs;
//	}
//
//	// 通过url获取实时福汇经济数据（在导入当天数据时使用，用于实时分析）
//	private void getEconomicDataFhRealTime(String url) throws ParserException, SQLException {
//		// String url="http://www.dailyfx.com.hk/calendar/index.html";
//		// String url = "X:/Projects/dailyfx.com/2014-03-12.htm";
//		String[] arrExceptStr = new String[] { "&nbsp;", "\t", "\r" };
//		Parser parser = new Parser(url);
//		parser.setEncoding("UTF-8");
//		NodeFilter[] nodeFilter = new NodeFilter[2];
//		nodeFilter[0] = new TagNameFilter("div");
//		nodeFilter[1] = new HasAttributeFilter("id", "inside-calendar");
//		AndFilter andFilter = new AndFilter(nodeFilter);
//		NodeList nodeList = parser.parse(andFilter);
//		Node[] nodes = nodeList.toNodeArray();
//		String line = null;
//		int newDataLineCount = 0;
//		EconomicData economicData = null;
//		for (int i = 0; i < nodes.length; i++) {
//			line = nodes[i].toPlainTextString();
//			if (line != null && !"".equals(line.trim())) {
//				for (String exceptStr : arrExceptStr) {
//					line = line.replace(exceptStr, "");
//				}
//				System.out.println(line);
//				String[] lineArr = line.split("\n");
//				for (String item : lineArr) {
//					if (newDataLineCount == 0 && "".equals(UtilString.isNil(item)))
//						continue;
//					// 判断是否以yyyy-MM-dd日期开头
//					if (isDate(item)) {
//						newDataLineCount = 0;
//						economicData = new EconomicData(1);
//						economicData.setPublishDate(item.replace("-", ""));
//					} else {
//						newDataLineCount += 1;
//						if (newDataLineCount % 7 == 1) {
//							if (economicData == null)
//								continue;
//							String time = item.replace(":", "");
//							if (UtilDate.isTime(item)) {
//								economicData.setPublishTime(time);
//							} else
//								economicData.setPublishTime("");
//						} else if (newDataLineCount % 7 == 2) {
//							if (economicData == null)
//								continue;
//							String sline = item;
//							for (int index = 0; index < Const.EscapeWord.length; index++) {
//								sline = sline.replace(Const.EscapeWord[index][0], Const.EscapeWord[index][1]);
//							}
//							economicData.setIndicator(sline.toUpperCase());
//						} else if (newDataLineCount % 7 == 3) {
//							if (economicData == null)
//								continue;
//							economicData.setImportance(item);
//						} else if (newDataLineCount % 7 == 4) {
//							if (economicData == null)
//								continue;
//							economicData.setPreviousValue(item);
//						} else if (newDataLineCount % 7 == 5) {
//							if (economicData == null)
//								continue;
//							economicData.setPredictedValue(item);
//						} else if (newDataLineCount % 7 == 6) {
//							if (economicData == null)
//								continue;
//							// 记录未发布的时间
//							if ("侦查中".equals(item.trim())) {
//								if (!"".equals(UtilString.isNil(economicData.getPublishTime()))) {
//									listUnPublisTimeFH.add(economicData.getPublishTime());
//								}
//							}
//							economicData.setPublishedValue(item);
//							String key = economicData.getIndicator() + economicData.getPublishTime() + economicData.getSource();
//							EconomicData tmpEconomicData = mapEconomicDataFH.get(key);
//							if (tmpEconomicData != null) {
//								if (!economicData.getPublishedValue().equals(tmpEconomicData.getPublishedValue())) {
//									System.out.println(economicData.getPublishedValue() + "  " + tmpEconomicData.getPublishedValue());
//									this.psUpdate.setString(1, economicData.getPublishedValue());
//									this.psUpdate.setString(2, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
//									this.psUpdate.setString(3, economicData.getPublishDate());
//									this.psUpdate.setString(4, economicData.getIndicator());
//									this.psUpdate.setInt(5, economicData.getSource());
//									this.psUpdate.addBatch();
//									mapEconomicDataFH.put(key, economicData);
//								}
//							} else {
//								System.out.println(economicData.getPublishTime());
//								this.psInsert.setString(1, economicData.getPublishDate());
//								this.psInsert.setString(2, economicData.getPublishTime());
//								this.psInsert.setString(3, economicData.getCountry());
//								this.psInsert.setString(4, economicData.getIndicator());
//								this.psInsert.setString(5, economicData.getImportance());
//								this.psInsert.setString(6, economicData.getPreviousValue());
//								this.psInsert.setString(7, economicData.getPredictedValue());
//								this.psInsert.setString(8, economicData.getPublishedValue());
//								this.psInsert.setInt(9, economicData.getSource());
//								this.psInsert.setString(10, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
//								this.psInsert.setString(11, this.getcompareValue(economicData.getPredictedValue(), economicData.getPreviousValue()));
//								this.psInsert.addBatch();
//								mapEconomicDataFH.put(key, economicData);
//							}
//						}
//					}
//				}
//			}
//		}
//		if (listUnPublisTimeFH.size() == 0)
//			hasUnPublishIndicatorFH = false;
//	}
//
//	public static void main(String[] args) throws ParserException, SQLException {
//		String url = "http://www.dailyfx.com.hk/calendar/index.html";
//		new Task().getEconomicDataFhRealTime(url);
//	}
//}
