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
public class Log4jByteCodeEnhancer extends MessagePlayerByteCodeEnhancer {

	public static final String CLASS_TO_ENHANCE = "org.apache.log4j.Category";

	public Log4jByteCodeEnhancer(String className) {
		super(className);
	}

	@Override
	protected byte[] doEnhance(ClassLoader loader, byte[] classfileBuffer) {

		String dotSeperatedClassname = slashToDot(className);

		ByteCodeManipulator byteCodeMan = ByteCodeManipulatorRepository
				.getManipulator(loader);

		classfileBuffer = enhanceMethod_callAppenders(classfileBuffer,
				dotSeperatedClassname, byteCodeMan);

		return classfileBuffer;

	}

	private byte[] enhanceMethod_callAppenders(byte[] classfileBuffer,
			String className, ByteCodeManipulator byteCodeMan) {

		String methodName = "callAppenders";
		List<String> methodParamTypes = asList("org.apache.log4j.spi.LoggingEvent");

		// for example, log4j-over-slfj.jar has the class but the class doesn't
		// have this method; do nothing in
		// this case
		if (!byteCodeMan.hasMethod(className, methodName, methodParamTypes)) {
			return classfileBuffer;
		}

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
				.getClassLoader(), "Log4j_addToGlasswallMessagePipe.snippet",
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
