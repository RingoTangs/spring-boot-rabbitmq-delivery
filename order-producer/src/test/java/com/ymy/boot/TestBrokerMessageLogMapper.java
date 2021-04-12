package com.ymy.boot;

import com.ymy.boot.entity.BrokerMessageLog;
import com.ymy.boot.mapper.BrokerMessageLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ringo
 * @since 2021/4/11 22:15
 */
@Slf4j
@SpringBootTest
public class TestBrokerMessageLogMapper {
    @Resource
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Test
    void queryTimeoutMessages() {
        List<BrokerMessageLog> brokerMessageLogs = brokerMessageLogMapper.queryTimeoutMessages();
        brokerMessageLogs.forEach(m -> log.info(m + ""));
    }

    @Test
    void updateTryCountWhenReSend() {
        int ret = brokerMessageLogMapper.updateTryCountWhenReSend("0092dd02");
        log.info(ret + "");
    }

    @Test
    void changeBrokerMessageLogStatus() {
        int ret = brokerMessageLogMapper.changeBrokerMessageLogStatus(0, "1");
        log.info(ret + "");
    }
}
