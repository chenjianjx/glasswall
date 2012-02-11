package org.googlecode.glasswall.util.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author chenjianjx
 * 
 */
public class GlasswallReflectUtilsTest {

	@Test
	public void invokeStaticBooleanMethodTest_Setter_True() {
		GlasswallReflectUtils.invokeStaticBooleanSetter(
				StaticBooleanTestee.class, "setBool", true);
		Assert.assertTrue(StaticBooleanTestee.isBool());
	}

	@Test
	public void invokeStaticBooleanMethodTest_Setter_False() {
		GlasswallReflectUtils.invokeStaticBooleanSetter(
				StaticBooleanTestee.class, "setBool", false);
		Assert.assertFalse(StaticBooleanTestee.isBool());
	}

	@Test
	public void invokeStaticBooleanMethodTest_Getter() {
		StaticBooleanTestee.setBool(true);
		Assert.assertTrue(GlasswallReflectUtils.invokeStaticBooleanGetter(
				StaticBooleanTestee.class, "isBool"));
	}

	private static final class StaticBooleanTestee {
		private static boolean bool = false;

		public static boolean isBool() {
			return bool;
		}

		public static void setBool(boolean bool) {
			StaticBooleanTestee.bool = bool;
		}

	}

}
