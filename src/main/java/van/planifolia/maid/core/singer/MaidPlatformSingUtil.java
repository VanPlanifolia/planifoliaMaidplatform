package van.planifolia.maid.core.singer;

import lombok.extern.slf4j.Slf4j;
import van.planifolia.maid.core.annotations.IgnoreSing;
import van.planifolia.maid.util.str.MD5Util;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @Description: 管家婆加签工具
 * @Author: Planifolia.Van
 * @Date: 2024/12/4 14:10
 */
@Slf4j
public class MaidPlatformSingUtil {

    /**
     * 管家婆查询接口加签器
     *
     * @param obj 查询参数信息
     * @return 加签后数据
     */
    public static String commSinger(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("对象不能为空");
        }

        StringBuilder sb = new StringBuilder();
        Class<?> clazz = obj.getClass();

        // 循环遍历父类字段
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();

            // 按字段名称排序
            Arrays.sort(fields, Comparator.comparing(Field::getName));

            for (Field field : fields) {
                // 允许访问私有字段
                field.setAccessible(true);

                // 跳过标记了 @ExcludeFromSign 的字段
                if (field.isAnnotationPresent(IgnoreSing.class)) {
                    continue;
                }

                try {
                    Object value = field.get(obj);
                    // 忽略值为 null 的字段
                    if (value != null) {
                        sb.append(field.getName()).append(value);
                    }
                } catch (IllegalAccessException e) {
                    log.info("访问字段失败！堆栈消息:{}", e.getMessage());
                    throw new RuntimeException("访问字段失败！");
                }
            }

            // 获取父类字段
            clazz = clazz.getSuperclass();
        }

        try {
            Field token = obj.getClass().getDeclaredField("endStr");
            // 允许访问私有字段
            token.setAccessible(true);

            String tokenStr = token.get(obj).toString();
            sb.append(tokenStr);
        } catch (NoSuchFieldException e) {
            log.error("被签名的方法必须有结尾字符串，错误消息:{}", e.getMessage());
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("被签名的方法结尾字符串不为空，错误消息:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * 做MD5加密操作
     * @param string 被加密字符串
     * @return 加密后结果
     */
    public static String doMd5(String string){
        log.info("加签前数据\n {}",string);
        String singedString = MD5Util.MD5Encode(string, "utf-8");
        log.info("加签后数据\n {}",singedString);
        return singedString;
    }
}
