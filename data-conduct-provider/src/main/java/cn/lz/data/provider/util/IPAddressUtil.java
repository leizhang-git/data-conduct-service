package cn.lz.data.provider.util;

import cn.hutool.core.util.StrUtil;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:40
 * @Desc IP地址工具类
 */
public class IPAddressUtil {

    private static final Logger log = LoggerFactory.getLogger(IPAddressUtil.class);

    /**
     * 默认是内存搜索算法，经测试，读取-关闭文件的速度 BTREE>BINARY>>MEMORY, 而使用不关闭资源的方式 MEMORY>BTREE>BINARY
     */
    static final int ALGORITHM = DbSearcher.MEMORY_ALGORITYM;

    /**
     * 预加载DbSearcher
     */
    static DbSearcher searcher;

    /**
     * 获取ip城市
     *
     * @param ip 地址
     * @return ip城市 例如山西/太原
     * @author fengshuonan
     * @Date 2019-09-27 19:34
     */
    public static String getCityInfo(String ip) {
        return getCityInfo(ip, ALGORITHM);
    }

    public static String getCityInfo(String ip, int algorithm) {
        String cityInfo;
        try {
            cityInfo = getIpAddress(ip, algorithm);
        } catch (Exception e) {
            log.error("解析登录IP地址异常！", e);
            cityInfo = "未找到";
        }
        return cityInfo;
    }

    public static String getIpAddress(String ip, int algorithm) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getSearcherMethod(algorithm);
        if (!Util.isIpAddress(ip)) {
            log.error("Error: 无效的IP地址！");
        }
        DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);
        String region = dataBlock.getRegion();
        if (StrUtil.isEmpty(region)) {
            return "未找到";
        }
        if (region.contains("内网IP")) {
            return "内网IP";
        }
        String[] split = region.split("\\|");
        if (split.length > 2) {
            return split[2] + "/" + split[3];
        }
        return region;
    }

    public static Method getSearcherMethod(int algorithm) throws NoSuchMethodException {
        Class<DbSearcher> dbSearcherClass = DbSearcher.class;
        Method method = null;
        switch (algorithm) {
            case DbSearcher.BTREE_ALGORITHM:
                method = dbSearcherClass.getMethod("btreeSearch", String.class);
                break;
            case DbSearcher.BINARY_ALGORITHM:
                method = dbSearcherClass.getMethod("binarySearch", String.class);
                break;
            case DbSearcher.MEMORY_ALGORITYM:
                method = dbSearcherClass.getMethod("memorySearch", String.class);
                break;
        }
        return method;
    }

    /**
     * 获取IP地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                assert inet != null;
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 判断是否为内网IP
     * @param addr
     * @return
     */
    public static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];

        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;

        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;

        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                if (b1 == SECTION_6) {
                    return true;
                }
            default:
                return false;
        }
    }
}
