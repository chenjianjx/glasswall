package org.googlecode.glasswall.agent.enhancement.message.provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.googlecode.glasswall.agent.enhancement.message.MessagePlayerByteCodeEnhancer;
import org.googlecode.glasswall.agent.enhancement.message.consumer.HttpServletClassEnhancer;
import org.googlecode.glasswall.agent.enhancement.message.provider.vendor.JulByteCodeEnhancer;
import org.googlecode.glasswall.agent.enhancement.message.provider.vendor.Log4jByteCodeEnhancer;
import org.googlecode.glasswall.agent.enhancement.message.provider.vendor.LogbackByteCodeEnhancer;
import org.googlecode.glasswall.util.lang.GlasswallClassnameUtils;

/**
 * 
 * @author chenjianjx
 * 
 */
public class MessagePlayerByteCodeEnhancerRepository {

	private static MessagePlayerByteCodeEnhancerRepository instance;

	private static final Map<String, Class<? extends MessagePlayerByteCodeEnhancer>> ENHANCER_TYPES = new HashMap<String, Class<? extends MessagePlayerByteCodeEnhancer>>();
	static {
		ENHANCER_TYPES.put(Log4jByteCodeEnhancer.CLASS_TO_ENHANCE,
				Log4jByteCodeEnhancer.class);
		
		ENHANCER_TYPES.put(LogbackByteCodeEnhancer.CLASS_TO_ENHANCE,
				LogbackByteCodeEnhancer.class);		

		ENHANCER_TYPES.put(JulByteCodeEnhancer.CLASS_TO_ENHANCE,
				JulByteCodeEnhancer.class);

		ENHANCER_TYPES.put(HttpServletClassEnhancer.CLASS_TO_ENHANCE,
				HttpServletClassEnhancer.class);

	}

	private MessagePlayerByteCodeEnhancerRepository() {
	}

	public MessagePlayerByteCodeEnhancer getEnhancer(String className) {
		String dotSeperatedClassname = GlasswallClassnameUtils
				.slashToDot(className);
		Class<? extends MessagePlayerByteCodeEnhancer> enhancerClass = ENHANCER_TYPES
				.get(dotSeperatedClassname);
		if (enhancerClass == null) {
			return null;
		}
		return newInstance(enhancerClass, className);

	}

	private MessagePlayerByteCodeEnhancer newInstance(
			Class<? extends MessagePlayerByteCodeEnhancer> enhancerClass,
			String enhancedClass) {

		try {
			Constructor<? extends MessagePlayerByteCodeEnhancer> constructor = enhancerClass
					.getConstructor(String.class);
			MessagePlayerByteCodeEnhancer enhancer = constructor
					.newInstance(enhancedClass);
			return enhancer;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}

	}

	public static MessagePlayerByteCodeEnhancerRepository getInstance() {
		if (null == instance) {
			instance = new MessagePlayerByteCodeEnhancerRepository();
		}
		return instance;
	}
}
