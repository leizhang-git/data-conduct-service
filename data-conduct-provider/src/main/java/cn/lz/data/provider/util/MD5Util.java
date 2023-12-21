package cn.lz.data.provider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:42
 * @Desc
 */
public class MD5Util {

    private static Logger log = LoggerFactory.getLogger(MD5Util.class);

    /**
     * MD5加密
     * @param inStr password
     * @return MD5串
     */
    public static String stringMD5(String inStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            log.info("",e);
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        return new String(a);
    }

    //主测试类
    public static void main(String[] args) {
        String password = "admin";
        System.out.println("原始：" + password);
        System.out.println("MD5后：" + stringMD5(password));
        System.out.println("加密的：" + convertMD5(stringMD5(password)));
        System.out.println("解密的：" + convertMD5(convertMD5(stringMD5(password))));
    }
}
