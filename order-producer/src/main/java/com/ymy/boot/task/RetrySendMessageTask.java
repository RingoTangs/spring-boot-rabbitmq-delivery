package com.ymy.boot.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymy.boot.constant.PublisherConstants;
import com.ymy.boot.entity.BrokerMessageLog;
import com.ymy.boot.entity.Order;
import com.ymy.boot.mapper.BrokerMessageLogMapper;
import com.ymy.boot.producer.RabbitOrderSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 补偿机制：重新投递消息的定时任务
 *
 * @author Ringo
 * @since 2021/4/11 23:24
 */
@Slf4j
@Component
public class RetrySendMessageTask {

    @Resource
    private RabbitOrderSender rabbitOrderSender;

    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 3000, fixedDelay = 10000)
    public void reSend() {
        log.info("------------------定时任务开始------------------");

        // 获取 timeoutMessageLog 列表
        List<BrokerMessageLog> timeoutLogs = brokerMessageLogMapper.queryTimeoutMessages();
        timeoutLogs.forEach(log -> {
            if (log.getTryCount() >= 3) {
                // 投递失败 状态更新为 ORDER_SENDING_FAILURE(2)
                brokerMessageLogMapper.changeBrokerMessageLogStatus(PublisherConstants.ORDER_SENDING_FAILURE, log.getMessageId());
            } else {
                // 更新重投次数(同时更新下次投递时间)
                brokerMessageLogMapper.updateTryCountWhenReSend(log.getMessageId());
                try {
                    // 重新投递
                    Order order = new ObjectMapper().readValue(log.getMessage(), Order.class);
                    rabbitOrderSender.sendOrder(order);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常处理...");
                }
            }
        });
    }

}
