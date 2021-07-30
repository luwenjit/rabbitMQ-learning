package com.cloud.mq.eight;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

/**
 * 生产者
 * 测试死信队列 关闭消费者1，消息进入死信交换机【模拟消费者消息积压】
 *  2、设置队列最大存储长度
 */
public class Product_1 {

    //普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, "direct");

        //死信消息 设置ttl过期时间 10s
//        AMQP.BasicProperties properties = new AMQP.BasicProperties()
//                .builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String message = "info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
        }


    }

}
