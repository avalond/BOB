package com.ywhyw.bob.compat;

import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by kevin@BizToolShop Inc.
 */
public class CompatCompatUtils {
    private static final String TAG = CompatCompatUtils.class.getSimpleName();

    public static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> targetClass, String name, Class<?>... parameterTypes) {
        if (targetClass == null || TextUtils.isEmpty(name))
            return null;
        try {
            return targetClass.getMethod(name, parameterTypes);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(Class<?> targetClass, String name) {
        if (targetClass == null || TextUtils.isEmpty(name))
            return null;
        try {
            return targetClass.getField(name);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Constructor<?> getConstructor(Class<?> targetClass, Class<?>... types) {
        if (targetClass == null || types == null)
            return null;
        try {
            return targetClass.getConstructor(types);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object newInstance(Constructor<?> constructor, Object... args) {
        if (constructor == null)
            return null;
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            Log.e(TAG, "Exception in newInstance: " + e.getClass().getSimpleName());
        }
        return null;
    }

    public static Object invoke(Object receiver, Object defaultValue, Method method, Object... args) {
        if (method == null)
            return defaultValue;
        try {
            return method.invoke(receiver, args);
        } catch (Exception e) {
            Log.e(TAG, "Exception in invoke: " + e.getClass().getSimpleName());
        }
        return defaultValue;
    }

    public static Object getFieldValue(Object receiver, Object defaultValue, Field field) {
        if (field == null)
            return defaultValue;
        try {
            return field.get(receiver);
        } catch (Exception e) {
            Log.e(TAG, "Exception in getFieldValue: " + e.getClass().getSimpleName());
        }
        return defaultValue;
    }

    public static void setFieldValue(Object receiver, Field field, Object value) {
        if (field == null)
            return;
        try {
            field.set(receiver, value);
        } catch (Exception e) {
            Log.e(TAG, "Exception in setFieldValue: " + e.getClass().getSimpleName());
        }
    }

    private CompatCompatUtils() {
        // This class is non-instantiable.
    }
}
