package org.googlecode.glasswall.agent.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.googlecode.glasswall.agent.enhancement.message.MessagePlayerByteCodeEnhancer;
import org.googlecode.glasswall.agent.enhancement.message.provider.MessagePlayerByteCodeEnhancerRepository;
import org.googlecode.glasswall.util.verbose.Verbose;
import org.googlecode.glasswall.util.verbose.VerboseFactory;

/**
 * 
 * @author chenjianjx
 * 
 */
public class GlasswallClassTransformer implements ClassFileTransformer {

	private Verbose verbose = VerboseFactory.getVerbose();

	private MessagePlayerByteCodeEnhancerRepository msgPlayerEhancerRepository = MessagePlayerByteCodeEnhancerRepository
			.getInstance();

	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		try {
			return doTransform(loader, className, classfileBuffer);
		} catch (RuntimeException e) {
			verbose.println(e);
			return classfileBuffer;
		}
	}

	private byte[] doTransform(ClassLoader loader, String className,
			byte[] classfileBuffer) {

		MessagePlayerByteCodeEnhancer msgProviderEnhancer = msgPlayerEhancerRepository
				.getEnhancer(className);
		if (msgProviderEnhancer == null) {
			return classfileBuffer;
		}

		byte[] bytes = msgProviderEnhancer.enhance(loader, classfileBuffer);
		return bytes;

	}

}