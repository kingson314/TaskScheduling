package com.task.Mail;

import module.mail.MailDao;
import module.mail.MailSend;
import module.mail.MailSender;

import com.taskInterface.TaskAbstract;

import common.util.file.UtilFile;
import common.util.json.UtilJson;

public class Task extends TaskAbstract {

	// 执行发邮件功能
	public void fireTask() {
		try {
			Bean m = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			MailSender mailsender = MailDao.getInstance().getMailSenderByAddress(m.getAddress());
			// 多封邮件发送
			String mail = m.getMail();
			if (mail.lastIndexOf(";") < 0)
				mail = mail + ";";
			String[] mailArray = mail.split(";");
			// System.out.println(mailArray.length);
			if (mailArray.length < 1) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("发送邮件执行出错:" + "接收邮件地址不能为空！");
			}
			for (int i = 0; i < mailArray.length; i++) {
				String[] files = null;
				if (m.getMailFileName().length() < 1) {
					files = UtilFile.getAllFilePath(m.getMailFilePath());
				} else {
					files = new String[] { m.getMailFilePath() + m.getMailFileName() };
				}
				// 发送邮件
				MailSend sendmail = new MailSend(mailsender, mailArray[i], files, m.getSubject(), m.getContent());
				this.setTaskStatus(sendmail.getStatus());
				this.setTaskMsg(sendmail.getFailReason());
			}
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("发送邮件执行出错:", e);
		} finally {
		}

	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}
}
