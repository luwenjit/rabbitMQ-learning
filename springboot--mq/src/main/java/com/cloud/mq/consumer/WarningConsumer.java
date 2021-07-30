package com.cloud.mq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author lisw
 * @create 2021/6/26 16:44
 * 报警消费者 接收备份交换机来的消息
 */
@Component
@Slf4j
public class WarningConsumer {
    @RabbitListener(queues = {"warning-queue"})
    public void receiveWarningMsg(Message message){
        log.warn("出现不可路由消息:",message);
    }
}
