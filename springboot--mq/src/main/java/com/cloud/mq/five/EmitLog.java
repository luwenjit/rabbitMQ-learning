package com.cloud.mq.five;

import com.cloud.mq.two.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import java.util.Scanner;

/**
 * 生产者
 * 测试生产者通过扇出模式（发布订阅），两个接收者接收消息
 */
public class EmitLog {

    //交换机名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //发送消息
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);

        }

    }

}
