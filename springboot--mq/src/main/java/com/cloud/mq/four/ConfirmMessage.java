package com.cloud.mq.four;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 *  比较耗时，确认选哪一种
 *  1、单个确认：发一个确认一个，再发下一个
 *  2、批量确认：批量操作存在中间有失败情况
 *  3、异步批量确认：利用回调函数确保消息可靠性传递
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1、单个确认
//        ConfirmMessage.publicMessageOne();  //722ms
        //2、批量确认
        ConfirmMessage.publicMessageTwo();  //147ms
        //3、异步批量确认
        ConfirmMessage.publicMessageThree();  //62ms

    }


    public static void publicMessageOne() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        //申明队列
        String queneName = UUID.randomUUID().toString();
        channel.queueDeclare(queneName,false,false,false,null);

        //开启发布确认
        channel.confirmSelect();

        //开始时间
        long begin = System.currentTimeMillis();

        //单个确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            //发送消息
            channel.basicPublish("",queneName,null,message.getBytes());
            //单个消息进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag){
                System.out.println("消息发送成功");
            }

        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个消息，单个确认模式耗时：" + (end - begin) + "ms");

    }

    public static void publicMessageTwo() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        //申明队列
        String queneName = UUID.randomUUID().toString();
        channel.queueDeclare(queneName,false,false,false,null);

        //开启发布确认
        channel.confirmSelect();

        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 1000;

        //批量发送，批量确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            //发送消息
            channel.basicPublish("",queneName,null,message.getBytes());
        }
        //单个消息进行发布确认
        boolean flag = channel.waitForConfirms();
        if (flag){
            System.out.println("批量消息发送成功");
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个消息，批量确认模式耗时：" + (end - begin) + "ms");

    }

    public static void publicMessageThree() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();

        //申明队列
        String queneName = UUID.randomUUID().toString();
        channel.queueDeclare(queneName,false,false,false,null);

        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的hash表：适用于高并发的情况下【参数long String根据原理图来】
         * 1、轻松的将序号与消息进行关联 【序号--》消息的map结构】
         * 2、轻松批量删除条目 只要给到序号
         * 3、支持高并发
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirm = new ConcurrentSkipListMap<>();

        //开始时间
        long begin = System.currentTimeMillis();

        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple)->{
            //2、删除已确认的消息 剩下的就是未确认的消息
            if (multiple){
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirm.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outstandingConfirm.remove(deliveryTag);
            }
            System.out.println("确认的消息："+deliveryTag);
        };
        //消息确实失败 回调函数
        /**
         * 1、消息标记
         * 2、是否批量
         */
        ConfirmCallback nackCallback = (deliveryTag,multiple)->{
            //3、打印下未确认的消息
            String message = outstandingConfirm.get(deliveryTag);
            System.out.println("未确认的消息："+message + ":::未确认消息的tag:" + deliveryTag);
        };
        /**
         * 准备消息的监听器，哪些消息成功，哪些消息失败【监听器s放在发送前】
         *  1、监听哪些消息成功
         *  2、监听哪些消息失败
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        //批量发送，异步批量确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            //发送消息
            channel.basicPublish("",queneName,null,message.getBytes());
            /**
             * 1、记录所有要发送的消息 消息总数
             */
            outstandingConfirm.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个消息，异步批量确认模式耗时：" + (end - begin) + "ms");

    }


}
