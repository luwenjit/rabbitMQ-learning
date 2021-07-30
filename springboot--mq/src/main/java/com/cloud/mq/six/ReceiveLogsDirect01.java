package com.cloud.mq.six;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 直连模式
 */
public class ReceiveLogsDirect01 {

    //交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        //声明一个队列
        channel.queueDeclare("console",false,false,false,null);

        //绑定到交换机【绑定两种routekey】
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");

        //消费消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsDirect01接收到消息："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("console",true,deliverCallback,consumerTag->{});


    }
}
