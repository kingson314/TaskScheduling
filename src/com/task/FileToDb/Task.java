package com.task.FileToDb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.taskInterface.TaskAbstract;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;

public class Task extends TaskAbstract {
	private static Map<String, Integer> mapFileToDb = new HashMap<String, Integer>();

	public void fireTask() {
		Connection con = null;
		PreparedStatement ps = null;
		DbConnection dbconn = null;
		BufferedReader buffReader = null;
		String sline = "";
		String[] line = null;
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			dbconn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbconn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取文件导入数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbconn);
			if (bean.getFilePath().equals("")) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件路径为空");
				return;
			}

			File inFile = new File(bean.getFilePath());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" + inFile.getAbsolutePath() + " 不存在");
				return;
			}

			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			// buffReader.mark((int) inFile.length() );// 在首行做个标记
			if (mapFileToDb.get(inFile.getName()) == null) {
				// 首次运行，清空
				if (!bean.getDelSql().equals("")) {
					String[] sql = bean.getDelSql().split(";");
					for (int i = 0; i < sql.length; i++) {
						if (sql[i].trim().length() > 1) {
							ps = con.prepareStatement(sql[i]);
							ps.executeUpdate();
							ps.clearBatch();
						}
					}
				}
			} else {
				buffReader.skip(mapFileToDb.get(inFile.getName()));
			}
			ps = null;
			mapFileToDb.put(inFile.getName(), (int) inFile.length());
			ps = con.prepareStatement(bean.getInsertSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int count = 0;
			while ((sline = buffReader.readLine()) != null) {
				line = sline.split(bean.getSeparate());
				for (int i = 0; i < line.length; i++) {
					ps.setString(i + 1, line[i]);
				}
				ps.addBatch();
				if (count % 500 == 0) {
					try {
						ps.executeBatch();
					} catch (Exception e) {
						ps.clearBatch();
						continue;
					}
					ps.clearBatch();
				}
				count += 1;
			}
			ps.executeBatch();
			this.setTaskStatus("执行成功");
			this.setTaskMsg(bean.getFilePath() + "\n新增记录： " + count);
			buffReader.close();
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件导入错误:", e);
		} finally {
			UtilSql.close(con, ps);
		}
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		fireTask();
	}

}
