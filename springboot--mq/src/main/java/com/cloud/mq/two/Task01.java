package com.cloud.mq.two;

import com.rabbitmq.client.Channel;
import java.util.Scanner;

/**
 * 生产者：发送大量消息
 *  实验多消费者轮询接收消息
 */
public class Task01 {

    //队列
    public static final String QUEUE_NAME = "hello";

    //发送大量消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 生成一个队列
         * 1、队列名称
         * 2、队列是否持久化（磁盘），默认储存在内存中【队列持久化】
         * 3、该队列是否只供一个消费者消费，是否进行消费共享，true可以多个消费者消费
         * 4、是否自动删除，最后一个消费者端开连接后，该队列是否自动删除 true自动删除
         * 5、其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //从控制台中发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 发送一个消息
             *  1、发送到的交换机
             *  2、路由key，本次是队列名
             *  3、其他参数【MessageProperties.PERSISTENT_TEXT_PLAN 设置发送的消息持久化】
             *  4、发送的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成："+message);

        }


    }
}
