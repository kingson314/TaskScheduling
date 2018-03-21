package com.task.TableUpdateMultiple;

import java.util.List;

public class Bean {
	// 源数据库连接
	private String srcDbName;
	// 目的数据库连接
	private String dstDbName;

	// 明细参数列表
	private List<BeanDetail> listBeanDetail;

	public String getSrcDbName() {
		return this.srcDbName;
	}

	public void setSrcDbName(String srcDbName) {
		this.srcDbName = srcDbName;
	}

	public String getDstDbName() {
		return this.dstDbName;
	}

	public void setDstDbName(String dstDbName) {
		this.dstDbName = dstDbName;
	}

	public List<BeanDetail> getListBeanDetail() {
		return listBeanDetail;
	}

	public void setListBeanDetail(List<BeanDetail> listBeanDetail) {
		this.listBeanDetail = listBeanDetail;
	}

}
