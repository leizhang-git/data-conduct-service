package cn.lz.data.consumer.util;

import cn.lz.data.consumer.context.IContextInfoProxy;

import java.util.Objects;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/4 17:43
 */
public class ContextUtil {

    public static String getLogin() {
        Object login = IContextInfoProxy.getInstance().getContextAttribute("login");
        if(Objects.nonNull(login)) {
            return (String) login;
        }
        return null;
    }

    public static String getName() {
        Object name = IContextInfoProxy.getInstance().getContextAttribute("name");
        if(Objects.nonNull(name)) {
            return (String) name;
        }
        return null;
    }
}
