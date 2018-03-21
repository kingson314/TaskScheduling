package com.task.FileCheck;

import java.io.File;

import com.taskInterface.TaskAbstract;

import common.util.json.UtilJson;

public class Task extends TaskAbstract {

	public void fireTask() {
		try {
			Bean fileCheck = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			String FileCheckPath = fileCheck.getFileCheckDir() + fileCheck.getFileCheckName();
			File srcFile = new File(FileCheckPath);

			if (srcFile.exists() == false) {// 文件不存在提示
				String FileCheckNotExistWarning = fileCheck.getFileCheckNotExistWarning().trim();
				if (fileCheck.getIfFileCheckNotExistWarning()) {
					// this.setTaskStatus("执行警告");
					this.setTaskStatus("执行提示");
					this.setTaskMsg(FileCheckNotExistWarning);
				} else {
					this.setTaskStatus("执行成功");
					this.setTaskMsg("文件 " + srcFile.getAbsolutePath() + " 不存在!");
				}
				return;
			} else {// 文件存在提示
				String FileCheckExistWarning = fileCheck.getFileCheckExistWarning().trim();
				if (fileCheck.getIfFileCheckExistWarning()) {
					this.setTaskStatus("执行提示");
					this.setTaskMsg(FileCheckExistWarning);
				} else {
					this.setTaskStatus("执行成功");
					this.setTaskMsg("文件 " + srcFile.getAbsolutePath() + " 存在!");
				}
			}
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("执行错误:", e);
		} finally {
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

}
