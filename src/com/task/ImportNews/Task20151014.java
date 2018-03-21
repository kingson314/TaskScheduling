package com.task.ImportNews;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;
import com.taskInterface.TaskAbstract;

import common.util.conver.UtilConver;
import common.util.file.UtilFile;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import consts.Const;

public class Task20151014 extends TaskAbstract {
	public void fireTask() throws Exception {
		System.out.println(UtilConver.dateToStr(this.getDate(),Const.fm_yyyyMMdd));
		if(UtilConver.dateToStr(this.getDate(),Const.fm_yyyyMMdd).compareTo("20150628")<0){
			importFile20150628("utf-8");
		}else{
			importFile("utf-8");
		}
	}
	
	//20150628之前使用
	private void importFile20150628(String encoding){
		Connection con = null;
		PreparedStatement ps = null;
		DbConnection dbconn = null;
		BufferedReader buffReader = null;
		String sline = "";
		String[] lineArr = null;
		Bean bean =null;
		try {
			bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			bean.setSeparate("\t");
			dbconn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbconn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取文件导入数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbconn);
			File inFile = new File(bean.getFilePath());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" + inFile.getAbsolutePath() + " 不存在");
				return;
			}
			UtilFile.copyFile(bean.getFilePath(), inFile.getParent()+"/bak/"+inFile.getName());
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
			ps = con.prepareStatement(bean.getInsertSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int count = 0;
			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), encoding));
			while ((sline = buffReader.readLine()) != null) {
				System.out.println(sline);
				lineArr = sline.split(bean.getSeparate());
				if (count > 0) {
					if (lineArr.length == 2) {
						ps.setString(3, "");
						ps.addBatch();
					} else if (lineArr.length == 1) {
						ps.setString(3, sline);
						ps.addBatch();
						continue;
					}
				}
				for (int i = 0; i < lineArr.length; i++) {
					if (i == 0) {
						ps.setString(i + 1, lineArr[i].substring(lineArr[i].indexOf("日") + 1).replace(":",""));
					} else {
						ps.setString(i + 1, lineArr[i]);
					}
//					System.out.println(line[i]);
				}
				count += 1;
			}
			ps.addBatch();
			ps.executeBatch();
			this.setTaskStatus("执行成功");
			this.setTaskMsg(bean.getFilePath() + "\n新增记录： " + count);
			buffReader.close();
		} catch (Exception e) {
			//if(e.getMessage().toString().indexOf("ORA-12899")>=0 && encoding.equals("utf-8")){
		//		importFile("gbk");
		//	}else
			{
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件["+bean.getFilePath()+"]导入错误:", e);
			}
		} finally {
			UtilSql.close(con, ps);
		}
	}
	
	
	private void importFile(String encoding){
		Connection con = null;
		PreparedStatement ps = null;
		DbConnection dbconn = null;
		BufferedReader buffReader = null;
		String sline = "";
		String[] line = null;
		Bean bean =null;
		try {
			bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			bean.setSeparate("\t");
			dbconn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbconn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取文件导入数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbconn);
			File inFile = new File(bean.getFilePath());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" + inFile.getAbsolutePath() + " 不存在");
				return;
			}
			UtilFile.copyFile(bean.getFilePath(), inFile.getParent()+"/bak/"+inFile.getName());
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
			ps = con.prepareStatement(bean.getInsertSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int count = 0;
			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), encoding));
			while ((sline = buffReader.readLine()) != null) {
				System.out.println(sline);
				line = sline.split(bean.getSeparate());
				if (count > 0) {
					if (line.length == 2) {
						ps.setString(3, "");
						ps.addBatch();
					} else if (line.length == 1) {
						ps.setString(3, sline);
						ps.addBatch();
						continue;
					}
				}
				for (int i = 0; i < line.length; i++) {
					if (i == 0) {
						ps.setString(i + 1, line[i].substring(line[i].indexOf(" ") + 1).replace(":",""));
					} else {
						ps.setString(i + 1, line[i]);
					}
//					System.out.println(line[i]);
				}
				count += 1;
			}
			ps.addBatch();
			ps.executeBatch();
			this.setTaskStatus("执行成功");
			this.setTaskMsg(bean.getFilePath() + "\n新增记录： " + count);
			buffReader.close();
		} catch (Exception e) {
			//if(e.getMessage().toString().indexOf("ORA-12899")>=0 && encoding.equals("utf-8")){
		//		importFile("gbk");
		//	}else
			{
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件["+bean.getFilePath()+"]导入错误:", e);
			}
		} finally {
			UtilSql.close(con, ps);
		}
	}
	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		try {
			fireTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
