package com.taskgroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import app.AppCon;

import com.log.Log;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import consts.Const;

public class TaskGroupDao {
	public static TaskGroupDao taskGroupdao = null;
	private Connection con;

	public static TaskGroupDao getInstance() {
		if (taskGroupdao == null)
			taskGroupdao = new TaskGroupDao();
		return taskGroupdao;
	}

	// 构造
	public TaskGroupDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 根据任务组id获取任务组名称
	public String getGroupNameFromGroupId(String groupId) {
		Statement sm = null;
		ResultSet rs = null;
		String result = "";
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			Long gId = 0l;
			try {
				gId = Long.valueOf(groupId);
			} catch (Exception e) {
				return "";
			}

			rs = sm.executeQuery("select groupName From  " + AppCon.TN_Taskgroup + "   where groupId= " + gId);
			rs.last();
			if (rs.getRow() > 0)
				result = rs.getString("groupName");
		} catch (SQLException e) {
			Log.logError("查询当前日志摘要时，获取任务组名称SQL错误:", e);
		} catch (Exception e) {
			Log.logError("查询当前日志摘要时，获取任务组名称错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;

	}

	// 根据任务组id获取任务实例
	public TaskGroup getTaskGroupFromGroupID(Long groupId) {
		Statement sm = null;
		ResultSet rs = null;
		TaskGroup taskGroup = new TaskGroup();
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_Taskgroup + "   where groupid=" + groupId);
			rs.last();
			int rcount = rs.getRow();
			if (rcount > 0) {
				taskGroup.setGroupId(Long.valueOf(rs.getString("groupid")));
				taskGroup.setGroupName(rs.getString("groupName"));
				taskGroup.setGroupMemo(rs.getString("groupMemo"));
				taskGroup.setErrorDeal(rs.getInt("ErrorDeal"));
				taskGroup.setExecType(rs.getInt("execType"));
				taskGroup.setGroupOrder(rs.getString("groupOrder"));
			}
		} catch (SQLException e) {
			Log.logError("根据任务组ID获取任务组SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务组ID获取任务组错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return taskGroup;
	}

	// 获取任务组实例 List
	public List<TaskGroup> getTaskGroupList() {
		Statement sm = null;
		ResultSet rs = null;
		List<TaskGroup> taskGroupList = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_Taskgroup + "   order by groupOrder");
			taskGroupList = new LinkedList<TaskGroup>();
			while (rs.next()) {
				TaskGroup taskGroup = new TaskGroup();
				taskGroup.setGroupId(new Long(rs.getInt("groupid")));
				taskGroup.setGroupName(rs.getString("groupname"));
				taskGroup.setGroupMemo(rs.getString("groupMemo"));
				taskGroup.setErrorDeal(rs.getInt("ErrorDeal"));
				taskGroup.setExecType(rs.getInt("execType"));
				taskGroup.setGroupOrder(rs.getString("groupOrder"));
				taskGroupList.add(taskGroup);
			}
		} catch (SQLException e) {
			Log.logError("获取任务组列表SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取任务组列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return taskGroupList;
	}

	// 获取任务组实例 Array
	public String[] getTaskGroup() {
		Statement sm = null;
		ResultSet rs = null;
		String[] taskGroupList = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_Taskgroup + "   order by groupOrder");
			rs.last();
			taskGroupList = new String[rs.getRow()];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				taskGroupList[i] = rs.getString("groupname");
				i = i + 1;
			}
		} catch (SQLException e) {
			Log.logError("获取任务组字符串列表SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取任务组字符串列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return taskGroupList;
	}

	// 添加任务组
	public void addTaskGroup(TaskGroup taskGroup) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String taskGroupName = taskGroup.getGroupName() == null ? "" : taskGroup.getGroupName();
			String taskGroupMemo = taskGroup.getGroupMemo() == null ? "" : taskGroup.getGroupMemo();
			ps = con.prepareStatement("insert into  " + AppCon.TN_Taskgroup + "  " + "(groupid,groupName,groupMemo,errorDeal,execType,groupOrder,state) " + "values (?,?,?,?,?,?,0)");
			ps.setString(1, String.valueOf(taskGroup.getGroupId()));
			ps.setString(2, taskGroupName);
			ps.setString(3, taskGroupMemo);
			ps.setInt(4, taskGroup.getErrorDeal());
			ps.setInt(5, taskGroup.getExecType());
			ps.setString(6, taskGroup.getGroupOrder());
			ps.addBatch();
			ps.executeBatch();
			con.commit();
		} catch (SQLException e) {
			Log.logError("新增任务组SQL错误:", e);
		} catch (Exception e) {
			Log.logError("新增任务组错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 修改任务组
	public void modTaskGroup(TaskGroup taskGroup) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("update  " + AppCon.TN_Taskgroup + "   set " + "groupName=?,groupMemo=?,errorDeal=?,execType=?,groupOrder=? " + " where groupid=" + taskGroup.getGroupId());

			ps.setString(1, taskGroup.getGroupName());
			ps.setString(2, taskGroup.getGroupMemo());
			ps.setInt(3, taskGroup.getErrorDeal());
			ps.setInt(4, taskGroup.getExecType());
			ps.setString(5, taskGroup.getGroupOrder());
			ps.addBatch();
			ps.executeBatch();
			con.commit();
		} catch (SQLException e) {
			Log.logError("修改任务组SQL错误:", e);
		} catch (Exception e) {
			Log.logError("修改任务组错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 删除任务组
	public void delTaskGroup(Long GroupId) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from  " + AppCon.TN_Taskgroup + "   where groupid=" + GroupId);
			sm.addBatch("delete from  " + AppCon.TN_TaskGroupDetail + "   where groupid=" + GroupId);
			sm.executeBatch();
			con.commit();
		} catch (SQLException e) {
			Log.logError("删除任务组SQL错误:", e);
		} catch (Exception e) {
			Log.logError("删除任务组错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

	// 根据参数获取任务表格数组
	public Vector<?> getTaskGroupVector(Map<String, String> map) {
		Statement sm = null;
		ResultSet rs = null;
		Vector<Vector<Object>> taskGroupVector = new Vector<Vector<Object>>();
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String vsql = null;
			if (map == null || map.get("Type").equals("全部")) {
				vsql = "select * from  " + AppCon.TN_Taskgroup + "  where state=0  order by groupOrder";
			} else if (map.get("Type").equals("任务组过滤")) {
				vsql = "select * from  " + AppCon.TN_Taskgroup + "    where 1=1  ";
				if (map.get("任务组名称").length() > 0) {
					vsql = vsql + " and groupName  like ('%" + map.get("任务组名称") + "%')";
				}
				if (!map.get("状态").equalsIgnoreCase("")) {
					vsql = vsql + " and state ="+ map.get("状态");
				}
				vsql = vsql + " order by groupOrder ";
			}
			rs = sm.executeQuery(vsql);

			while (rs.next()) {
				Vector<Object> rowValue = new Vector<Object>();
				rowValue.add(false);
				rowValue.add(rs.getString("groupOrder") == null ? "" : rs.getString("groupOrder"));
				rowValue.add(rs.getString("groupID"));
				rowValue.add(rs.getString("groupName"));
				rowValue.add(Const.TASK_Group_ErrorType[rs.getInt("errorDeal")]);
				rowValue.add(Const.TASK_Group_ExecType[rs.getInt("execType")]);
				rowValue.add(rs.getString("groupMemo"));
				taskGroupVector.add(rowValue);
			}
		} catch (SQLException e) {
			Log.logError("获取任务组表格参数SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取任务组表格参数错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return taskGroupVector;
	}

	// 获取最大任务ID
	public long getMaxGroupID() {
		long maxID = 0;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select max(GroupId) as maxID from  " + AppCon.TN_Taskgroup + "   ");
			rs.last();
			if (rs.getRow() > 0)
				maxID = rs.getLong("maxID");
		} catch (SQLException e) {
			Log.logError("获取最大任务ID执行SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取最大任务ID错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return maxID;
	}

	// 获取最大任务任务组顺序号
	public int getMaxOrderLen() {
		int len = 0;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			try {
				rs = sm.executeQuery("select max(len(groupOrder)) as len from  " + AppCon.TN_Taskgroup);
			} catch (Exception e) {
			}
			rs.last();
			if (rs.getRow() > 0)
				len = rs.getInt("len");
		} catch (SQLException e) {
			Log.logError("获取最大任务Order执行SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取最大任务Order错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return len;
	}

	// 修改任务组顺序号
	public void modOrder(long groupId, String groupOrder) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("update  " + AppCon.TN_Taskgroup + "   set  groupOrder=? where groupId=" + groupId);
			ps.setString(1, groupOrder);
			ps.addBatch();
			ps.executeBatch();
			ps.clearBatch();
		} catch (SQLException e) {
			Log.logError("修改任务组Order错误:", e);
		} finally {
			UtilSql.close(ps);
		}
	}
	
	// 修改任务状态
	public void modState(long groupId, int state) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("update  " + AppCon.TN_Taskgroup + "   set  state=? where groupId=" + groupId);
			ps.setInt(1, state);
			ps.addBatch();
			ps.executeBatch();
			ps.clearBatch();
		} catch (SQLException e) {
			Log.logError("修改任务Order错误:", e);
		} finally {
			UtilSql.close(ps);
		}
	}
}
