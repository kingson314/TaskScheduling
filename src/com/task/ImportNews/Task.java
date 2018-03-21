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

public class Task extends TaskAbstract {
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
			StringBuilder sbDetails=new StringBuilder();
			while ((sline = buffReader.readLine()) != null) {
				if("".equals(sline.trim()))continue;
				System.out.println(sline);
				lineArr = sline.split(bean.getSeparate());
				int monthIdx=lineArr[0].indexOf("月");
				int dayIdx=lineArr[0].indexOf("日");
				int timeIdx=lineArr[0].indexOf(":");
				if(monthIdx<4&&monthIdx>0&& dayIdx<7 && dayIdx>0 && timeIdx>0 && monthIdx<dayIdx && dayIdx<timeIdx){
					if(count>0){//从第二条开始
						ps.setString(3, sbDetails.toString());
						ps.addBatch();
						sbDetails=new StringBuilder();
					}
					if(lineArr.length>0)
						ps.setString(1, lineArr[0].substring(lineArr[0].indexOf("日") + 1).replace(":",""));
					if(lineArr.length>1)
						ps.setString(2,lineArr[1]);
				}else{
					sbDetails.append(sline+"\n");
				}
				count += 1;
			}
			ps.addBatch();//加上最后一条
			ps.executeBatch();
			this.setTaskStatus("执行成功");
			this.setTaskMsg(bean.getFilePath() + "\n新增记录： " + count);
			buffReader.close();
		} catch (Exception e) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件["+bean.getFilePath()+"]导入错误:", e);
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
			StringBuilder sbDetails=new StringBuilder();
			while ((sline = buffReader.readLine()) != null) {
				if("".equals(sline.trim()))continue;
				System.out.println(sline);
				lineArr = sline.split(bean.getSeparate());
				int monthIdx=lineArr[0].indexOf("/");
				int dayIdx=lineArr[0].indexOf(" ");
				int timeIdx=lineArr[0].indexOf(":");
				if(monthIdx<4&&monthIdx>0&& dayIdx<7 && dayIdx>0 && timeIdx>0 && monthIdx<dayIdx && dayIdx<timeIdx){
					if(count>0){//从第二条开始
						ps.setString(3, sbDetails.toString());
						ps.addBatch();
						sbDetails=new StringBuilder();
					}
					if(lineArr.length>0)
					ps.setString(1, lineArr[0].substring(lineArr[0].indexOf(" ") + 1).replace(":",""));
					if(lineArr.length>1)
						ps.setString(2,lineArr[1]);
				}else{
					sbDetails.append(sline+"\n");
				}
				count += 1;
			}
			ps.addBatch();//加上最后一条
			ps.executeBatch();
			this.setTaskStatus("执行成功");
			this.setTaskMsg(bean.getFilePath() + "\n新增记录： " + count);
			buffReader.close();
		} catch (Exception e) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件["+bean.getFilePath()+"]导入错误:", e);
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
