package org.googlecode.glasswall.util.bytecode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.googlecode.glasswall.util.lang.GlasswallSpiImplClassLoader;

/**
 * to retrieve a manipulator
 * 
 * @author chenjianjx
 * 
 */
public class ByteCodeManipulatorRepository {

	private static GlasswallSpiImplClassLoader spiImplclassLoader = new GlasswallSpiImplClassLoader();

	private static final String JAVASSIST_MANIPULATOR = "org.googlecode.glasswall.util.bytecode.javaassist.JavassistByteCodeManipulator";

	/**
	 * get the manipulator and register a class-loader
	 * 
	 * @param classLoader
	 *            nullable
	 * @return
	 */
	public static ByteCodeManipulator getManipulator(ClassLoader classLoader) {
		try {
			Class<?> clazz = spiImplclassLoader
					.loadClass(JAVASSIST_MANIPULATOR);
			Method getInstanceMethod = clazz.getMethod("getInstance",
					ClassLoader.class);
			return (ByteCodeManipulator) getInstanceMethod.invoke(null,
					classLoader);
		} catch (ClassNotFoundException e) {
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
