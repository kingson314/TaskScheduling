package com.task.Zhgov;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.task.Gdgov.IssueNews;
import com.taskInterface.TaskAbstract;

import common.util.UtilWeb;
import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import consts.Const;
import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

/**
 * @Description:广东省工业信息
 * @date Aut 1,o19
 * @author:fgq
 */
public class Task extends TaskAbstract {
	private Connection con;
	private PreparedStatement psInsert;
	private String sqlNews = "insert into IssueNews(mark,editor,year,coverUrl,createDate,"
			+ "title,subTitle,originalTitle,type,subType," + "source,region,summary,content,memo,"
			+ "newsTime,modifyTime,createTime,ord,createUserId)" + "values(?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
			+ "?,?,?,?,?) ";

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
			int cntNews = this.addNews(this.initNews());
//			int cntNews = this.addNews(this.getNews());
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
//			if(isExits(news.getNewsTime(),news.getTitle()))continue;
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
	 * 1.中国珠海网-珠海要闻-初始化
	 **/
	private List<IssueNews> initNews() throws Exception {
		List<IssueNews> listNews = new ArrayList<IssueNews>();
		String[] urlArr = new String[] { "http://www.zhuhai.gov.cn/xw/xwzx_44483/zhyw/" };
		try {
		for (String url : urlArr) {
			String _url = "";
			for (int i = 1; i < 100; i++) {
				_url = url + "index.html";
				if (i > 1)
					_url = url + "index_" + i + ".html";
				Document doc = UtilWeb.getDoc(_url);
				Elements elNewsList = doc.getElementsByClass("list").first().getElementsByTag("li");
				for (Element elNews : elNewsList) {
					IssueNews news = new IssueNews();
					news.setSource("中国珠海网");
					news.setType("珠海要闻");
					news.setSubType("");
					Elements elSpan = elNews.getElementsByTag("span");
					news.setNewsTime(elSpan.first().html().substring(1,11));
					news.setYear(news.getNewsTime().substring(0, 4));
					Element elA = elNews.getElementsByTag("a").first();
					news.setTitle(elA.html());
					if (isExits(news))
						break;// 存在最近一条就可以退出了
//					System.out.println(news.getTitle());
					news.setRegion("珠海");
					String contentUrl ="http://www.zhuhai.gov.cn/xw/xwzx_44483/zhyw"+ elA.attr("href").substring(1);
					news.setMemo(contentUrl);
					Element elDoc = UtilWeb.getDoc(contentUrl).getElementById("new_zh").getElementsByClass("TRS_Editor").first();
					if (elDoc != null) {
						System.out.println(news.getTitle()+"  "+contentUrl);
						news.setContent(elDoc.html());
					} else {
						System.out.println(news.getTitle()+"  "+contentUrl);
						elDoc = UtilWeb.getDoc(contentUrl).getElementById("new_zh").getElementsByClass("TRS_PreAppend").first();
						news.setContent(elDoc.html());
					}
//					System.out.println(UtilJackSon.toJson(news));
					listNews.add(news);
					Thread.sleep(1000);// 隔10秒
//					System.out.println(UtilConver.dateToStr(Const.fm_yyyy_MM_dd_HH_mm_ss));
				}
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return listNews;
	}

	/**
	 * 1.中国珠海网-珠海要闻-日常调度
	 **/
	private List<IssueNews> getNews() throws Exception {
		List<IssueNews> listNews = new ArrayList<IssueNews>();
		String url = "http://www.zhuhai.gov.cn/xw/xwzx_44483/zhyw/index.html";
		Document doc = UtilWeb.getDoc(url);
		Elements elNewsList = doc.getElementsByClass("list").first().getElementsByTag("li");
		for (Element elNews : elNewsList) {
			IssueNews news = new IssueNews();
			news.setSource("中国珠海网");
			news.setType("珠海要闻");
			news.setSubType("");
			Elements elSpan = elNews.getElementsByTag("span");
			news.setNewsTime(elSpan.first().html().substring(1,11));
			news.setYear(news.getNewsTime().substring(0, 4));
			Element elA = elNews.getElementsByTag("a").first();
			news.setTitle(elA.attr("title"));
			if (isExits(news))
				break;// 存在最近一条就可以退出了
//			System.out.println(news.getTitle());
			news.setRegion("珠海");
			String contentUrl ="http://www.zhuhai.gov.cn/xw/xwzx_44483/zhyw"+ elA.attr("href").substring(1);
			news.setMemo(contentUrl);
			Element elDoc = UtilWeb.getDoc(contentUrl).getElementById("new_zh");
			if (elDoc != null) {
				news.setContent(elDoc.getElementsByClass("TRS_Editor").first().html());
			} else {
				System.out.println(news.getTitle()+"  "+contentUrl);
				news.setContent(UtilWeb.getDoc(contentUrl).getElementsByClass("main").first().html());
			}
//			System.out.println(UtilJackSon.toJson(news));
			listNews.add(news);
			Thread.sleep(1000);// 隔10秒
//			System.out.println(UtilConver.dateToStr(Const.fm_yyyy_MM_dd_HH_mm_ss));
		}
		return listNews;
	}
	 
}
