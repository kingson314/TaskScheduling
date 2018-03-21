package com.monitor;

//监控员Bean
public class Monitor {
	// 监控员标志
	private String mCode;
	// 监控员名称
	private String mName;
	// 监控员电话
	private String mTel;
	// 监控员邮箱地址
	private String mMailAddress;

	public String getMCode() {
		return mCode;
	}

	public void setMCode(String code) {
		mCode = code;
	}

	public String getMName() {
		return mName;
	}

	public void setMName(String name) {
		mName = name;
	}

	public String getMTel() {
		return mTel;
	}

	public void setMTel(String tel) {
		mTel = tel;
	}

	public String getMMailAddress() {
		return mMailAddress;
	}

	public void setMMailAddress(String mailAddress) {
		mMailAddress = mailAddress;
	}

}
