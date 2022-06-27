package cn.bos.monitor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private static final String host = PropertiesUtil.getProperty("sys.config.sendmail.host.address");
    private static final String hostPort = PropertiesUtil.getProperty("sys.config.sendmail.host.port");
    private static final String hostUser = PropertiesUtil.getProperty("sys.config.sendmail.host.user");
    private static final String hostPassword = PropertiesUtil.getProperty("sys.config.sendmail.host.password");
    static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    public static void send(String[] receivers, String subject, String content) throws MessagingException {
        Properties properties = System.getProperties();
        logger.info("SendMail parameters: host = " + host + ", port = " + hostPort + ", user = " + hostUser);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", hostPort);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.transport.protocol", "smtp");
        logger.debug("Parameters loaded: mail.smtp.host=" + host + ", mail.smtp.port=" + hostPort + ", hostUser=" + hostUser);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                // 设置发件人邮件帐号和密码
                return new PasswordAuthentication(hostUser, hostPassword);
            }
        });
        logger.debug("Session established.");
        MimeMessage message = new MimeMessage(session);

        // 设置发件人邮件地址
        message.setFrom(new InternetAddress(hostUser));

        // 设置收件人邮件地址
        for (String receiver : receivers) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
        }
        message.setSubject(subject);

        message.setText(content);

        Transport.send(message);
        logger.info("Alert email sent.");
    }

    public static void printParameters() {
        System.out.println("host = " + host + ", port = " + hostPort + ", user = " + hostUser + ", password = " + hostPassword);
    }

}
