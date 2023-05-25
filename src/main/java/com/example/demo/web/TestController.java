package com.example.demo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mly
 * @date 2022/10/28 10:45
 */
@RestController
public class TestController {
    @RequestMapping("/test")
    public Integer test(Integer a) {

        return a;
    }
}
