package TestUnit.web;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

class Test1 extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L;
	JButton button;
	URL url;
	JTextField text;
	JEditorPane area;
	byte b[] = new byte[118];
	Thread thread;

	public Test1() {
		text = new JTextField(30);
		area = new JEditorPane();
		area.setEditable(false);
		button = new JButton("确定");
		button.addActionListener(this);
		thread = new Thread(this);
		JPanel p = new JPanel();
		p.add(new JLabel("输入网址："));
		p.add(text);
		p.add(button);
		Container con = this.getContentPane();
		con.add(new JScrollPane(area), BorderLayout.CENTER);
		con.add(p, BorderLayout.NORTH);
		setBounds(60, 60, 360, 360);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		area.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						area.setPage(e.getURL());
					} catch (Exception w) {
					}
				}
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		if (!thread.isAlive()) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void run() {
		try {
			url = new URL(text.getText().trim());
			area.setPage(url);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new Test1();
	}
}