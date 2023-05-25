package com.example.demo.fork;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * 类(接口)描述:
 * @author xnn
 * 2018年10月23日下午10:36:44
 */
public class TestForkJion extends RecursiveTask<Long> {

    private static final long serialVersionUID = 1L;
    //起始值
    private long start;
    //末尾值
    private long end;
    //分割的阈值
    private static final long THRESHOLD=10000;

    /**
     * @param start
     * @param end
     */
    public TestForkJion(long start, long end) {
        super();
        this.start = start;
        this.end = end;
    }

    /**
     * @return the start
     */
    public long getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public long getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(long end) {
        this.end = end;
    }
    /**
     * 计算start到end的累加和
     */
    @Override
    protected Long compute() {
        //分割的长度
        long length = end - start;
        //分割的长度小于等于阈值时,不再拆分 直接计算start到end之间对的和，然后进行返回
        if(length<=THRESHOLD) {
            long sum = 0;
            for (long  i = start; i <=end; i++) {
                sum+=i;
            }
            return sum;
        }

        //否则就继续进行拆分，
        else {
            //取出中间值
            long middle = (start+end)/2;

            //构造查分后左侧数据的任务类
            TestForkJion left  = new TestForkJion(start,middle);

            //拆分子任务，同时压入线程队列
            left.fork();

            //构造查分后右侧数据的任务类
            TestForkJion right = new TestForkJion(middle+1, end);

            //拆分子任务，同时压入线程队列
            right.fork();

            //进行小任务间的join汇总，并返回结果
            return left.join()+right.join();
        }
    }
}

