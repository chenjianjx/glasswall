package org.googlecode.glasswall.util.verbose;

/**
 * do verbose for this tool itself
 * 
 * @author chenjianjx
 * 
 */
public interface Verbose {
	public static final String PROJECT_NAME = "Glasswall";
	public void println(String msg);
	public void println(Throwable e);
 
}


