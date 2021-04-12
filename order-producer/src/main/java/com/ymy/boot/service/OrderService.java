package com.ymy.boot.service;

import com.ymy.boot.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Ringo
 * @since 2021-04-11
 */
public interface OrderService extends IService<Order> {
    Order createOrder() throws Exception;
}
