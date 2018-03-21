package com.app;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;
import module.systemparam.SystemParamsValue;


import com.log.Log;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;

/**
 * 
 * @author fgq
 * @memo 短信报警线程
 */
public class MsgAlter extends Thread {
	private String Fmsg = "";
	private SystemParamsValue systemParamsValue;

	// 构造
	public MsgAlter(SystemParamsValue sysParamsValue, String vmsg) {
		this.Fmsg = vmsg;
		this.systemParamsValue = sysParamsValue;
	}

	// 线程运行
	public void run() {
		Execute();
	}

	// 短信报警方法
	public void Execute() {
		CallableStatement proc = null;
		Connection con = null;
		try {
			DbConnection dbconn = DbConnectionDao.getInstance().getMapDbConn(systemParamsValue.getDbLink());
			if (dbconn == null) {
				Log.logInfo("发送短信失败:短信数据库连接不存在");
				return;
			}
			con = UtilJDBCManager.getConnection(dbconn);
			if (con == null) {
				Log.logInfo("发送短信失败:短信数据库连接错误");
				return;
			}
			proc = con.prepareCall("{ CALL " + systemParamsValue.getListener_Proc_Name() + "(?,?,?,?,?,?,?,?) }");

			proc.setString(1, systemParamsValue.getTel());
			String tmp_Fmsg = Fmsg;

			if (Fmsg.length() > 370)
				tmp_Fmsg = Fmsg.substring(0, 370);

			proc.setString(2, tmp_Fmsg);
			proc.setString(3, systemParamsValue.getMessageServerName());

			if (systemParamsValue.getSendTime() != null && !"".equals(systemParamsValue.getSendTime()))
				proc.setTimestamp(4, java.sql.Timestamp.valueOf(systemParamsValue.getSendTime()));
			else
				proc.setTimestamp(4, null);

			if (systemParamsValue.getCloseTime() != null && !"".equals(systemParamsValue.getCloseTime()))
				proc.setTimestamp(5, java.sql.Timestamp.valueOf(systemParamsValue.getCloseTime()));
			else
				proc.setTimestamp(5, null);

			proc.setString(6, systemParamsValue.getTraceId());

			proc.registerOutParameter(7, Types.INTEGER);
			proc.registerOutParameter(8, Types.VARCHAR);
			proc.execute();
			int o_errcode = proc.getInt(7);
			String o_errmsg = proc.getString(8);

			if (o_errcode == 1)
				Log.logInfo("发送短信失败" + "参数列表:" + systemParamsValue.getTel().trim() + "\n" + Fmsg.trim() + "\n" + systemParamsValue.getMessageServerName().trim() + "\n"
						+ systemParamsValue.getSendTime().trim() + "\n" + systemParamsValue.getCloseTime().trim() + "\n" + systemParamsValue.getTraceId().trim() + "\n" + o_errcode
						+ "\n" + o_errmsg);
			else if (o_errcode == 2) {
				Log.logInfo("发送短信部分成功" + "参数列表:" + systemParamsValue.getTel().trim() + "\n" + Fmsg.trim() + "\n" + systemParamsValue.getMessageServerName().trim() + "\n"
						+ systemParamsValue.getSendTime().trim() + "\n" + systemParamsValue.getCloseTime().trim() + "\n" + systemParamsValue.getTraceId().trim() + "\n" + o_errcode
						+ "\n" + o_errmsg);

			}
		} catch (Exception e) {
			Log.logError("发送短信失败:", e);
		} finally {
			UtilSql.close(proc, con);
		}
	}
}
