package com.taskBusiBean.base;

import java.util.Date;
public abstract class Super implements Cloneable{
	private Long id;
	protected String createDate;
	protected Date createTime;
	protected Date modifyTime;
	protected Long createUserId;
	protected Long modifyUserId;
	 
	public Object clone() {   
        try {   
            return super.clone();   
        } catch (CloneNotSupportedException e) {   
            return null;   
        }   
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}   
	 
}
