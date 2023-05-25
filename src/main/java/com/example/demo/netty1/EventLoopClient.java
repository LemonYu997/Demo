package com.example.demo.netty1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author mly
 * @date 2022/11/8 11:13
 */
@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        //带有Future、Promise的类型都是和异步方法配套使用
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //把字符串编码成ByteBuf
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //1、连接到服务器
                //异步非阻塞方法，调用connect方法的是主线程（main），真正执行连接的是nio线程
                .connect(new InetSocketAddress("localhost", 8080)); //1s后执行

        //2.1 使用sync()方法同步处理结果
//        channelFuture.sync();   //阻塞住当前线程，直到nio线程连接建立完毕
//
//        Channel channel = channelFuture.channel();
//        log.info("{}", channel);    //[main] INFO com.example.demo.netty1.EventLoopClient - [id: 0x5d1bb94a]
//        channel.writeAndFlush("hello, world");

        //2.2 使用addListener(回调对象)方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            //在nio线程建立之后会调用operationComplete
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("{}", channel);
                channel.writeAndFlush("hello, world");
            }
        });
    }
}
