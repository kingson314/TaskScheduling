//package TestUnit.commonTest;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPFile;
//
//
//
//public class TestFTP {
//
//	/**
//	 * @param args
//	 * @throws FTPListParseException
//	 */
//	private List<String> folder = new ArrayList<String>();
//
//	public static void main(String[] args) throws FTPListParseException {
//		System.out.println(Long.SIZE);
//		String ftpIp = "127.0.0.1";
//		String ftpPort = "21";
//		String ftpUser = "fenggq";
//		String ftpPassword = "fenggq";
//		String ftpfolder = "/";
//		String remotefileName = "飞鸽传书 UM2010.txt";
//		String localfile = "D:/t3/复件 复件 开发通讯录.xls";
//		int flag = 0;
//		if (flag == 1) {
//			new TestFTP().ftpDownLoad(ftpIp, ftpPort, ftpUser, ftpPassword,
//					ftpfolder, remotefileName, localfile);
//		} else {
//			ftpUpLoad(ftpIp, ftpPort, ftpUser, ftpPassword, ftpfolder,
//					remotefileName, localfile);
//		}
//		/*
//		 * TaskTreeEntry taskTreeEntry = new TaskTreeEntry() ;
//		 * taskTreeEntry.getJobEntry().getTask().setJobStatus("正在执行");
//		 * taskTreeEntry.getJobEntry().getTask().setFailReason("");
//		 * taskTreeEntry.getJobEntry().getTask().fireTask();
//		 */
//	}
//
//	protected void ftpDownLoad(String ftpIp, String ftpPort, String ftpUser,
//			String ftpPassword, String ftpfolder, String remotefileName,
//			String localfile) throws FTPListParseException {
//		FTPClient client = new FTPClient();
//		try {
//			client.connect(ftpIp, Integer.parseInt(ftpPort));
//			if (!ftpUser.equals("anonymous"))
//				client.login(ftpUser, ftpPassword);
//			else
//				client.login("anonymous", "no@password.com");
//
//			client.changeDirectory(ftpfolder);
//			List<String> f = getAllFtpFolder(ftpfolder, localfile, client);
//			f.add(ftpfolder);
//
//			for (int j = 0; j < folder.size(); j++) {
//				// System.out.println(folder.get(j));
//				client.changeDirectory(folder.get(j));
//				FTPFile[] file = client.list();
//				// System.out.println(file.length);
//				for (int i = 0; i < file.length; i++) {
//					if (file[i].getType() == 1)
//						continue;
//					// System.out.println(localfile+folder.get(j)+file[i].getName());
//					client.download(file[i].getName(), new java.io.File(
//							localfile + folder.get(j) + file[i].getName()));
//				}
//			}
//
//			client.disconnect(true);
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (FTPIllegalReplyException e) {
//			e.printStackTrace();
//		} catch (FTPException e) {
//			e.printStackTrace();
//		} catch (FTPDataTransferException e) {
//			e.printStackTrace();
//		} catch (FTPAbortedException e) {
//			e.printStackTrace();
//		}
//	}
//
//	protected List<String> getAllFtpFolder(String ftpfolder, String localfile,
//			FTPClient client) {
//
//		FTPFile[] file;
//		try {
//			// System.out.println(client.currentDirectory());
//			file = client.list();
//			for (int i = 0; i < file.length; i++) {
//				if (file[i].getType() == 1) {
//					File lfile = new File(localfile + file[i].getName() + "/");
//					if (!lfile.exists()) {
//						lfile.mkdir();
//					}
//					// System.out.println(ftpfolder+file[i].getName()+"/");
//					folder.add(ftpfolder + file[i].getName() + "/");
//					client.changeDirectory(ftpfolder + file[i].getName() + "/");
//					getAllFtpFolder(ftpfolder + file[i].getName() + "/",
//							localfile + file[i].getName() + "/", client);
//
//				} /*
//					 * else if (file[i].getType()==0){
//					 * 
//					 * ftpDownLoadFile(file[i].getName(),localfile+"/"+file[i].getName(),client);
//					 * System.out.println(file[i].getName());
//					 * //System.out.println(localfile); }
//					 */
//			}
//
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (FTPIllegalReplyException e) {
//			e.printStackTrace();
//		} catch (FTPException e) {
//			e.printStackTrace();
//		} catch (FTPDataTransferException e) {
//			e.printStackTrace();
//		} catch (FTPAbortedException e) {
//			e.printStackTrace();
//		} catch (FTPListParseException e) {
//			e.printStackTrace();
//		}
//		return folder;
//	}
//
//	public static String[] getAllFilePath(String path, int flag)
//			throws IOException {
//		String[] sfile = null;
//		List<String> filePath = new ArrayList<String>();
//		LinkedList<File> list = new LinkedList<File>();
//		File dir = new File(path);
//		File file[] = dir.listFiles();
//		for (int i = 0; i < file.length; i++) {
//			if (file[i].isDirectory())
//				list.add(file[i]);
//			else {
//
//				// System.out.println(file[i].getAbsolutePath());
//				if (flag == 0) {
//					filePath.add(file[i].getAbsolutePath());
//				} else {
//					filePath.add(file[i].getParent());
//				}
//			}
//		}
//		File tmp;
//		while (!list.isEmpty()) {
//			tmp = (File) list.removeFirst();
//			if (tmp.isDirectory()) {
//				file = tmp.listFiles();
//				if (file == null)
//					continue;
//				for (int i = 0; i < file.length; i++) {
//					if (file[i].isDirectory())
//						list.add(file[i]);
//					else {
//					//	 System.out.println(file[i].getAbsolutePath());
//						if (flag == 0) {
//							filePath.add(file[i].getAbsolutePath());
//						} else {
//							filePath.add(file[i].getParent());
//						}
//					}
//				}
//			} else {
//				// System.out.println(tmp.getAbsolutePath());
//				if (flag == 0) {
//					filePath.add(tmp.getAbsolutePath());
//				} else {
//					filePath.add(tmp.getParent());
//				}
//			}
//		}
//
//		sfile = new String[filePath.size()];
//		for (int i = 0; i < filePath.size(); i++) {
//			sfile[i] = filePath.get(i);		
//
//		}
//		return sfile;
//	}
//
//	protected static void ftpUpLoad(String ftpIp, String ftpPort,
//			String ftpUser, String ftpPassword, String ftpfolder,
//			String remotefileName, String localfile) {
//		FTPClient client = new FTPClient();
//		try {
//			client.connect(ftpIp, Integer.parseInt(ftpPort));
//			if (!ftpUser.equals("anonymous"))
//				client.login(ftpUser, ftpPassword);
//			else
//				client.login("anonymous", "no@password.com");
//			try {
//
//				client.changeDirectory(ftpfolder);
//			} catch (FTPException e) {
//				client.createDirectory(ftpfolder);
//				client.changeDirectory(ftpfolder);
//			}
//			
//			
//			String[] upFile = null;
//			String[] upFilePath = null;
//			String dir = null;
//			File lFile = new File(localfile);
//			if (lFile.isFile()) {
//				upFile = new String[] { localfile };
//			} else if (lFile.isDirectory()) {
//				dir = lFile.getPath();
//				upFilePath = getAllFilePath(localfile, 1);
//				upFile = getAllFilePath(localfile, 0);
//			}
//			/*for(int j=0;j<upFile.length;j++){
//				System.out.println(upFile[j]);
//			}*/
//			for (int i = 0; i < upFile.length; i++) {
//				//System.out.println(upFile[i]);
//				client.changeDirectory(ftpfolder);
//				if (upFilePath == null) {
//					 client.upload(new java.io.File(localfile));
//					// client.rename(Parser.getLocalFileName(localfile),remotefileName);
//				} else { 
//					
//					String tmpPath = upFilePath[i].substring(upFilePath[i]
//							.indexOf(dir)
//							+ dir.length());
//					
//					if (tmpPath.length() > 0) {
//						if (tmpPath.substring(0, 1).equalsIgnoreCase("/")) {
//							tmpPath=tmpPath.substring(1);
//						
//						}
//						//System.out.println("upFilePath:      " + upFilePath[i]);
//						// System.out.println("upFile: "+upFile[i]);
//						@SuppressWarnings("unused")
//						String curfolder= client.currentDirectory();
//						try{
//							client.changeDirectory(tmpPath);
//						}catch (FTPException e) {
//							client.createDirectory(tmpPath); 
//						}
//						 client.upload(new java.io.File(upFile[i]));
//					}else{
//						client.upload(new java.io.File(upFile[i]));
//					}
//				}
//			}
//
//			client.disconnect(true);
//		} catch (IllegalStateException e) {
//		} catch (IOException e) {
//		} catch (FTPIllegalReplyException e) {
//		} catch (FTPException e) {
//		} catch (FTPDataTransferException e) {
//			e.printStackTrace();
//		} catch (FTPAbortedException e) {
//			e.printStackTrace();
//		}
//	}
//}
