package org.brijframework.util.runtime;

import java.util.UUID;

import org.brijframework.util.casting.CastingUtil;

public class RandomUtil {
	public static String genRandomUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String genRandomUCString(int length) {
		return genRandomString(length).toUpperCase();
	}

	public static String genRandomString(int length) {
		StringBuilder buffer = new StringBuilder();
		while (buffer.length() < length) {
			buffer.append(genRandomUUID());
		}
		return buffer.substring(0, length);
	}

	public static Integer genRandomNumber(int numOfCharacters) {
		int maximum = (int) (Math.pow(CastingUtil.integerForInt(10), numOfCharacters) - 1);
		int minimum = (int) Math.pow(CastingUtil.integerForInt(10), numOfCharacters - 1);
		return (int) (Math.random() * ((maximum - minimum) + 1)) + minimum;

	}

	public static Long genRandomLong(int numOfCharacters) {
		long maximum = (long) (Math.pow(CastingUtil.integerForInt(10), numOfCharacters) - 1);
		long minimum = (long) Math.pow(CastingUtil.integerForInt(10), numOfCharacters - 1);
		return (long) (Math.random() * ((maximum - minimum) + 1)) + minimum;

	}

	public static int getOverLap(int startT1, int endT1, int startT2, int endT2) {
		if (startT1 > endT2 || endT1 < startT2) {
			return 0;
		}
		if (startT1 <= startT2 && endT1 >= endT2) {
			return endT2 - startT2 + 1;
		} else if (startT1 > startT2 && (endT1 >= endT2 || startT1 == endT2)) {
			return endT2 - startT1 + 1;
		} else if (endT1 < endT2 && (startT1 <= startT2 || endT1 == startT2)) {
			return endT1 - startT2 + 1;
		} else if (startT1 >= startT2 && endT1 <= endT2) {
			return endT1 - startT1 + 1;
		}
		return 0;
	}

	public static String RoundDouble(double d, int place) {
		if (place <= 0) {
			return "" + (int) (d + ((d > 0) ? 0.5 : -0.5));
		}
		String s = "";
		if (d < 0) {
			s += "-";
			d = -d;
		}
		d += 0.5 * Math.pow(10, -place);
		if (d > 1) {
			int i = (int) d;
			s += i;
			d -= i;
		} else {
			s += "0";
		}
		if (d > 0) {
			d += 1.0;
			String f = "" + (int) (d * Math.pow(10, place));
			s += "." + f.substring(1);
		}
		return s;
	}

}
