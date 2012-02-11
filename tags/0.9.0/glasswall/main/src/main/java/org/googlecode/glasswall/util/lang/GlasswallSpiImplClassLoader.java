package org.googlecode.glasswall.util.lang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.googlecode.glasswall.util.io.GlasswallIOUtils;
import org.googlecode.glasswall.util.verbose.Verbose;
import org.googlecode.glasswall.util.verbose.VerboseFactory;

/**
 * to load spi-implementations, which is an "*.jar.plugin" file inside the main
 * jar archive
 * 
 * @author chenjianjx
 * 
 */
public class GlasswallSpiImplClassLoader extends ClassLoader {

	private static final String GLASSWALL_SPI_IMPL_JAR_PLUGIN = "glasswall-spi-impl.jar.plugin";

	@Override
	protected synchronized Class<?> loadClass(String className, boolean resolve)
			throws ClassNotFoundException {
		Class<?> c = letParentLoad(className, resolve);
		if (c == null) {
			c = loadFromSpiImplPlugin(className, c);
		}
		if (resolve) {
			resolveClass(c);
		}
		return c;
	}

	private Class<?> letParentLoad(String className, boolean resolve) {
		try {
			return super.loadClass(className, resolve);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	private Class<?> loadFromSpiImplPlugin(String className, Class<?> c)
			throws ClassFormatError {
		InputStream pluginInput = this.getClass().getClassLoader()
				.getResourceAsStream(GLASSWALL_SPI_IMPL_JAR_PLUGIN);
		if (pluginInput == null) {
			throw new RuntimeException(GLASSWALL_SPI_IMPL_JAR_PLUGIN
					+ " is not found under the classpath");
		}

		try {
			byte[] byteCodes = getByteCodes(className, pluginInput);
			if (byteCodes == null) {
				return null;
			}
			c = this.defineClass(className, byteCodes, 0, byteCodes.length);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			GlasswallIOUtils.closeQuietly(pluginInput);
		}
		return c;
	}

	private byte[] getByteCodes(String className, InputStream pluginInput)
			throws IOException {
		ZipInputStream zin = new ZipInputStream(pluginInput);
		String targetEntryName = GlasswallClassnameUtils.dotToSlash(className)
				+ ".class";

		byte[] buf = new byte[1024];
		int count = 0;

		while (true) {
			ZipEntry entry = zin.getNextEntry();
			if (entry == null) {
				break;
			}

			if (entry.getName().equals(targetEntryName)) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				while ((count = zin.read(buf, 0, 1024)) != -1) {
					out.write(buf, 0, count);
				}
				return out.toByteArray();
			}
		}
		return null;
	}

}
