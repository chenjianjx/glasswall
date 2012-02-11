package org.googlecode.glasswall.util.lang;

/**
 * 
 * @author chenjianjx
 *
 */
public class GlasswallClassnameUtils {

	public static String slashToDot(String slashSeparated) {
		return slashSeparated.replace('/', '.');
	}
	
	public static String dotToSlash(String dotSeperated) {
		return dotSeperated.replace('.', '/');
	}

}
