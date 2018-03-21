package com.task.TableUpdate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.taskInterface.TaskAbstract;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;

/*
 * 此任务有限制如下:update语句格式必须为:update tableName set 
 * +按照compareFields 字段顺序编写，where 语句必须按照 compareKey字段顺编写
 */
public class Task extends TaskAbstract {

	public void fireTask() {
		execute();
	}

	private void execute() {
		Connection conSrc = null;
		Connection conDst = null;
		ResultSet rsSrc = null;
		PreparedStatement psSrc = null;
		Bean bean = (Bean) UtilJson.getJsonBean(this.getJsonStr(), Bean.class);
		try {
			// 1. 获得目标数据库连接
			DbConnection dbConnDst = DbConnectionDao.getInstance().getMapDbConn(bean.getDstDbName());
			if (dbConnDst == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取目标数据库连接错误");
				return;
			}
			conDst = UtilJDBCManager.getConnection(dbConnDst);

			// 2. 获得源数据库连接
			DbConnection dbConnSrc = DbConnectionDao.getInstance().getMapDbConn(bean.getSrcDbName());
			if (dbConnSrc == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取源数据库连接错误");
				return;
			}
			conSrc = UtilJDBCManager.getConnection(dbConnSrc);
			bean.setDstCompareFields(bean.getDstCompareFields().toUpperCase().replaceAll(" ", ""));
			bean.setDstCompareKey(bean.getDstCompareKey().toUpperCase().replaceAll(" ", ""));
			bean.setDstInsertSql(bean.getDstInsertSql().toUpperCase());
			bean.setDstSelectSql(bean.getDstSelectSql().toUpperCase());
			bean.setDstUpdateSql(bean.getDstUpdateSql().toUpperCase());
			bean.setSrcCompareFields(bean.getSrcCompareFields().toUpperCase().replaceAll(" ", ""));
			bean.setSrcCompareKey(bean.getSrcCompareKey().toUpperCase().replaceAll(" ", ""));
			bean.setSrcSelectSql(bean.getSrcSelectSql().toUpperCase());
			String[] srcCompareFields = bean.getSrcCompareFields().split(",");
			String[] dstCompareFields = bean.getDstCompareFields().split(",");
			if (srcCompareFields.length != dstCompareFields.length) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("比较字段个数不一致！");
				return;
			}
			Map<Object, HashMap<String, String>> dstMap = UtilSql.executeSql(conDst, bean.getDstSelectSql(), null, bean.getDstCompareKey());

			psSrc = conSrc.prepareStatement(bean.getSrcSelectSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rsSrc = psSrc.executeQuery();
			HashMap<String, String> mapDst = null;
			List<Object[]> paramInsert = new ArrayList<Object[]>();
			List<Object[]> paramUpdate = new ArrayList<Object[]>();
			int insertCnt = 0;
			int updateCnt = 0;
			ResultSetMetaData rsmdSrc = rsSrc.getMetaData();
			String keyValue = "";
			while (rsSrc.next()) {
				// System.out.println("insertCnt:"+insertCnt+"+updateCnt:"+updateCnt);
				keyValue = UtilSql.getKeyValue(rsSrc, bean.getSrcCompareKey());
				mapDst = dstMap.get(keyValue);// 从目的表中查找
				if (mapDst == null) {// 不存在时插入
					if (bean.isIfInsertWhenNotExist()) {
						insertCnt += 1;
						paramInsert.add(UtilSql.getParamList(rsSrc, rsmdSrc));
						if (insertCnt % 100 == 0) {
							UtilSql.executeUpdate(conDst, bean.getDstInsertSql(), paramInsert);
							paramInsert.clear();
						}
					}
				} else {
					if (bean.getSrcCompareFields().length() > 0) {
						if (!isEquals(mapDst, rsSrc, dstCompareFields, srcCompareFields)) {
							// 不一样时插入
							if (bean.isIfInsertWhenDifferent()) {
								insertCnt += 1;
								paramInsert.add(UtilSql.getParamList(rsSrc, rsmdSrc));
								if (insertCnt % 100 == 0) {
									UtilSql.executeUpdate(conDst, bean.getDstInsertSql(), paramInsert);
									paramInsert.clear();
								}
							} else if (bean.isIfUpdateWhenDifferent()) {// 不一样时更新
								updateCnt += 1;
								paramUpdate.add(UtilSql.getParamListByFields(rsSrc, rsmdSrc, bean.getSrcCompareKey(), bean.getSrcCompareFields()));
								if (updateCnt % 100 == 0) {
									UtilSql.executeUpdate(conDst, bean.getDstUpdateSql(), paramUpdate);
									paramUpdate.clear();
								}
							}
						}
					}
				}
			}
			if (!paramInsert.isEmpty())
				UtilSql.executeUpdate(conDst, bean.getDstInsertSql(), paramInsert);
			if (!paramUpdate.isEmpty())
				UtilSql.executeUpdate(conDst, bean.getDstUpdateSql(), paramUpdate);
			this.setTaskStatus("执行成功");
			this.setTaskMsg("新增记录数:" + insertCnt + "    更新记录数:" + updateCnt);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("数据表更新错误:", e);
		} finally {
			UtilSql.close(rsSrc, psSrc, conSrc, conDst);
		}
	}

	// 这个函数判断只要有一个比较字段不一致都认为不一样 缺点:没有按照字段类型进行比较，都转换为字符串比较，
	// 可能出现原来数值相同的字段返回不一致的情况， 增加了更新的记录数，不过不影响最终结果
	private boolean isEquals(Map<String, String> mapDst, ResultSet rs, String[] dstCompareFields, String[] srcCompareFields) throws SQLException {
		boolean result = true;
		for (int i = 0; i < srcCompareFields.length; i++) {
			// System.out.println(srcCompareFields[i].trim());
			// System.out.println(rs.getObject(srcCompareFields[i]));
			// System.out.println(UtilString.isNil(rs.getObject(srcCompareFields[i])));
			String src = UtilString.isNil(rs.getObject(srcCompareFields[i].trim())).toString();
			String dst = UtilString.isNil(mapDst.get(dstCompareFields[i].trim()));
			if (!dst.equals(src)) {
				// System.out.println(dstCompareFields[i] + "：" + dst + " "
				// + srcCompareFields[i].trim() + "：" + src);
				result = false;
				break;
			}
		}
		return result;
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}
}
