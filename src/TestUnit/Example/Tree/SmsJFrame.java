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
	 * 树型结构
	 * 
	 */

	public void menuTree() {

		// 创建一个用于树的scrollable 视图

		javax.swing.JScrollPane jsp = new javax.swing.JScrollPane();

		// 创建一个首页根节点及子节点

		DefaultMutableTreeNode indexMenu = new DefaultMutableTreeNode("首页");

		DefaultMutableTreeNode index1Menu = new DefaultMutableTreeNode("短信中心管理");

		// 将子节点添加到首页根节点


		indexMenu.add(index1Menu);

		// 以根节点为参数创建一个树对象

//		javax.swing.JTree indexTree = new javax.swing.JTree(indexMenu);

		// 将首页根节点添加到scrollable 视图

//		jsp.setViewportView(indexTree);

		// 创建信息收发根节点及子节点

		DefaultMutableTreeNode smsMenu = new DefaultMutableTreeNode("信息收发");

		DefaultMutableTreeNode sms1Menu = new DefaultMutableTreeNode("文本发送");

		// 将子节点添加到信息收发根节点

		smsMenu.add(sms1Menu);

		// 以根节点为参数创建一个树对象

//		javax.swing.JTree smsTree = new javax.swing.JTree(smsMenu);

		// 将信息收发根节点添加到scrollable 视图

//		jsp.setViewportView(smsTree);

		// 创建通讯管理根节点及子节点

		DefaultMutableTreeNode addressMenu = new DefaultMutableTreeNode("通讯录管理");

		DefaultMutableTreeNode address1Menu = new DefaultMutableTreeNode(
				"编辑通记录");

		// 将子节点添加到通讯管理根节点

		addressMenu.add(address1Menu);

		// 以根节点为参数创建一个树对象

//		javax.swing.JTree addressTree = new javax.swing.JTree(addressMenu);

		// 将通讯管理根节点添加到scrollable 视图

//		jsp.setViewportView(addressTree);

		// 创建帮助根节点及子节点

		DefaultMutableTreeNode helpMenu = new DefaultMutableTreeNode("帮助");

		DefaultMutableTreeNode help1Menu = new DefaultMutableTreeNode("帮助文档");

		// 将子节点添加到帮助根节点

		helpMenu.add(help1Menu);

		// 以根节点为参数创建一个树对象

//		javax.swing.JTree helpTree = new javax.swing.JTree(helpMenu);

		// 将帮助根节点添加到scrollable 视图

//		jsp.setViewportView(helpTree);

		// 创建一个操作节点

		DefaultMutableTreeNode menuTreeNode = new DefaultMutableTreeNode("操作");

		// 将其它根节点添加到添加到操作节点下

		menuTreeNode.add(indexMenu);

		menuTreeNode.add(addressMenu);

		menuTreeNode.add(smsMenu);

		menuTreeNode.add(helpMenu);

		// 以根节点为参数创建一个树对象

		javax.swing.JTree menuTree = new javax.swing.JTree(menuTreeNode);

		// 将操作树添加到scrollable 视图

		jsp.setViewportView(menuTree);

		// 添加树选择事件
//		menuTree.addTreeSelectionListener(new TreeEventProcess(this));
		menuTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// 创建布局管理器并传入一个容器

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());

		getContentPane().setLayout(layout);// 设置此容器的布局管理器

		// 设置沿水平轴确定组件位置和大小的组

		layout.setHorizontalGroup(

		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

		.addGroup(
				layout.createSequentialGroup()

				.addComponent(jsp, javax.swing.GroupLayout.PREFERRED_SIZE, 96,
						javax.swing.GroupLayout.PREFERRED_SIZE)

				.addContainerGap(304, Short.MAX_VALUE))

		);

		// 设置沿垂直轴确定组件位置和大小的组

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
