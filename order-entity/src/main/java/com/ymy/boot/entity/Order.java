package com.ymy.boot.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author Ringo
 * @since 2021-04-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.NONE)
    private String id;

    /**
     * 订单名称
     */
    private String name;

    /**
     * 消息唯一ID
     */
    private String messageId;

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

    public Order(String id, String name, String messageId) {
        this.id = id;
        this.name = name;
        this.messageId = messageId;
    }
}
