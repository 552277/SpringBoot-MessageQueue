package com.example.SpringBootActiveMQ.firstTopic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 *
 * 本类演示在fourthDeviceId客户端上的sixthConsumerId和sevenConsumerId用户订阅非持久消息
 *
 */
public class SourceSubscriber {
    private static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin",
            "admin", "tcp://10.242.62.239:61616");
    public static void main(String[] args) {

        // Connection ：JMS 客户端到JMS Provider 的连接
        try {
            final Connection connection = connectionFactory.createConnection();
            connection.setClientID("fourthDeviceId"); //持久订阅需要设置这个。
            connection.start();
            int INDIVIDUAL_ACK_TYPE = 4;
            // Session： 一个发送或接收消息的线程
            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // Destination ：消息的目的地;消息送谁那获取.
//            Topic topic = session.createTopic("STOCKS.myTopic");// 创建topic
            Topic topic = session.createTopic("first_topic");// 创建topic

            // 消费者，消息接收者
            //    MessageConsumer consumer1 = session.createConsumer(destination); 普通订阅
            // 第2个消费者，订阅持久化消息
            MessageConsumer secondConsumer = session.createDurableSubscriber(topic,"sixthConsumerId");//持久化订阅
            secondConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {

                    try {

                        TextMessage message = (TextMessage) msg;
                        System.out.println("secondConsumer 收到消息： " + message.getText());                        //    JmsUtils.commitIfNecessary(session);
                        session.commit();

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });

            // 第3个消费者，订阅持久化消息
            MessageConsumer thirdConsumer = session.createDurableSubscriber(topic,"sevenConsumerId");//持久化订阅
            secondConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {

                    try {

                        TextMessage message = (TextMessage) msg;
                        System.out.println("thirdConsumer 收到消息： " + message.getText());                        //    JmsUtils.commitIfNecessary(session);
                        session.commit();


                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });

                /*// 再来一个消费者，消息接收者
                MessageConsumer consumer2 = session.createConsumer(destination);

                consumer2.setMessageListener(new MessageListener() {
                    @Override
                    public void onMessage(Message msg) {

                        try {

                            TextMessage message = (TextMessage) msg;
                            System.out.println("BBBBBBBBB收到消息： " + message.getText());
                            JmsUtils.commitIfNecessary(session);
                          //  session.commit();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }

                    }
                });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}