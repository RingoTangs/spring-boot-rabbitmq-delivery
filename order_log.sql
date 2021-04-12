/* 创建数据库 */
CREATE DATABASE `test_rabbitmq`;

/* 表 t_order 订单表 */
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
`id` VARCHAR(128) NOT NULL COMMENT '订单ID',
`name` VARCHAR(128) DEFAULT NULL COMMENT '订单名称', -- 其他订单字段忽略
`message_id` VARCHAR(128) NOT NULL COMMENT '消息唯一ID',
`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/* 表 broker_message_log 消息记录结构 */
DROP TABLE IF EXISTS `broker_message_log`;
CREATE TABLE `broker_message_log` (
`message_id` VARCHAR(128) NOT NULL COMMENT '消息唯一ID',
`message` VARCHAR(4000) DEFAULT NULL COMMENT '消息内容',
`try_count` INT(4) DEFAULT 0 COMMENT '重试次数',
`status` TINYINT DEFAULT 0 COMMENT '消息投递状态', -- 0：投递中, 1: 投递成功, 2: 投递失败
`create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
`update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
`next_retry` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '下次重试时间/超时时间',
PRIMARY KEY(`message_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 查询投递超时的消息(投递时间超过1分钟即为超时) queryTimeoutMessages()
SELECT * FROM broker_message_log WHERE `status` = 0 AND DATE_ADD(`next_retry`, INTERVAL 1 MINUTE) <= NOW();

-- 消息重新投递时,更新重试次数(同时更新`next_retry`字段) updateTryCountWhenReSend(String messageId)
UPDATE `broker_message_log` SET `try_count` = `try_count` + 1, `next_retry` = NOW()
WHERE `message_id` = '0092dd02';

-- 设置消息投递状态 changeBrokerMessageLogStatus(Integer status, String messageId)
UPDATE `broker_message_log` SET `status` = 1 WHERE `message_id` = 1;





