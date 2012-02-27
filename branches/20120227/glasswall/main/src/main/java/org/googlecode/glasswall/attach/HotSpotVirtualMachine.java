package org.googlecode.glasswall.attach;

import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

/**
 * 
 * @author chenjianjx
 * 
 */
public class HotSpotVirtualMachine implements TargetVirtualMachine {
	private VirtualMachine vm;

	 
	public void attach(String jvmId) {
		try {
			vm = VirtualMachine.attach(jvmId);
		} catch (AttachNotSupportedException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	 
	public void loadAgent(String agentPath, String options) {
		if (vm == null) {
			throw new IllegalStateException("virtual machine not attached yet");
		}
		try {
			vm.loadAgent(agentPath, options);
		} catch (AgentLoadException e) {
			throw new RuntimeException(e);
		} catch (AgentInitializationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
