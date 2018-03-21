package TestUnit.commonTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

import common.component.SComboBox;


/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class Kalendar extends JDialog {
	private static final long serialVersionUID = 1L;
	private String useModule;
	int fnR = 212;
	int fnG = 208;
	int fnB = 200;
	int fontSize = 12;
	Date retDate = new Date();
	String strmaxDate = "";
	String strminDate = "";
	Date slin = new Date();
	int[] days_in_month = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	JPanel panelCenter = new JPanel();
	BorderLayout borderLayoutCenter = new BorderLayout();
	JPanel jPanelBody = new JPanel();
	JPanel jPanelNorth = new JPanel();

	public static void main(String[] args) {

		Kalendar dialog = new Kalendar(null);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setSize(420, 280);
		Dimension frameSize = dialog.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		dialog.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		dialog.setVisible(true);

	}

	void jJBAPanelJButton_focusGained(FocusEvent e, int numi) {
		int yy = 2000;
		int mm = 3;
		int dd = 1;
		Integer yyI = new Integer(jSpinner.getValue().toString());
		yy = yyI.intValue();
		Integer mmI = new Integer(SComboBoxMonth.getSelectedItem().toString());
		mm = mmI.intValue();
		Integer ddI = new Integer(JBAPanel[numi].GetJBAPLabel());
		dd = ddI.intValue();
		strminDate = yyI.toString() + "-" + mmI.toString() + "-"
				+ ddI.toString();
		strmaxDate = yyI.toString() + "-";

		if (mm < 10)
			strmaxDate = strmaxDate + "0" + mmI.toString() + "-";
		else
			strmaxDate = strmaxDate + mmI.toString() + "-";
		if (dd < 10)
			strmaxDate = strmaxDate + "0" + ddI.toString();
		else
			strmaxDate = strmaxDate + ddI.toString();

		retDate = SetCurDate(yy, mm, dd);
		System.out.println(strmaxDate);
		if (this.useModule.equals("调度模块_开始日期")) {

		}
		this.dispose();
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {// 主程序关闭事件
		this.dispose();
	}

	JBAPanel[] JBAPanel = { new JBAPanel(2, fnR, fnG, fnB),
			new JBAPanel(2, fnR, fnG, fnB), new JBAPanel(2, fnR, fnG, fnB),
			new JBAPanel(2, fnR, fnG, fnB), new JBAPanel(2, fnR, fnG, fnB),
			new JBAPanel(2, fnR, fnG, fnB), new JBAPanel(2, fnR, fnG, fnB),
			new JBAPanel(1, fnR, fnG, fnB),// 10--------7
			new JBAPanel(1, fnR, fnG, fnB),// 11--------8
			new JBAPanel(1, fnR, fnG, fnB),// 12--------9
			new JBAPanel(1, fnR, fnG, fnB),// 13--------10
			new JBAPanel(1, fnR, fnG, fnB),// 14--------11
			new JBAPanel(1, fnR, fnG, fnB),// 15--------12
			new JBAPanel(1, fnR, fnG, fnB),// 16--------13
			new JBAPanel(1, fnR, fnG, fnB),// 20--------14
			new JBAPanel(1, fnR, fnG, fnB),// 21--------15
			new JBAPanel(1, fnR, fnG, fnB),// 22--------16
			new JBAPanel(1, fnR, fnG, fnB),// 23--------17
			new JBAPanel(1, fnR, fnG, fnB),// 24--------18
			new JBAPanel(1, fnR, fnG, fnB),// 25--------19
			new JBAPanel(1, fnR, fnG, fnB),// 26--------20
			new JBAPanel(1, fnR, fnG, fnB),// 30--------21
			new JBAPanel(1, fnR, fnG, fnB),// 31--------22
			new JBAPanel(1, fnR, fnG, fnB),// 32--------23
			new JBAPanel(1, fnR, fnG, fnB),// 33--------24
			new JBAPanel(1, fnR, fnG, fnB),// 34--------25
			new JBAPanel(1, fnR, fnG, fnB),// 35--------26
			new JBAPanel(1, fnR, fnG, fnB),// 36--------27
			new JBAPanel(1, fnR, fnG, fnB),// 40--------28
			new JBAPanel(1, fnR, fnG, fnB),// 41--------29
			new JBAPanel(1, fnR, fnG, fnB),// 42--------30
			new JBAPanel(1, fnR, fnG, fnB),// 43--------31
			new JBAPanel(1, fnR, fnG, fnB),// 44--------32
			new JBAPanel(1, fnR, fnG, fnB),// 45--------33
			new JBAPanel(1, fnR, fnG, fnB),// 46--------34
			new JBAPanel(1, fnR, fnG, fnB),// 50--------35
			new JBAPanel(1, fnR, fnG, fnB),// 51--------36
			new JBAPanel(1, fnR, fnG, fnB),// 52--------37
			new JBAPanel(1, fnR, fnG, fnB),// 53--------38
			new JBAPanel(1, fnR, fnG, fnB),// 54--------39
			new JBAPanel(1, fnR, fnG, fnB),// 55--------40
			new JBAPanel(1, fnR, fnG, fnB),// 56--------41
			new JBAPanel(1, fnR, fnG, fnB),// 69--------42
			new JBAPanel(1, fnR, fnG, fnB),// 61--------43
			new JBAPanel(1, fnR, fnG, fnB),// 62--------44
			new JBAPanel(1, fnR, fnG, fnB),// 63--------45
			new JBAPanel(1, fnR, fnG, fnB),// 64--------46
			new JBAPanel(1, fnR, fnG, fnB),// 65--------47
			new JBAPanel(1, fnR, fnG, fnB),// 66--------48
	};

	public static SComboBox SComboBoxMonth;
	public static JSpinner jSpinner = new JSpinner();
	JLabel JLabelMonth = new JLabel();
	JLabel JLabelYear = new JLabel();
	TitledBorder titledBorder1;
	JPanel jPanelHead = new JPanel();
	JPanel jPanelBottom = new JPanel();
	BorderLayout borderLayoutBody = new BorderLayout();

	public Kalendar(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			pack();
			this.setSize(449, 110);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Kalendar(String usemodule) {
		this(null, "日期控件", false);
		this.useModule = usemodule;
		this.addWindowListener(new WindowAdapter() {// 添加窗体退出事件
					public void windowClosing(java.awt.event.WindowEvent evt) {
						formWindowClosing(evt);
					}
				});
	}

	private void jbInit() throws Exception {

		titledBorder1 = new TitledBorder("");
		panelCenter.setLayout(borderLayoutCenter);
		jPanelNorth.setBackground(Color.gray);
		jPanelNorth.setBorder(BorderFactory.createRaisedBevelBorder());

		JBAPanel[0].SetJBAPLabel(" 日");
		JBAPanel[1].SetJBAPLabel(" 一");
		JBAPanel[2].SetJBAPLabel(" 二");
		JBAPanel[3].SetJBAPLabel(" 三");
		JBAPanel[4].SetJBAPLabel(" 四");
		JBAPanel[5].SetJBAPLabel(" 五");
		JBAPanel[6].SetJBAPLabel(" 六");
		JLabelMonth.setFont(new java.awt.Font("Dialog", 0, 16));
		JLabelMonth.setForeground(Color.white);
		JLabelMonth.setText("\u6708\u4efd");
		JLabelYear.setFont(new java.awt.Font("Dialog", 0, 16));
		JLabelYear.setForeground(Color.white);
		JLabelYear.setText("\u5e74\u4efd");
		Calendar calendar = Calendar.getInstance();
		Integer[] items = new Integer[12];
		for (int i = 1; i <= 12; i++) {
			items[i - 1] = i;
		}
		SComboBoxMonth = new SComboBox(items);
		SComboBoxMonth.setSelectedIndex(calendar.get(Calendar.MONTH));
		SComboBoxMonth.setBackground(Color.white);
		SComboBoxMonth.setFont(new java.awt.Font("Dialog", 0, 16));
		SComboBoxMonth.addItemListener(new Clock_SComboBoxMonth_itemAdapter(
				this));
		jSpinner.setValue(new Integer(calendar.get(Calendar.YEAR)));
		leapYear(calendar.get(Calendar.YEAR));
		jSpinner.setEditor(new JSpinner.NumberEditor(jSpinner, "0000"));
		jSpinner.setFont(new java.awt.Font("Dialog", 0, 16));
		jSpinner.getEditor().setFont(new java.awt.Font("Dialog", 0, 16));
		jSpinner.addChangeListener(new Clock_jSpinner_changeAdapter(this));

		jPanelBottom.setBackground(new Color(fnR, fnG, fnB));
		jPanelBody.setLayout(borderLayoutBody);
		jPanelHead.setBackground(new Color(255, 164, 190));
		jPanelHead.setBorder(titledBorder1);
		this.addMouseListener(new Clock_this_mouseAdapter(this));

		this.setResizable(false);
		BorderLayout thisLayout = new BorderLayout();
		getContentPane().setLayout(thisLayout);
		jPanelBody.add(jPanelBottom, BorderLayout.CENTER);
		getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.add(jPanelBody, BorderLayout.CENTER);
		jPanelBody.add(jPanelHead, BorderLayout.NORTH);
		jPanelHead.setPreferredSize(new java.awt.Dimension(443, 32));

		for (int i = 0; i < JBAPanel.length; i++) {
			if (i < 7) {
				JBAPanel[i].setBorder(null);
				JBAPanel[i].setBackground(jPanelHead.getBackground());
				JBAPanel[i].setSize(20, 20);
				jPanelHead.add(JBAPanel[i], null);
			} else {
				JBAPanel[i].JButton
						.addFocusListener(new Clock_jJBAPanelJButton_focusAdapter(
								this, i));
				jPanelBottom.add(JBAPanel[i], null);
			}
		}

		getContentPane().add(jPanelNorth, BorderLayout.NORTH);
		jPanelNorth.setPreferredSize(new java.awt.Dimension(205, 30));
		jPanelNorth.setLayout(null);
		jPanelNorth.add(JLabelMonth);
		JLabelMonth.setBounds(217, 3, 32, 23);
		jPanelNorth.add(SComboBoxMonth);
		SComboBoxMonth.setBounds(254, 5, 50, 21);
		jPanelNorth.add(JLabelYear);
		JLabelYear.setBounds(108, 3, 32, 23);
		jPanelNorth.add(jSpinner);
		jSpinner.setBounds(145, 5, 60, 21);
		SetShowDate();
	}

	void SetShowDate() {
		int yy = 0;
		int mm = 0;
		int dd = 0;
		// int zz = 0;
		Integer yyI = new Integer(jSpinner.getValue().toString());
		yy = yyI.intValue();
		Integer mmI = new Integer(SComboBoxMonth.getSelectedItem().toString());
		mm = mmI.intValue();
		dd = 1;
		dd = getWeekday(yy, mm, dd);

		SetJBLabel(dd, days_in_month[SComboBoxMonth.getSelectedIndex()]);
	}

	void SetJBLabel(int min, int max) {
		int num = 1;
		// String jpname = "hjp";
		for (int i = 7; i < JBAPanel.length; i++) {
			if (i < (min + 6) || i >= (max + 6 + min)) {

				JBAPanel[i].SetJBAPLabel("");
				JBAPanel[i].setBorder(null);
				JBAPanel[i].JButton.setVisible(false);
			} else {
				JBAPanel[i].SetJBAPLabel(new Integer(num).toString());
				JBAPanel[i].setBorder(BorderFactory.createRaisedBevelBorder());
				JBAPanel[i].JButton.setVisible(true);
				num++;
			}
		}

	}

	void leapYear(int year) {
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			days_in_month[1] = 29;
		} else {
			days_in_month[1] = 28;
		}
	}

	int getWeekday(int yy, int mm) {

		slin = SetCurDate(yy, mm - 1, 1);
		int week = 0;
		week = Getweek(slin);
		return week;
	}

	
	@SuppressWarnings("deprecation")
	int Getweek(Date dat) {
		return dat.getDay();
	}

	
	@SuppressWarnings("deprecation")
	Date SetCurDate(int yy, int mm, int dd) {
		slin.setYear(yy);
		slin.setMonth(mm);
		slin.setDate(dd);

		return slin;
	}

	int getWeekday(int yy, int mm, int dd) {
		slin = SetCurDate(yy, mm - 1, dd);
		int week = 0;
		week = Getweek(slin);
		return week;

	}

	void jSpinner_stateChanged(ChangeEvent e) {
		Integer i = new Integer(jSpinner.getValue().toString());
		int ii = i.intValue();
		Calendar dd = Calendar.getInstance();
		if (ii < 0)
			jSpinner.setValue(new Integer(dd.get(Calendar.YEAR)));

		if (ii > 9999)
			jSpinner.setValue(new Integer(dd.get(Calendar.YEAR)));

		i = new Integer(jSpinner.getValue().toString());
		ii = i.intValue();
		leapYear(ii);
		SetShowDate();
	}

	void SComboBoxMonth_itemStateChanged(ItemEvent e) {
		SetShowDate();
	}

	void this_mouseClicked(MouseEvent e) {
		for (int i = 0; i < JBAPanel.length; i++)
			if (JBAPanel[i].GetDisflag()) {
				this.dispose();
			}

	}

	public Date GetRetDate() {
		return retDate;
	}

	public String GetStrminDate() {
		return strminDate;
	}

	public String GetStrmaxDate() {
		return strmaxDate;
	}

}

class Clock_jSpinner_changeAdapter implements javax.swing.event.ChangeListener {
	Kalendar adaptee;

	Clock_jSpinner_changeAdapter(Kalendar adaptee) {
		this.adaptee = adaptee;
	}

	public void stateChanged(ChangeEvent e) {
		adaptee.jSpinner_stateChanged(e);
	}
}

class Clock_SComboBoxMonth_itemAdapter implements java.awt.event.ItemListener {
	Kalendar adaptee;

	Clock_SComboBoxMonth_itemAdapter(Kalendar adaptee) {
		this.adaptee = adaptee;
	}

	public void itemStateChanged(ItemEvent e) {
		adaptee.SComboBoxMonth_itemStateChanged(e);
	}
}

class Clock_this_mouseAdapter extends java.awt.event.MouseAdapter {
	Kalendar adaptee;

	Clock_this_mouseAdapter(Kalendar adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.this_mouseClicked(e);
	}
}

class Clock_jJBAPanelJButton_focusAdapter extends java.awt.event.FocusAdapter {
	Kalendar adaptee;
	int num = 0;

	Clock_jJBAPanelJButton_focusAdapter(Kalendar adaptee, int numi) {
		this.adaptee = adaptee;
		this.num = numi;
	}

	public void focusGained(FocusEvent e) {
		adaptee.jJBAPanelJButton_focusGained(e, num);
	}
}

class JBAPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int fnR = 100;
	int fnG = 100;
	int fnB = 100;

	int flag = 0;
	boolean disflag = false;
	public JButton JButton = new JButton();
	BorderLayout borderLayout1 = new BorderLayout();
	JLabel JLabel = new JLabel();

	public JBAPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JBAPanel(int flag, int fnR, int fnG, int fnB) {
		this.flag = flag;
		this.fnR = fnR;
		this.fnG = fnG;
		this.fnB = fnB;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		if (flag == 1 || flag == 2) {
			this.setPreferredSize(new Dimension(52, 30));
			this.setBackground(new Color(fnR, fnG, fnB));
			this.setBorder(BorderFactory.createRaisedBevelBorder());
			this.setDebugGraphicsOptions(0);
			this.setLayout(borderLayout1);

			if (flag == 1) {
				JButton.setFont(new java.awt.Font("Dialog", 0, 12));
				JButton.setHorizontalAlignment(SwingConstants.CENTER);
				JButton.addActionListener(new JBAPanel_JButton_actionAdapter(
						this));
				JButton
						.addMouseListener(new JBAPanel_JButton_mouseAdapter(
								this));

				this.add(JButton, BorderLayout.CENTER);
			}
			if (flag == 2) {
				JLabel.setFont(new java.awt.Font("Dialog", 0, 12));
				this.add(JLabel, BorderLayout.NORTH);
			}
		}
		this.addMouseListener(new JBAPanel_this_mouseAdapter(this));
	}

	public void SetJBAPLabel(String str) {
		if (flag == 2) {
			JLabel.setText(str);
		} else
			JButton.setText(str);
	}

	public String GetJBAPLabel() {
		if (flag == 2) {
			return JLabel.getText();
		} else
			return JButton.getText();
	}

	public boolean GetDisflag() {
		return disflag;
	}

	void this_mouseEntered(MouseEvent e) {
		if (flag == 1) {
			if (this.getBorder() == null) {
				this.setBackground(new Color(fnR, fnG, fnB));
				JButton.setBackground(new Color(fnR, fnG, fnB));
			} else
				this.setBackground(new Color(104, 90, 190));
			JButton.setBackground(new Color(104, 90, 190));

		}

	}

	void this_mouseExited(MouseEvent e) {
		if (flag == 1) {
			this.setBackground(new Color(fnR, fnG, fnB));
			JButton.setBackground(new Color(fnR, fnG, fnB));
		}

	}

	void this_mouseClicked(MouseEvent e) {
		disflag = true;
	}

	void JButton_mouseClicked(MouseEvent e) {
		disflag = true;
	}

	void JButton_actionPerformed(ActionEvent e) {
		disflag = true;
	}

	void JButton_mouseEntered(MouseEvent e) {
		if (flag == 1) {
			if (this.getBorder() == null) {
				this.setBackground(new Color(fnR, fnG, fnB));
				JButton.setBackground(new Color(fnR, fnG, fnB));
			} else
				this.setBackground(new Color(104, 90, 190));
			JButton.setBackground(new Color(104, 90, 190));

		}

	}

	void JButton_mouseExited(MouseEvent e) {
		if (flag == 1) {
			this.setBackground(new Color(fnR, fnG, fnB));
			JButton.setBackground(new Color(fnR, fnG, fnB));
		}

	}

}

class JBAPanel_this_mouseAdapter extends java.awt.event.MouseAdapter {
	JBAPanel adaptee;

	JBAPanel_this_mouseAdapter(JBAPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseEntered(MouseEvent e) {
		adaptee.this_mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {
		adaptee.this_mouseExited(e);
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.this_mouseClicked(e);
	}
}

class JBAPanel_JButton_mouseAdapter extends java.awt.event.MouseAdapter {
	JBAPanel adaptee;

	JBAPanel_JButton_mouseAdapter(JBAPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.JButton_mouseClicked(e);
	}

	public void mouseEntered(MouseEvent e) {
		adaptee.JButton_mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {
		adaptee.JButton_mouseExited(e);
	}
}

class JBAPanel_JButton_actionAdapter implements java.awt.event.ActionListener {
	JBAPanel adaptee;

	JBAPanel_JButton_actionAdapter(JBAPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.JButton_actionPerformed(e);
	}
}
