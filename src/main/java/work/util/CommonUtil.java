package work.util;

import com.google.common.collect.Maps;
import work.domain.ErrorCode;
import work.domain.User;

import java.lang.reflect.Field;
import java.util.Map;

public class CommonUtil {
    private CommonUtil() {
    }

    public static ErrorCode checkPage(int pageNo, int pageSize, int maxPageSize, int maxSize) {
        if (pageNo <= 0 || pageSize <= 0) {
            return ErrorCode.PARAM_ERROR;
        }
        if (pageSize > maxPageSize) {
            return ErrorCode.PARAM_ERROR;
        }
        if (pageNo * pageSize > maxSize) {
            return ErrorCode.PARAM_ERROR;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 利用反射获取类里面的名称和值
     *
     * @param obj 对象
     * @return Map
     * @throws IllegalAccessException
     */
    public static Map<String, Object> object2Map(Object obj) throws IllegalAccessException {
        Map<String, Object> map = Maps.newHashMap();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    public static void main(String[] args) throws IllegalAccessException {
        User user = new User();
        user.setName("test");
        Map<String, Object> map = object2Map(user);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }


    }

}
