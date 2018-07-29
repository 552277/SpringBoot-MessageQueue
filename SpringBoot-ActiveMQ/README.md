# SpringBoot-ActiveMQ

执行顺序及结果

### 1，首先将第1个topic的所有消费者程序执行一次，将消费者及其订阅的主题注册到消息服务中心

打开http://10.242.62.239:8161/admin/subscribers.jsp 页面，查看注册情况：

#### 第一个topic消费者注册结果如下：

Active Durable Topic Subscribers（在线状态的持久订阅消费者）：

    1个第1个客户端中的持久订阅消费者
    2个第2个客户端中的持久订阅消费者
    1个第3个客户端中的持久订阅消费者
    
Offline Durable Topic Subscribers（离线状态的持久订阅消费者）：

    无
    
Active Non-Durable Topic Subscribers（在线状态的非持久订阅消费者）：

    第3个客户端的非持久订阅者
    第4个客户端的两个持久订阅者
    
### 2，将第1个topic的所有消费者程序全部停止，此时服务中心的注册情况如下：

Active Durable Topic Subscribers（在线状态的持久订阅消费者）：
    
    无
    
Offline Durable Topic Subscribers（离线状态的持久订阅消费者）：

        1个第1个客户端中的持久订阅消费者
        2个第2个客户端中的持久订阅消费者
        1个第3个客户端中的持久订阅消费者
        
Active Non-Durable Topic Subscribers（在线状态的非持久订阅消费者）：

    无
    
### 3，执行生产者向消息队列发送3条消息，服务中心状态变化：

    原来第个订阅者Pending Queue Size字段的值由0变为3，表示各个订阅者未消费的消息

### 4，依次启动第1个topic的各个客户端程序：

#### 对于持久订阅消费者：

   比如执行FirstClientSubscriber，则其对应的消费者将原来未消息的消息进行消费，服务中心中其中未消费消息数量由3变为0
   
   如果执行SecondClientSubscriber，则其中的两个订阅者中secondConsumer消费者会一次性将未消费的3条消息进行消费，
   secondConsumer消费者将所有消息消费完后，thirdConsumer消费者再消费（而不是轮次着去消费相同的消息再进行下一轮）
   如下所示：
   
    第二个客户端上的 secondConsumer 收到消息： j0
    第二个客户端上的 secondConsumer 收到消息： j1
    第二个客户端上的 secondConsumer 收到消息： j2
    第二个客户端上的 thirdConsumer 收到消息： j0
    第二个客户端上的 thirdConsumer 收到消息： j1
    第二个客户端上的 thirdConsumer 收到消息： j2
   
#### 对于非持久订阅者：

    不会输出任何消息，即没有可消费的消息
    
### 5，当所有消费者处理在线状态时，再次执行生产者生产3条first_topic消息

    此时持久订阅者和非持久订阅者都会消费这3条消息并输出
    但是对于如SecondClientSubscriber中有两个消费者的情况，他们消费顺序不再是一个消费者将所有的3条消费完后，
    再由第2个消费者消费，而是如下顺序，即是第一条消息轮流消费完，再轮流消费第2条消息：
    
    第二个客户端上的 secondConsumer 收到消息： j0
    第二个客户端上的 thirdConsumer 收到消息： j0
    第二个客户端上的 secondConsumer 收到消息： j1
    第二个客户端上的 thirdConsumer 收到消息： j1
    第二个客户端上的 secondConsumer 收到消息： j2
    第二个客户端上的 thirdConsumer 收到消息： j2
    
    刚开始我以为是生产者第生产第生产一条消息，就及时推送给两个消费者，两个消费者消费完后才收到第2条消息，
    经过打断点发现实际并不是这样。而是生产者将消息生产出后，只要消费者处于在线状态，消息队列就会将所有消息推送给消费者
    所在即使消费者被打了断点没有消费消息，但是只要处于在线状态，在订阅者列表中，可以看到在线状态的消息者没有待消费消息，
    即消息都已经被推送给在线的消费者，并由消费者如上所示轮流消费每条消息
    
    
### 6，对于第2个topic消息
    
    生产者可以同时生产发送到first_topic和second_topic的消息，对于第2个topic的消费者注册情况
    和消费情况同第1个topic，只是第1个topic只由第1个topic的消费者消费，第2个topic只有第2个topic
    的消费者消费。
  
    
    
