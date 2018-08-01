package com.example.SpringBootActiveMQ.service;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * @author zhongweichang
 * @email 15090552277@163.com
 * @date 2018/8/1 上午11:00
 */
@Service
public class PushService {




    public boolean publishMessage(String topic, String message) throws JMSException, InterruptedException{

        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        //61616是ActiveMQ默认端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "admin", "admin", "tcp://10.242.62.239:61616");

        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection =  connectionFactory.createConnection();
        connection.start();
        // Session： 一个发送或接收消息的线程
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  //自动确认

        // Destination ：消息的目的地;消息发送给谁.
        //Destination destination =  session.createQueue("my-queue");
        Destination destination = session.createTopic(topic); //创建topic   myTopic
        // MessageProducer：消息发送者
        MessageProducer producer =  session.createProducer(destination);

        // 设置不持久化，可以更改
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        //创建文本消息
        TextMessage textMessage = session.createTextMessage(message);
        //发送消息
        System.out.println("first_topic 发送消息成功：" + message);
        producer.send(textMessage);

        session.close();
        connection.close();
        return true;
    }
}
