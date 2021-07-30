package com.cloud.mq.nine;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者：发消息
 *  测试优先队列
 *      需要设置队列优先级，设置发送消息时优先级，并模拟消息在排队中才能看出优先级
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

        Map<String,Object> arguments = new HashMap<>();
        //官方允许0-255之间，此处设置10，不要设置过大，浪费cpu和内存
        arguments.put("x-max-priority", 10);

        /**
         * 1、设置队列优先级
         */
        channel.queueDeclare(QUEUE_NAME,true, false, false, arguments);

        for (int i = 1; i < 11; i++) {
            String message = "info"+i;
            if (i==5){
                /**
                 * 2、设置消息的优先级
                 */
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("",QUEUE_NAME,properties,message.getBytes());
            }else {
                channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            }
        }

        System.out.println("消息发送完毕");


    }


}
