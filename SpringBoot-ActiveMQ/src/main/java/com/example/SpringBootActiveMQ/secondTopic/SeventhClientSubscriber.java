package com.example.SpringBootActiveMQ.secondTopic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.support.JmsUtils;

import javax.jms.*;

/**
 *
 * 本类演示在thirdDeviceId客户端上的secondConsumerId用户订阅持久消息功能
 * fiveConsumerId 非持久订阅
 */
public class SeventhClientSubscriber {
    private static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin",
            "admin", "tcp://10.242.62.239:61616");
    public static void main(String[] args) {

        // Connection ：JMS 客户端到JMS Provider 的连接
        try {
            final Connection connection = connectionFactory.createConnection();
            connection.setClientID("seventhDeviceId"); //持久订阅需要设置这个。
            connection.start();
            int INDIVIDUAL_ACK_TYPE = 4;
            // Session： 一个发送或接收消息的线程
            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // Destination ：消息的目的地;消息送谁那获取.
            Topic topic = session.createTopic("second_topic");// 创建topic

            // 消费者，消息接收者
            //    MessageConsumer consumer1 = session.createConsumer(destination); 普通订阅
            // 第4个消费者，订阅持久化消息
            MessageConsumer fourthConsumer = session.createDurableSubscriber(topic,"fourthConsumerId");//持久化订阅
            fourthConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {

                    try {

                        TextMessage message = (TextMessage) msg;
                        System.out.println("第七个客户端上的持久订阅 fourthConsumerId 收到second_topic消息： " + message.getText());
                        session.commit();

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });

            // 第5个消费者，订阅持久化消息
            MessageConsumer fiveConsumer = session.createConsumer(topic, null, true); // 普通订阅
            fiveConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {

                    try {

                        TextMessage message = (TextMessage) msg;
                        System.out.println("第七个客户端上的非持久订阅，fiveConsumerId 收到second_topic消息： " + message.getText());
                        JmsUtils.commitIfNecessary(session);
                        //  session.commit();
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