package com.task.Zhjr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.task.ConstTask;
import com.task.Gdgov.IssueNews;
import com.taskInterface.TaskAbstract;

import common.util.UtilWeb;
import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJackSon;
import common.util.json.UtilJson;
import consts.Const;
import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

/**
 * @Description:珠海金融工作局
 * @date Aut 1,o19
 * @author:fgq
 */
public class Task extends TaskAbstract {
	private Connection con;
	private PreparedStatement psInsert;
	private String sqlNews = "insert into IssueNews(mark,editor,year,coverUrl,createDate,"
			+ "title,subTitle,originalTitle,type,subType," 
			+ "source,region,summary,content,memo,"
			+ "newsTime,modifyTime,createTime,ord,createUserId,"
			+ "hasFile,files)" 
			+ "values(?,?,?,?,?,"
			+ "?,?,?,?,?," 
			+ "?,?,?,?,?,"
			+ "?,?,?,?,?,"
			+ "?,?) ";

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
			con = UtilJDBCManager.getConnection(dbconn);
			int cntNews = this.addNews(this.getNews());
			this.setTaskStatus("执行成功");
			this.setTaskMsg("珠海要文[" + cntNews + "]\n");
		} catch (Exception e) {
			System.out.println(pulishDate);
			this.setTaskStatus("执行失败");
			this.setTaskMsg("执行错误[" + pulishDate + "]:", e);
		} finally {
			UtilSql.close(this.con, this.psInsert);
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

	private int addNews(List<IssueNews> newsList) throws Exception {
		this.psInsert = con.prepareStatement(sqlNews);
		boolean has = false;
		for (IssueNews news : newsList) {
			has = true;
			if(isExits(news))continue;
			this.psInsert.setString(1, news.getMark());
			this.psInsert.setString(2, news.getEditor());
			this.psInsert.setString(3, news.getYear());
			this.psInsert.setString(4, news.getCoverUrl());
			this.psInsert.setString(5, UtilConver.dateToStr(Const.fm_yyyy_MM_dd));// createDate

			this.psInsert.setString(6, news.getTitle());
			this.psInsert.setString(7, news.getSubTitle());
			this.psInsert.setString(8, news.getOriginalTitle());
			this.psInsert.setString(9, news.getType());
			this.psInsert.setString(10, news.getSubType());

			this.psInsert.setString(11, news.getSource());
			this.psInsert.setString(12, news.getRegion());
			this.psInsert.setString(13, news.getSummary());
			this.psInsert.setString(14, news.getContent());
			this.psInsert.setString(15, news.getMemo());

			this.psInsert.setString(16, news.getNewsTime());
			this.psInsert.setObject(17, new Date());// modifyTime
			this.psInsert.setObject(18, new Date());// createTime
			this.psInsert.setString(19, "0");// ord
			this.psInsert.setString(20, "0");// createUserId
			this.psInsert.setBoolean(21, news.getHasFile());// hasFile
			this.psInsert.setString(22, news.getFiles());// files
			this.psInsert.addBatch();
		}
		if (has) {
			this.psInsert.executeBatch();
			this.psInsert.clearBatch();
		}
		return newsList.size();
	}

	private boolean isExits(IssueNews news) throws Exception {
		String sql = "select count(1) from IssueNews where title='" + news.getTitle() + "' and newsTime='"
				+ news.getNewsTime() + "'";
		return UtilSql.isExist(con, sql, new Object[] {});
	}

	/**
	 * 1.中国珠海网-珠海要闻-日常调度
	 **/
	private List<IssueNews> getNews() throws Exception {
		List<IssueNews> listNews = new ArrayList<IssueNews>();
		String url = "http://www.zhjr.gov.cn/xxgk/zcfg/";
		Document doc = UtilWeb.getDoc(url);
		Element elTable = doc.getElementsByClass("Border_gray").first().getElementsByTag("table").first()
				.getElementsByTag("tbody").first().select("tr:eq(1)").select("td:eq(1)").first().getElementsByTag("table").first();
		Elements elTr=elTable.getElementsByTag("tr");
		for (Element tr : elTr) {
			if(tr.getElementsByTag("td").size()<3)continue;
			IssueNews news=new IssueNews();
			news.setType("项目申报");
			news.setSubType("政策法规");
			news.setSource("珠海市金融工作局");
			news.setRegion("珠海");
			news.setHasFile(false);
			Element aTitle=tr.select("td:eq(1)").first().getElementsByTag("a").first();
			news.setTitle(aTitle.attr("title"));
			String contentUrl="http://www.zhjr.gov.cn/xxgk/zcfg"+aTitle.attr("href").substring(1);
			news.setMemo(contentUrl);
			news.setNewsTime(tr.getElementsByClass("date").first().html());
			news.setYear(news.getNewsTime().substring(0,4));
			Thread.sleep(ConstTask.SleepTime);
			Element elDoc = UtilWeb.getDoc(contentUrl);
			Element elContent=elDoc.getElementsByClass("TRS_Editor").first();
			if (elContent != null) {
				news.setContent(elContent.html());
				try {
					Elements elFiles=elDoc.getElementsByClass("Border_gray").first().getElementsByClass("a5");
					if(elFiles.size()>0) {
						news.setHasFile(true);
						List<String> listFiles= new ArrayList<String>();
						for(Element elA:elFiles) {
							listFiles.add(contentUrl.substring(0,contentUrl.lastIndexOf("/"))+elA.attr("href").substring(1)+"?attname="+elA.html());
						}
						news.setFiles(UtilJackSon.toJson(listFiles));
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(UtilJackSon.toJson(news));
			listNews.add(news);
			Thread.sleep(ConstTask.SleepTime);
		}
		return listNews;
	}

	public static void main(String[] args) throws Exception {
		List<IssueNews> listNews = new ArrayList<IssueNews>();
		String url = "http://www.zhjr.gov.cn/xxgk/zcfg/";
		Document doc = UtilWeb.getDoc(url);
		Element elTable = doc.getElementsByClass("Border_gray").first().getElementsByTag("table").first()
				.getElementsByTag("tbody").first().select("tr:eq(1)").select("td:eq(1)").first().getElementsByTag("table").first();
		Elements elTr=elTable.getElementsByTag("tr");
		for (Element tr : elTr) {
			if(tr.getElementsByTag("td").size()<3)continue;
			IssueNews news=new IssueNews();
			news.setType("项目申报");
			news.setSubType("政策法规");
			news.setSource("珠海市金融工作局");
			news.setRegion("珠海");
			news.setHasFile(false);
			Element aTitle=tr.select("td:eq(1)").first().getElementsByTag("a").first();
			news.setTitle(aTitle.attr("title"));
			String contentUrl="http://www.zhjr.gov.cn/xxgk/zcfg"+aTitle.attr("href").substring(1);
			news.setMemo(contentUrl);
			news.setNewsTime(tr.getElementsByClass("date").first().html());
			news.setYear(news.getNewsTime().substring(0,4));
			Thread.sleep(ConstTask.SleepTime);
			Element elDoc = UtilWeb.getDoc(contentUrl);
			Element elContent=elDoc.getElementsByClass("TRS_Editor").first();
			if (elContent != null) {
				news.setContent(elContent.html());
				try {
					Elements elFiles=elDoc.getElementsByClass("Border_gray").first().getElementsByClass("a5");
					if(elFiles.size()>0) {
						news.setHasFile(true);
						List<String> listFiles= new ArrayList<String>();
						for(Element elA:elFiles) {
							listFiles.add(contentUrl.substring(0,contentUrl.lastIndexOf("/"))+elA.attr("href").substring(1)+"?attname="+elA.html());
						}
						news.setFiles(UtilJackSon.toJson(listFiles));
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(UtilJackSon.toJson(news));
			listNews.add(news);
			Thread.sleep(ConstTask.SleepTime);
		}
	}
}