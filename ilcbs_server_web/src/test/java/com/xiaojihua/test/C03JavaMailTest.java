package com.xiaojihua.test;

import com.sun.mail.util.MailSSLSocketFactory;
import com.sun.net.ssl.internal.ssl.Provider;
import com.xiaojihua.utils.MailUtil;
import org.junit.Test;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.Security;
import java.util.Properties;

/**
 * 使用javamail来发送普通邮件和带有附件和图片内嵌的邮件
 * 使用的是QQ邮箱
 */
public class C03JavaMailTest {

    /**
     * 普通的邮件发送，走得SMTP 25端口
     *
     * DEBUG SMTP: trying to connect to host "smtp.qq.com", port 25, isSSL false
     * 未加密未走SSL
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        //1、初始化相关参数
        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.qq.com");
        props.put("mail.smtp.auth","true");
        props.put("mail.transport.protocol","SMTP");

        //2、创建验证器，注意此处密码为邮箱授权码
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //设置发送人的帐号和密码
                return new PasswordAuthentication("240971505@qq.com","fvycxesvnrehbjdc");
            }
        };

        //3、使用参数和验证器初始化邮件会话
        Session session = Session.getDefaultInstance(props,auth);
        session.setDebug(true);

        //4、建立邮件对象
        MimeMessage message = new MimeMessage(session);
        //4.1 设置发件人
        InternetAddress address = new InternetAddress("240971505@qq.com");
        message.setFrom(address);
        //4.2 设置收件人
        InternetAddress addressTo = new InternetAddress("2542213767@qq.com");
        message.setRecipient(Message.RecipientType.TO,addressTo);
        //4.3 设置主题
        message.setSubject("使用普通javamailAPI发送的简单文本");
        //4.4 设置内容，setText只能设置普通的文本 MIME type of "text/plain"
        message.setText("使用普通javamailAPI发送的简单文本");

        //5、发送邮件
        Transport.send(message);
    }

    /**
     * 使用465端口以及SSL协议发送邮件（QQ邮箱）
     * 发送带有图片以及附件的邮件
     * @throws Exception
     */
    @Test
    public void test2() throws Exception{

        //1、设置SSL Factory
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        //2、设置初始化属性
        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.qq.com");
        props.put("mail.smtp.ssl.enable", "true");//开启SSL
        props.put("mail.smtp.ssl.socketFactory", sf);//指定sf
        //props.put("mail.smtp.port","465");//不加这个端口号也能发送
        props.put("mail.smtp.auth","true");
        //3、设置认证数据
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //设置发送人的帐号和密码
                return new PasswordAuthentication("240971505@qq.com","fvycxesvnrehbjdc");
            }
        };
        //4、创建邮件会话
        Session session = Session.getDefaultInstance(props,auth);
        //session.setDebug(true);
        //5、创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //5.1 设置发件人，收件人以及主题
        message.setFrom(new InternetAddress("240971505@qq.com"));
        message.setRecipients(Message.RecipientType.TO,"2542213767@qq.com");
        message.setSubject("图片加附件的邮件");
        //5.2 设置一个文本MIME body
        MimeBodyPart textPart = new MimeBodyPart();
        //设置内容为一个img标签，其中src为"cid:myimg"，cid：为固定写法，后面跟着的是另个MimeBodyPart设置的ContentId
        textPart.setContent("图<img src='cid:myimg'/>文加附件邮件测试", "text/html;charset=UTF-8");
        //设置一个数据part,通过DataHandler来读取数据，并设置CID
        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.setDataHandler(new DataHandler(new FileDataSource("I:\\mailTest.jpg")));
        imagePart.setContentID("myimg");
        //整合文字和图片，如果内类似这种内嵌方式的话，subtype（MIME类型）必须设置为realted
        MimeMultipart mmp1 = new MimeMultipart();
        mmp1.addBodyPart(textPart);
        mmp1.addBodyPart(imagePart);
        mmp1.setSubType("related");
        //将上面的MimeMultipart整合成一个MimeBodyPart
        MimeBodyPart textImagePart = new MimeBodyPart();
        textImagePart.setContent(mmp1);

        //5.3 设置附件
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("I:\\Spring 3.x中文名称.docx"));//文件路径
        String fileName = dh.getName();
        System.out.println("发送的附件名称：" + fileName);
        attachmentPart.setDataHandler(dh);
        System.out.println(MimeUtility.encodeText(fileName, "UTF-8", "B"));
        //如果附件名称带有中文必须使用如下语句进行UTF-8进行编码然后通过BASE64进行编码传输
        //第三个参数可以为B(BASE64),Q(ASCII)或者null(根据字符串内容自行选择)
        attachmentPart.setFileName((MimeUtility.encodeText(fileName, "UTF-8", "B")));

        //整合图文部分和附件部分形成一个新的MimeMultipart
        MimeMultipart mmp2 = new MimeMultipart();
        mmp2.addBodyPart(textImagePart);
        mmp2.addBodyPart(attachmentPart);
        //如果MimeMultipart含有附件那么MIME类型必须设置为mixed
        mmp2.setSubType("mixed");

        //6、将邮件对象添加到邮件内容中并确认
        message.setContent(mmp2);
        message.saveChanges();

        //7、发送邮件
        Transport.send(message);


    }

    @Test
    public void test3() throws Exception {
        MailUtil.sendMsg("2542213767@qq.com","test","tesss");
    }
}
