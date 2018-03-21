//package TestUnit;
//
//import java.awt.BorderLayout;
//import java.awt.TextArea;
//import java.awt.Toolkit;
//import java.awt.event.KeyEvent;
//import java.awt.event.WindowAdapter;
//
//import javax.swing.JFrame;
//import javax.swing.WindowConstants;
//
//import com.app.AppFun;
//
//import common.component.ShortcutManager;
//
///**
// * @info 主程序
// * 
// * @author fgq 20120831
// * 
// */
//public class AppMain extends JFrame {
//	private static final long serialVersionUID = 20111030;
//	private static AppMain appMain;
//
//	public static void main(String[] args) {
//		appMain = new AppMain();
//	}
//
//	// 构造函数，执行参数初始化、界面初始化
//	public AppMain() {
//		ShortcutManager.getInstance().addShortcutListener(
//				new ShortcutManager.ShortcutListener() {
//					public void handle() {
//						if (appMain.isVisible()) {
//							appMain.setVisible(false);
//						} else {
//							appMain.setVisible(true);
//						}
//					}
//				}, KeyEvent.VK_F12);
//		this.setSize(900, 700);
//		this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
//		int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this
//				.getWidth()) / 2;
//		int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this
//				.getHeight()) / 2;
//		this.setLocation(w, h);
//		this.setLayout(new BorderLayout());
//		this.setSize(1200, 800);
//		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//		TextArea txtarea=new TextArea();
//		this.add(txtarea);
//		this.setVisible(true);
//		
//		// 添加窗体退出事件
//		this.addWindowListener(new WindowAdapter() {
//			public void windowClosing(java.awt.event.WindowEvent evt) {
//				appMain.dispose();
//				System.exit(0);
//			}
//		});
//	}
//
//}
