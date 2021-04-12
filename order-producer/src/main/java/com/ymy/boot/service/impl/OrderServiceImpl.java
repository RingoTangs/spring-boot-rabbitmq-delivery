package com.ymy.boot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymy.boot.entity.BrokerMessageLog;
import com.ymy.boot.entity.Order;
import com.ymy.boot.mapper.BrokerMessageLogMapper;
import com.ymy.boot.mapper.OrderMapper;
import com.ymy.boot.producer.RabbitOrderSender;
import com.ymy.boot.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Ringo
 * @since 2021-04-11
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Resource
    private RabbitOrderSender rabbitOrderSender;

    @Transactional
    public Order createOrder() throws Exception {

        // 1: 创建订单
        String[] split = UUID.randomUUID().toString().split("-");
        String messageId = split[0];
        String orderId = split[1];
        Order order = new Order(orderId, "订单", messageId);

        // 2: 业务数据入库
        orderMapper.insert(order);

        // 3: 构建消息日志对象
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog(order.getMessageId(), new ObjectMapper().writeValueAsString(order));

        // 4: 日志消息入库
        brokerMessageLogMapper.insert(brokerMessageLog);

        // 5: 发送消息
        rabbitOrderSender.sendOrder(order);

        return order;
    }
}
