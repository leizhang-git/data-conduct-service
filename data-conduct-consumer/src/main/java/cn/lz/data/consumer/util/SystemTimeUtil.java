package cn.lz.data.consumer.util;

import cn.hutool.core.util.StrUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/4 17:43
 */
public class SystemTimeUtil {

    public static final DateTimeFormatter NOW_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    public SystemTimeUtil() {
    }

    public static String getCurrentTime() {
        return LocalDateTime.now().format(NOW_TIME);
    }

    public static Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public static String getTime(Instant time) {
        if(null == time) {
            return StrUtil.EMPTY;
        }
        return NOW_TIME.format(time);
    }
}
