package com.task.BackMySQL;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;
import com.taskInterface.TaskAbstract;

import common.util.file.UtilFile;
import common.util.json.UtilJson;
import common.util.security.UtilCrypt;

public class Task extends TaskAbstract {

	// 执行
	public void fireTask() {
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			DbConnection dbconn=DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			 String tmp=dbconn.getDbCon().substring(dbconn.getDbCon().indexOf("//")+2);
			String dbUrl=tmp.substring(0, tmp.indexOf(":"));
			 String dbName=dbconn.getDbCon().substring(dbconn.getDbCon().lastIndexOf("/")+1);
			 String userName=dbconn.getDbUser();
			 String password= UtilCrypt.getInstance().decryptAES(
						dbconn.getDbPassword(), UtilCrypt.key);
			 String filePath=bean.getFilePath();
			 System.out.println(dbUrl+"/"+dbName+"/"+userName+"/"+password+"/"+filePath);
			 String mysqlPath=bean.getDbPath();
			 this.backupMySql( mysqlPath,dbUrl,dbName,userName,password,filePath);
			this.setTaskStatus("执行成功");
			this.setTaskMsg(filePath);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("执行错误:", e);
		} finally {
		}
	}
	
	private void backupMySql(String mysqlPath,String dbUrl,String dbName,String userName ,String password,String filePath) throws Exception{
		   Runtime rt = Runtime.getRuntime();
		   // 调用 调用mysql的安装目录的命令
		   Process child = rt.exec(mysqlPath+"//bin//mysqldump -h "+dbUrl+" -u"+userName+" -p"+password+"  "+dbName);
		   // 设置导出编码为utf-8。这里必须是utf-8
		   // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
		   InputStream in = child.getInputStream();// 控制台的输出信息作为输入流
		   InputStreamReader inputStreamReader = new InputStreamReader(in, "utf-8");
		   // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码
		   String line;
		   StringBuffer sb = new StringBuffer();
		   // 组合控制台输出信息字符串
		   BufferedReader br = new BufferedReader(inputStreamReader);
		   while ((line = br.readLine()) != null) {
		    sb.append(line + "\r\n");
		   }
		   // 要用来做导入用的sql目标文件：
		   UtilFile.writeFile(filePath, sb.toString());
		   System.out.println("done");
}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

}