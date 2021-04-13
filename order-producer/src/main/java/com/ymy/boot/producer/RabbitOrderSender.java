package com.ymy.boot.producer;

import com.ymy.boot.constant.PublisherConstants;
import com.ymy.boot.entity.Order;
import com.ymy.boot.mapper.BrokerMessageLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 发送订单消息
 *
 * @author Ringo
 * @since 2021/4/11 23:06
 */
@Slf4j
@Component
public class RabbitOrderSender implements RabbitTemplate.ConfirmCallback {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    /**
     * Broker消息返回确认
     *
     * @author Ringo
     * @date 2021/4/12
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("correlationData：" + correlationData);
        String messageId = correlationData.getId();
        if (ack) {
            // 如果confirm返回成功则进行更新
            brokerMessageLogMapper.changeBrokerMessageLogStatus(PublisherConstants.ORDER_SENDING_SUCCESS, messageId);
        } else {
            // 失败则进行具体后续操作: 重试或者补偿等手段
            log.info("投递消息失败 Broker Nack....");
        }
    }

    /**
     * 向 Broker 发送消息调用该方法即可
     *
     * @author Ringo
     * @date 2021/4/13
     */
    public void sendOrder(Order order) {
        rabbitTemplate.setConfirmCallback(this);

        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order_exchange", "", order, correlationData);
    }
}
