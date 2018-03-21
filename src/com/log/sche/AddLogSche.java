package com.log.sche;


//增加内存调度日志
public class AddLogSche {
	private int index;
	private LogSche[] logSche;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public LogSche[] getLogSche() {
		return logSche;
	}

	public void setLogSche(LogSche[] logSche) {
		this.logSche = logSche;
	}

	public AddLogSche(int length) {
		this.index = 0;
		this.logSche = new LogSche[length];
	}

	public void addLog(LogSche vlogSche) {
		if (this.index < this.logSche.length) {
			this.logSche[index] = vlogSche;
			this.index = this.index + 1;
		} else {
			this.logSche[0] = vlogSche;
			this.index = 1;
		}
	}
}
