package com.cloud.mq.five;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消息接收
 */
public class ReceiveLogs02 {

    //交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        /**
         * 1、交换机名称
         * 2、交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //声明一个队列 临时队列
        /**
         * 生成一个临时队列 名称随机
         * 当消费者断开队列连接后，队列就自动删除了
         */
        String queueName = channel.queueDeclare().getQueue();

        //绑定交换机与队列
        /**
         * 1、队列名称
         * 2、交换机名称
         * 3、路由key
         */
        channel.queueBind(queueName, EXCHANGE_NAME,null);

        System.out.println("等待接收消息，吧接收到的消息打印屏幕上......");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("ReceiveLogs02接收到消息："+new String(message.getBody(),"UTF-8"));
        };

        /**
         *  消费者消费消息
         *  1、消费的队列
         *  2、消费成功之后是否自动应答 true自动应答
         *  3、消费者未成功消费的回调
         *  4、消费者取消消费的回调
         */
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});

    }
}
