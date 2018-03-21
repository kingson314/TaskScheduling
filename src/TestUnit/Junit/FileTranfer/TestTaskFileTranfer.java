//package TestUnit.Junit.FileTranfer;
//import static org.junit.Assert.*;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.junit.Test;
//
//import com.ts.common.Parser;
//import com.ts.task.FileTransfer.TaskFile;
//
//public class TestTaskFileTranfer {
//	private static TaskFile task = new TaskFile();
//	private SimpleDateFormat formatdate = new SimpleDateFormat(// ���ڸ�ʽ
//	"yyyyMMdd");
//
//	@Test(timeout=1000,expected=ArithmeticException.class)
//	public void fireTask() {
//		
//		task.setTaskId(1l);
//		task.setTaskName("�ļ�����");
//		task.setInterval(60l);
//		String jsonStr = "{'srcFileType':'FILE','pluginPath':'D:/Program Files/WinRAR/','srcFilePath':'D:/Դ�ļ���/����/','ifCreateOkFile':false,'consoleParam':'x -ibck -y D:/Ŀ���ļ���/settings%date:yyyyMMdd%.rar D:/Ŀ���ļ���/','destFileType':'FILE','destFilePath':'D:/Ŀ���ļ���/','delSrcFile':false,'pluginName':'WinRAR.exe','overwrite':true,'destFileName':'settings%date:yyyyMMdd%.rar','srcFileName':'settings.rar','locked':false,'enablePlugin':false}";
//		task.setJsonStr(jsonStr);
//		task.setLogType("��ͨ������־");
//		task.setMonitorGroup("1");
//		task.setNowDate(formatdate.format(new Date()));
//		task.setOverTime(0l);
//		task.setTaskMemo("");
//		task.setTaskOrder("");
//		task.setTaskType("�ļ��ַ�");
//		task.setWarnType("ȫ��");
//		task.fireTask();
//		File file=new File(Parser.parse("D:/Ŀ���ļ���/settings%date:yyyyMMdd%.rar",new Date()));
//		assertEquals(true, file.exists());
//	}
//}
