package com.scher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Vector;
import app.AppCon;

import com.log.Log;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import consts.Const;

/**
 * @author: fenggq
 * @Email:
 * @Company:
 * @Action:调度信息持久化(数据库交互)
 * @DATE: Mar 8, 2012
 */
public class ScherDao {
	private static ScherDao scherdao = null;
	private Connection con;

	public static ScherDao getInstance() {
		if (scherdao == null)
			scherdao = new ScherDao();
		return scherdao;
	}

	// 构造
	public ScherDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 根据调度ID获取调度名称
	public String getScheNameFromScheCode(String ScheId) {
		Statement sm = null;
		ResultSet rs = null;
		String result = "";
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Long scheCode = 0l;
			try {
				scheCode = Long.valueOf(ScheId);
			} catch (Exception e) {
				return "";
			}
			rs = sm.executeQuery("select schNme from  " + AppCon.TN_ScheParam + "   where schCde=" + scheCode);
			rs.last();
			if (rs.getRow() > 0)
				result = rs.getString("schNme");
		} catch (Exception e) {
			Log.logError("查询当前日志摘要时，获取调度名称错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;

	}

	// 获取调度数组
	@SuppressWarnings("unchecked")
	public Vector<?> getScheVector(Map<String, String> map) {
		Statement sm = null;
		ResultSet rs = null;
		Vector<Vector<String>> scheVector = new Vector<Vector<String>>();
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String vsql = null;
			if (map.get("Type").equals("全部")) {
				vsql = "select * from  " + AppCon.TN_ScheParam + "   order by schOrder ";
			} else if (map.get("Type").equals("调度过滤")) {
				vsql = "select * from  " + AppCon.TN_ScheParam + "    where 1=1  ";
				if (map.get("调度名称").length() > 0) {
					vsql = vsql + " and schNme  like ('%" + map.get("调度名称") + "%')";
				}
				if (map.get("调度状态") != null)
					if (map.get("调度状态").length() > 0) {
						vsql = vsql + " and status  like ('%" + map.get("调度状态") + "%')";
					}
				vsql = vsql + " order by schOrder ";
			}
			rs = sm.executeQuery(vsql);
			while (rs.next()) {
				ScherParam sp = new ScherParam();
				sp.setStartWhen(rs.getString("StartWhen"));
				sp.setStartDate(rs.getString("StartDate"));
				sp.setEndDate(rs.getString("EndDate"));
				sp.setEndBy(rs.getString("EndBy"));
				sp.setDailyN(rs.getString("DailyN"));
				sp.setWeeklyN(rs.getString("WeeklyN"));
				sp.setWeek(rs.getString("Week"));
				sp.setMonth(rs.getString("month"));
				sp.setRecurMonthly(rs.getString("RecurMonthly"));
				sp.setMonthlyDay(rs.getString("MonthlyDay"));
				sp.setRecurPrimary(rs.getString("RecurPrimary"));
				sp.setSchCde(rs.getString("schCde"));
				sp.setSchNme(rs.getString("schNme"));
				sp.setSchType(rs.getString("schType"));
				sp.setSchComent(rs.getString("schComent"));
				sp.setStartTime(rs.getString("StartTime"));
				sp.setEndTime(rs.getString("EndTime"));
				sp.setMonthlyDOW(rs.getString("MonthlyDOW"));
				sp.setMonthlyNth(rs.getString("MonthlyNth"));
				sp.setSecondN(rs.getString("SecondN"));
				sp.setMinuteN(rs.getString("MinuteN"));
				sp.setPerMinuteSecond(rs.getString("PerMinuteSecond"));
				sp.setHourlyN(rs.getString("HourlyN"));
				sp.setHourlyMinute(rs.getString("HourlyMinute"));
				sp.setHourlySecond(rs.getString("HourlySecond"));
				sp.setTaskID(rs.getString("taskID"));
				sp.setNextFireTime(rs.getString("NextFireTime"));
				sp.setPreviousFireTime(rs.getString("PreviousFireTime"));
				String status = rs.getString("Status");
				if (status == null)
					status = "停止";
				if (status.equals(""))
					status = "停止";
				sp.setStatus(status);
				sp.setGroupID(rs.getString("GroupID"));
				sp.setDateType(rs.getString("dateType"));
				sp.setExecTime(rs.getString("execTime"));
				sp.setSchOrder(rs.getString("schOrder"));
				sp.setSpecialDate(rs.getString("specialDate"));
				sp.setNowDate(rs.getString("nowDate") == null ? false : rs.getBoolean("nowDate"));
				Vector rowValue = new Vector();
				rowValue.add(false);
				rowValue.add(sp.getSchOrder() == null ? "" : sp.getSchOrder());
				rowValue.add(sp.getSchCde() == null ? "" : sp.getSchCde());
				rowValue.add(sp.getSchNme() == null ? "" : sp.getSchNme());
				rowValue.add(sp.getGroupID() == null ? "" : sp.getGroupID());
				rowValue.add(sp.getTaskID() == null ? "" : sp.getTaskID());
				rowValue.add(sp.getPreviousFireTime() == null ? "" : sp.getPreviousFireTime());
				rowValue.add(sp.getNextFireTime() == null ? "" : sp.getNextFireTime());
				if (sp.getStatus() == null || sp.getStatus().equalsIgnoreCase("null"))
					rowValue.add("");
				else
					rowValue.add(sp.getStatus());
				rowValue.add(Const.SCHE_Type[Integer.valueOf(sp.getSchType() == null ? "0" : sp.getSchType())]);
				rowValue.add(sp.getDateType() == null ? "" : sp.getDateType());
				rowValue.add(sp.getExecTime() == null ? "" : sp.getExecTime());
				rowValue.add(sp.getSchComent() == null ? "" : sp.getSchComent());

				scheVector.add(rowValue);
			}
		} catch (Exception e) {
			Log.logError("获取调度表格参数错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return scheVector;
	}

	// 获取调度的时间信息
	public ScherParam[] getScherTime() {
		Statement sm = null;
		ResultSet rs = null;
		ScherParam[] scher = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select schCde,previousFireTime,NextFireTime from  " + AppCon.TN_ScheParam + "    order by schOrder");
			rs.last();

			if (rs.getRow() == 0)
				return scher;
			scher = new ScherParam[rs.getRow()];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				scher[i] = new ScherParam();
				scher[i].setSchCde(rs.getString("schCde"));
				scher[i].setPreviousFireTime(rs.getString("previousFireTime") == null ? "" : rs.getString("previousFireTime"));
				scher[i].setNextFireTime(rs.getString("nextFireTime") == null ? "" : rs.getString("nextFireTime"));
				i = i + 1;
			}

		} catch (Exception e) {
			Log.logError("根据任务ID获取调度列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return scher;
	}

	// 根据任务id获取调度数组
	public ScherParam[] getScheParamsFromTaskID(Long taskID) {
		Statement sm = null;
		ResultSet rs = null;
		ScherParam[] sp = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_ScheParam + "   where taskID='" + taskID + "'  order by schOrder");
			rs.last();
			if (rs.getRow() == 0)
				return sp;
			sp = new ScherParam[rs.getRow()];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				// System.out.println("startwhen:" + rs.getString("StartWhen"));
				sp[i] = new ScherParam();
				sp[i].setStartWhen(rs.getString("StartWhen"));
				sp[i].setStartDate(rs.getString("StartDate"));
				sp[i].setEndDate(rs.getString("EndDate"));
				sp[i].setEndBy(rs.getString("EndBy"));
				sp[i].setDailyN(rs.getString("DailyN"));
				sp[i].setWeeklyN(rs.getString("WeeklyN"));
				sp[i].setWeek(rs.getString("Week"));
				sp[i].setMonth(rs.getString("month"));
				sp[i].setRecurMonthly(rs.getString("RecurMonthly"));
				sp[i].setMonthlyDay(rs.getString("MonthlyDay"));
				sp[i].setRecurPrimary(rs.getString("RecurPrimary"));
				sp[i].setSchCde(rs.getString("schCde"));
				sp[i].setSchNme(rs.getString("schNme"));
				sp[i].setSchType(rs.getString("schType"));
				sp[i].setSchComent(rs.getString("schComent"));
				sp[i].setStartTime(rs.getString("StartTime"));
				sp[i].setEndTime(rs.getString("EndTime"));
				sp[i].setMonthlyDOW(rs.getString("MonthlyDOW"));
				sp[i].setMonthlyNth(rs.getString("MonthlyNth"));
				sp[i].setSecondN(rs.getString("SecondN"));
				sp[i].setMinuteN(rs.getString("MinuteN"));
				sp[i].setPerMinuteSecond(rs.getString("PerMinuteSecond"));
				sp[i].setHourlyN(rs.getString("HourlyN"));
				sp[i].setHourlyMinute(rs.getString("HourlyMinute"));
				sp[i].setHourlySecond(rs.getString("HourlySecond"));
				sp[i].setTaskID(rs.getString("taskID"));
				sp[i].setNextFireTime(rs.getString("NextFireTime"));
				sp[i].setPreviousFireTime(rs.getString("PreviousFireTime"));
				sp[i].setStatus(rs.getString("status"));
				sp[i].setGroupID(rs.getString("GroupId"));
				sp[i].setDateType(rs.getString("dateType"));
				sp[i].setExecTime(rs.getString("execTime"));
				sp[i].setSchOrder(rs.getString("schOrder"));
				sp[i].setSpecialDate(rs.getString("specialDate"));
				sp[i].setNowDate(rs.getString("nowDate") == null ? false : rs.getBoolean("nowDate"));
				i = i + 1;
			}
		} catch (Exception e) {
			Log.logError("根据任务ID获取调度列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return sp;
	}

	// 根据任务组id获取调度数组
	public ScherParam[] getScheParamsFromGroupID(Long GrouId) {
		Statement sm = null;
		ResultSet rs = null;
		ScherParam[] sp = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_ScheParam + "   where GroupId='" + GrouId + "'  order by schOrder");
			rs.last();
			if (rs.getRow() == 0)
				return sp;
			sp = new ScherParam[rs.getRow()];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				sp[i] = new ScherParam();
				sp[i].setStartWhen(rs.getString("StartWhen"));
				sp[i].setStartDate(rs.getString("StartDate"));
				sp[i].setEndDate(rs.getString("EndDate"));
				sp[i].setEndBy(rs.getString("EndBy"));
				sp[i].setDailyN(rs.getString("DailyN"));
				sp[i].setWeeklyN(rs.getString("WeeklyN"));
				sp[i].setWeek(rs.getString("Week"));
				sp[i].setMonth(rs.getString("month"));
				sp[i].setRecurMonthly(rs.getString("RecurMonthly"));
				sp[i].setMonthlyDay(rs.getString("MonthlyDay"));
				sp[i].setRecurPrimary(rs.getString("RecurPrimary"));
				sp[i].setSchCde(rs.getString("schCde"));
				sp[i].setSchNme(rs.getString("schNme"));
				sp[i].setSchType(rs.getString("schType"));
				sp[i].setSchComent(rs.getString("schComent"));
				sp[i].setStartTime(rs.getString("StartTime"));
				sp[i].setEndTime(rs.getString("EndTime"));
				sp[i].setMonthlyDOW(rs.getString("MonthlyDOW"));
				sp[i].setMonthlyNth(rs.getString("MonthlyNth"));
				sp[i].setSecondN(rs.getString("SecondN"));
				sp[i].setMinuteN(rs.getString("MinuteN"));
				sp[i].setPerMinuteSecond(rs.getString("PerMinuteSecond"));
				sp[i].setHourlyN(rs.getString("HourlyN"));
				sp[i].setHourlyMinute(rs.getString("HourlyMinute"));
				sp[i].setHourlySecond(rs.getString("HourlySecond"));
				sp[i].setTaskID(rs.getString("taskID"));
				sp[i].setNextFireTime(rs.getString("NextFireTime"));
				sp[i].setPreviousFireTime(rs.getString("PreviousFireTime"));
				sp[i].setStatus(rs.getString("status"));
				sp[i].setGroupID(rs.getString("GroupId"));
				sp[i].setDateType(rs.getString("dateType"));
				sp[i].setExecTime(rs.getString("execTime"));
				sp[i].setSchOrder(rs.getString("schOrder"));
				sp[i].setSpecialDate(rs.getString("specialDate"));
				sp[i].setNowDate(rs.getString("nowDate") == null ? false : rs.getBoolean("nowDate"));
				i = i + 1;
			}
		} catch (Exception e) {
			Log.logError("根据任务ID获取调度列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return sp;
	}

	// 获取最大调度ID
	public long getMaxScheID() {
		long maxID = 0;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			rs = sm.executeQuery("select max(schCde) as maxID from  " + AppCon.TN_ScheParam + "   ");
			rs.last();
			if (rs.getRow() > 0)
				maxID = rs.getLong("maxID");
		} catch (SQLException e) {
			Log.logError("获取最大调度ID执行SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取最大调度ID错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return maxID;
	}

	// 判断任务是否存在调度
	public boolean IfTaskSechExist(long taskid) {
		Statement sm = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			rs = sm.executeQuery("select taskID from   " + AppCon.TN_ScheParam + "   where taskID='" + String.valueOf(taskid) + "'  ");
			rs.last();
			if (rs.getRow() > 0)
				result = true;
		} catch (Exception e) {
			Log.logError(" 判断是否存在调度错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 判断任务组是否存在调度
	public boolean IfTaskGroupSechExist(long GrouId) {
		Statement sm = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select groupId from   " + AppCon.TN_ScheParam + "   where GroupId='" + String.valueOf(GrouId) + "' ");
			rs.last();
			if (rs.getRow() > 0)
				result = true;
		} catch (Exception e) {
			Log.logError(" 判断是否存在调度错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 根据调度id获取调度信息
	public ScherParam getScheParamsFromSchCde(Long schCde) {
		Statement sm = null;
		ResultSet rs = null;
		ScherParam sp = null;

		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_ScheParam + "   where schCde=" + schCde + "  order by schOrder");
			rs.last();
			sp = new ScherParam();
			rs.beforeFirst();
			while (rs.next()) {
				sp.setStartWhen(rs.getString("StartWhen"));
				sp.setStartDate(rs.getString("StartDate"));
				sp.setEndDate(rs.getString("EndDate"));
				sp.setEndBy(rs.getString("EndBy"));
				sp.setDailyN(rs.getString("DailyN"));
				sp.setWeeklyN(rs.getString("WeeklyN"));
				sp.setWeek(rs.getString("Week"));
				sp.setMonth(rs.getString("month"));
				sp.setRecurMonthly(rs.getString("RecurMonthly"));
				sp.setMonthlyDay(rs.getString("MonthlyDay"));
				sp.setRecurPrimary(rs.getString("RecurPrimary"));
				sp.setSchCde(rs.getString("schCde"));
				sp.setSchNme(rs.getString("schNme"));
				sp.setSchType(rs.getString("schType"));
				sp.setSchComent(rs.getString("schComent"));
				sp.setStartTime(rs.getString("StartTime"));
				sp.setEndTime(rs.getString("EndTime"));
				sp.setMonthlyDOW(rs.getString("MonthlyDOW"));
				sp.setMonthlyNth(rs.getString("MonthlyNth"));
				sp.setSecondN(rs.getString("SecondN"));
				sp.setMinuteN(rs.getString("MinuteN"));
				sp.setPerMinuteSecond(rs.getString("PerMinuteSecond"));
				sp.setHourlyN(rs.getString("HourlyN"));
				sp.setHourlyMinute(rs.getString("HourlyMinute"));
				sp.setHourlySecond(rs.getString("HourlySecond"));
				sp.setTaskID(rs.getString("taskID"));
				sp.setNextFireTime(rs.getString("NextFireTime"));
				sp.setPreviousFireTime(rs.getString("PreviousFireTime"));
				sp.setStatus(rs.getString("status"));
				sp.setGroupID(rs.getString("groupID"));
				sp.setDateType(rs.getString("dateType"));
				sp.setExecTime(rs.getString("execTime"));
				sp.setSchOrder(rs.getString("schOrder"));
				sp.setSpecialDate(rs.getString("specialDate"));
				sp.setNowDate(rs.getString("nowDate") == null ? false : rs.getBoolean("nowDate"));
			}
		} catch (Exception e) {
			Log.logError("根据调度编码获取调度参数错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return sp;
	}

	// 获取所有调度
	public ScherParam[] getScheParams() {
		Statement sm = null;
		ResultSet rs = null;
		ScherParam[] sp = null;

		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_ScheParam + "   order by schOrder");
			rs.last();
			if (rs.getRow() == 0)
				return sp;
			sp = new ScherParam[rs.getRow()];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				sp[i] = new ScherParam();
				sp[i].setStartWhen(rs.getString("StartWhen"));
				sp[i].setStartDate(rs.getString("StartDate"));
				sp[i].setEndDate(rs.getString("EndDate"));
				sp[i].setEndBy(rs.getString("EndBy"));
				sp[i].setDailyN(rs.getString("DailyN"));
				sp[i].setWeeklyN(rs.getString("WeeklyN"));
				sp[i].setWeek(rs.getString("Week"));
				sp[i].setMonth(rs.getString("month"));
				sp[i].setRecurMonthly(rs.getString("RecurMonthly"));
				sp[i].setMonthlyDay(rs.getString("MonthlyDay"));
				sp[i].setRecurPrimary(rs.getString("RecurPrimary"));
				sp[i].setSchCde(rs.getString("schCde"));
				sp[i].setSchNme(rs.getString("schNme"));
				sp[i].setSchType(rs.getString("schType"));
				sp[i].setSchComent(rs.getString("schComent"));
				sp[i].setStartTime(rs.getString("StartTime"));
				sp[i].setEndTime(rs.getString("EndTime"));
				sp[i].setMonthlyDOW(rs.getString("MonthlyDOW"));
				sp[i].setMonthlyNth(rs.getString("MonthlyNth"));
				sp[i].setSecondN(rs.getString("SecondN"));
				sp[i].setMinuteN(rs.getString("MinuteN"));
				sp[i].setPerMinuteSecond(rs.getString("PerMinuteSecond"));
				sp[i].setHourlyN(rs.getString("HourlyN"));
				sp[i].setHourlyMinute(rs.getString("HourlyMinute"));
				sp[i].setHourlySecond(rs.getString("HourlySecond"));
				sp[i].setTaskID(rs.getString("taskID"));
				sp[i].setNextFireTime(rs.getString("NextFireTime"));
				sp[i].setPreviousFireTime(rs.getString("PreviousFireTime"));
				sp[i].setStatus(rs.getString("status"));
				sp[i].setGroupID(rs.getString("groupID"));
				sp[i].setDateType(rs.getString("dateType"));
				sp[i].setExecTime(rs.getString("execTime"));
				sp[i].setSchOrder(rs.getString("schOrder"));
				sp[i].setSpecialDate(rs.getString("specialDate"));
				sp[i].setNowDate(rs.getString("nowDate") == null ? false : rs.getBoolean("nowDate"));
				i = i + 1;
			}
		} catch (Exception e) {
			Log.logError("获取调度列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return sp;
	}

	// 新增调度信息
	public void addSche(ScherParam sp) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("insert into  " + AppCon.TN_ScheParam + "  (" + "StartWhen,StartDate,EndDate,EndBy," + "DailyN,WeeklyN,Week,month,"
					+ "RecurMonthly,MonthlyDay,RecurPrimary,schCde," + "schNme,schType,schComent,StartTime," + "EndTime,MonthlyDOW,MonthlyNth,SecondN,"
					+ "MinuteN,PerMinuteSecond,HourlyN,HourlyMinute," + "HourlySecond,status,taskID,GroupID,dateType,execTime,schOrder,specialDate,nowDate)" + " values (?,?,?," + "?,?,?,?,"
					+ "?,?,?,?," + "?,?,?,?," + "?,?,?,?," + "?,?,?,?," + "?,?,?,?," + "?,?,?,?,?,?)");
			ps.setString(1, sp.getStartWhen());
			ps.setString(2, sp.getStartDate());
			ps.setString(3, sp.getEndDate());
			ps.setString(4, sp.getEndBy());
			ps.setString(5, sp.getDailyN());
			ps.setString(6, sp.getWeeklyN());
			ps.setString(7, sp.getWeek());
			ps.setString(8, sp.getMonth());
			ps.setString(9, sp.getRecurMonthly());
			ps.setString(10, sp.getMonthlyDay());
			ps.setString(11, sp.getRecurPrimary());
			ps.setString(12, sp.getSchCde());
			ps.setString(13, sp.getSchNme());
			ps.setString(14, sp.getSchType());
			ps.setString(15, sp.getSchComent());
			ps.setString(16, sp.getStartTime());
			ps.setString(17, sp.getEndTime());
			ps.setString(18, sp.getMonthlyDOW());
			ps.setString(19, sp.getMonthlyNth());
			ps.setString(20, sp.getSecondN());
			ps.setString(21, sp.getMinuteN());
			ps.setString(22, sp.getPerMinuteSecond());
			ps.setString(23, sp.getHourlyN());
			ps.setString(24, sp.getHourlyMinute());
			ps.setString(25, sp.getHourlySecond());
			ps.setString(26, sp.getStatus());
			ps.setString(27, sp.getTaskID());
			ps.setString(28, sp.getGroupID());
			ps.setString(29, sp.getDateType());
			ps.setString(30, sp.getExecTime());
			ps.setString(31, sp.getSchOrder());
			ps.setString(32, sp.getSpecialDate());
			ps.setBoolean(33, sp.isNowDate());
			ps.addBatch();
			ps.executeBatch();
		} catch (Exception e) {
			Log.logError("新增调度错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 修改调度信息
	public void modSche(ScherParam sp) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("update    " + AppCon.TN_ScheParam + "   set " + "StartWhen=?,StartDate=?,EndDate=?,EndBy=?," + "DailyN=?,WeeklyN=?,Week=?,month=?,"
					+ "RecurMonthly=?,MonthlyDay=?,RecurPrimary=?,schCde=?," + "schNme=?,schType=?,schComent=?,StartTime=?," + "EndTime=?,MonthlyDOW=?,MonthlyNth=?,SecondN=?,"
					+ "MinuteN=?,PerMinuteSecond=?,HourlyN=?,HourlyMinute=?," + "HourlySecond=?,PreviousFireTime=?,"
					+ "NextFireTime=?,dateType=?,execTime=? ,status=?,schOrder=? , specialDate=? , nowDate=?" + " where schCde=" + Long.valueOf(sp.getSchCde()));
			ps.setString(1, sp.getStartWhen());
			ps.setString(2, sp.getStartDate());
			ps.setString(3, sp.getEndDate());
			ps.setString(4, sp.getEndBy());
			ps.setString(5, sp.getDailyN());
			ps.setString(6, sp.getWeeklyN());
			ps.setString(7, sp.getWeek());
			ps.setString(8, sp.getMonth());
			ps.setString(9, sp.getRecurMonthly());
			ps.setString(10, sp.getMonthlyDay());
			ps.setString(11, sp.getRecurPrimary());
			ps.setString(12, sp.getSchCde());
			ps.setString(13, sp.getSchNme());
			ps.setString(14, sp.getSchType());
			ps.setString(15, sp.getSchComent());
			ps.setString(16, sp.getStartTime());
			ps.setString(17, sp.getEndTime());
			ps.setString(18, sp.getMonthlyDOW());
			ps.setString(19, sp.getMonthlyNth());
			ps.setString(20, sp.getSecondN());
			ps.setString(21, sp.getMinuteN());
			ps.setString(22, sp.getPerMinuteSecond());
			ps.setString(23, sp.getHourlyN());
			ps.setString(24, sp.getHourlyMinute());
			ps.setString(25, sp.getHourlySecond());
			ps.setString(26, sp.getPreviousFireTime());
			ps.setString(27, sp.getNextFireTime());
			ps.setString(28, sp.getDateType());
			ps.setString(29, sp.getExecTime());
			ps.setString(30, sp.getStatus());
			ps.setString(31, sp.getSchOrder());
			ps.setString(32, sp.getSpecialDate());
			ps.setBoolean(33, sp.isNowDate());
			ps.addBatch();
			ps.executeBatch();
		} catch (Exception e) {
			Log.logError("根据ScheParam修改调度错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 新增调度时间信息
	public void modScher(ScherParam sp) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (sp.getPreviousFireTime().length() > 1) {
				ps = con.prepareStatement("update  " + AppCon.TN_ScheParam + "   set " + "NextFireTime=?,status=?,PreviousFireTime=? " + " where schCde=" + sp.getSchCde());
				ps.setString(1, sp.getNextFireTime());
				ps.setString(2, sp.getStatus());
				ps.setString(3, sp.getPreviousFireTime());
				ps.addBatch();

			} else {
				ps = con.prepareStatement("update  " + AppCon.TN_ScheParam + " set " + "NextFireTime=?,status=? " + " where schCde=" + sp.getSchCde());
				ps.setString(1, sp.getNextFireTime());
				ps.setString(2, sp.getStatus());
				ps.addBatch();
			}

			ps.executeBatch();
		} catch (Exception e) {
			Log.logError("根据Scher修改调度错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 删除调度信息
	public void delSche(Long schCde) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from  " + AppCon.TN_ScheParam + "   where schCde=" + schCde);

			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError("删除调度SQL错误:", e);
		} catch (Exception e) {
			Log.logError("删除调度错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

	// 获取最大调度ID
	public int getMaxOrderLen() {
		int len = 0;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			try {
				rs = sm.executeQuery("select max(len(SchOrder)) as len from  " + AppCon.TN_ScheParam);
			} catch (Exception e) {
				rs = sm.executeQuery("select max(length(SchOrder)) as len from  " + AppCon.TN_ScheParam);
			}
			rs.last();
			if (rs.getRow() > 0)
				len = rs.getInt("len");
		} catch (SQLException e) {
			Log.logError("获取最大调度Order执行SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取最大调度Order错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return len;
	}

	// 修改调度顺序号
	public void modOrder(long schCde, String SchOrder) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("update  " + AppCon.TN_ScheParam + "   set  SchOrder=? where schCde=" + schCde);
			ps.setString(1, SchOrder);
			ps.addBatch();
			ps.executeBatch();
			ps.clearBatch();
		} catch (SQLException e) {
			Log.logError("修改调度Order错误:", e);
		} finally {
			UtilSql.close(ps);
		}
	}

	// 获取调度中是否有使用当前执行日期的调度，如果使用了则在启动时先获取一次该日期，以免启动后因延时造成调度连续启动
	public boolean isUseNowDate() {
		boolean result = false;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select count(1) as cnt from  " + AppCon.TN_ScheParam + " where nowDate <> '0'  ");

			rs.last();
			if (rs.getRow() > 0) {
				int len = rs.getInt("cnt");
				if (len > 0)
					result = true;
			}
		} catch (Exception e) {
			Log.logError("判断调度中是否使用了当前执行日期错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}
}
