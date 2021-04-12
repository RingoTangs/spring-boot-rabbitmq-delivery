package com.ymy.boot.controller;


import com.ymy.boot.entity.Order;
import com.ymy.boot.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Ringo
 * @since 2021-04-11
 */
@RestController
public class OrderController {

    @Resource
    private OrderService orderServiceImpl;

    @GetMapping("/order")
    public Order order() throws Exception {
        return orderServiceImpl.createOrder();
    }

}

