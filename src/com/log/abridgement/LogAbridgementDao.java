package com.log.abridgement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import app.AppCon;

import com.log.Log;
import com.scher.ScherDao;
import com.taskInterface.ITask;
import com.taskInterface.TaskDao;
import com.taskgroup.TaskGroupDao;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.string.UtilString;
import consts.Const;

public class LogAbridgementDao {
	public static LogAbridgementDao logAbridgementdao = null;
	private Connection con;

	public static LogAbridgementDao getInstance() {
		if (logAbridgementdao == null)
			logAbridgementdao = new LogAbridgementDao();
		return logAbridgementdao;
	}

	// 构造
	private LogAbridgementDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 新增高频日志摘要
	public synchronized void addLogAbridgement(LogAbridgement logAbridgement) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("insert into " + AppCon.TN_ExecLogAbridgement + " (" + " scheId,groupID,taskID,startTime,endTime, successTimes,"
					+ "failedTimes,warnedTimes,totalTimes,totalExecTime,maxExecTime,minExecTime," + "avgExecTime,LastExceTime " + ") values (?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?)");
			String scheId = logAbridgement.getScheID() == null ? "" : logAbridgement.getScheID();
			ps.setString(1, scheId);
			String groupId = logAbridgement.getGroupId() == null ? "" : logAbridgement.getGroupId();
			ps.setString(2, groupId);
			String taskId = logAbridgement.getTaskId() == null ? "" : logAbridgement.getTaskId();
			ps.setString(3, taskId);
			String startTime = logAbridgement.getStartTime() == null ? "" : logAbridgement.getStartTime();
			ps.setString(4, startTime);
			String endTime = logAbridgement.getEndTime() == null ? "" : logAbridgement.getEndTime();
			ps.setString(5, endTime);
			long successTimes = logAbridgement.getSuccessTimes() == null ? 0l : logAbridgement.getSuccessTimes();
			ps.setString(6, String.valueOf(successTimes));
			long failedTimes = logAbridgement.getFailedTimes() == null ? 0l : logAbridgement.getFailedTimes();
			ps.setString(7, String.valueOf(failedTimes));
			long warnedTimes = logAbridgement.getWarnedTimes() == null ? 0l : logAbridgement.getWarnedTimes();
			ps.setString(8, String.valueOf(warnedTimes));
			long totalTimes = logAbridgement.getTotalTimes() == null ? 0l : logAbridgement.getTotalTimes();
			ps.setString(9, String.valueOf(totalTimes));
			long totalExecTime = logAbridgement.getTotalExecTime() == null ? 0l : logAbridgement.getTotalExecTime();
			ps.setString(10, String.valueOf(totalExecTime));
			long maxExecTime = logAbridgement.getMaxExecTime() == null ? 0l : logAbridgement.getMaxExecTime();
			ps.setString(11, String.valueOf(maxExecTime));
			long minExecTime = logAbridgement.getMinExecTime() == null ? 0l : logAbridgement.getMinExecTime();
			ps.setString(12, String.valueOf(minExecTime));
			double avgExecTime = logAbridgement.getAvgExecTime();
			ps.setString(13, String.valueOf(avgExecTime));

			String lastExecTime = logAbridgement.getLastExceTime() == null ? "" : logAbridgement.getLastExceTime();
			ps.setString(14, lastExecTime);
			// System.out.println("scheId:"+scheId);
			// System.out.println("groupId:"+groupId);
			// System.out.println("taskId:"+taskId);
			// System.out.println("startTime:"+startTime);
			// System.out.println("endTime:"+endTime);
			// System.out.println("successTimes:"+successTimes);
			// System.out.println("failedTimes:"+failedTimes);
			// System.out.println("warnedTimes:"+warnedTimes);
			// System.out.println("totalTimes:"+totalTimes);
			// System.out.println("totalExecTime:"+totalExecTime);
			// System.out.println("maxExecTime:"+maxExecTime);
			// System.out.println("minExecTime:"+minExecTime);
			// System.out.println("avgExecTime:"+avgExecTime);
			// System.out.println("lastExecTime:"+lastExecTime);
			ps.addBatch();
			ps.executeBatch();

		} catch (SQLException e) {
			Log.logError("新增任务执行日志摘要SQL错误:", e);
		} catch (Exception e) {
			Log.logError("新增任务执行日志摘要错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 保存所有高频日志摘要
	@SuppressWarnings("unchecked")
	public synchronized void addAllLogAbridgement() {
		Iterator<?> it = Log.MapLogAbridgementBatch.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, LogAbridgement> entry = (Entry<String, LogAbridgement>) it.next();
			LogAbridgement logAbridgement = entry.getValue();
			LogAbridgementDao.getInstance().addLogAbridgement(logAbridgement);
		}
	}

	// 清空高频日志摘要
	public void clearLogAbridgement() {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from   " + AppCon.TN_ExecLogAbridgement);
			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError("清空任务日志摘要表SQL错误:", e);
		} catch (Exception e) {
			Log.logError("清空任务日志摘要表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

	// 查询来自数据库或者当前内存的日志摘要
	public Vector<?> getLogAbridgementVector(Map<String, String> map, Map<String, LogAbridgement> map_LogAbridgement) {
		Statement sm = null;
		ResultSet rs = null;
		Vector<Vector<String>> execLongAbridgementVector = new Vector<Vector<String>>();
		if (map == null)
			return execLongAbridgementVector;
		try {
			if (map.get("查询类型") == Const.LogAbridgementType[0]) {
				Set<Map.Entry<String, LogAbridgement>> set = map_LogAbridgement.entrySet();
				for (Iterator<Map.Entry<String, LogAbridgement>> it = set.iterator(); it.hasNext();) {
					Map.Entry<String, LogAbridgement> entry = (Map.Entry<String, LogAbridgement>) it.next();
					LogAbridgement log = entry.getValue();
					String scheid = log.getScheID();

					String scheName = ScherDao.getInstance().getScheNameFromScheCode(scheid);
					String groupid = log.getGroupId();
					String groupName = TaskGroupDao.getInstance().getGroupNameFromGroupId(groupid);

					String starttime = log.getStartTime();
					String endtime = log.getEndTime();
					String successTimes = String.valueOf(log.getSuccessTimes() == null ? 0l : log.getSuccessTimes());
					String failedTimes = String.valueOf(log.getFailedTimes() == null ? 0l : log.getFailedTimes());
					String warnedTimes = String.valueOf(log.getWarnedTimes() == null ? 0l : log.getWarnedTimes());
					String totalTimes = String.valueOf(log.getTotalTimes() == null ? 0l : log.getTotalTimes());
					String totalExecTime = String.valueOf(log.getTotalExecTime() == null ? 0l : log.getTotalExecTime());
					String maxExecTime = String.valueOf(log.getMaxExecTime() == null ? 0l : log.getMaxExecTime());
					String minExecTime = String.valueOf(log.getMinExecTime() == null ? 0l : log.getMinExecTime());
					String avgExecTime = String.valueOf(log.getAvgExecTime());
					String LastExceTime = log.getLastExceTime();
					Vector<String> rowValue = new Vector<String>();
					rowValue.add(scheid);
					rowValue.add(scheName);
					rowValue.add(starttime);
					rowValue.add(endtime);
					rowValue.add(failedTimes);
					rowValue.add(warnedTimes);
					rowValue.add(successTimes);
					rowValue.add(totalTimes);
					rowValue.add(totalExecTime);
					rowValue.add(avgExecTime);
					rowValue.add(maxExecTime);
					rowValue.add(minExecTime);
					rowValue.add(LastExceTime);
					rowValue.add(groupid);
					rowValue.add(groupName);
					String taskId = UtilString.isNil(log.getTaskId());
					if (!"".equals(taskId)) {
						ITask task = TaskDao.getInstance().getMapTask(Long.valueOf(taskId));
						rowValue.add(taskId);
						rowValue.add(task.getTaskName());
						rowValue.add(task.getTaskType());
					} else {
						rowValue.add("");
						rowValue.add("");
						rowValue.add("");
					}

					execLongAbridgementVector.add(rowValue);
				}
			}

			if (map.get("查询类型") == Const.LogAbridgementType[1]) {
				sm = con.createStatement();
				String sql = "select ";

				if (AppCon.DbconApp.getDbType().toLowerCase().indexOf("access") >= 0) {
					if (map.get("查询记录数") != null && map.get("查询记录数").trim().length() > 0)
						sql = sql + "  top " + Long.valueOf(map.get("查询记录数"));

					sql = sql + "   iif(a.scheId='-1','手工',a.scheId) as scheId,b.schNme,a.groupID,c.groupName, a.taskID,d.taskName,d.taskType,a.startTime,a.endTime, a.successTimes,"
							+ "a.failedTimes,a.warnedTimes,a.totalTimes,a.totalExecTime,a.maxExecTime,a.minExecTime,a.avgExecTime,a.LastExceTime " + " from ((( " + AppCon.TN_ExecLogAbridgement
							+ " as a left join  " + AppCon.TN_ScheParam + "   as b on trim(a.scheId)= trim(str(b.schCde))) " + " left join   " + AppCon.TN_Taskgroup
							+ "  as c on trim(a.groupId)=trim(str(c.groupId))) " + " left join  " + AppCon.TN_Task + "  as d on trim(a.taskid)=trim(str(d.taskid))) where 1=1";
					if (map.get("开始日期") != null && map.get("开始日期").trim().length() > 0)
						sql = sql + " and dates>=Format('" + map.get("开始日期") + " ','yyyymmdd')  ";
					if (map.get("结束日期") != null && map.get("结束日期").trim().length() > 0)
						sql = sql + " and dates<=Format('" + map.get("结束日期") + " ','yyyymmdd')  ";
				} else {

					sql = sql + "  a.scheId scheId ,b.schNme,a.groupID,c.groupName, a.taskID,d.taskName,d.taskType,a.startTime,a.endTime, a.successTimes,"
							+ "a.failedTimes,a.warnedTimes,a.totalTimes,a.totalExecTime,a.maxExecTime,a.minExecTime,a.avgExecTime,a.LastExceTime " + " from ((( " + AppCon.TN_ExecLogAbridgement
							+ "   a left join  " + AppCon.TN_ScheParam + "     b on trim(a.scheId)= ((b.schCde))) " + " left join   " + AppCon.TN_Taskgroup
							+ "    c on trim(a.groupId)=((c.groupId))) " + " left join  " + AppCon.TN_Task + "    d on (a.taskid)=((d.taskid))) where 1=1";
					if (map.get("开始日期") != null && map.get("开始日期").trim().length() > 0)
						sql = sql + " and DATE_FORMAT(dateTime,'%Y%m%d')>='" + map.get("开始日期") + "'";
					if (map.get("结束日期") != null && map.get("结束日期").trim().length() > 0)
						sql = sql + " and DATE_FORMAT(dateTime,'%Y%m%d')<='" + map.get("结束日期") + "'";
					if (map.get("查询记录数") != null && map.get("查询记录数").trim().length() > 0)
						sql = sql + "  and rownum<= " + Long.valueOf(map.get("查询记录数"));
				}

				if (map.get("调度名称") != null && map.get("调度名称").trim().length() > 0)
					sql = sql + " and schNme  like ('%" + map.get("调度名称") + "%')  ";
				if (map.get("任务名称") != null && map.get("任务名称").trim().length() > 0)
					sql = sql + " and taskName like ('%" + map.get("任务名称") + "%')  ";
				if (map.get("任务组名称") != null && map.get("任务组名称").trim().length() > 0)
					sql = sql + " and groupName like ('%" + map.get("任务组名称") + "%')  ";
				if (map.get("任务类型") != null && map.get("任务类型").trim().length() > 0)
					sql = sql + " and d.taskType like ('%" + map.get("任务类型") + "%')  ";

				sql = sql + " order by a.dateTime desc";
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
					String successTimes = rs.getString("successTimes");
					String failedTimes = rs.getString("failedTimes");
					String warnedTimes = rs.getString("warnedTimes");
					String totalTimes = rs.getString("totalTimes");
					String totalExecTime = rs.getString("totalExecTime");
					String maxExecTime = rs.getString("maxExecTime");
					String minExecTime = rs.getString("minExecTime");
					String avgExecTime = rs.getString("avgExecTime");
					String LastExceTime = rs.getString("LastExceTime");

					Vector<String> rowValue = new Vector<String>();
					rowValue.add(scheid);
					rowValue.add(scheName);
					rowValue.add(starttime);
					rowValue.add(endtime);
					rowValue.add(failedTimes);
					rowValue.add(warnedTimes);
					rowValue.add(successTimes);
					rowValue.add(totalTimes);
					rowValue.add(totalExecTime);
					rowValue.add(avgExecTime);
					rowValue.add(maxExecTime);
					rowValue.add(minExecTime);
					rowValue.add(LastExceTime);
					rowValue.add(groupid);
					rowValue.add(groupName);
					rowValue.add(taskid);
					rowValue.add(taskName);
					rowValue.add(tasktype);
					execLongAbridgementVector.add(rowValue);
				}
			}
		} catch (SQLException e) {
			Log.logError("获取执行结果表格参数SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取执行结果表格参数错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return execLongAbridgementVector;
	}
}
