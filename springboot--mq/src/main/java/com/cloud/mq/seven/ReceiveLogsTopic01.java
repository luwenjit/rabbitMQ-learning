package com.cloud.mq.seven;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 主题模式
 *  消费者1
 */
public class ReceiveLogsTopic01 {

    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        //声明一个队列
        channel.queueDeclare("Q1",false,false,false,null);

        //绑定到交换机
        channel.queueBind("Q1",EXCHANGE_NAME,"*.orange.*");

        //消费消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsTopic01接收到消息："+new String(message.getBody(),"UTF-8"));
            System.out.println("接收队列：Q1"+"绑定键："+message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume("Q1",true,deliverCallback,consumerTag->{});


    }
}
