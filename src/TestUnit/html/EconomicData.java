package TestUnit.html;

/**
 * @Description: 经济数据
 * @date Feb 8, 2014
 * @author:fgq
 */
public class EconomicData {
	// 发布日期YYYY-MM-DD
	private String publishDate;
	// 公布时间 HH:mm:ss
	private String publishTime;
	// 国家
	private String country;
	// 指标名称
	private String indicator;
	// 重要性
	private String importance;
	// 前值
	private String previousValue;
	// 预测值
	private String predictedValue;
	// 公布值
	private String publishedValue;

	// 0表示来自汇通网，1表示来自福汇网
	private int source;

	//福汇使用
	public EconomicData(int source) {
		this.source = source;
		this.country="";
	}
	//汇通使用
	public EconomicData() {
		this.source=0;
	}

	public String toString() {
		return this.publishDate + " " + this.publishTime + " " + this.country + " " + this.indicator + " " + this.importance + " " + this.previousValue + " " + this.predictedValue + " "
				+ this.publishedValue;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	public String getPredictedValue() {
		return predictedValue;
	}

	public void setPredictedValue(String predictedValue) {
		this.predictedValue = predictedValue;
	}

	public String getPublishedValue() {
		return publishedValue;
	}

	public void setPublishedValue(String publishedValue) {
		this.publishedValue = publishedValue;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

}
