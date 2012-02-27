package org.googlecode.glasswall.agent.enhancement.message;

import org.googlecode.glasswall.util.verbose.Verbose;
import org.googlecode.glasswall.util.verbose.VerboseFactory;

/**
 * to enhance those classes which provide or consume messages
 * 
 * @author chenjianjx
 * 
 */
public abstract class MessagePlayerByteCodeEnhancer {

	/**
	 * slash separated class-name
	 */
	protected String className;

	protected Verbose verbose = VerboseFactory.getVerbose();

	public MessagePlayerByteCodeEnhancer(String className) {
		super();
		this.className = className;
	}

	public byte[] enhance(ClassLoader loader, byte[] classfileBuffer) {
		byte[] buffer = this.doEnhance(loader, classfileBuffer);
		verbose.println(this.getClass().getSimpleName() + " has enhanced "
				+ className);
		
		return buffer;
	}

	protected abstract byte[] doEnhance(ClassLoader loader,
			byte[] classfileBuffer);

}
