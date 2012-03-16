package org.googlecode.glasswall.agent.message;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;

import org.googlecode.glasswall.util.bytecode.ByteCodeManipulator;
import org.googlecode.glasswall.util.bytecode.ByteCodeManipulatorRepository;
import org.googlecode.glasswall.util.lang.GlasswallStringBuilder;
import org.googlecode.glasswall.util.verbose.Verbose;
import org.googlecode.glasswall.util.verbose.VerboseFactory;

/**
 * to generate the byte code of "Message Pipe", a pipe which contains the
 * messages
 * 
 * @author chenjianjx
 * 
 */
public class MessagePipeClassGenerator {

	public static final String MESSAGE_PIPE_CLASS = "org.googlecode.glasswallbytecode.message.MessagePipe";

	public static final String ADD_MESSAGE = "addMessage";

	public static final String CLEAR_MESSAGES = "clearMessages";

	public static final String IS_CAN_ADD_MESSAGE = "isCanAddMessage";

	public static final String SET_CAN_ADD_MESSAGE = "setCanAddMessage";

	private Verbose verbose = VerboseFactory.getVerbose();

	private static MessagePipeClassGenerator instance;

	public Class<?> generate(ClassLoader classLoader) {
		ByteCodeManipulator byteCodeMan = ByteCodeManipulatorRepository
				.getManipulator(classLoader);

		List<String> fieldDeclarations = new ArrayList<String>();
		List<String> methods = new ArrayList<String>();

		fieldDeclarations.add(getFieldString_canAddMessage());
		fieldDeclarations.add(getFieldString_messageBag());


		methods.add(getMethodString_isCanAddMessage());

		methods.add(getMethodString_setCanAddMessage());

		methods.add(getMethodString_getMessages());

		methods.add(getMethodString_addMessage());

		methods.add(getMethodString_clearMessages());



		Class<?> bytes = byteCodeMan.addClass(MESSAGE_PIPE_CLASS,
				fieldDeclarations, methods);

		verbose.println(MESSAGE_PIPE_CLASS + " has been generated");
		return bytes;

	}

	private  static String getFieldString_canAddMessage() {
		return "private static final ThreadLocal canAddMessage = new ThreadLocal();";
	}

	private  static String getFieldString_messageBag() {
		return "private static final ThreadLocal messageBag = new ThreadLocal();";
	}

	private static String getMethodString_setCanAddMessage() {
		GlasswallStringBuilder sb = new GlasswallStringBuilder();
		sb.appendLine("public static void setCanAddMessage(Boolean canAdd) {");
		sb.appendLine("    canAddMessage.set(canAdd);");
		sb.appendLine("}");
		return sb.toString();
	}

	private static String getMethodString_isCanAddMessage() {
		GlasswallStringBuilder sb = new GlasswallStringBuilder();
		sb.appendLine("private static Boolean isCanAddMessage() {");
		sb.appendLine("    Boolean canAdd = (Boolean)canAddMessage.get();");
		sb.appendLine("    return canAdd;");
		sb.appendLine("}");
		return sb.toString();
	}

	private static String getMethodString_addMessage() {
		GlasswallStringBuilder sb = new GlasswallStringBuilder();
		sb.appendLine(
				format("public static void {0}(Object message)", ADD_MESSAGE))
				.append("{");
		sb.appendLine("    java.util.List messages = getMessages();");
		sb.appendLine("    boolean canAdd = isCanAddMessage() == null ? false: isCanAddMessage().booleanValue();");
		sb.appendLine("    if(canAdd){messages.add(message);}");
		sb.appendLine("}");
		return sb.toString();
	}

	private static String getMethodString_getMessages() {
		GlasswallStringBuilder sb = new GlasswallStringBuilder();
		sb.appendLine("private static java.util.List getMessages() {");
		sb.appendLine("    java.util.List messages = (java.util.List)messageBag.get();");
		sb.appendLine("    if (messages == null) {");
		sb.appendLine("        messages = new java.util.ArrayList();");
		sb.appendLine("        messageBag.set(messages);");
		sb.appendLine("    }");
		sb.appendLine("    return messages;");
		sb.appendLine("}");
		return sb.toString();
	}

	private static String getMethodString_clearMessages() {
		GlasswallStringBuilder sb = new GlasswallStringBuilder();

		sb.appendLine(
				format("public static java.util.List {0}()", CLEAR_MESSAGES))
				.append("{");
		sb.appendLine("    java.util.List messages = getMessages();");
		sb.appendLine("    java.util.ArrayList cloned = new java.util.ArrayList();");
		sb.appendLine("    cloned.addAll(messages);");
		sb.appendLine("    messages.clear();");
		sb.appendLine("    return cloned;");
		sb.appendLine("}");

		return sb.toString();
	}

	private MessagePipeClassGenerator() {
	}

	public static MessagePipeClassGenerator getInstance() {
		if (null == instance) {
			instance = new MessagePipeClassGenerator();
		}
		return instance;
	}

}
