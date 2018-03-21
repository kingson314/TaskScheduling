package TestUnit.commonTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import common.util.file.UtilFile;

public class TestFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		writeFile();
	}

	private static void writeFile() {
		String[] filePath = UtilFile.getAllFilePath("D:/SVN/eFund/tyreal/高频调度管理助手/src/FileTrans");
		for (int i = 0; i < filePath.length; i++) {
			String fName = filePath[i];
			File destFile = new File(fName);
			String fName1 = "D:/File/" + destFile.getName() + ".txt";
			// System.out.println(fName);
			// File f = new File(fName);

			try {
				String content = "";
				String tmpline = "";
				BufferedReader fReader = new BufferedReader(new FileReader(fName)); // 读取文件原有内容
				while ((tmpline = fReader.readLine()) != null) {
					if (tmpline.indexOf("//") > 0)
						tmpline = tmpline.substring(0, tmpline.indexOf("//") - 1);
					if (tmpline.indexOf("*") > 0)
						continue;
					content += tmpline + "\n";
				}
				fReader.close();
				content = content.replaceAll("\n", "");
				content = content.replaceAll("\t", "");
				content = content.replaceAll("\b", "");
				content = content.replaceAll("\0", "");
				content = content.replaceAll("\r", "");
				content = content.replaceAll("\f", "");
				content = content.replaceAll("  ", "");
				content = content.replaceAll("   ", " ");
				content = content.replaceAll("    ", " ");
				content = content.replaceAll("     ", " ");
				content = content.replaceAll("      ", " ");
				content = content.replaceAll("       ", " ");
				content = content.replaceAll("        ", " ");
				content = content.replaceAll("         ", " ");
				content = content.replaceAll("          ", " ");
				content = content.replaceAll("           ", " ");
				content = content.replaceAll("            ", " ");
				content = content.replaceAll("             ", " ");
				content = content.replaceAll("              ", " ");
				content = content.replaceAll("               ", " ");
				content = content.replaceAll("                ", " ");
				content = content.replaceAll("                 ", " ");
				content = content.replaceAll("                  ", " ");
				content = content.replaceAll("                   ", " ");
				content = content.replaceAll("                    ", " ");
				content = content.replaceAll("                     ", " ");
				content = content.replaceAll("                      ", " ");
				content = content.replaceAll("                       ", " ");
				content = content.replaceAll("                        ", " ");
				content = content.replaceAll("                         ", " ");

				BufferedWriter fWriter = new BufferedWriter(new FileWriter(fName1));
				fWriter.write(content);
				fWriter.flush();
				fWriter.close();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println(1);

	}
}
