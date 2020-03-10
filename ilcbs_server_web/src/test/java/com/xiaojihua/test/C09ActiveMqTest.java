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


    /**
     * 操作TOPIC
     * 使用JMS原生API编写测试类，向消息中间件写入消息。
     * 消息生产者
     *
     * 基于topic的生产者和消费者，需要消费者先启动
     * 然后再启动生产者，这样才能收到消息
     */
    @Test
    public void test4() throws JMSException {
        //1、创建工厂
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        //2、创建并开启链接
        Connection connection = factory.createConnection();
        connection.start();
        //3、创建session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4、创建topic
        Topic xiaojihuaTopic = session.createTopic("xiaojihuaTopic");
        //5、创建消息生产者
        MessageProducer producer = session.createProducer(xiaojihuaTopic);
        //6、发送10条消息
        for(int i=0; i<10; i++){
            //发送一个mapMassage
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("username","zhangsan");
            mapMessage.setString("password","123456");
            producer.send(mapMessage);
        }
        //7、关闭链接
        session.close();
        connection.close();
    }

    /**
     * 操作Topic
     * 手动recive
     */
    @Test
    public void test5() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic xiaojihuaTopic = session.createTopic("xiaojihuaTopic");
        MessageConsumer consumer = session.createConsumer(xiaojihuaTopic);
        MapMessage receive = (MapMessage)consumer.receive();
        System.out.println(receive.getString("username"));
        session.close();
        connection.close();
    }

    /**
     * 操作Topic
     * 使用监听器接收信息
     * 一条信息可以被多个消费者消费
     * @throws JMSException
     */
    @Test
    public void test6() throws JMSException{
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("itcast297_topic");
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message arg0) {
                // TODO Auto-generated method stub
                try {
                    MapMessage message = (MapMessage) arg0;
                    System.out.println("Listener1=====用户名是："+message.getString("username"));
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        while(true);
    }

}
