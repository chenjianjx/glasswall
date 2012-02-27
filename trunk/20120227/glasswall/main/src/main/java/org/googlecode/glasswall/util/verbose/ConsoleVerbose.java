package org.googlecode.glasswall.util.verbose;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * 
 * @author chenjianjx
 * 
 */
public class ConsoleVerbose implements Verbose {

	
	private static ConsoleVerbose instance;

	private ConsoleVerbose() {
	}

	public static ConsoleVerbose getInstance() {
		if (null == instance) {
			instance = new ConsoleVerbose();
		}
		return instance;
	}

	public void println(String msg) {
		pn(msg);
	}

	public void println(Throwable e) {
		StringWriter buffer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(buffer);
		e.printStackTrace(printWriter);
		pn(buffer.getBuffer().toString());
		printWriter.close();
	}
	
	@Override
	public void println(String msg, Throwable e) {
		 this.println(msg);
		 this.println(e);
		
	}

	private void pn(String msg) {
		System.out.println(PROJECT_NAME + ":  " + msg);
	}



 
}
