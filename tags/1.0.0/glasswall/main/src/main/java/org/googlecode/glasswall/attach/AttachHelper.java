package org.googlecode.glasswall.attach;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.googlecode.glasswall.agent.GlasswallAgent;
import org.googlecode.glasswall.util.verbose.Verbose;
import org.googlecode.glasswall.util.verbose.VerboseFactory;

/**
 * 
 * @author chenjianjx
 * 
 */
public class AttachHelper {

	private Verbose verbose = VerboseFactory.getVerbose();

	public void doAttach(String jvmId, String command) {
		TargetVirtualMachine tvm = TargetVirtualMachineFactory
				.createVirtualMachine();
		String agentPath = getAgentPath();
		tvm.attach(jvmId);
		tvm.loadAgent(agentPath, command);
	}

	private String getAgentPath() {
		String agentClassResource = toResourceStyle(GlasswallAgent.class
				.getName());		
		URL agentClassUrl = this.getClass().getClassLoader()
				.getResource(agentClassResource);
		String agentClassPath = agentClassUrl.toString();		
		String jarPath =  extractJarPath(agentClassPath);
		verbose.println("agent-jar-file-path: " + jarPath);
		return jarPath;
	}

	private String toResourceStyle(String className) {
		return  className.replace('.', '/') + ".class";
	}

	String extractJarPath(String agentClassPath) {
		String path = agentClassPath.substring("jar:".length(),
				agentClassPath.indexOf("!"));
		path = new File(toURI(path)).getAbsolutePath();	
		return path;
	}

	private URI toURI(String path) {
		try {
			return new URI(path);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getMessage() + "----" + path,
					e);
		}
	}

 
}
