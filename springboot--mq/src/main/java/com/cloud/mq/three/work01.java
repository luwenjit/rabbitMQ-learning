package com.cloud.mq.three;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 工作线程：相当于之前的消费者
 */
public class work01 {

    //队列
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //接收消息
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        // 模拟两个消费者消费情况不一样
        System.out.println("C1等待接收消息处理时间较短");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 沉睡1秒
            SleepUtils.sleep(1);
            System.out.println("接收到消息：" + new String(message.getBody(),"UTF-8"));
            // 手动应答
            /**
             * 手动应答
             * 1、消息标记tag
             * 2、是否批量应答 true批量
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者取消消费");
        };

        //设置不公平分发
//        int prodectCount = 1;
        //预取值【队列堆积个数，如果前面分发的消费完了，后面过来的就是重新算个数】
//        int prodectCount = 5;
//        channel.basicQos(prodectCount);

        //消息接收
        /**
         *  消费者消费消息
         *  1、消费的队列
         *  2、消费成功之后是否自动应答 true自动应答
         *  3、消费者未成功消费的回调
         *  4、消费者取消消费的回调
         */
        System.out.println("C1等待接收消息......");
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);


    }

}