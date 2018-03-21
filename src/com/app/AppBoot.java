package com.app;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JProgressBar;
import javax.swing.JWindow;

import module.datetype.NowDateDao;
import app.AppCon;
import app.AppConfig;
import app.AppLookAndFeel;
import app.AppServer;
import app.AppStatus;

import com.log.Log;
import com.scher.ScherDao;
import common.component.SLabel;
import common.component.SSplitPane;
import common.component.ShowMsg;
import common.util.log.UtilLog;
import common.util.string.UtilString;

import consts.ImageContext;

/**
 * @info 程序启动
 * 
 * @author fgq 20120831
 * 
 */
public class AppBoot extends JWindow {
	private static final long serialVersionUID = 1L;
	private SSplitPane splt;
	private JProgressBar pbBoot;
	private SLabel lImag;
	private Thread thread;
	private final int[] pgValue = new int[] { 10, 20, 30, 90, 100 };
	private final String[] pgMsg = new String[] { "程序自动检测更新", "程序初始化", "程序获取运行时日期", "程序初始化界面", "程序启动成功" };
	public static int hadNowDate;

	private static AppBoot dialogBoot = null;
	private static long bootTime;
	private final static long BootoverTime = 60;

	// public static void main(String[] args) {
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// AppBoot inst = new AppBoot();
	// inst.setVisible(true);
	// }
	// });
	// }

	public static AppBoot getInstance() {
		if (dialogBoot == null)
			dialogBoot = new AppBoot();
		return dialogBoot;
	}

	// 构造
	private AppBoot() {
		try {
			initGUI();
			String appLookAndFell = UtilString.isNil(AppConfig.getInstance().getMapAppConfig().get("appLookAndFeel"), "默认风格");
			if (!"默认风格".equals(appLookAndFell)) {
				AppLookAndFeel.getInstance().updateLookAndFeel(appLookAndFell);
			}
		} catch (Exception e) {
			UtilLog.logError("启动对话框构造错误:", e);
		}
	}

	// 设置进度条信息
	private void setprogressAndMsg(int index) {
		pbBoot.setValue(pgValue[index]);
		pbBoot.setString(pgMsg[index]);
	}

	// 设置延时进度条
	private void setprogress() {
		int value = pbBoot.getValue() + 4;
		if (value <= 90) {
			pbBoot.setValue(value);
		}
	}

	// 启动系统线程
	public void execBootThread() {
		try {
			bootTime = System.currentTimeMillis();
			this.thread = new Thread() {
				public void run() {
					try {
						dialogBoot.setprogressAndMsg(0);
						try {
							new AppCon();// 获取App连接
							AppFun.getInstance().AutoUpdate();
						} catch (Exception e) {// 系统数据库连接错误时处理
							try {
								dialogBoot.setprogressAndMsg(1);
								AppFun.getInstance().loadTaskType();
								AppMain.appMain = new AppMain();
								Log.showLog(AppLogView.LogSystem, "系统数据库错误:\n  " + e.getMessage(), true);
								dialogBoot.dispose();
								AppMain.appMain.setVisible(true);
								AppMain.appMain.disabled(0);
							} catch (Exception e1) {
							}
							return;
						}
						dialogBoot.setprogressAndMsg(1);
						AppFun.getInstance().initParam();

						if (AppFun.getInstance().isRunning(// 已有运行实例时退出本次运行
								AppConfig.getInstance().getMapAppConfig().get("appTitle"))) {
							pbBoot.setValue(0);
							ShowMsg.showWarn(dialogBoot, AppConfig.getInstance().getMapAppConfig().get("appTitle") + " 正在运行中...");
							dialogBoot.dispose();
							System.exit(0);
						}
						dialogBoot.setprogressAndMsg(2);
						Thread threadSysDate = new Thread() {// 启动线程获取当前系统日期
							public void run() {
								try {
									getSysDate(dialogBoot);
								} catch (Exception e) {
									// UtilLog.logError("程序退出错误:", e);
								}
							}
						};
						threadSysDate.start();

						while (true) {// 当前线程检测是否已获取了当前系统日期
							long wasteTime = (System.currentTimeMillis() - bootTime) / 1000;
							// System.out.println("启动耗时:"+wasteTime);
							if (hadNowDate != 2 && wasteTime >= BootoverTime) {
								hadNowDate = 2;
								ShowMsg.showWarn(dialogBoot, "启动超时，请检查预设历史日期相关配置！");
								break;
							}

							if (hadNowDate == 1 || hadNowDate == 2) {
								break;
							} else if (hadNowDate == -1) {
								// pbBoot.setValue(pgValue[3]);
							} else {
								dialogBoot.setprogress();
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							}

						}

						dialogBoot.setprogressAndMsg(3);
						AppMain.appMain = new AppMain();
						dialogBoot.setprogressAndMsg(4);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						dialogBoot.dispose();
						AppMain.appMain.setVisible(true);

						// 执行在表中实例化的调度
						if (hadNowDate != 2) {
							AppFun.getInstance().runHistorySche();
							// 启动jetty
							AppServer.getInstance().autoStartJetty();
						} else {
							// AppMain.appMain.disabled(1);
						}
					} catch (Exception e) {
						ShowMsg.showError("启动错误:" + e.getMessage());
						dialogBoot.dispose();
						System.exit(0);
					} finally {
//						int count = 0;
//						int nullcount = 0;
//						 for (int i = 64991; i<= 80296; i++) {
//							Connection con = null;
//							try {
//								Thread.sleep(1000);
//								count++;
//								System.out.print(i + ":      " + count);
//								System.out.print("      " + UtilConver.dateToStr("HH:mm:ss"));
//								String url = "";
//								String title = Test.gethtml(url, "title").toString();
//								if ("".equals(UtilString.isNil(title))) {
//									nullcount++;
//									System.out.println("         " + UtilConver.dateToStr("HH:mm:ss") + "      空:" + nullcount);
//									continue;
//								}
//								String content = Test.gethtml(url, "n_bd").toString();
//								String sql = "insert into html(type,idx,title,content)values(?,?,?,?)";
//								con = UtilJDBCManager.getOracleConn(Const.DbName);
//								UtilSql.executeUpdate(con, sql, new Object[] { "21", i, title, content });
//							} catch (Exception e) {
//								e.printStackTrace();
//								count--;
//								 i--;
//								try {
//									Thread.sleep(30000);
//								} catch (InterruptedException e1) {
//								}
//								continue;
//							} finally {
//								UtilSql.close(con);
//							}
//							System.out.println("         " + UtilConver.dateToStr("HH:mm:ss") + "  共" + (count - nullcount));
//						}
//						System.out.println("done");
					}
				}
			};
			this.thread.start();
		} catch (Exception e) {
			ShowMsg.showError("启动错误:" + e.getMessage());
			this.dispose();
			System.exit(0);
		}
	}

	// 初始化界面
	private void initGUI() {
		try {
			this.setSize(530, 340);
			// this.setUndecorated(true);
			// this.setAlwaysOnTop(true);
			// this.setTitle("程序启动中...");
			// this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			// this.addWindowListener(new WindowAdapter() {// 添加窗体退出事件
			// public void windowClosing(java.awt.event.WindowEvent evt) {
			// close();
			// }
			// });

			// 有关闭按钮时
			// this.setUndecorated(false);
			// final int pgHeigth = 50;

			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);

			{
				splt = new SSplitPane(0, this.getHeight() - 25, false);
				splt.setDividerSize(1);
				getContentPane().add(splt, BorderLayout.CENTER);
				{
					lImag = new SLabel("", ImageContext.SystemBootUI);
					splt.add(lImag, SSplitPane.TOP);
				}
				{
					pbBoot = new JProgressBar();
					pbBoot.setValue(0);
					pbBoot.setString(pgMsg[0]);
					pbBoot.setDoubleBuffered(true);
					pbBoot.setBorder(null);
					// pbBoot.setBorder(new LineBorder(new java.awt.Color(128,
					// 128, 128), 1, false));
					pbBoot.setForeground(new java.awt.Color(17, 106, 196));
					pbBoot.setStringPainted(true);
				}
				splt.add(pbBoot, SSplitPane.BOTTOM);
				this.setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			UtilLog.logError("启动对话框初始化界面错误:", e);
		}
	}

	// 强制退出启动
	@SuppressWarnings("unused")
	private void close() {
		int msg = ShowMsg.showConfig(this, "是否强制退出程序?");
		if (msg == 0) {
			try {
				this.thread.interrupt();
			} catch (Exception e) {
				UtilLog.logError("程序强制退出错误:", e);
			} finally {
				this.dispose();
				System.exit(0);
			}
		}
	}

	// 启动时获取一次系统当前执行日期，防止调度因获取该日期时因延迟而启动时启动多次
	private void getSysDate(JWindow dialog) {
		hadNowDate = 0;
		try {
			boolean isNowDate = ScherDao.getInstance().isUseNowDate();
			if (NowDateDao.getInstance().getNowDate(isNowDate, "程序启动:").equals("")) {
				if (hadNowDate != 2) {
					ShowMsg.showWarn(dialog, "获取当前预设历史日期错误，请重新配置后重启！");
					hadNowDate = 2;
				}
			} else {
				AppStatus.getInstance().setNowDate();
				hadNowDate = 1;
			}
		} catch (Exception e) {
			// if (hadSysDate != 2) {
			// hadSysDate = 2;
			// ShowMsg.showWarn(dialog, "获取当前预设历史日期错误，请重新配置后重启！");
			// }
		}

	}
}
