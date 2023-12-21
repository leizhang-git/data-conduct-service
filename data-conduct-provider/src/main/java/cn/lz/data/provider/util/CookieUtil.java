package cn.lz.data.provider.util;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:31
 * @Desc
 */
public class CookieUtil {

    /**
     * 获取Cookie
     * @param cookieKey
     * @param request
     * @return
     */
    public static Map<String, String> getCookieMap(String cookieKey, HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        if(StrUtil.isBlank(cookieKey)) {
            return result;
        }
        if(null == request) {
            return result;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieKey.equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    result.put(cookieKey,cookieValue);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取Cookie
     * @param request
     * @param key
     * @return
     */
    public static String get(HttpServletRequest request, String key) {
        String ticket = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    ticket = cookie.getValue();
                }
            }
        }
        return ticket;
    }

    /**
     * 设置Cookie
     * @param response
     * @param key
     * @param value
     */
    public static void set(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        set(response, cookie);
    }

    /**
     * 设置cookie
     * @param response
     * @param cookie
     */
    public static void set(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    /**
     * 删除Cookie
     * @param response
     * @param key
     */
    public static void remove(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, "");
        cookie.setMaxAge(0);
        remove(response, cookie);
    }

    /**
     * 删除Cookie
     * @param response
     * @param cookie
     */
    public static void remove(HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
