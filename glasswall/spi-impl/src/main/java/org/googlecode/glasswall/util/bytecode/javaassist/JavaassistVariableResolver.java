package org.googlecode.glasswall.util.bytecode.javaassist;

import org.googlecode.glasswall.util.bytecode.GlasswallSourceCodeVariables;

/**
 * 
 * @author chenjianjx
 * 
 */
public class JavaassistVariableResolver {

	/**
	 * replace Glasswall's variables with the ones of javassists
	 * @param sourceCode
	 * @return
	 */
	public static String resolveToJavassistVariables(String sourceCode) {

		String newSource = sourceCode;
		newSource = newSource.replace(GlasswallSourceCodeVariables.METHOD_PARAM_PREFIX, "$");		
		return newSource;
	}

}
