package uk.co.jacekk.bukkit.skylandsplus.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils
{
    public static <T> T getFieldValue(Class<?> src, String name, Class<T> type, Object from) throws NoSuchFieldException{
        try {
            Field field = src.getDeclaredField(name);
            field.setAccessible(true);
            return type.cast(field.get(from));
        } catch (NoSuchFieldException e) {
           throw e;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setFieldValue(Class<?> src, String name, Object in, Object value)
            throws SecurityException, NoSuchFieldException
    {
        Field field = src.getDeclaredField(name);
        field.setAccessible(true);
        if (Modifier.isFinal(field.getModifiers()))
        {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            try
            {
                modifiers.set(field, field.getModifiers() & 0xFFFFFFEF);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            field.set(in, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static <T> T invokeMethod(Class<?> src, String name, Class<T> returnType, Object in, Class<?>[] args, Object[] params)
            throws SecurityException, NoSuchMethodException
    {
        Method method = src.getDeclaredMethod(name, args);
        method.setAccessible(true);
        try
        {
            return returnType.cast(method.invoke(in, params));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}