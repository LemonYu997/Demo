package com.example.demo.netty1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author mly
 * @date 2022/9/7 12:06
 */
public class HelloServer {
    public static void main(String[] args) {
        //1、启动器 负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2、NioEventLoopGroup表示的是BossEventLoop和WorkerEventLoop(Selector, thread)
                //EventLoop 就是由一个selector和thread组成
                .group(new NioEventLoopGroup())
                //3、选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //4、Boss负责处理连接 worker(child) 负责处理读写，决定了worker(child)能执行哪些操作(handler)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    //5、channel：代表和客户端进行数据读写的通道Initializer初始化，负责添加别的handler
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //6、添加具体handler
                        ch.pipeline().addLast(new StringDecoder()); //将传输的ByteBuf解码为字符串
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {  //自定义handler
                            //处理读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                }).bind(8080);
    }
}
