package com.task.Kxt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.task.Economic.entity.EconomicData;
import com.task.Economic.entity.EconomicEvent;
import com.task.Economic.entity.EconomicHoliday;
import com.taskInterface.TaskAbstract;
import common.util.UtilGuid;
import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

/**
 * @Description:
 * @date Feb 11, 2014
 * @author:fgq
 * @modify : 20140310 增加福汇财经数据导入
 */
public class Task extends TaskAbstract {
	private Connection con;
	private PreparedStatement psInsert;
	private PreparedStatement psDelete;
	// 插入经济数据记录sql
	private String sqlInsertEconomicData = "insert into economic_data(publishdate,publishtime,country,indicator,importance,previousvalue,predictedvalue,publishedvalue,source,compareresult,predictedresult,advice,INDICATORID,id)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private String sqlDeleteEconomicData = "delete from economic_data where publishDate=? ";
	// 插入财经事件记录sql
	private String sqlInsertEconomicEvent = "insert  into economic_event(occurDate,occurTime,country,site,importance,event,id)values(?,?,?,?,?,?,?)";
	private String sqlDeleteEconomicEvent = "delete from economic_event where occurDate=? ";
	// 获取假期预告记录Sql
	private String sqlInsertEconomicHoliday = "insert  into economic_holiday(occurDate,occurTime,country,site,event,id)values(?,?,?,?,?,?)";
	private String sqlDeleteEconomicHoliday = "delete from economic_holiday where occurDate=? ";
	
	public void fireTask() {
		DbConnection dbconn = null;
		String pulishDate = "";
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			dbconn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbconn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取数据库连接错误");
				return;
			}
			pulishDate = UtilConver.dateToStr(this.getDate(), Const.fm_yyyyMMdd);
			con = UtilJDBCManager.getConnection(dbconn);
			String[] html=this.getHtml(bean,pulishDate);
			this.updateEconomicData(pulishDate, html[0]);
			this.updateEconomicHoliday(pulishDate,html[1]);
			this.updateEconomicEvent(pulishDate, html[2]);
			this.setTaskStatus("执行成功");
			this.setTaskMsg( "数据采集执行成功[" + pulishDate + "]");
		} catch (Exception e) {
			System.out.println(pulishDate);
			this.setTaskStatus("执行失败");
			this.setTaskMsg("执行错误[" + pulishDate + "]:", e);
		} finally {
			UtilSql.close(this.con, this.psInsert, this.psDelete);
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}
 
	private void updateEconomicData(String date,String html) throws Exception{
		this.psDelete = this.con.prepareStatement(sqlDeleteEconomicData);
		this.psDelete.setString(1,date);
		this.psDelete.addBatch();
		this.psDelete.executeBatch();
		this.psDelete.clearBatch();
		this.psInsert = con.prepareStatement(sqlInsertEconomicData);
		boolean has=false;
		String[] rows=html.split("</tr>");
		for(int i=1;i<rows.length;i++){
			String[] cols=rows[i].split("</td>");
			if(cols.length<8)continue;
			has=true;
			EconomicData economicData=new EconomicData();
			economicData.setPublishDate(date);
			economicData.setPublishTime(cols[0].replace(":", "").trim());
			economicData.setCountry(this.getCountry(cols[1]).trim());
			economicData.setIndicator(cols[2].trim());
			economicData.setImportance(cols[3].trim());
			economicData.setPreviousValue(cols[4].trim());
			economicData.setPredictedValue(cols[5].trim());
			economicData.setPublishedValue(cols[6].trim());
			economicData.setSource(0);
			economicData.setAdvice(this.getAdvice(cols[7].trim()));
			economicData.setIndicatorId(this.getIndicatorId(economicData.getIndicator(), economicData.getCountry()));
			economicData.setId(UtilGuid.getGuid());
			this.psInsert.setString(1, economicData.getPublishDate());
			this.psInsert.setString(2, economicData.getPublishTime());
			this.psInsert.setString(3, economicData.getCountry());
			this.psInsert.setString(4, economicData.getIndicator());
			this.psInsert.setString(5, economicData.getImportance());
			this.psInsert.setString(6, economicData.getPreviousValue());
			this.psInsert.setString(7, economicData.getPredictedValue());
			this.psInsert.setString(8, economicData.getPublishedValue());
			this.psInsert.setInt(9, economicData.getSource());
			this.psInsert.setString(10, this.getcompareValue(economicData.getPublishedValue(), economicData.getPredictedValue(), economicData.getPreviousValue()));
			this.psInsert.setString(11, this.getcompareValue(economicData.getPredictedValue(), economicData.getPreviousValue()));
			this.psInsert.setString(12,economicData.getAdvice());
			this.psInsert.setString(13,economicData.getIndicatorId());
			this.psInsert.setString(14,economicData.getId());
			this.psInsert.addBatch();
//			for(int j=0;j<economicArr.length;j++){
//				System.out.print(economicArr[j].trim()+"|");
//			}
//			System.out.println();
		}
		if(has){
			this.psInsert.executeBatch();
			this.psInsert.clearBatch();
		}
	}
	
	// 获取汇通财经事件
	private void updateEconomicEvent(String date,String html) throws Exception{
		this.psDelete = this.con.prepareStatement(sqlDeleteEconomicEvent);
		this.psDelete.setString(1,date);
		this.psDelete.addBatch();
		this.psDelete.executeBatch();
		this.psDelete.clearBatch();
		this.psInsert = con.prepareStatement(sqlInsertEconomicEvent);
		String[] rows=html.split("</tr>");
		boolean has=false;
		for(int i=1;i<rows.length;i++){
			String[] cols=rows[i].split("</td>");
			if(cols.length<3)continue;
			has=true;
			EconomicEvent economicEvent=new EconomicEvent();
			economicEvent.setOccurDate(date);
			economicEvent.setOccurTime(cols[0].replace(":", "").trim());
			economicEvent.setCountry(cols[1].trim()); 
			economicEvent.setSite(cols[2].trim());
			economicEvent.setImportance(cols[3].trim());
			economicEvent.setEvent(cols[4].trim());
			economicEvent.setId(UtilGuid.getGuid());
			this.psInsert.setString(1, economicEvent.getOccurDate());
			this.psInsert.setString(2, economicEvent.getOccurTime());
			this.psInsert.setString(3, economicEvent.getCountry());
			this.psInsert.setString(4, economicEvent.getSite());
			this.psInsert.setString(5, economicEvent.getImportance());
			this.psInsert.setString(6, economicEvent.getEvent());
			this.psInsert.setString(7, economicEvent.getId());
			this.psInsert.addBatch();
//			for(int j=0;j<cols.length;j++){
//				System.out.print(cols[j].trim()+"|");
//			}
//			System.out.println();
		}
		if(has){
			this.psInsert.executeBatch();
			this.psInsert.clearBatch();
		}
	}
 
	// 获取汇通假期预告
	private void updateEconomicHoliday(String date,String html) throws Exception {
		this.psDelete = this.con.prepareStatement(sqlDeleteEconomicHoliday);
		this.psDelete.setString(1,date);
		this.psDelete.addBatch();
		this.psDelete.executeBatch();
		this.psDelete.clearBatch();
		this.psInsert = con.prepareStatement(sqlInsertEconomicHoliday);
		String[] rows=html.split("</tr>");
		boolean has=false;
		for(int i=1;i<rows.length;i++){
			String[] cols=rows[i].split("</td>");
			if(cols.length<3)continue;
			has=true;
			EconomicHoliday economicHoliday=new EconomicHoliday();
			economicHoliday.setOccurDate(date);
			economicHoliday.setOccurTime(cols[0].replace(":", "").trim());
			economicHoliday.setCountry(cols[1].trim());
			economicHoliday.setSite(cols[2].trim());
			economicHoliday.setEvent(cols[3].trim());
			economicHoliday.setId(UtilGuid.getGuid());
			this.psInsert.setString(1, economicHoliday.getOccurDate());
			this.psInsert.setString(2, economicHoliday.getOccurTime());
			this.psInsert.setString(3, economicHoliday.getCountry());
			this.psInsert.setString(4, economicHoliday.getSite());
			this.psInsert.setString(5, economicHoliday.getEvent());
			this.psInsert.setString(6, economicHoliday.getId());
			this.psInsert.addBatch();
//			for(int j=0;j<cols.length;j++){
//				System.out.print(cols[j].trim()+"|");
//			}
//			System.out.println();
		}
		if(has){
			this.psInsert.executeBatch();
			this.psInsert.clearBatch();
		}
		
	} 

	
	/**
	 * pulishDate:HH-MM-DD
	 * @throws IOException 
	 */
	private String[] getHtml(Bean bean,String pulishDate) throws Exception{
		String date=UtilConver.dateToStr(this.getDate(), Const.fm_yyyy_MM_dd);
		String url="http://www.kxt.com/cjrl/ajax?date="+date;
		org.jsoup.Connection conn =  Jsoup.connect(url).timeout(20000).ignoreContentType(true);
		conn.header("Accept", "application/json, text/javascript, */*; q=0.01");
		conn.header("Accept-Encoding", "gzip, deflate, sdch");
		conn.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		conn.header("Cache-Control", "no-cache");
		conn.header("Connection", "keep-alive");
		conn.header("Cookie", bean.getCookie().replace("$$$", "%"));
		conn.header("Host", "www.kxt.com");
		conn.header("Referer", "http://www.kxt.com/rili");
		conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
		conn.header("X-Requested-With", "XMLHttpRequest");
		conn.header("Pragma", "no-cache");
		Document doc = conn.get();

//		System.out.println(doc.html());
		String html=doc.html().replace("&gt;", ">").replace("&lt;", "<").replace("&quot;", "\"").replace(" </tr>", "</tr><tr>");
		html=UtilConver.decodeUnicode(html);
		
		int  indexEconomic=html.indexOf("cjDataHtml");
		int  indexHoliday=html.indexOf("jiaqiHtml");
		int  indexEvent=html.indexOf("dashiHtml");
		int  indexDate=html.indexOf("dateHtml");
		String[] rs=new String[3];
		String htmlEconomic=html.substring(indexEconomic,indexHoliday).replace("\"\"", "\"").replace("\r", "").replace("\n", "");
		String htmlHoliday=html.substring(indexHoliday,indexEvent).replace("\"\"", "\"").replace("\r", "").replace("\n", "");
		String htmlEvent=html.substring(indexEvent,indexDate).replace("\"\"", "\"").replace("\r", "").replace("\n", "");
		rs[0]=htmlEconomic;
		rs[1]=htmlHoliday;
		rs[2]=htmlEvent;
		return rs;
	}


	private String getAdvice(String html){
		String rs="";
		Document doc=Jsoup.parse(html);
		Elements elements=doc.select("span");
		for(Element ele:elements){
			if("".equals(rs)){
			rs+=ele.html();
			}else{
				rs=rs+","+ele.html();
			}
		}
		return rs;
	}
	private String getCountry(String html){
		String rs="";
		Document doc=Jsoup.parse(html);
		Element element = doc.select("img").first();
		rs=element.attr("alt");
		return rs;
	}

	/**
	 * @Description:去掉指标值中的非数字字符
	 * @param indicatorValue
	 * @return double
	 * @date Mar 27, 2014
	 * @author:fgq
	 */
	private Double getNumericValue(String indicatorValue) {
		double rs = 0;
		indicatorValue=UtilString.isNil(indicatorValue);
		for (String unit : Const.UnitIndicator) {
			indicatorValue = indicatorValue.replace(unit, "");
		}
		try {
			rs = Double.valueOf(indicatorValue);
		} catch (Exception e) {
			return null;
		}
		return rs;
	}

	/**
	 * @Description:获取公布值与预测值、前值的比较结果
	 * @param publishedValue
	 * @param predictedValue
	 * @param previousValue
	 * @return String
	 * @date Mar 28, 2014
	 * @author:fgq
	 */
	private String getcompareValue(String publishedValue, String predictedValue, String previousValue) {
		StringBuilder rs = new StringBuilder();
		Double publishedVal = getNumericValue(publishedValue);
		Double predictedVal = getNumericValue(predictedValue);
		Double previousVal = getNumericValue(previousValue);
		if (publishedVal == null)
			return "";
		if (predictedVal != null) {
			if (publishedVal > predictedVal) {
				rs.append("大于预测值;");
			} else if ((double) publishedVal < (double) predictedVal) {
				rs.append("小于预测值;");
			} else if (((double) publishedVal) == ((double) predictedVal)) {
				rs.append("等于预测值;");
			}
		}
		if (previousVal != null) {
			if ((double) publishedVal > (double) previousVal) {
				rs.append("大于前值");
			} else if ((double) publishedVal < (double) previousVal) {
				rs.append("小于前值");
			} else if ((double) publishedVal == (double) previousVal) {
				rs.append("等于前值");
			}
		}
		return rs.toString();
	}

	/**
	 * @Description:获取预测值、前值的比较结果
	 * @param predictedValue
	 * @param previousValue
	 * @return String
	 * @date 2014-8-31
	 * @author:fgq
	 */
	private String getcompareValue(String predictedValue, String previousValue) {
		StringBuilder rs = new StringBuilder();
		Double predictedVal = getNumericValue(predictedValue);
		Double previousVal = getNumericValue(previousValue);
		if (predictedVal == null)
			return "";
		if (previousVal == null)
			return "";
		if (predictedVal > previousVal) {
			rs.append("预测值大于前值");
		} else if (predictedVal < previousVal) {
			rs.append("预测值小于前值");
		} else {
			rs.append("预测值等于前值");
		}
		return rs.toString();
	}
 
	/**
	 * @Description:获取匹配的指标与国家
	 * @param economic_data_indicator
	 * @param source
	 * @return List<HashMap<String,Object>>
	 * @date Mar 27, 2014
	 * @author:fgq
	 */
	private String  getIndicatorId(String economic_data_indicator,String country) {
		String rs="";
		String sql = "select id from economic_indicator where instr('" + economic_data_indicator + "',trim(indicatorFh))>0   and country='" + country + "'";
//		System.out.println(sql);
		List<HashMap<String, Object>> list= UtilSql.QueryM(con, sql);
		if(list==null||list.size()==0){
			System.out.println(country+"/"+economic_data_indicator+":没找到匹配指标！");
		}else {
			if(list.size()>1){
				System.out.println(country+"/"+economic_data_indicator+":出现重复匹配指标");
			}
			rs=list.get(0).get("ID").toString();
		}
		return rs; 
	}
	
	 
 
}
