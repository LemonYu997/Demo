package com.example.demo.netty1;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author mly
 * @date 2022/11/8 11:57
 */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        //得到系统的核心线程数
//        System.out.println(NettyRuntime.availableProcessors()); //12

        //1、创建事件循环组
        //没有指定线程数时，会取系统的核心线程数*2作为线程数量，没取到就1个
        //也可以手动指定线程数
        EventLoopGroup group = new NioEventLoopGroup(2);     //可以处理IO事件、普通任务、定时任务
//        EventLoopGroup group = new DefaultEventLoopGroup(); //可以处理普通任务、定时任务
        //2、获取下一个事件循环对象
        //这里只设置了2个线程，所以会循环执行
        log.info("{}", group.next());   //io.netty.channel.nio.NioEventLoop@566776ad
        log.info("{}", group.next());   //io.netty.channel.nio.NioEventLoop@566776ad
        log.info("{}", group.next());   //io.netty.channel.nio.NioEventLoop@566776ad

        //3、执行普通任务
        //主线程先执行结束，group中的子线程才执行
        group.next().submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.debug("Group OK");
        });

        //4、执行定时任务
        //参数一：定时任务
        //参数二：初始延时时间，第一次执行在多长时间后开始
        //参数三：间隔时间，多长时间执行一次
        //参数四：时间单位
        group.next().scheduleAtFixedRate(() -> {
            log.info("Schedule OK");
        }, 0, 1, TimeUnit.SECONDS);

        log.info("Main OK");
    }
}
