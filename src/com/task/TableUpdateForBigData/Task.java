package com.task.TableUpdateForBigData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.taskInterface.TaskAbstract;

import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;

/*
 * 此任务有限制如下:update语句格式必须为:update tableName set 
 * @按照compareFields 字段顺序编写，where 语句必须按照 compareKey字段顺编写
 * @要求必须有一个主键，并且查询源数据时按主键排序
 */
public class Task extends TaskAbstract {
	private int MaxRecordCount = 100000;// 每次查询只插入到这个最大值，之后根据key排序后重新查询
	private static String keyValue;
	private static int insertCnt;

	public void fireTask() {
		execute();
	}

	private void execute() {
		Bean bean = (Bean) UtilJson.getJsonBean(this.getJsonStr(), Bean.class);
		try {
			bean.setDstKey(bean.getDstKey().replaceAll(" ", "").toUpperCase());
			bean.setDstInsertSql(bean.getDstInsertSql().toUpperCase());
			bean.setDstUpdateSql(bean.getDstUpdateSql().toUpperCase());
			bean.setSrcKey(bean.getSrcKey().replaceAll(" ", "").toUpperCase());
			bean.setSrcSelectSql(bean.getSrcSelectSql().toUpperCase());
			String[] srcCompareKeys = bean.getSrcKey().split(",");
			String[] dstCompareKeys = bean.getDstKey().split(",");
			if (srcCompareKeys.length != dstCompareKeys.length) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("比较关键字个数不一致！");
				return;
			}
			if (srcCompareKeys.length != 1) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("只能有一个比较关键字！");
				return;
			}
			// 1. 获得目标数据库连接
			DbConnection dbConnDst = DbConnectionDao.getInstance().getMapDbConn(bean.getDstDbName());
			if (dbConnDst == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取目标数据库连接错误");
				return;
			}

			// 2. 获得源数据库连接
			DbConnection dbConnSrc = DbConnectionDao.getInstance().getMapDbConn(bean.getSrcDbName());
			if (dbConnSrc == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取源数据库连接错误");
				return;
			}
			keyValue = "0";
			insertCnt = 0;
			exec(bean, dbConnSrc, dbConnDst);
			this.setTaskStatus("执行成功");
			this.setTaskMsg("新增记录数:" + insertCnt);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("更新错误:", e);
		} finally {
		}
	}

	private void exec(Bean bean, DbConnection dbConnSrc, DbConnection dbConnDst) throws SQLException {
		Connection conDst = UtilJDBCManager.getConnection(dbConnDst);
		Connection conSrc = UtilJDBCManager.getConnection(dbConnSrc);
		PreparedStatement psInsert = conDst.prepareStatement(bean.getDstInsertSql());
		String srcSelectSql = bean.getSrcSelectSql() + " where " + bean.getSrcKey() + " > '" + keyValue + "' order by " + bean.getSrcKey();
		System.out.println(keyValue);
		PreparedStatement psSrc = conSrc.prepareStatement(srcSelectSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rsSrc = psSrc.executeQuery();
		while (rsSrc.next()) {
			insertCnt += 1;
			addBatch(psInsert, rsSrc);
			if (insertCnt % 10000 == 0) {
				psInsert.executeBatch();
				psInsert.clearBatch();
				System.out.println(UtilConver.dateToStr("HH:mm:ss") + ":" + insertCnt);
			}
			keyValue = rsSrc.getString(bean.getSrcKey());
			if (insertCnt % MaxRecordCount == 0) {
				psInsert.executeBatch();
				psInsert.clearBatch();
				UtilSql.close(rsSrc, psSrc, psInsert, conSrc, conDst);
				// 递归
				exec(bean, dbConnSrc, dbConnDst);
			}
		}
		psInsert.executeBatch();
		psInsert.clearBatch();
		UtilSql.close(rsSrc, psSrc, psInsert, conSrc, conDst);
	}

	private void addBatch(PreparedStatement ps, ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			ps.setObject(i + 1, rs.getObject(i + 1));
		}
		ps.addBatch();
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}
}
