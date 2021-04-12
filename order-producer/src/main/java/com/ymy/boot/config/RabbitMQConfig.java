package com.ymy.boot.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 *
 * @author Ringo
 * @since 2021/4/11 23:36
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 声明交换机
     *
     * @author Ringo
     * @date 2021/4/12
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("order_exchange");
    }

    /**
     * 声明队列
     *
     * @author Ringo
     * @date 2021/4/12
     */
    @Bean
    public Queue queue() {
        return new Queue("order.queue");
    }

    /**
     * 交换机-队列 绑定关系
     *
     * @author Ringo
     * @date 2021/4/12
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(fanoutExchange());
    }


    /**
     * 消息序列化 -
     * 实体类中有 LocalDateTime 则需要对Jackson2JsonMessageConverter增强
     *
     * @author Ringo
     * @date 2021/4/11
     */
    @Bean
    public MessageConverter messageConverter() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(om);
    }
}
