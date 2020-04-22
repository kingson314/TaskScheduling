package com.task.Cebpubservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.task.ConstTask;
import com.taskBusiBean.info.InfoBidNotice;
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
 * @Description:中国招投标公共服务平台-招标公告
 * @date Aut 1,o19
 * @author:fgq
 */
public class TaskNotice extends TaskAbstract {
	private Connection con;
	private PreparedStatement ps;
	private String sql = "insert into InfoBidNotice(createDate,name,state,ord,modifyTime,"
			+ "webSite,industry, busiType,unit,source," + "province,city,region,publishTime,openTime,"
			+ "contentType,contentCls,content,contentUrl)" + "values(?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
			+ "?,?,?,?) ";

	private final static String webSite="中国招投标公共服务平台";
	private final static String type="(招标公告)";
	private final static String cls="cebpubservice Notice";
	
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
			this.setTaskMsg(webSite+type+"[" + this.exec(bean) + "]\n");
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

	private int add(List<InfoBidNotice> list) {
		int rs = 0;
		try {
			this.ps = con.prepareStatement(sql);
			for (InfoBidNotice entity : list) {
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
				this.ps.setString(15, entity.getOpenTime());

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

	private boolean isExits(InfoBidNotice model) throws Exception {
		String sql = "select count(1) from InfoBidNotice where name='" + model.getName() + "' and unit='"
				+ model.getUnit() + "'";
		return UtilSql.isExist(con, sql, new Object[] {});
	}

	private int exec(Bean bean) throws Exception {
		int rs = 0;
		for (int i = 1; i <= bean.getPageIndex(); i++) {
			List<InfoBidNotice> list = new ArrayList<InfoBidNotice>();
			String url = "http://bulletin.cebpubservice.com/xxfbcmses/search/bulletin.html?searchDate="
					+ UtilConver.dateToStr(Const.fm_yyyy_MM_dd)
					+ "&dates=300&categoryId=88&industryName=&area=&status=&publishMedia=&sourceInfo=&showStatus=&word=&page="
					+ i;
			System.out.println(webSite+type+"[page]:" + i);
			Document doc = UtilWeb.getDoc(url);
			Elements elList = doc.getElementsByTag("table").first().getElementsByTag("tr");
			for (Element tr : elList) {
				Elements tds = tr.select("td");
				if (tds.size() > 0) {
					try {
						InfoBidNotice info = new InfoBidNotice();
						info.setName(tds.first().getElementsByTag("a").first().attr("title"));
						info.setWebSite(webSite);
						info.setIndustry(tr.select("td:eq(1)").first().getElementsByTag("span").first().attr("title"));
//					if(info.getName().indexOf("知识")<0 && info.getName().indexOf("产权")<0 )continue;
						info.setBusiType(getBusiType(info.getName()));
						info.setProvince("");
						info.setCity("");
						info.setRegion(tr.select("td:eq(2)").first().getElementsByTag("span").first().attr("title"));
						info.setUnit(getUnit(tr.select("td:eq(3)").html()));
						info.setSource("");
						info.setPublishTime(tr.select("td:eq(4)").html());
						info.setOpenTime(tr.select("td:eq(5)").attr("id"));
						info.setContentType("swf");
						info.setContentCls(cls);
						String contentUrl = tds.first().getElementsByTag("a").first().attr("href")
								.replace("javascript:urlOpen('", "").replace("')", "");
						info.setContentUrl(contentUrl);
						Thread.sleep(ConstTask.SleepTime);
						String contentHtml = UtilWeb.getDoc(contentUrl).html();
						String swfPath = contentHtml.substring(contentHtml.indexOf("escape('") + "escape('".length());
						swfPath = swfPath.substring(0, swfPath.indexOf("')"));
						info.setContent(swfPath);
//				System.out.println(UtilJackSon.toJson(info));
//				UtilHttp.download(swfPath, "/Volumes/Docs/Git/nginx/infoCenter/cebpubservice/"+swfPath.substring(swfPath.lastIndexOf("/")+1));
						list.add(info);
						Thread.sleep(ConstTask.SleepTime);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			rs += this.add(list);
		}
		return rs;
	}

	private static String getUnit(String unit) {
		if ("".equals(unit) || "发布工具".equals(unit)) {
			return "中国招投标公共服务平台";
		} else {
			return unit;
		}
	}

	private static String getBusiType(String name) {
		if (name.indexOf("知识") >= 0 || name.indexOf("产权") >= 0) {
			return "知识产权";
		} else {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		List<InfoBidNotice> list = new ArrayList<InfoBidNotice>();
		int pageIndex = 1;
		String url = "http://bulletin.cebpubservice.com/xxfbcmses/search/bulletin.html?searchDate="
				+ UtilConver.dateToStr(Const.fm_yyyy_MM_dd)
				+ "&dates=300&categoryId=88&industryName=&area=&status=&publishMedia=&sourceInfo=&showStatus=&word=&page="
				+ pageIndex;
		Document doc = UtilWeb.getDoc(url);
		Elements elList = doc.getElementsByTag("table").first().getElementsByTag("tr");

		for (Element tr : elList) {
			Elements tds = tr.select("td");
			if (tds.size() > 0) {
				InfoBidNotice info = new InfoBidNotice();
				info.setName(tds.first().getElementsByTag("a").first().attr("title"));
				info.setWebSite(webSite);
				info.setIndustry(tr.select("td:eq(1)").first().getElementsByTag("span").first().attr("title"));
				info.setBusiType(getBusiType(info.getName()));
				info.setProvince("");
				info.setCity("");
				info.setRegion(tr.select("td:eq(2)").first().getElementsByTag("span").first().attr("title"));
				info.setUnit(getUnit(tr.select("td:eq(3)").html()));
				info.setSource("");
				info.setPublishTime(tr.select("td:eq(4)").html());
				info.setOpenTime(tr.select("td:eq(5)").attr("id"));
				info.setContentType("swf");
				info.setContentCls(cls);

				String contentUrl = tds.first().getElementsByTag("a").first().attr("href")
						.replace("javascript:urlOpen('", "").replace("')", "");
				info.setContentUrl(contentUrl);

				String contentHtml = UtilWeb.getDoc(contentUrl).html();
				String swfPath = contentHtml.substring(contentHtml.indexOf("escape('") + "escape('".length());
				swfPath = swfPath.substring(0, swfPath.indexOf("')"));
				info.setContent(swfPath);
//				System.out.println(swfPath);
//				UtilHttp.download(swfPath, "/Volumes/Docs/Git/nginx/infoCenter/cebpubservice/"+swfPath.substring(swfPath.lastIndexOf("/")+1));
				list.add(info);
				System.out.println(UtilJackSon.toJson(list));
				break;
			}
		}
	}

}
