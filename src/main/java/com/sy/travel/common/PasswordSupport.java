package com.sy.travel.common;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordSupport {

	private static final BCryptPasswordEncoder B_CRYPT = new BCryptPasswordEncoder();

	private PasswordSupport() {
	}

	public static String hash(String password) {
		return B_CRYPT.encode(password);
	}

	public static boolean isBcryptHash(String encodedPassword) {
		return encodedPassword != null && encodedPassword.matches("^\\$2[aby]\\$.{56}$");
	}

	public static boolean matches(String username, String rawPassword, String encodedPassword) {
		if (isBcryptHash(encodedPassword)) {
			return B_CRYPT.matches(rawPassword, encodedPassword);
		}
		return legacyEncode(username, rawPassword).equals(encodedPassword);
	}

	public static String legacyEncode(String username, String password) {
		String source = username + ":" + password;
		return Base64.getEncoder().encodeToString(source.getBytes(StandardCharsets.UTF_8));
	}

	public static String legacyDecode(String encodedPassword) {
		if (encodedPassword == null) {
			return "";
		}
		try {
			byte[] decode = Base64.getDecoder().decode(encodedPassword);
			String pass = new String(decode, StandardCharsets.UTF_8);
			int separator = pass.indexOf(":");
			return separator >= 0 ? pass.substring(separator + 1) : pass;
		} catch (IllegalArgumentException ex) {
			return encodedPassword;
		}
	}
}
