package org.googlecode.glasswall.util.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author chenjianjx
 * 
 */
public class GlasswallReflectUtils {

	public static void invokeStaticBooleanSetter(Class<?> clazz,
			String methodName, boolean value) {
		try {
			Method method = clazz.getMethod(methodName, boolean.class);
			method.invoke(null, value);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	public static boolean invokeStaticBooleanGetter(Class<?> clazz,
			String methodName) {
		try {
			Method method = clazz.getMethod(methodName);
			return (Boolean) method.invoke(null,new Object[0]);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

	}

}
