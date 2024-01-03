package cn.lz.data.provider.l;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String text = "撒是的绝杀机会：（一）你好我是大。撒低级八分。（二）啥的哈哈。说的啥。暗红色的比较舒服大家，书法家。东方。分段锁。（三）是你撒都。";
        String pattern = "（[^）]+）";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);
        while (m.find()) {
            System.out.println("Match: " + m.group(0));
        }
    }
}