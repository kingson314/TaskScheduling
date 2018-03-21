package TestUnit.Example.Tree;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeDemo3 {
	public TreeDemo3() {
		// ���ó�Alloy������ʽ
		// try {
		//
		// JFrame.setDefaultLookAndFeelDecorated(true);
		// // UIManager.setLookAndFeel(alloyLnF);
		// } catch (UnsupportedLookAndFeelException ex) {
		// // You may handle the exception here
		// }
		// this line needs to be implemented in order to make JWS work properly
		UIManager.getLookAndFeelDefaults().put("ClassLoader",
				getClass().getClassLoader());

		// JDialog.setDefaultLookAndFeelDecorated(true);
		JFrame f = new JFrame("firstTree");

		Container contentPane = f.getContentPane();
		// if (contentPane instanceof JComponent) {
		// ((JComponent) contentPane).setMinimumSize(new Dimension(100, 100));
		// }
		// Container contentPane = f.getContentPane();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("��Դ������");
		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("�ҵĹ��İ�");
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("�ҵĵ���");
		DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("�ղؼ�");
		DefaultMutableTreeNode node4 = new DefaultMutableTreeNode("Readme");

		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		treeModel.insertNodeInto(node1, root, root.getChildCount());
		treeModel.insertNodeInto(node2, root, root.getChildCount());
		treeModel.insertNodeInto(node3, root, root.getChildCount());
		treeModel.insertNodeInto(node4, root, root.getChildCount());

		DefaultMutableTreeNode leafnode = new DefaultMutableTreeNode("��˾�ļ�");
		treeModel.insertNodeInto(leafnode, node1, node1.getChildCount());
		leafnode = new DefaultMutableTreeNode("�����ż�");
		treeModel.insertNodeInto(leafnode, node1, node1.getChildCount());
		leafnode = new DefaultMutableTreeNode("˽���ļ�");
		treeModel.insertNodeInto(leafnode, node1, node1.getChildCount());

		leafnode = new DefaultMutableTreeNode("��������(C:)");
		treeModel.insertNodeInto(leafnode, node2, node2.getChildCount());
		leafnode = new DefaultMutableTreeNode("��������(D:)");
		treeModel.insertNodeInto(leafnode, node2, node2.getChildCount());
		leafnode = new DefaultMutableTreeNode("��������(E:)");
		treeModel.insertNodeInto(leafnode, node2, node2.getChildCount());

		DefaultMutableTreeNode node31 = new DefaultMutableTreeNode("��վ�б�");
		treeModel.insertNodeInto(node31, node3, node3.getChildCount());
		leafnode = new DefaultMutableTreeNode("��Ħվ");
		treeModel.insertNodeInto(leafnode, node3, node3.getChildCount());
		leafnode = new DefaultMutableTreeNode("ְ����Ϣ");
		treeModel.insertNodeInto(leafnode, node3, node3.getChildCount());
		leafnode = new DefaultMutableTreeNode("�������");
		treeModel.insertNodeInto(leafnode, node3, node3.getChildCount());

		JTree tree = new JTree(treeModel);
		/* �ı�JTree�����* */
		tree.putClientProperty("JTree.lineStyle", "Horizontal");
		/* �ı�JTree�����* */
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(tree);

		contentPane.add(scrollPane);
		f.pack();
		f.setVisible(true);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public static void main(String args[]) {

		new TreeDemo3();
	}
}
