package com.app;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.SQLException;

import javax.swing.text.JTextComponent;

import module.about.DialogAbout;
import module.datetype.HolidayDao;
import module.datetype.NowDateDao;
import module.dbconnection.DbConnecitionTable;
import module.dbconnection.DbConnectionDialog;
import module.encrypt.EncryptAESDialog;
import module.ftp.FtpDialog;
import module.ftp.FtpTable;
import module.mail.MailDialog;
import module.mail.MailTable;
import module.systemparam.SysAutoUpdate;
import module.systemparam.SystemParamsDialog;
import module.systemparam.SystemParamsValueDao;

import app.AppConfig;
import app.AppStatus;

import com.dialog.DialogBackupDb;
import com.log.Log;
import com.monitor.MonitorDialog;
import com.monitor.MonitorGroupDialog;
import com.scher.JobOperater;
import com.scher.ScherDao;
import com.scher.ScherParam;
import com.settings.SettingsDialog;
import com.settings.SettingsTable;
import com.taskClass.TaskClassImp;
import com.taskInterface.TaskDao;

import common.component.DoManager;
import common.component.MessageDialog;
import common.component.STextArea;
import common.component.STextField;
import common.component.ShortcutManager;
import common.component.ShowMsg;
import common.util.array.UtilArray;
import common.util.reflect.UtilDynamicLoader;
import common.util.string.UtilString;
import consts.Const;
import consts.ImageContext;
import consts.VariableApp;

/**
 * @info 程序级公共函数
 * 
 * @author fgq 20120831
 * 
 */
public class AppFun {
	private static AppFun appFun = null;

	public static AppFun getInstance() {
		if (appFun == null)
			appFun = new AppFun();
		return appFun;
	}

	// 获取任务类型
	public void loadTaskType() throws MalformedURLException {
		AppVar.TASK_TYPE = TaskClassImp.getTaskTypeFromXml(Const.XmlTaskClass);
		TaskClassImp.mapTaskClass = TaskClassImp.getTasksFromXml(Const.XmlTaskClass);
		if (AppVar.TASK_TYPE == null) {
			ShowMsg.showError("加载任务类型taskClass.xml错误，请检查！");
			System.exit(0);
		}
		String[] jarPath = TaskClassImp.getTaskJarFromXml(Const.XmlTaskClass);
		for (int i = 0; i < jarPath.length; i++) {
			if (jarPath[i].length() > 0)
				UtilDynamicLoader.getInstance().addURL(jarPath[i]);
		}

		AppVar.TASK_TYPE_Combobox = new String[AppVar.TASK_TYPE.length + 1];
		AppVar.TASK_TYPE_Combobox[0] = "";
		for (int i = 0; i < AppVar.TASK_TYPE.length; i++) {
			AppVar.TASK_TYPE_Combobox[i + 1] = AppVar.TASK_TYPE[i];
		}
	}

	// 初始化参数、全局变量
	public void initParam() {
		try {
			loadTaskType();
			File excelDir = new File(Const.TEMP_DIR);
			if (!excelDir.exists())
				excelDir.mkdir();
			// 加载守护任务
			TaskDao.getInstance().modTask(new com.task.Watch.Task());
			VariableApp.systemParamsValue = SystemParamsValueDao.getInstance().getSystemParamsValue();
			// 初始化节假日HashSet
//			HolidayDao.getInstance().loadMapHoliday(VariableApp.systemParamsValue.getDateType());
		} catch (Exception e) {
			Log.logError("主程序初始化参数信息错误:", e);
		} finally {
		}
	}

	// 自动更新
	public void AutoUpdate() throws SQLException {
		if (!AppConfig.getInstance().getMapAppConfig().get("appVersion").equals(AppConfig.version)) {
			if (SysAutoUpdate.getInstance().autoUpdate()) {
				AppConfig.getInstance().updateAppVersion(AppConfig.version);
			}
		}
	}

	// 系统参数对话框

	@SuppressWarnings("deprecation")
	public void sysDialog() {
		try {
			SystemParamsDialog sysDialog = new SystemParamsDialog();
			sysDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开系统参数对话框错误:", e);
		}
	}

	// 数据库连接对话框
	@SuppressWarnings("deprecation")
	public void dbConnDialog() {
		try {
			DbConnectionDialog connDialog = new DbConnectionDialog();
			connDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开数据源配置对话框错误:", e);
		}
	}

	// 配置信息对话框
	@SuppressWarnings("deprecation")
	public void settingsDialog() {
		try {
			SettingsDialog settingsDialog = new SettingsDialog();
			settingsDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开配置信息错误:", e);
		}
	}

	// FTP信息对话框
	@SuppressWarnings("deprecation")
	public void ftpDialog() {
		try {
			FtpDialog ftpDialog = new FtpDialog();
			ftpDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开FTP信息对话框错误:", e);
		}
	}

	// 邮箱信息对话框
	@SuppressWarnings("deprecation")
	public void mailDialog() {
		try {
			MailDialog mailDialog = new MailDialog();
			mailDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开邮箱信息对话框错误:", e);
		}
	}

	// 监控员数对话框
	@SuppressWarnings("deprecation")
	public void monitorDialog() {
		try {
			MonitorDialog monitorDialog = new MonitorDialog();
			monitorDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开监控员信息错误:", e);
		}
	}

	// 监控组对话框
	@SuppressWarnings("deprecation")
	public void monitorGroupDialog() {
		try {
			MonitorGroupDialog monitorGroupDialog = new MonitorGroupDialog();
			monitorGroupDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开监控员信息错误:", e);
		}
	}

	// 备份对话框
	@SuppressWarnings("deprecation")
	public void backupDbDialog() {
		try {
			DialogBackupDb backupDbDialog = new DialogBackupDb();
			backupDbDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开备份数据对话框错误:", e);
		}
	}

	// 加密工具对话框
	@SuppressWarnings("deprecation")
	public void encryptAESDialog() {
		try {
			EncryptAESDialog encryptAESDialog = new EncryptAESDialog();
			encryptAESDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开加密工具错误:", e);
		}
	}

	// about对话框
	@SuppressWarnings("deprecation")
	public void aboutDialog() {
		try {
			DialogAbout aboutDialog = new DialogAbout();
			aboutDialog.show(true);
		} catch (Exception e) {
			Log.logError("主程序打开关于对话框错误:", e);
		}
	}

	// 主程序关闭事件
	public void systemClosing() {
		int msg = ShowMsg.showConfig("确定退出程序? ");
		if (msg == 0) {
			AppExit.getInstance().execExitThread();
		}
	}

	// 程序锁屏事件
	public void systemLock() {
		if (AppMain.appMain.isVisible()) {
			AppMain.appMain.setVisible(false);
			TaskScheduling appLogin = new TaskScheduling("锁屏");
			appLogin.setTitle(AppMain.appMain.getTitle());
			appLogin.setVisible(true);
		}
	}

	// 执行实例化调度
	public void runHistorySche() {
		try {
			ScherParam[] sp = ScherDao.getInstance().getScheParams();
			if (sp == null)
				return;
			int selectcount = 0;
			for (int i = 0; i < sp.length; i++) {
				if (UtilString.isNil(sp[i].getStatus()).equals("正常")) {
					selectcount += 1;
					JobOperater.resumeTrigger(sp[i].getSchCde());
				}
			}
			if (selectcount > 0) {
				JobOperater.refreshSche();
			}
		} catch (Exception e) {
			Log.logError("主程序启动时运行历史调度错误:", e);
		}
	}

	// 判断程序是否运行
	public boolean isRunning(String applicationName) {
		boolean rv = false;
		try {
			String path = System.getProperty("user.dir") + "/temp";
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			RandomAccessFile fis = new RandomAccessFile(path + "/" + applicationName + ".lock", "rw");
			FileChannel lockfc = fis.getChannel();
			FileLock flock = lockfc.tryLock();
			if (flock == null) {
				rv = true;
			}
		} catch (Exception e) {
		}
		return rv;
	}

	// 设置全局快捷键
	public void setGlobalShortCut() {
		try {
			boolean isRight = true;
			{
				// undo快捷键
				String appUnDoShortKey = AppConfig.getInstance().getMapAppConfig().get("appUnDoShortKey");
				String[] appUnDoShortKeyArr = appUnDoShortKey.split("/+");
				int[] unDoShortCutKeyArr = new int[appUnDoShortKeyArr.length];
				for (int i = 0; i < appUnDoShortKeyArr.length; i++) {
					int index = UtilArray.getArrayIndex(ShortcutManager.ShortCut, appUnDoShortKeyArr[i]);
					// 如果有一个设错了，则不设置快捷键
					if (index >= 0) {
						unDoShortCutKeyArr[i] = ShortcutManager.ShortCutKey[index];
					} else {
						isRight = false;
						break;
					}
				}
				if (isRight) {
					ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
						public void handle() {
							DoManager.getInstance().unDo();
						}
					}, unDoShortCutKeyArr);
				}
			}
			isRight = true;
			{
				// redo快捷键
				String appReDoShortKey = AppConfig.getInstance().getMapAppConfig().get("appReDoShortKey");
				String[] appReDoShortKeyArr = appReDoShortKey.split("/+");
				int[] reDoShortCutKeyArr = new int[appReDoShortKeyArr.length];
				for (int i = 0; i < appReDoShortKeyArr.length; i++) {
					int index = UtilArray.getArrayIndex(ShortcutManager.ShortCut, appReDoShortKeyArr[i]);
					// 如果有一个设错了，则不设置快捷键
					if (index >= 0) {
						reDoShortCutKeyArr[i] = ShortcutManager.ShortCutKey[index];
					} else {
						isRight = false;
						break;
					}
				}
				if (isRight) {
					ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
						public void handle() {
							DoManager.getInstance().reDo();
						}
					}, reDoShortCutKeyArr);
				}
			}
			isRight = true;
			{
				// 设置锁屏快捷键
				String appLockShortKey = AppConfig.getInstance().getMapAppConfig().get("appLockShortKey");
				String[] appLockShortKeyArr = appLockShortKey.split("/+");
				int[] lockShortCutKeyArr = new int[appLockShortKeyArr.length];
				for (int i = 0; i < appLockShortKeyArr.length; i++) {
					int index = UtilArray.getArrayIndex(ShortcutManager.ShortCut, appLockShortKeyArr[i]);
					// 如果有一个设错了，则不设置快捷键
					if (index >= 0) {
						lockShortCutKeyArr[i] = ShortcutManager.ShortCutKey[index];
					} else {
						isRight = false;
						break;
					}
				}
				if (isRight) {
					ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
						public void handle() {
							systemLock();
						}
					}, lockShortCutKeyArr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 更新当前执行日期
	public void updateNowDate() {
		NowDateDao.getInstance().setNowDate();
		if (NowDateDao.nowDate != null) {
			if (NowDateDao.nowDate.length() == 8)
				AppStatus.getInstance().setStatus("nowDate", "程序当前执行日期:" + NowDateDao.nowDate.substring(0, 4) + "-" + NowDateDao.nowDate.substring(4, 6) + "-" + NowDateDao.nowDate.substring(6, 8));
		}
	}

	// 获取数据库连接配置

	@SuppressWarnings("deprecation")
	public static void getDbName(final JTextComponent jtxt) {
		final MessageDialog connectionMessageDialog = new MessageDialog("数据库连接配置信息", ImageContext.DbCon, new DbConnecitionTable().getJtable());
		connectionMessageDialog.jTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					String dbName = connectionMessageDialog.jTable.getValueAt(connectionMessageDialog.jTable.getSelectedRow(), 0).toString();
					jtxt.setText(dbName);
					connectionMessageDialog.dispose();
				}
			}
		});
		connectionMessageDialog.show(true);
	}

	// 获取设置配置

	// 弹出配置信息对话框，可以选择相关的配置信息
	@SuppressWarnings("deprecation")
	public static void getSet(final STextArea jtxt, String setName) {
		final MessageDialog settingsMessageDialog = new MessageDialog("配置信息", ImageContext.Settings, new SettingsTable(setName).getJtable());
		settingsMessageDialog.jTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					String setValue = settingsMessageDialog.jTable.getValueAt(settingsMessageDialog.jTable.getSelectedRow(), 2).toString();
					jtxt.setText(setValue);
					settingsMessageDialog.dispose();
				}
			}
		});
		settingsMessageDialog.show(true);
	}

	// 获取ftp配置

	// 弹出FTP信息对话框，可以选择已配置的FTP配置
	@SuppressWarnings("deprecation")
	public static void getftp(final STextField jtxt) {
		final MessageDialog ftpMessageDialog = new MessageDialog("FTP信息", ImageContext.Ftp, new FtpTable().getJtable());
		ftpMessageDialog.jTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					jtxt.setText("ftp://%ftpname:" + ftpMessageDialog.jTable.getValueAt(ftpMessageDialog.jTable.getSelectedRow(), 1).toString() + "%/");
					ftpMessageDialog.dispose();
				}
			}
		});
		ftpMessageDialog.show(true);
	}

	// 获取数邮箱地址配置

	// 弹出Mail对话框，可以选择已配置的mail地址
	@SuppressWarnings("deprecation")
	public static void getMail(final STextField jtxt) {
		final MessageDialog mailMessageDialog = new MessageDialog("邮箱信息", ImageContext.Mail, new MailTable().getJtable());
		mailMessageDialog.jTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					String dbName = mailMessageDialog.jTable.getValueAt(mailMessageDialog.jTable.getSelectedRow(), 0).toString();
					jtxt.setText(dbName);
					mailMessageDialog.dispose();
				}
			}
		});
		mailMessageDialog.show(true);
	}
}
