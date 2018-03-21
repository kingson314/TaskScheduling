package TestUnit.commonTest;

import java.util.Calendar;
import java.util.Date;


public class T {
	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		System.out.println(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
//		JTable t = new JTable(5, 5) {
//			@Override
//			protected JTableHeader createDefaultTableHeader() {
//				return new JTableHeader(columnModel) {
//					@Override
//					public void setDraggedColumn(TableColumn aColumn) {
//						if (aColumn == getColumnModel().getColumn(0)) {
//							return;
//						}
//						super.setDraggedColumn(aColumn);
//					}
//				};
//			}
//
//			protected TableColumnModel createDefaultColumnModel() {
//				return new DefaultTableColumnModel() {
//					@Override
//					public void moveColumn(int columnIndex, int newIndex) {
//						if (columnIndex == 0 || newIndex == 0) {
//							return;
//						}
//						super.moveColumn(columnIndex, newIndex);
//					}
//				};
//			}
//		};
//
//		JFrame f = new JFrame();
//		f.getContentPane().add(new SScrollPane(t), BorderLayout.CENTER);
//		f.pack();
//		f.setLocationRelativeTo(null);
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setVisible(true);
}
}





