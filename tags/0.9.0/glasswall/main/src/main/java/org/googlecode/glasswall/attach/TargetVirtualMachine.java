package org.googlecode.glasswall.attach;

/**
 * to provide jdk-vendor-independent attach utilities
 * 
 * @author chenjianjx
 * 
 */
public interface TargetVirtualMachine {

	void attach(String jvmId);

	void loadAgent(String agentPath, String options);

	 
	
}
