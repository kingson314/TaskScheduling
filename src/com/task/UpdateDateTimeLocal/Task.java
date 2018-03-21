package com.task.UpdateDateTimeLocal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;
import net.sf.json.JSONObject;

import com.taskInterface.TaskAbstract;

import common.util.UtilFun;
import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;

public class Task extends TaskAbstract {
	private static Map<Integer, int[]> mapDaylightSaving;// 夏令时，key=year

	// int[0] 夏令时开始日期，int[1]夏令时结束日期

	public Task() {
	}

	public void manuExecTask(String[] params) {
		this.fireTask();
	}

	public void fireTask() {
		Bean bean = (Bean) JSONObject.toBean(JSONObject.fromObject(this.getJsonStr()), Bean.class);
		Connection con = null;
		try {
			DbConnection dbConn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbConn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbConn);
			if (mapDaylightSaving == null) {
				mapDaylightSaving = new HashMap<Integer, int[]>();
			}
			int year = Integer.valueOf(this.getNowDate().substring(0, 4));
			int[] daylightSavingArr = mapDaylightSaving.get(year);
			if (daylightSavingArr == null) {
				// 美国夏令时 (3月第二个周日，结束时间是11月第一个周日，剩下时间为冬令时;)
				int begin = Integer.valueOf(UtilConver.dateToStr(UtilFun.getDate(year, 3, 2, 1), "yyyyMMdd"));
				int end = Integer.valueOf(UtilConver.dateToStr(UtilFun.getDate(year, 11, 1, 1), "yyyyMMdd"));
				daylightSavingArr = new int[] { begin, end };
				mapDaylightSaving.put(year, daylightSavingArr);
			}
			int nNowDate = Integer.valueOf(this.getNowDate());
			// 夏令时 NBF 差300分钟
			int timeDiff = 0;
			if (nNowDate >= daylightSavingArr[0] && nNowDate <= daylightSavingArr[1]) {
				timeDiff = bean.getTimeDiffDaylightSaving();
			} else {// 冬令时 NBF 差360分钟
				timeDiff = bean.getTimeDiffWinterTime();
			}

			String sql = "";
			String tableName = "price_" + bean.getSymbol();
			if (bean.isDetail()) {
				sql = getSql(tableName + "_detail", this.getNowDate(), timeDiff);
				execute(con, sql);
			}
			if (bean.isPeriod1()) {
				sql = getSql(tableName + 1, this.getNowDate(), timeDiff);
				execute(con, sql);
			}
			if (bean.isPeriod5()) {
				sql = getSql(tableName + 5, this.getNowDate(), timeDiff);
				execute(con, sql);
			}
			if (bean.isPeriod15()) {
				sql = getSql(tableName + 15, this.getNowDate(), timeDiff);
				execute(con, sql);
			}
			if (bean.isPeriod30()) {
				sql = getSql(tableName + 30, this.getNowDate(), timeDiff);
				execute(con, sql);
			}
			if (bean.isPeriod60()) {
				sql = getSql(tableName + 60, this.getNowDate(), timeDiff);
				execute(con, sql);
			}
			if (bean.isPeriod240()) {
				sql = getSql(tableName + 240, this.getNowDate(), timeDiff);
				execute(con, sql);
			}
			if (bean.isPeriod1440()) {
				sql = getSql(tableName + 1440, this.getNowDate(), timeDiff);
				execute(con, sql);
			}

			this.setTaskStatus("执行成功");
			this.setTaskMsg(this.getNowDate() + "更新完毕");
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("错误:", e);
		} finally {
			UtilSql.close(con);
		}
	}

	private void execute(Connection con, String sql) throws SQLException {
//		System.out.println(sql);
		PreparedStatement ps = con.prepareStatement(sql);
		ps.execute();
		UtilSql.close(ps);
	}

	private String getSql(String tableName, String curDate, int timeDiff) {
//		String sql = " update " + tableName + " set dateLocal=to_char(to_date(dateServer||' '||timeserver, 'yyyyMMdd HH24miss') +" + timeDiff + " * 1 / (24 * 60),'yyyyMMdd'),"
//				+ " timeLocal=to_char(to_date(dateServer||' '||timeserver, 'yyyyMMdd HH24miss') +" + timeDiff + " * 1 / (24 * 60),'HH24miss')  where  dateserver='" + curDate + "'";
		String sql = " update " + tableName + " set dateLocal=date_format(date_add(str_to_date(concat(dateServer,timeserver), '%Y%m%d %H%i%S'),interval "+timeDiff+" minute),'%Y%m%d'),"
		+ " timeLocal=date_format(date_add(str_to_date(concat(dateServer,timeserver), '%Y%m%d %H%i%S'),interval "+timeDiff+" minute),'%H%i%S')  where  dateserver='" + curDate + "'";
		return sql;
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		fireTask();
	}

	public static void main(String[] args) throws NumberFormatException, ParseException {
		// String date1 = "2012.12.05 15:30:31";
		// String date2 = "2012.12.05 15:35:32";
		// try {
		// System.out.println(Fun.diffMinute(date1, date2,
		// ConstApp.fmDateTime3));
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		String curDate = "20150306";
		System.out.println(curDate.substring(6, 8));
		int year=2014;
		int begin = Integer.valueOf(UtilConver.dateToStr(UtilFun.getDate(year, 3, 2, 1), "yyyyMMdd"));
		int end = Integer.valueOf(UtilConver.dateToStr(UtilFun.getDate(year, 11, 1, 1), "yyyyMMdd"));
		System.out.println(begin+" - "+end);
	}
}
