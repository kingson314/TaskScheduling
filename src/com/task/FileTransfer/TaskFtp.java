package com.task.FileTransfer;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import module.ftp.FtpSite;

import com.app.Parser;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.log.Log;

import common.util.file.UtilFile;
import common.util.json.UtilJson;
import common.util.security.UtilCrypt;
import consts.Const;

public class TaskFtp extends TaskFile implements FTPDataTransferListener {
	protected List<String> folder = new ArrayList<String>();

	public void fireTask() {
		try {
			this.date = this.getDate();
			this.bean = (Bean) UtilJson.getJsonBean(this.getJsonStr(), Bean.class);

			if (this.bean.getSrcFileType().equals(Const.TRANS_TYPE_FTP)// ftp-ftp
					&& this.bean.getDestFileType().equals(Const.TRANS_TYPE_FTP)) {
				downLoad(this.bean.getSrcFilePath(), this.bean.getSrcFileName(), Const.TEMP_DIR, this.bean.getSrcFileName());
				upLoad(Const.TEMP_DIR, this.bean.getSrcFileName(), this.bean.getDestFilePath(), this.bean.getDestFileName());
				if (this.bean.getEnablePlugin() && !this.getTaskStatus().equals("执行失败")) {
					pluginInvoke();
				}
				UtilFile.delAllFile(Const.TEMP_DIR);

			} else if (this.bean.getSrcFileType().equals(Const.TRANS_TYPE_FTP)// 下载
					&& this.bean.getDestFileType().equals(Const.TRANS_TYPE_FILE)) {
				downLoad(this.bean.getSrcFilePath(), this.bean.getSrcFileName(), this.bean.getDestFilePath(), this.bean.getDestFileName());

				if (this.bean.getEnablePlugin() && !this.getTaskStatus().equals("执行失败")) {
					pluginInvoke();
				}
			} else if (this.bean.getSrcFileType().equals(Const.TRANS_TYPE_FILE)// 上传
					&& this.bean.getDestFileType().equals(Const.TRANS_TYPE_FTP)) {
				upLoad(this.bean.getSrcFilePath(), this.bean.getSrcFileName(), this.bean.getDestFilePath(), this.bean.getDestFileName());
				if (this.bean.getEnablePlugin() && !getTaskStatus().equals("执行失败")) {
					pluginInvoke();
				}
			} else {
				super.fireTask();
			}
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("错误: ", e);
		}
	}

	// 上传
	protected void upLoad(String sFilePath, String sFileName, String dFilePath, String dFileName) {
		FtpSite ftpSite = Parser.parseFtpSite(dFilePath);
		if (ftpSite.getFtpType().equals("SFTP")) {
			sftpUpLoad(ftpSite, sFilePath, sFileName, dFilePath, dFileName);
		} else {
			for (int i = 1; i <= 10; i++) {
				if (ftpUpLoad(ftpSite, sFilePath, sFileName, dFilePath, dFileName) != 10002) {
					break;
				}
				Log.logWarn("任务[" + this.getTaskId() + "]上传FTP连接错误，自动重试上传 第" + i + "次");
			}
		}
	}

	// 下载
	protected void downLoad(String sFilePath, String sFileName, String dFilePath, String dFileName) {
		FtpSite ftpSite = Parser.parseFtpSite(sFilePath);
		if (ftpSite.getFtpType().equals("SFTP")) {
			sftpDownLoad(ftpSite, sFilePath, sFileName, dFilePath, dFileName);
		} else {
			for (int i = 1; i <= 10; i++) {
				if (ftpDownLoad(ftpSite, sFilePath, sFileName, dFilePath, dFileName) != 10002) {
					break;
				}
				Log.logWarn("任务[" + this.getTaskId() + "]下载FTP连接错误，自动重试下载 第" + i + "次");
			}

		}
	}

	// 获取Sftp通道
	private ChannelSftp getsFtp(FtpSite ftpSite) {
		ChannelSftp sftp = null;
		try {
			UtilCrypt crypt = UtilCrypt.getInstance();
			JSch jsch = new JSch();
			jsch.getSession(ftpSite.getFtpUser(), ftpSite.getFtpIp(), Integer.valueOf(ftpSite.getFtpPort()));
			Session sshSession = jsch.getSession(ftpSite.getFtpUser(), ftpSite.getFtpIp(), Integer.valueOf(ftpSite.getFtpPort()));
			sshSession.setPassword(crypt.decryptAES(ftpSite.getFtpPassword(), UtilCrypt.key));
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (NumberFormatException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg(ftpSite.getFtpId() + "获取sFTP连接数字转换错误: ", e);
		} catch (JSchException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg(ftpSite.getFtpId() + "获取sFTP连接错误: ", e);
		}
		return sftp;
	}

	// sftp上传
	protected void sftpUpLoad(FtpSite ftpSite, String sFilePath, String sFileName, String dFilePath, String dFileName) {
		ChannelSftp sftp = getsFtp(ftpSite);
		FileInputStream fo = null;
		try {
			sFilePath = Parser.parse(sFilePath, date);
			sFileName = Parser.parse(sFileName, date);
			dFilePath = Parser.parse(dFilePath, date);
			dFileName = Parser.parse(dFileName, date);

			File file = new File(sFilePath + sFileName);
			System.out.println(sFilePath + sFileName);
			String ftpdir = "ftp://" + ftpSite.getFtpName() + "/";
			// 获取FTP自动配置后面的手工添加目录
			String ftp = dFilePath.replace(ftpdir, "");
			if (ftp != null && !"".equals(ftp)) {
				sftp.cd(ftp);
			}

			String[] upFilePath = null;
			if (file.isFile()) {
				upFilePath = new String[] { file.getAbsolutePath() };
			} else if (file.isDirectory()) {
				upFilePath = UtilFile.getFilePathInCurrentDir(file.getAbsolutePath(), 0);
			}

			for (int i = 0; i < upFilePath.length; i++) {
				File sFtpFile = new File(upFilePath[i]);
				fo = new FileInputStream(sFtpFile);
				// System.out.println(sFtpFile.getName());
				sftp.put(fo, sFtpFile.getName());
				fo.close();
				if (upFilePath.length == 1) {// 单个文件上传支持改名
					if (!sFtpFile.getName().equalsIgnoreCase(dFileName)) {
						sftp.rename(sFtpFile.getName(), dFileName);
					}
				}
				// 在临时文件夹内创建一个ok文件
				if (this.bean.getIfCreateOkFile()) {
					String upOKfilepath = UtilFile.createOkFile(Const.TEMP_DIR + sFtpFile.getName());
					File uploadOkFile = new java.io.File(upOKfilepath);
					fo = new FileInputStream(uploadOkFile);
					sftp.put(fo, uploadOkFile.getName());
					uploadOkFile.delete();
				}
				if (this.bean.getDelSrcFile()) {
					try {
						sFtpFile.delete();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			this.setTaskStatus("执行成功");
			this.setTaskMsg(sFilePath + sFileName + "到\n" + dFilePath + dFileName);

		} catch (SftpException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg(ftpSite.getFtpId() + "上传错误: ", e);

		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg(ftpSite.getFtpId() + "上传错误1: ", e);

		} finally {
			try {
				if (fo != null)
					fo.close();
			} catch (IOException e) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg(ftpSite.getFtpId() + "释放下载变量文件错误: ", e);

			}
			try {
				if (sftp != null) {
					if (sftp.isConnected()) {
						sftp.disconnect();
						sftp = null;
					}
				}
			} catch (Exception e) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg(ftpSite.getFtpId() + "释放sFTP连接错误: ", e);

			}
		}
	}

	// sftp下载
	@SuppressWarnings("unchecked")
	protected void sftpDownLoad(FtpSite ftpSite, String sFilePath, String sFileName, String dFilePath, String dFileName) {
		ChannelSftp sftp = getsFtp(ftpSite);
		FileOutputStream fo = null;
		String ftpPath = "";
		try {
			sFilePath = Parser.parse(sFilePath, date);
			sFileName = Parser.parse(sFileName, date);
			dFilePath = Parser.parse(dFilePath, date);
			dFileName = Parser.parse(dFileName, date);
			if (sFilePath != null && !"".equals(sFilePath)) {
				String ftpdir = "ftp://" + ftpSite.getFtpName() + "/";
				// 获取FTP自动配置后面的手工添加目录
				// System.out.println(ftpdir);
				// System.out.println(sFilePath);
				ftpPath = Parser.parse(sFilePath.replace(ftpdir, ""), date);
				// System.out.println(ftpPath);
				sftp.cd(ftpPath);
			}

			File dir = new File(dFilePath);
			dir.mkdirs();
			if (sFileName.length() < 1 && dFileName.length() < 1) {
				Vector v = sftp.ls(ftpPath);
				Iterator it = v.iterator();
				while (it.hasNext()) {
					try {
						Object object = it.next();
						String[] filemsg = object.toString().split(" ");
						String filePath = dFilePath + filemsg[filemsg.length - 1];
						if (!dFilePath.trim().endsWith("/") && !dFilePath.trim().endsWith("/"))
							filePath = dFilePath + "/" + filemsg[filemsg.length - 1];
						File file = new File(filePath);
						fo = new FileOutputStream(file);
						sftp.get(filemsg[filemsg.length - 1], fo);
						fo.close();
						if (this.bean.getIfCreateOkFile()) {
							UtilFile.createOkFile(file.getAbsolutePath());
						}
						// if (this.bean.getDelSrcFile()) {
						// try {
						// sftp.rm(filemsg[filemsg.length - 1]);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
					} catch (Exception e) {
						fo.close();
						continue;
					}
				}
			} else {
				String filePath = dFilePath + dFileName;
				if (!dFilePath.trim().endsWith("/") && !dFilePath.trim().endsWith("/"))
					filePath = dFilePath + "/" + dFileName;
				// System.out.println(filePath);
				File file = new File(filePath);
				if (file.isFile())
					if (file.exists()) {
						if (!this.bean.getOverwrite()) {
							this.setTaskStatus("执行成功");
							this.setTaskMsg("文件已存在,任务忽略");
							return;
						}
					}
				fo = new FileOutputStream(file);
				sftp.get(sFileName, fo);
				// if (this.bean.getDelSrcFile()) {
				// try {
				// sftp.rm(sFileName);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// }

				if (this.bean.getIfCreateOkFile()) {
					UtilFile.createOkFile(file.getAbsolutePath());
				}
			}
			this.setTaskStatus("执行成功");
			this.setTaskMsg(sFilePath + sFileName + "到\n" + dFilePath + dFileName);

		} catch (SftpException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg(ftpSite.getFtpId() + "下载错误: ", e);

		} catch (IOException e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg(ftpSite.getFtpId() + "下载错误1: ", e);
		} finally {
			try {
				if (fo != null)
					fo.close();
			} catch (IOException e) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg(ftpSite.getFtpId() + "释放下载变量文件错误: ", e);

			}
			try {
				if (sftp != null) {
					if (sftp.isConnected()) {
						sftp.disconnect();
						sftp = null;
					}
				}
			} catch (Exception e) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg(ftpSite.getFtpId() + "释放sFTP连接错误: ", e);

			}
		}
	}

	// ftp上传
	protected int ftpUpLoad(FtpSite ftpSite, String sFilePath, String sFileName, String dFilePath, String dFileName) {
		int rs = 0;
		if (ftpSite == null) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP上传错误: " + "FTP站点模板解析错误!");
			return -1;
		}
		sFilePath = Parser.parse(sFilePath, date);
		sFileName = Parser.parse(sFileName, date);
		dFilePath = Parser.parse(dFilePath, date);
		dFileName = Parser.parse(dFileName, date);

		String src = sFilePath + sFileName;
		File srcFile = new File(src);
		// String spath = srcFile.getParent();
		// File sfile = new File(spath);
		// if (!sfile.exists())
		// sfile.mkdirs();
		if (srcFile.exists() == false) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件 " + Parser.removeSlash(src) + " 不存在!");
			return -1;
		} else {
			// 判断文件是否正被外部程序打开
			if (this.bean.getLocked() == true) {
				if (srcFile.isFile()) {
					if (locked(srcFile, src) == false)
						return -1;
				}
			}
		}
		FTPClient client = new FTPClient();
		try {
			UtilCrypt crypt = UtilCrypt.getInstance();
			client.connect(ftpSite.getFtpIp(), Integer.parseInt(ftpSite.getFtpPort()));
			if (!ftpSite.getFtpUser().equals("anonymous"))
				client.login(ftpSite.getFtpUser(), crypt.decryptAES(ftpSite.getFtpPassword(), UtilCrypt.key));
			else
				client.login("anonymous", "no@password.com");
			// 如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码
			client.setType(FTPClient.TYPE_BINARY);
			try {
				client.changeDirectory(Parser.parse(ftpSite.getFtpFolder(), date));
			} catch (FTPException e) {
				try {
					client.createDirectory(Parser.parse(ftpSite.getFtpFolder(), date));
					client.changeDirectory(Parser.parse(ftpSite.getFtpFolder(), date));
				} catch (Exception e1) {
					client.changeDirectory(Parser.parse(ftpSite.getFtpFolder(), date));
				}
			}

			String[] upFile = null;
			String[] upFilePath = null;
			String dir = null;
			// File lFile = new File(src);
			if (srcFile.isFile()) {
				upFile = new String[] { src };
			} else if (srcFile.isDirectory()) {
				dir = srcFile.getPath();
				upFilePath = UtilFile.getAllFilePath(src, 1);
				upFile = UtilFile.getAllFilePath(src, 0);
			}
			for (int i = 0; i < upFile.length; i++) {
				client.changeDirectory(Parser.parse(ftpSite.getFtpFolder(), date));
				if (upFilePath == null) {// 单个文件
					// 判断文件是否正被外部程序打开
					if (this.bean.getLocked() == true) {
						if (srcFile.isFile()) {
							if (locked(srcFile, src) == false)
								return -1;
						}
					}
					// File uploadFile = new java.io.File(src);
					client.upload(srcFile);
					if (dFileName.length() > 0 && !srcFile.getName().equalsIgnoreCase(dFileName)) {
						try {
							client.deleteFile(dFileName);
						} catch (Exception e) {
							System.out.println(this.getTaskName() + "上传文件删除同名文件失败");
						}
						try {
							client.rename(srcFile.getName(), dFileName);
						} catch (Exception e) {

							System.out.println(this.getTaskName() + "上传文件重命名失败");
						}
					}
					// 在临时文件夹内创建一个ok文件
					if (this.bean.getIfCreateOkFile()) {
						String upOKfilepath = UtilFile.createOkFile(Const.TEMP_DIR + dFileName);
						File uploadOkFile = new java.io.File(upOKfilepath);
						client.upload(uploadOkFile);
						uploadOkFile.delete();
					}
					if (this.bean.getDelSrcFile()) {
						try {
							srcFile.delete();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					String tmpPath = upFilePath[i].substring(upFilePath[i].indexOf(dir) + dir.length());
					if (tmpPath.length() > 0) {
						if (tmpPath.substring(0, 1).equalsIgnoreCase("/")) {
							tmpPath = tmpPath.substring(1);
						}
						try {
							client.changeDirectory(tmpPath);
						} catch (FTPException e) {
							try {
								client.createDirectory(tmpPath);
								client.changeDirectory(tmpPath);
							} catch (Exception e1) {
								client.changeDirectory(tmpPath);
								System.out.println("FTP上传:" + e1.getMessage());
							}
						}

						File uploadFile = new java.io.File(upFile[i]);
						// 判断文件是否正被外部程序打开
						if (this.bean.getLocked() == true) {
							if (srcFile.isFile()) {
								if (locked(uploadFile, upFile[i]) == false)
									return -1;
							}
						}
						client.upload(uploadFile);
						if (dFileName.length() > 0 && !uploadFile.getName().equalsIgnoreCase(dFileName)) {
							try {
								client.deleteFile(dFileName);
							} catch (Exception e) {
								System.out.println(this.getTaskName() + "上传文件删除同名文件失败");
							}
							try {
								client.rename(uploadFile.getName(), dFileName);
							} catch (Exception e) {
								System.out.println(this.getTaskName() + "上传文件重命名失败");
							}
						}
						// 在临时文件夹内创建一个ok文件
						if (this.bean.getIfCreateOkFile()) {
							String upOKfilepath = UtilFile.createOkFile(Const.TEMP_DIR + uploadFile.getName());
							System.out.println(upOKfilepath);
							File uploadOkFile = new java.io.File(upOKfilepath);
							client.upload(uploadOkFile);
							uploadOkFile.delete();
						}
						if (this.bean.getDelSrcFile()) {
							try {
								uploadFile.delete();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						File uploadFile = new java.io.File(upFile[i]);
						// 判断文件是否正被外部程序打开
						if (this.bean.getLocked() == true) {
							if (srcFile.isFile()) {
								if (locked(uploadFile, upFile[i]) == false)
									return -1;
							}
						}
						client.upload(uploadFile);
						if (dFileName.length() > 0 && !uploadFile.getName().equalsIgnoreCase(dFileName)) {
							try {
								client.deleteFile(dFileName);
							} catch (Exception e) {
								System.out.println(this.getTaskName() + "上传文件删除同名文件失败");
							}
							try {
								client.rename(uploadFile.getName(), dFileName);
							} catch (Exception e) {

								System.out.println(this.getTaskName() + "上传文件重命名失败");
							}
						}
						// 在临时文件夹内创建一个ok文件
						if (this.bean.getIfCreateOkFile()) {
							String upOKfilepath = UtilFile.createOkFile(Const.TEMP_DIR + uploadFile.getName());
							File uploadOkFile = new java.io.File(upOKfilepath);
							client.upload(uploadOkFile);
							uploadOkFile.delete();
						}
						if (this.bean.getDelSrcFile()) {
							try {
								uploadFile.delete();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			client.disconnect(true);
			this.setTaskStatus("执行成功");
			String plugin = this.bean.getEnablePlugin() ? this.bean.getPluginName() : "";
			String execresult = Parser.removeSlash(sFilePath + sFileName + "\n" + "到  : " + dFilePath + dFileName) + "\n";

			if (plugin.length() > 1)
				execresult = execresult + "使用插件:  " + plugin + "\n";
			this.setTaskMsg(execresult);
		} catch (IllegalStateException e) {
			rs = 10001;
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP上传错误1: ", e);

		} catch (IOException e) {
			rs = 10002;
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP上传错误2: ", e);

		} catch (FTPIllegalReplyException e) {
			rs = 10003;
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP上传错误3: ", e);

		} catch (FTPException e) {
			rs = 10004;
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP上传错误4: ", e);

		} catch (FTPDataTransferException e) {
			rs = 10005;
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP上传错误5: ", e);

		} catch (FTPAbortedException e) {
			rs = 10006;
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP上传错误6: ", e);

		} catch (Exception e) {
			rs = 10007;
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP上传错误7: ", e);

		}
		return rs;
	}

	// ftp下载
	protected int ftpDownLoad(FtpSite ftpSite, String sFilePath, String sFileName, String dFilePath, String dFileName) {
		int rs = 0;
		sFilePath = Parser.parse(sFilePath, date);
		sFileName = Parser.parse(sFileName, date);
		dFilePath = Parser.parse(dFilePath, date);
		dFileName = Parser.parse(dFileName, date);

		File filedest = new File(dFilePath + dFileName);
		String dpath = filedest.getParent();

		if (ftpSite == null) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误: " + "FTP站点模板解析错误!");
			return -1;
		}
		FTPClient client = new FTPClient();
		try {
			UtilCrypt crypt = UtilCrypt.getInstance();
			client.connect(ftpSite.getFtpIp(), Integer.parseInt(ftpSite.getFtpPort()));
			// crypt.decryptAES(ftpSite.getFtpPassword(), CryptUtil.key);
			if (!ftpSite.getFtpUser().equals("anonymous"))
				client.login(ftpSite.getFtpUser(), crypt.decryptAES(ftpSite.getFtpPassword(), UtilCrypt.key));
			else
				client.login("anonymous", "no@password.com");

			// 如果缺省该句 传输txt正常 但图片和其他格式的文件传输出现乱码
			client.setType(FTPClient.TYPE_BINARY);

			if (sFileName.length() > 1 && dFileName.length() > 1) {
				File dfile = new File(dpath);
				if (!dfile.exists())
					dfile.mkdirs();

				if (filedest.exists()) {
					if (!this.bean.getOverwrite()) {
						this.setTaskStatus("执行成功");
						this.setTaskMsg("文件已存在，任务忽略");
						if (this.bean.getIfCreateOkFile()) {
							UtilFile.createOkFile(dFilePath + dFileName);
						}
						return -1;
					}
				}
				// System.out.println(Parser.parse(ftpSite.getFtpFolder(),
				// date));
				client.changeDirectory(Parser.parse(ftpSite.getFtpFolder(), date));
				client.download(sFileName, new java.io.File(dFilePath + dFileName), this);
				client.disconnect(true);
				if (this.bean.getIfCreateOkFile()) {
					UtilFile.createOkFile(dFilePath + dFileName);
				}
				this.setTaskStatus("执行成功");
				String plugin = this.bean.getEnablePlugin() ? this.bean.getPluginName() : "";
				String execresult = Parser.removeSlash(sFilePath + sFileName + "\n" + "到  : " + dFilePath + dFileName) + "\n";

				if (plugin.length() > 1)
					execresult = execresult + "使用插件:  " + plugin + "\n";
				this.setTaskMsg(execresult);
			} else {
				String destPath = dFilePath;
				folder.clear();
				// System.out.println(Parser.parse(ftpSite.getFtpFolder(),
				// date));
				client.changeDirectory(Parser.parse(ftpSite.getFtpFolder(), date));
				getAllFtpFolder(Parser.parse(ftpSite.getFtpFolder(), date), destPath, client);
				folder.add(Parser.parse(ftpSite.getFtpFolder(), date));

				String ftpdir = "ftp://" + ftpSite.getFtpName() + "";
				// 获取FTP自动配置后面的手工添加目录
				String ftp = sFilePath.replace(ftpdir, "");
				for (int j = 0; j < folder.size(); j++) {
					client.changeDirectory(folder.get(j));
					FTPFile[] file = client.list();
					for (int i = 0; i < file.length; i++) {
						if (file[i].getType() == 1)
							continue;
						// 截掉手工添加的目录
						String nowfoler = "/" + folder.get(j).substring(folder.get(j).indexOf(ftp) + ftp.length());
						String destPath1 = destPath + nowfoler;// folder.get(j);
						File filefoder = new File(destPath1);
						if (!filefoder.exists())
							filefoder.mkdirs();
						if (destPath1.endsWith("/") == false && destPath1.endsWith("/") == false) {
							destPath1 = destPath1 + "/";
						}
						filedest = new java.io.File(destPath1 + file[i].getName());
						if (filedest.exists()) {
							if (!this.bean.getOverwrite()) {
								// this.setTaskStatus("执行成功");
								// this.setTaskFailMsg("文件已存在，任务忽略");
								if (this.bean.getIfCreateOkFile()) {
									UtilFile.createOkFile(destPath1 + file[i].getName());
								}
								continue;
							}
						}
						client.download(file[i].getName(), filedest);
						if (this.bean.getIfCreateOkFile()) {
							UtilFile.createOkFile(destPath1 + file[i].getName());
						}
					}
				}
				client.disconnect(true);
				this.setTaskStatus("执行成功");
				String plugin = this.bean.getEnablePlugin() ? this.bean.getPluginName() : "";
				String execresult = Parser.removeSlash(sFilePath + sFileName + "\n" + "到  : " + dFilePath + dFileName) + "\n";

				if (plugin.length() > 1)
					execresult = execresult + "使用插件:  " + plugin + "\n";
				this.setTaskMsg(execresult);

			}
		} catch (IllegalStateException e) {
			rs = 10001;
			filedest.delete();
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误1: ", e);

		} catch (IOException e) {
			rs = 10002;
			filedest.delete();
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误2: ", e);

		} catch (FTPIllegalReplyException e) {
			rs = 10003;
			filedest.delete();
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误3: ", e);

		} catch (FTPException e) {
			rs = 10004;
			filedest.delete();
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误4: ", e);

		} catch (FTPDataTransferException e) {
			rs = 10005;
			filedest.delete();
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误5: ", e);

		} catch (FTPAbortedException e) {
			rs = 10006;
			filedest.delete();
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误6: ", e);

		} catch (FTPListParseException e) {
			rs = 10007;
			filedest.delete();
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误7: ", e);

		} catch (Exception e) {
			rs = 10008;
			filedest.delete();
			this.setTaskStatus("执行失败");
			this.setTaskMsg("FTP下载错误8: ", e);

		}
		return rs;
	}

	// 获取ftp根目录下所有目录
	protected void getAllFtpFolder(String ftpfolder, String localfile, FTPClient client) {
		FTPFile[] file;
		try {
			// System.out.println(client.currentDirectory());
			file = client.list();
			for (int i = 0; i < file.length; i++) {
				if (file[i].getType() == 1) {
					// File lfile = new File(localfile + file[i].getName() +
					// "/");
					/*
					 * System.out.println(lfile.getAbsolutePath()); if
					 * (!lfile.exists()) { lfile.mkdir(); }
					 */
					/**
					 * 中证指数FTP下载时，在此进入死循环 控制,只支持本目录下载
					 */
					if (file[i].getName().trim().equals("."))
						break;
					// ************************************

					folder.add(ftpfolder + file[i].getName() + "/");
					client.changeDirectory(ftpfolder + file[i].getName() + "/");
					getAllFtpFolder(ftpfolder + file[i].getName() + "/", localfile + file[i].getName() + "/", client);
				} /*
					 * else if (file[i].getType()==0){
					 * 
					 * ftpDownLoadFile(file[i].getName(),localfile+"/"+file[i].getName(),client);
					 * System.out.println(file[i].getName());
					 * //System.out.println(localfile); }
					 */
			}

		} catch (IllegalStateException e) {

		} catch (IOException e) {

		} catch (FTPIllegalReplyException e) {

		} catch (FTPException e) {

		} catch (FTPDataTransferException e) {

		} catch (FTPAbortedException e) {

		} catch (FTPListParseException e) {

		}

	}

	public void started() {
		// Transfer started
	}

	public void transferred(int length) {
		// Yet other length bytes has been transferred since the last time this
		// method was called
	}

	public void completed() {
		// Transfer completed
	}

	public void aborted() {
		// Transfer aborted
	}

	public void failed() {
		// Transfer failed
	}

}
