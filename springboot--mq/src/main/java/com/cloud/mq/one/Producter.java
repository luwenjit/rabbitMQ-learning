package com.cloud.mq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者：发消息
 */
public class Producter {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip 连接到队列
        factory.setHost("192.168.200.129");
        factory.setUsername("admin");
        factory.setPassword("123");

        //创建连接
        Connection connection = factory.newConnection();

        //获取信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         * 1、队列名称
         * 2、队列里面的消息是否持久化（磁盘），默认储存在内存中
         * 3、该队列是否只供一个消费者消费，是否进行消费共享，true可以多个消费者消费
         * 4、是否自动删除，最后一个消费者端开连接后，该队列是否自动删除 true自动删除
         * 5、其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false, false, false, null);

        //发消息
        String message = "hello world";

        /**
         * 发送一个消息
         *  1、发送到的交换机
         *  2、路由key，本次是队列名
         *  3、其他参数
         *  4、发送的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

        System.out.println("消息发送完毕");



    }


}
