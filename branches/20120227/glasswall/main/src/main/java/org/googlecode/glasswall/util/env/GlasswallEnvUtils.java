package org.googlecode.glasswall.util.env;

/**
 * 
 * @author chenjianjx
 *
 */
public class GlasswallEnvUtils {

	public static boolean isJava5() {
		String version = System.getProperty("java.version");
		if (version != null && version.startsWith("1.5")) {
			return true;
		}
		return false;
	}
}
