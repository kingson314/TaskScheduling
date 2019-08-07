package com.log.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Vector;
import app.AppCon;

import com.log.Log;

import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import consts.Const;

public class LogCommonDao {
	public static LogCommonDao execResultDao = null;
	private Connection con;

	public static LogCommonDao getInstance() {
		if (execResultDao == null)
			execResultDao = new LogCommonDao();
		return execResultDao;
	}

	// 构造
	public LogCommonDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 获取执行日志的最大时间戳
	public String getExecUpdateTime() {
		String result = "";
		try {
			String sql = "";
			if (AppCon.DbconApp.getDbType().toLowerCase().indexOf("access") >= 0)
				sql = "select max(format(dateTime,'yyyyMMdd HHmmss')) as updateTime from " + AppCon.TN_ExecResult;
			else
				sql = "select max(DATE_FORMAT (dateTime,'%Y%m%d  %H%i%S')) as updateTime from " + AppCon.TN_ExecResult;
			result = UtilSql.QueryForMax(con, sql, new Object[0]);
			if ("0".equals(result))
				result = UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss);
		} catch (SQLException e) {
			Log.logError("获取执行结果最大时间戳 SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取执行结果最大时间戳错误:", e);
		}
		return result;
	}

	// 获取执行日志数组
	public Vector<?> getLogCommonVector(Map<String, String> map) {

		Statement sm = null;
		ResultSet rs = null;
		Vector<Vector<String>> LogCommonVector = new Vector<Vector<String>>();
		if (map == null)
			return LogCommonVector;
		try {
			sm = con.createStatement();
			String sql = "select ";

			if (AppCon.DbconApp.getDbType().toLowerCase().indexOf("access") >= 0) {
				if (map.get("查询记录数") != null && map.get("查询记录数").trim().length() > 0)
					sql = sql + "  top " + Long.valueOf(map.get("查询记录数"));
				sql = sql + " iif(a.scheId='-1','手工',a.scheId) as scheId,b.schNme,a.groupID,c.groupName, a.taskID,d.taskName,d.taskType,a.startTime,a.endTime, a.taskStatus,a.taskFailmsg"
						+ " from (((  " + AppCon.TN_ExecResult + " a left join  " + AppCon.TN_ScheParam + " b on trim(a.scheId)= trim(str(b.schCde))) " + " left join   " + AppCon.TN_Taskgroup
						+ " c on trim(a.groupId)=trim(str(c.groupId))) " + " left join  " + AppCon.TN_Task + " d on trim(a.taskid)=trim(str(d.taskid))) where 1=1";

				if (map.get("开始日期") != null && map.get("开始日期").trim().length() > 0)
					sql = sql + " and dates>=Format('" + map.get("开始日期") + " ','yyyymmdd')  ";
				if (map.get("结束日期") != null && map.get("结束日期").trim().length() > 0)
					sql = sql + " and dates<=Format('" + map.get("结束日期") + " ','yyyymmdd')  ";
			} else {
				sql = sql + " a.scheId scheId,b.schNme,a.groupID,c.groupName, a.taskID,d.taskName,d.taskType,a.startTime,a.endTime, a.taskStatus,a.taskFailmsg" + " from (((  "
						+ AppCon.TN_ExecResult + " a left join  " + AppCon.TN_ScheParam + " b on (a.scheId)= ((b.schCde))) " + " left join   " + AppCon.TN_Taskgroup
						+ " c on  (a.groupId)= ((c.groupId))) " + " left join  " + AppCon.TN_Task + " d on  (a.taskid)= ( (d.taskid))) where 1=1";
				if (map.get("开始日期") != null && map.get("开始日期").trim().length() > 0)
					sql = sql + " and DATE_FORMAT (dateTime,'%Y%m%d')>='" + map.get("开始日期") + "'";
				if (map.get("结束日期") != null && map.get("结束日期").trim().length() > 0)
					sql = sql + " and DATE_FORMAT (dateTime,'%Y%m%d')<='" + map.get("结束日期") + "'";
//				if (map.get("查询记录数") != null && map.get("查询记录数").trim().length() > 0)
//					sql = sql + "  and rownum<= " + Long.valueOf(map.get("查询记录数"));
			}

			if (map.get("调度名称") != null && map.get("调度名称").trim().length() > 0)
				sql = sql + " and schNme  like ('%" + map.get("调度名称") + "%')  ";
			if (map.get("任务名称") != null && map.get("任务名称").trim().length() > 0)
				sql = sql + " and taskName like ('%" + map.get("任务名称") + "%')  ";
			if (map.get("任务组名称") != null && map.get("任务组名称").trim().length() > 0)
				sql = sql + " and groupName like ('%" + map.get("任务组名称") + "%')  ";
			if (map.get("任务类型") != null && map.get("任务类型").trim().length() > 0)
				sql = sql + " and d.taskType like ('%" + map.get("任务类型") + "%')  ";
			if (map.get("执行状态") != null && map.get("执行状态").trim().length() > 0)
				sql = sql + " and a.taskStatus like ('%" + map.get("执行状态") + "%')  ";
			if (map.get("执行结果") != null && map.get("执行结果").trim().length() > 0)
				sql = sql + " and a.taskFailmsg like ('%" + map.get("执行结果") + "%')  ";

			if (map.get("是否发送短信") != null && map.get("是否发送短信").trim().length() > 0)
				sql = sql + " and a.ifSendMsg like ('%" + map.get("是否发送短信") + "%')  ";

			// 手工执行显示执行结果时使用
			if (map.get("最大更新时间") != null)
				if (AppCon.DbconApp.getDbType().toLowerCase().indexOf("access") >= 0) {
					sql = sql + " and  format(dateTime,'yyyymmdd HHmmss') >'" + map.get("最大更新时间").trim() + "' and scheid='-1' ";
				} else {
					sql = sql + " and  dateTime>to_date('" + map.get("最大更新时间").trim() + "','yyyymmdd HH24miss')  and scheid=-1 ";
				}
			
			sql = sql + " order by a.starttime desc";
			if (map.get("查询记录数") != null && map.get("查询记录数").trim().length() > 0)
				sql = sql + " limit 0,"+map.get("查询记录数").trim()+" ";
			rs = sm.executeQuery(sql);
			while (rs.next()) {
				String scheid = rs.getString("scheId");
				String scheName = rs.getString("schNme");
				String groupid = rs.getString("groupid");
				String groupName = rs.getString("groupName");
				String taskid = rs.getString("taskid");
				String taskName = rs.getString("taskName");
				String tasktype = rs.getString("tasktype");
				String starttime = rs.getString("starttime");
				String endtime = rs.getString("endtime");
				String taskstatus = rs.getString("taskstatus");
				String taskFailmsg = rs.getString("taskFailmsg");

				Vector<String> rowValue = new Vector<String>();
				rowValue.add(scheid);
				rowValue.add(scheName);
				rowValue.add(starttime);
				rowValue.add(endtime);
				rowValue.add(taskstatus);
				rowValue.add(taskFailmsg);
				rowValue.add(groupid);
				rowValue.add(groupName);
				rowValue.add(taskid);
				rowValue.add(taskName);
				rowValue.add(tasktype);
				LogCommonVector.add(rowValue);
			}
		} catch (SQLException e) {
			Log.logError("获取执行结果表格参数SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取执行结果表格参数错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return LogCommonVector;
	}

	// 新增执行日志
	public synchronized void addLogCommon(LogCommon execResult) {
		try {

			String sql = "insert into   " + AppCon.TN_ExecResult + " (" + "scheid,taskid,groupid," + "starttime,endtime,taskstatus," + "taskfailmsg,ifSendMsg) values (?,?,?,?,?,?,?,?)";
			Object[] param = new Object[8];
			param[0] = execResult.getScheID();
			param[1] = execResult.getTaskId();
			param[2] = execResult.getGroupId();
			param[3] = execResult.getStartTime();
			param[4] = execResult.getEndTime();
			param[5] = execResult.getTaskStatus();
			param[6] = execResult.getTaskFailMsg();
			param[7] = execResult.getIfSendMsg();
			UtilSql.executeUpdate(con, sql, param);
		} catch (SQLException e) {
			Log.logError("新增执行结果SQL错误:", e);
		} catch (Exception e) {
			Log.logError("新增执行结果错误:", e);
		}
	}

	// 清空执行日志
	public void clearLogCommon() {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from   " + AppCon.TN_ExecResult);
			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError("清空执行结果表SQL错误:", e);
		} catch (Exception e) {
			Log.logError("清空执行结果表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}
}
