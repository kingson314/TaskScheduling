package com.app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import app.AppConfig;
import app.AppLookAndFeel;
import app.AppServer;
import app.AppStatus;

import com.log.Log;
import common.component.SMenu;
import common.component.SMenuItem;
import common.util.swing.UtilComponent;
import common.util.xml.UtilXml;

import consts.Const;
import consts.ImageContext;

/**
 * @info 程序菜单
 * 
 * @author fgq 20120831
 * 
 */
public class AppMenu {

	private JMenuBar mb;
	private SMenu mMsg;
	private SMenu mSys;
	private SMenu mHelp;
	private SMenu mTool;
	private SMenuItem miExit;
	private SMenuItem miAbout;
	private SMenuItem miSys;
	private SMenuItem miDbConn;
	private SMenuItem miBackupDb;
	private SMenuItem miEncryptAES;
	private SMenuItem miFtp;
	private SMenuItem miMail;
	private SMenuItem miSettings;
	private SMenuItem miMonitor;
	private SMenuItem miMonitorGroup;
	private SMenu miLook;
	private SMenuItem miLock;
	private SMenuItem miNowDate;
	private SMenuItem miStartJetty;
	private SMenuItem miStopJetty;
	private SMenuItem miTest;
	private static AppMenu menu;

	public static AppMenu getInstance() {
		if (menu == null)
			menu = new AppMenu();
		return menu;
	}

	// 系统菜单
	private void initSys() {
		mSys = new SMenu("系统(S)", KeyEvent.VK_S);
		mb.add(mSys);
		{
			miNowDate = new SMenuItem("更新当前执行日期(U)", KeyEvent.VK_U, ImageContext.NowDate);
			mSys.add(miNowDate);
			miNowDate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().updateNowDate();
				}
			});
		}
		{
			mSys.addSeparator();
		}
		{
			miStartJetty = new SMenuItem("启动Jetty服务(R)", KeyEvent.VK_R, ImageContext.StartJetty);
			mSys.add(miStartJetty);
			miStartJetty.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppServer.getInstance().startJetty();
				}
			});
		}
		{
			miStopJetty = new SMenuItem("停止Jetty服务(S)", KeyEvent.VK_S, ImageContext.StopJetty);
			mSys.add(miStopJetty);
			miStopJetty.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppServer.getInstance().stopJetty();
				}
			});
		}
		{
			mSys.addSeparator();
		}
		{
			miLock = new SMenuItem("锁定程序(L)", KeyEvent.VK_L, ImageContext.SystemLock);
			mSys.add(miLock);
			miLock.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().systemLock();
				}
			});
		}
		{
			mSys.addSeparator();
		}

		{
			miExit = new SMenuItem("退出程序(E)", KeyEvent.VK_E, ImageContext.SystemExit);
			mSys.add(miExit);
			miExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().systemClosing();
				}
			});
		}
	}

	// 信息菜单
	private void initMsg() {
		mMsg = new SMenu("信息维护(M)", KeyEvent.VK_M);
		mb.add(mMsg);
		{
			miSys = new SMenuItem("系统参数(S)", KeyEvent.VK_S, ImageContext.SystemParam);
			mMsg.add(miSys);
			miSys.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().sysDialog();
				}
			});
		}
		{
			miDbConn = new SMenuItem("数据源配置(D)", KeyEvent.VK_D, ImageContext.DbCon);
			mMsg.add(miDbConn);
			miDbConn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().dbConnDialog();
				}
			});
		}

		{
			miSettings = new SMenuItem("配置信息(C)", KeyEvent.VK_C, ImageContext.Settings);
			mMsg.add(miSettings);
			miSettings.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().settingsDialog();
				}
			});
		}
		{
			miFtp = new SMenuItem("FTP信息(F)", KeyEvent.VK_F, ImageContext.Ftp);
			mMsg.add(miFtp);
			miFtp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().ftpDialog();
				}
			});
		}
		{
			miMail = new SMenuItem("邮箱信息(M)", KeyEvent.VK_M, ImageContext.Mail);
			mMsg.add(miMail);
			miMail.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().mailDialog();
				}
			});
		}

		{
			miMonitor = new SMenuItem("监控员信息(N)", KeyEvent.VK_N, ImageContext.Monitor);
			mMsg.add(miMonitor);
			miMonitor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().monitorDialog();
				}
			});
		}

		{
			miMonitorGroup = new SMenuItem("监控组信息(G)", KeyEvent.VK_G, ImageContext.MonitorGroup);
			mMsg.add(miMonitorGroup);
			miMonitorGroup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().monitorGroupDialog();
				}
			});
		}

	}

	// 工具菜单
	private void initTool() {
		mTool = new SMenu("工具(T)", KeyEvent.VK_T);
		mb.add(mTool);
		{
			miBackupDb = new SMenuItem("备份数据(B)", KeyEvent.VK_B, ImageContext.Backup);
			mTool.add(miBackupDb);
			miBackupDb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().backupDbDialog();
				}
			});

		}

		{
			miEncryptAES = new SMenuItem("加密工具(S)", KeyEvent.VK_S, ImageContext.EncryptAES);
			mTool.add(miEncryptAES);
			miEncryptAES.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					AppFun.getInstance().encryptAESDialog();
				}
			});

		}
	}

	// 帮助菜单
	private void initHelp() {
		mHelp = new SMenu("帮助(H)", KeyEvent.VK_H);
		mb.add(mHelp);
		{
			{
				miLook = new SMenu("皮肤(L)", KeyEvent.VK_L, ImageContext.LookAndFeel);
				mHelp.add(miLook);
				final JRadioButtonMenuItem[] miLf = new JRadioButtonMenuItem[AppLookAndFeel.LookAndFeelCnName.length];
				for (int i = 0; i < AppLookAndFeel.LookAndFeelCnName.length - 1; i++) {
					miLf[i] = new JRadioButtonMenuItem(AppLookAndFeel.LookAndFeelCnName[i]);
					miLf[i].setFont(Const.tfont);
					if (AppLookAndFeel.LookAndFeelCnName[i].equals(AppConfig.getInstance().getMapAppConfig().get("appLookAndFeel"))) {
						miLf[i].setSelected(true);
					} else {
						miLf[i].setSelected(false);
					}
					final int index = i;
					miLf[index].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							UtilComponent.enableRadion(miLf, index, false);
							// AppLookAndFeel.getInstance().setLookAndFeel(
							// AppMain.appMain, miLf[i] .getText());
							AppLookAndFeel.getInstance().updateLookAndFeel(miLf[index].getText());
							UtilXml.updateXml(Const.XmlAppConfig, Const.XmlAppConfig, "appLookAndFeel", miLf[index].getText());
							AppStatus.getInstance().autoRefresh();
						}
					});
					miLook.add(miLf[i]);
				}
			}

			{
				miAbout = new SMenuItem("关于(A)", KeyEvent.VK_A, ImageContext.About);
				mHelp.add(miAbout);
				miAbout.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						AppFun.getInstance().aboutDialog();
					}
				});
			}
			{
				miTest = new SMenuItem("测试(A)", KeyEvent.VK_A, ImageContext.About);
				mHelp.add(miTest);
				miTest.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {}
				});
			}
		}
	}

	// 构造
	public AppMenu() {
		try {
			mb = new JMenuBar();
			mb.setOpaque(true);
			initSys();
			initMsg();
			initTool();
			initHelp();

		} catch (Exception e) {
			Log.logError("主程序创建菜单栏错误:", e);
		} finally {
		}

	}

	public JMenuBar getMb() {
		return mb;
	}
	// public SMenu getMMsg() {
	// return mMsg;
	// }
	//
	// public SMenu getMSys() {
	// return mSys;
	// }
	//
	// public SMenu getMHelp() {
	// return mHelp;
	// }
	//
	// public SMenu getMTool() {
	// return mTool;
	// }
	//
	// public SMenuItem getMiFtp() {
	// return miFtp;
	// }
	//
	// public SMenuItem getMiMail() {
	// return miMail;
	// }
	//
	// public SMenuItem getMiMonitor() {
	// return miMonitor;
	// }
	//
	// public SMenuItem getMiMonitorGroup() {
	// return miMonitorGroup;
	// }
	//
	// public SMenuItem getMiSettings() {
	// return miSettings;
	// }
	//
	// public SMenuItem getMiSys() {
	// return miSys;
	// }
	//
	// public SMenuItem getMiDbConn() {
	// return miDbConn;
	// }

}
