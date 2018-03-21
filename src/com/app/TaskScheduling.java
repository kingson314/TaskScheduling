package com.app;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import common.component.ImagePanel;
import common.component.SButton;
import common.component.SLabel;
import common.component.SMenuItem;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.string.UtilString;
import consts.ImageContext;

import app.AppConfig;
import app.AppLookAndFeel;

/**
 * @info 程序登陆
 * 
 * @author fgq 20120831
 * 
 */
/**
 * @info锁定后: 关闭时显示托盘; 最小化/取消时，显示任务栏最小化
 * 
 * @author fgq 20120903
 */
public class TaskScheduling extends JFrame {
	private static final long serialVersionUID = -2518486013777499825L;
	private STextField txtUserName;
	private SLabel lPassword;
	private SButton btnCancel;
	private SButton btnLogin;
	private SLabel luserName;
	private JPasswordField txtPassword;
	private String bootType = "";
	private TrayIcon trayIcon;

	// 程序入口
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (AppConfig.getInstance().isUseAppLog()) {
					TaskScheduling appLogin = new TaskScheduling("登陆");
					appLogin.setVisible(true);
				} else {
					AppBoot.getInstance().execBootThread();
				}
			}
		});
	}

	public TaskScheduling(String booType) {
		this.bootType = booType;
		initGUI();

	}

	// 初始化界面
	private void initGUI() {
		try {
			String appLookAndFell = UtilString.isNil(AppConfig.getInstance().getMapAppConfig().get("appLookAndFeel"), "默认风格");
			if (!"默认风格".equals(appLookAndFell)) {
				AppLookAndFeel.getInstance().updateLookAndFeel(appLookAndFell);
			}
			this.setSize(480, 205);
			this.setTitle(AppConfig.getInstance().getMapAppConfig().get("appTitle"));
			// this.setUndecorated(true);//去掉标题栏
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.Login));
			this.setResizable(false);
			ImagePanel pnlLogin = new ImagePanel(this.getWidth(), this.getHeight(), ImageContext.LoginBackGround);
			getContentPane().add(pnlLogin);
			{
				luserName = new SLabel("\u7528\u6237\u540d");
				pnlLogin.add(luserName);
				luserName.setBounds(120, 48, 60, 14);
			}
			{
				txtUserName = new STextField();
				pnlLogin.add(txtUserName);
				txtUserName.setText(AppConfig.getInstance().getMapAppConfig().get("appUserName"));
				txtUserName.setBounds(189, 45, 149, 21);
				txtUserName.setFocusable(false);
				txtUserName.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent evt) {
						keyPressUserName(evt);
					}
				});
			}
			{
				lPassword = new SLabel("\u5bc6  \u7801");
				pnlLogin.add(lPassword);
				lPassword.setBounds(120, 83, 60, 14);
			}
			{
				txtPassword = new JPasswordField();
				pnlLogin.add(txtPassword);
				txtPassword.setBounds(189, 76, 149, 21);
				txtPassword.grabFocus();
				txtPassword.requestFocus();
				txtPassword.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent evt) {
						keyPressPassword(evt);
					}
				});
			}

			{
				btnLogin = new SButton("", ImageContext.Ok);
				pnlLogin.add(btnLogin);
				if ("登陆".equals(this.bootType)) {
					btnLogin.setText("\u767b \u9646");
				} else if ("锁屏".equals(this.bootType)) {
					btnLogin.setText("解  锁");
				}
				btnLogin.setBounds(120, 121, 110, 25);
				btnLogin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						login();
					}
				});
			}
			{
				btnCancel = new SButton("\u53d6   \u6d88", ImageContext.Exit);
				pnlLogin.add(btnCancel);
				btnCancel.setBounds(253, 121, 110, 25);
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cancel();
					}

				});
			}

			if ("登陆".equals(this.bootType)) {
				this.addWindowListener(new WindowAdapter() {// 添加窗体退出事件
							public void windowClosing(java.awt.event.WindowEvent evt) {
								cancel();
							}
						});
			} else if ("锁屏".equals(this.bootType)) {
				this.addWindowListener(new WindowAdapter() {// 添加窗体退出事件
							public void windowClosing(java.awt.event.WindowEvent evt) {
								if (SystemTray.isSupported()) {
									setTrayOnClose();
								} else {
									cancel();
								}
							}
						});
			}
			this.toFront();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 登陆事件
	private void login() {
		String userName = UtilString.isNil(txtUserName.getText());
		String password = UtilString.isNil(String.valueOf(txtPassword.getPassword()));
		if (userName.equalsIgnoreCase(AppConfig.getInstance().getMapAppConfig().get("appUserName"))) {
			if (password.equalsIgnoreCase(AppConfig.getInstance().getMapAppConfig().get("appPassword"))) {
				this.dispose();
				if ("登陆".equals(this.bootType)) {
					AppBoot.getInstance().execBootThread();
				}
				if ("锁屏".equals(this.bootType)) {
					AppMain.appMain.setVisible(true);
					AppMain.appMain.toFront();
				}
			} else {
				// System.out.println(mapLogin.get("appPassword"));
				ShowMsg.showWarn("密码错误!");
				txtPassword.setSelectionStart(0);
				txtPassword.setSelectionEnd(String.valueOf(txtPassword.getPassword()).length());
				setFocus(txtPassword);
			}
		} else {
			ShowMsg.showWarn("用户名错误!");
			txtUserName.setSelectionStart(0);
			txtUserName.setSelectionEnd(String.valueOf(txtPassword.getPassword()).length());
			setFocus(txtUserName);
		}
	}

	// 设置焦点
	private void setFocus(STextField txt) {
		txt.grabFocus();
		txt.requestFocus();
	}

	// 设置焦点
	private void setFocus(JPasswordField txt) {
		txt.grabFocus();
		txt.requestFocus();
	}

	// 取消事件
	private void cancel() {
		if ("登陆".equals(this.bootType))
			System.exit(0);
		else if ("锁屏".equals(this.bootType)) {
			minimum();
		}
	}

	// 最小化
	private void minimum() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setState(JFrame.ICONIFIED);// 最小化，任务栏显示
	}

	// 用户名entry事件
	private void keyPressUserName(KeyEvent evt) {
		// System.out.println(evt.getKeyCode());
		if (evt.getKeyCode() == 10) {
			setFocus(txtPassword);
		} else if (evt.getKeyCode() == 27) {
			cancel();
		}
	}

	// 密码entry事件
	private void keyPressPassword(KeyEvent evt) {
		if (evt.getKeyCode() == 10) {
			login();
		} else if (evt.getKeyCode() == 27) {
			cancel();
		}
	}

	// 还原
	private void restore() {
		removeTray(trayIcon);
		setVisible(true);
		setExtendedState(JFrame.NORMAL);
		toFront();
		setFocus(txtPassword);
	}

	// 设置托盘
	private void setTrayOnClose() { // 窗口最小化到任务栏托盘
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(false);// 最小化，任务栏不显示
		removeTray(trayIcon);

		final JPopupMenu pmTray = new JPopupMenu(); // 增加托盘右击菜单
		SMenuItem miUnLock = new SMenuItem("解锁", ImageContext.TrayUnLock);
		miUnLock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 按下还原键
				restore();
			}

		});
		pmTray.add(miUnLock);
		pmTray.setInvoker(pmTray);

		ImageIcon trayImg = new ImageIcon(ImageContext.SystemTray);// 托盘图标
		trayIcon = new TrayIcon(trayImg.getImage(), this.getTitle());
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { // 鼠标器双击事件
				pmTray.setVisible(false);
				if (e.getClickCount() == 2) {
					restore();
				}
				if (e.isMetaDown()) {
					// if (e.isPopupTrigger()) {
					pmTray.setLocation(e.getX(), e.getY());
					pmTray.setVisible(true);
					// }
				}

			}
		});
		addTray(trayIcon);
	}

	// 移除托盘
	public void removeTray(TrayIcon trayIcon) {
		SystemTray.getSystemTray().remove(trayIcon);
	}

	// 添加托盘
	public void addTray(TrayIcon trayIcon) {
		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}
}
