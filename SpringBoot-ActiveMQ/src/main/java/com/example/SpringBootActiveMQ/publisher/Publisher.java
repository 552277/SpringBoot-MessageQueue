package com.example.SpringBootActiveMQ.publisher;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Publisher {
    public static void main(String[] args) throws JMSException, InterruptedException {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        //61616是ActiveMQ默认端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://10.242.62.239:61616");

        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection =  connectionFactory.createConnection();
        connection.start();
        // Session： 一个发送或接收消息的线程
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  //自动确认

        // Destination ：消息的目的地;消息发送给谁.
        //Destination destination =  session.createQueue("my-queue");
        Destination destination = session.createTopic("first_topic"); //创建topic   myTopic
        // MessageProducer：消息发送者
        MessageProducer producer =  session.createProducer(destination);

        // 设置不持久化，可以更改
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for(int i=0;i<3;i++){
            //创建文本消息
            TextMessage message = session.createTextMessage("j"+i);
            //发送消息
            System.out.println("first_topic 发送消息成功："+i);
            producer.send(message);
        }

        // 创建second_topic消息
        Destination secondDestination = session.createTopic("second_topic"); //创建topic   myTopic
        // MessageProducer：消息发送者
        MessageProducer secondProducer =  session.createProducer(secondDestination);

        // 设置不持久化，可以更改
        secondProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for(int i=0;i<3;i++){
            //创建文本消息
            TextMessage message = session.createTextMessage("second_topic："+i);
            //发送消息
            System.out.println("second_topic 发送消息成功："+i);
            secondProducer.send(message);
        }

        session.close();
        connection.close();
    }
}