package org.googlecode.glasswall.agent.enhancement.message.provider.vendor;

import static java.util.Arrays.asList;
import static org.googlecode.glasswall.agent.enhancement.message.provider.MessageProviderByteCodeEnhanceHelper.generate_ifNotSilent;
import static org.googlecode.glasswall.util.lang.GlasswallClassnameUtils.slashToDot;

import java.util.List;

import org.googlecode.glasswall.agent.enhancement.message.MessagePlayerByteCodeEnhancer;
import org.googlecode.glasswall.agent.message.MessagePipeClassGenerator;
import org.googlecode.glasswall.util.bytecode.ByteCodeManipulator;
import org.googlecode.glasswall.util.bytecode.ByteCodeManipulatorRepository;
import org.googlecode.glasswall.util.bytecode.GlasswallSourceCodeVariables;
import org.googlecode.glasswall.util.io.GlasswallIOUtils;
import org.googlecode.glasswall.util.lang.GlasswallStringBuilder;

/**
 * 
 * @author chenjianjx
 * 
 */
public class LogbackByteCodeEnhancer extends MessagePlayerByteCodeEnhancer {

	public static final String CLASS_TO_ENHANCE = "ch.qos.logback.classic.Logger";

	public LogbackByteCodeEnhancer(String className) {
		super(className);
	}

	@Override
	protected byte[] doEnhance(ClassLoader loader, byte[] classfileBuffer) {

		String dotSeperatedClassname = slashToDot(className);

		ByteCodeManipulator byteCodeMan = ByteCodeManipulatorRepository
				.getManipulator(loader);

		classfileBuffer = enhanceMethod_callAppenders(dotSeperatedClassname,
				byteCodeMan);

		return classfileBuffer;

	}

	private byte[] enhanceMethod_callAppenders(String className,
			ByteCodeManipulator byteCodeMan) {
		byte[] classfileBuffer;
		String methodName = "callAppenders";
		List<String> methodParamTypes = asList("ch.qos.logback.classic.spi.ILoggingEvent");
		GlasswallStringBuilder newStatement = new GlasswallStringBuilder();
		newStatement.appendLine(generate_ifNotSilent()).append("{");
		newStatement.appendLine(generate_addToGlasswallMessagePipe());
		newStatement.append("}");
		classfileBuffer = byteCodeMan.insertAtMethodEnd(className, methodName,
				methodParamTypes, newStatement.toString());
		return classfileBuffer;
	}

	private String generate_addToGlasswallMessagePipe() {
		String method = GlasswallIOUtils.readClasspathResource(this.getClass()
				.getClassLoader(), "Logback_addToGlasswallMessagePipe.snippet",
				"UTF-8");

		method = method.replace("#MESSAGE_PIPE",
				MessagePipeClassGenerator.MESSAGE_PIPE_CLASS);
		method = method.replace("#ADD_MESSAGE",
				MessagePipeClassGenerator.ADD_MESSAGE);
		method = method.replace("#EVENT",
				GlasswallSourceCodeVariables.METHOD_PARAM_PREFIX + 1);
		return method;
	}

 

}
