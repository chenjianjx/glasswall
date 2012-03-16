package org.googlecode.glasswall.agent.enhancement.message.consumer;

import static java.util.Arrays.asList;
import static org.googlecode.glasswall.agent.message.MessagePipeClassGenerator.MESSAGE_PIPE_CLASS;
import static org.googlecode.glasswall.util.bytecode.GlasswallSourceCodeVariables.METHOD_PARAM_PREFIX;
import static org.googlecode.glasswall.util.lang.GlasswallClassnameUtils.slashToDot;

import java.text.MessageFormat;
import java.util.List;

import org.googlecode.glasswall.agent.enhancement.message.MessagePlayerByteCodeEnhancer;
import org.googlecode.glasswall.agent.message.MessagePipeClassGenerator;
import org.googlecode.glasswall.util.bytecode.ByteCodeManipulator;
import org.googlecode.glasswall.util.bytecode.ByteCodeManipulatorRepository;
import org.googlecode.glasswall.util.io.GlasswallIOUtils;

/**
 * 
 * @author chenjianjx
 * 
 */
public class HttpServletClassEnhancer extends MessagePlayerByteCodeEnhancer {

	private static final List<String> SERVICE_METHOD_PARAM_TYPES = asList(
			"javax.servlet.ServletRequest", "javax.servlet.ServletResponse");
	private static final String SERVICE_METHOD = "service";

	public static final String CLASS_TO_ENHANCE = "javax.servlet.http.HttpServlet";

	public HttpServletClassEnhancer(String className) {
		super(className);

	}

	@Override
	protected byte[] doEnhance(ClassLoader loader, byte[] classfileBuffer) {
		return enhanceServiceMethod(loader);
	}

	private byte[] enhanceServiceMethod(ClassLoader cl) {

		className = slashToDot(className);

		ByteCodeManipulator byteCodeMan = ByteCodeManipulatorRepository
				.getManipulator(cl);

		enhanceOnRequest(className, byteCodeMan);

		byte[] bytes = enhanceOnResponse(className, byteCodeMan);

		return bytes;

	}

	private byte[] enhanceOnRequest(String className,
			ByteCodeManipulator byteCodeMan) {
		StringBuffer newStatements = new StringBuffer();

		newStatements.append(MessageFormat.format("{0}.clearMessages();",
				MESSAGE_PIPE_CLASS));

		newStatements.append(MessageFormat.format(
				"{0}.setCanAddMessage(Boolean.TRUE);", MESSAGE_PIPE_CLASS));

		byte[] bytes = byteCodeMan.insertAtMethodStart(className,
				SERVICE_METHOD, SERVICE_METHOD_PARAM_TYPES,
				newStatements.toString());

		verbose.println("Ehancement to pre-handle Servlet's request has been done");

		return bytes;
	}

	private byte[] enhanceOnResponse(String className,
			ByteCodeManipulator byteCodeMan) {

		byte[] bytes = byteCodeMan.insertAtMethodEnd(className, SERVICE_METHOD,
				SERVICE_METHOD_PARAM_TYPES, generate_consumeMessages());
		verbose.println("Ehancement to post-handle Servlet's response has been done");
		return bytes;
	}

	private String generate_consumeMessages() {
		String method = GlasswallIOUtils.readClasspathResource(this.getClass()
				.getClassLoader(), "HttpServlet_outputMessages.snippet",
				"UTF-8");
		method = method.replace("#RESPONSE", METHOD_PARAM_PREFIX + 2);

		method = method.replace("#MESSAGE_PIPE",
				MessagePipeClassGenerator.MESSAGE_PIPE_CLASS);
		method = method.replace("#CLEAR_MESSAGES",
				MessagePipeClassGenerator.CLEAR_MESSAGES);
		return method;
	}

}
