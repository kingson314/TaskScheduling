package com.settings;

//配置信息Bean
public class Settings {
	// 标志
	private int id;
	// 配置信息名称
	private String setName;
	// 配置信息值
	private String setValue;
	// 配置信息说明
	private String setMemo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getSetValue() {
		return setValue;
	}

	public void setSetValue(String setValue) {
		this.setValue = setValue;
	}

	public String getSetMemo() {
		return setMemo;
	}

	public void setSetMemo(String setMemo) {
		this.setMemo = setMemo;
	}
}
