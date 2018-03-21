package com.task.Mail;

public class Bean {
	// 发件人邮箱地址
	private String address;
	// 收件人邮箱
	private String mail;
	// 发送主题
	private String subject;
	// 发送内容
	private String content;
	// 邮件附件名称
	private String mailFileName;
	// 邮件附件路径
	private String mailFilePath;

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMailFileName() {
		return this.mailFileName;
	}

	public void setMailFileName(String mailFileName) {
		this.mailFileName = mailFileName;
	}

	public String getMailFilePath() {
		return this.mailFilePath;
	}

	public void setMailFilePath(String mailFilePath) {
		this.mailFilePath = mailFilePath;
	}

}
