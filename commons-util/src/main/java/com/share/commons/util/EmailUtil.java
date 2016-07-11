package com.share.commons.util;

import java.util.Date;
import java.util.Properties;

import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailUtil {

	public static boolean sendMail(MailSender mailSender, MailMessage mail) {
		// TODO Auto-generated method stub
		try {
			SimpleMailMessage[] mailMessages = { (SimpleMailMessage) mail };
			mailSender.send(mailMessages);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 根据自定义条件，获取邮件服务
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public static MailSender getMailSender(String host, int port,
			String username, String password) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);

		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("prop", "true");
		properties.setProperty("mail.smtp.timeout", "3000");
		mailSender.setJavaMailProperties(properties);
		return mailSender;
	}

	/**
	 * 包装邮件体，to字段可以多个，以逗号分隔，如果from、to为空，则使用默认,from应为tong.liu方式，如果使用tong.liu@corp
	 * .elong.com会报错，to示例如下： tong.liu@corp.elong.com,Zhongyu.Wang@corp.elong.com
	 * 
	 * @param from
	 * @param to
	 * @param mailSubject
	 * @param mailBody
	 * @return
	 */
	public static MailMessage getMailMessage(String from, String to,
			String mailSubject, String mailBody) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(from);// 发送人邮箱

		if (StringUtil.isNotEmpty(to) && to.contains(",")) {
			mail.setTo(to.split(","));
		} else {
			mail.setTo(to);// 收件人邮箱
		}

		mail.setSubject(mailSubject);// 邮件主题
		mail.setSentDate(new Date());// 邮件发送时间
		mail.setText(mailBody);

		return mail;
	}

}
