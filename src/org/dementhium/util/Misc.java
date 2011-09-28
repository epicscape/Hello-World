package org.dementhium.util;

import java.util.StringTokenizer;

import org.dementhium.model.Item;

public class Misc {

	public static boolean isLinux() {
		return System.getProperty("os.name").contains("Linux");
	}

	/**
	 * Checks if the operation system is windows.
	 * @return {@code True} if so.
	 */
	public static boolean isWindows() {
		return System.getProperty("os.name").contains("Windows");
	}

	/**
	 * Checks if the host is the VPS used.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public static boolean isVPS() {
		return isWindows() && System.getProperty("user.home").contains("Administrator");
	}

	public static int percentage(double d) {
		return (int) (d * 100);
	}

	public static int IPAddressToNumber(String ipAddress) {
		StringTokenizer st = new StringTokenizer(ipAddress, ".");
		int[] ip = new int[4];
		int i = 0;
		while (st.hasMoreTokens())
			ip[i++] = Integer.parseInt(st.nextToken());
		return ((ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | (ip[3]));
	}

	public static int random(int random) {
		return (int) (Math.random() * (random + 1));
	}

	public static int random(double random) {
		return (int) (Math.random() * (random + 1));
	}


	public static int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}

	public static String formatPlayerNameForProtocol(String name) {
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}

	public static String withPrefix(String string) {
		return ((string.startsWith("a") || string.startsWith("e") || string.startsWith("i") || string.startsWith("o") || string.startsWith("u")) ? "an " : "a ") + string;
	}

	public static String formatPlayerNameForDisplay(String name) {
		name = name.replaceAll("_", " ");
		name = name.toLowerCase();
		StringBuilder newName = new StringBuilder();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				newName.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				newName.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return newName.toString();
	}

	public static String longToString(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return null;
		if (l % 37L == 0L)
			return null;
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = VALID_CHARS[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static long stringToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	public static final char[] VALID_CHARS = {'_', 'a', 'b', 'c', 'd', 'e',
		'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
		's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9'};

	public static boolean allowed(char c) {
		if (c == ' ')
			return true;
		for (char c2 : VALID_CHARS)
			if (c == c2)
				return true;
		return false;
	}

	public static int packGJString2(int position, byte[] buffer, String string) {
		int length = string.length();
		int offset = position;
		for (int i_6_ = 0; length > i_6_; i_6_++) {
			int character = string.charAt(i_6_);
			if (character > 127) {
				if (character > 2047) {
					buffer[offset++] = (byte) ((character | 919275) >> 12);
					buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
					buffer[offset++] = (byte) (128 | (character & 63));
				} else {
					buffer[offset++] = (byte) ((character | 12309) >> 6);
					buffer[offset++] = (byte) (128 | (character & 63));
				}
			} else
				buffer[offset++] = (byte) character;
		}
		return offset - position;
	}

	public static int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX1 - coordX2;
		int deltaY = coordY1 - coordY2;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	public static String upperFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String optimizeText(String text) {
		char buf[] = text.toLowerCase().toCharArray();
		boolean endMarker = true;
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;
				endMarker = false;
			}
			if (c == '.' || c == '!' || c == '?') {
				endMarker = true;
			}
		}
		return new String(buf, 0, buf.length);
	}

	public static String checkString(String l) {
		char s[] = l.toCharArray();
		int count = 0;
		for (int i = 0; i < s.length; i++) {
			char c = s[i];
			if (c == '_') {
				s[i] = ' ';
				c = s[i];
			}
			if (count == 0) {
				s[i] -= 0x20;
				count = 1;
			}
			if (c == '.' || c == '!' || c == '?' || c == ' ') {
				s[i + 1] -= 0x20;
			}
		}
		return new String(s, 0, s.length);
	}

	public static int[] itemToIntArray(Item[] itemArray) {
		int[] newArray = new int[itemArray.length];
		for (int i = 0; i < itemArray.length; i++) {
			newArray[i] = itemArray[i].getId();
		}
		return newArray;
	}

	public static String amountToString(int amt) {
		if (amt > 100000) {
			if (amt > 100000 && amt < 1000000) {
				return Integer.toString(amt / 10000) + "K";
			} else if (amt > 1000000 && amt < 10000000) {
				return Integer.toString(amt / 100000) + "M";
			} else if (amt > 10000000 && amt < 100000000) {
				return Integer.toString(amt / 1000000) + "M";
			} else if (amt > 10000000 && amt < Integer.MAX_VALUE) {
				return Integer.toString(amt / 1000000) + "M";
			}
		} else {
			return Integer.toString(amt);
		}
		return null;

	}

	public static int stringToAmount(char c, int start) {
		int amt = start;
		if (c == 'k' || c == 'm') {
			if (c == 'k') {
				return amt * 10000;
			} else if (c == 'm') {
				return amt * 100000;
			}
		}
		return amt;

	}


}
