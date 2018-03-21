package com.log.lately;

//新增最近执行日志(内存)
public class AddLogLately {

	private int index;
	private LogLately[] logLately;

	public AddLogLately(int length) {
		this.index = 0;
		this.logLately = new LogLately[length];
	}

	public void addLog(LogLately logLately) {
		if (this.index < this.logLately.length) {
			this.logLately[index] = logLately;
			this.index = this.index + 1;
		} else {
			this.logLately[0] = logLately;
			this.index = 1;
		}
	}

	public LogLately[] getLogLately() {
		return logLately;
	}

	public void setLogLately(LogLately[] logLately) {
		this.logLately = logLately;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
