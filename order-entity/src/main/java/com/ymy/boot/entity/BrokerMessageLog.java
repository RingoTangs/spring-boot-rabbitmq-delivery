package com.ymy.boot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Broker消息日志
 * </p>
 *
 * @author Ringo
 * @since 2021-04-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BrokerMessageLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息唯一ID
     */
    @TableId(value = "message_id", type = IdType.NONE)
    private String messageId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 重试次数
     */
    private Integer tryCount;

    /**
     * 消息投递状态
     */
    private Integer status;

    /**
     * 创建日期
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改日期
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 下次重试时间/超时时间
     */
    @TableField(value = "next_retry")
    private LocalDateTime nextRetry;

    public BrokerMessageLog(String messageId, String message) {
        this.messageId = messageId;
        this.message = message;
    }
}
