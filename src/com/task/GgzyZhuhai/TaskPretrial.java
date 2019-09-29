package com.task.GgzyZhuhai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.taskBusiBean.info.InfoBidPretrial;
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
 * @Description:珠海市公共资源交易中心-资格预审公告
 * @date Aut 1,o19
 * @author:fgq
 */
public class TaskPretrial extends TaskAbstract {
	private Connection con;
	private PreparedStatement ps;
	private String sql = "insert into InfoBidPretrial(createDate,name,state,ord,modifyTime,"
			+ "webSite,industry, busiType,unit,source," + "province,city,region,publishTime,openTime,"
			+ "contentType,contentCls,content,contentUrl)" + "values(?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
			+ "?,?,?,?) ";

	private final static String webSite="珠海市公共资源交易中心";
	private final static String type="(资格预审公告)";
	private final static String cls="ggzyZhuhai Pretrial";
	
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

	private int add(List<InfoBidPretrial> list) {
		int rs = 0;
		try {
			this.ps = con.prepareStatement(sql);
			for (InfoBidPretrial entity : list) {
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

	private boolean isExits(InfoBidPretrial model) throws Exception {
		String sql = "select count(1) from InfoBidPretrial where name='" + model.getName() + "' and unit='"
				+ model.getUnit() + "'";
		return UtilSql.isExist(con, sql, new Object[] {});
	}

	private int exec(Bean bean) throws Exception {
		int rs = 0;
		for (int i = 1; i <= bean.getPageIndex(); i++) {
			List<InfoBidPretrial> list = new ArrayList<InfoBidPretrial>();
			String url = "http://ggzy.zhuhai.gov.cn/exchangeinfo/govbuy/yzbgg/index.jhtml";
			if(i>1) {
				url="http://ggzy.zhuhai.gov.cn/exchangeinfo/govbuy/yzbgg/index_"+i+".jhtml";
			}
			Document doc = UtilWeb.getDoc(url);
			Elements elList = doc.getElementsByClass("rl-box-right").first().getElementsByTag("li");

			for (Element li : elList) {
				InfoBidPretrial info = new InfoBidPretrial();
				info.setName(li.getElementsByTag("a").first().attr("title"));
				info.setBusiType(getBusiType(info.getName()));
				info.setWebSite(webSite);
				info.setIndustry("");
				info.setProvince("广东");
				info.setCity("珠海");
				info.setRegion("广东");
				info.setUnit("");
				info.setSource("");
				info.setPublishTime(li.getElementsByTag("span").first().html());
				info.setOpenTime("");
				info.setContentType("html");
				info.setContentCls(cls);

				String contentUrl = li.getElementsByTag("a").first().attr("href");
				info.setContentUrl(contentUrl);
				Element contentEl = UtilWeb.getDoc(contentUrl);
				info.setContent(contentEl.getElementsByClass("contentone").first().html());
				list.add(info);
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
		List<InfoBidPretrial> list = new ArrayList<InfoBidPretrial>();
		String url = "http://ggzy.zhuhai.gov.cn/exchangeinfo/govbuy/cggg/index.jhtml";
		Document doc = UtilWeb.getDoc(url);
		Elements elList = doc.getElementsByClass("rl-box-right").first().getElementsByTag("li");

		for (Element li : elList) {
			InfoBidPretrial info = new InfoBidPretrial();
			info.setName(li.getElementsByTag("a").first().attr("title"));
			info.setBusiType(getBusiType(info.getName()));
			info.setWebSite(webSite);
			info.setIndustry("");
			info.setProvince("广东");
			info.setCity("珠海");
			info.setRegion("广东");
			info.setUnit("");
			info.setSource("");
			info.setPublishTime(li.getElementsByTag("span").first().html());
			info.setOpenTime("");
			info.setContentType("html");
			info.setContentCls(cls);

			String contentUrl = li.getElementsByTag("a").first().attr("href");
			info.setContentUrl(contentUrl);
			Element contentEl = UtilWeb.getDoc(contentUrl);
			info.setContent(contentEl.getElementsByClass("contentone").first().html());
			list.add(info);
//			System.out.println(UtilJackSon.toJson(list));
			break;
		}
	}

}
