package com.example.demo.aio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author mly
 * @date 2022/8/31 14:51
 */
@Slf4j
public class AioFileChannel {
    public static void main(String[] args) {
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.debug("read begin...");
            //异步操作 会开一个新的守护线程执行
            //守护线程：主线程结束，守护线程也结束
            //参数1 ByteBuffer
            //参数2 读取的起始位置
            //参数3 附件
            //参数4 回调对象
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                //read成功调用该方法
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    //result是读取字节的长度
                    log.debug("read completed...{}", result);
                    attachment.flip();
                    log.debug(String.valueOf(attachment));
                }

                //read失败该方法
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });

            log.debug("read end....");

            //防止主线程结束后导致守护线程也结束，没有执行完成
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
