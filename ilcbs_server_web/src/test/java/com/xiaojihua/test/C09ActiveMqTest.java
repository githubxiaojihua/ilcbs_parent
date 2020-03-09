package com.xiaojihua.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class C09ActiveMqTest {

    /**
     * 操作QUUEU。
     * 使用JMS原生API编写测试类，向消息中间件写入消息。
     * 消息生产者
     */
    @Test
    public void test1() throws JMSException {
        //1、建立工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory();
        //2、创建链接，并且开启链接
        Connection connection = factory.createConnection();
        connection.start();
        //3、建立会话，第一个参数是是否使用事物，如果true则需要session.commit()
        //第二个参数是设置消费者在获取消息后采用自动应答的方式通知中间件
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //4、在中间件中创建队列对象
        Queue queue = session.createQueue("xiaojihuaQueue");
        //5、创建一个生产者
        MessageProducer producer = session.createProducer(queue);
        //6、生产者发送消息
        producer.send(session.createTextMessage("hello,i am man!"));
        //7、提交和关闭链接
        session.commit();
        session.close();
        connection.close();
    }

    /**
     * 操作QUEUE
     * 使用JMS原生API编写消费者
     * 采用手工recive的方式进行同步消费
     */
    @Test
    public void test2() throws JMSException {
        //1、建立工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory();
        //2、创建链接，并且开启链接
        Connection connection = factory.createConnection();
        connection.start();
        //3、建立会话，第一个参数是是否使用事物，如果true则需要session.commit()
        //第二个参数是设置消费者在获取消息后采用自动应答的方式通知中间件
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、在中间件中创建队列对象
        Queue queue = session.createQueue("xiaojihuaQueue");
        //5、创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //6、消费消息
        while(true){
            //设置了超时时间，如果不设置则一直阻塞
            TextMessage receive = (TextMessage)consumer.receive(1000);
            if(receive != null){
                System.out.println(receive.getText());
            }else{
                break;
            }
        }
        //7、提交，关闭链接
        /**
         * 如果启用事物则这里必须commit否则中间件不知道消费者已经消费了，在中间件的控制台上
         * 不会有变化，而且消费者可以消费多次，因为中间件没有删除。
         * 如果上面createSession的时候设置了false则不需要
         */
        //session.commit();
        session.close();
        connection.close();
    }

    /**
     * 操作QUEUE
     * 使用JMS原生API编写消费者
     * 采用手工监听的方式进行同步消费
     * 使用监听机制的时候要注意，消费者必须启动在前，生产者必须启动在后
     * 否则收不到消息。
     */
    @Test
    public void test3() throws JMSException {
        //1、建立工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory();
        //2、创建链接，并且开启链接
        Connection connection = factory.createConnection();
        connection.start();
        //3、建立会话，第一个参数是是否使用事物，如果true则需要session.commit()
        //第二个参数是设置消费者在获取消息后采用自动应答的方式通知中间件
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、在中间件中创建队列对象
        Queue queue = session.createQueue("xiaojihuaQueue");
        //5、创建消费者
        MessageConsumer consumer = session.createConsumer(queue);

        //6、使用监听器机制对消息进行消费
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //当有message发送的中间件以后就会调用listener并将message传递过来
                TextMessage textMessage = (TextMessage)message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //为了保证监听器一直开着，所以做如下设置。
        while(true);
    }
}
