package com.xiaojihua.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-mq.xml")
public class C10SpringActiveMqTest {

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate queueTemplate;

    @Autowired
    @Qualifier(value="jmsTopicTemplate")
    private JmsTemplate topicTemplate;

    /**
     * 使用spring jmsTemplate向中间件发送
     * queue请求
     */
    @Test
    public void test1(){
        queueTemplate.send("springQueue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage("spring发送的消息");
                return message;
            }
        });
    }

    @Test
    public void testQueueReceive() throws JMSException{
        TextMessage message = (TextMessage) queueTemplate.receive("spirngQueue");
        System.out.println(message.getText());
    }

    /**
     * 测试Topic
     * 注意启动顺序
     */
    @Test
    public void testTopicSend(){
        topicTemplate.send("spring_topic", new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                // TODO Auto-generated method stub
                //TextMessage message = session.createTextMessage("spring发送的消息");
                MapMessage map = session.createMapMessage();
                map.setString("username", "cgx");
                return map;
            }
        });
    }

    /**
     * 手动调用的方式
     * @throws JMSException
     */
    @Test
    public void testTopicReceive() throws JMSException{
        MapMessage message = (MapMessage) topicTemplate.receive("spring_topic");
        System.out.println(message.getString("username"));
        System.out.println("执行结束");
    }
}
