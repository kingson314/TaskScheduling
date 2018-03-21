package com.task.FileTransfer;

public class Bean {
	// 源文件类型
	private String srcFileType;
	// 源文件名称
	private String srcFileName;
	// 源文件路径
	private String srcFilePath;
	// 是否删除源文件
	private Boolean delSrcFile;
	// 目的文件类型
	private String destFileType;
	// 目的文件名称
	private String destFileName;
	// 目的文件路径
	private String destFilePath;
	// 是否启用插件
	private Boolean enablePlugin;
	// 插件命令
	private String consoleParam;
	// 插件名称
	private String pluginName;
	// 插件路径
	private String pluginPath;
	// 是否覆盖
	private Boolean overwrite;
	// 是否锁定
	private Boolean locked;
	// 是否创建okFile
	private Boolean IfCreateOkFile;

	public String getSrcFileType() {
		return this.srcFileType;
	}

	public void setSrcFileType(String srcFileType) {
		this.srcFileType = srcFileType;
	}

	public String getSrcFileName() {
		return this.srcFileName;
	}

	public void setSrcFileName(String srcFileName) {
		this.srcFileName = srcFileName;
	}

	public String getSrcFilePath() {
		return this.srcFilePath;
	}

	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	public Boolean getDelSrcFile() {
		return this.delSrcFile;
	}

	public void setDelSrcFile(Boolean delSrcFile) {
		this.delSrcFile = delSrcFile;
	}

	public String getDestFileType() {
		return this.destFileType;
	}

	public void setDestFileType(String destFileType) {
		this.destFileType = destFileType;
	}

	public String getDestFileName() {
		return this.destFileName;
	}

	public void setDestFileName(String destFileName) {
		this.destFileName = destFileName;
	}

	public String getDestFilePath() {
		return this.destFilePath;
	}

	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}

	public Boolean getEnablePlugin() {
		return this.enablePlugin;
	}

	public void setEnablePlugin(Boolean enablePlugin) {
		this.enablePlugin = enablePlugin;
	}

	public String getConsoleParam() {
		return this.consoleParam;
	}

	public void setConsoleParam(String consoleParm) {
		this.consoleParam = consoleParm;
	}

	public String getPluginName() {
		return this.pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginPath() {
		return this.pluginPath;
	}

	public void setPluginPath(String pluginPath) {
		this.pluginPath = pluginPath;
	}

	public Boolean getOverwrite() {
		return overwrite;
	}

	public void setOverwrite(Boolean overwrite) {
		this.overwrite = overwrite;
	}

	public Boolean getLocked() {
		return this.locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getIfCreateOkFile() {
		return IfCreateOkFile;
	}

	public void setIfCreateOkFile(Boolean ifCreateOkFile) {
		IfCreateOkFile = ifCreateOkFile;
	}

}
