package com.cloud.mq.controller;

import com.cloud.mq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author lisw
 * @create 2021/6/25 21:51
 * 生产者-发送延时消息
 */
@RestController
@RequestMapping("/ttl")
@Slf4j
public class MsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 1、定义两个普通队列，接收生产者发来得消息，通过QA,QB设置过期时间
     * 问题：可以按预先设置队列ttl时间收到消息，但是不能灵活运用
     */
    /**开始发消息*/
    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable("msg")String msg){
      /**后者会给占位符赋值，实现动态传递*/
      log.info("当前时间:{},发送一条消息给两个TTL队列",new Date(),msg);
      rabbitTemplate.convertAndSend("X","XA", "消息来自ttl为10s的队列:" + msg);
      rabbitTemplate.convertAndSend("X","XB", "消息来自ttl为40s的队列:" + msg);
    }

    /**
     * 2、定义一个普通队列，不再设置该队列的消息过期时间，而是让生产者指定TTL时间【优化1中的时间不能自定义问题】
     * 问题：两次指定不同ttl的消息，发生了排队情况，实际收到消息的时间并不是参数ttl时间【rabbitMQ只会检测第一条消息是否过期】
     */
    /**开始发定义有过期时间的消息*/
    @GetMapping("/sendMsg/{msg}/{ttlTime}")
    public void sendExpireMsg(@PathVariable("msg")String msg,@PathVariable("ttlTime")String ttlTime){
        /**后者会给占位符赋值，实现动态传递*/
        log.info("当前时间:{},发送一条时长是{}ms的TTL消息给QC队列,内容是{}",new Date(),ttlTime,msg);
        rabbitTemplate.convertAndSend("X","XC", "ttl消息为"+ ttlTime +"的时间，内容为:" + msg,message ->{
            /**生产者设置过期时间*/
            message.getMessageProperties().setExpiration(ttlTime);
            return message;
        });
    }

    /**
     * 3、基于插件的延迟消息发送，解决2中的问题
     *
     */
    /**基于插件的延时队列*/
    @GetMapping("/sendDelayedMsg/{msg}/{delayedTime}")
    public void sendDelayedMsg(@PathVariable("msg")String msg,@PathVariable("delayedTime")Integer delayedTime){
        /**后者会给占位符赋值，实现动态传递*/
        log.info("当前时间:{},发送一条时长是{}ms的延时消息给QC队列,内容是{}",new Date(),delayedTime,msg);
        rabbitTemplate.convertAndSend("delayed.exchange","delayed.routingkey",
                "延时消息的时间是"+ delayedTime +"，内容为:" + msg, message ->{
            /**生产者设置延时时间 */
            /**和上面的有区别*/
            message.getMessageProperties().setDelay(delayedTime);
            return message;
        });
    }
}
