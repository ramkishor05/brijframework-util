package org.brijframework.util.casting;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.LogicUnit;
import org.brijframework.util.text.StringUtil;
import org.brijframework.util.validator.ValidationUtil;

public abstract class CastingUtil {
	public static final String[] specialNames = { "", " thousand", " lakh", " carore", " carore", " arave", " arave",
			" karav" };
	public static final String[] tensNames = { "", " ten", " twenty", " thirty", " forty", " fifty", " sixty",
			" seventy", " eighty", " ninety" };
	public static final String[] numNames = { "", " one", " two", " three", " four", " five", " six", " seven",
			" eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen",
			" seventeen", " eighteen", " nineteen" };
	public static final Class<?> _ObjectClass = Object.class;
	public static final Class<?> _ClassClass = Class.class;
	public static final Class<?> _StringClass = String.class;
	public static final Class<?> _NumberClass = Number.class;
	public static final Class<?> _BigDecimalClass = BigDecimal.class;
	public static final Class<?> _BigIntegerClass = BigInteger.class;
	public static final Class<?> _BooleanClass = Boolean.class;
	public static final Class<?> _DateClass = Date.class;
	public static final Class<?> _LocaleClass = Locale.class;
	public static final Class<?> _ShortClass = Short.class;
	public static final Class<?> _ByteClass = Byte.class;
	public static final Class<?> _IntegerClass = Integer.class;
	public static final Class<?> _LongClass = Long.class;
	public static final Class<?> _DoubleClass = Double.class;
	public static final Class<?> _FloatClass = Float.class;
	public static final Class<?> _VoidClass = Void.class;
	public static final Class<?> _CharacterClass = Character.class;
	public static final Short _shortOff = new Short((short) 0);
	public static final Short _shortOn = new Short((short) 1);

	public static final Short _zeroShort = Short.valueOf("0");
	public static final Integer _zeroInteger = Integer.valueOf(0);
	public static final Double _zeroDobule = Double.valueOf(0d);

	public static final BigDecimal _zeroBigDecimal = BigDecimal.valueOf(0L);
	public static final BigInteger _zeroBigInteger = BigInteger.valueOf(0L);

	private static final Integer _integers[];
	private static final String undefined = "undefined";

	static {
		_integers = new Integer[517];
		char c = '\u0204';
		for (int i = 0; i < c; i++) {
			_integers[i] = Integer.valueOf(i - 3);
		}
	}
	
	public static Object defaultValue(Field field){
		if(field==null){
			return null;
		}
		if(String.class.isAssignableFrom(field.getType())){
			return stringValue(null);
		}
		if(Float.class.isAssignableFrom(field.getType())){
			return floatValue(null);
		}
		if(Double.class.isAssignableFrom(field.getType())){
			return doubleValue(null);
		}
		if(Integer.class.isAssignableFrom(field.getType())){
			return intValue(null);
		}
		if(Short.class.isAssignableFrom(field.getType())){
			return shortValue(null);
		}
		if(Boolean.class.isAssignableFrom(field.getType())){
		   return false;
		}
		if(Character.class.isAssignableFrom(field.getType())){
			return new Character((char) 0);
		}
		if(Long.class.isAssignableFrom(field.getType())){
			return longValue(null);
		}
		return null;
	}

	/**
	 * Returns an short value for the given object. It follows this sequence:
	 * <br>
	 * null = 0 <br>
	 * String - if length=0, returns 0. <br>
	 * If the string contains a decimal point, parse as <br>
	 * BigDecimal(s).shortValue() representation of the object.
	 * 
	 * @param v
	 *            - some value object, can be null
	 * @return the short value represented by the object
	 */
	public static short shortValue(final Object v) {
		if (v == null) {
			return 0;
		}
		if (v instanceof Number) {
			return ((Number) v).shortValue();
		}
		if (v instanceof String) {
			String s = (String) v;
			if (s.trim().length() == 0 || s.equals("null")) {
				return 0;
			}
			try {
				if (s.indexOf('.') >= 0) {
					return new BigDecimal(s).shortValue();
				}
				return Short.parseShort(s);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return shortValue(v.toString());
	}

	/**
	 * Returns an float value for the given object. It follows this sequence:
	 * <br>
	 * null = 0 <br>
	 * String - if length=0, returns 0. <br>
	 * If the string contains a decimal point, parse as <br>
	 * BigDecimal(s).intValue() representation of the object.
	 * 
	 * @param v
	 *            - some value object, can be null
	 * @return the int value represented by the object
	 */
	public static int intValue(final Object v) {
		if (v == null) {
			return 0;
		}
		if (v instanceof Number) {
			return ((Number) v).intValue();
		}
		if (v instanceof String) {
			String s = (String) v;
			if (s.trim().length() == 0 || s.equals("null")) {
				return 0;
			}
			try {
				if (s.indexOf('.') >= 0) {
					return new BigDecimal(s).intValue();
				}
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return intValue(v.toString());
	}

	/**
	 * Returns an long value for the given object. It follows this sequence:
	 * <br>
	 * null = 0 <br>
	 * String - if length=0, returns 0. <br>
	 * If the string contains a decimal point, parse as <br>
	 * BigDecimal(s).longValue() representation of the object.
	 * 
	 * @param v
	 *            - some value object, can be null
	 * @return the long value represented by the object
	 */
	public static long longValue(final Object v) {
		if (v == null) {
			return 0;
		}
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		if (v instanceof String) {
			String s = (String) v;
			if (s.length() == 0 || s.equals("null")) {
				return 0;
			}
			try {
				if (s.indexOf('.') >= 0) {
					return new BigDecimal(s).longValue();
				}
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return longValue(v.toString());
	}

	/**
	 * Returns an float value for the given object. It follows this sequence:
	 * <br>
	 * null = 0.0f <br>
	 * String - if length=0, returns 0.0f. <br>
	 * If the string contains a decimal point, parse as <br>
	 * BigDecimal(s).floatValue() representation of the object.
	 * 
	 * @param v
	 *            - some value object, can be null
	 * @return the float value represented by the object
	 */
	public static float floatValue(final Object v) {
		if (v == null) {
			return 0.0f;
		}
		if (v instanceof Number) {
			return ((Number) v).floatValue();
		}
		if (v instanceof String) {
			String s = (String) v;
			if (s.length() == 0 || s.equals("null")) {
				return 0.0f;
			}
			try {
				if (s.indexOf('.') >= 0) {
					return new BigDecimal(s).floatValue();
				}
				return Float.parseFloat(s);
			} catch (NumberFormatException e) {
				return 0.0f;
			}
		}
		return floatValue(v.toString());
	}

	/**
	 * Returns an float value for the given object. It follows this sequence:
	 * <br>
	 * null = 0.0 <br>
	 * String - if length=0, returns 0.0. <br>
	 * If the string contains a decimal point, parse as <br>
	 * BigDecimal(s).doubleValue() representation of the object.
	 * 
	 * @param v
	 *            - some value object, can be null
	 * @return the double value represented by the object
	 */
	public static double doubleValue(final Object v) {
		if (v == null) {
			return 0.0;
		}
		if (v instanceof Number) {
			return ((Number) v).doubleValue();
		}
		if (v instanceof String) {
			String s = (String) v;
			if (s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("on")
					|| s.equalsIgnoreCase("true")) {
				return 1.0;
			}
			if (s.trim().length() == 0 || s.equals("null")) {
				return 0.0;
			}
			try {
				if (s.indexOf('.') >= 0) {
					return new BigDecimal(s).doubleValue();
				}
				return Double.parseDouble(s);
			} catch (NumberFormatException e) {
				return 0.0;
			}
		}
		return doubleValue(v.toString());
	}

	public static boolean boolValue(final String s) {
		if(s==null) {
			return false;
		}
		if (s.equals("") || s.equals("null")) {
			return false;
		} else if (s.equalsIgnoreCase("n") || s.equalsIgnoreCase("no")) {
			return false;
		} else if (s.equalsIgnoreCase("f") || s.equalsIgnoreCase("false")) {
			return false;
		} else if (s.equalsIgnoreCase("u") || s.equalsIgnoreCase("undefined")) {
			return false;
		} else if (s.equalsIgnoreCase("0") || s.equalsIgnoreCase("zero") || s.equalsIgnoreCase("off")) {
			return false;
		} else if (s.equalsIgnoreCase("y") || s.equalsIgnoreCase("YES")) {
			return true;
		} else if (s.equalsIgnoreCase("t") || s.equalsIgnoreCase("true")) {
			return true;
		} else if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("one") || s.equalsIgnoreCase("on")) {
			return true;
		}
		return false;
	}
	/**
	 * Returns an boolean value for the given object. <br>
	 * It follows this sequence: <br>
	 * null = false <br>
	 * String - if length=0, returns false.
	 * 
	 * @param v
	 *            - some value object, can be null
	 * @return the boolean value represented by the object
	 */
	public static boolean boolValue(final Object v) {
		if (v == null) {
			return false;
		} else if (v instanceof String) {
			return boolValue((String) v);
		} else if (v instanceof Character) {
			char c0 = (char) v;
			if ((c0 == 'N' || c0 == 'n')) {
				return false;
			} else if ((c0 == 'f' || c0 == 'F')) {
				return false;
			} else if ((c0 == 'u' || c0 == 'U')) {
				return false;
			} else if ((c0 == 'y' || c0 == 'Y')) {
				return true;
			} else if ((c0 == 't' || c0 == 'T')) {
				return true;
			}
		} else if (v instanceof Boolean) {
			return ((Boolean) v).booleanValue();
		} else if (v instanceof Number) {
			return ((Number) v).intValue() != 0;
		} else if (v instanceof Collection) {
			return ((Collection<?>) v).size() > 0;
		}
		return false;
	}

	private static Character charValue(Object v) {
		if (v == null) {
			return ' ';
		}
		if (v instanceof Character) {
			return ((Character) v).charValue();
		}
		if (v instanceof Number) {
			return (char) ((Number) v).intValue();
		}
		if (v instanceof String) {
			String s = (String) v;
			if (s.trim().length() == 0 || s.equals("null")) {
				return ' ';
			}
			if (s.trim().length() == 1) {
				return s.charAt(0);
			}
		}
		return null;
	}

	/**
	 * Returns a String representing the object. This has special processing for
	 * arrays, which are rendered using the stringValueForArray method.
	 * 
	 * @param _value
	 *            - value to convert to a String
	 * @return a String representing the object _value
	 */
	public static String stringValue(final Object _value) {
		if (_value == null) {
			return null;
		}
		if (_value instanceof String) {
			return String.valueOf(_value);
		}
		if (_value instanceof Object[]) {
			return stringValueForArray((Object[]) _value);
		}
		return _value.toString();
	}

	/**
	 * 
	 * <p>
	 * This method will return string for the specified array.
	 * </p>
	 * 
	 * 
	 * 
	 * @param _array
	 * @return String value by appendin all the elements in a string.
	 * @since Brijframework 1.0
	 * 
	 */
	public static String stringValueForArray(final Object[] _array) {
		if (_array == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(_array.length * 16);
		sb.append("[ ");
		boolean isFirst = true;
		for (Object o : _array) {
			String s = stringValue(o);
			if (s == null) {
				s = " ";
			}
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append(", ");
			}
			sb.append(s);
		}
		sb.append(" ]");
		return sb.toString();
	}

	/**
	 * Returns a java.util.Date for the given object. This method checks:
	 * <ul>
	 * <li>for null, which is returned as null
	 * <li>for Date, which is returned as-is
	 * <li>for java.util.Calendar, getTime() will be called and returned
	 * <li>for String's. Which will get parsed using the default DateFormat.
	 * <li>for Number's, which are treated like ms since 1970
	 * </ul>
	 * All other objects are checked for a 'dateValue' method, which is then
	 * called.
	 * 
	 * @param _v
	 *            - some object
	 * @param _style
	 *            the given formatting style. For example, SHORT for "M/d/yy" in
	 *            the US locale.
	 * @param _locale
	 *            the given locale.
	 * 
	 * @return a java.util.Date or null
	 */
	public static Date dateValue(final Object _v, final int _style, final Locale _locale) {
		if (_v == null) {
			return null;
		}
		if (_v instanceof Date) {
			return (Date) _v;
		}
		if (_v instanceof Calendar) {
			return ((Calendar) _v).getTime();
		}
		if (_v instanceof String) {
			String s = ((String) _v).trim();
			if (s.length() == 0) {
				return null;
			}
			/* Rhino hack */
			if ("undefined".equals(s)) {
				return null;
			}
			if (ValidationUtil.isValidLong(s)) {
				return new Date(Long.parseLong(s));
			}
			DateFormat df = DateFormat.getDateInstance(_style, _locale);
			try {
				return df.parse(s.replaceAll("_", "/"));
			} catch (Exception _e) {
			}
		}
		if (_v instanceof Number) {
			return new Date(((Number) _v).longValue());
		}
		try {
			Method m = _v.getClass().getMethod("dateValue");
			if (m != null) {
				Object v = m.invoke(_v);
				if (v == null) {
					return null;
				}
				if (v != _v) {
					return dateValue(v, _style, _locale);
				}
				return null;
			}
		} catch (Exception e) {
		}
		System.err.println("WARN: unexpected object in Object.dateValue(): " + _v);
		return null;
	}

	/**
	 * Returns a java.util.Date for the given object. This method checks:
	 * <ul>
	 * <li>for null, which is returned as null
	 * <li>for Date, which is returned as-is
	 * <li>for java.util.Calendar, getTime() will be called and returned
	 * <li>for String's. Which will get parsed using the default DateFormat.
	 * <li>for Number's, which are treated like ms since 1970
	 * </ul>
	 * All other objects are checked for a 'dateValue' method, which is then
	 * called.
	 * 
	 * @param _v
	 *            - some object
	 * 
	 * @return a java.util.Date or null
	 */
	public static Date dateValue(final Object _v) {
		return dateValue(_v, DateFormat.SHORT, Locale.getDefault());
	}

	public static Timestamp timeStampValue(final Object _v, final int _style, final Locale _locale) {
		if (_v == null) {
			return null;
		}
		if (_v instanceof Timestamp) {
			return (Timestamp) _v;
		}
		if (_v instanceof Calendar) {
			return new Timestamp(((Calendar) _v).getTimeInMillis());
		}
		if (_v instanceof String) {
			String s = ((String) _v).trim();
			if (s.length() == 0) {
				return null;
			}
			/* Rhino hack */
			if (undefined.equals(s)) {
				return null;
			}
			if (ValidationUtil.isValidLong(s)) {
				return new Timestamp(Long.parseLong(s));
			}
			DateFormat df = DateFormat.getDateTimeInstance(_style, DateFormat.FULL, _locale);
			try {
				return new Timestamp(df.getCalendar().getTimeInMillis());
			} catch (Exception _e) {
			}
		}
		if (_v instanceof Number) {
			return new Timestamp(((Number) _v).longValue());
		}
		/* other object */
		try {
			Method m = _v.getClass().getMethod("timeStampValue");
			if (m != null) {
				Object v = m.invoke(_v);
				if (v == null) {
					return null;
				}
				if (v != _v) {
					return timeStampValue(v, _style, _locale);
				}
				return null;
			}
		} catch (Exception e) {
		}
		System.err.println("WARN: unexpected object in Object.dateValue(): " + _v);
		return null;
	}

	public static Date timeStampValue(final Object _v) {
		return timeStampValue(_v, DateFormat.SHORT, Locale.getDefault());
	}

	public static Object castObject(Object _value, Type type) {
		return castObject(_value, ( Class<?> )type);
	} 
	
	public static int valueInt(Object _value) {
		return (int)castObject(_value, int.class);
	}
	
	public static float valueFloat(Object  _value) {
		return (float)castObject(_value, float.class);
	}
	
	@SuppressWarnings({ "unchecked" })
	public static Object castObject(Object _value, Class<?> type) {
		if (Boolean.class.isAssignableFrom(type)|| type.toString().equals("boolean")) {
			return boolValue(_value);
		}
		if (Short.class.isAssignableFrom(type) || type.toString().equals("boolean")) {
			return shortValue(_value);
		}
		if (Integer.class.isAssignableFrom(type) || type.toString().equals("int")) {
			return intValue(_value);
		}
		if (Long.class.isAssignableFrom(type)|| type.toString().equals("long")) {
			return longValue(_value);
		}
		if (Float.class.isAssignableFrom(type)|| type.toString().equals("float")) {
			return floatValue(_value);
		}
		if (Double.class.isAssignableFrom(type)|| type.toString().equals("double")) {
			return doubleValue(_value);
		}
		if (Character.class.isAssignableFrom(type)|| type.toString().equals("char")) {
			return charValue(_value);
		}
		if (_value == null) {
			return null;
		}
		if(type.isAssignableFrom(_value.getClass())){
			return _value;
		}
		if (Enum.class.isAssignableFrom(type)) {
			Object[] objects = type.getEnumConstants();
			for (Object object : objects) {
				if (object.toString().equals(_value.toString())) {
					return object;
				}
			}
			return null;
		}

		if (Class.class.isAssignableFrom(type)) {
			if(String.class.isAssignableFrom(_value.getClass())){
				return _value.toString();
			}
		}
		
		if (String.class.isAssignableFrom(type)) {
			return stringValue(_value);
		}
		if (Date.class.isAssignableFrom(type)) {
			return dateValue(_value);
		}
		if (java.sql.Date.class.isAssignableFrom(type)) {
			return DateUtil.sqlDateObject(_value);
		}
		if (java.sql.Timestamp.class.isAssignableFrom(type)) {
			return DateUtil.sqlTimestempObject(_value);
		}
		if (Map.class.isAssignableFrom(_value.getClass())) {
			Object inObject = InstanceUtil.getInstance(type);
			Map<String, Object> _map = (Map<String, Object>) _value;
			for (String key : _map.keySet()) {
				LogicUnit.setField(inObject, key, _map.get(key));
			}
			return inObject;
		}
		if(_value!=null && !type.isAssignableFrom(_value.getClass())){
			return null;
		}
		return _value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T castObject(Object _value, Class<?> type, Class<?> paramType) {
		if (Collection.class.isAssignableFrom(type)) {
			Collection inObject = (Collection<?>) InstanceUtil.getInstance(type);
			if (paramType != null) {
				if (ValidationUtil.isPrimative(paramType)) {
					inObject.addAll((Collection) _value);
				} else {
					for (Object _obj : (Collection) _value) {
						Object _bean = InstanceUtil.getInstance(paramType);
						if (_obj instanceof Map) {
							LogicUnit.setAllField(_bean, (Map) _obj);
						}
						inObject.add(_bean);
					}
				}
			} else {
				inObject.addAll((Collection) _value);
			}
			return (T) inObject;
		} else {
			return (T) castObject(_value, type);
		}
	}

	/**
	 * Convert from Binary/Octal/Hexadecimal format to Decimal format.
	 * 
	 * @param value
	 *            Binary/Octal/Hexadecimal input
	 * @param base
	 *            2/8/16 input
	 * @return its equivalent Decimal value
	 * @throws NumberFormatException
	 *             If {@code value} is not corresponding to {@code base}
	 *             argument.
	 */
	public static String toDecimal(String value, int base) {
		int pow = 0;
		long result = 0;
		for (char c : StringUtil.reverseText(value).toCharArray()) {
			result += (int) (Math.pow(base, pow++) * Character.getNumericValue(c));
		}
		return String.valueOf(result);
	}

	/**
	 * Convert from Decimal format to Binary/Octal/Hexadecimal format.
	 * 
	 * @param value
	 *            Decimal value
	 * @param base
	 *            2/8/16
	 * @return its equivalent Binary/Octal/Hexadecimal value
	 * 
	 * @throws NumberFormatException
	 *             If {@code value} is not corresponding to {@code base}
	 *             argument.
	 */

	public static String decimalToAnyNumber(String value, int base) {

		long data = Long.valueOf(value);
		StringBuffer strBuffer = new StringBuffer();
		if (base == 16) {
			String hex[] = { "A", "B", "C", "D", "E", "F" };
			while (data > 0) {
				int r = (int) data % base;
				data /= base;
				strBuffer.append((r < 10 ? r : hex[r - 10]));
			}
			return StringUtil.reverseText(strBuffer.toString());
		}
		while (data > 0) {
			strBuffer.append(data % base);
			data /= base;
		}
		return StringUtil.reverseText(strBuffer.toString());
	}

	/**
	 * Convert from Binary format to Octal format.
	 * 
	 * @param binary
	 *            Binary Number
	 * @return its equivalent Octal value
	 * 
	 * @throws NumberFormatException
	 *             If {@code binary} not in correct format.
	 */
	public static String binaryToOctal(String binary) {
		return decimalToAnyNumber(toDecimal(binary, 2), 8);
	}

	/**
	 * Convert from Binary format to Hexadecimal format.
	 * 
	 * @param binary
	 *            Binary Number
	 * @return its equivalent Hexadecimal value
	 * 
	 * @throws NumberFormatException
	 *             If {@code binary} not in correct format.
	 */
	public static String binaryToHexa(String binary) {
		return decimalToAnyNumber(toDecimal(binary, 2), 16);
	}

	/**
	 * Convert from Octal format to Binary format.
	 * 
	 * @param octal
	 *            Octal Number
	 * @return its equivalent Binary value
	 * 
	 * @throws NumberFormatException
	 *             If {@code octal} not in correct format.
	 */
	public static String octalToBinary(String octal) {
		return decimalToAnyNumber(toDecimal(octal, 8), 2);
	}

	/**
	 * Convert from Octal format to Hexadecimal format.
	 * 
	 * @param octal
	 *            Octal Number
	 * @return its equivalent Hexadecimal value
	 * 
	 * @throws NumberFormatException
	 *             If {@code octal} not in correct format.
	 */
	public static String octalToHexa(String octal) {
		return decimalToAnyNumber(toDecimal(octal, 8), 16);
	}

	/**
	 * Convert from Hexadecimal format to Binary format.
	 * 
	 * @param hexa
	 *            Hexadecimal Number
	 * @return its equivalent Binary value
	 * 
	 * @throws NumberFormatException
	 *             If {@code hexa} not in correct format.
	 */
	public static String hexaToBinary(String hexa) {
		return decimalToAnyNumber(toDecimal(hexa, 16), 2);
	}

	/**
	 * Convert from Hexadecimal format to Octal format.
	 * 
	 * @param hexa
	 *            Hexadecimal Number
	 * @return its equivalent Octal value
	 * 
	 * @throws NumberFormatException
	 *             If {@code hexa} not in correct format.
	 */
	public static String hexaToOctal(String hexa) {
		return decimalToAnyNumber(toDecimal(hexa, 16), 8);
	}

	public static Double getDoubleFromString(String val1) {
		val1 = val1.trim();
		if (val1.startsWith("(") && val1.endsWith(")")) {
			return new Double(new Double(val1.substring(1, val1.length() - 1)).doubleValue() * -1.0);
		} else {
			return new Double(val1);
		}
	}

	public static int getIntforBoolean(boolean value) {
		if (value == false) {
			return 0;
		} else {
			return 1;
		}

	}

	public static boolean getBooleanValue(Integer i) {
		if (i.intValue() == 1) {
			return true;
		} else {
			return false;
		}

	}

	public static Double getDoubleFromDict(Hashtable<?, ?> hash, String key) {
		Object o = hash.get(key);
		if (o instanceof String) {
			return new Double(Double.parseDouble((String) o));
		}
		Number numb = (Number) o;
		if (numb == null) {
			return new Double(0.0);
		} else {
			return new Double(numb.doubleValue());
		}
	}

	/**
	 * Converts a positive integer from one radix into another
	 * 
	 * @param in
	 *            positive integer
	 * @param inRadix
	 *            input Radix
	 * @param outRadix
	 *            output Radix
	 * @return positive integer equivalent to {@code outRadix}
	 */
	public static String toRadix(String integer, int inRadix, int outRadix) {
		if (outRadix == inRadix) {
			return integer;
		}
		if (outRadix == 10) {
			return toDecimal(integer, inRadix);
		}
		if (inRadix == 10) {
			return decimalToAnyNumber(integer, outRadix);
		}
		return decimalToAnyNumber(toDecimal(integer, inRadix), outRadix);
	}

	/**
	 * convert byte to hex
	 *
	 * @param Byte
	 *            array which need to convert into hex string
	 * @return Hex String
	 **/
	public static String byteArrayToHex(byte[] a) {
		int hn, ln, cx;
		String hexDigitChars = "0123456789abcdef";
		StringBuffer buf = new StringBuffer(a.length * 2);
		for (cx = 0; cx < a.length; cx++) {
			hn = ((a[cx]) & 0x00ff) / 16;
			ln = ((a[cx]) & 0x000f);
			buf.append(hexDigitChars.charAt(hn));
			buf.append(hexDigitChars.charAt(ln));
		}
		return buf.toString();
	}

	/**
	 * Converts number into day word
	 * 
	 * @param number
	 * 
	 * @return String day
	 */
	public static String dayInWord(int day) {
		String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thesday", "Friday", "Saturday" };
		return (0 <= day && day < 7) ? days[day] : "Not Valid Day Number";
	}

	private static String convertLessThanOneThousand(int number) {
		String current;
		if (number % 100 < 20) {
			current = numNames[number % 100];
			number /= 100;
		} else {
			current = numNames[number % 10];
			number /= 10;
			current = tensNames[number % 10] + current;
			number /= 10;
		}
		if (number == 0) {
			return current;
		}
		return numNames[number] + " hundred" + current;
	}

	/**
	 * Converts number into String word
	 * 
	 * @param number
	 * 
	 * @return String word
	 */
	public static String numberInWord(long number) {

		if (number == 0) {
			return "zero";
		}
		String prefix = "";
		if (number < 0) {
			number = -number;
			prefix = "negative";
		}
		String current = "";
		int place = 0;
		do {
			long n = number % 1000;
			if (n != 0) {
				String s = convertLessThanOneThousand((int) n);
				current = s + specialNames[place] + current;
			}
			place++;
			number /= 1000;
		} while (number > 0);
		return (prefix + current).trim();
	}

	/**
	 * Converts <code>int</code> value to {@link Integer}
	 * 
	 * @param i
	 * @return
	 */
	public static Integer integerForInt(int i) {
		if (i < 513 && i >= -3) {
			return _integers[i + 3];
		} else {
			return Integer.valueOf(i);
		}
	}

	public static Object dobuleTOshort(Double d) {
		return (short) (d * 2.0);
	}

	/**
	 * Converts objects(Boolean or Number) into compatible {@link Object}
	 * 
	 * @param boolean1
	 *            {@link Boolean}
	 * @param class1
	 *            {@link Class}
	 * @return {@link Number}
	 */
	public static Object convertNumberOrBooleanIntoCompatibleValue(Object obj, Class<?> class1) {
		if (obj != null) {
			Class<?> class2 = obj.getClass();
			if (ValidationUtil.isClassAsBoolean(class1)) {
				return class2 != _BooleanClass ? convertNumberIntoBooleanValue((Number) obj) : obj;
			} else {
				return class2 != _BooleanClass ? convertNumberIntoCompatibleValue((Number) obj, class1)
						: convertBooleanIntoCompatibleNumberValue((Boolean) obj, class1);
			}
		} else {
			return obj;
		}
	}

	/**
	 * Converts {@link Boolean} into compatible {@link Number} value
	 * 
	 * @param boolean1
	 *            {@link Boolean}
	 * @param class1
	 *            {@link Class}
	 * @return {@link Number}
	 */
	public static Number convertBooleanIntoCompatibleNumberValue(Boolean boolean1, Class<?> class1) {
		return convertNumberIntoCompatibleValue(convertBooleanIntoNumberValue(boolean1), class1);
	}

	/**
	 * Converts {@link Number} into compatible {@link Number} value
	 * 
	 * @param number
	 *            {@link Number}
	 * @param class1
	 *            {@link Class}
	 * @return {@link Number}
	 */
	public static Number convertNumberIntoCompatibleValue(Number number, Class<?> class1) {
		if (number != null) {
			Class<?> class2 = number.getClass();
			if (class2 != class1) {
				if (class1 == Integer.TYPE || class1 == _IntegerClass) {
					return integerForInt(number.intValue());
				}
				if (class1 == Double.TYPE || class1 == _DoubleClass) {
					return new Double(number.doubleValue());
				}
				if (class1 == Float.TYPE || class1 == _FloatClass) {
					return new Float(number.floatValue());
				}
				if (class1 == Long.TYPE || class1 == _LongClass) {
					return new Long(number.longValue());
				}
				if (class1 == Short.TYPE || class1 == _ShortClass) {
					return new Short(number.shortValue());
				}
				if (class1 == Byte.TYPE || class1 == _ByteClass) {
					return new Byte(number.byteValue());
				}
				if (class1 == _BigDecimalClass) {
					if (class2 == _IntegerClass || class2 == _LongClass || class2 == _ShortClass
							|| class2 == _ByteClass) {
						return BigDecimal.valueOf(number.longValue());
					}
					if (number instanceof BigInteger) {
						return new BigDecimal((BigInteger) number);
					} else {
						return new BigDecimal(number.doubleValue());
					}
				}
				if (class1 == _BigIntegerClass) {
					if (number instanceof BigDecimal) {
						return ((BigDecimal) number).toBigInteger();
					} else {
						return BigInteger.valueOf(number.longValue());
					}
				}
			}
		}
		return number;
	}

	/**
	 * Compares two objects(Boolean or Number ) and returns 0 if equal , 1 if
	 * number is greater than number1 and -1 if number is less than number1
	 * 
	 * @param obj
	 *            first object(Boolean or Number) to be compared
	 * @param obj1
	 *            second object(Boolean or Number) with which first object to be
	 *            compared
	 * @return int
	 * 
	 */
	public static int compareNumbersOrBooleans(Object obj, Object obj1) {
		if (obj == obj1) {
			return 0;
		}
		if (obj == null) {
			return -1;
		}
		if (obj1 == null) {
			return 1;
		}
		Class<?> class1 = obj.getClass();
		Class<?> class2 = obj1.getClass();
		if (class1 == _BooleanClass && class2 == _BooleanClass) {
			boolean flag = ((Boolean) obj).booleanValue();
			boolean flag1 = ((Boolean) obj1).booleanValue();
			if (flag == flag1) {
				return 0;
			}
			return !flag1 ? 1 : -1;
		}
		if (class1 == _BooleanClass) {
			return compareNumbers(convertBooleanIntoNumberValue((Boolean) obj), (Number) obj1);
		}
		if (class2 == _BooleanClass) {
			return compareNumbers((Number) obj, convertBooleanIntoNumberValue((Boolean) obj1));
		} else {
			return compareNumbers((Number) obj, (Number) obj1);
		}
	}

	/**
	 * Converts {@link Boolean} to {@link Number}
	 * 
	 * @param boolean1
	 *            {@link Boolean} object to be convert into {@link Number}
	 * @return {@link Number}
	 */
	public static Number convertBooleanIntoNumberValue(Boolean boolean1) {
		if (boolean1 != null) {
			return boolean1.booleanValue() ? _shortOn : _shortOff;
		} else {
			return null;
		}
	}

	/**
	 * Converts {@link Number} value to {@link Boolean} {@link Object} if values
	 * equal to 0 then {@link Boolean#FALSE} else {@link Boolean#TRUE}
	 * 
	 * @param number
	 *            {@link Number} which to be convert to {@link Boolean}
	 * @return {@link Boolean}
	 */
	public static Boolean convertNumberIntoBooleanValue(Number number) {
		if (number != null) {
			Class<?> class1 = number.getClass();
			if (class1 == _IntegerClass || class1 == _ShortClass || class1 == _ByteClass) {
				return number.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
			}
			if (class1 == _LongClass) {
				return number.longValue() == 0L ? Boolean.FALSE : Boolean.TRUE;
			}
			if (class1 == _DoubleClass || class1 == _FloatClass) {
				return number.doubleValue() == 0.0D ? Boolean.FALSE : Boolean.TRUE;
			}
			if (class1 == _BigDecimalClass) {
				return ((BigDecimal) number).compareTo(_zeroBigDecimal) == 0 ? Boolean.FALSE : Boolean.TRUE;
			}
			if (class1 == _BigIntegerClass) {
				return ((BigInteger) number).compareTo(_zeroBigInteger) == 0 ? Boolean.FALSE : Boolean.TRUE;
			} else {
				return number.doubleValue() == 0.0D ? Boolean.FALSE : Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Compares two numbers and returns 0 if equal , 1 if number is greater than
	 * number1 and -1 if number is less than number1
	 * 
	 * @param number
	 *            first number to be compared
	 * @param number1
	 *            second number with which first number to be compared
	 * @return int
	 * 
	 */
	public static int compareNumbers(Number number, Number number1) {
		if (number == number1) {
			return 0;
		}
		if (number == null) {
			return -1;
		}
		if (number1 == null) {
			return 1;
		}
		Class<?> class1 = number.getClass();
		Class<?> class2 = number1.getClass();
		if (class1 != class2) {
			boolean flag = class1 == _ShortClass || class1 == _ByteClass;
			boolean flag1 = class2 == _ShortClass || class2 == _ByteClass;
			boolean flag2 = flag || class1 == _IntegerClass || class1 == _LongClass;
			boolean flag3 = flag1 || class2 == _IntegerClass || class2 == _LongClass;
			if (flag2 && flag3) {
				long l2 = number.longValue();
				long l3 = number1.longValue();
				if (l2 == l3) {
					return 0;
				} else {
					return l2 >= l3 ? 1 : -1;
				}
			}
			if ((flag || class1 == _FloatClass || class1 == _DoubleClass)
					&& (flag1 || class2 == _FloatClass || class2 == _DoubleClass)) {
				double d2 = number.doubleValue();
				double d3 = number1.doubleValue();
				if (d2 == d3) {
					return 0;
				} else {
					return d2 >= d3 ? 1 : -1;
				}
			}
			if ((flag2 || class1 == _BigIntegerClass) && (flag3 || class2 == _BigIntegerClass)) {
				if (class1 != _BigIntegerClass) {
					number = BigInteger.valueOf(number.longValue());
				}
				if (class2 != _BigIntegerClass) {
					number1 = BigInteger.valueOf(number1.longValue());
				}
			} else {
				if (!_BigDecimalClass.isAssignableFrom(class1)) {
					if (flag2) {
						number = BigDecimal.valueOf(number.longValue());
					} else if (_BigIntegerClass.isAssignableFrom(class1)) {
						number = new BigDecimal((BigInteger) number);
					} else {
						number = new BigDecimal(number.doubleValue());
					}
				}
				if (!_BigDecimalClass.isAssignableFrom(class2)) {
					if (flag3) {
						number1 = BigDecimal.valueOf(number1.longValue());
					} else if (_BigIntegerClass.isAssignableFrom(class2)) {
						number1 = new BigDecimal((BigInteger) number1);
					} else {
						number1 = new BigDecimal(number1.doubleValue());
					}
				}
			}
		}
		Class<?> class3 = number.getClass();
		if (class3 == _LongClass || class3 == _IntegerClass || class3 == _ShortClass || class3 == _ByteClass) {
			long l = number.longValue();
			long l1 = number1.longValue();
			if (l == l1) {
				return 0;
			} else {
				return l >= l1 ? 1 : -1;
			}
		}
		if (_BigDecimalClass.isAssignableFrom(class3)) {
			int i = ((BigDecimal) number).compareTo((BigDecimal) number1);
			if (i < 0) {
				return -1;
			}
			return i <= 0 ? 0 : 1;
		}
		if (_BigIntegerClass.isAssignableFrom(class3)) {
			int j = ((BigInteger) number).compareTo((BigInteger) number1);
			if (j < 0) {
				return -1;
			}
			return j <= 0 ? 0 : 1;
		}
		double d = number.doubleValue();
		double d1 = number1.doubleValue();
		if (d == d1) {
			return 0;
		} else {
			return d >= d1 ? 1 : -1;
		}
	}

	/**
	 * Returns an 'Integer' object if the value in v is small enough to fit,
	 * otherwise a 'Long' object. Note: this downsizes Long objects!
	 * 
	 * @param v
	 *            - some value, usually a Number
	 * @return null, an Integer or a Long object
	 */
	public static Number intOrLongValue(final Object v) {
		if (v == null) {
			return null;
		}
		if (v instanceof Integer) {
			return (Number) v;
		}
		if (v instanceof Number) {
			long lv = ((Number) v).longValue();
			return (lv >= Integer.MIN_VALUE && lv <= Integer.MAX_VALUE) ? new Integer((int) lv) : new Long(lv);
		}
		if (v instanceof String) {
			String s = ((String) v).trim();
			if (s.length() == 0) {
				return null;
			}
			long lv = longValue(s);
			return (lv >= Integer.MIN_VALUE && lv <= Integer.MAX_VALUE) ? new Integer((int) lv) : new Long(lv);
		}
		return intOrLongValue(v.toString());
	}

	/**
	 * Returns an 'Integer' object if the value in v is small enough to fit,
	 * otherwise a 'Long' object. Note: this downsizes Long objects! if string
	 * consisting of alphabets is provided then it returns 0.
	 * 
	 * @param _objs
	 *            - some values, usually a List&lt;Number&gt;
	 * @return null, or a List of Integer or Long objects
	 */
	public static List<Number> listOfIntsOrLongs(final Collection<?> _objs) {
		if (_objs == null) {
			return null;
		}
		ArrayList<Number> nums = new ArrayList<Number>(_objs.size());
		for (Object o : _objs) {
			Number n = intOrLongValue(o);
			if (n != o && n == null) {
				return null;
			}
			nums.add(n);
		}
		return nums;
	}

	

}
