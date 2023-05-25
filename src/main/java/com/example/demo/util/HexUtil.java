package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wang hao
 * @Description:
 * @Date Create in 2018/6/12 15:42
 */
@Slf4j
public class HexUtil {

    public static short byteToShort(byte[] data, int offset) {
        return (short) ((((data[offset] << 8) & 0xFF00) | (data[offset + 1] & 0xFF)) & 0xFFFF);
    }

    public static String getNullValue(int count) {
        String lngNullValue = "";
        byte valueNull = 0;
        for (int i = 0; i < count; i++) {
            lngNullValue += HexUtil.convertHexToString(HexUtil.byteToStr(valueNull));
        }
        return lngNullValue;
    }

    public static String bytesToHexString(int length, byte[] bytes) {
        String BLANK = " ";
        Integer ZERO = 0;
        boolean prefix = false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (prefix) {
                sb.append(BLANK);
            } else {
                prefix = true;
            }
            String hex = Integer.toHexString(bytes[i] & 0xFF).toUpperCase();
            if (hex.length() == 1) {
                sb.append(ZERO);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * @Description ASCII码和HEX相互转换工具
     */
    public static String stringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString((int) aChar));
        }
        return hex.toString();
    }

    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String output = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
        }
        return sb.toString();
    }

    public static int byte2Int(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }

    //将byte字节数据转换成字符串 拼接字节
    public static String byteToStr(byte[] data, int begin, int end) {
        String str = "";
        for (int i = begin; i < end; i++) {
            str += data[i] & 0xff;
        }
        return str;
    }

    //将byte字节数据转换成字符串 拼接字节 byteToStrDecimal十进制
    public static String byteToStrDecimal(byte[] data, int begin, int end) {
        String str = "";
        for (int i = begin; i < end; i++) {
            String temp = data[i] + "";
            str += Integer.toHexString(Integer.valueOf(temp)) + "";
        }
        return str;
    }

    //将10 转16 拼接
    public static String moreTypeOfInt(byte[] data, int begin, int end) {
        String str = "";
        for (int i = begin; i < end; i++) {
            String hex =Integer.toHexString(data[i] & 0xff);
            if(hex.length() < 2){
                hex = "0" + hex;
            }
            str += hex;
        }
        return str;
    }

    public static String byteToStrForAZ(byte[] data, int begin, int end) {
        String str = "";
        for (int i = begin; i < end; i++) {
            int dto = data[i] & 0xff;
            String res = ""+dto;
            if (res.length()>=2){
                res = ""+(char) dto;
            }
            str += res;
        }
        return str;
    }

    //data转码，将0x30到0x39转为0-9
    public static byte[] byteTobyte(byte[] data) {
        byte[] res = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            res[i] = data[i];
        }
        for (int i = 0; i < res.length; i++) {
            String str = res[i] + "";
            String patternA = "[89]";
            String patternB = "[01234567]";
            if (str.startsWith("4") && str.length() > 1) {
                if (str.substring(1, 2).matches(patternA)) {
                    String strA = "";
                    strA += (char) res[i];
                    res[i] = Byte.parseByte(strA);
                }
            } else if (str.startsWith("5") && str.length() > 1) {
                if (str.substring(1, 2).matches(patternB)) {
                    String strA = "";
                    strA += (char) res[i];
                    res[i] = Byte.parseByte(strA);
                }
            }
        }
        return res;
    }

    public static String byteToStr(byte data) {
        String str = "";
        str += data & 0xff;
        return str;
    }

    //将十六进制字符串信息 转换成ascii图形返回字符串
    public static String convertHexToString(String hex) {
        return (char) Integer.parseInt(hex) + "";
    }

    //将十六位字节数据转换成asicc码对应的字符串
    public static String byteToAsiccStr(byte[] data, int begin, int end) {
        String str = "";
        for (int i = begin; i < end; i++) {
            str += (char) data[i];
        }
        return str;
    }

    //计算税控上报报文的异或校验值
    public static boolean isTaxDataValid(byte[] data) {
        //第5位为长度值，5位往后的长度减一个字节数据数据部分，5位往后的第长度位标识第6位开始到结尾前一位数据异或结果
        int length = data[4];
        int value = 0;
        for (int i = 5; i < 4 + length; i++) {
            value = value ^ data[i];
        }
        return value == data[4 + length] ? true : false;
    }

    //把十六进制字符转换成字节数据
    public static byte[] hexToBytes(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    public static int getStringByteLength(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255) {
                length++;
            } else {
                length += 2;
            }
        }
        return length;
    }
}
