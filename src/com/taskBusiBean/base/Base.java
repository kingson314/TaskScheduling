package com.taskBusiBean.base;

public abstract class Base extends Super {
	protected String name;
	protected Long state;
	protected Long ord;
	protected String memo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Long getOrd() {
		return ord;
	}

	public void setOrd(Long ord) {
		this.ord = ord;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
