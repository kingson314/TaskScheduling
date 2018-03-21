package com.task.FileTransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;

import com.app.Parser;
import com.taskInterface.TaskAbstract;

import common.util.file.UtilFile;
import common.util.json.UtilJson;

public class TaskFile extends TaskAbstract {
	protected Bean bean;
	protected Date date;

	public void fireTask() {
		try {
			this.bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			// 执行文件复制功能
			fileTransTask();
			// 执行插件功能
			if (this.bean.getEnablePlugin() && !getTaskStatus().equals("执行失败")) {
				pluginInvoke();
			}
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("错误: ", e);
		}
	}

	/**
	 * @description 本地文件拷贝
	 * 
	 * (此方案已舍弃) 若两个参数都为null, 则直接取this.srcFilePath和this.destFilePath, 此为不加外挂的情况.
	 * 
	 * ((此方案已舍弃)) 若有一个参数不为null, 则此参数即为temp文件夹的路径,
	 * 另一个参数取this.srcFilePath(或this.destFilePath), 此为加外挂的情况.
	 */
	protected void fileTransTask() {
		if (this.bean.getSrcFileName().length() > 1 && this.bean.getSrcFileName().indexOf("*") < 0) {
			// 目标文件为空，则复制为源文件名
			if (this.bean.getDestFileName().length() < 1)
				this.bean.setDestFileName(this.bean.getSrcFileName());

			String src = this.bean.getSrcFilePath() + this.bean.getSrcFileName();
			String destPath = this.bean.getDestFilePath();
			String dest = destPath + this.bean.getDestFileName();
			try {
				File srcFile = new File(src);
				if (srcFile.exists() == false) {
					this.setTaskStatus("执行失败");
					this.setTaskMsg("文件 " + Parser.removeSlash(src) + " 不存在!");
					return;
				} else {

					if (copyFile(src, dest) == false)
						return;
					if (!this.getTaskMsg().equals("文件已存在，任务忽略")) {
						if (this.bean.getDelSrcFile())
							srcFile.delete();
						String execresult = Parser.removeSlash(this.bean.getSrcFilePath() + this.bean.getSrcFileName()) + "\n" + "到  : "
								+ Parser.removeSlash(this.bean.getDestFilePath() + this.bean.getDestFileName()) + "\n";
						if (!this.bean.getEnablePlugin()) {
							this.setTaskStatus("执行成功");
						} else {
							this.setTaskStatus("等待外挂程序执行");
						}
						this.setTaskMsg(execresult);
					}
				}
			} catch (Exception e) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件拷贝错误: ", e);
			}
		} else if (this.bean.getDestFileName().length() <= 1 // 源文件名包含*与目的文件名为空
				// 做过滤文件夹复制
				&& this.bean.getSrcFileName().indexOf("*") >= 0) {

			String srcpth = this.bean.getSrcFilePath();

			String destPath = this.bean.getDestFilePath();

			if (destPath.endsWith("/") == false && destPath.endsWith("/") == false) {
				destPath = destPath + "/";
			}
			String filter = null;
			if (this.bean.getSrcFileName().indexOf("*") >= 0) {// 获取过滤器
				filter = this.bean.getSrcFileName().toLowerCase().trim().substring(this.bean.getSrcFileName().indexOf("*") + 1);
			}

			File srcFile = new File(srcpth);
			if (srcFile.exists() == false) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件夹 " + Parser.removeSlash(srcpth) + " 不存在!");
				return;
			} else {
				copyAllFileByFilter(srcpth, destPath, filter);
			}
			String execresult = Parser.removeSlash(this.bean.getSrcFilePath() + this.bean.getSrcFileName()) + "\n" + "到  : "
					+ Parser.removeSlash(this.bean.getDestFilePath() + this.bean.getDestFileName()) + "\n";
			if (!this.bean.getEnablePlugin()) {
				this.setTaskStatus("执行成功");
			} else {
				this.setTaskStatus("等待外挂程序执行");
			}
			this.setTaskMsg(execresult);

		} else if (this.bean.getDestFileName().length() <= 1 // 源文件名与目的文件名为空
				// 做全文件夹复制
				&& this.bean.getSrcFileName().length() <= 1) {
			String srcpth = this.bean.getSrcFilePath();
			String destPath = this.bean.getDestFilePath();

			if (destPath.endsWith("/") == false && destPath.endsWith("/") == false) {
				destPath = destPath + "/";
			}

			File srcFile = new File(srcpth);
			if (srcFile.exists() == false) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件夹 " + Parser.removeSlash(srcpth) + " 不存在!");
				return;
			} else {
				copyAllFile(srcpth, destPath);
			}
			String execresult = Parser.removeSlash(this.bean.getSrcFilePath() + this.bean.getSrcFileName()) + "\n" + "到  : "
					+ Parser.removeSlash(this.bean.getDestFilePath() + this.bean.getDestFileName()) + "\n";
			if (!this.bean.getEnablePlugin()) {
				this.setTaskStatus("执行成功");
			} else {
				this.setTaskStatus("等待外挂程序执行");
			}
			this.setTaskMsg(execresult);

		}
	}

	// 执行插件
	protected void pluginInvoke() {
		try {
			String cmd = Parser.parse(this.bean.getPluginPath() + this.bean.getPluginName() + " " + this.bean.getConsoleParam(), date);
			Process process = Runtime.getRuntime().exec(cmd);
			// 如果不考虑temp文件夹, 这里可以不同步, 这样Quartz任务线程就不会一直等着了, 但是也就不能得知外挂是否正常执行了
			int result = process.waitFor();// 此处需要同步一下,否则可能会还没压缩好就去从temp往dest拷贝了
			// result == 0 表示process正常结束
			// 如果外挂出错了不返回, 这个Quartz任务线程就会一直等着(如果是WinRAR的话需要点一下对话框)
			// 如果waitfor这个process太久了, 就不wait了, 然后记录下"外挂程序失败"
			if (result == 0) {
				this.setTaskStatus("执行成功");
				this.setTaskMsg(this.getTaskMsg() + cmd);
			} else {
				this.setTaskStatus("执行失败");
				this.setTaskMsg(this.getTaskMsg() + "外挂程序执行错误:" + cmd);
			}
		} catch (IOException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("外挂程序错误: ", e);
		} catch (InterruptedException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("外挂程序错误: ", e);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("外挂程序错误: ", e);
		}
	}

	// 设置文件后缀
	protected String setFilePostfix(String fileName, String newFilePostfix) {
		StringBuilder sb = new StringBuilder();
		int lastDotIndex = fileName.lastIndexOf(".");
		sb.append(fileName.substring(0, lastDotIndex));
		sb.append(".");
		sb.append(newFilePostfix);
		return sb.toString();
	}

	// 获取文件后缀
	protected String getFilePostfix(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
	}

	// 测试文件是否可以锁定
	protected boolean locked(File File, String FileName) {
		Boolean result = true;
		try {
			FileChannel channel = new RandomAccessFile(File, "rw").getChannel();
			FileLock lock = channel.tryLock();
			lock.release();
			channel.close();
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件 " + Parser.removeSlash(FileName) + " 无法锁定!");
			result = false;
		}
		return result;
	}

	/**
	 * 复制目录下所有文件和目录 过滤后的
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	@SuppressWarnings("static-access")
	protected void copyAllFileByFilter(String oldPath, String newPath, String filter) {

		File file = new File(oldPath);
		if (!file.exists()) {
			return;
		}

		if (file.isFile()) {
			if (file.getAbsolutePath().lastIndexOf(filter) >= 0)
				copyFile(oldPath, newPath);
			return;
		}

		File[] fileList = file.listFiles();
		File mkdir = new File(newPath);

		if (!mkdir.exists()) {
			mkdir.mkdirs();
		}
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				copyAllFileByFilter(oldPath + fileList[i].separator + fileList[i].getName(), newPath + fileList[i].separator + fileList[i].getName(), filter);
			} else {
				if ((oldPath + fileList[i].separator + fileList[i].getName()).lastIndexOf(filter) >= 0)
					copyFile(oldPath + fileList[i].separator + fileList[i].getName(), newPath + fileList[i].separator + fileList[i].getName());

			}
		}
	}

	/**
	 * 复制目录下所有文件和目录
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	@SuppressWarnings("static-access")
	protected void copyAllFile(String oldPath, String newPath) {
		File file = new File(oldPath);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			copyFile(oldPath, newPath);
			return;
		}
		File[] fileList = file.listFiles();
		File mkdir = new File(newPath);

		if (!mkdir.exists()) {
			mkdir.mkdirs();
		}
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				copyAllFile(oldPath + fileList[i].separator + fileList[i].getName(), newPath + fileList[i].separator + fileList[i].getName());
			} else {
				copyFile(oldPath + fileList[i].separator + fileList[i].getName(), newPath + fileList[i].separator + fileList[i].getName());
			}
		}
	}

	/**
	 * 复制目录下单个文件
	 * 
	 * @param oldPass
	 * @param newPath
	 * @return
	 */
	protected boolean copyFile(String oldPath, String newPath) {
		boolean result = false;
		try {
			File file = new File(oldPath);
			if (file.isDirectory()) {
				return true;
			}
			Long fileTime = file.lastModified();
			if (file.exists()) {
				if (this.bean.getLocked() == true) {
					if (file.isFile()) {
						if (locked(file, oldPath) == false)
							return true;
					}
				}

				File destFile = new File(newPath);
				File destFilePath = new File(destFile.getParent());
				if (!destFilePath.exists())
					destFilePath.mkdirs();
				if (!destFile.exists()) {
					destFile.createNewFile();
				} else {
					if (!this.bean.getOverwrite()) {
						this.setTaskStatus("执行成功");
						this.setTaskMsg("文件已存在，任务忽略");
						if (this.bean.getIfCreateOkFile()) {
							UtilFile.createOkFile(newPath);
						}
						return true;
					}
				}
				FileInputStream inputStream = new FileInputStream(file);
				FileOutputStream outputStream = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, len);
				}
				inputStream.close();
				outputStream.close();
				destFile.setLastModified(fileTime);
				if (this.bean.getDelSrcFile())
					file.delete();
				if (this.bean.getIfCreateOkFile()) {
					UtilFile.createOkFile(newPath);
				}
			}
			result = true;
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件拷贝错误: ", e);
			result = false;
		}
		return result;
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}
}
