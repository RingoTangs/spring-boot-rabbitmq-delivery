package com.ymy.boot.mapper;

import com.ymy.boot.entity.BrokerMessageLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Ringo
 * @since 2021-04-11
 */
public interface BrokerMessageLogMapper extends BaseMapper<BrokerMessageLog> {

    /**
     * 查询投递超时的消息(投递时间超过1分钟即为超时)
     *
     * @author Ringo
     * @date 2021/4/11
     */
    List<BrokerMessageLog> queryTimeoutMessages();

    /**
     * 消息重新投递时,更新重试次数(同时会更改消息下一次投递时间)
     *
     * @param messageId 消息唯一ID
     * @author Ringo
     * @date 2021/4/11
     */
    int updateTryCountWhenReSend(@Param("messageId") String messageId);

    /**
     * 设置消息投递状态
     *
     * @param status    消息的投递状态 0：投递中, 1: 投递成功, 2: 投递失败
     * @param messageId 消息唯一ID
     * @author Ringo
     * @date 2021/4/11
     */
    int changeBrokerMessageLogStatus(@Param("status") Integer status, @Param("messageId") String messageId);


}
