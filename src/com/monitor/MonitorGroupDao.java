package com.monitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import app.AppCon;

import com.log.Log;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;

public class MonitorGroupDao {
	private Connection con;
	public static MonitorGroupDao mgDao = null;

	public static MonitorGroupDao getInstance() {
		if (mgDao == null)
			mgDao = new MonitorGroupDao();
		return mgDao;
	}

	// 构造
	private MonitorGroupDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 获取监控组
	public MonitorGroup getMonitorGroup(String mgCode) {
		MonitorGroup mg = new MonitorGroup();
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_MonitorGroup + "  where mgCode='" + mgCode + "'");
			rs.last();
			mg.setMgCode(rs.getString("mgCode") == null ? "" : rs.getString("mgCode"));
			mg.setMgName(rs.getString("mgName") == null ? "" : rs.getString("mgName"));
			mg.setMgMemo(rs.getString("mgMemo") == null ? "" : rs.getString("mgMemo"));

		} catch (SQLException e) {
			Log.logError(" 获取监控组对象SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 获取监控组对象错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return mg;
	}

	// 获取监控组标志 array
	public String[] getMonitorGroupCode() {
		String[] mgCode = null;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select mgCode from  " + AppCon.TN_MonitorGroup + "  ");
			rs.last();
			mgCode = new String[rs.getRow()];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				mgCode[i] = rs.getString("mgCode");
				i += 1;
			}

		} catch (SQLException e) {
			Log.logError(" 获取监控组对象SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 获取监控组对象错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return mgCode;
	}

	// 获取监控组表格数组
	public Vector<Vector<String>> getMonitorGroupVector(String mgName) {
		Statement sm = null;
		ResultSet rs = null;
		Vector<Vector<String>> MonitorGroupVector = new Vector<Vector<String>>();
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (mgName.length() > 1)
				rs = sm.executeQuery("select * from  " + AppCon.TN_MonitorGroup + "  where mgName like ('%" + mgName + "%')");
			else
				rs = sm.executeQuery("select * from  " + AppCon.TN_MonitorGroup + "  ");
			while (rs.next()) {
				Vector<String> rowValue = new Vector<String>();
				rowValue.add(rs.getString("mgCode") == null ? "" : rs.getString("mgCode"));
				rowValue.add(rs.getString("mgName") == null ? "" : rs.getString("mgName"));
				rowValue.add(rs.getString("mgMemo") == null ? "" : rs.getString("mgMemo"));
				MonitorGroupVector.add(rowValue);
			}
		} catch (SQLException e) {
			Log.logError(" 获取监控组信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 获取监控组信息错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return MonitorGroupVector;
	}

	// 添加监控组
	public void addMonitorGroup(MonitorGroup monitorGroup) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("insert into  " + AppCon.TN_MonitorGroup + " (" + "mgCode,mgName,mgMemo) values (?,?,?)");
			ps.setString(1, monitorGroup.getMgCode());
			ps.setString(2, monitorGroup.getMgName());
			ps.setString(3, monitorGroup.getMgMemo());
			ps.addBatch();
			ps.executeBatch();
		} catch (SQLException e) {
			Log.logError(" 新增监控组信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 新增监控组信息错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 修改监控组
	public void modMonitorGroup(MonitorGroup monitorGroup) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("update    " + AppCon.TN_MonitorGroup + "  set " + "mgCode=?,mgName=?,mgMemo=? " + " where  mgCode='" + monitorGroup.getMgCode() + "'");
			ps.setString(1, monitorGroup.getMgCode());
			ps.setString(2, monitorGroup.getMgName());
			ps.setString(3, monitorGroup.getMgMemo());
			ps.addBatch();
			ps.executeBatch();
		} catch (SQLException e) {
			Log.logError(" 修改监控组信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 修改监控组信息错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 删除监控组
	public void delMonitorGroup(String mgCode) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from  " + AppCon.TN_MonitorGroup + "  where mgCode='" + mgCode + "'");
			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError(" 删除监控组信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 删除监控组信息错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

	// 判断是否存在监控组
	public boolean ifExistMonitorGroup(String mgCode) {
		boolean result = false;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_MonitorGroup + "  where mgCode ='" + mgCode + "'");
			rs.last();
			if (rs.getRow() > 0)
				result = true;
		} catch (SQLException e) {
			Log.logError(" 判断是否存在监控组信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 判断是否存在监控组信息错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}
}
