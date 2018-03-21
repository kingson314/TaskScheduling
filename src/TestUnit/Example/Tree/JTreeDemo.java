package TestUnit.Example.Tree;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/*

 JTree�Ĺ��캯��:

 JTree()

 JTree(Hashtable value)

 JTree(Object[] value)//ֻ��������캯����Դ����������

 JTree(TreeModel newModel)

 JTree(TreeNode root)

 JTree(TreeNode root, boolean asksAllowsChildren)

 JTree(Vector value)

 */

public class JTreeDemo

{
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args)

	{

		// ���캯��:JTree()

		JTree example1 = new JTree();

		// ���캯��:JTree(Object[] value)

		Object[] letters = { " a ", " b ", " c ", " d ", " e " };

		JTree example2 = new JTree(letters);

		// ���캯��:JTree(TreeNode root)(TreeNode��)

		// �ÿս�㴴����

		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(); // ���������

		JTree example3 = new JTree(node1); // �ô��������������� JTree�Ĺ��캯�����һ���������

		// ���캯��:JTree(TreeNode root)(ͬ��,ֻ��TreeNode�ǿ�)

		// ��һ�����㴴����

		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(" Color ");

		JTree example4 = new JTree(node2); // ��㲻������ɫ,Ĭ��Ϊ�������

		example4.setBackground(Color.lightGray);

		// ���캯��:JTree(TreeNode root, boolean
		// asksAllowsChildren)(ͬ��,ֻ��TreeNode���в�ͬ)

		// ʹ��DefaultMutableTreeNode������һ�����㴴����������Ϊ����Ӻ��ӽ��,����Ӻ��ӽ��

		DefaultMutableTreeNode color = new DefaultMutableTreeNode(" Color ",
				true);

		DefaultMutableTreeNode gray = new DefaultMutableTreeNode(" Gray ");

		color.add(gray);

		color.add(new DefaultMutableTreeNode(" Red "));

		gray.add(new DefaultMutableTreeNode(" Lightgray "));

		gray.add(new DefaultMutableTreeNode(" Darkgray "));

		color.add(new DefaultMutableTreeNode(" Green "));

		JTree example5 = new JTree(color);

		// ���캯��:JTree(TreeNode root)(ͬ��,ֻ��TreeNode�ǿ�)

		// ͨ�������ӽ�㴴����

		DefaultMutableTreeNode biology = new DefaultMutableTreeNode(" Biology ");

		DefaultMutableTreeNode animal = new DefaultMutableTreeNode(" Animal ");

		DefaultMutableTreeNode mammal = new DefaultMutableTreeNode(" Mammal ");

		DefaultMutableTreeNode horse = new DefaultMutableTreeNode(" Horse ");

		mammal.add(horse);

		animal.add(mammal);

		biology.add(animal);

		JTree example6 = new JTree(biology);

		horse.isLeaf();

		horse.isRoot();

		// ���캯��:JTree(TreeModel newModel)

		// ��DefaultMutableTreeNodel�ඨ��һ�����������������������һ����DefaultTreeMode

		// ����һ������ģ��,����JTree�Ĺ��캯��һ����

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(" Root1 ");

		DefaultMutableTreeNode child1 = new DefaultMutableTreeNode(" Child1 ");

		DefaultMutableTreeNode child11 = new DefaultMutableTreeNode(" Child11 ");

		DefaultMutableTreeNode child111 = new DefaultMutableTreeNode(
				" Child111 ");

		root.add(child1);
		child1.add(child11);
		child11.add(child111);

		DefaultTreeModel model = new DefaultTreeModel(root);

		JTree example7 = new JTree(model);

		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.setPreferredSize(new Dimension(700, 400));

		panel.add(new JScrollPane(example1)); // JTree�������JScrollPane��

		panel.add(new JScrollPane(example2));

		panel.add(new JScrollPane(example3));

		panel.add(new JScrollPane(example4));

		panel.add(new JScrollPane(example5));

		panel.add(new JScrollPane(example6));

		panel.add(new JScrollPane(example7));

		JFrame frame = new JFrame(" JTreeDemo ");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setContentPane(panel);

		frame.pack(); 
		frame.show();

	}

}