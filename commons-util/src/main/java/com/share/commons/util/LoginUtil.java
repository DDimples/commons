package com.share.commons.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.LoggerFactory;

public final class LoginUtil {
	private LoginUtil() {
	}

	private static final Key key = new SecretKeySpec(
			")O[EL]1,YF}+efcaj{+oESb9d8>Z'e9M".getBytes(), "AES");

	private static final IvParameterSpec iv = new IvParameterSpec(
			"L+\\~f4,Ir)b$=pkf".getBytes());

	/**
	 * 
	 * @param src
	 * @return
	 * @throws NullPointerException
	 *             while src is null
	 */
	public static String encrypt(String src) {
		try {
			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
			return URLEncoder.encode(
					Base64.encode(cipher.doFinal(src.getBytes())), "UTF-8");
		} catch (Exception e) {
			LoggerFactory.getLogger(LoginUtil.class).error(
					"error in encrypt src:" + src, e);
		}
		return null;

	}

	/**
	 * 
	 * @param src
	 * @return
	 * @throws NullPointerException
	 *             while src is null
	 */
	public static String decrypt(String src) {
		try {
			Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
			return new String(cipher.doFinal(Base64.decode(URLDecoder.decode(
					src, "UTF-8"))));
		} catch (Exception e) {
			LoggerFactory.getLogger(LoginUtil.class).error(
					"error in decrypt src:" + src, e);
		}
		return null;
	}

	private static Cipher getCipher(int mode) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(mode, key, iv);
		return cipher;
	}
}
