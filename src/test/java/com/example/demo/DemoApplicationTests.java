package com.example.demo;

import com.example.demo.util.EncodeUtil;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

//@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        //如果采集时间比当前时间小超过30天不处理该条数据
//        long diffDay = (System.currentTimeMillis() - time.getTime()) / (24 * 3600 * 1000);
//        if (diffDay > 30) {
//            logger.info("oilinfo time is expire 30 days {}", info.toString());
//            logger.info("time is " + time.toString());
//            OilInfoShard oilInfoShard = new OilInfoShard();
//            BeanUtils.copyProperties(info, oilInfoShard);
//            oilInfoShard.setOilFlow(OilRedis.aesEncrypt(info.getOilFlow(), MYSQL_ENTRYPT_KEY));
//            oilInfoShard.setOilSpeed(OilRedis.aesEncrypt(info.getOilSpeed(), MYSQL_ENTRYPT_KEY));
//            //因为是超过30天的数据,不知当时油价 油价流速流量 不做处理
//            //入库err
//            oilInfoShard.setTime(time);
//            oilInfoMapper.insertErr(oilInfoShard);
//            return true;
    }

    @Test
    void getPassword() {
        String password = "Augz753862_!34";
        String code = EncodeUtil.MD5EncodeUtf8(password);
        System.out.println(code);
    }

    @Test
    void test01() {
        BigDecimal limitValue = new BigDecimal(5.5);
        BigDecimal PRICE_LOW = new BigDecimal(5);
        BigDecimal PRICE_HIGH = new BigDecimal(9);
        //浮动价格上限
        BigDecimal floatRangeUp = new BigDecimal(3);
        //浮动价格下限
        BigDecimal floatRangeDown = new BigDecimal(2.5);
        //标准价格浮动下限
        BigDecimal floatRangeDownStandPrice = new BigDecimal(2.5);


        System.out.println(limitValue.subtract(floatRangeDown));

        BigDecimal roundingPrecisionUp = new BigDecimal(5).divide(new BigDecimal(10));
        System.out.println(roundingPrecisionUp);
    }

    @Test
    void test02() {
        List<Integer> tempList = new ArrayList<>();
        tempList.add(124);
        tempList.add(0);
        tempList.add(0);
        tempList.add(222);
        tempList.add(232);
        tempList.add(242);

        System.out.println(CarFlowCountAllAverageUtil(tempList));
    }

    /**
     * 清洗最近几天车流量数据  520, 533, 544, 644, 1200，会把644和1200清洗出去
     *
     * @param tempList
     * @return
     */
    public static List<Integer> CarFlowCountAllAverageUtil(List<Integer> tempList) {
        //过滤为0的数
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            //将不等于0的数组存储下来
            if (tempList.get(i) != 0) {
                result.add(tempList.get(i));
            }
        }
        tempList = result;

        //过滤位数
        Integer[] numberList = {0, 0, 0, 0, 0};
        for (Integer integer : tempList) {
            int length = integer.toString().length();
            if (length == 1) {
                numberList[0] += 1;
            } else if (length == 2) {
                numberList[1] += 1;
            } else if (length == 3) {
                numberList[2] += 1;
            } else if (length == 4) {
                numberList[3] += 1;
            } else if (length == 5) {
                numberList[4] += 1;
            }
        }
        Integer numberLength = 1;
        Integer temp = numberList[0];
        for (int i = 0; i < numberList.length; i++) {
            if (temp < numberList[i]) {
                temp = numberList[i];       //1
                numberLength = i + 1;         //2
            }
        }

        //理论上永远进不去
        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.get(i).toString().length() < numberLength) {
                tempList.remove(i);
                i--;
            }
        }

        //过滤起始值
        if (numberLength > 2) {
            //位数达到百位 才计算首位值
            String startNumber = "1";
            Integer[] startNumberList = {0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (Integer integer : tempList) {
                String startNumberIntegerString = integer.toString().substring(0, 1);
                Integer startNumberInteger = Integer.valueOf(startNumberIntegerString);
                if (startNumberInteger == 1) {
                    startNumberList[0] += 1;
                } else if (startNumberInteger == 2) {
                    startNumberList[1] += 1;
                } else if (startNumberInteger == 3) {
                    startNumberList[2] += 1;
                } else if (startNumberInteger == 4) {
                    startNumberList[3] += 1;
                } else if (startNumberInteger == 5) {
                    startNumberList[4] += 1;
                } else if (startNumberInteger == 6) {
                    startNumberList[5] += 1;
                } else if (startNumberInteger == 7) {
                    startNumberList[6] += 1;
                } else if (startNumberInteger == 8) {
                    startNumberList[7] += 1;
                } else if (startNumberInteger == 9) {
                    startNumberList[8] += 1;
                }
            }
            Integer numberStart = 1;
            Integer tempStart = startNumberList[0];
            for (int i = 0; i < startNumberList.length; i++) {
                if (tempStart < startNumberList[i]) {
                    tempStart = startNumberList[i];
                    numberStart = i + 1;
                }
            }
            for (int i = 0; i < tempList.size(); i++) {
                if (!Integer.valueOf(tempList.get(i).toString().substring(0, 1)).equals(numberStart)) {
                    tempList.remove(i);
                    i--;
                }
            }
        }
        return tempList;
    }

    @Test
    void test03() {
        System.out.println((int) (Math.random() * 10));
        System.out.println((int) (Math.random() * 10));
        System.out.println((int) (Math.random() * 10));
        System.out.println((int) (Math.random() * 10));
        System.out.println((int) (Math.random() * 10));
        System.out.println((int) (Math.random() * 10));
        System.out.println((int) (Math.random() * 10));
    }

    @Test
    void test04() {
        BigDecimal a = new BigDecimal(0.0);
        BigDecimal c = new BigDecimal(0.00);
        BigDecimal b = BigDecimal.valueOf(0.0);
        BigDecimal d = BigDecimal.valueOf(0.00);

        System.out.println(a.equals(BigDecimal.ZERO));
        System.out.println(b.equals(BigDecimal.ZERO));
        System.out.println(c.equals(BigDecimal.ZERO));
        System.out.println(d.equals(BigDecimal.ZERO));

        System.out.println(a.compareTo(BigDecimal.ZERO)!=0);
        System.out.println(b.compareTo(BigDecimal.ZERO)!=0);
        System.out.println(c.compareTo(BigDecimal.ZERO)!=0);
        System.out.println(d.compareTo(BigDecimal.ZERO)!=0);
    }

    @Test
    void test05() {

        BigInteger prime = BigInteger.probablePrime(5, new Random());

        System.out.println(prime);
    }


    @Test
    void test06() {
        System.out.println(getRandomRate("95-100"));
    }


    //得到输入范围内的一个随机小数
    public BigDecimal getRandomRate(String segment) {
        String[] split = segment.split("-");
        Double min = Double.parseDouble(split[0]);
        Double max = Double.parseDouble(split[1]);

        double result = Math.random() * (max - min) + min;
        result = Double.parseDouble(String.format("%.2f", result));

        return BigDecimal.valueOf(result);
    }

    @Test
    public void test07() {
        String s = "oil_info_2021_1";

        System.out.println(s.substring(9));
    }

    @Test
    public void test08() {
        Date date = tableName2Date("oil_info_2021_1");
        System.out.println(date.toString());
    }

    /**
     * 将tableName 如oil_info_2021_1 转为日期格式
     * 2022-1
     * */
    public Date tableName2Date(String tableName) {
        String[] strs = tableName.split("_");
//        int year = Integer.parseInt(strs[2]);      //2022
//        int month = Integer.parseInt(strs[3]);     //1

        String year = strs[2];      //2022
        String month = strs[3];     //1
        String dateStr = year + "-" + month + "-1";

        return DateUtil.parse(dateStr);
    }

    @Test
    public void test09() {
        String s = "     ";
        System.out.println(StringUtils.isNotBlank(s));
    }

//    @Test
//    public void test111() {
//        Long[] ids = {111L, 222L};
//        System.out.println(StrU);
//    }Arrays.toString(ids)


}
