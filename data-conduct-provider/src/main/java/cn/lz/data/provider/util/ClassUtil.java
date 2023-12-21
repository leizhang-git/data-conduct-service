package cn.lz.data.provider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author zhanglei
 * @Date 2023/12/21 14:30
 * @Desc
 */
public class ClassUtil {

    private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取Class对象信息
     * @param obj
     * @return
     */
    public static Class<?> getClass(Object obj) {
        return obj.getClass();
    }

    /**
     * 获取Class对象信息
     * @param classPath
     * @return
     */
    public static Class<?> getClass(String classPath) {
        Class<?> cls = null;
        try {
            cls = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            log.error("class not found.", e);
        }
        return cls;
    }

    /**
     * 获取方法信息
     * @param path
     * @return
     */
    public static Method[] getMethods(String path) {
        Class<?> aClass = getClass(path);
        if(aClass == null) {
            return null;
        }
        return aClass.getMethods();
    }

    /**
     * 获取成员属性信息
     * @param path
     * @return
     */
    public static Field[] getFields(String path) {
        Class<?> aClass = getClass(path);
        if(aClass == null) {
            return null;
        }
        return aClass.getFields();
    }

    /**
     * 获取构造方法信息
     * @param path
     * @return
     */
    public static Constructor[] getConstructors(String path) {
        Class<?> aClass = getClass(path);
        if(aClass == null) {
            return null;
        }
        return aClass.getConstructors();
    }

    /**
     * 创建对象
     * @param path
     * @return
     */
    public static Object getInstance(String path) {
        Class<?> aClass = getClass(path);
        if(aClass == null) {
            return null;
        }
        Object obj = new Object();
        try {
            obj = aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Instantiation/IllegalAccess error.", e);
        }
        return obj;
    }
}
