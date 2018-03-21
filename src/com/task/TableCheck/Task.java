package com.task.TableCheck;

import java.sql.Connection;
import java.sql.SQLException;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.taskInterface.TaskAbstract;

import common.util.Math.UtilMath;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;

public class Task extends TaskAbstract {

	public void fireTask() {
		Connection con = null;
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			DbConnection dbConnSrc = DbConnectionDao.getInstance().getMapDbConn(bean.getTcDbName());
			if (dbConnSrc == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取数据表检查数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbConnSrc);

			if (bean.getTcSQL().length() < 1) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("Sql为空");
				return;
			}
			// 查询表记录
			long rownum = UtilSql.queryForCount(con, bean.getTcSQL(), UtilString.getSqlRuleParam(bean.getTcParamsRule()));

			// 记录数限制提示
			if (bean.getTcIfNumLimitedWarn()) {
				String numLimitedWarning = bean.getTcNumLimitedWarning().trim();
				String beginWarning = "[表记录数为: " + String.valueOf(rownum) + " ;";
				String compareType = null;
				String warning = "指定记录数: " + String.valueOf(bean.getTcnumLimited() + "]");

				if (UtilMath.compare(bean.getCompareType(), rownum, bean.getTcnumLimited()) == true) {
					compareType = bean.getCompareType();
				}
				if (compareType != null) {
					this.setTaskStatus("执行提示");
					this.setTaskMsg(numLimitedWarning + " " + beginWarning + compareType + warning);
				} else {
					this.setTaskStatus("执行成功");
					this.setTaskMsg("");
				}
			} else if (bean.getTcIfNumWarn()) {// 记录数提示
				String numWarning = bean.getTcNumWarning();
				String warning = "[表记录数为: " + String.valueOf(rownum) + "]";
				this.setTaskStatus("执行提示");
				this.setTaskMsg(numWarning + " " + warning);
			}
		} catch (SQLException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("执行SQL错误:", e);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("数据表检查错误:", e);
		} finally {
			UtilSql.close(con);
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}
}
