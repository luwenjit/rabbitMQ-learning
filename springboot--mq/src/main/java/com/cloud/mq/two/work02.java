package com.cloud.mq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 工作线程：相当于之前的消费者
 */
public class work02 {

    //队列
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到消息：" + new String(message.getBody()));
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者取消消费");
        };

        //消息接收
        /**
         *  消费者消费消息
         *  1、消费的队列
         *  2、消费成功之后是否自动应答 true自动应答
         *  3、消费者未成功消费的回调
         *  4、消费者取消消费的回调
         */
        System.out.println("C2等待接收消息......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);


    }

}
