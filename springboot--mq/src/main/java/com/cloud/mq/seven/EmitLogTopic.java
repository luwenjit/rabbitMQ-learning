package com.cloud.mq.seven;


import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import java.util.Scanner;

/**
 * 生产者
 *  topic模式
 */
public class EmitLogTopic {

    //交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 绑定关系：
         *  Q1--》*.orange.*
         *  Q2--》*.*.rabbit lazy.#
         */

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //发送消息,调整不同routekey，发送到不同队列
            channel.basicPublish(EXCHANGE_NAME, "quick.orange.rabbit", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);

        }

    }

}
