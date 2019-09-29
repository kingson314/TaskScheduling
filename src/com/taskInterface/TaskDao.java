package com.taskInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import app.AppCon;

import com.log.Log;
import com.taskClass.TaskClass;
import com.taskClass.TaskClassImp;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;

public class TaskDao {

	public static TaskDao dao = null;
	private static ConcurrentHashMap<Long, ITask> MapTask = new ConcurrentHashMap<Long, ITask>();

	public static TaskDao getInstance() {
		if (dao == null)
			dao = new TaskDao();
		return dao;
	}

	protected Connection con;

	// 构造
	private TaskDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 根据Id获取任务实例
	public synchronized ITask getMapTask(Long taskId) {
		ITask task = MapTask.get(taskId);
		if (task == null) {
			task = getTaskFromTaskID(taskId);
			MapTask.put(taskId, task);
		}
		return task;
	}

	// 根据任务id获取任务对象
	private ITask getTaskFromTaskID(Long taskId) {
		Statement sm = null;
		ResultSet rs = null;
		ITask task = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_Task + "   where taskid=" + taskId);
			rs.last();
			String taskType = rs.getString("taskType");
			TaskClass taskClass = TaskClassImp.mapTaskClass.get(taskType);
			task = TaskClassImp.getTask(taskClass);

			task.setTaskOrder(rs.getString("taskOrder") == null ? "" : rs.getString("taskOrder"));
			task.setTaskId(Long.valueOf(rs.getInt("taskid")));
			task.setTaskName(rs.getString("taskName") == null ? "" : rs.getString("taskName"));
			task.setTaskType(taskType);
			task.setTaskMemo(rs.getString("taskMemo") == null ? "" : rs.getString("taskMemo"));
			task.setOverTime(Long.valueOf(Long.valueOf(rs.getString("overTime") == null ? "0" : rs.getString("overTime"))));
			task.setMonitorGroup(rs.getString("monitorGroup") == null ? "" : rs.getString("monitorGroup"));
			task.setInterval(Long.valueOf(rs.getString("Interval") == null ? "0" : rs.getString("Interval")));
			task.setWarnType(rs.getString("warnType") == null ? "" : rs.getString("warnType"));
			task.setLogType(rs.getString("logType") == null ? "" : rs.getString("logType"));
			task.setJsonStr(rs.getString("jsonStr") == null ? "" : rs.getString("jsonStr"));

		} catch (SQLException e) {
			Log.logError("根据任务ID获取任务SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务ID获取任务错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return task;
	}

	// 新增任务
	public void addTask(ITask task) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("insert into  " + AppCon.TN_Task + "  (taskID,taskName,taskType,taskMemo,MonitorGroup,`Interval`,logType,taskOrder,overTime,warnType,jsonStr,state) "
					+ " values(?,?,?,?,?,?,?,?,?,?,?,0)");
			ps.setString(1, String.valueOf(task.getTaskId()));
			ps.setString(2, task.getTaskName());
			ps.setString(3, task.getTaskType());
			ps.setString(4, task.getTaskMemo());
			ps.setString(5, task.getMonitorGroup());
			ps.setString(6, String.valueOf(task.getInterval()));
			ps.setString(7, task.getLogType());
			ps.setString(8, task.getTaskOrder());
			ps.setString(9, String.valueOf(task.getOverTime()));
			ps.setString(10, String.valueOf(task.getWarnType()));
			ps.setString(11, task.getJsonStr());
			ps.addBatch();
			ps.executeBatch();
			ps.clearBatch();
		} catch (SQLException e) {
			Log.logError("新增任务SQL错误:", e);
		} catch (Exception e) {
			Log.logError("新增任务错误:", e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				Log.logError("新增任务回滚错误:", e);
			}
		} finally {
			MapTask.put(task.getTaskId(), getTaskFromTaskID(task.getTaskId()));
			UtilSql.close(ps);
		}
	}

	// 修改任务
	public void modTask(ITask task) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("update  " + AppCon.TN_Task + "   set " + " taskName=?,taskType=?,taskMemo=?,MonitorGroup=?,`Interval`=?,"
					+ "logType=?,taskOrder=?,overTime=?,warnType=? ,jsonStr=? " + " where taskId=" + task.getTaskId());
			ps.setString(1, task.getTaskName());
			ps.setString(2, task.getTaskType());
			ps.setString(3, task.getTaskMemo());
			ps.setString(4, task.getMonitorGroup());
			ps.setString(5, String.valueOf(task.getInterval()));
			ps.setString(6, task.getLogType());
			ps.setString(7, task.getTaskOrder());
			ps.setString(8, String.valueOf(task.getOverTime()));
			ps.setString(9, String.valueOf(task.getWarnType()));
			ps.setString(10, task.getJsonStr());
			ps.addBatch();
			ps.executeBatch();
			ps.clearBatch();
		} catch (SQLException e) {
			Log.logError("修改任务错误:", e);
		} finally {
			MapTask.put(task.getTaskId(), task);
			UtilSql.close(ps);
		}
	}

	// 删除任务
	public void delTask(Long taskId) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("delete from  " + AppCon.TN_Task + "   where taskid=" + taskId);
			ps.addBatch();
			ps.executeBatch();
		} catch (SQLException e) {
			Log.logError("删除任务" + taskId + "SQL错误:", e);
		} catch (Exception e) {
			Log.logError("删除任务" + taskId + "错误:", e);
		} finally {
			MapTask.remove(taskId);
			UtilSql.close(ps);
		}

	}

	// key 用于任务组 任务查询
	@SuppressWarnings("rawtypes")
	public Vector<?> getTaskVector(Map<String, String> map) {
		Statement sm = null;
		ResultSet rs = null;
		Vector<Vector<Comparable>> taskVector = new Vector<Vector<Comparable>>();
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (map.get("Type").equalsIgnoreCase("全部"))
				rs = sm.executeQuery("select * from  " + AppCon.TN_Task + "  where state=0 order by taskorder");
			else if (map.get("Type").equalsIgnoreCase("任务组任务")) {
				String vsql = "select ts.*,tgd.groupId,tgd.taskOrder as torder " + " from  " + AppCon.TN_Task + "   ts, " + AppCon.TN_TaskGroupDetail + "   tgd " + "where ts.taskid=tgd.taskid"
						+ " and tgd.groupID=" + map.get("GroupId") + " order by tgd.taskOrder";
				rs = sm.executeQuery(vsql);
			} else if (map.get("Type").equalsIgnoreCase("任务过滤")) {
				String vsql = "select * from  " + AppCon.TN_Task + " where 1=1 ";
				if (!map.get("任务类型").equalsIgnoreCase("")) {
					vsql = vsql + " and taskType='" + map.get("任务类型") + "'";
				}
				if (!map.get("任务名称").equalsIgnoreCase("")) {
					vsql = vsql + " and taskName like ('%" + map.get("任务名称") + "%') ";
				}
				
				if (!map.get("状态").equalsIgnoreCase("")) {
					vsql = vsql + " and state ="+ map.get("状态");
				}
				vsql = vsql + " order by taskorder";
				rs = sm.executeQuery(vsql);
			}
			while (rs.next()) {
				Vector<Comparable> rowValue = new Vector<Comparable>();
				rowValue.add(false);
				rowValue.add(rs.getString("taskOrder") == null ? "" : rs.getString("taskOrder"));
				rowValue.add(rs.getString("taskid"));
				rowValue.add(rs.getString("taskName") == null ? "" : rs.getString("taskName"));
				rowValue.add(rs.getString("taskType") == null ? "" : rs.getString("taskType"));

				rowValue.add(rs.getString("logType") == null ? "" : rs.getString("logType"));
				rowValue.add(rs.getString("monitorGroup") == null ? "" : rs.getString("monitorGroup"));
				rowValue.add(rs.getString("Interval") == null ? "0" : rs.getString("Interval"));
				rowValue.add(rs.getString("warnType") == null ? "" : rs.getString("warnType"));
				rowValue.add(rs.getString("overTime") == null ? "" : rs.getString("overTime"));
				rowValue.add(rs.getString("taskMemo") == null ? "" : rs.getString("taskMemo"));
				if (map.get("Type").equalsIgnoreCase("任务组任务")) {
					rowValue.add(rs.getString("torder") == null ? "" : rs.getString("torder"));
				} else {
					rowValue.add("");
				}
				taskVector.add(rowValue);
			}
		} catch (SQLException e) {
			Log.logError("获取任务表格参数SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取任务表格参数错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return taskVector;
	}

	// 根据任务Id获取任务Id
	public Long[] GetTaskIdFromGroupId(Long GroupId) {
		Statement sm = null;
		ResultSet rs = null;
		Long[] taskId = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select taskid from  " + AppCon.TN_TaskGroupDetail + "   where GroupId=" + GroupId + " order by taskorder");
			rs.last();
			int len = rs.getRow();
			taskId = new Long[len];
			rs.beforeFirst();
			int index = 0;
			while (rs.next()) {
				taskId[index] = rs.getLong("taskID");
				index += 1;
			}
		} catch (SQLException e) {
			Log.logError("根据任务组ID获取任务Id执行SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务组ID获取任务Id错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return taskId;
	}

	// 根据任务组id获取任务id以及任务顺序号
	public Long[][] GetTaskIdTaskOrderFromGroupId(Long GroupId) {
		Statement sm = null;
		ResultSet rs = null;
		Long[][] taskIdOrder = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select taskid,taskorder from  " + AppCon.TN_TaskGroupDetail + "   where GroupId=" + GroupId + " order by taskorder");
			rs.last();
			int len = rs.getRow();
			taskIdOrder = new Long[len][2];
			rs.beforeFirst();
			int index = 0;
			while (rs.next()) {
				taskIdOrder[index][0] = rs.getLong("taskID");
				taskIdOrder[index][1] = rs.getLong("taskorder");
				index += 1;
			}
		} catch (SQLException e) {
			Log.logError("根据任务组ID与任务顺序获取任务Id执行SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据任务组ID与任务顺序获取任务Id错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return taskIdOrder;
	}

	// 判断是否存在该类型的任务
	public boolean isExsitTask(String taskType) {
		boolean result = false;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select taskid from  " + AppCon.TN_Task + "   where taskTyp='" + taskType + "'");
			rs.last();
			int len = rs.getRow();
			if (len > 0)
				result = true;
		} catch (SQLException e) {
			Log.logError("判断行情监控任务是否存在SQL错误:", e);
		} catch (Exception e) {
			Log.logError("判断行情监控任务是否存在错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 判断该任务组中是否存在使用基金代码作为参数
	public boolean IsHasFundCode(Long[] groupID) {
		boolean result = false;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String groupid = "";
			for (int i = 0; i < groupID.length; i++) {
				groupid = groupID[i] + ",";
			}
			groupid = groupid.substring(0, groupid.length() - 1);
			rs = sm.executeQuery("select taskType from  " + AppCon.TN_Task + "   where taskid in" + "(SELECT taskid from  " + AppCon.TN_TaskGroupDetail + "   where groupid in(" + groupid + ")) "
					+ "and taskType='存储过程'");
			rs.last();
			int len = rs.getRow();
			if (len > 0)
				result = true;
		} catch (SQLException e) {
			Log.logError("判断任务组中是否存在存储过程任务SQL错误:", e);
		} catch (Exception e) {
			Log.logError("判断任务组中是否存在存储过程任务错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 获取最大任务ID
	public long getMaxTaskID() {
		long maxID = 0;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select max(taskid) as maxID from  " + AppCon.TN_Task + "   ");
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

	// 获取最大任务任务顺序号
	public int getMaxOrderLen() {
		int len = 0;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			try {
				rs = sm.executeQuery("select max(length(taskOrder)) as len from  " + AppCon.TN_Task);
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

	// 根据调度id获取任务id
	public String getTaskIdFromSchCde(Long scheCde) {
		String result = "";
		Statement sm = null;
		ResultSet rs = null;

		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select taskid from  " + AppCon.TN_ScheParam + "   where schCde=" + scheCde);
			rs.last();
			result = rs.getString(1);
		} catch (SQLException e) {
			Log.logError("根据调度编码获取任务编码SQL错误:", e);
		} catch (Exception e) {
			Log.logError("根据调度编码获取任务编码错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}

	// 获取任务名称
	public String[] GetTaskName() {
		String[] taskName = null;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select taskName from  " + AppCon.TN_Task + "   order by taskorder");
			rs.last();
			int len = rs.getRow();
			if (len > 0)
				taskName = new String[len + 1];
			taskName[0] = "";
			int i = 1;
			rs.beforeFirst();
			while (rs.next()) {
				taskName[i] = rs.getString("taskName");
				i += 1;
			}

		} catch (SQLException e) {
			Log.logError("获取任务名称列表SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取任务名称列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}

		return taskName;
	}

	// 根据任务id获取任务名称以及任务类型
	public String[] getTaskName_TypeFromTaskID(Long taskID) {
		Statement sm = null;
		ResultSet rs = null;
		String[] result = new String[2];
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			rs = sm.executeQuery("select taskName,taskType from  " + AppCon.TN_Task + "   where taskID=" + taskID);
			rs.last();
			if (rs.getRow() > 0) {
				result[0] = rs.getString("taskName") == null ? "" : rs.getString("taskName");
				result[1] = rs.getString("taskType") == null ? "" : rs.getString("taskType");
			}
		} catch (SQLException e) {
			Log.logError("查询当前日志摘要时，获取任务名称，任务类型SQL错误:", e);
		} catch (Exception e) {
			Log.logError("查询当前日志摘要时，获取任务名称，任务类型错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;

	}

	// 修改任务顺序号
	public void modOrder(long taskId, String taskOrder) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("update  " + AppCon.TN_Task + "   set  taskOrder=? where taskId=" + taskId);
			ps.setString(1, taskOrder);
			ps.addBatch();
			ps.executeBatch();
			ps.clearBatch();
		} catch (SQLException e) {
			Log.logError("修改任务Order错误:", e);
		} finally {
			UtilSql.close(ps);
		}
	}
	
	// 修改任务状态
	public void modState(long taskId, int state) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("update  " + AppCon.TN_Task + "   set  state=? where taskId=" + taskId);
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
