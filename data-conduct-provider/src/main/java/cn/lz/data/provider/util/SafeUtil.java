package cn.lz.data.provider.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang.StringEscapeUtils;

import java.text.Normalizer;
import java.util.Arrays;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:44
 * @Desc 安全处理工具类
 */
public class SafeUtil {

    /**
     * Log Forging日志伪造 黑名单
     */
    public static final String[] LOG_STR = {"%0a", "%0A", "%0d", "%0D", "\r", "\n", "\t"};

    public static final String[] INJ_STR = {"'", "and", "exec", "insert", "select", "delete", "update", "count", "*", "%", "chr", "mid", "master", "truncate", "char", "declare", ";", "or", "-", "+", ","};

    public static String replace(String logs) {
        if (StrUtil.isNotEmpty(logs)) {
            String normalize = Normalizer.normalize(logs, Normalizer.Form.NFKC);
            for (String str : LOG_STR) {
                normalize = normalize.replace(str, "");
            }
            return normalize;
        }
        return logs;
    }

    /**
     * Xss过滤
     * @param str
     * @return
     */
    public static String xssFilter(String str){
        if(StrUtil.isNotEmpty(str)){
            return StringEscapeUtils.escapeHtml(StringEscapeUtils.escapeJavaScript(str));
        }else {
            return str;
        }
    }

    /**
     * 过滤SQL注入
     * @param str
     * @return
     */
    public static String encodeForSQL(String str){
        return StringEscapeUtils.escapeSql(str);
    }

    public static String[] replace(String[] str) {
        if (ArrayUtil.isNotEmpty(str)) {
            return Arrays.stream(str).map(SafeUtil::replace).toArray(String[]::new);
        }
        return str;
    }
}
