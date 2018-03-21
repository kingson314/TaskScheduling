package com.task.Procedure;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.app.Parser;
import com.log.Log;
import com.taskInterface.TaskAbstract;

import common.util.Math.UtilMath;
import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

public class Task extends TaskAbstract {
    private Bean bean;

    public void fireTask() {
	try {
	    this.bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
	    if (this.getNowDate() == null) {
		Log.logInfo(this.getTaskName() + "获取t_sys_date 为NULL,任务取消，请检查t_sys_date表数据");
		return;
	    }
	    execProcedure();
	} catch (Exception e) {
	    this.setTaskStatus("执行失败");
	    this.setTaskMsg("错误:", e);
	}
    }

    private void execProcedure() {
	this.setTaskStatus("执行成功");
	Connection con = null;
	try {
	    DbConnection dbConn = DbConnectionDao.getInstance().getMapDbConn(bean.getPDbName());
	    if (dbConn == null) {
		this.setTaskStatus("执行失败");
		this.setTaskMsg("获取存储过程" + this.bean.getPName() + "数据库连接错误");
		return;
	    }
	    con = UtilJDBCManager.getConnection(dbConn);
	    String[][] pParams = UtilString.getProcedureParamsRule(this.bean.getPParamsRule());
	    Map<String, String> map = UtilSql.execProcedureAsMap(con, this.bean.getPName(), pParams);
	    String taskResult = "";
	    for (int i = 0; i < pParams.length; i++) {
		if (i == pParams.length - 1)
		    taskResult = taskResult + map.get(pParams[i][0]);
		else
		    taskResult = taskResult + map.get(pParams[i][0]) + "/";
	    }
	    // Iterator<?> it = map.entrySet().iterator();
	    // while (it.hasNext()) {
	    // Entry<String, String> entry = (Entry<String, String>) it.next();
	    // taskResult = taskResult + entry.getKey() + ":"
	    // + entry.getValue() + "\n";
	    // }

	    // 错误代码必须为数值
	    if (this.bean.isIfErrorWarn()) {
		long errorCode = 0;
		long compareValue = 0;
		try {
		    errorCode = Long.valueOf(map.get(this.bean.getComparePapam()));
		} catch (Exception e) {
		    this.setTaskStatus("执行失败");
		    this.setTaskMsg("存储过程" + this.bean.getPName() + "报警参数应为Long类型:", e);
		    return;
		}
		try {
		    compareValue = Long.valueOf(this.bean.getCompareValue());
		} catch (Exception e) {
		    this.setTaskStatus("执行失败");
		    this.setTaskMsg("存储过程" + this.bean.getPName() + "报警参数的比较值应为Long类型:", e);
		    return;
		}

		if (UtilMath.compare(this.bean.getCompareType(), errorCode, compareValue)) {
		    this.setTaskStatus("执行失败");
		}
	    }
	    if (this.getTaskStatus().equals("执行失败")) {
		this.setTaskMsg(taskResult + "\n" + this.bean.getWarning());
	    } else {
		this.setTaskMsg(taskResult);
	    }
	} catch (SQLException e) {
	    this.setTaskStatus("执行失败");
	    this.setTaskMsg("存储过程" + this.bean.getPName() + "执行SQL出错:", e);
	} catch (Exception e) {
	    this.setTaskStatus("执行失败");
	    this.setTaskMsg("存储过程" + this.bean.getPName() + "执行出错:", e);
	} finally {
	    UtilSql.close(con);
	}
    }

    public void manuExecTask(String[] params) {
	Bean bean = (Bean) UtilJson.getJsonBean(this.getJsonStr(), Bean.class);
	// 存储过程任务类型，接收界面传来的存储过程参数
	bean.setPParamsRule(this.parserManuParamRule(params, bean.getPParamsRule()));
	this.setJsonStr(UtilJson.getJsonStr(bean));
	this.fireTask();
    }

    private String parserManuParamRule(String[] params, String paramsRule) {
	// 只替换 【开始日期; 结束日期; 基金代码】 3个参数
	String newParamsRule = "";
	String[] oldParamsRule = paramsRule.replaceAll("\n", "").split(";");
	for (int j = 0; j < oldParamsRule.length; j++) {
	    try {
		Date date = new Date(UtilConver.strToDate(params[j], Const.fm_yyyyMMdd).getTime());
		oldParamsRule[j] = Parser.parse(oldParamsRule[j], date);
	    } catch (Exception e) {
	    }

	    if (oldParamsRule[j].indexOf(";") < 0)
		newParamsRule = newParamsRule + oldParamsRule[j] + ";";
	    else
		newParamsRule = newParamsRule + oldParamsRule[j];
	}

	return newParamsRule;

    }

    public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
	fireTask();
    }
}
