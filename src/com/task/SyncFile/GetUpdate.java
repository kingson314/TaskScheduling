package com.task.SyncFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import common.util.conver.UtilConver;

public class GetUpdate {
	public static void main(String[] args) throws Exception {
		String fmDate = "yyyy-MM-dd";
		String oldPath = "E:/fgq/swjoa"; // 过滤掉的文件夹
		String newPath = "E:/fgq/swjoa" + UtilConver.dateToStr("yyyyMMdd");

		// 不对比包含这些字符的源文件夹，模糊
		String[] exceptSrcPath = new String[] { ".svn" ,"WEB-INF"};
		// 不对比包含这些字符的源文件，模糊
		String[] exceptSrcFilePath = new String[] { ".svn",".class" };

		// 目标目录的文件夹
		String[] exceptDestFilePath = new String[] {};
		// 复制文件时间大于该时间的文件
		//String modifyTime = UtilDate.addDateMinut(UtilConver.dateToStr(fmDate), -1440 * 2, fmDate);
		String modifyTime ="2014-06-23";
		System.out.println("限定大于该时间：" + modifyTime);
		// 1 清除已存在目录
		deleteDirectory(newPath);
		// 2 拷贝更新的文件
		copyAllFile(oldPath, newPath, modifyTime, fmDate, exceptSrcPath, exceptSrcFilePath);
		// 3 清除指定的目录
		for (String path : exceptDestFilePath)
			deleteDirectory(path);
		// 3 清除空的目录
		deleteEmptyDir(new File(newPath));
		System.out.println("done");
	}

	/**
	 * 复制目录下所有文件和目录
	 * 
	 * @param oldPath
	 * @param newPath
	 */
	@SuppressWarnings("static-access")
	private static void copyAllFile(String oldPath, String newPath, String modifyTime, String fmDate, String[] exceptSrcPath, String[] exceptSrcFilePath) {
		File file = new File(oldPath);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			copyFile(oldPath, newPath, modifyTime, fmDate,exceptSrcFilePath);
			return;
		}
		File[] fileList = file.listFiles();
		File mkdir = new File(newPath);

		if (!mkdir.exists()) {
			mkdir.mkdirs();
		}
		for (int i = 0; i < fileList.length; i++) {
			String old = (oldPath + fileList[i].separator + fileList[i].getName()).replaceAll("\\\\", "/");
			String new1 = (newPath + fileList[i].separator + fileList[i].getName());
			if (fileList[i].isDirectory()) {
				boolean ingored = false;
				for (String path : exceptSrcPath) {
					if (oldPath.indexOf(path) >= 0) {
						ingored = true;
						break;
					}
				}
				if (!ingored)
					copyAllFile(old, new1, modifyTime, fmDate, exceptSrcPath, exceptSrcFilePath);
			} else {
				copyFile(old, new1, modifyTime, fmDate,exceptSrcFilePath);
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
	private static boolean copyFile(String oldPath, String newPath, String modifyTime, String fmDate,String[]exceptSrcFilePath) {
		boolean result = false;
		try {
			for (String filePath : exceptSrcFilePath) {
				if (oldPath.indexOf(filePath) >= 0)
					return false ;
			}
			File file = new File(oldPath);
			if (file.isDirectory()) {
				return true;
			}
			Long fileTime = file.lastModified();
			String sfileTime = UtilConver.dateToStr(new Date(fileTime), fmDate);
			if (sfileTime.compareTo(modifyTime) <= 0)
				return true;
			System.out.println(file.getAbsolutePath() + "修改时间：" + sfileTime);
			if (file.exists()) {
				File destFile = new File(newPath);
				File destFilePath = new File(destFile.getParent());
				if (!destFilePath.exists())
					destFilePath.mkdirs();
				if (!destFile.exists()) {
					destFile.createNewFile();
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
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 循环删除空的文件夹
	 * 
	 * @param dir
	 */
	private static void deleteEmptyDir(File dir) {
		if (dir.isDirectory()) {
			File[] fs = dir.listFiles();
			if (fs != null && fs.length > 0) {
				for (int i = 0; i < fs.length; i++) {
					File tmpFile = fs[i];
					if (tmpFile.isDirectory()) {
						deleteEmptyDir(tmpFile);
					}
					if (tmpFile.isDirectory() && tmpFile.listFiles().length <= 0) {
						tmpFile.delete();
					}
				}
			}
			if (dir.isDirectory() && dir.listFiles().length == 0) {
				dir.delete();
			}
		}
	}
}
