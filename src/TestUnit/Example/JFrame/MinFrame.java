package TestUnit.Example.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

public class MinFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpringLayout springLayout;

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String args[]) {
		try {
			MinFrame frame = new MinFrame();
			frame.setVisible(true);
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			frame.setExtendedState(frame.MAXIMIZED_BOTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("static-access")
	public final void min() {
		this.setState(this.ICONIFIED);
	}

	/**
	 * Create the frame
	 */
	public MinFrame() {
		super();
		springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		setBounds(100, 100, 500, 375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JButton button = new JButton();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				min();
			}
		});
		button.setText("最小化窗口");
		getContentPane().add(button);
		springLayout.putConstraint(SpringLayout.NORTH, button, 130,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, button, 155,
				SpringLayout.WEST, getContentPane());
		//
	}

}
