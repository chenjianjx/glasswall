package org.googlecode.glasswall.util.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author chenjianjx
 *
 */
public class GlasswallStringBuilderTest {

	
	@Test
	public void appendLineTest(){
		GlasswallStringBuilder sb = new GlasswallStringBuilder();
		sb.appendLine("line1");
		sb.appendLine("line2");
		
		System.out.println(sb);
		Assert.assertEquals("line1\nline2\n" , sb.toString());
		
	}
	
}
