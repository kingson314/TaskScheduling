package TestUnit.commonTest;
 
 
  

public class FtpTaskTest /*extends TaskFtp*/ { 
 /*

	@Test
	public final void testGetAllFtpFolder() {
		FtpSite ftpSite = new FtpSite();
		ftpSite.setFtpFolder("/");
		ftpSite.setFtpId(Long.valueOf(1));
		ftpSite.setFtpInfo("测试服务器");
		ftpSite.setFtpIp("127.0.0.1");
		ftpSite.setFtpName("测试服务器");
		ftpSite.setFtpPassword("fenggq");
		ftpSite.setFtpPort("21");
		ftpSite.setFtpUser("fenggq");
		FTPClient client = new FTPClient();
		String localfile = "D/t2";

		try {
			client.connect(ftpSite.getFtpIp(), Integer.parseInt(ftpSite
					.getFtpPort()));

			if (!ftpSite.getFtpUser().equals("anonymous"))
				client.login(ftpSite.getFtpUser(), ftpSite.getFtpPassword());
			else
				client.login("anonymous", "no@password.com");
			FtpTask ftpTask = new FtpTask();
			ftpTask.getAllFtpFolder(ftpSite.getFtpFolder(),
					localfile, client);
			
			for(int i=0;i<ftpTask.folder.size();i++){
				System.out.println(ftpTask.folder.get(i));
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			e.printStackTrace();
		} catch (FTPException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testFtpDownLoad() {
		FtpSite ftpSite = new FtpSite();
		ftpSite.setFtpFolder("/");
		ftpSite.setFtpId(Long.valueOf(1));
		ftpSite.setFtpInfo("测试服务器");
		ftpSite.setFtpIp("127.0.0.1");
		ftpSite.setFtpName("测试服务器");
		ftpSite.setFtpPassword("fenggq");
		ftpSite.setFtpPort("21");
		ftpSite.setFtpUser("fenggq");
		 ;
		FtpTask ftpTask=new FtpTask();
		//文件夹下载
		ftpTask.setDestFileName("");
		ftpTask.setSrcFileName("ftp://%ftpname:测试服务器%");
		ftpTask.setDestFilePath("d:/t1/");
		ftpTask.setSrcFilePath("/1");
		ftpTask.ftpDownLoad(ftpSite, null,null);
		//----
		//文件下载
		ftpTask.setDestFileName("飞鸽传书 UM2010.txt");
		ftpTask.setSrcFileName("飞鸽传书 UM2010.txt");
		ftpTask.setDestFilePath("d:/t1/");
		ftpTask.setSrcFilePath("ftp://%ftpname:测试服务器%");		
		String src = Parser.parse(ftpTask.getSrcFileName()); 		 
		String destPath = Parser.parse(ftpTask.getDestFilePath());// temp文件夹
		String dest = destPath + Parser.parse(ftpTask.getDestFileName());// 注意: 此处用目的文件名	
		ftpTask.ftpDownLoad(ftpSite, src, dest); 
		//--- 
		
		
	}

	@Test
	public final void testFtpUpLoad() {
		FtpSite ftpSite = new FtpSite();
		ftpSite.setFtpFolder("/");
		ftpSite.setFtpId(Long.valueOf(1));
		ftpSite.setFtpInfo("测试服务器");
		ftpSite.setFtpIp("127.0.0.1");
		ftpSite.setFtpName("测试服务器");
		ftpSite.setFtpPassword("fenggq");
		ftpSite.setFtpPort("21");
		ftpSite.setFtpUser("fenggq");
		 ;
		FtpTask ftpTask=new FtpTask();
		//文件夹上传
		ftpTask.setDestFileName("");
		ftpTask.setSrcFileName("");
		ftpTask.setDestFilePath("ftp://%ftpname:测试服务器%");
		ftpTask.setSrcFilePath("d:/t1/");
		ftpTask.ftpUpLoad( ftpTask.getSrcFilePath(), ftpSite,null);
		//----
		//文件上传
		ftpTask.setDestFileName("飞鸽传书 UM2010.txt");
		ftpTask.setSrcFileName("飞鸽传书 UM2010.txt");
		ftpTask.setDestFilePath("ftp://%ftpname:测试服务器%");
		ftpTask.setSrcFilePath("d:/t1/");	
		String src = Parser.parse(ftpTask.getSrcFilePath())+Parser.parse(ftpTask.getSrcFileName()); 		 
		String destPath = Parser.parse(ftpTask.getDestFilePath());// temp文件夹
		String dest = destPath + Parser.parse(ftpTask.getDestFileName());// 注意: 此处用目的文件名	
		ftpTask.ftpUpLoad(src, ftpSite,dest);
		//--- 
		
	}
   public static void main(String[] args){
	   //new FtpTaskTest().testGetAllFtpFolder();
	   //new FtpTaskTest().testFtpDownLoad();
	   new FtpTaskTest().testFtpUpLoad();
   }*/
}
