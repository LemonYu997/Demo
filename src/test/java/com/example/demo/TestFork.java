package com.example.demo;

import com.example.demo.fork.TestForkJion;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author mly
 * @date 2023/4/1 15:58
 */
public class TestFork {
    @Test
    public void test() {
        Instant start = Instant.now();

        //需要ForkJoinPool支持
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        //创建任务
        ForkJoinTask<Long> task = new TestForkJion(1, 10000000l);
        Long sum = forkJoinPool.invoke(task);
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());
    }

    @Test
    public void test1() {
        Instant start = Instant.now();

        long sum = 0;
        for (long i = 0; i <= 10000000l; i++) {
            sum+=i;
        }
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());
    }
}
