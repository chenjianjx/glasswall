package org.googlecode.glasswall.util.io;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author chenjianjx
 * 
 */
public class GlasswallIOUtilsTest {

	@Test
	public void readClassAdjacentResourceTest() {
		Assert.assertEquals("0123456789", GlasswallIOUtils
				.readClasspathResource(this.getClass().getClassLoader(),
						"snippets/zero_to_nine.txt", "UTF-8"));
	}

}
