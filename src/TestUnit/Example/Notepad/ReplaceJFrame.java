package TestUnit.Example.Notepad;

//�滻��� ReplaceJFrame

//��FindJFrame�Ļ�����ʵ���滻����
import java.awt.*;
import java.awt.event.*; // Action

import javax.swing.*;
import javax.swing.event.*; // Caret

public class ReplaceJFrame extends JFrame implements ActionListener,
		CaretListener {

	private static final long serialVersionUID = 1L;
	private JTextPane text;
	private TextField text_find, text_replace;
	private JButton btn_findnext, btn_replace, btn_replaceall;
	FindJFrame findjframe;

	public ReplaceJFrame() {
		super("�滻");
		this.setBounds(200, 200, 400, 200);
		// this.setLayout(new GridLayout(5,1));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());

		Container container = this.getContentPane();

		JPanel panel1 = new JPanel(); // PanelĬ���������FlowLayout
		container.add(panel1);
		panel1.add(new JLabel("�������ݣ�N��: "));
		text_find = new TextField("", 20);
		panel1.add(text_find);

		JPanel panel2 = new JPanel();
		container.add(panel2);
		btn_findnext = new JButton("������һ��");
		btn_findnext.addActionListener(this);
		panel2.add(btn_findnext);

		JPanel panel3 = new JPanel();
		container.add(panel3);
		panel3.add(new JLabel("�滻Ϊ��P):    "));
		text_replace = new TextField("", 20);
		panel3.add(text_replace);

		JPanel panel4 = new JPanel();
		container.add(panel4);
		btn_replace = new JButton("�滻");
		btn_replace.addActionListener(this);
		panel4.add(btn_replace);

		JPanel panel5 = new JPanel();
		container.add(panel5);
		btn_replaceall = new JButton("ȫ���滻��A��");
		btn_replaceall.addActionListener(this);
		panel5.add(btn_replaceall);

		this.setResizable(false);
		this.setVisible(true);
	}

	public ReplaceJFrame(JTextPane text) {
		this();
		this.text = text;
		findjframe = new FindJFrame(this.text);
		findjframe.setVisible(false);
		findjframe.setStart(0);
		findjframe.setEnd(text.getText().length());

	}

	public void caretUpdate(CaretEvent e) {
		findjframe.setfindstr(text_find.getText());
	}

	public void actionPerformed(ActionEvent e) {
		{

			findjframe.setfindstr(text_find.getText());
			if (e.getSource() == btn_findnext) {

				findjframe.find();

				if (!findjframe.isFind())
					JOptionPane.showMessageDialog(this, "û���ҵ�"
							+ text_find.getText());
			} else if (e.getSource() == btn_replace) {
				if (text_replace.getText() == "") {
					JOptionPane.showMessageDialog(this, "������Ҫ�滻������");
					return;
				}
				if (findjframe.isFind()) // ����find��,����ҵ����ַ���ѡ��
				{
					String tempstr = text.getText().substring(
							text.getSelectionStart(), text.getSelectionEnd());

					if (tempstr.equals(text_find.getText())) // �ҵ���
																// ��text_replace�滻
						text.replaceSelection(text_replace.getText());
				}
			}

			else if (e.getSource() == btn_replaceall) // ���ı���ͷ,�滻����
			{
				if (text_replace.getText() == "") {
					JOptionPane.showMessageDialog(this, "������Ҫ�滻������");
					return;
				}
				int len = text.getText().length();
				findjframe.setStart(text.getSelectionStart());
				findjframe.setEnd(len);
				findjframe.find();
				while (findjframe.isFind()) {
					String tempstr = text.getText().substring(
							text.getSelectionStart(), text.getSelectionEnd());
					if (tempstr.equals(text_find.getText())) {
						text.replaceSelection(text_replace.getText());
					}
					findjframe.find();
				}
			}
		}

	}
}