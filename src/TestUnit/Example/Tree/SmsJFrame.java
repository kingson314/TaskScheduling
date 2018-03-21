package TestUnit.Example.Tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * 
 * 
 * 
 * @author oyrh
 * 
 */

public class SmsJFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates new form SmsJFrame */

	public SmsJFrame() {

		menuTree();

	}

	/**
	 * 
	 * ���ͽṹ
	 * 
	 */

	public void menuTree() {

		// ����һ����������scrollable ��ͼ

		javax.swing.JScrollPane jsp = new javax.swing.JScrollPane();

		// ����һ����ҳ���ڵ㼰�ӽڵ�

		DefaultMutableTreeNode indexMenu = new DefaultMutableTreeNode("��ҳ");

		DefaultMutableTreeNode index1Menu = new DefaultMutableTreeNode("�������Ĺ���");

		// ���ӽڵ���ӵ���ҳ���ڵ�


		indexMenu.add(index1Menu);

		// �Ը��ڵ�Ϊ��������һ��������

//		javax.swing.JTree indexTree = new javax.swing.JTree(indexMenu);

		// ����ҳ���ڵ���ӵ�scrollable ��ͼ

//		jsp.setViewportView(indexTree);

		// ������Ϣ�շ����ڵ㼰�ӽڵ�

		DefaultMutableTreeNode smsMenu = new DefaultMutableTreeNode("��Ϣ�շ�");

		DefaultMutableTreeNode sms1Menu = new DefaultMutableTreeNode("�ı�����");

		// ���ӽڵ���ӵ���Ϣ�շ����ڵ�

		smsMenu.add(sms1Menu);

		// �Ը��ڵ�Ϊ��������һ��������

//		javax.swing.JTree smsTree = new javax.swing.JTree(smsMenu);

		// ����Ϣ�շ����ڵ���ӵ�scrollable ��ͼ

//		jsp.setViewportView(smsTree);

		// ����ͨѶ������ڵ㼰�ӽڵ�

		DefaultMutableTreeNode addressMenu = new DefaultMutableTreeNode("ͨѶ¼����");

		DefaultMutableTreeNode address1Menu = new DefaultMutableTreeNode(
				"�༭ͨ��¼");

		// ���ӽڵ���ӵ�ͨѶ������ڵ�

		addressMenu.add(address1Menu);

		// �Ը��ڵ�Ϊ��������һ��������

//		javax.swing.JTree addressTree = new javax.swing.JTree(addressMenu);

		// ��ͨѶ������ڵ���ӵ�scrollable ��ͼ

//		jsp.setViewportView(addressTree);

		// �����������ڵ㼰�ӽڵ�

		DefaultMutableTreeNode helpMenu = new DefaultMutableTreeNode("����");

		DefaultMutableTreeNode help1Menu = new DefaultMutableTreeNode("�����ĵ�");

		// ���ӽڵ���ӵ��������ڵ�

		helpMenu.add(help1Menu);

		// �Ը��ڵ�Ϊ��������һ��������

//		javax.swing.JTree helpTree = new javax.swing.JTree(helpMenu);

		// ���������ڵ���ӵ�scrollable ��ͼ

//		jsp.setViewportView(helpTree);

		// ����һ�������ڵ�

		DefaultMutableTreeNode menuTreeNode = new DefaultMutableTreeNode("����");

		// ���������ڵ���ӵ���ӵ������ڵ���

		menuTreeNode.add(indexMenu);

		menuTreeNode.add(addressMenu);

		menuTreeNode.add(smsMenu);

		menuTreeNode.add(helpMenu);

		// �Ը��ڵ�Ϊ��������һ��������

		javax.swing.JTree menuTree = new javax.swing.JTree(menuTreeNode);

		// ����������ӵ�scrollable ��ͼ

		jsp.setViewportView(menuTree);

		// �����ѡ���¼�
//		menuTree.addTreeSelectionListener(new TreeEventProcess(this));
		menuTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// �������ֹ�����������һ������

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());

		getContentPane().setLayout(layout);// ���ô������Ĳ��ֹ�����

		// ������ˮƽ��ȷ�����λ�úʹ�С����

		layout.setHorizontalGroup(

		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

		.addGroup(
				layout.createSequentialGroup()

				.addComponent(jsp, javax.swing.GroupLayout.PREFERRED_SIZE, 96,
						javax.swing.GroupLayout.PREFERRED_SIZE)

				.addContainerGap(304, Short.MAX_VALUE))

		);

		// �����ش�ֱ��ȷ�����λ�úʹ�С����

		layout.setVerticalGroup(

		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

		.addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup()

				.addContainerGap(15, Short.MAX_VALUE)

				.addComponent(jsp, javax.swing.GroupLayout.PREFERRED_SIZE, 275,
						javax.swing.GroupLayout.PREFERRED_SIZE)

				.addContainerGap())

		);

		pack();

	}

	/**
	 * 
	 * @param args
	 *            the command line arguments
	 * 
	 */

	public static void main(String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {

				new SmsJFrame().setVisible(true);

			}

		});

	}

}
