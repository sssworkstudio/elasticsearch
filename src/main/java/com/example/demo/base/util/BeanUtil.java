package com.example.demo.base.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: shenshanshan
 * @Date: 09:22 2019-12-10
 */
public class BeanUtil {


    /**
     * Map转换为Javabean
     *
     * @param mp
     * @param beanCls
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static <T, K, V> T mapToBean(Map<K, V> mp, Class<T> beanCls) throws Exception, IllegalArgumentException, InvocationTargetException {
        T t = null;
        try {

            BeanInfo beanInfo = Introspector.getBeanInfo(beanCls);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            t = beanCls.newInstance();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (mp.containsKey(key)) {
                    Object value = mp.get(key);
                    // Java中提供了用来访问某个属性的
                    Method setter = property.getWriteMethod();
                    // getter、setter方法
                    setter.invoke(t, value);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * Javabean转换为Map
     *
     * @param bean
     * @param mp
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     * @throws IllegalAccessException
     */
    public static <T, K, V> Map<String, Object> beanToMap(T bean, Map<String, Object> mp) throws Exception, IllegalAccessException {
        if (bean == null) {
            return null;
        }
        try {

            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value;
                    value = getter.invoke(bean);
                    mp.put(key, value);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mp;
    }



}
