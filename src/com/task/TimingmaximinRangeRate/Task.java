package com.task.TimingmaximinRangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.taskInterface.TaskAbstract;

import common.util.Math.UtilMath;
import common.util.array.UtilArray;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;

/**
 * @Description: 统计某一指标在某一时间点前后价格波动极值出现在一定波动区间内的频率(用于统计出指标对价格影响波动大小)
 * @date Jul 15, 2014
 * @author:fgq
 */
public class Task extends TaskAbstract {
    private Bean bean;
    private Connection con;
    private final String sa_timingmaximin = " sa_timingmaximin ";
    private final String insertSql = " insert into Sa_Timingmaximin_RangeRate(indicatorId,symbol,range,count,totalCount,rate,average,maxValue,minValue,minute)values(?,?,?,?,?,?,?,?,?,?)";
    private final String deleteSql = "delete from Sa_Timingmaximin_RangeRate where indicatorId=? and symbol=? ";

    public void manuExecTask(String[] params) {
	Statement sm = null;
	ResultSet rs = null;
	PreparedStatement psInsert = null;
	PreparedStatement psDelete = null;
	try {
	    this.bean = (Bean) UtilJson.getJsonBean(this.getJsonStr(), Bean.class);
	    bean.setBegDate(params[0]);
	    bean.setEndDate(params[1]);
	    DbConnection dbConn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
	    if (dbConn == null) {
		this.setTaskStatus("执行失败");
		this.setTaskMsg("数据库连接错误");
		return;
	    }
	    con = UtilJDBCManager.getConnection(dbConn);
	    psInsert = this.con.prepareStatement(insertSql);
	    psDelete = this.con.prepareStatement(deleteSql);
	    List<HashMap<String, String>> list = this.getListIndicatorSymbol(bean);
	    if (list == null) {
		this.setTaskStatus("执行成功");
		this.setTaskMsg("该日期段内没有数据！");
		return;
	    }
	    int cnt = 0;
	    for (HashMap<String, String> map : list) {
		String indicatorId = map.get("INDICATORID");
		String symbol = map.get("SYMBOL");
		String field = "";
		if (bean.getMinute() < 0) {
		    field = "MAXIMINMINUTEBEFORE" + 1;
		} else {
		    field = "MAXIMINMINUTEAFTER" + bean.getMinute();
		}
		// 最大最小值
		String sql = "select b.publishDate,max(abs(" + field + "))" + field + " from sa_timingmaximin a ,economic_data b where  a.economicDataId=b.id and " + "   a.symbol='" + symbol + "'  and b.indicatorId='" + indicatorId
			+ "'   and b.publishDate between '" + bean.getBegDate() + "' and '" + bean.getEndDate() + "' group by b.publishDate  order by b.publishDate ";
		sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		System.out.println(sql);
		rs = sm.executeQuery(sql);
		double value = 0.0;
		String[][] pointArr = UtilArray.getArray2(bean.getRange().replace("\"", ""));
		int[] count = new int[pointArr.length];
		double[] timingvalue = new double[pointArr.length];
		double[] maxValue = new double[pointArr.length];
		double[] minValue = new double[pointArr.length];
		for (int i = 0; i < pointArr.length; i++) {
		    maxValue[i] = 0;
		    minValue[i] = 999999999;
		}
		int totalCount = 0;
		while (rs.next()) {
		    value = Math.abs(Double.valueOf(UtilString.isNil(rs.getString(field))));
		    for (int i = 0; i < pointArr.length; i++) {
			if (value > Double.valueOf(pointArr[i][0]) && value <= Double.valueOf(pointArr[i][1])) {
			    count[i] += 1;
			    timingvalue[i] += value;
			    maxValue[i] = Math.max(maxValue[i], value);
			    minValue[i] = Math.min(maxValue[i], value);
			}
		    }
		    totalCount += 1;
		}
		TimingmaximinRangeRate tr = new TimingmaximinRangeRate(indicatorId, symbol, totalCount, bean.getMinute());
		// 先删除
		addBatchForDeleteTimingmaximinRangeRate(psDelete, tr);
		psDelete.executeBatch();
		psDelete.clearBatch();

		for (int i = 0; i < count.length; i++) {
		    if (count[i] == 0)
			continue;
		    tr.setRange(pointArr[i][0] + "," + pointArr[i][1]);
		    double rate = Double.valueOf(count[i]) / totalCount;
		    tr.setCount(count[i]);
		    tr.setRate(UtilMath.round(rate * 100, 2));
		    double val = timingvalue[i] / count[i];
		    tr.setAverage(UtilMath.round(val, 2));
		    tr.setMaxValue(UtilMath.round(maxValue[i], 2));
		    tr.setMinValue(UtilMath.round(minValue[i], 2));
		    addBatchForTimingmaximinRangeRate(psInsert, tr);
		    cnt += 1;
		    if (cnt != 0 && cnt % 1000 == 0) {
			try {
			    psInsert.executeBatch();
			} catch (Exception e) {
			    continue;
			} finally {
			    psInsert.clearBatch();
			}
		    }
		}
	    }
	    psInsert.executeBatch();
	    this.setTaskStatus("执行成功");
	    this.setTaskMsg("执行完成");
	} catch (Exception e) {
	    this.setTaskStatus("执行失败");
	    this.setTaskMsg("", e);
	} finally {
	    UtilSql.close(rs, sm, psDelete, psInsert, con);
	}
    }

    private List<HashMap<String, String>> getListIndicatorSymbol(Bean bean) throws SQLException {
	String sql = "select distinct b.indicatorId,symbol from " + sa_timingmaximin + "a,economic_data b  where a.economicDataId=b.id and b.publishDate between '" + bean.getBegDate() + "' and '" + bean.getEndDate() + "'";
	if (bean.getSymbol().equals("全部")) {

	} else {
	    sql = sql + " and symbol='" + bean.getSymbol() + "'";
	}
	return UtilSql.queryM(con, sql);
    }

    private void addBatchForTimingmaximinRangeRate(PreparedStatement ps, TimingmaximinRangeRate tr) throws Exception {
	Object[] params = new Object[] { tr.getIndicatorId(), tr.getSymbol(), tr.getRange(), tr.getCount(), tr.getTotalCount(), tr.getRate(), tr.getAverage(), tr.getMaxValue(), tr.getMinValue(),
		tr.getMinute() };
	for (int i = 0; i < params.length; i++) {
	    ps.setObject(i + 1, params[i]);
	}
	ps.addBatch();
    }

    private void addBatchForDeleteTimingmaximinRangeRate(PreparedStatement ps, TimingmaximinRangeRate tr) throws Exception {
	Object[] params = new Object[] { tr.getIndicatorId(), tr.getSymbol() };
	for (int i = 0; i < params.length; i++) {
	    ps.setObject(i + 1, params[i]);
	}
	ps.addBatch();
    }

    public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
    }

}
