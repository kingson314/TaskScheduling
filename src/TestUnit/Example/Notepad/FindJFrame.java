package TestUnit.Example.Notepad;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class FindJFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -7238472661557455110L;
	private JTextPane text;
	private JTextField findstr;
	private JButton btn_findnext;
	@SuppressWarnings("unused")
	private JButton btn_cannel;
	private int start = 0, end = 0;
	private final int FIND = 1; // �ҵ���־
	private int flag = 0; // ��־�Ƿ��ҵ�

	public FindJFrame() {
		super("����");
		this.setBounds(200, 300, 200, 200);
		this.setLayout(new GridLayout(2, 1));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		Panel panel1 = new Panel();
		panel1.setLayout(new FlowLayout());
		this.getContentPane().add(panel1);

		panel1.add(new Label("��������"));
		findstr = new JTextField(15);
		panel1.add(findstr);
		btn_findnext = new JButton("������һ��");
		btn_findnext.addActionListener(this);
		panel1.add(btn_findnext);

		Panel panel2 = new Panel();
		this.getContentPane().add(panel2);
		// panel2.add(new SLabel("����"));
		// JButton
		this.setResizable(false);
		this.setVisible(true);// //////////////////////////

	}

	public FindJFrame(JTextPane text) {
		this();
		this.text = text;
		end = text.getText().length();
	}

	public void setfindstr(String str) // ����Ҫ���ҵ��ַ���
	{
		this.findstr.setText(str);
	}

	public int getStart() // ���ѡ���ı�����ʼλ��
	{
		return start;
	}

	public int getEnd() // ѡ���ı�����ֹλ��
	{
		return end;
	}

	public void setTitle(String name) // �����ı�����
	{
		super.setTitle(name);
	}

	public void setStart(int start) // ���������ʼλ��
	{
		if (start >= 0)
			this.start = start;
		else
			start = 0;
	}

	public void setEnd(int end) // ���������ֹλ��
	{
		if (end >= 0)
			this.end = end;
		else
			end = text.getText().length();
	}

	public boolean isFind() // �ж��Ƿ��ҵ�
	{
		if (flag == FIND)
			return true;
		else
			return false;
	}

	public void find() {
		flag = 0;
		if (start < 0) {
			JOptionPane.showMessageDialog(this, "λ�ó�����Χ");
			return;
		}

		String str = text.getText().substring(start, end);
		int findstart = str.indexOf(findstr.getText()); // û���ҵ�findstr�Ļ�����-1
		if (findstart != -1)// �ҵ�
			flag = FIND;
		else {
			text.setSelectionStart(end);
			text.setSelectionEnd(end);
			return;
		}
		System.out.println("findstart= " + findstart);
		text.setSelectionStart(start + findstart);
		int findend = start + findstart + findstr.getText().length();
		text.setSelectionEnd(findend);
		start = findend;
	}

	public void actionPerformed(ActionEvent e) // ���"������һ��"
	{
		if (e.getSource() == btn_findnext) {
			find();
			if (!isFind())
				JOptionPane.showMessageDialog(this, "û���ҵ�");
		}
	}

}
