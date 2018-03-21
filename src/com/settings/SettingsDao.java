package com.settings;

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
import common.util.string.UtilString;

public class SettingsDao {
	public static SettingsDao settingsdao = null;
	private Connection con;

	public static SettingsDao getInstance() {
		if (settingsdao == null)
			settingsdao = new SettingsDao();
		return settingsdao;
	}

	// 构造
	public SettingsDao() {
		this.con = UtilJDBCManager.getConnection(AppCon.DbconApp);
	}

	// 获取配置信息 Array
	public String[] getValueS(String setName) {
		String[] set = null;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select setValue from  " + AppCon.TN_Settings + "  where setName='" + setName + "' order by id desc ");
			rs.last();
			int len = rs.getRow();
			if (len > 0)
				set = new String[len + 1];
			else
				set = new String[1];
			set[0] = "全部";
			int i = 1;
			rs.beforeFirst();
			while (rs.next()) {
				set[i] = rs.getString("setValue");
				i += 1;
			}

		} catch (SQLException e) {
			Log.logError("获取配置列表SQL错误:", e);
		} catch (Exception e) {
			Log.logError("获取配置列表错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return set;
	}

	// 获取配置信息值
	public String getValue(String setName) {
		String value = "";
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select setValue from  " + AppCon.TN_Settings + "  where setName='" + setName + "'");
			rs.last();
			if (rs.getRow() > 0)
				value = rs.getString("setValue");

		} catch (Exception e) {
			Log.logError("获取配置错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return value;
	}

	// 获取配置信息标志
	public int getId(String setName) {
		int value = -1;
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = sm.executeQuery("select id from  " + AppCon.TN_Settings + "  where setName='" + setName + "'");
			rs.last();
			if (rs.getRow() > 0)
				value = rs.getInt("id");

		} catch (Exception e) {
			Log.logError("获取配置错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return value;
	}

	// 获取配置信息表格
	public Vector<Vector<String>> getSettingsVector(String setName) {
		Statement sm = null;
		ResultSet rs = null;
		Vector<Vector<String>> settingsVector = new Vector<Vector<String>>();
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (setName.length() > 1)
				rs = sm.executeQuery("select * from  " + AppCon.TN_Settings + "  where setName like ('%" + setName + "%')");
			else
				rs = sm.executeQuery("select * from  " + AppCon.TN_Settings + "  ");
			while (rs.next()) {
				Settings settings = new Settings();
				settings.setId(rs.getInt("id"));
				settings.setSetName(rs.getString("setName") == null ? "" : rs.getString("setName"));
				settings.setSetValue(rs.getString("setValue") == null ? "" : rs.getString("setValue"));
				settings.setSetMemo(rs.getString("setMemo") == null ? "" : rs.getString("setMemo"));
				Vector<String> rowValue = new Vector<String>();
				rowValue.add(String.valueOf(settings.getId()));
				rowValue.add(settings.getSetName());
				rowValue.add(settings.getSetValue());
				rowValue.add(settings.getSetMemo());
				settingsVector.add(rowValue);
			}
		} catch (SQLException e) {
			Log.logError(" 获取配置信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 获取配置信息错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
		return settingsVector;
	}

	// 新增配置信息
	public void addSettings(Settings settings) {
		PreparedStatement ps = null;
		try {
			Long maxId = Long.valueOf(UtilString.isNil(UtilSql.QueryForMax(con, "select max(id)+1 from " + AppCon.TN_Settings, new Object[0]), "0"));
			String sql = "insert into  " + AppCon.TN_Settings + " (id," + "setName,setValue,setMemo) values (?,?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, String.valueOf(maxId));
			ps.setString(2, settings.getSetName());
			ps.setString(3, settings.getSetValue());
			ps.setString(4, settings.getSetMemo());
			ps.addBatch();
			ps.executeBatch();
			con.commit();
		} catch (SQLException e) {
			Log.logError(" 新增配置信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 新增配置信息错误:", e);
		}
		UtilSql.close(ps);
	}

	// 修改配置信息
	public void modSettings(Settings settings) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("update    " + AppCon.TN_Settings + "  set " + "setName=?,setValue=?,setMemo=? " + " where  id=" + settings.getId());
			ps.setString(1, settings.getSetName());
			ps.setString(2, settings.getSetValue());
			ps.setString(3, settings.getSetMemo());
			ps.addBatch();
			ps.executeBatch();
			con.commit();
		} catch (SQLException e) {
			Log.logError(" 修改配置信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 修改配置信息错误:", e);
		} finally {
			UtilSql.close(rs, ps);
		}
	}

	// 删除配置信息
	public void delSettings(int id) {
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			sm.addBatch("delete from  " + AppCon.TN_Settings + "  where id=" + id);
			sm.executeBatch();
			con.commit();
		} catch (SQLException e) {
			Log.logError(" 删除配置信息SQL错误:", e);
		} catch (Exception e) {
			Log.logError(" 删除配置信息错误:", e);
		} finally {
			UtilSql.close(rs, sm);
		}
	}

}
