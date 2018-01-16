package se.vgregion.arbetsplatskoder.util;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static Field getDeclaredField(String fieldName, Class<?> type) {
        try {
            Field declaredField = type.getDeclaredField(fieldName);

            return declaredField;
        } catch (NoSuchFieldException e) {
            if (type.getSuperclass() != null) {
                return getDeclaredField(fieldName, type.getSuperclass());
            } else {
                return null;
            }
        }
    }
}
