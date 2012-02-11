package org.googlecode.glasswall.util.lang;

/**
 * 
 * @author chenjianjx
 * 
 */
public class GlasswallStringBuilder {

	private StringBuilder delegate = new StringBuilder();

	public GlasswallStringBuilder appendLine(String str) {
		append(str);
		delegate.append("\n");
		return this;
	}

	public GlasswallStringBuilder append(String str) {
		delegate.append(str);
		return this;
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
