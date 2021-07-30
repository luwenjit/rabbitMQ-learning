package com.cloud.mq.six;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import java.util.Scanner;

/**
 * 生产者
 * 测试直连模式，一个生产者，两个消费者，三个routekey
 */
public class DirectLog {

    //交换机名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //发送消息,调整不同routekey，发送到不同队列
            channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);

        }

    }

}
