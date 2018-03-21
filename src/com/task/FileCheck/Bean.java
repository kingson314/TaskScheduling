package com.task.FileCheck;

public class Bean {
	// 文件目录
	private String fileCheckDir;
	// 文件名称
	private String fileCheckName;
	// 文件存在警告信息
	private String fileCheckExistWarning;
	// 文件不存在警告信息
	private String fileCheckNotExistWarning;
	// 是否启用文件存在警告
	private Boolean ifFileCheckExistWarning;
	// 是否启用文件不存在警告
	private Boolean ifFileCheckNotExistWarning;

	public String getFileCheckDir() {
		return fileCheckDir;
	}

	public void setFileCheckDir(String fileCheckDir) {
		this.fileCheckDir = fileCheckDir;
	}

	public String getFileCheckName() {
		return fileCheckName;
	}

	public void setFileCheckName(String fileCheckName) {
		this.fileCheckName = fileCheckName;
	}

	public String getFileCheckExistWarning() {
		return fileCheckExistWarning;
	}

	public void setFileCheckExistWarning(String fileCheckExistWarning) {
		this.fileCheckExistWarning = fileCheckExistWarning;
	}

	public String getFileCheckNotExistWarning() {
		return fileCheckNotExistWarning;
	}

	public void setFileCheckNotExistWarning(String fileCheckNotExistWarning) {
		this.fileCheckNotExistWarning = fileCheckNotExistWarning;
	}

	public Boolean getIfFileCheckExistWarning() {
		return ifFileCheckExistWarning;
	}

	public void setIfFileCheckExistWarning(Boolean ifFileCheckExistWarning) {
		this.ifFileCheckExistWarning = ifFileCheckExistWarning;
	}

	public Boolean getIfFileCheckNotExistWarning() {
		return ifFileCheckNotExistWarning;
	}

	public void setIfFileCheckNotExistWarning(Boolean ifFileCheckNotExistWarning) {
		this.ifFileCheckNotExistWarning = ifFileCheckNotExistWarning;
	}

}
