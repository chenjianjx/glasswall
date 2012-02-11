package org.googlecode.glasswall.attach;

/**
 * 
 * @author chenjianjx
 * 
 */
public class TargetVirtualMachineFactory {

	public static TargetVirtualMachine createVirtualMachine() {

		// TODO: support other implementations such as IBM's
		return new HotSpotVirtualMachine();

	}

}
