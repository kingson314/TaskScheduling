package TestUnit.Example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import common.component.DoManager;
import common.component.STextField;


public class UnDoTest extends JDialog implements ActionListener, MouseListener,
		CaretListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private STextField TextField1;
	private JButton btnreDo;
	private JButton btnunDo;
	private STextField TextField2;

	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		new UnDoTest().show();
	}

	public UnDoTest() {
		initGUI();
	}

	private void initGUI() {
		try {
			this.setLayout(null);
			{
				this.setSize(424, 223);
			}
			{
				TextField1 = new STextField();
				getContentPane().add(TextField1, BorderLayout.CENTER);
				TextField1.setText("TextField1");
				TextField1.setBounds(101, 60, 240, 30);
			}
			{
				TextField2 = new STextField();
				getContentPane().add(TextField2, BorderLayout.CENTER);
				TextField2.setText("TextField2");
				TextField2.setBounds(101, 100, 240, 70);
			}
			{
				btnunDo = new JButton();
				getContentPane().add(btnunDo);
				btnunDo.setText("unDo");
				btnunDo.setBounds(134, 20, 55, 21);
				btnunDo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						DoManager.getInstance().unDo();
					}
				});
			}
			{
				btnreDo = new JButton();
				getContentPane().add(btnreDo);
				btnreDo.setText("reDo");
				btnreDo.setBounds(225, 20, 37, 21);
				btnreDo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						DoManager.getInstance().reDo();
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {

	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void caretUpdate(CaretEvent e) {

	}
}
