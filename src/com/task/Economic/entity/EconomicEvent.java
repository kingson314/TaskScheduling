package com.task.Economic.entity;

/**
 * @Description: 财经事件
 * @date Feb 8, 2014
 * @author:fgq
 */
public class EconomicEvent {
	// 发生日期YYYYMMDD
	private String occurDate;
	// 发生时间 HHmmss
	private String occurTime;
	// 国家
	private String country;
	// 地点
	private String site;
	// 重要性
	private String importance;
	// 事件
	private String event;

	private String id;
	 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getOccurDate() {
		return occurDate;
	}

	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}

	public String getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(String occurTime) {
		this.occurTime = occurTime;
	}
}
