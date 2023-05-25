package com.example.demo.netty1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author mly
 * @date 2022/11/8 11:13
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1、启动器
        new Bootstrap()
                //2、添加EventLoop
                .group(new NioEventLoopGroup())
                //3、选择客户端的channel实现
                .channel(NioSocketChannel.class)
                //4、添加处理器 在连接建立后会被调用
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    //处理逻辑
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //把字符串编码成ByteBuf
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //5、连接到服务器
                .connect(new InetSocketAddress("localhost", 8080))
                //阻塞方法，直到连接建立
                .sync()
                //代表链接对象
                .channel()
                //6、向服务器发送数据
                .writeAndFlush("hello, world");
    }
}
