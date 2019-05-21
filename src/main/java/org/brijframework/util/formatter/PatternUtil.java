package org.brijframework.util.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.brijframework.util.support.Patterns;

public class PatternUtil {

	public static String[] datetime = { Patterns.ddmmmyyyy_hmm_ampm, Patterns.ddmmyyyy_hmm_ampm, Patterns.yyyymmdd_HHmmss_SSSZ,
			Patterns.yyyymmdd_HHmmss_SSS, Patterns.yyyymmdd_HHmmss, Patterns.ddmmyyyy_HHmmss, Patterns.ddmyyy_hhmmss,Patterns. DDMMMYYYY_hmm_ampm, Patterns.DDMMYYYY_hmm_ampm,
			Patterns.ddmmyyyy_hmm, Patterns.ddmmyyyy_hmm, Patterns.MMDDYYYY_HHmm_ZONE,Patterns. MMDDYYYY_HHmm, Patterns.YYYYMMDD_HHmmss_SSSZ, Patterns.YYYYMMDD_HHmmss_SSS,
			Patterns.YYYYMMDD_HHmmss, Patterns.DDMMYYYY_HHmmss, Patterns.DDMMYYYY_hmm,Patterns. DDMMMYYYY_hmm, Patterns.EEEEEMMMMMyyyy_HHmmss_SSSZ,
			Patterns.DATE_TIME_STR_FORMAT_WITH_ZONE };
	public static String[] time = { Patterns.TIME_hmm_AMPM, Patterns.TIME_hhmm, Patterns.TIME_HHmm, };
	public static String[] date = { Patterns.DDMMYY, Patterns.DDMMYYYY, Patterns.MMDDYYYY, Patterns.YYYYDDMM, Patterns.YYYYMMDD, Patterns.ddmmyy, Patterns.ddmmyyyy, Patterns.YYYYDDMM,
			Patterns.mmddyyyy, Patterns.yyyymmdd };

	public static Map<String, String> patternMap() {
		HashMap<String, String> mapping = new HashMap<String, String>();
		return mapping;
	}

	public static String patternMatch(String date) {
		Set<Entry<String, String>> entries = patternMap().entrySet();
		for (Entry<String, String> entry : entries) {
			String pattern = entry.getValue();
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				if (dateFormat.parseObject(date) != null) {
					return pattern;
				}
			} catch (ParseException e) {

			}
		}
		return null;
	}

	public static String dateMatch(String dateStr) {
		for (String pattern : date) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				if (dateFormat.parseObject(dateStr) != null) {
					return pattern;
				}
			} catch (ParseException e) {

			}
		}
		return null;
	}

	public static String timeMatch(String timeStr) {
		for (String pattern : time) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				if (dateFormat.parseObject(timeStr) != null) {
					System.err.println();
					return pattern;
				}
			} catch (ParseException e) {

			}
		}
		return null;
	}

	/**
	 * Match a String against the given pattern, supporting the following simple
	 * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an
	 * arbitrary number of pattern parts), as well as direct equality.
	 * 
	 * @param pattern
	 *            the pattern to match against
	 * @param str
	 *            the String to match
	 * @return whether the String matches the given pattern
	 */
	public static String dateTimeMatch(String dateTimeStr) {
		for (String pattern : datetime) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				if (dateFormat.parseObject(dateTimeStr) != null) {
					return pattern;
				}
			} catch (ParseException e) {

			}
		}
		return null;
	}

	/**
	 * Match a String against the given pattern, supporting the following simple
	 * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an
	 * arbitrary number of pattern parts), as well as direct equality.
	 * 
	 * @param pattern
	 *            the pattern to match against
	 * @param str
	 *            the String to match
	 * @return whether the String matches the given pattern
	 */
	public static boolean simpleMatch(String pattern, String str) {
		if (pattern == null || str == null) {
			return false;
		}
		int firstIndex = pattern.indexOf('*');
		if (firstIndex == -1) {
			return pattern.equals(str);
		}
		if (firstIndex == 0) {
			if (pattern.length() == 1) {
				return true;
			}
			int nextIndex = pattern.indexOf('*', firstIndex + 1);
			if (nextIndex == -1) {
				return str.endsWith(pattern.substring(1));
			}
			String part = pattern.substring(1, nextIndex);
			int partIndex = str.indexOf(part);
			while (partIndex != -1) {
				if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
					return true;
				}
				partIndex = str.indexOf(part, partIndex + 1);
			}
			return false;
		}
		return (str.length() >= firstIndex && pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex))
				&& simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
	}

	/**
	 * Match a String against the given patterns, supporting the following simple
	 * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an
	 * arbitrary number of pattern parts), as well as direct equality.
	 * 
	 * @param patterns
	 *            the patterns to match against
	 * @param str
	 *            the String to match
	 * @return whether the String matches any of the given patterns
	 */
	public static boolean simpleMatch(String[] patterns, String str) {
		if (patterns != null) {
			for (int i = 0; i < patterns.length; i++) {
				if (simpleMatch(patterns[i], str)) {
					return true;
				}
			}
		}
		return false;
	}

}
