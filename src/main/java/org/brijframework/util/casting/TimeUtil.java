package org.brijframework.util.casting;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.brijframework.util.formatter.PatternUtil;
import org.brijframework.util.text.StringUtil;
import org.brijframework.util.validator.ValidationUtil;

public abstract class TimeUtil {
	private static Map<String, String> tzMap = new HashMap<>();
	static {
		tzMap.put("AKST", "AST");
		tzMap.put("AST", "PRT");
	}

	public static String timeZoneId(String tzId) {
		String zoneId = tzMap.get(tzId);
		return zoneId != null ? zoneId : tzId;
	}

	/**
	 * converts minute to HrsAndMin like (3:00) to minute
	 * 
	 * @param timeStr
	 * @return int
	 */
	public static String minutesToHrsAndMin(int minutes) {
		int hrs = minutes / 60;
		int min = minutes % 60;
		if (min == 0) {
			return hrs + ":00";
		} else {
			return hrs > 0 ? hrs + ":" + min : min + "";
		}
	}

	/**
	 * converts strings like (3:00PM,3:00pm,3:00AM,3:00am) to minute returns -1
	 * if corrupt string
	 * 
	 * @param timeStr
	 * @return int
	 */
	public static int getMinutes(String timeStr) {
		try {
			return minutes(timeStr);
		} catch (ParseException e) {
			return -1;
		}
	}

	/**
	 * converts strings like (3:00PM,3:00pm,3:00AM,3:00am) to minute
	 * 
	 * @param timeStr
	 * @return int
	 * @throws ParseException
	 */
	public static int minutes(String timeStr) throws ParseException {
		String time = timeStr.replaceAll("[a-zA-Z]", "").trim();
		DateFormat dateFormat = new SimpleDateFormat("hh:mm");
		Date date = dateFormat.parse(time);
		Calendar calendar = dateFormat.getCalendar();
		calendar.setTime(date);
		int minutes = (calendar.get(Calendar.HOUR) * 60) + calendar.get(Calendar.MINUTE);
		if (timeStr.toLowerCase().contains("pm")) {
			minutes += 720;
		}
		return minutes;
	}

	/**
	 * converts strings like (3:00PM,3:00pm,3:00AM,3:00am) to minute
	 * 
	 * @param timeStr
	 * @return date
	 * @throws ParseException
	 */
	public static Date getDateTime(String timeDateStr, String format) {
		if (StringUtil.isEmpty(timeDateStr)) {
			return null;
		}
		StringBuffer timeDate = new StringBuffer(timeDateStr);
		int pm = timeDate.indexOf("pm") < 0 ? timeDate.indexOf("PM") : timeDate.indexOf("pm");
		int am = timeDate.indexOf("am") < 0 ? timeDate.indexOf("AM") : timeDate.indexOf("am");
		if (pm > 0) {
			if (timeDate.charAt(pm - 1) != ' ')
				timeDate.insert(pm, ' ');
		}
		if (am > 0) {
			if (timeDate.charAt(am - 1) != ' ')
				timeDate.insert(am, ' ');
		}
		String formet = StringUtil.isNonEmpty(format) ? format : PatternUtil.dateTimeMatch(timeDate.toString());
		if (formet == null) {
			return null;
		}
		DateFormat timeFormat = new SimpleDateFormat(formet);
		Date date = null;
		try {
			date = timeFormat.parse(timeDate.toString());
		} catch (ParseException e) {
		}
		return date;
	}

	@SuppressWarnings("deprecation")
	public static Time getCurrentTime(String timeStr) {
		if (ValidationUtil.isEmptyObject(timeStr)) {
			return null;
		}
		StringBuffer time = new StringBuffer(timeStr);
		int pm = time.indexOf("pm") < 0 ? time.indexOf("PM") : time.indexOf("pm");
		int am = time.indexOf("am") < 0 ? time.indexOf("AM") : time.indexOf("am");
		if (pm > 0) {
			if (time.charAt(pm - 1) != ' ')
				time.insert(pm, ' ');
		}
		if (am > 0) {
			if (time.charAt(am - 1) != ' ')
				time.insert(am, ' ');
		}
		String formet = PatternUtil.timeMatch(time.toString());
		if (formet == null) {
			return null;
		}
		DateFormat dateFormat = new SimpleDateFormat(formet);
		Date date = null;
		try {
			date = dateFormat.parse(time.toString());
		} catch (ParseException e) {
			return null;
		}
		Date current = new Date();
		current.setHours(date.getHours());
		current.setMinutes(date.getMinutes());
		current.setSeconds(date.getSeconds());
		return new Time(current.getTime());
	}

	@SuppressWarnings("deprecation")
	public static Date getDateTime(Object timeStr) {
		if (ValidationUtil.isEmptyObject(timeStr)) {
			return null;
		}
		Time time = getCurrentTime(timeStr.toString());
		Date current = new Date();
		current.setHours(time.getHours());
		current.setMinutes(time.getMinutes());
		current.setSeconds(time.getSeconds());
		return current;
	}

	public static long timeDaysInterval(String time) {
		long miliseconds = timeMilisecondsInterval(time);
		return TimeUnit.DAYS.convert(miliseconds, TimeUnit.MILLISECONDS);
	}

	public static long timeHoursInterval(String time) {
		long miliseconds = timeMilisecondsInterval(time);
		return TimeUnit.HOURS.convert(miliseconds, TimeUnit.MILLISECONDS);
	}

	public static long timeMinutesInterval(String time) {
		long miliseconds = timeMilisecondsInterval(time);
		return TimeUnit.MINUTES.convert(miliseconds, TimeUnit.MILLISECONDS);
	}

	public static long timeSecondsInterval(String time) {
		long miliseconds = timeMilisecondsInterval(time);
		return TimeUnit.SECONDS.convert(miliseconds, TimeUnit.MILLISECONDS);
	}

	@SuppressWarnings("deprecation")
	public static long timeMilisecondsInterval(String time) {
		Date futureTime = getDateTime(time);
		Date date = new Date();
		long miliseconds = futureTime.getTime() - date.getTime();
		if (miliseconds < 0) {
			futureTime.setDate(futureTime.getDate() + 1);
			miliseconds = futureTime.getTime() - date.getTime();
			;
		}
		return miliseconds;
	}

	public static long timeMicroSecondsDateInterval(String future) {
		long miliseconds = (dateMilisecondsDateInterval(future));
		long seconds = TimeUnit.MICROSECONDS.convert(miliseconds, TimeUnit.MILLISECONDS);
		return seconds;
	}

	public static long timeNanoSecondsDateInterval(String future) {
		long miliseconds = (timeMilisecondsInterval(future));
		long seconds = TimeUnit.NANOSECONDS.convert(miliseconds, TimeUnit.MILLISECONDS);
		return seconds;
	}

	public static long dateDaysInterval(String future) {
		long miliseconds = (timeMilisecondsInterval(future));
		return TimeUnit.DAYS.convert(miliseconds, TimeUnit.MILLISECONDS);
	}

	public static long dateHoursDateInterval(String future) {
		long miliseconds = (dateMilisecondsDateInterval(future));
		return TimeUnit.HOURS.convert(miliseconds, TimeUnit.MILLISECONDS);
	}

	public static long dateMinutesDateInterval(String future) {
		long miliseconds = (dateMilisecondsDateInterval(future));
		return TimeUnit.MINUTES.convert(miliseconds, TimeUnit.MILLISECONDS);
	}

	public static long dateSecondsDateInterval(String future) {
		long miliseconds = (dateMilisecondsDateInterval(future));
		long seconds = TimeUnit.SECONDS.convert(miliseconds, TimeUnit.MILLISECONDS);
		return seconds;
	}

	public static long dateMilisecondsDateInterval(String future) {
		String formet = PatternUtil.dateTimeMatch(future);
		if (formet == null) {
			return 0;
		}
		DateFormat dateFormat = new SimpleDateFormat(formet);
		Date futureDate = null;
		try {
			futureDate = dateFormat.parse(future);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date date = new Date();
		long miliseconds = (futureDate.getTime() - date.getTime());
		return miliseconds;
	}

	public static long dateMicroSecondsDateInterval(String future) {
		long miliseconds = (dateMilisecondsDateInterval(future));
		long seconds = TimeUnit.MICROSECONDS.convert(miliseconds, TimeUnit.MILLISECONDS);
		return seconds;
	}

	public static long dateNanoSecondsDateInterval(String future) {
		long miliseconds = (dateMilisecondsDateInterval(future));
		long seconds = TimeUnit.NANOSECONDS.convert(miliseconds, TimeUnit.MILLISECONDS);
		return seconds;
	}

	public static long unitDifferenceFromTime(String time, TimeUnit unit) {
		long returnValue = 0;
		switch (unit) {
		case MILLISECONDS:
			returnValue = timeMilisecondsInterval(time);
			break;
		case SECONDS:
			returnValue = timeSecondsInterval(time);
			break;
		case MINUTES:
			returnValue = timeMinutesInterval(time);
			break;
		case HOURS:
			returnValue = timeHoursInterval(time);
			break;
		case DAYS:
			returnValue = timeDaysInterval(time);
			break;
		case MICROSECONDS:
			returnValue = timeMicroSecondsDateInterval(time);
			break;
		case NANOSECONDS:
			returnValue = timeNanoSecondsDateInterval(time);
			break;
		default:
			returnValue = timeMinutesInterval(time);
			break;
		}
		return returnValue;
	}

	public static long unitDifferenceFromDate(String future, TimeUnit unit) {
		long returnValue = 0;
		switch (unit) {
		case MILLISECONDS:
			returnValue = dateMilisecondsDateInterval(future);
			break;
		case SECONDS:
			returnValue = dateSecondsDateInterval(future);
			break;
		case MINUTES:
			returnValue = dateMinutesDateInterval(future);
			break;
		case HOURS:
			returnValue = dateHoursDateInterval(future);
			break;
		case DAYS:
			returnValue = dateDaysInterval(future);
			break;
		case MICROSECONDS:
			returnValue = dateMicroSecondsDateInterval(future);
			break;
		case NANOSECONDS:
			returnValue = dateNanoSecondsDateInterval(future);
			break;
		default:
			returnValue = dateMinutesDateInterval(future);
			break;
		}
		return returnValue;
	}

	/**
	 * converts value to miliseconds according to TimeUnit Defined
	 * 
	 * @param value
	 * @param unit
	 * @return long
	 */
	public static long miliseconds(long value, TimeUnit unit) {
		long returnValue = 0;
		switch (unit) {
		case MILLISECONDS:
			returnValue = TimeUnit.MILLISECONDS.toMillis(value);
			break;
		case SECONDS:
			returnValue = TimeUnit.SECONDS.toMillis(value);
			break;
		case MINUTES:
			returnValue = TimeUnit.MINUTES.toMillis(value);
			break;
		case HOURS:
			returnValue = TimeUnit.HOURS.toMillis(value);
			break;
		case DAYS:
			returnValue = TimeUnit.DAYS.toMillis(value);
			break;
		case MICROSECONDS:
			returnValue = TimeUnit.MICROSECONDS.toMillis(value);
			break;
		case NANOSECONDS:
			returnValue = TimeUnit.NANOSECONDS.toMillis(value);
			break;
		default:
			returnValue = value;
			break;
		}
		return returnValue;
	}

	/**
	 * converts value to seconds according to TimeUnit Defined
	 * 
	 * @param value
	 * @param unit
	 * @return long
	 */
	public static long seconds(long value, TimeUnit unit) {
		long returnValue = 0;
		switch (unit) {
		case MILLISECONDS:
			returnValue = TimeUnit.MILLISECONDS.toSeconds(value);
			break;
		case SECONDS:
			returnValue = TimeUnit.SECONDS.toSeconds(value);
			break;
		case MINUTES:
			returnValue = TimeUnit.MINUTES.toSeconds(value);
			break;
		case HOURS:
			returnValue = TimeUnit.HOURS.toSeconds(value);
			break;
		case DAYS:
			returnValue = TimeUnit.DAYS.toSeconds(value);
			break;
		case MICROSECONDS:
			returnValue = TimeUnit.MICROSECONDS.toSeconds(value);
			break;
		case NANOSECONDS:
			returnValue = TimeUnit.NANOSECONDS.toSeconds(value);
			break;
		default:
			returnValue = value;
			break;
		}
		return returnValue;
	}

	/**
	 * converts value to seconds according to TimeUnit Defined
	 * 
	 * @param value
	 * @param unit
	 * @return long
	 */
	public static long minutes(long value, TimeUnit unit) {
		long returnValue = 0;
		switch (unit) {
		case MILLISECONDS:
			returnValue = TimeUnit.MILLISECONDS.toMinutes(value);
			break;
		case SECONDS:
			returnValue = TimeUnit.SECONDS.toMinutes(value);
			break;
		case MINUTES:
			returnValue = TimeUnit.MINUTES.toMinutes(value);
			break;
		case HOURS:
			returnValue = TimeUnit.HOURS.toMinutes(value);
			break;
		case DAYS:
			returnValue = TimeUnit.DAYS.toMinutes(value);
			break;
		case MICROSECONDS:
			returnValue = TimeUnit.MICROSECONDS.toMinutes(value);
			break;
		case NANOSECONDS:
			returnValue = TimeUnit.NANOSECONDS.toMinutes(value);
			break;
		default:
			returnValue = value;
			break;
		}
		return returnValue;
	}

	/**
	 * converts value to seconds according to TimeUnit Defined
	 * 
	 * @param value
	 * @param unit
	 * @return long
	 */
	public static long hours(long value, TimeUnit unit) {
		long returnValue = 0;
		switch (unit) {
		case MILLISECONDS:
			returnValue = TimeUnit.MILLISECONDS.toHours(value);
			break;
		case SECONDS:
			returnValue = TimeUnit.SECONDS.toHours(value);
			break;
		case MINUTES:
			returnValue = TimeUnit.MINUTES.toHours(value);
			break;
		case HOURS:
			returnValue = TimeUnit.HOURS.toHours(value);
			break;
		case DAYS:
			returnValue = TimeUnit.DAYS.toHours(value);
			break;
		case MICROSECONDS:
			returnValue = TimeUnit.MICROSECONDS.toHours(value);
			break;
		case NANOSECONDS:
			returnValue = TimeUnit.NANOSECONDS.toHours(value);
			break;
		default:
			returnValue = value;
			break;
		}
		return returnValue;
	}

	/**
	 * converts value to seconds according to TimeUnit Defined
	 * 
	 * @param value
	 * @param unit
	 * @return long
	 */
	public static long days(long value, TimeUnit unit) {
		long returnValue = 0;
		switch (unit) {
		case MILLISECONDS:
			returnValue = TimeUnit.MILLISECONDS.toDays(value);
			break;
		case SECONDS:
			returnValue = TimeUnit.SECONDS.toDays(value);
			break;
		case MINUTES:
			returnValue = TimeUnit.MINUTES.toDays(value);
			break;
		case HOURS:
			returnValue = TimeUnit.HOURS.toDays(value);
			break;
		case DAYS:
			returnValue = TimeUnit.DAYS.toDays(value);
			break;
		case MICROSECONDS:
			returnValue = TimeUnit.MICROSECONDS.toDays(value);
			break;
		case NANOSECONDS:
			returnValue = TimeUnit.NANOSECONDS.toDays(value);
			break;
		default:
			returnValue = value;
			break;
		}
		return returnValue;
	}
}
