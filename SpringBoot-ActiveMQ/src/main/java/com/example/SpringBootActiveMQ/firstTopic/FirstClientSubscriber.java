package com.example.SpringBootActiveMQ.firstTopic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

/**
 * 本类演示在firstDeviceId客户端上的firstConsumerId用户订阅持久消息功能
 */
public class FirstClientSubscriber {

    private static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin",
            "admin", "tcp://10.242.62.239:61616");
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(FirstClientSubscriber.class);

        // Connection ：JMS 客户端到JMS Provider 的连接
        try {
            final Connection connection = connectionFactory.createConnection();
            connection.setClientID("firstDeviceId"); //持久订阅需要设置这个。
            connection.start();

            // Session： 一个发送或接收消息的线程
            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 创建topic
            Topic topic = session.createTopic("first_topic");

            // 第1个消息者，订阅持久化消息
            MessageConsumer firstConsumer = session.createDurableSubscriber(topic,"firstConsumerId");//持久化订阅

            firstConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {

                    try {

                        TextMessage message = (TextMessage) msg;
//                        System.out.println("第一个客户端上的 firstConsumer 收到消息： " + message.getText());
                        logger.info("第一个客户端上的 firstConsumer 收到消息： " + message.getText());
                        session.commit();

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}