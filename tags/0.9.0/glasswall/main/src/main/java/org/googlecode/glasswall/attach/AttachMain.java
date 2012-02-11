package org.googlecode.glasswall.attach;

/**
 * 
 * @author chenjianjx
 * 
 */
public class AttachMain {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("\n\n");
			System.out.println("Usuage: glasswall <pid>");
			System.out.println("Usuage: glasswall <pid> silent");
			System.out.println("\n\n");
			return;
		}
		String jvmId = args[0];
	    String command = args.length > 1 ? args[1] : null;
		AttachHelper attachHelper = new AttachHelper();
		attachHelper.doAttach(jvmId, command);		
	}

}
