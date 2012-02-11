package org.googlecode.glasswall.util.verbose;

/**
 * 
 * @author chenjianjx
 *
 */
public class VerboseFactory {
	
	public static Verbose getVerbose(){
		//TODO: to support file verbose
		return ConsoleVerbose.getInstance();		
	}

}
