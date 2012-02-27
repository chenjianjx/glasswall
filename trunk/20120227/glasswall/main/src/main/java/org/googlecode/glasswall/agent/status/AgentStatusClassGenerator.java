package org.googlecode.glasswall.agent.status;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;

import org.googlecode.glasswall.util.bytecode.ByteCodeManipulator;
import org.googlecode.glasswall.util.bytecode.ByteCodeManipulatorRepository;
import org.googlecode.glasswall.util.lang.GlasswallStringBuilder;
import org.googlecode.glasswall.util.verbose.Verbose;
import org.googlecode.glasswall.util.verbose.VerboseFactory;

/**
 * Dynamically generate a class indicating the status of the agent
 * 
 * @author chenjianjx
 * 
 */
public class AgentStatusClassGenerator {

	public static final String IS_APPENDER_SILENT_METHOD = "isAppenderSilent";

	public static final String SET_APPENDER_SILENT_METHOD = "setAppenderSilent";

	public static final String AGENT_STATUS_CLASS = "org.googlecode.glasswallbytecode.status.GlasswallAgentStatus";
	
	private Verbose verbose = VerboseFactory.getVerbose();

	private static AgentStatusClassGenerator instance;

	private AgentStatusClassGenerator() {
	}

	/**
	 * 
	 * @param classLoader nullable
	 * @return
	 */
	public Class<?> generate(ClassLoader classLoader) {
		ByteCodeManipulator byteCodeMan = ByteCodeManipulatorRepository
				.getManipulator(classLoader);

		List<String> fieldDeclarations = new ArrayList<String>();
		List<String> methods = new ArrayList<String>();

		fieldDeclarations.add("private static  boolean appenderSilent;");

		methods.add(getMethodString_isAppenderSilent());

		methods.add(getMethodString_setAppenderSilent());

		Class<?> bytes = byteCodeMan.addClass(AGENT_STATUS_CLASS, fieldDeclarations,
				methods);
		
		verbose.println(AGENT_STATUS_CLASS + " has been generated");
		
		return bytes;
	}

	private String getMethodString_setAppenderSilent() {
		GlasswallStringBuilder sb = new GlasswallStringBuilder();
		sb.appendLine(format("public static  void {0}(boolean s) '{'",
				SET_APPENDER_SILENT_METHOD));
		sb.appendLine("    appenderSilent = s;");
		sb.appendLine("}");
		return sb.toString();
	}

	private String getMethodString_isAppenderSilent() {
		GlasswallStringBuilder sb = new GlasswallStringBuilder();
		sb.appendLine(format("public static  boolean {0}() '{'",
				IS_APPENDER_SILENT_METHOD));
		sb.appendLine("    return appenderSilent;");
		sb.appendLine("}");
		return sb.toString();
	}

	public static AgentStatusClassGenerator getInstance() {
		if (null == instance) {
			instance = new AgentStatusClassGenerator();
		}
		return instance;
	}

}
