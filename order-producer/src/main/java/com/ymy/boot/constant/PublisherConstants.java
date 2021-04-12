package com.ymy.boot.constant;

/**
 * 常量
 * @author Ringo
 * @since 2021/4/11 22:26
 */
public interface PublisherConstants {

    // 订单正在投递
    static final int ORDER_SENDING = 0;

    // 订单投递成功
    static final int ORDER_SENDING_SUCCESS = 1;

    // 订单投递失败
    static final int ORDER_SENDING_FAILURE = 2;

    // 订单投递超时
    static final boolean ORDER_TIMEOUT = true;
}
