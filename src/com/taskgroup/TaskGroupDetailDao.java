package com.taskgroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import app.AppCon;

import com.log.Log;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.string.UtilString;

public class TaskGroupDetailDao {
	public static TaskGroupDetailDao taskGroupDetailDao = null;
	private Connection con;

	public static TaskGroupDetailDao getInstance() {
		if (taskGroupDetailDao == null)
			taskGroupDetailDao = new TaskGroupDetailDao();
		return taskGroupDetailDao;
	}

	// 构造
	public TaskGroupDetailDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 获取下一个任务顺序号
	private Long GetNextOrder(Long GroupID, Long taskOrder) {
		Statement sm = null;
		ResultSet rs = null;
		Long result = -1l;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select min(taskOrder) as to from  " + AppCon.TN_TaskGroupDetail + "  " + " where groupId=" + GroupID + " and taskOrder> " + taskOrder);
			rs.last();
			if (rs.getRow() > 0)
				result = rs.getLong("to");
		} catch (SQLException e) {
			Log.logError("根据任务组中本任务的下一个任务顺序SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务组中本任务的下一个任务顺序错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 获取上一个任务顺序号
	private Long GetPirorOrder(Long GroupID, Long taskOrder) {
		Statement sm = null;
		ResultSet rs = null;
		Long result = -1l;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select max(taskOrder) as to from  " + AppCon.TN_TaskGroupDetail + "   " + " where groupId=" + GroupID + " and taskOrder< " + taskOrder);
			rs.last();
			if (rs.getRow() > 0)
				result = rs.getLong("to");
		} catch (SQLException e) {
			Log.logError("根据任务组中本任务的下一个任务顺序SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务组中本任务的下一个任务顺序错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 获取任务组明细标志
	private Long GetTaskGrouDetailId(Long GroupID, Long taskOrder) {
		Statement sm = null;
		ResultSet rs = null;
		Long result = -1l;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select id  from  " + AppCon.TN_TaskGroupDetail + "   where groupId=" + GroupID + " and taskOrder=" + taskOrder);
			rs.last();
			if (rs.getRow() > 0)
				result = rs.getLong("id");
		} catch (SQLException e) {
			Log.logError("根据任务组,任务顺序获取任务明细ID执行SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务组,任务顺序获取任务明细ID错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 上移任务组任务
	public void upTaskGroupDetail(Long GroupId, Long taskOrder) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			Long PirorOrder = GetPirorOrder(GroupId, taskOrder);
			Long id = GetTaskGrouDetailId(GroupId, taskOrder);
			Long pirorId = GetTaskGrouDetailId(GroupId, PirorOrder);
			if (PirorOrder == -1)
				return;
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("update  " + AppCon.TN_TaskGroupDetail + "   set taskOrder=" + taskOrder + " where id=" + pirorId);

			sm.addBatch("update  " + AppCon.TN_TaskGroupDetail + "   set taskOrder=" + PirorOrder + " where id=" + id);
			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError("上移任务组任务SQL错误:", e);
		} catch (Exception e) {
			Log.logError("上移任务组任务错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

	// 下移任务组任务
	public void downTaskGroupDetail(Long GroupId, Long taskOrder) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			Long NextOrder = GetNextOrder(GroupId, taskOrder);
			Long id = GetTaskGrouDetailId(GroupId, taskOrder);
			Long nextId = GetTaskGrouDetailId(GroupId, NextOrder);
			if (NextOrder == -1)
				return;
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			sm.addBatch("update  " + AppCon.TN_TaskGroupDetail + "   set taskOrder=" + taskOrder + " where id=" + nextId);

			sm.addBatch("update  " + AppCon.TN_TaskGroupDetail + "   set taskOrder=" + NextOrder + " where id=" + id);
			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError("下移任务组任务SQL错误:", e);
		} catch (Exception e) {
			Log.logError("下移任务组任务错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

	// 删除任务组任务
	public void delTaskGroupDetail(Long GroupId, Long[] taskOrder) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("delete from  " + AppCon.TN_TaskGroupDetail + "   " + "where Groupid=? and taskOrder=?");
			for (int i = 0; i < taskOrder.length; i++) {
				ps.setString(1, String.valueOf(GroupId));
				ps.setString(2, String.valueOf(taskOrder[i]));
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			Log.logError("删除任务组任务SQL错误:", e);
		} catch (Exception e) {
			Log.logError("删除任务组任务错误:", e);
		} finally {
			UtilSql.close(ps);
		}
	}

	// 删除任务组任务
	public void delTaskGroupDetailWhenDelTask(Long taskID) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("delete from  " + AppCon.TN_TaskGroupDetail + "   " + "where  taskid=?");
			ps.setString(1, String.valueOf(taskID));
			ps.addBatch();
			ps.executeBatch();
		} catch (SQLException e) {
			Log.logError("当删除任务时删除任务组任务SQL错误:", e);
		} catch (Exception e) {
			Log.logError("当删除任务时删除任务组任务错误:", e);
		} finally {
			UtilSql.close(ps);
		}
	}

	// 添加任务组任务
	public void addTaskGroupDetail(Long groupId, Long[] taskId) {
		try {
			String sql = "insert into  " + AppCon.TN_TaskGroupDetail + "  (Groupid,taskid,taskOrder,id)" + "values(?,?,?,?)";
			Long taskOrder = getTaskGroupDetailOrder(groupId) + 1;
			Long maxId = Long.valueOf(UtilString.isNil(UtilSql.QueryForMax(con, "select max(id)+1 from " + AppCon.TN_TaskGroupDetail, new Object[0]), "0"));
			List<List<Object>> paramsList = new ArrayList<List<Object>>();
			for (int i = 0; i < taskId.length; i++) {
				List<Object> params = new ArrayList<Object>();
				params.add(String.valueOf(groupId));
				params.add(String.valueOf(taskId[i]));
				params.add(String.valueOf(taskOrder + i));
				params.add(String.valueOf(maxId));
				maxId += 1;
				paramsList.add(params);
			}
			UtilSql.executeUpdateByListParam(con, sql, paramsList);
		} catch (SQLException e) {
			Log.logError("新增任务组明细SQL错误:", e);
		} catch (Exception e) {
			Log.logError("新增任务组明细错误:", e);
		}
	}

	// 根据任务获取任务组任务顺序号
	private Long getTaskGroupDetailOrder(Long GroupId) {
		Statement sm = null;
		ResultSet rs = null;
		Long result = 0l;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select max(taskOrder)as maxOrder from  " + AppCon.TN_TaskGroupDetail + "   where groupId=" + GroupId);
			rs.last();
			if (rs.getRow() > 0)
				result = rs.getLong("maxOrder");
		} catch (SQLException e) {
			Log.logError("根据任务获取任务组任务顺序号获取任务组SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务获取任务组任务顺序号获取任务组错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 根据任务组获取任务明细
	public TaskGroupDetail[] getTaskGroupDetail(Long GroupId) {
		Statement sm = null;
		ResultSet rs = null;
		TaskGroupDetail[] taskGroupDetail = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_TaskGroupDetail + "   where groupId=" + GroupId);
			rs.last();
			int rcnt = rs.getRow();
			if (rcnt == 0)
				return null;
			taskGroupDetail = new TaskGroupDetail[rcnt];
			int i = 0;
			rs.beforeFirst();
			while (rs.next()) {
				taskGroupDetail[i] = new TaskGroupDetail();
				taskGroupDetail[i].setId(rs.getLong("id"));
				taskGroupDetail[i].setTaskId(rs.getLong("taskid"));
				taskGroupDetail[i].setGroupId(rs.getLong("groupId"));
				taskGroupDetail[i].setTaskOrder(rs.getLong("taskOrder"));
				i = i + 1;
			}
		} catch (SQLException e) {
			Log.logError("根据任务组ID获取任务组SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务组ID获取任务组错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return taskGroupDetail;
	}
}
