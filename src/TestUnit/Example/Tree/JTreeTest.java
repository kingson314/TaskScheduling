package TestUnit.Example.Tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import consts.ImageContext;



public class JTreeTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JTreeTest() {
		init();
	}

	private void init() {
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(createJTree(), BorderLayout.CENTER);
		this.setVisible(true);
	}

	/**
	 * ����һ����
	 * 
	 * @return
	 */
	private JScrollPane createJTree() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject(new MyTreeNode(new ImageIcon(ImageContext.Add),
				"root"));
		JTree jt = new JTree(node);
		jt.setCellRenderer(new MyTreeCellRenderer());
		createRootMutableTreeNode(node);
		JScrollPane jsp = new JScrollPane(jt);
		return jsp;

	}

	private void createRootMutableTreeNode(DefaultMutableTreeNode root) {
		root.add(new DefaultMutableTreeNode(new MyTreeNode(new ImageIcon(
				ImageContext.Del), "node1")));
		root.add(new DefaultMutableTreeNode(new MyTreeNode(new ImageIcon(
				ImageContext.Mod), "node2")));
		root.add(new DefaultMutableTreeNode(new MyTreeNode(new ImageIcon(
				ImageContext.Add), "node3")));
		root.add(new DefaultMutableTreeNode(new MyTreeNode(new ImageIcon(
				ImageContext.Add), "node4")));
		DefaultMutableTreeNode node1=new DefaultMutableTreeNode(new MyTreeNode(new ImageIcon(
				ImageContext.Add), "node5"));
		DefaultMutableTreeNode node2=new DefaultMutableTreeNode(new MyTreeNode(new ImageIcon(
				ImageContext.Add), "node5"));
		node1.add(node2);
		root.add(node1);
	}
	public static void main(String[] args) {
	 new JTreeTest();
	}

	private class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -3147870557553148166L;

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			MyTreeNode jtreeNode = (MyTreeNode) node.getUserObject();

			this.setIcon(jtreeNode.getIcon());
			this.setText(jtreeNode.getName());
			this.setOpaque(true);
			if (selected) {
				this.setBackground(new Color(178, 180, 191));
			} else {
				this.setBackground(new Color(255, 255, 255));
			}
			return this;
		}
	}

	private class MyTreeNode extends DefaultMutableTreeNode {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Icon icon;
		private String name;

		public MyTreeNode(Icon icon, String name) {
			this.icon = icon;
			this.name = name;
		}

		@SuppressWarnings("unused")
		public MyTreeNode() {
			super();
		}

		/**
		 * @return the icon
		 */
		public Icon getIcon() {
			return icon;
		}

		/**
		 * @param icon
		 *            the icon to set
		 */
		@SuppressWarnings("unused")
		public void setIcon(Icon icon) {
			this.icon = icon;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		@SuppressWarnings("unused")
		public void setName(String name) {
			this.name = name;
		}
	}

}
