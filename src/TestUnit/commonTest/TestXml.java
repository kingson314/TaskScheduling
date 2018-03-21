//package TestUnit.commonTest;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.util.List;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.SAXReader;
//import org.dom4j.io.XMLWriter;
//
//public class TestXml {
//	@SuppressWarnings("unchecked")
//	public static void main(String[] args) {
//		try {
//			XMLWriter writer = null;
//			SAXReader reader = new SAXReader();
//
//			OutputFormat format = OutputFormat.createPrettyPrint();
//			format.setEncoding("GBK");
//
//			String filePath = "d:/test.xml";
//			File file = new File(filePath);
//			if (file.exists()) {
//				Document document = reader.read(file);
//				Element root = document.getRootElement();
//
//				// for (Iterator<?> it = root.attributeIterator();
//				// it.hasNext();) {
//				// Attribute attribute = (Attribute) it.next();
//				// String text = attribute.getText();
//				// System.out.println(text);
//				// }
//				List list = root.elements();
//				for (int i = 0; i < list.size(); i++) {
//					Element item = (Element) list.get(i);
//					List it1 = item.elements();
//					for (int j = 0; j < it1.size(); j++) {
//						Element item1 = (Element) it1.get(j);
//						System.out.println(item1.getText());
//					}
//				}
//				boolean bl = false;
//				// for (Iterator<?> it = root.elementIterator("学生");
//				// it.hasNext();) {
//				// Element student = (Element) it.next();
//				// List list = student.elements("学生");
//				//
//				// // System.out.println(student.attributeValue("sid"));
//				// // bl = true;
//				//
//				// if (student.attributeValue("sid").equals("100")) {
//				// student.selectSingleNode("姓名").setText("fgq");
//				// student.selectSingleNode("年龄").setText("28");
//				// writer = new XMLWriter(new FileWriter(filePath), format);
//				// writer.write(document);
//				// writer.close();
//				//
//				// break;
//				// }
//				//
//				// }
//				if (bl) {
//					// 添加一个学生信息
//					Element student = root.addElement("学生");
//
//					Element sid = student.addElement("编号");
//					sid.setText("33333");
//
//					Element name = student.addElement("姓名");
//					name.setText("fgq");
//
//					Element sex = student.addElement("性别");
//					sex.setText("男");
//					Element age = student.addElement("年龄");
//					age.setText("25");
//					Element work = student.addElement("工作");
//					work.setText("工程师");
//
//					writer = new XMLWriter(new FileWriter(filePath), format);
//					writer.write(document);
//					writer.close();
//
//				}
//
//			} else {
//				Document document = DocumentHelper.createDocument();
//				Element root = document.addElement("学生信息");
//				Element student = root.addElement("学生");
//				student.setText("dadads");
//				Element id = student.addElement("编号");
//				id.setText("1000");
//				Element name = student.addElement("姓名");
//				name.setText("jaiji");
//
//				Element sex = student.addElement("性别");
//				sex.setText("男");
//				Element age = student.addElement("年龄");
//				age.setText("25");
//				writer = new XMLWriter(new FileWriter(filePath), format);
//				writer.write(document);
//				writer.close();
//
//			}
//			System.out.println("finished");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//}
