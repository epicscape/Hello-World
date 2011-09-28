package org.dementhium.mysql;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class MD5Encryption {

	private static MessageDigest messageDigest;

	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String text) {
		byte[] md5hash;
		try {
			synchronized (messageDigest) {
				messageDigest.reset();
				messageDigest.update(text.getBytes("iso-8859-1"), 0, text.length());
				md5hash = messageDigest.digest();
			}
		} catch (UnsupportedEncodingException e) {
			return "";
		}
		StringBuilder buf = new StringBuilder();
		for (byte hashByte : md5hash) {
			int halfbyte = (hashByte >>> 4) & 0x0F;
			for (int i = 0; i < 2; i++) {
				if (0 <= halfbyte && halfbyte <= 9) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = hashByte & 0x0F;
			}
		}
		return buf.toString();
	}

}
