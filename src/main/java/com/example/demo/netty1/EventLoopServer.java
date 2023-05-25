package com.example.demo.netty1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author mly
 * @date 2022/11/8 14:46
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        //分工细化版本
        //创建一个独立的EventLoopGroup
        EventLoopGroup group = new DefaultEventLoopGroup();

        new ServerBootstrap()
                //Boss和Worker
                //第一个group为Boss，只处理ServerSocketChannel的accept事件
                //第二个group为Worker，只负责SocketChannel上的读写事件
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //自定义处理逻辑
                        ch.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter(){
                            //处理读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //此时msg应该是ByteBuf类型
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("Handler1:{}", buf.toString(StandardCharsets.UTF_8));

                                //将消息传给下一个Handler
                                ctx.fireChannelRead(msg);
                            }

                            //参数一：最外边自己指定的group，参数二：给线程起的名字
                        }).addLast(group, "handler2", new ChannelInboundHandlerAdapter(){
                            //处理读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //此时msg应该是ByteBuf类型
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("Handler2:{}", buf.toString(StandardCharsets.UTF_8));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
