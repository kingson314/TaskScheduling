package com.task.CustomQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.taskInterface.TaskAbstract;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;

public class Task extends TaskAbstract {
	// 用于输出表格需求；包括表头与表值
	private List<Object> list;

	public void fireTask() {
		Connection con = null;
		DbConnection dbconn = null;

		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			dbconn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbconn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbconn);
			// 执行返回记录数的SQL
			if ("记录数".equals(bean.getReturnType())) {
				long rownum = UtilSql.queryForCount(con, bean.getSql(), UtilString.getSqlRuleParam(bean.getRule()));
				this.setTaskStatus("执行成功");
				this.setTaskMsg("记录数为:" + rownum);
			} else if ("数值".equals(bean.getReturnType())) {// 只是更新、删除、插入sql
				long rownum = UtilSql.executeUpdate(con, bean.getSql(), UtilString.getSqlRuleParam(bean.getRule()));
				this.setTaskStatus("执行成功");
				this.setTaskMsg(this.getNowDate() + " 返回值为:" + rownum);
			} else if ("Vector".equals(bean.getReturnType())) {// 执行返回表格数据
				list = UtilSql.executeSqlAsObject(con, bean.getSql(), UtilString.getSqlRuleParam(bean.getRule()));
				// 设置为空，不打日志
				this.setTaskStatus("");
				this.setTaskMsg("返回值为:Vector");
			}
		} catch (SQLException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("执行SQL错误:", e);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("错误:", e);
		} finally {
			UtilSql.close(con);
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

	public List<Object> getList() {
		return list;
	}

}
