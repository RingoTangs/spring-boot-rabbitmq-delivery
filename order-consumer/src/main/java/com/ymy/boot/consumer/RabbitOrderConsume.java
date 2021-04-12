package com.ymy.boot.consumer;

import com.rabbitmq.client.Channel;
import com.ymy.boot.entity.Order;
import javafx.beans.binding.ObjectExpression;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Ringo
 * @since 2021/4/12 13:20
 */
@Component
public class RabbitOrderConsume {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queues = {"order.queue"})
    @RabbitHandler
    public void receiveOrder(@Payload Order order, @Headers Map<String, Object> headers,
                             Channel channel) throws Exception {

        // 消息的限流
        channel.basicQos(1);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        // 从Redis中查看消息是否消费过
        String val = stringRedisTemplate.opsForValue().get(order.getMessageId());

        System.out.println(val);
        if (val != null) {
            channel.basicAck(deliveryTag, false);
            System.out.println("重复订单: " + order);
            return;
        }

        // 消息第一次被消费, 调取下游服务消费消息
        System.out.println("order:" + order);
        stringRedisTemplate.opsForValue().set(order.getMessageId(), "1");
        // 消费端 ACK ==> RabbitMQ就会删除该消息！
        channel.basicAck(deliveryTag, false);
    }
}
