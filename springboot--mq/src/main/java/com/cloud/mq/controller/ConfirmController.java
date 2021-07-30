package com.cloud.mq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2021/6/26 15:38
 * 发消息 发布确认高级篇。确保交换机或队列出问题，
 *      程序依然能正常响应的关键是：回调。利用回调通知生产者并将这部分消息保存下来以便再发
 *  测试：写错交换机名称，模拟交换机故障 【通过RabbitTemplate.ConfirmCallback 捕捉到未被路由器接收的消息】
 *  测试：写错routingkey，模拟队列故障 【通过RabbitTemplate.ReturnCallback 捕获未被正确路由到队列的消息】
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ConfirmController {

    /**
     * 回调接口在RabbitTemplate这里面的confirmCallback
     */
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendConfirm/{msg}")
    public void sendConfirmMessage(@PathVariable("msg")String msg){
        /**声明回调的形参*/
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend("confirm-exchange", "confirm-key", msg,correlationData);
        log.info("发送信息为:" + msg);
    }
}
