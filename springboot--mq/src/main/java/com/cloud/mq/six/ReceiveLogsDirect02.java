package com.cloud.mq.six;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 直连模式
 */
public class ReceiveLogsDirect02 {

    //交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        //声明一个队列
        channel.queueDeclare("disk",false,false,false,null);

        //绑定到交换机
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        //消费消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsDirect02接收到消息："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume("disk",true,deliverCallback,consumerTag->{});


    }
}
