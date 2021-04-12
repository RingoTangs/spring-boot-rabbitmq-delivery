# RabbitMQ实现消息可靠性投递

## 1. 环境准备

### 1.1. MySQL

当前项目`MySQL`版本为5. 7，为了方便搭建环境，采用 `Docker` 安装。

```shell
docker run --name mysql -d -p 3306:3306 \
-v yourPath:/var/lib/mysql \
-v yourPath:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=yourPassword \
mysql:5.7
```

数据库表请看项目中的 `order_log.sql`。



### 1.2. RabbitMQ

`RabbitMQ`的 Docker 镜像为 3-management，带有 Web 界面的控制台。

默认端口 5672，web控制台端口 15672。

```shell
docker run -d \
--name rabbimtmq -p 5672:5672 \
-p 15672:15672 \
rabbitmq:3-management
```



### 1.3. 开发环境

- **JDK 1.8**
- **Maven 3.5**
- **IntelliJ IDEA**  (*注意：务必使用 IDEA 开发，同时保证安装 `lombok` 插件*)



## 2. 项目介绍

### 2.1. 基本介绍

该项目中包含两个子项目`order-entity` 和 `order-producer`

- `order-entity`：封装了实体类，导入一些工具包。
- `order-producer`：主要业务逻辑，Controller、Service、定时任务等，并且依赖 `order-entity`。

### 2.2. order-producer

- `config`：该包下是 RabbitMQ  的配置，包括声明交换机、队列、绑定关系和消息的转换。
- `constant`：该包定义常量，用于方便描述消息的投递状态。
- `controller`：定义了一个接口 `/order`，在浏览器/postman 中调用该接口可创建订单。
- `mapper`：定义了操作数据库的方法。
- `producer`：消息投递和消息确认模板。
- `service`：具体业务逻辑。
- `task`：定时任务。

### 2.3. 业务流程

![可靠性投递流程](https://img-blog.csdnimg.cn/20210412104515846.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L1JyaW5nb18=,size_16,color_FFFFFF,t_70)



- `step1`：订单和日志记录全部入库（消息日志入库的初始投递状态为0，表示正在投递）。
- `step2`：向 Broker 投递消息。
- `step3`：发送端监听 Broker 的确认。
- `step4`：如果发送端监听确认消息为ACK，表示Broker成功收到消息，将日志记录中的消息投递状态更改为1（1代表消息投递成功）。
- `step5、step6`：定时任务抓取超时消息（status = 0，并且投递时间大于1分钟即为超时消息），如果重投次数 >= 3就标记为投递失败（投递状态更改为2），反之重新投递。



## 3. 如何使用?

```shell
# 下载项目到本地
git clone https://github.com/RingoTangs/spring-boot-rabbitmq-delivery.git

# 使用Maven打开该项目下载依赖

# 配置application.yaml中的环境

# 启动项目, 访问 /order接口，观察 MySQL 和 RabbitMQ消息记录即可！
```

