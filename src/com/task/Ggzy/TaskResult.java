package com.task.Ggzy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.task.ConstTask;
import com.taskBusiBean.info.InfoBidResult;
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
 * @Description:全国公共资源交易平台-中标结果公示
 * @date Aut 1,o19
 * @author:fgq
 */
public class TaskResult extends TaskAbstract {
	private Connection con;
	private PreparedStatement ps;
	private String sql = "insert into InfoBidResult(createDate,name,state,ord,modifyTime,"
			+ "webSite,industry, busiType,unit,source," + "province,city,region,publishTime,memo,"
			+ "contentType,contentCls,content,contentUrl)" + "values(?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
			+ "?,?,?,?) ";

	private final static String webSite = "全国公共资源交易平台";
	private final static String type = "(中标结果公示)";
	private final static String cls = "ggzy Result";

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
			this.setTaskStatus("执行成功");
			this.setTaskMsg(webSite + type + "[" + this.exec(bean) + "]\n");
		} catch (Exception e) {
			System.out.println(pulishDate);
			this.setTaskStatus("执行失败");
			this.setTaskMsg("执行错误[" + pulishDate + "]:", e);
		} finally {
			UtilSql.close(this.con, this.ps);
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

	private int add(List<InfoBidResult> list) {
		int rs = 0;
		try {
			this.ps = con.prepareStatement(sql);
			for (InfoBidResult entity : list) {
				if (isExits(entity))
					continue;
				rs += 1;
				this.ps.setString(1, UtilConver.dateToStr(Const.fm_yyyy_MM_dd));// createDate
				this.ps.setString(2, entity.getName());
				this.ps.setInt(3, 0);// state
				this.ps.setInt(4, 0);// ord
				this.ps.setObject(5, new Date());// modifyTime

				this.ps.setString(6, entity.getWebSite());
				this.ps.setString(7, entity.getIndustry());
				this.ps.setString(8, entity.getBusiType());
				this.ps.setString(9, entity.getUnit());
				this.ps.setString(10, entity.getSource());

				this.ps.setString(11, entity.getProvince());
				this.ps.setString(12, entity.getCity());
				this.ps.setString(13, entity.getRegion());
				this.ps.setString(14, entity.getPublishTime());
				this.ps.setString(15, "");// memo

				this.ps.setString(16, entity.getContentType());
				this.ps.setString(17, entity.getContentCls());
				this.ps.setString(18, entity.getContent());
				this.ps.setString(19, entity.getContentUrl());
				this.ps.addBatch();
			}
			if (rs > 0) {
				this.ps.executeBatch();
				this.ps.clearBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	private boolean isExits(InfoBidResult model) throws Exception {
		String sql = "select count(1) from InfoBidResult where name='" + model.getName() + "' and unit='"
				+ model.getUnit() + "'";
		return UtilSql.isExist(con, sql, new Object[] {});
	}

	private int exec(Bean bean) throws Exception {
		int rs = 0;
		List<InfoBidResult> list = new ArrayList<InfoBidResult>();
		String url = "http://deal.ggzy.gov.cn/ds/deal/dealList.jsp";
		PhantomJSDriver driver = UtilWeb.getDriver(url);
		try {
//		driver.findElement(By.id("choose_time_05")).click();//发布时间
//			driver.findElement(By.id("choose_stage_0001")).click(); // 信息类型
//		driver.findElement(By.id("FINDTXT")).sendKeys("产权");
//		driver.findElement(By.id("FINDTXT")).submit();
//			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.id("searchButton")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		System.out.println(driver.getPageSource());
//		System.out.println(driver.findElements(By.className("a_righta")).get(1).getText());
//		driver.findElements(By.className("a_righta")).get(1).click();//下一页
//		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//		System.out.println(driver.getPageSource());
			Document doc = UtilWeb.getDocument(driver.getPageSource());
//			int pages=Integer.valueOf(doc.getElementsByClass("count").get(1).html().replace("共", "").replace("页", "").trim());
//			System.out.println();
			Elements elList = doc.getElementById("toview").getElementsByClass("publicont");
			for (Element li : elList) {
				InfoBidResult info = new InfoBidResult();
				Element h4 = li.getElementsByTag("h4").first();
				Element msg = li.getElementsByClass("p_tw").first();
				String title = h4.getElementsByTag("a").first().attr("title");
				String msgType=msg.select("span:eq(7)").first().html();
//				System.out.println(msgType);
				if(msgType.indexOf("结果")<0 && msgType.indexOf("中标")<0)continue;
				info.setName(title);
				info.setWebSite(webSite);
				info.setIndustry("");
//				info.setBusiType(msg.select("span:eq(5)").first().html());
				info.setBusiType(getBusiType(title));
				info.setProvince(msg.select("span:eq(1)").first().html());
				info.setCity("");
				info.setRegion(msg.select("span:eq(1)").first().html());
				info.setUnit("");
				info.setSource(msg.select("span:eq(3)").first().html());
				info.setPublishTime(h4.getElementsByTag("span").first().html());
				info.setContentType("html");
				info.setContentCls(cls);

				String contentUrl = h4.getElementsByTag("a").first().attr("href");
				info.setContentUrl(contentUrl);
//				System.out.println(contentUrl);
				Thread.sleep(ConstTask.SleepTime);
				Document documentDetail = UtilWeb.getPageDocument(contentUrl);
				Element iframe = documentDetail.getElementsByTag("iframe").first();
				String iframeSrc = "http://www.ggzy.gov.cn"+iframe.attr("src");
//				System.out.println(iframeSrc);
				Thread.sleep(ConstTask.SleepTime);
				Document documentIframe = UtilWeb.getPageDocument(iframeSrc);
				String contentHtml = documentIframe.getElementsByClass("detail").first().html();
				info.setContent(contentHtml);
				list.add(info);
				Thread.sleep(ConstTask.SleepTime);
//				System.out.println(UtilJackSon.toJson(list));
			}
		} finally {
			driver.quit();
		}
		rs += this.add(list);
		return rs;
	}

	private String getBusiType(String name) {
		if (name.indexOf("知识") >= 0 || name.indexOf("产权") >= 0) {
			return "知识产权";
		} else {
			return "";
		}
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://zbtb.gd.gov.cn/login";
		PhantomJSDriver driver = UtilWeb.getDriver(url);
		try {
			driver.findElement(By.id("zbgg")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); 
			Document doc = UtilWeb.getDocument(driver.getPageSource());
			System.out.println(doc.html());
		} finally {
			driver.quit();
		}
	}

}
