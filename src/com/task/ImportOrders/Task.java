package com.task.ImportOrders;

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

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;

public class Task extends TaskAbstract {
	private String separate = "\t";
	private String insertSql = "insert into t_orders"
			+ "(accountName,orderno,opendate,opentime,ordertype,lots,symbol,openprice,stoploss,takeprofit,closedate,closetime,closeprice,tax,fee,cost,profit,memo)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private String deleteSql = "delete t_orders where accountName=? and orderno=? ";

	public void fireTask() {
		Connection con = null;
		PreparedStatement psInsert = null;
		PreparedStatement psDelete = null;
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
			File inFile = new File(bean.getFilePath());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" + inFile.getAbsolutePath() + " 不存在");
				return;
			}
			psDelete = con.prepareStatement(deleteSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			psInsert = con.prepareStatement(insertSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int count = 0;
			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "gbk"));
			while ((sline = buffReader.readLine()) != null) {
				line = sline.split(separate);
				psInsert.setString(1, bean.getAccountName());// accountName
				psDelete.setString(1, bean.getAccountName());
				if (line.length == 5) {
					for (int i = 0; i < line.length; i++) {
						if (i == 0) {
							psInsert.setString(2, line[i]);// orderno
							psDelete.setString(2, line[i]);// orderno
						} else if (i == 1) {
							psInsert.setString(3, getDate(line[i]));// opendate
							psInsert.setString(4, getTime(line[i]));// opentime
						} else if (i == 2) {
							psInsert.setString(5, line[i]);// ordertype
						} else if (i == 3) {
							psInsert.setString(18, line[i]);// ordertype
						} else if (i == 4) {
							psInsert.setString(17, line[i].replace(" ", ""));// profit
						}
					}
					psInsert.setString(6, "0");
					psInsert.setString(7, "");
					psInsert.setString(8, "0");
					psInsert.setString(9, "0");
					psInsert.setString(10, "0");
					psInsert.setString(11, "");
					psInsert.setString(12, "");
					psInsert.setString(13, "0");
					psInsert.setString(14, "0");
					psInsert.setString(15, "0");
					psInsert.setString(16, "0");
					// System.out.println("5:" + sline);
				} else if (line.length == 11) {
					// System.out.println("11:" + sline);
					for (int i = 0; i < line.length; i++) {
						if (i == 0) {
							psInsert.setString(2, line[i]);// orderno
							psDelete.setString(2, line[i]);// orderno
						} else if (i == 1) {
							psInsert.setString(3, getDate(line[i]));// opendate
							psInsert.setString(4, getTime(line[i]));// opentime
						} else if (i == 2) {
							psInsert.setString(5, line[i]);// ordertype
						} else if (i == 3) {
							psInsert.setString(6, line[i]);// lots
						} else if (i == 4) {
							psInsert.setString(7, line[i]);// symbol
						} else if (i == 5) {
							psInsert.setString(8, line[i]);// openprice
						} else if (i == 6) {
							psInsert.setString(9, line[i]);// stoploss
						} else if (i == 7) {
							psInsert.setString(10, line[i]);// takeprofit
						} else if (i == 8) {
							psInsert.setString(11, getDate(line[i]));// closedate
							psInsert.setString(12, getTime(line[i]));// closetime
						} else if (i == 9) {
							psInsert.setString(13, line[i]);// closeprice
						} else if (i == 10) {
							psInsert.setString(18, line[i]);// memo
						}
					}
					psInsert.setString(14, "0");
					psInsert.setString(15, "0");
					psInsert.setString(16, "0");
					psInsert.setString(17, "0");

				} else if (line.length == 14) {
					// System.out.println("14:" + sline);
					for (int i = 0; i < line.length; i++) {
						if (i == 0) {
							psInsert.setString(2, line[i]);// orderno
							psDelete.setString(2, line[i]);// orderno
						} else if (i == 1) {
							psInsert.setString(3, getDate(line[i]));// opendate
							psInsert.setString(4, getTime(line[i]));// opentime
						} else if (i == 2) {
							psInsert.setString(5, line[i]);// ordertype
						} else if (i == 3) {
							psInsert.setString(6, line[i]);// lots
						} else if (i == 4) {
							psInsert.setString(7, line[i]);// symbol
						} else if (i == 5) {
							psInsert.setString(8, line[i]);// openprice
						} else if (i == 6) {
							psInsert.setString(9, line[i]);// stoploss
						} else if (i == 7) {
							psInsert.setString(10, line[i]);// takeprofit
						} else if (i == 8) {
							psInsert.setString(11, getDate(line[i]));// closedate
							psInsert.setString(12, getTime(line[i]));// closetime
						} else if (i == 9) {
							psInsert.setString(13, line[i]);// closeprice
						} else if (i == 10) {
							psInsert.setString(14, line[i]);// tax
						} else if (i == 11) {
							psInsert.setString(15, line[i]);// fee
						} else if (i == 12) {
							psInsert.setString(16, line[i]);// cost
						} else if (i == 13) {
							psInsert.setString(17, line[i].replace(" ", ""));// profit
						}
					}
					psInsert.setString(18, "");
				}
				psDelete.addBatch();
				psInsert.addBatch();
				count += 1;
			}
			psDelete.executeBatch();
			psInsert.executeBatch();
			this.setTaskStatus("执行成功");
			this.setTaskMsg(bean.getFilePath() + "\n新增记录： " + count);
			buffReader.close();
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件导入错误[" + sline + "]:", e);
		} finally {
			UtilSql.close(con, psInsert);
		}
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		fireTask();
	}

	private String getDate(String dateTime) {
		return dateTime.substring(0, 10).replace(".", "");
	}

	private String getTime(String dateTime) {
		return dateTime.substring(11).replace(":", "");
	}

	public static void main(String[] args) {
	}
}
