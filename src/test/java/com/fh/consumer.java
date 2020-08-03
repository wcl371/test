package com.fh;


import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class consumer {
        public static void main(String[] args) throws IOException, TimeoutException {
//        创建连接工厂实例
            ConnectionFactory connectionFactory=new ConnectionFactory();
//        为连接工厂绑定地址、端口、用户名、密码、虚拟主机
            connectionFactory.setHost("192.168.174.130");
            connectionFactory.setPort(5672);
            connectionFactory.setUsername("admin");
            connectionFactory.setPassword("123456");
            connectionFactory.setVirtualHost("/");
//        取得连接实例
            Connection connection = connectionFactory.newConnection();
//        取得频道
            Channel channel=connection.createChannel();
//        声明默认消费者
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    System.out.println("ID:" + envelope.getDeliveryTag() + ",   Exchange:" + envelope.getExchange() +
                            ",      RoutingKey:" + envelope.getRoutingKey() + ",      Class:" + envelope.getClass() +
                            body.toString());
                }
            };
//        接受信息并按方法处理
            channel.basicConsume("simple_queue",true,defaultConsumer);
            /*参数说明
             * String var1,          队列名称
             * boolean var2,         是否自动应答
             * Consumer var3         使用的消费方法实例s
             * */
        }
    }


