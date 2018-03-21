package com.task.SimplifyPriceFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.log.Log;
import com.taskInterface.TaskAbstract;

import common.util.Math.UtilMath;
import common.util.file.UtilFile;
import common.util.json.UtilJson;

/**
 * @Description:之前mt4中EA(getData)导出数据格式为Bars,timeServer,dateServer,timeLocal,dateLocal,Ask,Bid,Open[0],Close[0],High[0],Low[0],ma5,ma20,ma60,kdj,Volume[0]
 * @date May 26, 2014
 * @author:fgq
 */
// @2014-05-26现在简化之前导出的txt文件为Bars,timeServer{HHmmss},Ask,Open[0],Close[0],High[0],Low[0],round(ma5,4),round(ma20,4),round(ma60,4),round(kdj,4),Volume[0]
// @2014-05-31 再度简化之前导出的txt文件为timeServer{HHmmss},Close[0],Volume[0]
public class Task extends TaskAbstract {

	public void fireTask() {
//		check();
		BufferedReader buffReader = null;
		String[] lineArr = null;
		Bean bean = null;
		String sline = "";
		int cnt = 0;
		StringBuilder sbOut = new StringBuilder();
		try {
			bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			Log.logInfo("正在转换文件:" + bean.getFilePath());
			File inFile = new File(bean.getFilePath());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" + inFile.getName() + " 不存在");
				return;
			}
			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			int bars = 0;
			while ((sline = buffReader.readLine()) != null) {
				lineArr = sline.split(bean.getSeparate());
				int curBar = Integer.valueOf(lineArr[0]);
				if (curBar < bars)
					break;
				
				if (  bars == 0 &&  lineArr.length == 3) {
						this.setTaskStatus("执行成功");
						this.setTaskMsg(bean.getFilePath() + " 不需转换");
						return;
				}else  if (lineArr.length == 12) {
					sbOut.append(lineArr[1]).append(bean.getSeparate()).append(lineArr[4]).append(bean.getSeparate()).append(lineArr[11]);
					sbOut.append("\n");
					cnt += 1;
				}else if (lineArr.length == 16) {
					for (int i = 0; i < lineArr.length; i++) {
						if (i == 2 || i == 3 || i == 4 || i == 6)
							continue;
						if (i == lineArr.length - 1) {
							sbOut.append(lineArr[i]);
						} else {
							if (i == 1) {
								sbOut.append(lineArr[i].replace(":", "")).append(bean.getSeparate());
							} else if (i == 11 || i == 12 || i == 13 || i == 14) {
								sbOut.append(UtilMath.round(Double.valueOf(lineArr[i]), 4)).append(bean.getSeparate());
							} else {
								sbOut.append(lineArr[i]).append(bean.getSeparate());
							}
						}
					}
					sbOut.append("\n");
					cnt += 1;
				} else{
					sbOut.append(sline).append("\n");
				}
				bars = curBar;
			}
			this.setTaskStatus("执行成功");
			this.setTaskMsg(inFile.getName()+ "\n转换记录： " + cnt);
			buffReader.close();
			UtilFile.writeFile(bean.getFilePath(), sbOut.toString());
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件转换错误:", e);
		}
	}

	/**
	* @Description:检查文件是否只剩下timeServer{HHmmss},Close[0],Volume[0]3列
	* void
	* @date 2014-6-8
	* @author:fgq
	 */
	public void check(){
		BufferedReader buffReader = null;
		String[] lineArr = null;
		Bean bean = null;
		String sline = "";
		try {
			bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			File inFile = new File(bean.getFilePath());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" + inFile.getName() + " 不存在");
				return;
			}
			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			int i=0;
			while ((sline = buffReader.readLine()) != null) {
				lineArr = sline.split(bean.getSeparate());
				 i+=1;
				if (lineArr.length!= 3) {
						this.setTaskStatus("执行成功");
						this.setTaskMsg("");
						System.out.println(inFile.getName()+":"+i);
						return;
				} 
			}
			this.setTaskStatus("执行成功");
			this.setTaskMsg(inFile.getName());
			buffReader.close();
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件转换错误:", e);
		}
	}
	public void fireTask20140526() {
		BufferedReader buffReader = null;
		String[] lineArr = null;
		Bean bean = null;
		String sline = "";
		int cnt = 0;
		StringBuilder sbOut = new StringBuilder();
		try {
			bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			Log.logInfo("正在转换文件:" + bean.getFilePath());
			File inFile = new File(bean.getFilePath());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" +inFile.getName() + " 不存在");
				return;
			}
			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			while ((sline = buffReader.readLine()) != null) {
				try {
					lineArr = sline.split(bean.getSeparate());
					if (lineArr.length == 16) {
						for (int i = 0; i < lineArr.length; i++) {
							if (i == 2 || i == 3 || i == 4 || i == 6)
								continue;
							if (i == lineArr.length - 1) {
								sbOut.append(lineArr[i]);
							} else {
								if (i == 1) {
									sbOut.append(lineArr[i].replace(":", "")).append(bean.getSeparate());
								} else if (i == 11 || i == 12 || i == 13 || i == 14) {
									sbOut.append(UtilMath.round(Double.valueOf(lineArr[i]), 4)).append(bean.getSeparate());
								} else {
									sbOut.append(lineArr[i]).append(bean.getSeparate());
								}
							}
						}
						sbOut.append("\n");
						cnt += 1;
					} else {
						this.setTaskStatus("执行成功");
						this.setTaskMsg(inFile.getName() + " 不需转换");
						continue;
					}
				} catch (Exception e) {
					Log.logError(sline + "\n", e);
					continue;
				}
			}
			this.setTaskStatus("执行成功");
			this.setTaskMsg(inFile.getName()+ "\n转换记录： " + cnt);
			buffReader.close();
			UtilFile.writeFile(bean.getFilePath(), sbOut.toString());
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件转换错误:", e);
		}
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		fireTask();
	}

}
