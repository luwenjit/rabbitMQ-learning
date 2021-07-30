package com.cloud.mq.seven;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 主题模式
 *  消费者2
 */
public class ReceiveLogsTopic02 {

    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        //声明一个队列
        channel.queueDeclare("Q2",false,false,false,null);

        //绑定到交换机
        channel.queueBind("Q2",EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind("Q2",EXCHANGE_NAME,"lazy.#");

        //消费消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsTopic02接收到消息："+new String(message.getBody(),"UTF-8"));
            System.out.println("接收队列：Q2"+"绑定键："+message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume("Q2",true,deliverCallback,consumerTag->{});


    }
}
