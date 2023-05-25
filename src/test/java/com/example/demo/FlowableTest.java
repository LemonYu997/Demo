package com.example.demo;

import com.example.demo.DemoApplication;
import org.flowable.engine.ProcessEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author mly
 * @date 2023/2/16 16:08
 */
@SpringBootTest()
@RunWith(SpringRunner.class)
public class FlowableTest {
    @Autowired
    private ProcessEngine processEngine;

    @Test
    public void contextLoads() {
        System.out.println("processEngine = " + processEngine);
    }
}
