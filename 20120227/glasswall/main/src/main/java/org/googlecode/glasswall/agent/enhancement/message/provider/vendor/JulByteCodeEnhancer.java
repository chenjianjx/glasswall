package org.googlecode.glasswall.agent.enhancement.message.provider.vendor;

import static java.util.Arrays.asList;
import static org.googlecode.glasswall.agent.status.AgentStatusClassGenerator.AGENT_STATUS_CLASS;
import static org.googlecode.glasswall.agent.status.AgentStatusClassGenerator.IS_APPENDER_SILENT_METHOD;
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
public class JulByteCodeEnhancer extends MessagePlayerByteCodeEnhancer {

	public static final String CLASS_TO_ENHANCE = "java.util.logging.Logger";

	public JulByteCodeEnhancer(String className) {
		super(className);
	}

	@Override
	protected byte[] doEnhance(ClassLoader loader, byte[] classfileBuffer) {

		String dotSeperatedClassname = slashToDot(className);

		ByteCodeManipulator byteCodeMan = ByteCodeManipulatorRepository
				.getManipulator(loader);

 

		String methodName = "log";
		List<String> methodParamTypes = asList("java.util.logging.LogRecord");		
		
		return byteCodeMan.insertAtMethodEnd(dotSeperatedClassname, methodName,
				methodParamTypes,   makeNewStatments());
	}

	String makeNewStatments() {
		GlasswallStringBuilder newStatement = new GlasswallStringBuilder();
		newStatement.appendLine(generate_isGlasswallAppenderSilent());
		newStatement.appendLine("if(!glasswallSilent){");
		newStatement.appendLine(generate_addToGlasswallMessagePipe());
		newStatement.append("}");
		return newStatement.toString();
	}

	private String generate_addToGlasswallMessagePipe() {
		String method = GlasswallIOUtils.readClasspathResource(this.getClass()
				.getClassLoader(), "Jul_addToGlasswallMessagePipe.snippet",
				"UTF-8");
		method = method.replace("#MESSAGE_PIPE",
				MessagePipeClassGenerator.MESSAGE_PIPE_CLASS);
		method = method.replace("#ADD_MESSAGE",
				MessagePipeClassGenerator.ADD_MESSAGE);
		method = method.replace("#LOG_RECORD", GlasswallSourceCodeVariables.METHOD_PARAM_PREFIX + 1);
		return method;
	}

	private String generate_isGlasswallAppenderSilent() {
		String method = GlasswallIOUtils.readClasspathResource(this.getClass()
				.getClassLoader(), "Jul_isGlasswallAppenderSilent.snippet",
				"UTF-8");
		method = method.replace("#AGENT_STATUS_CLASS", AGENT_STATUS_CLASS);
		method = method.replace("#IS_APPENDER_SILENT_METHOD",
				IS_APPENDER_SILENT_METHOD);
		return method;
	}

 

}
