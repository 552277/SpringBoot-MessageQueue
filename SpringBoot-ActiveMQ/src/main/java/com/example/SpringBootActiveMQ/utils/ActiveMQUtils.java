package com.example.SpringBootActiveMQ.utils;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;

/**
 * @author zhongweichang
 * @email 15090552277@163.com
 * @date 2018/8/1 下午1:35
 */
public class ActiveMQUtils {

    private static ConnectionFactory connectionFactory;

    private static String userName;

    private static String password;

    private static String brokerUrl;

    private static Connection connection;

    private static Session session;

    private static Logger logger = LoggerFactory.getLogger(ActiveMQUtils.class);

    static {

        userName = "admin";

        password = "admin";

        brokerUrl = "tcp://10.242.62.239:61616";

        ActiveMQUtils.init(userName, password, brokerUrl);

    }

    public static void init(String userName, String password, String brokerUrl) {
        try {
            // ConnectionFactory ：连接工厂，JMS 用它创建连接
            //61616是ActiveMQ默认端口
            connectionFactory = new ActiveMQConnectionFactory(
                    userName, password, brokerUrl);
            connection =  connectionFactory.createConnection();
            // Connection ：JMS 客户端到JMS Provider 的连接
            connection.start();
        }catch (JMSException e) {
            logger.error("active 创建连接失败", e);
        }

    }


    /**
     * 推送文本消息
     * @param topic
     * @param message
     */
    public static void pushTextMessage(String topic, String message) {
        try {
            // Session： 一个发送或接收消息的线程
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  //自动确认
            Destination destination = session.createTopic(topic); //创建topic   myTopic
            // MessageProducer：消息发送者
            MessageProducer producer =  session.createProducer(destination);

            // 设置不持久化，可以更改
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //创建文本消息
            TextMessage textMessage = session.createTextMessage(message);
            //发送消息
            producer.send(textMessage);
            logger.info("消息对象推送成功：" + message);
            session.close();
        }catch (JMSException e) {
            logger.error("消息对象推送失败:" + message, e);
        }
    }

    /**
     * 推送对象消息
     * @param topic
     * @param object
     */
    public static void pushObjectMessage(String topic, Serializable object) {
        try {
            // Session： 一个发送或接收消息的线程
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  //自动确认
            Destination destination = session.createTopic(topic); //创建topic   myTopic
            // MessageProducer：消息发送者
            MessageProducer producer =  session.createProducer(destination);

            // 设置不持久化，可以更改
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            ObjectMessage objectMessage = session.createObjectMessage(object);
//            ActiveMQObjectMessage activeMQObjectMessage = (ActiveMQObjectMessage)session.createObjectMessage();
            objectMessage.setObject(object);
//            activeMQObjectMessage.setObject(object);
            producer.send(objectMessage);
//            producer.send(activeMQObjectMessage);
            logger.info("消息对象推送成功：" + object.toString());
            session.close();
        }catch (JMSException e) {
            logger.error("消息对象推送失败:" + object.toString(), e);
        }
    }
}
