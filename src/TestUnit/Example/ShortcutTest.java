package TestUnit.Example;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import common.component.ShortcutManager;


public class ShortcutTest {
	/**
	 * ����ʹ��
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();

		JButton button = new JButton("Button");
		JTextArea textArea = new JTextArea();
		JScrollPane scroller = new JScrollPane(textArea);
		frame.getContentPane().add(button, BorderLayout.NORTH);
		frame.getContentPane().add(scroller, BorderLayout.CENTER);

		// ////////////////////////////////////////////////////////////////////////////////////
		// ע���ݼ�
		ShortcutManager.getInstance().addShortcutListener(
				new ShortcutManager.ShortcutListener() {
					public void handle() {
						System.out.println("Meta + I");
					}
				}, KeyEvent.VK_META, KeyEvent.VK_I);

		ShortcutManager.getInstance().addShortcutListener(
				new ShortcutManager.ShortcutListener() {
					public void handle() {
						System.out.println("Ctrl + Meta + M");
					}
				}, KeyEvent.VK_CONTROL, KeyEvent.VK_META, KeyEvent.VK_M);

		// ���Ժ����ı���������Ŀ�ݼ��¼�, ����ť�õ�����ʱ, ��ݼ��¼�������
		ShortcutManager.getInstance().addIgnoredComponent(textArea);
		// /////////////////////////////////////////////////////////////////////////////////////

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
}
