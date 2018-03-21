package com.task.ExportExcel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import module.dbconnection.DbConnectionDao;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.app.Parser;
import com.taskInterface.TaskAbstract;

import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;

public class Task extends TaskAbstract {

	// 执行
	public void fireTask() {
		// SqlSession sesion = null;
		Connection con = null;
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			if (bean.getSql().length() < 1) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("Sql为空");
				return;
			}

			con = UtilJDBCManager.getConnection(DbConnectionDao.getInstance().getMapDbConn(bean.getDbName()));
			ArrayList<HashMap<Integer, String>> dataList = UtilSql.executeSqlByOrder(con, bean.getSql(), UtilString.getSqlRuleParam(bean.getSqlRule()));
			HSSFWorkbook book = getHSSFWorkbook(dataList, bean.getDocName(), bean.getTitle());

			File f = new File(bean.getDocPath() + bean.getDocName());
			File fpath = new File(f.getParent());
			if (fpath.exists() == false)
				fpath.mkdirs();
			f.createNewFile();
			FileOutputStream fileoutputStream = new FileOutputStream(f);
			book.write(fileoutputStream);
			fileoutputStream.close();
			this.setTaskStatus("执行成功");
			String execResult = Parser.removeSlash(bean.getDocPath()) + Parser.removeSlash(bean.getDocName());
			this.setTaskMsg(execResult);
		} catch (SQLException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("获取Excel数据集错误:", e);
		} catch (IOException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("创建Excel文件错误:", e);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("执行错误:", e);
		} finally {
			UtilSql.close(con);
			// Sql.close(con, sesion);
		}
	}

	// 导出doc
	private HSSFWorkbook getHSSFWorkbook(ArrayList<HashMap<Integer, String>> dataList, String sheetName, String sheetTitle) {
		String[] title = sheetTitle.split(";");
		HSSFWorkbook hb = new HSSFWorkbook();
		// 设置文档名称
		HSSFSheet sheet = hb.createSheet(UtilString.isNil(sheetName, " "));
		int rowNum = 0;
		HSSFRow titleRow = sheet.createRow(rowNum);
		for (int i = 0; i < title.length; i++) {
			insertCell(titleRow, i, title[i]);
		}
		rowNum += 1;
		for (Map<Integer, String> data : dataList) {
			HSSFRow row = sheet.createRow(rowNum);
			int i = 0;
			// for (i=0;i<data.entrySet().size();i++)Entry<Integer, String>
			// entry : data.entrySet()) {
			for (@SuppressWarnings("unused")
			Entry<Integer, String> entry : data.entrySet()) {
				// Object value = entry.getValue();
				insertCell(row, i, data.get(i));
				i += 1;
			}
			rowNum += 1;
		}
		return hb;
	}

	// 填充格子值
	private void insertCell(HSSFRow row, int cellNum, Object value) {
		HSSFCell cell = row.createCell(Short.valueOf(String.valueOf(cellNum)));
		if (value instanceof Double && value != null) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			Double dv = (Double) value;
			cell.setCellValue(dv);
		} else if (value != null) {
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			HSSFRichTextString rtsValue = new HSSFRichTextString(value.toString());
			cell.setCellValue(rtsValue);
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

}