package van.planifolia.maid.core.model;


import com.alibaba.fastjson2.JSONObject;
import van.planifolia.maid.core.annotations.IgnoreParma;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 定义一个父类用于实现构建请求JSON与请求MAP
 * @Author: Planifolia.Van
 * @Date: 2024/12/6 10:14
 */
public class MaidBasicModel {

    /**
     * 将Model构建成请求JSON
     * @return 构建成的请求JSON
     */
    public JSONObject toRequestJSON(){
        JSONObject requestJSON = new JSONObject();
        Class<?> clazz = this.getClass();
        // 包括父类的字段
        while (clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                // 跳过标记了 @IgnoreParma 的字段
                if (declaredField.isAnnotationPresent(IgnoreParma.class)) {
                    continue;
                }
                String thisFieldName = declaredField.getName();
                Object thisFieldValue;
                try {
                    // 允许访问私有字段
                    declaredField.setAccessible(true);
                    thisFieldValue = declaredField.get(this);
                    requestJSON.put(thisFieldName, thisFieldValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            // 获取父类
            clazz = clazz.getSuperclass();
        }
        return requestJSON;
    }
    /**
     * 将Model构建成请求JSON
     * @return 构建成的请求JSON
     */
    public Map<String,String> toRequestMap(){
        Map<String,String> queryMap = new HashMap<>();
        Class<?> clazz = this.getClass();
        // 包括父类的字段
        while (clazz != null) {
            for (Field declaredField : clazz.getDeclaredFields()) {
                // 跳过标记了 @ExcludeFromSign 的字段
                if (declaredField.isAnnotationPresent(IgnoreParma.class)) {
                    continue;
                }
                String thisFieldName = declaredField.getName();
                Object thisFieldValue;
                try {
                    // 允许访问私有字段
                    declaredField.setAccessible(true);
                    thisFieldValue = declaredField.get(this);
                    if (thisFieldValue!=null)
                        queryMap.put(thisFieldName, thisFieldValue.toString());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            // 获取父类
            clazz = clazz.getSuperclass();
        }
        return queryMap;
    }
}
