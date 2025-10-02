package com.es.core.model.phone.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityClassConvert {
    public static Map<String, Object> convertEntityToMap(Object entity) {
        Map<String, Object> parameters = new HashMap<>();
        Class<?> clazz = entity.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                parameters.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return parameters;
    }
}
