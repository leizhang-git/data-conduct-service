package cn.lz.data.consumer.util;

import cn.hutool.core.map.MapUtil;
import cn.lz.data.consumer.exception.StrException;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.Optional;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/4 17:42
 */
public class ConstantDefinitionUtil {

    public static final String Q1 = "Q1";
    public static final String Q2 = "Q2";
    public static final String Q3 = "Q3";
    public static final String Q4 = "Q4";




    public static Map<String,String> getAllMacro(){
        return getStaticMap();
    }


    /**
     * 时间日期常量
     * @return
     */
    private static Map<String, String> getStaticMap() {
        Map<String, String> result = MapUtil.newConcurrentHashMap();
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //当前年
        String year = String.valueOf(localDate.getYear());
        result.put("当前年", year);
        //当前月
        String month = String.valueOf(localDate.getMonth().getValue());
        result.put("当前月", month);
        //上一年
        String lastYear = String.valueOf(localDate.minusYears(1).getYear());
        result.put("上一年", lastYear);
        //两年前
        String twoYearAgo = String.valueOf(localDate.minusYears(2).getYear());
        result.put("两年前", twoYearAgo);
        //三年前
        String threeYearAgo = String.valueOf(localDate.minusYears(3).getYear());
        result.put("三年前", threeYearAgo);
        //五年前
        String fineYearAgo = String.valueOf(localDate.minusYears(5).getYear());
        result.put("五年前", fineYearAgo);
        //年-月
        String yearMonth = year + "-" + month;
        result.put("当前年月", yearMonth);
        //上年同月
        String lastYearMonth = lastYear + "-" + month;
        result.put("上年同月", lastYearMonth);
        //上期年月
        String lastMonth = localDate.minusWeeks(1).getYear() + "-" + localDate.minusWeeks(1).getMonthValue();
        result.put("上期年月", lastMonth);
        //当前日
        String nowDay = String.valueOf(localDate.getDayOfMonth());
        result.put("当前日", nowDay);
        //年-月-日
        String yearMonthDay = yearMonth + "-" + nowDay;
        result.put("当前年月日", yearMonthDay);
        //当前所在季度
        String nowSeason = getQSeason(getSeason(localDate));
        result.put("当前季度", nowSeason);
        //当前年-季度
        String nowYearSeason = year + "-" + nowSeason;
        result.put("当前年季度", nowYearSeason);
        //昨天
        String yesterday = String.valueOf(localDate.minusDays(1));
        result.put("昨天", yesterday);
        //明天
        String tomorrow = String.valueOf(localDate.plusDays(1));
        result.put("明天", tomorrow);
        //两天前
        String twoDay = String.valueOf(localDate.minusDays(2));
        result.put("两天前", twoDay);
        //三天前
        String threeDay = String.valueOf(localDate.minusDays(3));
        result.put("三天前", threeDay);
        //四天前
        String fourDay = String.valueOf(localDate.minusDays(4));
        result.put("四天前", fourDay);
        //五天前
        String fiveDay = String.valueOf(localDate.minusDays(5));
        result.put("五天前", fiveDay);
        //六天前
        String sixDay = String.valueOf(localDate.minusDays(6));
        result.put("六天前", sixDay);
        //七天前
        String serverDay = String.valueOf(localDate.minusDays(7));
        result.put("七天前", serverDay);
        //十天前
        String tenDay = String.valueOf(localDate.minusDays(10));
        result.put("十天前", tenDay);
        //十五天前
        String fifteenDay = String.valueOf(localDate.minusDays(15));
        result.put("十五天前", fifteenDay);
        //二十天前
        String twentyDay = String.valueOf(localDate.minusDays(20));
        result.put("二十天前", twentyDay);
        //三十天前
        String thirtyDay = String.valueOf(localDate.minusDays(30));
        result.put("三十天前", thirtyDay);
        //本年一季度第一天
        String nowYearQ1FirstDay = getSeasonDate(localDate, 1)[0].with(TemporalAdjusters.firstDayOfMonth()).format(format);
        result.put("本年一季度第一天", nowYearQ1FirstDay);
        //本年一季度最后一天
        String nowYearQ1LastDay = getSeasonDate(localDate, 1)[2].with(TemporalAdjusters.lastDayOfMonth()).format(format);
        result.put("本年一季度最后一天", nowYearQ1LastDay);
        //本年二季度第一天
        String nowYearQ2FirstDay = getSeasonDate(localDate, 2)[0].with(TemporalAdjusters.firstDayOfMonth()).format(format);
        result.put("本年二季度第一天", nowYearQ2FirstDay);
        //本年二季度最后一天
        String nowYearQ2LastDay = getSeasonDate(localDate, 2)[2].with(TemporalAdjusters.lastDayOfMonth()).format(format);
        result.put("本年二季度最后一天", nowYearQ2LastDay);
        //本年三季度第一天
        String nowYearQ3FirstDay = getSeasonDate(localDate, 3)[0].with(TemporalAdjusters.firstDayOfMonth()).format(format);
        result.put("本年三季度第一天", nowYearQ3FirstDay);
        //本年三季度最后一天
        String nowYearQ3LastDay = getSeasonDate(localDate, 3)[2].with(TemporalAdjusters.lastDayOfMonth()).format(format);
        result.put("本年三季度最后一天", nowYearQ3LastDay);
        //本年四季度第一天
        String nowYearQ4FirstDay = getSeasonDate(localDate, 4)[0].with(TemporalAdjusters.firstDayOfMonth()).format(format);
        result.put("本年四季度第一天", nowYearQ4FirstDay);
        //本年四季度最后一天
        String nowYearQ4LastDay = getSeasonDate(localDate, 4)[2].with(TemporalAdjusters.lastDayOfMonth()).format(format);
        result.put("本年一季度最后一天", nowYearQ4LastDay);
        //上年一季度第一天
        String lastYearQ1FirstDay = getSeasonDate(localDate, 1)[0].minusYears(1).with(TemporalAdjusters.firstDayOfMonth()).format(format);
        result.put("上年一季度第一天", lastYearQ1FirstDay);
        //上年一季度最后一天
        String lastYearQ1LastDay = getSeasonDate(localDate, 1)[2].minusYears(1).with(TemporalAdjusters.lastDayOfMonth()).format(format);
        result.put("上年一季度最后一天", lastYearQ1LastDay);
        //上年二季度第一天
        String lastYearQ2FirstDay = getSeasonDate(localDate, 2)[0].minusYears(1).with(TemporalAdjusters.firstDayOfMonth()).format(format);
        result.put("上年二季度第一天", lastYearQ2FirstDay);
        //上年二季度最后一天
        String lastYearQ2LastDay = getSeasonDate(localDate, 2)[2].minusYears(1).with(TemporalAdjusters.lastDayOfMonth()).format(format);
        result.put("上年二季度最后一天", lastYearQ2LastDay);
        //上年三季度第一天
        String lastYearQ3FirstDay = getSeasonDate(localDate, 3)[0].minusYears(1).with(TemporalAdjusters.firstDayOfMonth()).format(format);
        result.put("上年三季度第一天", lastYearQ3FirstDay);
        //上年三季度最后一天
        String lastYearQ3LastDay = getSeasonDate(localDate, 3)[2].minusYears(1).with(TemporalAdjusters.lastDayOfMonth()).format(format);
        result.put("上年三季度最后一天", lastYearQ3LastDay);
        //上年四季度第一天
        String lastYearQ4FirstDay = getSeasonDate(localDate, 4)[0].minusYears(1).with(TemporalAdjusters.firstDayOfMonth()).format(format);
        result.put("上年四季度第一天", lastYearQ4FirstDay);
        //上年四季度最后一天
        String lastYearQ4LastDay = getSeasonDate(localDate, 4)[2].minusYears(1).with(TemporalAdjusters.lastDayOfMonth()).format(format);
        result.put("上年四季度最后一天", lastYearQ4LastDay);
        return result;
    }

    /**
     * 根据季度获取月份
     * @param localDate
     * @param seasonNum
     * @return
     */
    public static LocalDate[] getSeasonDate(LocalDate localDate, int seasonNum) {
        LocalDate[] season = new LocalDate[3];
        if(seasonNum == 1) {//第一季度
            season[0] = localDate.withMonth(Month.JANUARY.getValue());
            season[1] = localDate.withMonth(Month.FEBRUARY.getValue());
            season[2] = localDate.withMonth(Month.MARCH.getValue());
        }else if(seasonNum == 2) {//第二季度
            season[0] = localDate.withMonth(Month.APRIL.getValue());
            season[1] = localDate.withMonth(Month.MAY.getValue());
            season[2] = localDate.withMonth(Month.JUNE.getValue());
        }else if(seasonNum == 3) {//第三季度
            season[0] = localDate.withMonth(Month.JULY.getValue());
            season[1] = localDate.withMonth(Month.AUGUST.getValue());
            season[2] = localDate.withMonth(Month.SEPTEMBER.getValue());
        }else if(seasonNum == 4) {//第四季度
            season[0] = localDate.withMonth(Month.OCTOBER.getValue());
            season[1] = localDate.withMonth(Month.NOVEMBER.getValue());
            season[2] = localDate.withMonth(Month.DECEMBER.getValue());
        }
        return season;
    }

    /**
     * 默认根据季度获取月份
     * @param localDate
     * @return
     */
    public static LocalDate[] getLastSeasonDate(LocalDate localDate) {
        return Optional.of(localDate)
                .map(time -> {
                    LocalDate[] season = new LocalDate[3];

                    int nSeason = getSeason(time);
                    if (nSeason == 1) {// 第一季度
                        season[0] = time.withMonth(Month.JANUARY.getValue());
                        season[1] = time.withMonth(Month.FEBRUARY.getValue());
                        season[2] = time.withMonth(Month.MARCH.getValue());
                    } else if (nSeason == 2) {// 第二季度
                        season[0] = time.withMonth(Month.APRIL.getValue());
                        season[1] = time.withMonth(Month.MAY.getValue());
                        season[2] = time.withMonth(Month.JUNE.getValue());
                    } else if (nSeason == 3) {// 第三季度
                        season[0] = time.withMonth(Month.JULY.getValue());
                        season[1] = time.withMonth(Month.AUGUST.getValue());
                        season[2] = time.withMonth(Month.SEPTEMBER.getValue());
                    } else if (nSeason == 4) {// 第四季度
                        season[0] = time.withMonth(Month.OCTOBER.getValue());
                        season[1] = time.withMonth(Month.NOVEMBER.getValue());
                        season[2] = time.withMonth(Month.DECEMBER.getValue());
                    }
                    return season;
                })
                .orElse(null);
    }

    public static void main(String[] args) {
        LocalDate of = LocalDate.of(2023, 12, 2);
        LocalDate[] lastSeasonDate = getLastSeasonDate(of);
        for (LocalDate localDate : lastSeasonDate) {
            System.out.println(localDate);
        }
    }

    /**
     * 获取当前季度
     * @param localDate
     * @return
     */
    public static int getSeason(LocalDate localDate) {
        int season = 0;
        Month month = localDate.getMonth();
        switch (month) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
                season = 1;
                break;
            case APRIL:
            case MAY:
            case JUNE:
                season = 2;
                break;
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                season = 3;
                break;
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    public static String getQSeason(Integer seasonNum) {
        if(1 == seasonNum) {
            return Q1;
        }else if (2 == seasonNum) {
            return Q2;
        }else if (3 == seasonNum) {
            return Q3;
        }else if (4 == seasonNum) {
            return Q4;
        }else {
            throw new StrException("未知的季度~");
        }
    }
}
