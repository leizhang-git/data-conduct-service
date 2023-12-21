package cn.lz.data.provider.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:31
 * @Desc
 */
public class DateUtil {
    public static void main(String[] args) throws ParseException {
        //===================================JDK1.8之前

        //Date：当前系统日期时间
        Date date = new Date();
        System.out.println("当前时间为：" + date);
        //获取1970年到目前的毫秒数
        long time = date.getTime();
        System.out.println("1970年截至目前的时间毫秒数：" + time);
        //2s后的时间
        Date twoAfterTime = new Date(date.getTime() + 2 * 1000);
        System.out.println("两秒后的时间为：" + twoAfterTime);

        //SimpleDateFormat格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        String format1 = simpleDateFormat.format(time);
        System.out.println(format);
        System.out.println(format1);

        //字符串解析时间
        String dateStr = "2023-05-05 11:11:11";
        Date parse = simpleDateFormat.parse(dateStr);
        System.out.println(parse);

        //得到此时的日历对象
        Calendar instance = Calendar.getInstance();
        System.out.println(instance);
        //获取日历中某个信息
        int year = instance.get(Calendar.YEAR);
        System.out.println("今年是：" + year + "年");

        //获取当前的日期对象
        Date instanceTime = instance.getTime();

        //获取时间毫秒值
        long timeInMillis = instance.getTimeInMillis();

        //修改日历信息
        instance.set(Calendar.YEAR, 2025);
        Date instanceTime1 = instance.getTime();
        System.out.println(instanceTime1);

        //加12小时
        instance.set(Calendar.HOUR_OF_DAY, 12);
        System.out.println(instance.getTime());



        //===================================JDK1.8
        LocalDateTime nowTime = LocalDateTime.now();
        System.out.println(nowTime);
        int year1 = nowTime.getYear();
        System.out.println("当前年是：" + year1);
        int monthValue = nowTime.getMonthValue();
        System.out.println("当前月是：" + monthValue);
        int dayOfMonth = nowTime.getDayOfMonth();
        System.out.println("当前日是：" + dayOfMonth);
        int dayOfYear = nowTime.getDayOfYear();
        System.out.println("当前是一年当中第几天：" + dayOfYear);
        int value = nowTime.getDayOfWeek().getValue();
        System.out.println("当前星期几：" + value);

        //修改月份,不会改变之前的值
        LocalDateTime localDateTime = nowTime.withMonth(12);
        System.out.println(localDateTime);

        //将年份增加2年
        LocalDateTime localDateTime1 = nowTime.plusYears(2);
        System.out.println(localDateTime1);

        //将日减少20日
        LocalDateTime localDateTime2 = nowTime.minusDays(20);
        System.out.println(localDateTime2);

        //获取指定local对象
        LocalDate of = LocalDate.of(2023, 12, 12);
        LocalDate of1 = LocalDate.of(2023, 12, 12);
        System.out.println(of);

        //判断日期排序
        System.out.println(of.equals(of1));
        System.out.println(of.isAfter(of1));
        System.out.println(of.isBefore(of1));

        LocalDate ofDate = LocalDate.of(2023, 12, 12);
        LocalTime ofTime = LocalTime.of(22, 12, 12);
        LocalDateTime of2 = LocalDateTime.of(ofDate, ofTime);
        System.out.println(of2);


        //获取系统默认时区
        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println("当前系统的默认时区是：" + zoneId);
        //获取Java支持的所有时区
        Set<String> ids = ZoneId.getAvailableZoneIds();
        System.out.println(ids);
        //封装
        ZoneId of3 = ZoneId.of("Asia/Kuching");
        //获取马来西亚时间
        ZonedDateTime now = ZonedDateTime.now(of3);
        System.out.println(now);
        //获取世界标准时间(和中国时间差8小时)
        System.out.println(ZonedDateTime.now(Clock.systemUTC()));

        //系统默认时区时间
        ZonedDateTime now1 = ZonedDateTime.now();
        System.out.println(now1);

        //其余功能与LocalDateTime几乎相同，均是不可变对象
        //旧版的Calender也是支持时区的
        Calendar instance1 = Calendar.getInstance(TimeZone.getTimeZone(of3));
        System.out.println(instance1);

        Instant instant = Instant.now();
        System.out.println(instant);
        //获取总秒数
        long epochSecond = instant.getEpochSecond();
        System.out.println(epochSecond);
        //不够1s的纳秒数
        int nano = instant.getNano();
        System.out.println(nano);
        //加11纳秒
        Instant instant1 = instant.plusNanos(111);
        System.out.println(instant1);

        //创建日期时间格式化器对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //对时间进行格式化，输出
        String format2 = formatter.format(LocalDateTime.now());
        System.out.println(format2);

        //还有一种格式化方式(逆向格式化)
        String format3 = LocalDateTime.now().format(formatter);
        System.out.println(format3);

        //解析时间(一般使用LocalDateTime),此处可能报错
        String dateStr4 = "2023年12月1日 12:11:02";
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy年MM月d日 HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr4, formatter1);
        System.out.println(dateTime);

        //Period：计算一段时期
        LocalDate of5 = LocalDate.of(2029, 8, 10);
        LocalDate of16 = LocalDate.of(2029, 8, 15);
        Period between = Period.between(of5, of16);
        //获取相差的天数
        System.out.println(between.getDays());

        //Duration：持续时间
        LocalDateTime l = LocalDateTime.of(2025, 11, 11, 12, 8, 10);
        LocalDateTime l1 = LocalDateTime.of(2025, 11, 10, 11, 8, 15);
        Duration between1 = Duration.between(l1, l);
        System.out.println(between1.toDays());
        System.out.println(between1.toHours());
        System.out.println(between1.toMinutes());
        System.out.println(between1.toMillis());
        System.out.println(between1.toNanos());
    }
}
