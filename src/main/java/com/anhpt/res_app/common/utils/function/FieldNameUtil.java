package com.anhpt.res_app.common.utils.function;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public class FieldNameUtil {
    public static <T, R> String getFieldName(FieldRef<T, R> getter) {
        try {
            Method writeReplace = getter.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) writeReplace.invoke(getter);

            String methodName = lambda.getImplMethodName();
            if (methodName.startsWith("get")) {
                return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
            }
            return methodName;
        } catch (Exception e) {
            throw new RuntimeException("Không thể lấy tên field từ method reference", e);
        }
    }
}