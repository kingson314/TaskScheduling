package com.monitor;

//监控组Bean
public class MonitorGroup {
	// 监控组标志
	private String mgCode;
	// 监控组名称
	private String mgName;
	// 监控组说明
	private String mgMemo;

	public String getMgCode() {
		return mgCode;
	}

	public void setMgCode(String mgCode) {
		this.mgCode = mgCode;
	}

	public String getMgName() {
		return mgName;
	}

	public void setMgName(String mgName) {
		this.mgName = mgName;
	}

	public String getMgMemo() {
		return mgMemo;
	}

	public void setMgMemo(String mgMemo) {
		this.mgMemo = mgMemo;
	}
}
