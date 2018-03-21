package TestUnit.commonTest;
 
import module.mail.MailSend;
import module.mail.MailSender;

import org.junit.Test;


 

public class SendMailTest {

	@Test
	public final void testSendMail() {
		MailSender mail=new MailSender();  
		mail.setMailPort("25"); 
		mail.setMailSendAdreess("fenggq@dev.com");
		mail.setMailSendPassword("fenggq");
		mail.setMailSendUserName("fenggq");
		mail.setMailServer("192.1.50.1"); 
		mail.setMailValidate("true");
		String[] files=new String[1];
		files[0]="D://SJSHQ.DBF"; 
		new MailSend(mail,"fenggq@dev.com",files,"测试发送邮件——fenggq","test");
	}

}
