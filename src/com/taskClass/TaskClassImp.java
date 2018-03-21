package com.taskClass;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;

import common.util.reflect.UtilDynamicLoader;

//子任务类
public class TaskClassImp {
	public static Map<String, TaskClass> mapTaskClass = null;
	public static int TaskCount = 0;

	// 从xml中获取子任务类
	public static Map<String, TaskClass> getTasksFromXml(String filePath) {
		ConcurrentHashMap<String, TaskClass> mapTaskClass = new ConcurrentHashMap<String, TaskClass>();
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			if (file.exists()) {
				Document document = reader.read(file);
				Element root = document.getRootElement();
				List<?> listRoot = root.elements();
				for (int i = 0; i < listRoot.size(); i++) {
					Element eTaskType = (Element) listRoot.get(i);
					TaskClass taskClass = new TaskClass();
					taskClass.setTaskType(eTaskType.getText().trim());
					List<?> eTaskClassDetail = eTaskType.elements();
					taskClass.setJarPath(((Element) eTaskClassDetail.get(0)).getText());
					taskClass.setTaskPath(((Element) eTaskClassDetail.get(1)).getText());
					taskClass.setPanelPath(((Element) eTaskClassDetail.get(2)).getText());
					mapTaskClass.put(taskClass.getTaskType(), taskClass);
				}
			}
		} catch (Exception e) {
			Log.logError("getTasksFromXml错误:", e);
		}
		return mapTaskClass;
	}

	// 从xml中获取子任务类型数组
	public static String[] getTaskTypeFromXml(String filePath) {
		String[] taskType = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			if (file.exists()) {
				Document document = reader.read(file);
				Element root = document.getRootElement();
				List<?> listRoot = root.elements();
				taskType = new String[listRoot.size()];
				for (int i = 0; i < listRoot.size(); i++) {
					Element eTaskType = (Element) listRoot.get(i);
					taskType[i] = eTaskType.getText().trim();
				}
			}
		} catch (Exception e) {
			Log.logError("getTaskTypeFromXml错误:", e);
		}
		return taskType;
	}

	// 从xml中获取子任务类jar路径
	public static String[] getTaskJarFromXml(String filePath) {
		String[] taskJar = null;
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			if (file.exists()) {
				Document document = reader.read(file);
				Element root = document.getRootElement();
				List<?> listRoot = root.elements();
				taskJar = new String[listRoot.size()];
				for (int i = 0; i < listRoot.size(); i++) {
					Element eTaskType = (Element) listRoot.get(i);
					TaskClass taskClass = new TaskClass();
					taskClass.setTaskType(eTaskType.getText().trim());
					List<?> eTaskClassDetail = eTaskType.elements();
					taskClass.setJarPath(((Element) eTaskClassDetail.get(0)).getText());
					taskClass.setTaskPath(((Element) eTaskClassDetail.get(1)).getText());
					taskClass.setPanelPath(((Element) eTaskClassDetail.get(2)).getText());
					taskJar[i] = ((Element) eTaskClassDetail.get(0)).getText();
				}
			}

		} catch (Exception e) {
			Log.logError("getTaskJarFromXml错误:", e);
		}
		return taskJar;
	}

	// 获取子任务对象
	public static ITask getTask(TaskClass taskClass) {
		String className = taskClass.getTaskPath();
		ITask task = null;
		try {
			task = (ITask) Class.forName(className, true, UtilDynamicLoader.getInstance()).newInstance();
		} catch (InstantiationException e) {
			Log.logError("获取任务类" + className + "错误1:", e);
		} catch (IllegalAccessException e) {
			Log.logError("获取任务类" + className + "错误2:", e);
		} catch (ClassNotFoundException e) {
			Log.logError("获取任务类" + className + "错误3:", e);
		}
		return task;
	}

	// 获取子任务面板
	public static ITaskPanel getTaskPanel(TaskClass taskClass) {
		String className = taskClass.getPanelPath();
		ITaskPanel taskPanel = null;
		try {
			taskPanel = (ITaskPanel) Class.forName(className, true, UtilDynamicLoader.getInstance()).newInstance();
		} catch (InstantiationException e) {
			Log.logError("获取任务PANEL类" + className + "错误1:", e);
		} catch (IllegalAccessException e) {
			Log.logError("获取任务PANEL类" + className + "错误2:", e);
		} catch (ClassNotFoundException e) {
			Log.logError("获取任务PANEL类" + className + "错误3:", e);
		}
		return taskPanel;
	}

}
