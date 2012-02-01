package org.googlecode.glasswall.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * 
 * @author chenjianjx
 * 
 */
public class GlasswallIOUtils {

	public static String readClasspathResource(ClassLoader classLoader, 
			String classpath, String encoding) {
		InputStream in = classLoader.getResourceAsStream(classpath);
		if(in == null){
			throw new IllegalArgumentException(MessageFormat.format("Cannot find resource \"{0}\"", classpath));
		}
		try {
			return toString(in, encoding);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			closeQuietly(in);
		}

	}

	public static void closeQuietly(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	private static String toString(InputStream in, String encoding)
			throws IOException {

		byte[] bytes = toBytes(in);
		return new String(bytes, encoding);

	}

	private static byte[] toBytes(InputStream in) throws IOException {
		byte[] data = null;
		int s = in.read();
		if (s == -1) {
			return null;
		}
		int alength = in.available();
		if (alength > 0) {
			data = new byte[alength + 1];
			in.read(data, 1, alength);
		} else {
			data = new byte[1];
		}
		data[0] = (byte) s;
		return data;

	}

}
