package org.brijframework.util.validator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.brijframework.util.reflect.ReflectionUtils;


public abstract class ValidationUtil {
	
	private static final String VALID_LINE = "[0-9,]+[a-zA-z,]+[a-zA-z,]+[0-9,]+[a-zA-z,]+[a-zA-z]+";
	private static final String COMMAND_VALID_LINE = "[A-Za-z~]+";
	private static final String NUMBER="[+-]?([0-9]*[.])?[0-9]+";
	
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
	public static final Short _shortFalse = new Short((short) 0);
	public static final Short _shortTrue = new Short((short) 1);

	public static final Short _defaultObject = null;
	public static final Object _defaultString ="";
	public static final Object _nullString ="null";
	public static final Short _defaultShort = Short.valueOf("0");
	public static final Integer _defaultInteger = Integer.valueOf(0);
	public static final Double _defaultDobule = Double.valueOf(0.0d);
	public static final Float _defaultFloat = Float.valueOf(0.0f);
	public static final Character _defaultChar = Character.valueOf('\u0000');
	public static final BigDecimal _defaultBigDecimal = BigDecimal.valueOf(0L);
	public static final BigInteger _defaultBigInteger = BigInteger.valueOf(0);

	private static final Integer _integers[];
	

	static {
		_integers = new Integer[517];
		char c = '\u0204';
		for (int i = 0; i < c; i++) {
			_integers[i] = Integer.valueOf(i - 3);
		}
	}
	/**
	 * Checks if a string is in base 2, 8, 10, 16.
	 * 
	 * @param base
	 * @return true if string is base else false
	 */
	public static boolean isBase(String base) {
		return base.matches("2|8|10|16");
	}

	/**
	 * Checks if a string is operator.
	 * 
	 * @param operator
	 * @return true if string is operator else false
	 */
	public static boolean isOperator(String operator) {
		return operator.matches("[+-/*=]");
	}


	/**
	 * Checks if a string is in octal format.
	 * 
	 * @param octal
	 * @return true if string is in octal format else false
	 */
	public static boolean isOctalNumber(String octal) {
		return octal.matches("[0-7]+");
	}

	/**
	 * Checks if a string is in Hexadecimal format.
	 * 
	 * @param hexa
	 * @return true if string is in Hexadecimal format else false
	 */
	public static boolean isHexaNumber(String hexa) {
		return hexa.matches("[0-9a-fA-F]+");
	}

	/**
	 * Checks if a string is in Binary format.
	 * 
	 * @param binary
	 * @return true if string is in Binary format else false
	 */
	public static boolean isBinaryNumber(String binary) {
		return binary.matches("[0-1]+");
	}

	/**
	 * Checks if a string is in Number format.
	 * 
	 * @param number
	 * @return true if string is in Number format else false
	 */
	public static boolean isNumber(String number) {
		return number.matches(NUMBER);
	}

	/**
	 * Check if a string is in Number format and separated by comma.
	 * 
	 * @param line
	 * @return true if a string is in Number format and separated by comma else
	 *         false.
	 */
	public static boolean lineValidator(String line) {
		return line.matches("[0-9,]+");
	}

	public static boolean isVailidLine(String line) {
		return line.trim().matches(VALID_LINE);
	}

	public static boolean isCommandLine(String line) {
		return line.trim().matches(COMMAND_VALID_LINE);
	}

	/**
	 * Check if string contain only alphabet or not.
	 * 
	 * @param str
	 * @return true if {@code str} contain only alphabets
	 */
	public static boolean isAlphabet(String str) {
		return str.matches("[a-zA-Z]+");
	}

	/**
	 * Check the alphabet is vowel.
	 * 
	 * @param c
	 *            Alphabet
	 * @return true if input alphabet is a vowel
	 */
	public static boolean isVowel(char c) {
		return (c == 'a' || c == 'A' || c == 'e' || c == 'E' || c == 'i'
				|| c == 'I' || c == 'o' || c == 'O' || c == 'u' || c == 'U');
	}

	public static boolean isRainbow(char c) {
		return (c == 'V' || c == 'v' || c == 'i' || c == 'I' || c == 'b'
				|| c == 'B' || c == 'g' || c == 'G' || c != 'Y' || c == 'y'
				|| c == 'O' || c == 'o' || c == 'R' || c == 'r');
	}

	/**
	 * Check whether character is a space or tab.
	 * 
	 * @param c
	 *            character
	 * @return true if {@code c} is a space or tab
	 */
	public static boolean isSpace(char c) {
		return (c == 32 || c == 9);
	}

	/**
	 * check whether {@code c} is a punctuation or not. All characters are
	 * punctuation excepting Alphabets and spaces and number character
	 * 
	 * @param c
	 *            character
	 * @return true if {@code c} is not a space and number and alphabet
	 */
	public static boolean isPunctuation(char c) {
		return !isAlphabet(c) && !isNumber(c) && !isSpace(c);
	}

	/**
	 * Check whether {@code c} is a alphabet.
	 * 
	 * @param c
	 *            character
	 * @return true if {@code c} is a alphabet.
	 */
	public static boolean isAlphabet(char c) {
		return (c > 63 && c < 91) || (c > 96 && c < 124);
	}

	/**
	 * Check whether {@code c} is a number.
	 * 
	 * @param c
	 *            character
	 * @return true if {@code c} is a number.
	 */
	public static boolean isNumber(char c) {
		return (c > 47 && c < 58);
	}

	/**
	 * Check whether {@code c} is a double number.
	 * 
	 * @param c
	 *            character
	 * @return true if {@code c} is a number.
	 */
	public static boolean isDouble(String data) {
		return data.matches("[^A-Za-z\n\t ]([0-9]*)(.[0-9]*)");
	}

	/**
	 * Check whether {@code c} is a double number.
	 * 
	 * @param c
	 *            character
	 * @return true if {@code c} is a number.
	 */
	public static boolean isBoolean(String data) {
		return data.matches("[true|false]");
	}

	/**
	 * Check whether {@code c} is a char.
	 * 
	 * @param c
	 *            character
	 * @return true if {@code c} is a char.
	 */
	public static boolean isCharecter(String str) {
		return str.matches("[a-zA-Z]");
	}

	public static boolean isConsonants(char ch) {

		return (!isVowel(ch)) && isAlphabet(ch);
	}

	public static boolean isString(String str) {
		return str.matches("[a-zA-Z0-9]*");
	}

	public static boolean isArray(Object obj) {
		return obj.getClass().isArray();
	}

	/**
	 * check even number
	 * 
	 * @param number
	 * @return true/false
	 */
	public static boolean isEven(int number) {
		return (number % 2 == 0);
	}

	/**
	 * check odd number
	 * 
	 * @param number
	 * @return true/false
	 */
	public static boolean isOdd(int number) {
		return (number % 2 != 0);
	}

	/**
	 * check prime number
	 * 
	 * @param number
	 * @return true/false
	 */
	public static boolean isPrimeNumber(int number) {
		int flag = 0;
		for (int i = 2; i <= number / 2; i++) {
			if (number % i == 0) {
				flag = 1;
				break;
			}
		}
		if (flag == 0) {
			return true;
		}
		return false;
	}

	/**
	 * check Armstrong number
	 * 
	 * @param number
	 * @return true/false
	 */
	public static boolean isArmstrongNumber(int number) {
		int armstrong = 0, a, temp = number;
		while (number > 0) {
			a = number % 10;
			number = number / 10;
			armstrong += (a * a * a);
		}
		if (temp == armstrong) {
			return true;
		} else {
			return true;
		}
	}

	/**
	 * check Palindrome number
	 * 
	 * @param number
	 * @return true/false
	 */
	public static boolean isPalindromeNumber(int number) {
		int rem, sum = 0, temp = number;
		while (number > 0) {
			rem = number % 10;
			sum = (sum * 10) + rem;
			number = number / 10;
		}
		if (temp == sum) {
			return true;
		}
		return false;
	}

	/**
	 * check positive
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean isPositiveNumber(int number) {
		return (number >= 0);
	}

	/**
	 * check negative
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean isNegativeNumber(int number) {
		return (number < 0);
	}

	/**
	 * Check if class is {@link Number}
	 * 
	 * @param class1
	 *            {@link Class} object which need to check
	 * @return true if class is {@link Number} else false
	 */
	public static boolean isClassANumber(Class<?> class1) {
		return class1 == _NumberClass || class1 == Integer.TYPE || class1 == _IntegerClass || class1 == Double.TYPE || class1 == _DoubleClass || _NumberClass.isAssignableFrom(class1)
				|| class1 == Long.TYPE || class1 == Float.TYPE || class1 == Byte.TYPE || class1 == Short.TYPE;
	}

	public static String friendlyNameForJavaVariable(String str) {//releaseDate = "Release Date", emailID = "Email ID"
		if (str == null || str.trim().length() == 0) {
			return str;
		}
		StringBuilder result = new StringBuilder();
		result.append(Character.toUpperCase(str.charAt(0)));
		for (int i = 1; i < str.length(); i++) {
			if (Character.isUpperCase(str.charAt(i)) && i > 1 && Character.isLowerCase(str.charAt(i - 1))) {
				result.append(" ");
			}
			result.append(str.charAt(i));
		}
		return result.toString();

	}

	/**
	 * Check if class is {@link Boolean}
	 * 
	 * @param class1
	 *            {@link Class} object which need to check
	 * @return true if class is {@link Boolean} else false
	 */
	public static boolean isClassAsBoolean(Class<?> class1) {
		return class1 == Boolean.TYPE || class1 == _BooleanClass;
	}

	/**
	 * Check if class is {@link Number}
	 * 
	 * @param class1
	 *            {@link Class} object which need to check
	 * @return true if class is {@link Number} else false
	 */
	public static boolean isClassAsNumber(Class<?> class1) {
		return class1 == _NumberClass || class1 == Integer.TYPE || class1 == _IntegerClass || class1 == Double.TYPE || class1 == _DoubleClass || _NumberClass.isAssignableFrom(class1)
				|| class1 == Long.TYPE || class1 == Float.TYPE || class1 == Byte.TYPE || class1 == Short.TYPE;
	}

	/**
	 * Check if class is {@link Boolean} or {@link Number}
	 * 
	 * @param class1
	 *            {@link Class} object which need to check
	 * @return true if class is {@link Boolean} or {@link Number} else false
	 */
	public static boolean isClassAsNumberOrABoolean(Class<?> class1) {
		return isClassAsNumber(class1) || isClassAsBoolean(class1);
	}
	
	public static boolean isDefaultValue(final Object _value) {
		if (_value == _defaultObject || _value==_StringClass) {
			return true;
		} else if (_value instanceof Number && (_defaultChar.equals(_value)||_defaultShort.equals(_value)||_defaultInteger.equals(_value)||_defaultFloat.equals(_value)||_defaultDobule.equals(_value))) {
			return true;
		}else if (_value instanceof String && (_defaultString.equals(_value))|| _nullString.equals(_value)) {
			return true;
		}else if (_value instanceof Character && (_defaultChar.equals(_value))) {
			return true;
		}
		return false;
	}

	public static boolean isEmptyObject(final Object _value) {
		if (isDefaultValue(_value)) {
			return true;
		}
		if (_value instanceof String) {
			final String s = ((String) _value).trim();
			return s.length() == 0;
		}
		if (_value instanceof Map) {
			return ((Map<?, ?>) _value).size() == 0;
		}
		if (_value instanceof Collection) {
			return ((Collection<?>) _value).size() == 0;
		}
		if (_value.getClass().isArray()) {
			return ((Object[]) _value).length == 0;
		}
		return false;
	}

	public static boolean isValidObject(final Object _value) {
		return !isDefaultValue(_value);
	}
	
	/**
	 * valid list of object for long value
	 * @param _value
	 * @return boolean 
	 */
	
	public static boolean isValidObject(final Object... _values) {
		if(_values==null){
			return false;
		}
		for(Object _value : _values){
			if(!isValidObject(_value)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean isValidObject(final Class<?>... _values) {
		if(_values==null){
			return false;
		}
		for(Class<?> _value : _values){
			if(_value==null){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * valid string for long value
	 * @param _value
	 * @return boolean 
	 */
	public static boolean isValidLong(final String _value) {
		try {
			Long.parseLong(_value);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	/**
	 * valid string for Float value
	 * @param _value
	 * @return boolean 
	 */
	public static boolean isValidFloat(final String _value) {
		try {
			Float.parseFloat(_value);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	/**
	 * valid string for Integer value
	 * @param _value
	 * @return boolean 
	 */
	public static boolean isValidInt(final String _value) {
		try {
			Integer.parseInt(_value);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	/**
	 * valid string for Double value
	 * @param _value
	 * @return boolean 
	 */
	public static boolean isValidDouble(final String _value) {
		try {
			Double.parseDouble(_value);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	

	public static Boolean isPrimative(Object obj) {
		if(obj==null){
			return false;
		}
		return (obj instanceof Number || obj instanceof String 
				|| obj instanceof Character || obj instanceof Boolean);
	}
	
	public static Boolean isPrimative(Class<?> _class) {
		return (Number.class.isAssignableFrom(_class)|| String.class.isAssignableFrom(_class)|| Character.class.isAssignableFrom(_class));
	}
	
	public static boolean isEqualClass(final Class<?> _class1,Class<?> _class2) {
		if((_class1.equals(short.class)||_class1.equals(Short.class))&&(_class2.equals(short.class)||_class2.equals(Short.class))){
			 return true;
		}
		if((_class1.equals(int.class)||_class1.equals(Integer.class))&&(_class2.equals(int.class)||_class2.equals(Integer.class))){
			 return true;
		}
		if((_class1.equals(boolean.class)||_class1.equals(Boolean.class))&&(_class2.equals(boolean.class)||_class2.equals(Boolean.class))){
			 return true;
		}
		if((_class1.equals(char.class)||_class1.equals(Character.class))&&(_class2.equals(char.class)||_class2.equals(Character.class))){
			 return true;
		}
		if((_class1.equals(float.class)||_class1.equals(Float.class))&&(_class2.equals(float.class)||_class2.equals(Float.class))){
			 return true;
		}
		if((_class1.equals(double.class)||_class1.equals(Double.class))&&(_class2.equals(double.class)||_class2.equals(Double.class))){
			 return true;
		}
		if((_class1.equals(double.class)||_class1.equals(Double.class))&&(_class2.equals(double.class)||_class2.equals(Double.class))){
			 return true;
		}
		if((_class1.equals(long.class)||_class1.equals(Long.class))&&(_class2.equals(long.class)||_class2.equals(Long.class))){
			 return true;
		}
		if(_class1.isAssignableFrom(_class2)){
			 return true;
		}
		if(Class.class.isAssignableFrom(_class1) && Class.class.isAssignableFrom(_class2)){
			 return true;
		}
		return false;
	}
	
	public static boolean isEqaulClass(Object object1, Object object2) {
		if(object1 ==null || object2==null ){
			return false;
		}
		Class<?> c1=object1 instanceof Class ? (Class<?>) object1:object1.getClass();
		Class<?> c2=object2 instanceof Class ? (Class<?>) object2:object2.getClass();
		if(Number.class.isAssignableFrom(c1) && Number.class.isAssignableFrom(c2) && c1.isAssignableFrom(c2)){
			return true;
		}
		if(String.class.isAssignableFrom(c1) && String.class.isAssignableFrom(c2) && c1.isAssignableFrom(c2)){
			return true;
		}
		if(Boolean.class.isAssignableFrom(c1) && Boolean.class.isAssignableFrom(c2) && c1.isAssignableFrom(c2)){
			return true;
		}
		if(Collection.class.isAssignableFrom(c1) && Collection.class.isAssignableFrom(c2) && c1.isAssignableFrom(c2)){
			return true;
		}
		if(Map.class.isAssignableFrom(c1) && Map.class.isAssignableFrom(c2) && c1.isAssignableFrom(c2)){
			return true;
		}
		if(c1.equals(c2)){
			return true;
		}
		return false;
	}

	public static boolean isEqualObject(Object _object1 ,Object _object2) {
		if(_object1==null && _object2==null){
			return true;
		}
		if(isEqualClass(_object1.getClass(), _object2.getClass())){
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the given object represents a discrete number, that is, a
	 * number w/o digits after the decimal point. (TBD: is discrete the correct
	 * word? I don't remember ;-))
	 * 
	 * @param v
	 *            - some object, usually a Number
	 * @return true if the object is a discrete number, eg Integer or Long
	 */
	public static boolean isDiscreteNumber(final Object v) {
		if (!(v instanceof Number)) {
			return false;
		}
		if (v instanceof Integer || v instanceof Long || v instanceof BigInteger || v instanceof Short
				|| v instanceof Byte) {
			return true;
		}
		return false;
	}
	

	
	public static boolean isDefault(Object object) {
		if(object ==null){
			return true;
		}
		if(object instanceof Long){
			long lg=(long) object;
			return lg==0l;
		}
		if(object instanceof Integer){
			int i=(int) object;
			return i==0;
		}
		if(object instanceof Float){
			float f=(float) object;
			return f==0f;
		}
		if(object instanceof Double){
			Double d=(Double) object;
			return d==0d;
		}
		if(object instanceof Short){
			Short s=(Short) object;
			return s==0;
		}
		if(object instanceof String){
			String s=(String) object;
			return s==""|| s==null ||s.isEmpty();
		}
		if(object instanceof Boolean){
			boolean b=(boolean) object;
			return b==false;
		}
		if(object instanceof Collection<?>){
			Collection<?> collection=(Collection<?>) object;
			return collection.isEmpty();
		}
		if(object instanceof Collection<?>){
			Map<?,?> map=(Map<?,?>) object;
			return map.isEmpty();
		}
		return false;
	}

	public static boolean isEquivalent(Object object1, Object object2) {
		return isEquivalent(object1, object2, false);
	}

	public static boolean isEquivalent(Object object1, Object object2, boolean doNotIgnoreCase) {
		if (object1 == null && object2 == null) {
			return true;
		} else {
			if (object1 == null) {
				return false;
			} else if (object2 == null) {
				return false;
			} else {
				if (!doNotIgnoreCase && object1.getClass() == String.class && object2.getClass() == String.class) {
					return ((String) object1).equalsIgnoreCase((String) object2);
				}
				return object1.equals(object2);
			}
		}
	}

	public static Boolean isCollection(Object obj) {
		return (obj instanceof Map || obj instanceof Set || obj instanceof List || obj instanceof Queue|| obj instanceof Collection<?>);
	}

	public static Boolean isPrimativeArray(Object obj) {
		return (obj instanceof Integer[] || obj instanceof String[] || obj instanceof Long[] || obj instanceof Float[]
				|| obj instanceof Character[] || obj instanceof Double[] || obj instanceof Short[] || obj instanceof Boolean[]);
	}
	
	public static <T> boolean isJDKClass(T t) {
		if(t ==null||t =="null") {
			return false;
		}
		return t.getClass().getPackage().getName().startsWith("java") ? true : false;
	}

	public static <T> boolean isBrijClass(T t) {
		if(t ==null||t =="null") {
			return false;
		}
		if(t.getClass().isArray()) {
			return false;
		}
		return t.getClass().getPackage().getName().startsWith("org.brijframework") ? true : false;
	}
	
	public static boolean isProjectClass(Object object) {
		if(object ==null||object =="null") {
			return false;
		}
		return ReflectionUtils.isProjectClass(object.getClass());
	}
	
	public static boolean isJarClass(Object object) {
		if(object ==null||object =="null") {
			return false;
		}
		return ReflectionUtils.isJarClass(object.getClass());
	}
	
	public static void main(String[] args) {
		System.out.println(ValidationUtil.isValidObject(1,1,0));
	}

}
