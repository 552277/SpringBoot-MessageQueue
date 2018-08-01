package com.example.SpringBootActiveMQ.otherMessageReceiver;

import com.example.SpringBootActiveMQ.bean.User;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author zhongweichang
 * @email 15090552277@163.com
 * @date 2018/8/1 下午1:20
 */
public class ObjectMessageReceiver {

    private static ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin",
            "admin", "tcp://10.242.62.239:61616");

    public static void main(String[] args) {

        //        // Connection ：JMS 客户端到JMS Provider 的连接
        try {
            final Connection connection = connectionFactory.createConnection();
            connection.setClientID("objectMessageReceiver"); //持久订阅需要设置这个。
            connection.start();
            // Session： 一个发送或接收消息的线程
            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 创建topic
            Topic topic = session.createTopic("id" + 666666);

            // 第2个消费者，订阅持久化消息
            MessageConsumer secondConsumer = session.createDurableSubscriber(topic,"secondConsumerId");//持久化订阅
            secondConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message msg) {

                    try {

                        User user = (User) ((ObjectMessage) msg).getObject();
//                        TextMessage message = (TextMessage) msg;
//                        ObjectMessage objectMessage = (ObjectMessage)msg;
//                        ActiveMQObjectMessage activeMQTextMessage = (ActiveMQObjectMessage)msg;
//                        System.out.println("第六个客户端上的 secondConsumer 收到second_topic消息： " + message.getText());
//                        System.out.println(activeMQTextMessage.getObject());
                        ObjectMessage objectMessage = (ObjectMessage)msg;
                        User user2 = (User)objectMessage.getObject();
                        System.out.println(user.toString());
                        session.commit();

                    } catch (JMSException e) {
                        e.printStackTrace();
                    }

                }
            });

//            connection.close(); // 如果关闭连接，每次执行一次程序就会停止，且每次执行时，只有secondConsumer消费者消费队列中的消息，thirdConsumer的队列中即使有消息，也不会进行消费
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
