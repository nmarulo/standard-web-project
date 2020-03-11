package red.softn.standard.common;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.util.*;

public class ReflectionUtils {
    
    private static final int MODIFIERS = Modifier.TRANSIENT | Modifier.STATIC;
    
    public static Class<?> getGenericSuperClass(Class<?> objectClass, int position) throws Exception {
        if (objectClass == null || !(objectClass.getGenericSuperclass() instanceof ParameterizedType)) {
            return null;
        }
        
        Type[] actualTypeArguments = ((ParameterizedType) objectClass.getGenericSuperclass()).getActualTypeArguments();
        
        if (position < 0 || position >= actualTypeArguments.length) {
            throw new Exception(String.format("No se puede obtener la clase según la posición \"%1$d\". La clase tiene declarado %2$d clase(s) genérica(s)", position, actualTypeArguments.length));
        }
        
        return (Class<?>) actualTypeArguments[position];
    }
    
    public static String getClassNameWithoutPackage(Class<?> clazz) {
        if (clazz == null) {
            return "";
        }
        
        String name = clazz.getName();
        
        if (StringUtils.containsNone(name, '.')) {
            return name;
        }
        
        String[] splitName = StringUtils.split(name, '.');
        ArrayUtils.reverse(splitName);
        
        return splitName[0];
    }
    
    public static <O> Map<String, Object> objectToMap(Class<?> clazz, O object) {
        Map<String, Object> objectMap         = new LinkedHashMap<>();
        Field[]             declaredFields    = clazz.getDeclaredFields();
        Object              returnValueMethod = null;
        
        for (Field declaredField : declaredFields) {
            if (excludeFieldDefault(declaredField)) {
                continue;
            }
            
            String methodName = "get" + StringUtils.capitalize(declaredField.getName());
            Method method     = ReflectionUtils.getMethod(clazz, methodName);
            
            if (object != null) {
                returnValueMethod = ReflectionUtils.invokeMethod(method, object);
            }
            
            objectMap.put(declaredField.getName(), returnValueMethod);
        }
        
        return objectMap;
    }
    
    public static boolean defaultConstructorIsAccessible(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                     .filter(value -> Modifier.isPublic(value.getModifiers()))
                     .anyMatch(value -> value.getParameterCount() == 0);
    }
    
    public static <T> T newInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        if (!defaultConstructorIsAccessible(clazz)) {
            throw new InstantiationException(String.format("El constructor, sin parámetros, de la clase [%1$s] no es accesible.", clazz.getName()));
        }
        
        return clazz.newInstance();
    }
    
    /**
     * Get Method by passing class and method name.
     */
    public static Method getMethod(Class<?> clazz, String methodName) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName()
                      .equals(methodName)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }
    
    /**
     * Invoke the specified {@link Method} against the supplied target object
     * with no arguments. The target object can be {@code null} when invoking a
     * static {@link Method}.
     * <p>
     * Thrown exceptions are handled via a call to
     * {@link #handleReflectionException}.
     *
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     *
     * @return the invocation result, if any
     *
     * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
     */
    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }
    
    /**
     * Invoke the specified {@link Method} against the supplied target object
     * with the supplied arguments. The target object can be {@code null} when
     * invoking a static {@link Method}.
     * <p>
     * Thrown exceptions are handled via a call to
     * {@link #handleReflectionException}.
     *
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @param args   the invocation arguments (may be {@code null})
     *
     * @return the invocation result, if any
     */
    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception ex) {
            handleReflectionException(ex);
        }
        throw new IllegalStateException("Should never get here");
    }
    
    /**
     * Handle the given reflection exception. Should only be called if no
     * checked exception is expected to be thrown by the target method.
     * <p>
     * Throws the underlying RuntimeException or Error in case of an
     * InvocationTargetException with such a root cause. Throws an
     * IllegalStateException with an appropriate message or
     * UndeclaredThrowableException otherwise.
     *
     * @param ex the reflection exception to handle
     */
    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        }
        if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage());
        }
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        throw new UndeclaredThrowableException(ex);
    }
    
    private static boolean excludeFieldDefault(Field declaredField) {
        Type type = declaredField.getGenericType();
        
        return (MODIFIERS & declaredField.getModifiers()) != 0 || !DefaultUtils.isPrimitiveOrWrapper(type);
    }
    
}
