package com.ymy.boot.producer;

import com.ymy.boot.constant.PublisherConstants;
import com.ymy.boot.entity.Order;
import com.ymy.boot.mapper.BrokerMessageLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
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
public class RabbitOrderSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    // 回调函数: confirm 确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
            log.info("correlationData：" + correlationData);
            String messageId = correlationData.getId();
            if (ack) {
                // 如果confirm返回成功则进行更新
                brokerMessageLogMapper.changeBrokerMessageLogStatus(PublisherConstants.ORDER_SENDING_SUCCESS, messageId);
            } else {
                // 失败则进行具体后续操作: 重试或者补偿等手段
                System.out.println("重试或者补偿等手段...");
            }
        }
    };

    // 发送消息方法调用: 构建自定义对象消息
    public void sendOrder(Order order) {
        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order_exchange", "", order, correlationData);
    }
}
