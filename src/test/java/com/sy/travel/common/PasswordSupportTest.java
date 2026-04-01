package com.sy.travel.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PasswordSupportTest {

	@Test
	public void shouldMatchBcryptHash() {
		String encoded = PasswordSupport.hash("123456");
		assertTrue(PasswordSupport.isBcryptHash(encoded));
		assertTrue(PasswordSupport.matches("admin", "123456", encoded));
		assertFalse(PasswordSupport.matches("admin", "bad", encoded));
	}

	@Test
	public void shouldMatchLegacyBase64() {
		String encoded = PasswordSupport.legacyEncode("admin", "123456");
		assertTrue(PasswordSupport.matches("admin", "123456", encoded));
		assertFalse(PasswordSupport.matches("admin", "bad", encoded));
	}
}
