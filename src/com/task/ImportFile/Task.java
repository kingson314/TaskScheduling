package com.task.ImportFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.taskInterface.TaskAbstract;

import common.util.array.UtilArray;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;

/**
 * 
 * @fun :导入文件到数据与fileToDB文件类型类似，区别于该任务使用更新插入方式，适用少量记录的导入
 * @date:2013-01-20
 */
public class Task extends TaskAbstract {

	public void fireTask() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BufferedReader buffReader = null;
		String sline = "";
		String[] lineArr = null;
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			con = UtilJDBCManager.getConnection(bean.getDbName());
			ps = con.prepareStatement(bean.getInsertSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			File inFile = new File(bean.getFilePath());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" + inFile.getAbsolutePath() + " 不存在");
				return;
			}

			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			int cnt_update = 0;
			int cnt_insert = 0;
			while ((sline = buffReader.readLine()) != null) {
				lineArr = sline.split(bean.getSeparate());
				if (isExits(con, bean, lineArr)) {// 存在则更新
					// 把Key放到最后一个参数
					String[] tmpArr = new String[lineArr.length];
					String[] columnIndex = bean.getKeyColumnIndex().split(bean.getSeparate());
					int j = 0;
					for (int i = 0; i < lineArr.length; i++) {
						if (UtilArray.getArrayIndex(columnIndex, String.valueOf(i)) < 0) {
							tmpArr[j] = lineArr[i];
							j += 1;
						}

					}
					for (int k = 0; k < columnIndex.length; k++) {
						tmpArr[j] = lineArr[Integer.valueOf(columnIndex[k])];
						j += 1;
					}
					UtilSql.executeUpdate(con, bean.getUpdateSql(), (Object[]) tmpArr);
					cnt_update += 1;
				} else {// 插入
					UtilSql.executeUpdate(con, bean.getInsertSql(), (Object[]) lineArr);
					cnt_insert += 1;
				}

			}
			buffReader.close();
			this.setTaskStatus("执行成功");
			this.setTaskMsg(bean.getFilePath() + "\n新增记录： " + cnt_insert + "   更新记录： " + cnt_update);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件导入错误:", e);
		} finally {
			UtilSql.close(con, rs, ps);
		}
	}

	private boolean isExits(Connection con, Bean bean, String[] lineArr) {
		try {
			String[] columnIndex = bean.getKeyColumnIndex().split(bean.getSeparate());
			String[] tmpArr = new String[columnIndex.length];
			int j = 0;
			for (int k = 0; k < columnIndex.length; k++) {
				tmpArr[j] = lineArr[Integer.valueOf(columnIndex[k])];
				j += 1;
			}
			long cnt = UtilSql.queryForCount(con, bean.getKeySql(), (Object[]) tmpArr);
			if (cnt > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		fireTask();
	}

}
