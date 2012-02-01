package org.googlecode.glasswall.agent.enhancement.message.provider;

import static java.text.MessageFormat.format;
import static org.googlecode.glasswall.agent.status.AgentStatusClassGenerator.AGENT_STATUS_CLASS;
import static org.googlecode.glasswall.agent.status.AgentStatusClassGenerator.IS_APPENDER_SILENT_METHOD;

/**
 * 
 * @author chenjianjx
 * 
 */
public class MessageProviderByteCodeEnhanceHelper {

	public static String generate_ifNotSilent() {
		return format("if(!{0}.{1}())", AGENT_STATUS_CLASS,
				IS_APPENDER_SILENT_METHOD);
	}

}
