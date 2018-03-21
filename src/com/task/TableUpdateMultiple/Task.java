package com.task.TableUpdateMultiple;

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
	private Connection conSrc;
	private Connection conDst;

	public void fireTask() {
		Bean bean = (Bean) UtilJson.getJsonBean(this.getJsonStr(), Bean.class);
		if (!initCon(bean))
			return;
		List<BeanDetail> listBeanDetail = bean.getListBeanDetail();
		for (BeanDetail beanDetail : listBeanDetail) {
			execute(beanDetail);
		}

		releaseCon();
	}

	private boolean initCon(Bean bean) {
		// 1. 获得目标数据库连接
		DbConnection dbConnDst = DbConnectionDao.getInstance().getMapDbConn(bean.getDstDbName());
		if (dbConnDst == null) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("获取目标数据库连接错误");
			return false;
		}
		conDst = UtilJDBCManager.getConnection(dbConnDst);

		// 2. 获得源数据库连接
		DbConnection dbConnSrc = DbConnectionDao.getInstance().getMapDbConn(bean.getSrcDbName());
		if (dbConnSrc == null) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("获取源数据库连接错误");
			return false;
		}
		conSrc = UtilJDBCManager.getConnection(dbConnSrc);
		return true;
	}

	private void releaseCon() {
		UtilSql.close(conDst, conSrc);
	}

	private void execute(BeanDetail beanDetail) {
		ResultSet rsSrc = null;
		PreparedStatement psSrc = null;
		try {
			beanDetail.setDstCompareFields(beanDetail.getDstCompareFields().toUpperCase().replaceAll(" ", ""));
			beanDetail.setDstCompareKey(beanDetail.getDstCompareKey().toUpperCase().replaceAll(" ", ""));
			beanDetail.setDstInsertSql(beanDetail.getDstInsertSql().toUpperCase());
			beanDetail.setDstSelectSql(beanDetail.getDstSelectSql().toUpperCase());
			beanDetail.setDstUpdateSql(beanDetail.getDstUpdateSql().toUpperCase());
			beanDetail.setSrcCompareFields(beanDetail.getSrcCompareFields().toUpperCase().replaceAll(" ", ""));
			beanDetail.setSrcCompareKey(beanDetail.getSrcCompareKey().toUpperCase().replaceAll(" ", ""));
			beanDetail.setSrcSelectSql(beanDetail.getSrcSelectSql().toUpperCase());
			String[] srcCompareFields = beanDetail.getSrcCompareFields().split(",");
			String[] dstCompareFields = beanDetail.getDstCompareFields().split(",");
			if (srcCompareFields.length != dstCompareFields.length) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("比较字段个数不一致！");
				return;
			}
			Map<Object, HashMap<String, String>> dstMap = UtilSql.executeSql(conDst, beanDetail.getDstSelectSql(), null, beanDetail.getDstCompareKey());
			psSrc = conSrc.prepareStatement(beanDetail.getSrcSelectSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
				keyValue = UtilSql.getKeyValue(rsSrc, beanDetail.getSrcCompareKey());
				mapDst = dstMap.get(keyValue);// 从目的表中查找
				if (mapDst == null) {// 不存在时插入
					if (beanDetail.isIfInsertWhenNotExist()) {
						insertCnt += 1;
						paramInsert.add(UtilSql.getParamList(rsSrc, rsmdSrc));
						if (insertCnt % 100 == 0) {
							UtilSql.executeUpdate(conDst, beanDetail.getDstInsertSql(), paramInsert);
							paramInsert.clear();
						}
					}
				} else {
					if (beanDetail.getSrcCompareFields().length() > 0) {
						if (!isEquals(mapDst, rsSrc, dstCompareFields, srcCompareFields)) {
							// 不一样时插入
							if (beanDetail.isIfInsertWhenDifferent()) {
								insertCnt += 1;
								paramInsert.add(UtilSql.getParamList(rsSrc, rsmdSrc));
								if (insertCnt % 100 == 0) {
									UtilSql.executeUpdate(conDst, beanDetail.getDstInsertSql(), paramInsert);
									paramInsert.clear();
								}
							} else if (beanDetail.isIfUpdateWhenDifferent()) {// 不一样是更新
								updateCnt += 1;
								paramUpdate.add(UtilSql.getParamListByFields(rsSrc, rsmdSrc, beanDetail.getSrcCompareKey(), beanDetail.getSrcCompareFields()));
								if (updateCnt % 100 == 0) {
									UtilSql.executeUpdate(conDst, beanDetail.getDstUpdateSql(), paramUpdate);
									paramUpdate.clear();
								}
							}
						}
					}
				}
			}
			if (!paramInsert.isEmpty())
				UtilSql.executeUpdate(conDst, beanDetail.getDstInsertSql(), paramInsert);
			if (!paramUpdate.isEmpty())
				UtilSql.executeUpdate(conDst, beanDetail.getDstUpdateSql(), paramUpdate);
			this.setTaskStatus("执行成功");
			this.setTaskMsg(this.getTaskMsg() + "新增记录数:" + insertCnt + "    更新记录数:" + updateCnt + "\n");
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("数据表更新错误:", e);
		} finally {
			UtilSql.close(rsSrc, psSrc);
		}
	}

	/**
	 * 这个函数判断只要有一个比较字段不一致都认为不一样 缺点:没有按照字段类型进行比较，都转换为字符串比较，
	 * 可能出现原来数值相同的字段返回不一致的情况， 增加了更新的记录数，不过不影响最终结果
	 */
	private boolean isEquals(Map<String, String> mapDst, ResultSet rs, String[] dstCompareFields, String[] srcCompareFields) throws SQLException {
		boolean result = true;
		for (int i = 0; i < srcCompareFields.length; i++) {
			// System.out.println(srcCompareFields[i].trim());
			// System.out.println(rs.getObject(srcCompareFields[i]));
			// System.out.println(Fun.isNil(rs.getObject(srcCompareFields[i])));
			String src = UtilString.isNil(rs.getObject(srcCompareFields[i].trim())).toString();
			String dst = UtilString.isNil(mapDst.get(dstCompareFields[i].trim()));
			if (!dst.equals(src)) {
				// System.out.println(dstCompareFields[i] + " " + src + " " +
				// dst);
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
