package email.demo.rrr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by ZhangPY on 2018年6月4日.
 * OVERUN-9299
 */

@Repository
public class MailUtil {
	
	@Value("${mail.myEmailAccount}")
	private String myEmailAccount;
	
	@Value("${mail.myEmailPassword}")
	private String myEmailPassword;

	@Value("${mail.myEmailSMTPHost}")
	private String myEmailSMTPHost;
	
	@Value("${mail.regex}")
	private String regex;
	
	
	public void sendMails(String mailAddress,String title,String body) throws Exception{
		
		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        
        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        session.setDebug(false);                                 // 设置为debug模式, 可以查看详细的发送 log












        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, mailAddress,title,body);
        
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        
        // 5. PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
        transport.connect(myEmailAccount, myEmailPassword);
        
        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        
        // 7. 关闭连接
        transport.close();
	}


	private MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail,String title,String body) throws Exception{
		// 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, "测试邮件", "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "soap9299发来贺电", "UTF-8"));

        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(title, "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent(body, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();
        
        return message;
	}


	//检查邮箱格式
	public boolean cheakMail(String address) {
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(address);
		return matcher.matches();
	}

}
