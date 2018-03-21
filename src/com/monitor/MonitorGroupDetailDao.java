package com.monitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import app.AppCon;

import com.log.Log;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;

public class MonitorGroupDetailDao {
	private Connection con;
	public static MonitorGroupDetailDao MonitorGroupDetailDao = null;

	public static MonitorGroupDetailDao getInstance() {
		if (MonitorGroupDetailDao == null)
			return new MonitorGroupDetailDao();
		return MonitorGroupDetailDao;
	}

	// 构造
	private MonitorGroupDetailDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 根据监控组标志获取监控员电话串
	public String getMTel(String mgCode) {
		String mtel = "";
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_Monitor + "  a, " + AppCon.TN_MonitorGroupDetail + "  b where a.mCode=b.mcode and mgCode='" + mgCode + "'");
			while (rs.next()) {
				String tel = rs.getString("mTel") == null ? "" : rs.getString("mTel");
				mtel = mtel + tel;
				if (!rs.isLast())
					mtel = mtel + ",";
			}
		} catch (SQLException e) {
			Log.logError(" 获取监控组监控员电话列表SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 获取监控组监控员电话列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return mtel;
	}

	// 根据监控组标志获取监控员邮箱地址 Array
	public String[] getMMailAddress(String mgCode) {
		String mMail[] = null;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select * from  " + AppCon.TN_Monitor + "  a, " + AppCon.TN_MonitorGroupDetail + "  b where a.mCode=b.mcode and mgCode='" + mgCode + "'");
			rs.last();
			if (rs.getRow() > 0) {
				mMail = new String[rs.getRow()];
				rs.beforeFirst();
				int i = 0;
				while (rs.next()) {
					mMail[i] = rs.getString("mMailAddress") == null ? "" : rs.getString("mMailAddress");
					i += 1;
				}
			}
		} catch (SQLException e) {
			Log.logError(" 获取监控组监控员邮箱列表SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 获取监控组监控员邮箱列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return mMail;
	}

	// 添加监控组监控员
	public void addMonitorGroupDetail(MonitorGroupDetail mgd) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("insert into  " + AppCon.TN_MonitorGroupDetail + "  (" + "mgCode,mcode) values (?,?)");
			ps.setString(1, mgd.getMgCode() == null ? "" : mgd.getMgCode());
			ps.setString(2, mgd.getMCode() == null ? "" : mgd.getMCode());

			ps.addBatch();
			ps.executeBatch();
		} catch (SQLException e) {
			Log.logError(" 新增监控组监控员SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 新增监控组监控员错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 删除监控组监控员
	public void delMonitorGroupDetail(MonitorGroupDetail mgd) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from  " + AppCon.TN_MonitorGroupDetail + "  where mgCode='" + mgd.getMgCode() + "'  and mCode='" + mgd.getMCode() + "'");
			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError(" 删除监控组监控员SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 删除监控组监控员错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

	// 删除监控组监控员
	public void delMonitorGroupDetail(String mgCode) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from  " + AppCon.TN_MonitorGroupDetail + "  where mgCode='" + mgCode + "'");
			sm.executeBatch();
		} catch (SQLException e) {
			Log.logError(" 删除监控组同时删除监控员SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 删除监控组同时删除监控员错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}
}
