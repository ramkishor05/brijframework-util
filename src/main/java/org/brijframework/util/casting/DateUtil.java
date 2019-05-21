package org.brijframework.util.casting;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.brijframework.util.formatter.PatternUtil;
import org.brijframework.util.support.Patterns;
import org.brijframework.util.validator.ValidationUtil;

public abstract class DateUtil {
	
	public static final int DAY_UNITS = 1;
	public static final int MONTH_UNITS = 2;
	public static final int YEAR_UNITS = 3;
	
	static int month[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	// function for checking for Leap Year

	public static int isLeap(int y) {
		if ((y % 400 == 0) || ((y % 100 != 0) && (y % 4 == 0)))
			return 29;
		else
			return 28;
	}

	// function for checking date validation

	public static boolean dateValidate(int d, int m, int y) {
		month[2] = isLeap(y);
		if (m < 0 || m > 12 || d < 0 || d > month[m] || y < 0 || y > 9999)
			return false;
		else
			return true;
	}

	// function for finding day number from year = 1 till the inputted year

	public static int NumberOfDays(int d, int m, int y) {
		int dn = 0;
		month[2] = isLeap(y);
		for (int i = 1; i < m; i++) {
			dn = dn + month[i];
		}
		dn = dn + d;
		for (int i = 1; i < y; i++) {
			if (isLeap(i) == 29)
				dn = dn + 366;
			else
				dn = dn + 365;
		}
		return dn;
	}

	public static long dateDifference(String inputString1, String inputString2, String format) {
		SimpleDateFormat myFormat = new SimpleDateFormat(format);
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = myFormat.parse(inputString1);
			date2 = myFormat.parse(inputString2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long diff = date2.getTime() - date1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static Date dateObject(String inputString, String format) {
		SimpleDateFormat myFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = myFormat.parse(inputString);
		} catch (ParseException e) {
			System.err.println("WARN: unexpected object in Object.dateValue(): " + inputString);
		}
		return date;
	}

	public static Date dateObject(Object dateStr) {
		if (!ValidationUtil.isValidObject(dateStr)) {
			return null;
		}
		String formet = PatternUtil.dateMatch(dateStr.toString());
		if (formet == null) {
			return null;
		}
		DateFormat timeFormat = new SimpleDateFormat(formet);
		Date date = null;
		try {
			date = timeFormat.parse(dateStr.toString());
		} catch (ParseException e) {
			System.err.println("WARN: unexpected object in Object.dateValue(): " + dateStr);
		}
		return date;
	}

	public static Date sqlDateObject(Object dateStr) {
		if (!ValidationUtil.isValidObject(dateStr)) {
			return null;
		}
		String formet = PatternUtil.dateMatch(dateStr.toString());
		if (formet == null) {
			return null;
		}
		DateFormat timeFormat = new SimpleDateFormat(formet);
		Date date = null;
		try {
			date = timeFormat.parse(dateStr.toString());
		} catch (ParseException e) {
			System.err.println("WARN: unexpected object in Object.dateValue(): " + dateStr);
		}
		return new java.sql.Date(date.getTime());
	}

	public static Date sqlDateTimeObject(Object dateStr) {
		String formet = PatternUtil.dateTimeMatch(dateStr.toString());
		if (formet == null) {
			return null;
		}
		DateFormat timeFormat = new SimpleDateFormat(formet);
		Date date = null;
		try {
			date = timeFormat.parse(dateStr.toString());
		} catch (ParseException e) {
			System.err.println("WARN: unexpected object in Object.dateValue(): " + dateStr);
		}
		return new java.sql.Date(date.getTime());
	}
	
	public static Timestamp sqlTimestempObject(Object dateStr) {
		String formet = PatternUtil.dateTimeMatch(dateStr.toString());
		if (formet == null) {
			return null;
		}
		DateFormat timeFormat = new SimpleDateFormat(formet);
		Date date = null;
		try {
			date = timeFormat.parse(dateStr.toString());
		} catch (ParseException e) {
			System.err.println("WARN: unexpected object in Object.dateValue(): " + dateStr);
		}
		return new java.sql.Timestamp(date.getTime());
	}

	public static Calendar calenderForDate(String date, String seperator, String format) {
		if (format.compareTo("mmddyy") == 0) {
			int month = Integer.parseInt(date.substring(0, date.indexOf(seperator)));
			int year = Integer.parseInt(date.substring(date.lastIndexOf(seperator) + 1, date.length()));
			int day = Integer.parseInt(date.substring(date.indexOf(seperator) + 1, date.lastIndexOf(seperator)));
			if (year < 100) {
				year = year + 2000;

			}
			return calenderForDate(year, month, day);
		} else if (format.compareTo("ddmmyy") == 0) {
			int day = Integer.parseInt(date.substring(0, date.indexOf(seperator)));
			int year = Integer.parseInt(date.substring(date.lastIndexOf(seperator) + 1, date.length()));
			int month = Integer.parseInt(date.substring(date.indexOf(seperator) + 1, date.lastIndexOf(seperator)));
			if (year < 100) {
				year = year + 2000;

			}
			return calenderForDate(year, month, day);

		} else if (format.compareTo("yymmdd") == 0) {
			int year = Integer.parseInt(date.substring(0, date.indexOf(seperator)));
			int day = Integer.parseInt(date.substring(date.lastIndexOf(seperator) + 1, date.length()));
			int month = Integer.parseInt(date.substring(date.indexOf(seperator) + 1, date.lastIndexOf(seperator)));
			if (year < 100) {
				year = year + 2000;

			}
			return calenderForDate(year, month, day);
		} else if (format.compareTo("yyddmm") == 0) {
			int year = Integer.parseInt(date.substring(0, date.indexOf(seperator)));
			int month = Integer.parseInt(date.substring(date.lastIndexOf(seperator) + 1, date.length()));
			int day = Integer.parseInt(date.substring(date.indexOf(seperator) + 1, date.lastIndexOf(seperator)));
			if (year < 100) {
				year = year + 2000;
			}
			return calenderForDate(year, month, day);
		} else {
			System.err.println("WARN: unexpected object in Object.dateValue(): " + date);
		}
		return null;

	}

	public static Calendar currentDate(int offSet) {
		Calendar date = Calendar.getInstance();
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		date.add(Calendar.MILLISECOND, -1 * date.get(Calendar.MILLISECOND));
		date.add(Calendar.DAY_OF_YEAR, -1 * offSet);
		return date;
	}
	public static Calendar currentDate() {
		Calendar date = Calendar.getInstance();
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		date.add(Calendar.MILLISECOND, -1 * date.get(Calendar.MILLISECOND));
		date.add(Calendar.DAY_OF_YEAR, 0);
		return date;
	}
	
	public static java.sql.Date  currentSqlDate(){
		return new java.sql.Date(new Date().getTime());
	}
	
	public static java.util.Date  currentUtilDate(){
		return new Date();
	}
	

	public static Calendar offsetDate(Calendar workingDate, int offSet) {
		Calendar date = Calendar.getInstance();
		date.set(workingDate.get(Calendar.YEAR), workingDate.get(Calendar.MONTH),
				workingDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		date.add(Calendar.MILLISECOND, -1 * workingDate.get(Calendar.MILLISECOND));
		date.add(Calendar.DAY_OF_YEAR, offSet);
		return date;
	}

	/// Date Function
	public static String dateString(Calendar calendar) {
		String YY = (calendar.get(Calendar.YEAR) + "");
		String MM = ((calendar.get(Calendar.MONTH) + 1) + "");
		String DD = (calendar.get(Calendar.DAY_OF_MONTH) + "");
		if (MM.length() == 1) {
			MM = "0" + MM;
		}
		if (DD.length() == 1) {
			DD = "0" + DD;
		}
		return (YY + "_" + MM + "_" + DD);
	}

	public static String dateString(Calendar calendar, String format, String seprater) {
		String YY = (calendar.get(Calendar.YEAR) + "");
		String MM = ((calendar.get(Calendar.MONTH) + 1) + "");
		String DD = (calendar.get(Calendar.DAY_OF_MONTH) + "");
		String HH = (calendar.get(Calendar.HOUR) + "");
		String MINS = (calendar.get(Calendar.MINUTE) + "");

		if (MM.length() == 1) {
			MM = "0" + MM;
		}
		if (DD.length() == 1) {
			DD = "0" + DD;
		}

		if (format.compareTo("mmddyy") == 0) {
			return (MM + seprater + DD + seprater + YY);
		} else if (format.compareTo("ddmmyy") == 0) {
			return (DD + seprater + MM + seprater + YY);
		} else if (format.compareTo("yymmdd") == 0) {
			return (YY + seprater + MM + seprater + DD);
		} else if (format.compareTo("yyddmm") == 0) {
			return (YY + seprater + DD + seprater + MM);
		} else if (format.compareTo("mmddyy HH:MM") == 0) {
			return (MM + seprater + DD + seprater + YY + " " + HH + ":" + MINS);
		} else if (format.compareTo("ddmmyy HH:MM") == 0) {
			return (DD + seprater + MM + seprater + YY + " " + HH + ":" + MINS);
		} else if (format.compareTo("yymmdd HH:MM") == 0) {
			return (YY + seprater + MM + seprater + DD + " " + HH + ":" + MINS);
		} else if (format.compareTo("yyddmm HH:MM") == 0) {
			return (YY + seprater + DD + seprater + MM + " " + HH + ":" + MINS);
		} else {
			return (MM + seprater + DD + seprater + YY);
		}
	}

	public static String dateStringFormatted(Calendar date, String dateFormat) {

		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String formatedDate = formatter.format(date.getTime());
		return formatedDate;
	}

	public static String dateStringFormatted(Date date, String dateFormat) {

		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String formatedDate = formatter.format(date);
		return formatedDate;
	}

	public static String dateFileString(Calendar calendar) {
		String YY = (calendar.get(Calendar.YEAR) + "");
		String MM = ((calendar.get(Calendar.MONTH) + 1) + "");
		String DD = (calendar.get(Calendar.DAY_OF_MONTH) + "");
		if (MM.length() == 1) {
			MM = "0" + MM;
		}
		if (DD.length() == 1) {
			DD = "0" + DD;
		}
		return (MM + DD + YY.substring(2, 4));
	}

	/// Date Function
	public static String dateTimeString(Calendar calendar) {
		String YY = (calendar.get(Calendar.YEAR) + "");
		String MM = ((calendar.get(Calendar.MONTH) + 1) + "");
		String DD = (calendar.get(Calendar.DAY_OF_MONTH) + "");
		String HH = (calendar.get(Calendar.HOUR) + "");
		String MI = (calendar.get(Calendar.MINUTE) + "");
		String SC = (calendar.get(Calendar.SECOND) + "");
		String MS = (calendar.get(Calendar.MILLISECOND) + "");
		int AMint = (calendar.get(Calendar.AM_PM));
		String AMStr;
		if (AMint == 0) {
			AMStr = "AM";
		} else {
			AMStr = "PM";
		}
		if (MM.length() == 1) {
			MM = "0" + MM;
		}
		if (DD.length() == 1) {
			DD = "0" + DD;
		}
		if (HH.length() == 1) {
			HH = "0" + HH;
		}
		if (MI.length() == 1) {
			MI = "0" + MI;
		}
		if (SC.length() == 1) {
			SC = "0" + SC;
		}
		if (MS.length() == 1) {
			MS = "00" + MS;
		}
		if (MS.length() == 2) {
			MS = "0" + MS;
		}
		if (MS.length() == 3) {
			MS = MS + "";
		}

		return (YY + "_" + MM + "_" + DD + "_" + HH + "_" + MI + "_" + SC + "_" + MS + "_" + AMStr);
	}

	public static String dateStringSTD(Calendar calendar) {
		String YY = (calendar.get(Calendar.YEAR) + "");
		String MM = ((calendar.get(Calendar.MONTH) + 1) + "");
		String DD = (calendar.get(Calendar.DAY_OF_MONTH) + "");
		if (MM.length() == 1) {
			MM = "0" + MM;
		}
		if (DD.length() == 1) {
			DD = "0" + DD;
		}
		return (MM + "/" + DD + "/" + YY);
	}

	public static String dateStringSTD1(Calendar calendar) {
		String YY = (calendar.get(Calendar.YEAR) + "");
		String MM = ((calendar.get(Calendar.MONTH) + 1) + "");
		String DD = (calendar.get(Calendar.DAY_OF_MONTH) + "");
		if (MM.length() == 1) {
			MM = "0" + MM;
		}
		if (DD.length() == 1) {
			DD = "0" + DD;
		}
		return (YY.substring(2, 4) + MM + DD);
	}

	public static Calendar calenderForDate(String date) {
		int month = Integer.parseInt(date.substring(0, date.indexOf("/")));
		int year = Integer.parseInt(date.substring(date.lastIndexOf("/") + 1, date.length()));
		int day = Integer.parseInt(date.substring(date.indexOf("/") + 1, date.lastIndexOf("/")));
		if (year < 100) {
			year = year + 2000;
		}
		return calenderForDate(year, month, day);

	}

	public static Calendar calenderForDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, 0, 0, 0);
		cal.add(Calendar.MILLISECOND, -1 * cal.get(Calendar.MILLISECOND));
		return cal;
	}

	public static Calendar calenderForDate(int year, int month, int day, int min) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, 0, min, 0);
		cal.add(Calendar.MILLISECOND, -1 * cal.get(Calendar.MILLISECOND));
		return cal;
	}

	public static int differeceDays(Calendar first, Calendar second) {
		long diff = second.getTime().getTime() - first.getTime().getTime();
		int days = (int) ((diff) / (1000 * 60 * 60 * 24));
		return days;

	}

	public static long getTime(Object obj) {
		if (obj instanceof Date ) {
			return ((Date)obj).getTime();
		}
		else if ( obj instanceof java.sql.Date) {
			return 	((java.sql.Date)obj).getTime();
		}
		else if ( obj instanceof Time ) {
			return  ((Time)obj).getTime();
		}
		else  if (  obj instanceof Timestamp) {
			return ((Timestamp)obj).getTime();
		}
		return 0l;
	}

	public static String getyyyyMMMddDateString() {
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Patterns.yyyymmdd);
		return dateFormat.format(gc.getTime());
	}

	public static String getStandard1DateString() {
		return getFormattedCurrentDateTime(Patterns.EEEEEMMMMMyyyy_HHmmss_SSSZ);
	}

	public static String getFormattedCurrentDateTime(String pattern) {
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(gc.getTime());
	}
	

	public static String getDateInAnotherFormat(String dateString, String inputPattern, String outputPattern) {
		SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputPattern);
		inputDateFormat.setLenient(false);
		java.util.Date date = inputDateFormat.parse(dateString, new java.text.ParsePosition(0));
		SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputPattern);
		return outputDateFormat.format(date);
	}

	public static String getDateInAnotherFormat(String dateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
		java.util.Date date = inputDateFormat.parse(dateString, new java.text.ParsePosition(0));
		return outputDateFormat.format(date);
	}

	public static java.util.Date getDateForPattern(String dateString, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		dateFormat.setLenient(false);
		return dateFormat.parse(dateString, new java.text.ParsePosition(0));
	}

	public static GregorianCalendar getTodayBusiGC() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc;
	}

	public static Calendar getTodayBusiCal() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static Calendar getCalForDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Calendar getCalForDate(Date date, int noOfDaysAway) {
		Calendar cal = getCalForDate(date);
		cal.add(Calendar.DATE, noOfDaysAway);
		return cal;
	}

	public static Calendar getBusiCalForDate(Date date) {
		Calendar cal = getCalForDate(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static Calendar getBusiCalForDate(Date date, int noOfDaysAway) {
		Calendar cal = getBusiCalForDate(date);
		cal.add(Calendar.DATE, noOfDaysAway);
		return cal;
	}

	public static java.sql.Date getBusiDateForDate(Date date, int noOfDaysAway) {
		Calendar cal = getBusiCalForDate(date, noOfDaysAway);
		return getBusiSqlDateForCal(cal);
	}

	public static Timestamp getTimestampForCal(Calendar cal) {
		return new Timestamp(cal.getTimeInMillis());
	}

	public static java.sql.Date getBusiSqlDateForCal(Calendar cal) {
		return new java.sql.Date(cal.getTimeInMillis());
	}

	public static java.sql.Date getTodayBusiSqlDate() {
		return new java.sql.Date(getTodayBusiGC().getTime().getTime());
	}

	public static GregorianCalendar getFirstDayOfMonthGC() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.DAY_OF_MONTH, 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc;
	}

	public static java.sql.Date getFirstDayOfMonthSqlDate() {
		return new java.sql.Date(getFirstDayOfMonthGC().getTime().getTime());
	}

	public static GregorianCalendar getFirstDayOfYearGC() {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.MONTH, 0);
		gc.set(Calendar.DAY_OF_MONTH, 1);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		return gc;
	}

	public static java.sql.Date getFirstDayOfYearSqlDate() {
		return new java.sql.Date(getFirstDayOfYearGC().getTime().getTime());
	}

	public static GregorianCalendar getBusiGcXDaysAwayFromToday(int noOfDaysAway) {
		GregorianCalendar gc = getTodayBusiGC();
		gc.add(Calendar.DATE, noOfDaysAway);
		return gc;
	}

	public static Calendar getBusiCalXDaysAwayFromToday(int noOfDaysAway) {
		Calendar cal = getTodayBusiCal();
		cal.add(Calendar.DATE, noOfDaysAway);
		return cal;
	}

	public static java.sql.Date getBusiSqlDateXDaysAwayFromToday(int noOfDaysAway) {
		return new java.sql.Date(getBusiGcXDaysAwayFromToday(noOfDaysAway).getTime().getTime());
	}
	public GregorianCalendar getNextMidNightGC(GregorianCalendar gc) {
		GregorianCalendar tempGC = (GregorianCalendar) gc.clone();
		tempGC.add(Calendar.DATE, 1);
		tempGC.set(Calendar.HOUR_OF_DAY, 0);
		tempGC.set(Calendar.MINUTE, 0);
		tempGC.set(Calendar.SECOND, 0);
		tempGC.set(Calendar.MILLISECOND, 0);
		return tempGC;
	}

	public static Calendar getNextHour() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.HOUR_OF_DAY, 1);
		return cal;
	}

	public static Calendar getNextMinute() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MINUTE, 1);
		return cal;
	}

	public static Calendar getNextDay() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.add(Calendar.DATE, 1);
		return cal;
	}

	public static GregorianCalendar getGCForPattern(String dateString, String pattern) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(getDateForPattern(dateString, pattern));
		return gc;
	}
	

	public static String getDateStringForPattern(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	public static String getDateStringForPattern(GregorianCalendar gc, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(gc.getTime());
	}

	
	

	public static double getOverLap(GregorianCalendar startT1, GregorianCalendar endT1, GregorianCalendar startT2, GregorianCalendar endT2) {
		if (startT1.after(endT2) || endT1.before(startT2)) {
			return 0.0;
		}
		double overLap = 0.0;
		if ((startT1.before(startT2) || startT1.equals(startT2)) && (endT1.after(endT2) || endT1.equals(endT2))) {
			overLap = ((double) (endT2.getTimeInMillis() - startT2.getTimeInMillis())) / 1000 / 3600;
		} else if (startT1.after(startT2) && (endT1.after(endT2) || endT1.equals(endT2) || startT1.equals(endT2))) {
			overLap = ((double) (endT2.getTimeInMillis() - startT1.getTimeInMillis())) / 1000 / 3600;
		} else if (endT1.before(endT2) && (startT1.before(startT2) || startT1.equals(startT2) || endT1.equals(startT2))) {
			overLap = ((double) (endT1.getTimeInMillis() - startT2.getTimeInMillis())) / 1000 / 3600;
		} else if ((startT1.after(startT2) || startT1.equals(startT2)) && (endT1.before(endT2) || endT1.equals(endT2))) {
			overLap = ((double) (endT1.getTimeInMillis() - startT1.getTimeInMillis())) / 1000 / 3600;
		}
		return overLap;
	}

	// This method doesn't handle single points
	public static long getOverLapInSeconds(Date startT1, Date endT1, Date startT2, Date endT2) {
		if (startT1.after(endT2) || endT1.before(startT2)) {
			return 0;
		}
		long overLap = 0;
		if ((startT1.before(startT2) || startT1.equals(startT2)) && (endT1.after(endT2) || endT1.equals(endT2))) {
			overLap = (endT2.getTime() - startT2.getTime()) / 1000;
		} else if (startT1.after(startT2) && (endT1.after(endT2) || endT1.equals(endT2) || startT1.equals(endT2))) {
			overLap = (endT2.getTime() - startT1.getTime()) / 1000;
		} else if (endT1.before(endT2) && (startT1.before(startT2) || startT1.equals(startT2) || endT1.equals(startT2))) {
			overLap = (endT1.getTime() - startT2.getTime()) / 1000;
		} else if ((startT1.after(startT2) || startT1.equals(startT2)) && (endT1.before(endT2) || endT1.equals(endT2))) {
			overLap = (endT1.getTime() - startT1.getTime()) / 1000;
		}
		return overLap;
	}

	public static boolean isOverlap(Date startT1, Date endT1, Date startT2, Date endT2) {
		if (startT1.compareTo(endT2) > 0 || endT1.compareTo(startT2) < 0) {
			return false;
		}
		return true;
	}

	public static int getOverlapInDays(Date startT1, Date endT1, Date startT2, Date endT2) {
		if (startT1.compareTo(endT2) > 0 || endT1.compareTo(startT2) < 0) {
			return 0;
		}
		long overLap = 0;
		if (startT1.compareTo(startT2) <= 0 && endT1.compareTo(endT2) >= 0) {
			overLap = (endT2.getTime() - startT2.getTime()) / 1000 / 3600 / 24 + 1;
		} else if (startT1.compareTo(startT2) > 0 && (endT1.compareTo(endT2) >= 0 || startT1.compareTo(endT2) == 0)) {
			overLap = (endT2.getTime() - startT1.getTime()) / 1000 / 3600 / 24 + 1;
		} else if (endT1.compareTo(endT2) < 0 && (startT1.compareTo(startT2) <= 0 || endT1.compareTo(startT2) == 0)) {
			overLap = (endT1.getTime() - startT2.getTime()) / 1000 / 3600 / 24 + 1;
		} else if (startT1.compareTo(startT2) >= 0 && endT1.compareTo(endT2) <= 0) {
			overLap = (endT1.getTime() - startT1.getTime()) / 1000 / 3600 / 24 + 1;
		}
		return (int) overLap;
	}

	public static long getOverLap(long startT1, long endT1, long startT2, long endT2) {
		if (startT1 > endT2 || endT1 < startT2) {
			return 0;
		}
		long overLap = 0;
		if (startT1 <= startT2 && endT1 >= endT2) {
			overLap = endT2 - startT2;
		} else if (startT1 > startT2 && (endT1 >= endT2 || startT1 == endT2)) {
			overLap = endT2 - startT1;
		} else if (endT1 < endT2 && (startT1 <= startT2 || endT1 == startT2)) {
			overLap = endT1 - startT2;
		} else if (startT1 >= startT2 && endT1 <= endT2) {
			overLap = endT1 - startT1;
		}
		return overLap;
	}

	public static long getOverLapInclEnds(long startT1, long endT1, long startT2, long endT2) {
		if (startT1 > endT2 || endT1 < startT2) {
			return 0;
		}
		long overLap = 0;
		if (startT1 <= startT2 && endT1 >= endT2) {
			overLap = endT2 - startT2 + 1;
		} else if (startT1 > startT2 && (endT1 >= endT2 || startT1 == endT2)) {
			overLap = endT2 - startT1 + 1;
		} else if (endT1 < endT2 && (startT1 <= startT2 || endT1 == startT2)) {
			overLap = endT1 - startT2 + 1;
		} else if (startT1 >= startT2 && endT1 <= endT2) {
			overLap = endT1 - startT1 + 1;
		}
		return overLap;
	}

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getPeriodInPieces(GregorianCalendar startDate, GregorianCalendar endDate, int unitType, int noOfUnits) {
		if (startDate.after(endDate)) {
			throw new RuntimeException("endDate is before startDate");
		}
		Vector periodPieces = new Vector();
		GregorianCalendar gc = (GregorianCalendar) startDate.clone();
		while (true) {
			Hashtable hash = new Hashtable();
			hash.put("startDate", gc.clone());
			periodPieces.addElement(hash);
			switch (unitType) {
			case DAY_UNITS:
				gc.add(Calendar.DATE, noOfUnits);
				break;
			case MONTH_UNITS:
				gc.add(Calendar.MONTH, noOfUnits);
				break;
			case YEAR_UNITS:
				gc.add(Calendar.YEAR, noOfUnits);
				break;
			default:
				throw new RuntimeException("unitType could not be recognized");
			}
			if (gc.before(endDate)) {
				GregorianCalendar endGC = (GregorianCalendar) gc.clone();
				endGC.add(Calendar.SECOND, -1);
				hash.put("endDate", endGC);
			} else {
				hash.put("endDate", endDate.clone());
				break;
			}
		}
		return periodPieces;
	}
	

	public static int getOverlap(int startT1, int endT1, int startT2, int endT2) {
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

	public static boolean isDateOverlap(Date startT1, Date endT1, Date startT2, Date endT2) {
		if (startT2.compareTo(startT1) >= 0 && startT2.compareTo(endT1) <= 0) {
			return true;
		}
		if (endT2.compareTo(startT1) >= 0 && endT2.compareTo(endT1) <= 0) {
			return true;
		}
		return false;
	}

}
