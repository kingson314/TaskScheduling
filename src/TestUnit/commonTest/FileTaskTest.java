package TestUnit.commonTest;
 



public class FileTaskTest   {
/*
	@Test
	public final void testFileTask() {
		// new FileTask().fireTask();
	}

	@Test
	public final void testGetAllFilePath() {
		String dir = "d:/t3";
		String[] filePath = new FileTask().getAllFilePath(dir);
		for (int i = 0; i < filePath.length; i++) {
			// 输出自定义 文件夹路径 dir 的所有文件目录
			System.out.println(filePath[i]);
		}
	}

	@Test
	public final void testCopyAllFile() {
		String scrDir = "D:/t1";
		String destDir = "d:/t3";
		FileTask fileTask = new FileTask();
		fileTask.copyAllFile(scrDir, destDir);

		String[] scrFile = fileTask.getAllFilePath(scrDir);
		String lack = "";
		for (int i = 0; i < scrFile.length; i++) {
			File scrf = new File(scrFile[i]);

			String tmpdest = scrf.getParent().substring(
					scrf.getParent().indexOf(scrDir) + scrDir.length() + 1);
			// System.out.println(scrf.getParent());
			// System.out.println(destDir+tmpdest);
			File destf = new File(destDir + tmpdest + "/" + scrf.getName());
			// System.out.println(destDir + "/" + scrf.getName());
			if (!destf.exists()) {
				lack = lack + scrf.getAbsolutePath() + "\n";
			}
		}

		if (lack.length() > 0) {
			System.out.println("复制失败:\n" + lack + "没复制到");
		} else
			System.out.println("复制成功");
	}

	@Test
	public final void testCopyFile() {
		String scrDir = "D:/t1/开发通讯录.xls";
		String destDir = "d:/t3/开发通讯录.xls";
		FileTask fileTask = new FileTask();
		 fileTask.copyFile(scrDir, destDir);

		String lack = "";

		File destf = new File(destDir);
		;
		if (!destf.exists()) {
			lack = lack + scrDir + "\n";
		}

		if (lack.length() > 0) {
			System.out.println("复制失败:\n" + lack + "没复制到");
		} else
			System.out.println("复制成功");
	}
 
	@Test
	public final void testPluginInvoke() {
		FileTask fileTask = new FileTask();
		fileTask.setPluginName("WinRAR.exe");
		fileTask.setPluginPath("D:/Program Files/WinRAR/");
		//压缩
		//fileTask.setConsoleParm("A D:/开发通讯录.rar D:/开发通讯录.xls");
		//解压
		fileTask.setConsoleParm("x -ibck -y D:/开发通讯录.rar D:/");
		
		fileTask.pluginInvoke();
		System.out.println(fileTask.getJobStatus());
		System.out.println(fileTask.getFailReason());
	}

	public static void main(String[] args) {
		// new FileTaskTest().testGetAllFilePath();
		// new FileTaskTest().testCopyAllFile();
		//new FileTaskTest().testCopyFile();
		new FileTaskTest().testPluginInvoke();
	}
*/
}
