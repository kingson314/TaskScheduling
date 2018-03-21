package com.task.ImportExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.log.Log;
import com.taskInterface.TaskAbstract;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;

public class Task extends TaskAbstract {
	private Bean bean;
	private Connection con;

	// 执行
	private void fireTask() {
		try {
			bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			if (this.getNowDate() == null) {
				Log.logInfo(this.getTaskName() + "获取t_sys_date 为NULL,任务取消，请检查t_sys_date表数据");
				return;
			}
			File docFile = new File(bean.getDocDir());
			if (!docFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("源文件:" + bean.getDocDir() + "不存在");
				return;
			}
			DbConnection dbConn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbConn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg(this.bean.getDbName() + "数据库连接错误");
				return;
			}
			if (bean.getInsertSql().trim().length() < 1) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("InsertSql为空");
				return;
			}
			if (bean.getDocFieldRule().trim().length() < 1) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("映射规则为空");
				return;
			}

			UtilSql.DataField[] dataField = getFieldRule(bean.getDocFieldRule());
			// 获取文档数据
			List<List<Object>> docList = getDocData(dataField);
			if (docList == null)
				return;
			con = UtilJDBCManager.getConnection(dbConn);
			con.setAutoCommit(false);
			// 删除语句
			if (bean.getDelSql().trim().length() > 0)
				UtilSql.executeUpdate(con, bean.getDelSql());
			// 导入数据
			UtilSql.executeUpdate(con, bean.getInsertSql(), docList, dataField);
			con.commit();
			this.setTaskStatus("执行成功");
			this.setTaskMsg("导入" + bean.getDocDir() + ";记录数为:" + docList.size());
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
			this.setTaskStatus("执行失败");
			this.setTaskMsg("导入" + bean.getDocDir() + "错误:", e);
		} finally {
			UtilSql.close(con);
		}
	}

	// 读取文档数据
	private List<List<Object>> getDocData(UtilSql.DataField[] dataField) {
		FileInputStream file = null;
		// 把每行记录存入到LIST中
		List<List<Object>> docList = new ArrayList<List<Object>>();
		try {
			file = new FileInputStream(bean.getDocDir());
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			HSSFSheet sheet = workbook.getSheetAt(bean.getDocSheetIndex());
			if (sheet == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文档中的Sheet不存在");
				return null;
			}
			for (int i = bean.getStartLine() - 1; i <= sheet.getLastRowNum(); i++) {
				// 忽略倒数最后的行数
				if (i > sheet.getLastRowNum() - bean.getIgnoreLastLines())
					continue;
				HSSFRow aRow = sheet.getRow(i);
				if (aRow == null)
					continue;
				// 把每一列存入LIST中
				List<Object> lineList = new ArrayList<Object>();
				// 导入包含的列值
				for (short j = 0; j < dataField.length; j++) {
					// for (short j = 0; j <= aRow.getLastCellNum(); j++) {
					Object value = "";
					// 按照字段映射规则中的顺序插入list列数据
					HSSFCell aCell = aRow.getCell((short) dataField[j].fieldColumnIndex);
					if (aCell != null) {
						int cellType = aCell.getCellType();
						try {
							switch (cellType) {
							case HSSFCell.CELL_TYPE_NUMERIC: {
								if (HSSFDateUtil.isCellDateFormatted(aCell)) {// 读取日期格式
									value = aCell.getDateCellValue().toString();
								} else {// 读取数字
									value = aCell.getNumericCellValue() + "";
								}
								break;
							}
							case HSSFCell.CELL_TYPE_STRING: {
								value = aCell.getRichStringCellValue().toString();
								break;
							}
							case HSSFCell.CELL_TYPE_FORMULA: {
								value = aCell.getNumericCellValue() + "";
								break;
							}
							default:
								break;
							}
						} catch (Exception e) {
							this.setTaskStatus("执行失败");
							this.setTaskMsg("EXCEL字段格式转换有误");
							return null;
						}
					}
					lineList.add(value);// 存入每一列值
				}
				docList.add(lineList);// 把一行数据存储在列表中
			}
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("导EXCEL出错,文件:" + bean.getDocDir(), e);
		} finally {
			if (null != file) {
				try {
					file.close();
					file = null;
				} catch (IOException e) {
					Log.logError("任务:" + this.getTaskId() + "释放文件错误", e);
				}
			}
		}
		return docList;
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

	public void setBean(Bean bean) {
		this.bean = bean;
	}

	private UtilSql.DataField[] getFieldRule(String docFieldRule) {
		String[] fields = null; // [procedurename=a,varchar];[starttime=b,date,yyyymmdd
		// HHmmss]
		docFieldRule = docFieldRule.replaceAll("/[", "");// procedurename=a,varchar];starttime=b,date,yyyymmdd
		// HHmmss]
		docFieldRule = docFieldRule.replaceAll("/]", "");// procedurename=a,varchar;starttime=b,date,yyyymmdd
		// HHmmss
		if (docFieldRule.indexOf(";") > 0) {
			fields = docFieldRule.split(";");
		}
		UtilSql.DataField[] dataField = new UtilSql.DataField[fields.length];

		for (int i = 0; i < fields.length; i++) {
			String[] field = fields[i].split(","); // starttime=b,date,yyyymmdd
			// HHmmss
			dataField[i] = new UtilSql.DataField();
			dataField[i].fieldIndex = i + 1;// ps.setString()从1开始
			String[] fieldName = field[0].split("=");// starttime=b
			dataField[i].fieldName = fieldName[0];
			dataField[i].fieldColumnIndex = letterToNum(fieldName[1]) - 1;// excel文件中列号
			dataField[i].fieldType = field[1];// date
			if (field.length == 3) {
				dataField[i].fieldFormat = field[2];// yyyymmdd HHmmss
			}
		}
		return dataField;
	}

	// 把excel里的列名A、B...换成序列1、2...
	private int letterToNum(String letters) {
		if (!letters.matches("[a-zA-Z]+")) {
			throw new IllegalArgumentException("Format ERROR!");
		}
		char[] chs = letters.toLowerCase().toCharArray();// a,b
		int result = 0;
		for (int i = chs.length - 1, p = 1; i >= 0; i--) {
			result += getNum(chs[i]) * p;
			p *= 26;
		}
		return result;
	}

	private int getNum(char c) {
		return c - 'a' + 1;
	}

	// public static void main(String[] args) {
	// Bean bean = new Bean();
	// bean.setDbName("otc");
	// bean.setDelSql("delete from proceduretask");
	// bean.setDocDir("D:/源文件夹/proceduretask.xls");
	// bean
	// .setDocFieldRule("[procedurename=a,varchar];[starttime=b,datetime,yyyyMMdd
	// HHmmss];"
	// + "[endtime=c,datetime,yyyy-MM-dd
	// HHmmss];[upatetime=d,datetime,yyyy-MM-dd HHmmss;"
	// +
	// "[param=e,varchar];[i_begdate=f,varchar];[i_enddate=g,varchar];[i_zhcode=h,varchar]");
	// System.out.println(bean.getDocFieldRule());
	// bean.setDocSheetIndex(0);
	// bean.setIgnoreLastLines(0);
	// bean
	// .setInsertSql("insert into
	// proceduretask(procedurename,starttime,endtime,upatetime,"
	// + "param,i_begdate,i_enddate,i_zhcode)values(?,?,?,?,?,?,?,?)");
	// System.out.println(bean.getInsertSql());
	// bean.setStartLine(2);
	// Task task = new Task();
	// task.setBean(bean);
	// // task.fireTask("", "", "", "");
	// System.out.println("success");
	// }
}