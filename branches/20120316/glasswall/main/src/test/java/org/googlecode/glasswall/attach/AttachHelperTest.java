package org.googlecode.glasswall.attach;



import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author chenjianjx
 * 
 */
public class AttachHelperTest {

	@Test
	public void extractJarPathTest() {
		AttachHelper helper = new AttachHelper();
		String agentClassPath = "jar:file:/somedir/someapi/someversion/someapi-someversion.jar!/someapi/somepackage/";
		Assert.assertEquals("/somedir/someapi/someversion/someapi-someversion.jar", helper.extractJarPath(agentClassPath));

	}

}
