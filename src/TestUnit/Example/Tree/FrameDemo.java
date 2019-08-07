package TestUnit.Example.Tree;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;


public class FrameDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;

	private JMenu SMenu = null;

	private JMenuItem SMenuItem = null;

	private JToolBar jJToolBarBar = null;

	private JButton SButton = null;

	private JSplitPane JSplitPane = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JLabel SLabel = null;

	@SuppressWarnings("rawtypes")
	private JList jList = null;

	private JPanel jPanel3 = null;

	private JLabel SLabel1 = null;

	private JTree jTree = null;

	private JScrollPane JScrollPane = null;

	private JPanel jPanel = null;

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	@SuppressWarnings("unused")
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getSMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes SMenu
	 * 
	 * @return javax.swing.SMenu
	 */
	private JMenu getSMenu() {
		if (SMenu == null) {
			SMenu = new JMenu();
			SMenu.setText("file ");
			SMenu.add(getSMenuItem());
		}
		return SMenu;
	}

	/**
	 * This method initializes SMenuItem
	 * 
	 * @return javax.swing.SMenuItem
	 */
	private JMenuItem getSMenuItem() {
		if (SMenuItem == null) {
			SMenuItem = new JMenuItem();
			SMenuItem.setText("test ");
		}
		return SMenuItem;
	}

	/**
	 * This method initializes jJToolBarBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getJJToolBarBar() {
		if (jJToolBarBar == null) {
			jJToolBarBar = new JToolBar();
			jJToolBarBar.add(getSButton());
		}
		return jJToolBarBar;
	}

	/**
	 * This method initializes SButton
	 * 
	 * @return javax.swing.SButton
	 */
	private JButton getSButton() {
		if (SButton == null) {
			SButton = new JButton();
			SButton.setText("test ");
		}
		return SButton;
	}

	/**
	 * This method initializes JSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (JSplitPane == null) {
			JSplitPane = new JSplitPane();
			JSplitPane.setLeftComponent(getJPanel1());
			JSplitPane.setRightComponent(getJScrollPane());
		}
		return JSplitPane;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getJPanel2(), BorderLayout.NORTH);
			jPanel1.add(getJPanel3(), BorderLayout.CENTER);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			SLabel = new JLabel();
			SLabel.setText("SLabel ");
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.add(SLabel, BorderLayout.NORTH);
			jPanel2.add(getJList(), BorderLayout.CENTER);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JList getJList() {
		if (jList == null) {
			DefaultListModel model = new DefaultListModel();
			model.addElement("ListItemOne ");
			model.addElement("ListItemTwo ");
			jList = new JList(model);

		}
		return jList;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			SLabel1 = new JLabel();
			SLabel1.setText("SLabel ");
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BorderLayout());
			jPanel3.add(SLabel1, BorderLayout.NORTH);
			jPanel3.add(getJTree(), BorderLayout.CENTER);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jTree
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			jTree = new JTree();
		}
		return jTree;
	}

	/**
	 * This method initializes JScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (JScrollPane == null) {
			JScrollPane = new JScrollPane();
			JScrollPane.setViewportView(getJPanel());
		}
		return JScrollPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
		}
		return jPanel;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FrameDemo thisClass = new FrameDemo();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public FrameDemo() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(650, 369);
		this.setJMenuBar(getJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame ");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJJToolBarBar(), BorderLayout.NORTH);
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

}