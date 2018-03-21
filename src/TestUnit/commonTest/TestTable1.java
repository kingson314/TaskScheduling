package TestUnit.commonTest;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

 

public class TestTable1  {
 public static DefaultTableModel model=null;
 public static void main(String[] ag){
  //定义一JTable类  

  JFrame jf = new JFrame(); 
  jf.setSize(800, 600);
        final JTable tbl_params=new JTable();
  //定义表格的行数据信息容器
  final Vector<Vector<String>> srcdata=new Vector<Vector<String>>();
  //初始化表格的列标题及相关的数据信息容器
  String[]columnName={"参数名称","参数的值"};
  //设置列标题
  final Vector<String> cname=new Vector<String>(2);
  cname.add(columnName[0]);
  cname.add(columnName[1]);
    model=new DefaultTableModel(srcdata,cname);  

  //填充数据演示
  Vector<String> rowdata=new Vector<String>(2);
  rowdata.add("参数名一");
  rowdata.add("参数值一");
  //将数据加入到容器，也就是加入到JTable中
  srcdata.add(rowdata); 
  tbl_params.setModel(model); 

  //最后刷新JTable控件
  //tbl_params.repaint();
  //tbl_params.updateUI();
  
  
  tbl_params.setSize(400, 300);
  final JScrollPane pan = new JScrollPane();
  pan.add(tbl_params);
  pan.setBounds(100,50, 800, 300);
  pan.getViewport().add(tbl_params);
   
  JButton insert = new JButton("Insert ");
  insert.addActionListener(new ActionListener(){
   public void actionPerformed(ActionEvent arg0) {
    System.out.println("111");
    @SuppressWarnings("unused")
	String[]columnName={"ss","bb"};
    //设置列标题
    //填充数据演示
    srcdata.clear();
    Vector<String> rowdata=new Vector<String>(2);
    rowdata.add("参数名er");
    rowdata.add("参数值er");
    //将数据加入到容器，也就是加入到JTable中
    srcdata.add(rowdata); 
    model=new DefaultTableModel(srcdata,cname);
    tbl_params.setModel(model); 

    //最后刷新JTable控件
    tbl_params.repaint();
    tbl_params.updateUI();
    pan.getViewport().add(tbl_params);

   }
  });
  insert.setBounds(20, 20, 100, 20);
  JPanel jPanel = new JPanel();
  jPanel.setLayout(new FlowLayout());
  jPanel.add(insert);
  jPanel.setBounds(0, 0, 800, 600);
  jf.add(jPanel,BorderLayout.NORTH);
  jf.add(pan,BorderLayout.CENTER);
  //jf.setContentPane(pan);
  //jf.pack();
  jf.setVisible(true);
 }
}


 
