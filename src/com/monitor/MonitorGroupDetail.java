package com.monitor;

//监控组监控员明细Bean
public class MonitorGroupDetail {
	// 监控组标志
	private String mgCode;
	// 监控员标志
	private String mCode;

	public String getMgCode() {
		return mgCode;
	}

	public void setMgCode(String mgCode) {
		this.mgCode = mgCode;
	}

	public String getMCode() {
		return mCode;
	}

	public void setMCode(String code) {
		mCode = code;
	}
}
