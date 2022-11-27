package com.yahelei.service.autotest.impl;

//import org.jeecg.common.util.SpringContextUtils;
//import org.jeecg.config.StaticConfig;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailUtil {
    public static void sendmail(String sendAddress,String text,String title,String attach,String filename) throws MessagingException {
//        String host = SpringContextUtils.getBean(StaticConfig.class).getHost();
//        String username = SpringContextUtils.getBean(StaticConfig.class).getUsername();
//        String password = SpringContextUtils.getBean(StaticConfig.class).getPassword();
        String host = "";
        String username = "";
        String password = "";
        System.out.println("最终的文案"+text);
        //创建一个配置文件并保存
        Properties properties = new Properties();
        properties.setProperty("mail.host",host);
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.smtp.socketFactory.port","465");
        properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth","true");
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
        MimeMessage message = new MimeMessage(session);

        // Set From: 头部头字段
        message.setFrom(new InternetAddress(username));

        // Set To: 头部头字段
//        message.addRecipients(Message.RecipientType.TO, new InternetAddress(sendAddress));
        String x[]=sendAddress.split(",");
        InternetAddress[] tos=new InternetAddress[x.length];
        for(int i=0;i<x.length;i++)
            tos[i]=new InternetAddress(x[i]);
        //邮件接收人
        message.setRecipients(Message.RecipientType.TO,tos);
        // Set Subject: 主题文字
        message.setSubject(title);
//        message.setContent(text,"text/html;charset=UTF-8");
        // 创建消息部分
        BodyPart messageBodyPart = new MimeBodyPart();

        // 消息
        messageBodyPart.setContent(text,"text/html;charset=UTF-8");

        // 创建多重消息
        Multipart multipart = new MimeMultipart();
        // 设置文本消息部分
        multipart.addBodyPart(messageBodyPart);

        // 附件部分
        messageBodyPart = new MimeBodyPart();
        //设置要发送附件的文件路径
        if(attach!=null&&attach.length()>0) {
            DataSource source = new FileDataSource(attach);
            messageBodyPart.setDataHandler(new DataHandler(source));
            //处理附件名称中文（附带文件路径）乱码问题
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

        }
        // 发送完整消息
        message.setContent(multipart);

        //   发送消息
        Transport.send(message);
        System.out.println("Sent message successfully....");
    }

    public static void sendmail(String sendAddress,String text,String title) throws MessagingException {
        sendmail(sendAddress,text,title,"","");
    }
}