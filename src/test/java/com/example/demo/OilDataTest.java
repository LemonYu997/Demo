package com.example.demo;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.HexUtil;
import com.example.demo.test.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mly
 * @date 2023/2/20 16:20
 */
@Slf4j
public class OilDataTest {
    @Test
    public void test01() {
        Integer startDeviceTaxNum = 110;
        Integer endDeviceTaxNum = 220;

        DecimalFormat decimalFormat = new DecimalFormat("0000");
        Long start = Long.parseLong(DateUtil.year(DateUtil.date()) + decimalFormat.format(startDeviceTaxNum));
        Long end = Long.parseLong(DateUtil.year(DateUtil.date()) + decimalFormat.format(endDeviceTaxNum));

        System.out.println(start);
        System.out.println(end);
    }

    @Test
    public void test02() {
        byte a = 0x4d;
        String s1 = Convert.toStr(a & 0xFF);
        String s2 = Convert.toStr(a & 0x7F);
        Integer i1 = Convert.toInt(a & 0x7F);
        System.out.println(i1);
    }

    @Test
    public void test03() {
        BigDecimal a = new BigDecimal("3.56");

        System.out.println(Convert.toInt(a));
    }

    @Test
    public void test04() {
        byte[] a = {0x4d, 0x3d, 0x2d, 0x1d};
        System.out.println(HexUtil.encodeHexStr(a));
    }

    @Test
    public void test05() {
        // 拼接时间
        Calendar calendar = Calendar.getInstance();
        // & 0xFF 保证了二进制补码的一致性 ，因为在byte转int的时候最高位会补1
        //年
        calendar.set(Calendar.YEAR, ((byte) 0x17 & 0xFF) + 2000);
        //月
        calendar.set(Calendar.MONTH, (((byte) 0x02 & 0xFF) - 1));
        //日
        calendar.set(Calendar.DAY_OF_MONTH, (byte) 0x20 & 0xFF);
        //时
        calendar.set(Calendar.HOUR_OF_DAY, (byte) 0x0b & 0xFF);
        //分
        calendar.set(Calendar.MINUTE, (byte) 0x1d & 0xFF);
        //秒
        calendar.set(Calendar.SECOND, (byte) 0x19 & 0xFF);
        //毫秒不记录
        calendar.set(Calendar.MILLISECOND, 0);

        System.out.println(calendar.getTime());
    }

    @Test
    public void test06() {
        String dateStr = "2017-03";
        Date date = DateUtil.parse(dateStr, DatePattern.NORM_MONTH_FORMAT);

        System.out.println(DateUtil.beginOfMonth(date));
    }

    @Test
    public void test07() {
        String dateStr = "2017-03";
        Date date = DateUtil.parse(dateStr, DatePattern.NORM_MONTH_FORMAT);


        System.out.println(DateUtil.beginOfQuarter(date));
        System.out.println(DateUtil.endOfQuarter(date));
    }

    @Test
    public void test08() {
        String dateStr = "2017";
        Date date = DateUtil.parse(dateStr, DatePattern.NORM_YEAR_PATTERN);
        Date begin = DateUtil.beginOfYear(date);

        System.out.println(date);
        System.out.println(DateUtil.offsetMonth(begin, -12));
        System.out.println(DateUtil.endOfYear(date));
    }

    @Test
    public void test09() {
        String startTime = "2022";
        String endTime = "2023";
        String timeType = "3";
        List<String> result = new ArrayList<>();

        if (timeType.equals("0")) {
            Date startDate = DateUtil.beginOfDay(DateUtil.parse(startTime));
            //开始时间应该往前一个周期，用来计算增幅
            startDate = DateUtil.offsetDay(startDate, -1);
            Date endDate = DateUtil.endOfDay(DateUtil.parse(endTime));

            while (startDate.getTime() < endDate.getTime()) {
                result.add(DateUtil.format(startDate, DatePattern.NORM_DATE_FORMAT));
                startDate = DateUtil.offsetDay(startDate, 1);
            }
        }

        if (timeType.equals("1")) {
            Date startDate = DateUtil.beginOfMonth(DateUtil.parse(startTime, DatePattern.NORM_MONTH_FORMAT));
            startDate = DateUtil.offsetMonth(startDate, -1);
            Date endDate = DateUtil.endOfMonth(DateUtil.parse(endTime, DatePattern.NORM_MONTH_FORMAT));

            while (startDate.getTime() < endDate.getTime()) {
                result.add(DateUtil.format(startDate, DatePattern.NORM_MONTH_FORMAT));
                startDate = DateUtil.offsetMonth(startDate, 1);
            }

        }

        if (timeType.equals("2")) {
            startTime = getMonthByQuarter(startTime);
            Date startDate = DateUtil.beginOfQuarter(DateUtil.parse(startTime, DatePattern.NORM_MONTH_FORMAT));
            startDate = DateUtil.offsetMonth(startDate, -3);
            endTime = getMonthByQuarter(endTime);
            Date endDate = DateUtil.endOfQuarter(DateUtil.parse(endTime, DatePattern.NORM_MONTH_FORMAT));

            while (startDate.getTime() < endDate.getTime()) {
                result.add(DateUtil.year(startDate) + "-" + DateUtil.quarter(startDate));
                startDate = DateUtil.offsetMonth(startDate, 3);
            }
        }

        if (timeType.equals("3")) {
            Date startDate = DateUtil.parse(startTime, DatePattern.NORM_YEAR_PATTERN);
            startDate = DateUtil.offsetMonth(startDate, -12);
            Date endDate = DateUtil.parse(endTime, DatePattern.NORM_YEAR_PATTERN);

            while (DateUtil.year(startDate) <= DateUtil.year(endDate)) {
                result.add(DateUtil.format(startDate, DatePattern.NORM_YEAR_PATTERN));
                startDate = DateUtil.offsetMonth(startDate, 12);
            }
        }

        System.out.println(result);
    }

    /**
     * 将2022-03季度转为2022-09月
     */
    public String getMonthByQuarter(String timeStr) {
        String[] timeArr = timeStr.split("-");
        int year = Integer.parseInt(timeArr[0]);
        int quarter = Integer.parseInt(timeArr[1]);

        int month = quarter * 3;

        return year + "-" + month;
    }

    @Test
    public void test10() {
        String birthday1 = "2022-03-03";
        String birthday2 = "2022-03-04";
        Date date1 = DateUtil.parse(birthday1);
        Date date2 = DateUtil.parse(birthday2);

        List<Student> list = new ArrayList<>();
//        list.add(new Student("张三", new BigDecimal("1"), date1));
//        list.add(new Student("李四", new BigDecimal("2"), date2));
//        list.add(new Student("王五", new BigDecimal("3"), date1));

//        Map<String, List<Student>> map = list
//                .stream().collect(Collectors.groupingBy(o -> DateUtil.format(o.getBirthday(), DatePattern.NORM_MONTH_FORMAT)));
//
//        for (Map.Entry<String, List<Student>> stringListEntry : map.entrySet()) {
//            System.out.println(stringListEntry.getKey());
//            System.out.println(stringListEntry.getValue());
//        }

        System.out.println(list.stream().map(Student::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
    }


    @Test
    public void test11() {
        Date yesterday = DateUtil.yesterday();
        Date beginDate = DateUtil.beginOfMonth(yesterday);
        Date endDate = DateUtil.endOfMonth(yesterday);

        System.out.println(beginDate);
        System.out.println(endDate);
    }

    @Test
    public void test12() {
        int a = 1;
        int b = 2;
        int c = 0;

        if (a != 0) {
            c = a;
        } else if (b != 0) {
            c = b;
        }

        System.out.println(c);
    }

    @Test
    public void test13() {
        Map<BigDecimal, Integer> map = new HashMap<>();
        map.put(BigDecimal.valueOf(2.2), 1);
        map.put(BigDecimal.valueOf(3.3), 2);
        map.put(BigDecimal.valueOf(4.4), 3);

        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();

        for (Map.Entry<BigDecimal, Integer> entry : map.entrySet()) {
            sb1.append(entry.getValue() + ",");
            sb2.append(entry.getKey() + ",");
        }

        System.out.println(sb1);
        System.out.println(sb2);
    }

    @Test
    public void test14() {
        BigDecimal a  = BigDecimal.valueOf(13.45);

        System.out.println(a.setScale(BigDecimal.ROUND_DOWN,BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void test15() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(5);
        list.add(3);
        list.add(6);
        list.add(6);

        //去重
        for (int i = 0; i < list.size(); i++) {
            int now = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                Integer target = list.get(j);
                if (target == now) {
                    list.remove(j);
                }
            }
        }

        System.out.println(list);
    }

    @Test
    public void test16() {
        BigDecimal a = BigDecimal.valueOf(2);
        BigDecimal b = BigDecimal.valueOf(1.5);

        System.out.println(a.compareTo(b));
    }

    @Test
    public void test17() {
        String jobParam = "2023-04-20, 2023-04-22, 2";
        List<String> params = new ArrayList<>(Arrays.asList(jobParam.split(",")));

        //默认时间都为昨天
        Date startDate = DateUtil.beginOfDay(DateUtil.yesterday());
        Date endDate = DateUtil.beginOfDay(DateUtil.yesterday());
        List<Long> stationIds = new ArrayList<>();

        if (params.size() >= 3) {
            log.info("计算开始时间入参：" + params.get(0));
            log.info("计算结束时间入参：" + params.get(1));

            if (!"null".equals(params.get(0).trim()) && !"null".equals(params.get(1).trim())) {
                startDate = DateUtil.beginOfDay(DateUtil.parseDate(params.get(0).trim()));
                endDate = DateUtil.beginOfDay(DateUtil.parseDate(params.get(1).trim()));
            }

            if (!"null".equals(params.get(2).trim())) {
                params.remove(0);
                params.remove(0);
                stationIds = params.stream().map(o -> Long.parseLong(o.trim())).collect(Collectors.toList());
            }

            log.info("要计算的加油站id：" + stationIds);
        }

        //根据传入的时间进行价格计算
        while (startDate.compareTo(endDate) <= 0) {
            log.info("当前正在计算的日期:" + startDate);

            startDate = DateUtil.offsetDay(startDate, 1);
        }
    }

    @Test
    public void test18() {
        List<BigDecimal> priceList = new ArrayList<>();
        priceList.add(new BigDecimal("7.22"));
        priceList.add(new BigDecimal("8.22"));
        BigDecimal tempPrice = new BigDecimal("7.1");

        BigDecimal close = priceList.get(0);
        BigDecimal closeDiff = close.subtract(tempPrice).abs();
        for (BigDecimal temp : priceList) {
            BigDecimal tempDiff = temp.subtract(tempPrice).abs();
            if (tempDiff.compareTo(closeDiff) < 0) {
                close = temp;
                closeDiff = tempDiff;
            }
        }

        System.out.println(close);
    }
}
