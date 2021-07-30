package com.cloud.mq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.Date;

/**
 * @author lisw
 * @create 2021/6/26 15:02
 * 基于插件的延迟队列消费者，对应生产者：MsgController中的sendDelayedMsg生产者
 */
@Component
@Slf4j
public class DelayedConsumer {
    @RabbitListener(queues = {"delayed.queue"})
    public void receiveMsg(Message message, Channel channel){
        String msg = new String(message.getBody());
        log.info("当前时间为{}，收到延迟消息为{}",new Date(), msg);
    }
}
