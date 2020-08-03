package com.fh;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class provider {
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
//        频道声明队列信息
            channel.queueDeclare("simple_queue",true,false,false,null);
            /*参数说明
             * String var1,          队列名称
             * boolean var2,         是否将信息持久化，一般为true
             * boolean var3,         是否独占队列，一般为false
             * boolean var4,         信息被消费后是否自动删除，一般为false
             * Map<String, Object> var5      附加参数
             * */

            String message="hello 12345!";
//        发送信息
            channel.basicPublish("","simple_queue",null,message.getBytes());
            /*参数说明
             * String var1,      交换机名称，不使用交换机为空
             * String var2,      RoutingKey名称（交换机按此进行派送），或队列名称
             * BasicProperties var5,         附加数据
             * byte[] var6       是要发送的数据
             * */
//        关闭资源
            channel.close();
            connection.close();

        }
    }


