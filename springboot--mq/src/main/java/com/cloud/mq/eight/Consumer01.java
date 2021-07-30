package com.cloud.mq.eight;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者1
 *  两个交换机，两个队列，且普通队列得往死信队列发消息
 */
public class Consumer01 {

    //普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";

    //普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, "direct");
        channel.exchangeDeclare(DEAD_EXCHANGE, "direct");

        /**
         * 队列参数，指明队列消息成为死信后转发到死信交换机
         */
        Map<String,Object> arguments = new HashMap<>();
        //过期时间
//        arguments.put("x-message-ttl",10000);  //过期时间10s【也可以通过生产者更改过期时间，更灵活】
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);  //转发到死信交换机
        //设置死信routekey
        arguments.put("x-dead-letter-routing-key","lisi");  //死信交换机--》死信队列

        //声明一个队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,null);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定普通交换机和普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信交换机和死信队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        //消费消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("Consumer01接收到消息："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,consumerTag->{});


    }
}