package com.task.DataUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.log.Log;
import com.taskInterface.TaskAbstract;

import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import consts.Const;

public class Task extends TaskAbstract {
	private Connection conSrc;
	private Connection conDst;

	public void fireTask() {
		if (this.getNowDate() == null) {
			Log.logInfo(this.getTaskName() + "获取t_sys_date 为NULL,任务取消，请检查t_sys_date表数据");
			return;
		}
		Bean bean = null;
		try {
			bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("解析JsonBean 错误", e);
			return;
		}
		try {

			// 1. 获得目标数据库连接
			DbConnection dbconnDst = DbConnectionDao.getInstance().getMapDbConn(bean.getDstDbName());
			if (dbconnDst == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取目标数据库连接错误");
				return;
			}
			// 2. 获得源数据库连接
			DbConnection dbconnSrc = DbConnectionDao.getInstance().getMapDbConn(bean.getSrcDbName());
			if (dbconnSrc == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取源数据库连接错误");
				return;
			}
			// 1. 获得目标数据库连接
			conDst = UtilJDBCManager.getConnection(dbconnDst);
			// 2. 获得源数据库连接
			conSrc = UtilJDBCManager.getConnection(dbconnSrc);

			if ("全表更新".equals(bean.getUpdateType())) {
				tableUpdate(bean);
			} else if ("增量更新".equals(bean.getUpdateType())) {
				increaseUpdate(bean);
			}
		} finally {
			UtilSql.close(conDst, conSrc);
		}
	}

	private void increaseUpdate(Bean bean) {
		// 增量的方式插入数据
		PreparedStatement psSrc = null;
		PreparedStatement psIncrease = null;
		PreparedStatement psDst = null;
		PreparedStatement psMax = null;

		ResultSet rsIncrease = null;
		ResultSet rsMax = null;
		ResultSet rsSrc = null;
		ResultSetMetaData rsmdSrc = null;
		int columnCount = 0;
		try {
			// 2. 查得结果集
			psDst = conDst.prepareStatement(bean.getDstSql().trim());
			// 3.0 事务控制
			conDst.setAutoCommit(false);

			// 3.1 如果全表插入
			int batchCount = 0;
			// 5. 以下为增量插入
			// 5.1 查得目标表最大的日期,记录下来

			psIncrease = conDst.prepareStatement(bean.getRuleSql().trim());
			psIncrease.setString(1, this.getNowDate());
			rsIncrease = psIncrease.executeQuery();

			String strMax = "";
			if (rsIncrease.next()) {
				strMax = rsIncrease.getString(1) == null ? "-1" : rsIncrease.getString(1);
			}

			psMax = conDst.prepareStatement(bean.getMaxValueSql().trim(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			psMax.setString(1, strMax);
			rsMax = psMax.executeQuery();

			psSrc = conSrc.prepareStatement(bean.getSrcSql().trim());
			psSrc.setString(1, this.getNowDate());
			psSrc.setString(2, strMax);
			rsSrc = psSrc.executeQuery();// 结果集
			rsmdSrc = rsSrc.getMetaData(); // 元数据
			columnCount = rsmdSrc.getColumnCount() - 1;

			String srcMaxValue = "";
			while (rsSrc.next()) {
				int flag = -1;
				// 3.1 如果记录时间等于最大值
				srcMaxValue = rsSrc.getString(columnCount + 1);
				flag = srcMaxValue.compareTo(strMax);
				// 3.2 查询结果大于最大值,插入
				if (flag != 0) {
					for (int i = 1; i <= columnCount; i++) {
						if (rsmdSrc.getColumnTypeName(i).toUpperCase().equals("DATE")) {
							psDst.setTimestamp(i, rsSrc.getTimestamp(i));
						} else {
							psDst.setString(i, rsSrc.getString(i));
						}
					}
					psDst.addBatch();
					batchCount++;
				} else if (flag == 0) {
					boolean insertFlag = true;
					while (rsMax.next()) {
						int k = 0;
						for (int i = 1; i <= columnCount; i++) {
							String s = rsSrc.getString(i) == null ? "" : rsSrc.getString(i).trim();
							String d = rsMax.getString(i) == null ? "" : rsMax.getString(i).trim();

							if (rsmdSrc.getColumnTypeName(i).equalsIgnoreCase("NUMBER")) {
								s = (double) Double.parseDouble("".equals(s) ? "0" : s) + "";
								d = (double) Double.parseDouble("".equals(d) ? "0" : s) + "";
							}

							if (rsmdSrc.getColumnTypeName(i).equalsIgnoreCase("DATE")) {
								s = UtilConver.dateToStr(rsSrc.getTimestamp(i), Const.fm_yyyyMMdd_HHmmss);
								d = UtilConver.dateToStr(rsMax.getTimestamp(i), Const.fm_yyyyMMdd_HHmmss);
							}

							if (s.equalsIgnoreCase(d)) {
								k++;
							}
						}

						if (k == columnCount) {
							insertFlag = false;
						}
					}
					if (insertFlag == true) {
						for (int h = 1; h <= columnCount; h++) {
							if (rsmdSrc.getColumnTypeName(h).toUpperCase().equals("DATE")) {
								psDst.setTimestamp(h, rsSrc.getTimestamp(h));
							} else {
								psDst.setString(h, rsSrc.getString(h));
							}
						}
						psDst.addBatch();
						batchCount++;
					}
				}
				if (batchCount % 100 == 0) {
					psDst.executeBatch();
					psDst.clearBatch();
				}
			}
			psDst.executeBatch();
			psDst.clearBatch();

			conDst.commit();
			this.setTaskStatus("执行成功");
			this.setTaskMsg("更新记录数:" + batchCount);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("数据同步错误:", e);
			// 异常回滚
			if (conDst != null) {
				try {
					conDst.rollback();
				} catch (SQLException e1) {
					this.setTaskStatus("执行失败");
					this.setTaskMsg(this.getTaskMsg() + "\n数据同步错误[回滚异常]:", e);
				}
			}
		} finally {
			UtilSql.close(rsSrc, rsMax, psSrc, psDst, psIncrease, psMax);
		}
	}

	private void tableUpdate(Bean bean) {
		PreparedStatement psSrc = null;
		PreparedStatement psDel = null;
		PreparedStatement psDst = null;

		ResultSet rsDst = null;
		ResultSet rsSrc = null;
		ResultSetMetaData rsmdSrc = null;

		int columnCount = 0;
		try {

			// 2. 查得结果集
			psDst = conDst.prepareStatement(bean.getDstSql().trim());
			// 3.0 事务控制
			conDst.setAutoCommit(false);
			// 3.1 如果全表插入
			int batchCount = 0;
			int j = 0;// 记录数累加器
			psSrc = conSrc.prepareStatement(bean.getSrcSql().trim());
			psSrc.setString(1, this.getNowDate());
			rsSrc = psSrc.executeQuery();// 结果集
			rsmdSrc = rsSrc.getMetaData(); // 元数据
			columnCount = rsmdSrc.getColumnCount();
			// 3.1 删除数据
			psDel = conDst.prepareStatement(bean.getRuleSql().trim());
			psDel.setString(1, this.getNowDate());
			psDel.addBatch();
			psDel.executeBatch();
			// 3.2 插入数据
			while (rsSrc.next()) {
				batchCount += 1;
				for (int i = 1; i <= columnCount; i++) {
					if (rsmdSrc.getColumnTypeName(i).toUpperCase().equals("DATE")) {
						psDst.setTimestamp(i, rsSrc.getTimestamp(i));
					} else {
						psDst.setString(i, rsSrc.getString(i));
					}
				}
				psDst.addBatch();
				j++;
				if (j % 500 == 0) {
					psDst.executeBatch();
					psDst.clearBatch();
				}
			}
			psDst.executeBatch();
			psDst.clearBatch();

			conDst.commit();
			this.setTaskStatus("执行成功");
			this.setTaskMsg("更新记录数:" + batchCount);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("数据同步错误:", e);
			// 异常回滚
			if (conDst != null) {
				try {
					conDst.rollback();
				} catch (SQLException e1) {
					this.setTaskStatus("执行失败");
					this.setTaskMsg(this.getTaskMsg() + "\n数据同步错误[回滚异常]:", e);
				}
			}
		} finally {
			UtilSql.close(rsSrc, rsDst, psSrc, psDst, psDel);
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

}
