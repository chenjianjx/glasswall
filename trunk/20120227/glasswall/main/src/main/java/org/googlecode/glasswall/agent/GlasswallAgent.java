package org.googlecode.glasswall.agent;

import static org.googlecode.glasswall.agent.status.AgentStatusClassGenerator.AGENT_STATUS_CLASS;
import static org.googlecode.glasswall.agent.status.AgentStatusClassGenerator.SET_APPENDER_SILENT_METHOD;
import static org.googlecode.glasswall.util.env.GlasswallEnvUtils.isJava5;
import static org.googlecode.glasswall.util.lang.GlasswallReflectUtils.invokeStaticBooleanGetter;
import static org.googlecode.glasswall.util.lang.GlasswallReflectUtils.invokeStaticBooleanSetter;

import java.lang.instrument.Instrumentation;

import org.googlecode.glasswall.agent.enhancement.message.provider.MessagePlayerByteCodeEnhancerRepository;
import org.googlecode.glasswall.agent.message.MessagePipeClassGenerator;
import org.googlecode.glasswall.agent.status.AgentStatusClassGenerator;
import org.googlecode.glasswall.agent.transformer.GlasswallClassTransformer;
import org.googlecode.glasswall.util.verbose.Verbose;
import org.googlecode.glasswall.util.verbose.VerboseFactory;

/**
 * 
 * @author chenjianjx
 * 
 */
public class GlasswallAgent {

	private static Verbose verbose = VerboseFactory.getVerbose();
	private static AgentStatusClassGenerator agentStatusClassGenerator = AgentStatusClassGenerator
			.getInstance();

	private static MessagePipeClassGenerator messagePipeClassGenerator = MessagePipeClassGenerator
			.getInstance();

	private static MessagePlayerByteCodeEnhancerRepository msgPlayerEhancerRepository = MessagePlayerByteCodeEnhancerRepository
			.getInstance();

	public static void agentmain(String command, Instrumentation inst) {
		try {
			doAgentMain(command, inst);
		} catch (Throwable e) {
			verbose.println(e);
		}

	}

	public static void premain(String agentArguments, Instrumentation inst) {
		try {
			doPremain(inst);
		} catch (Throwable e) {
			verbose.println(e);
		}
	}

	private static void doPremain(Instrumentation inst) {
		verbose.println("agent started with the application: "
				+ GlasswallAgent.class.getName());
		doMain(null, inst);
	}

	private static void doAgentMain(String command, Instrumentation inst) {
		verbose.println("agent invoked after the app has been started: "
				+ GlasswallAgent.class.getName());
		doMain(command, inst);

	}

	private static void doMain(String command, Instrumentation inst) {

		Class<?> agentStatusClass = findExistingAgentStatusClass(inst);
		if (agentStatusClass == null) {
			messagePipeClassGenerator.generate(getContextClassLoader());
			agentStatusClass = agentStatusClassGenerator
					.generate(getContextClassLoader());

			if (isJava5()) {
				//java5 doesn't support re-transform
				inst.addTransformer(new GlasswallClassTransformer());
			} else {
				inst.addTransformer(new GlasswallClassTransformer(), true);
				/**
				 * for the case that an agent is invoked after the web app has
				 * started; It's only supported for >=java6
				 */
				transformAllClasses(inst);
			}
		} else {
			verbose.println("There's been existing transformation by glasswall agent. No more transformation will be done.");
		}
		toggleSilentMode(command, agentStatusClass);
	}

	private static ClassLoader getContextClassLoader() {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl;
	}

	private static void toggleSilentMode(String command,
			Class<?> agentStatusClass) {

		boolean silent = "silent".equalsIgnoreCase(command);

		invokeStaticBooleanSetter(agentStatusClass, SET_APPENDER_SILENT_METHOD,
				silent);

		verbose.println("The agent has slipped into the mode of "
				+ (invokeStaticBooleanGetter(agentStatusClass,
						AgentStatusClassGenerator.IS_APPENDER_SILENT_METHOD) ? "silent"
						: "non-silent"));

	}

	private static Class<?> findExistingAgentStatusClass(Instrumentation inst) {
		Class<?>[] classes = inst.getAllLoadedClasses();
		for (Class<?> clazz : classes) {
			if (clazz.getName().equals(AGENT_STATUS_CLASS)) {
				return clazz;
			}
		}
		return null;
	}

	private static void transformAllClasses(Instrumentation inst) {
		Class<?>[] classes = inst.getAllLoadedClasses();
		for (Class<?> clazz : classes) {
			if (inst.isModifiableClass(clazz) && isCandidate(clazz)) {
				retransform(inst, clazz);
			}
		}
	}

	private static boolean isCandidate(Class<?> clazz) {
		return msgPlayerEhancerRepository.getEnhancer(clazz.getName()) != null;
	}

	private static void retransform(Instrumentation inst, Class<?> clazz) {
		try {
			inst.retransformClasses(clazz);
		} catch (Exception e) {
			verbose.println("Failed to transform class " + clazz, e);			
		}
	}

}
