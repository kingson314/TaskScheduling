package TestUnit.commonTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import common.util.file.UtilFile;

public class TestJavaToDoc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// System.out.println("2012-03-08 23:00:00".compareTo("2012-04-10
		// 23:30:00"));
		// String content="abc/***123ddd*/\ndase ada \n/**8ds8ds*/dadsad";
		// while(content.indexOf("*/")>=0){
		// String beg=content.substring(0,content.indexOf("/*"));
		// String end=content.substring(content.indexOf("*/")+2);
		// content=beg+end;
		// }
		// System.out.println(content);
		writeFile();
	}

	private static void writeFile() {
		String[] filePath = UtilFile.getAllFilePath("D:/File/FileTrans_java/");
		for (int i = 0; i < filePath.length; i++) {
			String fName = filePath[i];
			File srcFile = new File(fName);
			String destFilepath = srcFile.getAbsolutePath().replaceAll("FileTrans_java", "FileTrans_doc");
			File destFile = new File(destFilepath);
			File destpath = new File(destFile.getParent());
			destpath.mkdirs();
			try {
				String content = "";
				String tmpline = "";
				BufferedReader fReader = new BufferedReader(new FileReader(fName)); // 读取文件原有内容
				while ((tmpline = fReader.readLine()) != null) {
					if (tmpline.indexOf("//") > 0) {
						tmpline = tmpline.substring(0, tmpline.indexOf("//"));
					}
					if (tmpline.indexOf("import") >= 0) {
						continue;
					}
					content = content + tmpline;
				}
				fReader.close();
				content = ChineseSpelling.getInstance().getSelling(content);
				content = content.replaceAll("\n", "");
				content = content.replaceAll("\t", "");
				content = content.replaceAll("\b", "");
				content = content.replaceAll("\0", "");
				content = content.replaceAll("\r", "");
				content = content.replaceAll("\f", "");
				content = content.replaceAll("！", "!");
				content = content.replaceAll(":", ":");
				content = content.replaceAll("\"", "#");

				content = content.replaceAll("/(/)", "/( /)");
				content = content.replaceAll("==", "= =");

				content = content.replaceAll("e.printStackTrace();", "");
				content = content.replaceAll("  ", " ");

				int rowCount = 56;
				int row = 0;
				String space = " ";
				for (int k = 0; k < 10; k++) {
					space = space + "                    ";
				}
				String lSpace = "        ";
				String rSpace = space;
				String wContent = space + "\n" + space + "\n" + space + "\n" + space + "\n" + space + "\n";
				content = content.trim();

				// while (content.indexOf("*/") >= 0) {
				// String beg = content.substring(0, content.indexOf("/*"));
				// String end = content.substring(content.indexOf("*/") + 2);
				// content = beg + end;
				// }

				while (content.length() > 0) {
					int leng = 150;
					if (content.length() >= leng) {
						String tmpStr = content.substring(0, leng);
						if (tmpStr.getBytes().length > leng) {
							leng = 2 * leng - tmpStr.getBytes().length;
							if (leng % 2 != 0)
								leng = leng - 1;
							tmpStr = content.substring(0, leng);
						}
						int leng1 = tmpStr.lastIndexOf(";");
						int leng2 = tmpStr.lastIndexOf(" ");
						int tmpleng = 0;

						if (leng2 > leng1) {
							tmpleng = leng2;
						} else
							tmpleng = leng1;

						if (tmpleng != -1) {
							leng = tmpleng;
						}

						tmpStr = content.substring(0, leng + 1);
						wContent = wContent + lSpace + tmpStr + rSpace + "\n";
						row = row + 1;
						if (row % rowCount == 0) {
							wContent = wContent + space + "\n" + space + "\n" + space + "\n" + space + "\n" + space + "\n";
						}
						content = content.substring(leng + 1);
					} else {
						wContent = wContent + lSpace + content.substring(0);
						content = "";
					}
				}
				wContent = wContent + rSpace;

				BufferedWriter fWriter = new BufferedWriter(new FileWriter(destFilepath + ".txt"));
				fWriter.write(wContent);
				fWriter.flush();
				fWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// System.out.println(Fun.formattime.format(new Date()));
	}
}
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");
// content = content.replaceAll(" ", " ");

// content = content.replaceAll("/+", " + ");
//	
// content = content.replaceAll("/[", "4a");
// content = content.replaceAll("/]", "4b");
// content = content.replaceAll("/{", "4c");
// content = content.replaceAll("/}", "4d");
//	
// content = content.replaceAll("I", "4e");
// content = content.replaceAll("m", "4f");
// content = content.replaceAll("1", "4g");
// content = content.replaceAll("/(/)", "4h");
// content = content.replaceAll("==", "4k");

// String vchar="";
// for(int k=0;k<content.length();k++){
// vchar=vchar+ (int)content.charAt(k)+" ";
// }
// content=vchar;

// String file = "D:/ok.txt";
// File newFile = new File(file);
// try {
// if (!newFile.exists())
// newFile.createNewFile();
// } catch (IOException e) {
// e.printStackTrace();
// }
// file=file.replace("/", "/");
// String extension=file.substring(0,file.indexOf("."));
// //String trueName=extension.replace("/", "");
// System.out.println(extension);
// String str="1ab中国";
// for(int i=0;i<str.length();i++){
// if(str.charAt(i)>256){
// System.out.println(str.charAt(i));
// }
// }
