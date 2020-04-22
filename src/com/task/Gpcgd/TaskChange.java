package com.task.Gpcgd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.task.ConstTask;
import com.taskBusiBean.info.InfoBidChange;
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
 * @Description:广东省政府采购中心-变更公告公示
 * @date Aut 1,o19
 * @author:fgq
 */
public class TaskChange extends TaskAbstract {
	private Connection con;
	private PreparedStatement ps;
	private String sql = "insert into InfoBidChange(createDate,name,state,ord,modifyTime,"
			+ "webSite,industry, busiType,unit,source," + "province,city,region,publishTime,nameOriginal,"
			+ "contentType,contentCls,content,contentUrl,memo)" + "values(?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
			+ "?,?,?,?,?) ";
	
	private final static String webSite="广东省政府采购中心";
	private final static String type="(变更公告公示)";
	private final static String cls="gpcgd Change";
	
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

	private int add(List<InfoBidChange> list) {
		int rs = 0;
		try {
			this.ps = con.prepareStatement(sql);
			for (InfoBidChange entity : list) {
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
				this.ps.setString(15, entity.getNameOriginal());

				this.ps.setString(16, entity.getContentType());
				this.ps.setString(17, entity.getContentCls());
				this.ps.setString(18, entity.getContent());
				this.ps.setString(19, entity.getContentUrl());
				this.ps.setString(20, entity.getMemo());
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

	private boolean isExits(InfoBidChange model) throws Exception {
		String sql = "select count(1) from InfoBidChange where name='" + model.getName() + "' and unit='"
				+ model.getUnit() + "'";
		return UtilSql.isExist(con, sql, new Object[] {});
	}

	private int exec(Bean bean) throws Exception {
		int rs = 0;
		for (int i = 1; i <= bean.getPageIndex(); i++) {
			List<InfoBidChange> list = new ArrayList<InfoBidChange>();
			String url = "http://gpcgd.gd.gov.cn/gpcgd_buy_clarify/index.html";
			if(i>1) {
				url="http://gpcgd.gd.gov.cn/gpcgd_buy_clarify/index_"+i+".html";
			}
			Document doc = UtilWeb.getDoc(url);
			Elements elList = doc.getElementsByClass("pub_cont06").first().getElementsByTag("li");

			for (Element li : elList) {
				InfoBidChange info = new InfoBidChange();
				info.setWebSite(webSite);
				info.setIndustry("");
				info.setProvince("广东");
				info.setCity("");
				info.setRegion("广东");
				info.setUnit("");
				info.setSource("");
				info.setPublishTime(li.getElementsByClass("span_time").first().html());
				info.setContentType("html");
				info.setContentCls(cls);

				String contentUrl = li.getElementsByTag("a").first().attr("href");
				info.setContentUrl(contentUrl);
				Thread.sleep(ConstTask.SleepTime);
				Element contentEl = UtilWeb.getDoc(contentUrl);
				info.setName(contentEl.getElementsByClass("pub_title").first().html());
				info.setBusiType(getBusiType(info.getName()));
				info.setContent(contentEl.getElementsByClass("detial").first().html());
				list.add(info);
				Thread.sleep(ConstTask.SleepTime);
//				System.out.println(UtilJackSon.toJson(list));
			}
			rs += this.add(list);
		}
		return rs;
	}

	private static String getBusiType(String name) {
		if (name.indexOf("知识") >= 0 || name.indexOf("产权") >= 0) {
			return "知识产权";
		} else {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		List<InfoBidChange> list = new ArrayList<InfoBidChange>();
		String url = "http://gpcgd.gd.gov.cn/gpcgd_buy_clarify/index.html";
		Document doc = UtilWeb.getDoc(url);
		Elements elList = doc.getElementsByClass("pub_cont06").first().getElementsByTag("li");

		for (Element li : elList) {
			InfoBidChange info = new InfoBidChange();
//			info.setName();
			info.setWebSite(webSite);
			info.setIndustry("");
			info.setProvince("广东");
			info.setCity("");
			info.setRegion("广东");
			info.setUnit("");
			info.setSource("");
			info.setPublishTime(li.getElementsByClass("span_time").first().html());
			info.setContentType("html");
			info.setContentCls(cls);

			String contentUrl = li.getElementsByTag("a").first().attr("href");
			info.setContentUrl(contentUrl);

			Element contentEl = UtilWeb.getDoc(contentUrl);
			info.setName(contentEl.getElementsByClass("pub_title").first().html());
			info.setBusiType(getBusiType(info.getName()));
			info.setContent(contentEl.getElementsByClass("detial").first().html());
			list.add(info);
			System.out.println(info.getContent());
//			System.out.println(UtilJackSon.toJson(list));
			break;
		}
	}

}
