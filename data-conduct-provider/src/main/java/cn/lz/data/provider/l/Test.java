package cn.lz.data.provider.l;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String text = "突出动向XXXXXX，近日XXX如何如何XXX，综述如下:一、人员XXXXXX。钱XX说发表XX信息，社会学家如何如何，巴拉巴拉小魔仙XXX。二、我问题。组织XXX，如何如何xxxOK55555。";

        // 正则表达式匹配以任意中文数字和顿号开始，直到换行符前的所有内容
        Pattern pattern = Pattern.compile("([一二三四五六七八九零]+)、([^二三四五六七八九十]+)\\。?");
        Pattern pattern1 = Pattern.compile("([一二三四五六七八九零]+)、([^。]+)\\。?");

        Matcher matcher = pattern.matcher(text);
        Matcher matcher1 = pattern1.matcher(text);
        while (matcher.find()) {
            System.out.println(matcher.group(2));
        }
        while (matcher1.find()) {
            System.out.println(matcher1.group(2));
        }
    }
}