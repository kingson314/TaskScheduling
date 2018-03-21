package com.app;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import app.AppCon;
import app.AppConfig;
import app.AppServer;

import com.log.Log;
import com.log.abridgement.LogAbridgement;
import com.log.abridgement.LogAbridgementDao;
import com.scher.JobOperater;
import com.scher.ScherDao;
import com.threadPool.ThreadPool;

import common.component.SLabel;
import common.component.ShowMsg;
import common.util.jdbc.UtilJDBCManager;
import common.util.string.UtilString;
import consts.ImageContext;

/**
 * @info 程序退出
 * 
 * @author fgq 20120831
 * 
 */
public class AppExit extends JDialog {
	private static final long serialVersionUID = 1L;
	private SLabel lImg;
	private JProgressBar pbClose;
	private final int[] pgValue = new int[] { 10, 20, 30, 85, 90, 95, 100 };
	private final String[] pgMsg = new String[] { "记录当前调度信息", "停止任务调度", "等待为完成的任务线程执行", "记录内存日志摘要信息", "关闭Jetty", "关闭数据库连接", "程序安全退出" };
	private Thread thread;

	private static AppExit dialogExit = null;

	// public static void main(String[] args) {
	//
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// new AppExit();
	// }
	// });
	// }

	public static AppExit getInstance() {
		if (dialogExit == null)
			dialogExit = new AppExit();
		return dialogExit;
	}

	// 构造
	private AppExit() {
		try {
			initGUI();
		} catch (Exception e) {
			Log.logError("退出程序对话框构造错误:", e);
		} finally {
		}
	}

	// 初始化界面
	private void initGUI() {
		try {
			// this.setAlwaysOnTop(true);
			// this.setUndecorated(true);
			String appLookAndFell = UtilString.isNil(AppConfig.getInstance().getMapAppConfig().get("appLookAndFeel"), "默认风格");
			if ("默认风格".equals(appLookAndFell)) {
				this.setSize(520, 110);
			} else
				this.setSize(520, 110);
			Image img = Toolkit.getDefaultToolkit().getImage(ImageContext.SystemExit);
			this.setIconImage(img);
			this.setTitle("程序关闭中...");
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			this.setLayout(null);
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {// 添加窗体退出事件
						public void windowClosing(java.awt.event.WindowEvent evt) {
							close();
						}
					});
			{
				{
					lImg = new SLabel("", ImageContext.SystemExitUI);
					lImg.setBounds(0, 0, this.getWidth(), 55);
					this.add(lImg);
				}
				{
					pbClose = new JProgressBar();
					pbClose.setBounds(0, 50, this.getWidth(), 25);
					pbClose.setValue(0);
					pbClose.setString(pgMsg[0]);
					pbClose.setDoubleBuffered(true);
					pbClose.setForeground(new java.awt.Color(17, 106, 196));
					pbClose.setStringPainted(true);
					this.add(pbClose);
				}

			}
			this.setVisible(true);
		} catch (Exception e) {
			Log.logError("启动对话框初始化界面错误:", e);
		} finally {
		}
	}

	// 设置进度条信息
	private void setprogressAndMsg(int index) {
		pbClose.setValue(pgValue[index]);
		pbClose.setString(pgMsg[index]);
	}

	// 设置延时进度条
	private void setprogress() {
		int value = pbClose.getValue() + 1;
		if (value <= 90) {
			pbClose.setValue(value);
		}
	}

	// 设置调度线程信息
	private void setString(String scheCode, String scheName) {
		pbClose.setString("调度:" + scheName + "[" + scheCode + "]线程执行中,请等待...");
	}

	// 退出系统线程
	public void execExitThread() {
		this.thread = new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					// 隐藏主界面
					AppMain.appMain.setVisible(false);
					dialogExit.setprogressAndMsg(0);
					if (AppBoot.hadNowDate != 2)
						JobOperater.refreshSche();// 刷新调度状态
					dialogExit.setprogressAndMsg(1);
					// 关闭调度线程池
					JobOperater.shutDown();
					dialogExit.setprogressAndMsg(2);
					// 关闭线程池
					ThreadPool.getPool().shutDown();
					// 等待线程执行完成
					String scheCode = "";
					while (true) {
						boolean flag = true;
						Iterator<?> itFuture = ThreadPool.MapFuture.entrySet().iterator();
						while (itFuture.hasNext()) {
							Entry<String, LogAbridgement> entry = (Entry<String, LogAbridgement>) itFuture.next();
							Future<?> future = (Future<?>) entry.getValue();
							if (!future.isDone()) {
								flag = false;

								if (!scheCode.equals(entry.getKey().split("/|")[0])) {
									scheCode = entry.getKey().split("/|")[0];
									String scheName = ScherDao.getInstance().getScheNameFromScheCode(scheCode);
									if (scheName.equals(""))// 手工
										scheName = scheCode;
									dialogExit.setString(scheCode, scheName);
								}
								break;
							}
						}
						if (flag == true) {
							dialogExit.setprogressAndMsg(3);
							break;
						} else {
							try {
								dialogExit.setprogress();
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// e.printStackTrace();
							}
						}
					}

				} catch (Exception e) {
					Log.logError("程序退出错误:", e);
				} finally {
					// 记录高频任务摘要
					LogAbridgementDao.getInstance().addAllLogAbridgement();
					dialogExit.setprogressAndMsg(4);
					// 停止Jetty
					AppServer.getInstance().stopJetty();
					dialogExit.setprogressAndMsg(5);
					// 关闭本程序的数据库连接
					UtilJDBCManager.closeConnection(UtilJDBCManager.getConnection(AppCon.DbconApp));
					dialogExit.setprogressAndMsg(6);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					dialogExit.dispose();
					System.exit(0);
				}
			}
		};
		this.thread.start();
	}

	// 强制退出程序
	private void close() {
		int msg = ShowMsg.showConfig(this, "是否强制退出程序?");
		if (msg == 0) {
			try {
				this.thread.interrupt();
				LogAbridgementDao.getInstance().addAllLogAbridgement();
				UtilJDBCManager.closeConnection(UtilJDBCManager.getConnection(AppCon.DbconApp));
			} catch (Exception e) {
				Log.logError("程序强制退出错误:", e);
			} finally {
				this.dispose();
				System.exit(0);
			}
		}
	}
}
