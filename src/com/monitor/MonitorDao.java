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

public class MonitorDao {

	private Connection con;
	public static MonitorDao monitorDao = null;

	public static MonitorDao getInstance() {
		if (monitorDao == null)
			monitorDao = new MonitorDao();
		return monitorDao;
	}

	// 构造
	private MonitorDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 获取监控员表格数组
	@SuppressWarnings("unchecked")
	public Vector<Vector<Comparable>> getMonitorVector(String sql) {
		Statement sm = null;
		ResultSet rs = null;
		Vector<Vector<Comparable>> monitorVector = new Vector<Vector<Comparable>>();
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery(sql);
			while (rs.next()) {
				Vector<Comparable> rowValue = new Vector<Comparable>();
				rowValue.add(false);
				rowValue.add(rs.getString("mCode") == null ? "" : rs.getString("mCode"));
				rowValue.add(rs.getString("mName") == null ? "" : rs.getString("mName"));
				rowValue.add(rs.getString("mTel") == null ? "" : rs.getString("mTel"));
				rowValue.add(rs.getString("mMailAddress") == null ? "" : rs.getString("mMailAddress"));
				monitorVector.add(rowValue);
			}
		} catch (SQLException e) {
			Log.logError(" 获取监控人员信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 获取监控人员信息错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return monitorVector;
	}

	// 添加监控员
	public void addMonitor(Monitor monitor) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("insert into  " + AppCon.TN_Monitor + " (" + "mCode,mName,mTel,mMailAddress) values (?,?,?,?)");
			ps.setString(1, monitor.getMCode());
			ps.setString(2, monitor.getMName());
			ps.setString(3, monitor.getMTel());
			ps.setString(4, monitor.getMMailAddress());
			ps.addBatch();
			ps.executeBatch();
		} catch (SQLException e) {
			Log.logError(" 新增监控人员信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 新增监控人员信息错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 修改监控员
	public void modMonitor(Monitor monitor) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("update    " + AppCon.TN_Monitor + "  set " + " mName=?,mTel=?, mMailAddress=? " + " where  mCode='" + monitor.getMCode() + "'");
			ps.setString(1, monitor.getMName());
			ps.setString(2, monitor.getMTel());
			ps.setString(3, monitor.getMMailAddress());
			ps.addBatch();
			ps.executeBatch();
			con.commit();
		} catch (SQLException e) {
			Log.logError(" 修改监控人员信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 修改监控人员信息错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 删除监控员
	public void delMonitor(String mCode) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from  " + AppCon.TN_Monitor + "  where mCode='" + mCode + "'");
			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError(" 删除监控人员信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 删除监控人员信息错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

	// 判断是否存在监控员
	public boolean ifExistMonitor(String mCode) {
		boolean result = false;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_Monitor + "  where mCode ='" + mCode + "'");
			rs.last();
			if (rs.getRow() > 0)
				result = true;
		} catch (SQLException e) {
			Log.logError(" 判断是否存在监控人员信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 判断是否存在监控人员信息错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return result;
	}
}
