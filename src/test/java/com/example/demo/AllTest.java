package com.example.demo;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.example.demo.util.StreamUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author mly
 * @date 2023/3/28 18:03
 */
public class AllTest {
    @Test
    public void test01() {
        String dateS1 = "2023-03-28 00:00:00";
        String dateS2 = "2023-03-28 00:00:14";


        Date date1 = DateUtil.parse(dateS1);
        Date date2 = DateUtil.parse(dateS2);

        String s = DateUtil.formatBetween(DateUtil.betweenMs(date1, date2), BetweenFormatter.Level.MINUTE);

        System.out.println(s);
    }

    @Test
    public void test02() {
        List<Long> list1 = Arrays.asList(1L, 2L, 3L, 4L);
        List<Long> list2 = Arrays.asList(1L, 3L);

        List<Long> resutl = StreamUtils.filter(list1, e -> !list2.contains(e));

        System.out.println(resutl);
    }
}
