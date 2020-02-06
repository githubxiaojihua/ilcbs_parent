package com.xiaojihua.test;

import com.sun.mail.util.MailSSLSocketFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

/**
 * 使用QQ邮箱测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-mail.xml")
public class C04SpirngJavaMailTest {

    @Autowired
    private SimpleMailMessage mailMessage;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private MailSSLSocketFactory sf;//用于ssl

    /**
     * 普通邮件发送
     */
    @Test
    public void test1(){
        mailMessage.setTo("2542213767@qq.com");
        mailMessage.setSubject("通过spring整合javamail实现邮件发送");
        mailMessage.setText("通过spring整合javamail实现邮件发送");

        //增加下面三句以及相应的xml配置可以实现使用ssl协议以及465端口发送邮件
        //在配置文件中无法注入ref类型的值，只能通过编程实现
        Properties javaMailProperties = mailSender.getJavaMailProperties();
        javaMailProperties.put("mail.smtp.ssl.enable", "true");
        javaMailProperties.put("mail.smtp.ssl.socketFactory", sf);

        mailSender.send(mailMessage);
    }

    /**
     * 测试发送内嵌图片以及附件的邮件
     * @throws Exception
     */
    @Test
    public void test2() throws Exception{
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

        helper.setFrom("240971505@qq.com");
        helper.setTo("2542213767@qq.com");
        helper.setSubject("springjavaMail测试内容中含有图片");
        helper.setText("<html><head></head><body><h1>hello!!spring image html mail</h1><a href=http://www.baidu.com>baidu</a><img src='cid:image' /></body></html>",true);
        helper.addInline("image", new FileSystemResource(new File("I:\\JAVApc.jpg")));

        //不用做额外的配置可以直接发送带有空格和中文的名称
        helper.addAttachment("Spring 3.x中文名称.docx", new FileSystemResource(new File("I:\\Spring 3.x中文名称.docx")));

        //增加下面三句以及相应的xml配置可以实现使用ssl协议以及465端口发送邮件
        //在配置文件中无法注入ref类型的值，只能通过编程实现
        Properties javaMailProperties = mailSender.getJavaMailProperties();
        javaMailProperties.put("mail.smtp.ssl.enable", "true");
        javaMailProperties.put("mail.smtp.ssl.socketFactory", sf);

        mailSender.send(mimeMessage);
    }
}
