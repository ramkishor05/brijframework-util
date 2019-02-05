package org.brijframework.util.text;

import java.text.ParseException;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.text.MaskFormatter;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;

public class StringUtil {

	/**
	 * check its have null or empty string
	 * 
	 * @param _text
	 * @return boolean
	 */
	public static boolean hasLength(String _text) {
		if (_text == null) {
			return false;
		}
		return !_text.isEmpty();
	}

	/**
	 * check its type of string or char values
	 * 
	 * @param _text
	 * @return boolean
	 */
	public static boolean hasText(Object _text) {
		Assertion.notNull(_text, AssertMessage.arg_null_message);
		return _text instanceof String || _text instanceof Character;
	}
	
	/**
	 * check its null or empty char values
	 * 
	 * @param _text
	 * @return boolean
	 */
	public static boolean isEmpty(final Object _text) {
		if (_text == null) {
			return true;
		}
		final String s = _text.toString().trim();
		return s.length() == 0;
	}

	/**
	 * check its null or empty char values
	 * 
	 * @param _text
	 * @return boolean
	 */
	public static boolean isEmpty(final String _text) {
		if (_text == null) {
			return true;
		}
		final String s = _text.trim();
		return s.length() == 0;
	}

	/**
	 * check its not null or empty char values
	 * 
	 * @param _text
	 * @return boolean
	 */
	public static boolean isNonEmpty(final String _v) {
		return !isEmpty(_v);
	}

	/**
	 * 
	 * <p>
	 * This method searches for a specified string in string array.
	 * </p>
	 * 
	 * @param sources
	 *            array in which string is to be searched
	 * @param _search
	 *            string that is to be searched.
	 * @return boolean true,if string is found in array fasle, if string is not
	 *         found in the specified array.
	 * @since Foundation 1.0
	 * 
	 */
	public static boolean contains(String _search, String... sources) {
		Assertion.notNull(sources, AssertMessage.arg_null_message);
		for (String string : sources) {
			if (string == _search) { // same reference
				return true;
			}
			if (_search != null && _search.equals(string)) { // same string
																// content
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * <p>
	 * This method matching for both object .
	 * </p>
	 * 
	 * @param obj1
	 *            any type of object
	 * @param obj2
	 *            any type of object
	 * @return boolean true,if string is found in array false, if string is not
	 *         found in the specified array.
	 * @since brijframework 1.0
	 * 
	 */
	public static boolean containsEquals(Object obj1, Object obj2) {
		String s1 = new String("" + obj1);
		String s2 = new String("" + obj2);
		return s2.contentEquals(s1);
	}

	/**
	 * 
	 * <p>
	 * This method matching ignoreCase for both object .
	 * </p>
	 * 
	 * @param obj1
	 *            any type of object
	 * @param obj2
	 *            any type of object
	 * @return boolean true,if string is found in array false, if string is not
	 *         found in the specified array.
	 * @since brijframework 1.0
	 * 
	 */
	public static boolean matchIgnoreCase(Object obj1, Object obj2) {
		String s1 = new String("" + obj1);
		String s2 = new String("" + obj2);
		return s2.equalsIgnoreCase(s1);
	}

	/**
	 * 
	 * <p>
	 * This method searches for a specified string in string array ignoring the
	 * case.
	 * </p>
	 * 
	 * @param stringArray
	 *            array in which string is to be searched
	 * @param searchString
	 *            string that is to be searched.
	 * @return boolean true,if string is found in array fasle, if string is not
	 *         found in the specified array.
	 * @since brijframework 1.0
	 * 
	 */
	public static boolean containsIgnoreCase(String[] stringArray, String searchString) {
		if (stringArray == null) {
			return false;
		}
		for (String string : stringArray) {
			if (searchString != null && searchString.equalsIgnoreCase(string)) { // same
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * <p>
	 * This method searches for a specified string in string array ignoring the
	 * case.
	 * </p>
	 * 
	 * @param stringArray
	 *            array in which string is to be searched
	 * @param searchString
	 *            string that is to be searched.
	 * @return boolean true,if string is found in array fasle, if string is not
	 *         found in the specified array.
	 * @since brijframework 1.0
	 * 
	 */
	public static boolean containsIgnoreCase(Object stringArray, Object searchString) {
		if (stringArray == null) {
			return false;
		}
		String[] dataArray = new String("" + stringArray).split(" ");
		for (String string : dataArray) {
			if (string.toLowerCase().contains(new String("" + searchString).toLowerCase())) { // same
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * <p>
	 * This method searches for a specified string in string array ignoring the
	 * case.
	 * </p>
	 * 
	 * @param stringArray
	 *            array in which string is to be searched
	 * @param searchString
	 *            string that is to be searched.
	 * @return boolean true,if string is found in array fasle, if string is not
	 *         found in the specified array.
	 * @since brijframework 1.0
	 * 
	 */
	public static boolean containsIgnoreCase(Object[] stringArray, Object searchString) {
		if (stringArray == null) {
			return false;
		}
		int i = 0;
		String[] textArray = new String[stringArray.length];
		for (Object obj : stringArray) {
			textArray[i++] = new String("" + obj);
		}
		for (String string : textArray) {
			if (string.equalsIgnoreCase(new String("" + searchString))) { // same
																			// string
																			// content
																			// ignoring
																			// uppercase
																			// or
																			// lowercase
				return true;
			}
		}
		return false;
	}

	/**
	 * clean all and get string only number or char
	 * 
	 * @param _source
	 * @return string
	 */
	public static String cleanText(String _source) {
		Assertion.notNull(_source, AssertMessage.arg_null_message);
		String cleanString = "";
		for (int i = 0; i < _source.length(); i++) {
			if (Character.isLetter(_source.charAt(i)) || Character.isDigit(_source.charAt(i))) {
				cleanString = cleanString + _source.charAt(i);
			}
		}
		return cleanString;
	}

	/**
	 * clean all and replace ignore value to replace value and get string only
	 * number or char
	 * 
	 * @param _source
	 * @param _replaceValue
	 * @return string
	 */
	public static String cleanAndReplaceText(String _source, String _replaceValue) {
		Assertion.notNull(_source, AssertMessage.arg_null_message);
		String cleanString = "";
		_source = _source.trim();
		for (int i = 0; i < _source.length(); i++) {
			if (Character.isLetter(_source.charAt(i)) || Character.isDigit(_source.charAt(i))) {
				cleanString = cleanString + _source.charAt(i);
			} else {
				cleanString = cleanString + _replaceValue;
			}
		}
		return cleanString;
	}

	/**
	 * Ignore value and get clean text
	 * 
	 * @param _source
	 * @param _harToDump
	 * @return string
	 */
	public static String ignoreFromText(String _source, char _harToDump) {
		Assertion.notNull(_source, AssertMessage.arg_null_message);
		String result = "";
		_source = _source.trim();
		for (int i = 0; i < _source.length(); i++) {
			if (_source.charAt(i) != _harToDump) {
				result = result + _source.charAt(i);
			}
		}
		return result;
	}

	/**
	 * Will get the token at a specified index in the string according to the
	 * delimiter we want to tokenize the string with.
	 *
	 * @param _source
	 *            String
	 * @param index
	 *            int
	 * @param delimiter
	 *            String
	 * @return String
	 */
	public static String getTokenAtIndex(String _source, int index, String delimiter) {
		Assertion.notNull(_source, AssertMessage.arg_null_message);
		String resultString = null;
		int tokenCount = 0;
		StringTokenizer TK = new StringTokenizer(_source, delimiter);
		tokenCount = TK.countTokens();
		if (index <= tokenCount) {
			for (int i = 1; i <= index; i++) {
				String str = TK.nextToken();
				if (i == index) {
					resultString = str;
				}
			}
		}
		return resultString;
	}

	/**
	 * reverse text of sources text
	 * 
	 * @param _source
	 * @return destination
	 * @tested true
	 */
	public static String reverseText(String _source) {
		Assertion.notNull(_source, AssertMessage.arg_null_message);
		char[] str_arr = new char[_source.length()];
		for (int i = _source.length() - 1, j = 0; i >= 0; i--) {
			str_arr[j++] = _source.charAt(i);
		}
		String s = new String(str_arr);
		return s;
	}

	/**
	 * this is for replace text with given text from text
	 * 
	 * @param _sources
	 * @param replaceText
	 * @param withText
	 * @return replace text
	 * @tested :true
	 */
	public static String replaceText(String _sources, String replaceText, String withText) {
		Assertion.notNull(_sources, AssertMessage.arg_null_message);
		int index = _sources.indexOf(replaceText);
		if (index < 0) {
			return _sources;
		} else {
			return _sources.substring(0, index) + withText + replaceText(
					_sources.substring(index + replaceText.length(), _sources.length()), replaceText, withText);
		}
	}

	/**
	 * capital first letter in text
	 * 
	 * @param text
	 * @return first capital text
	 * @tested true
	 */
	public static String capitalFirst(String text) {
		Assertion.notNull(text, AssertMessage.arg_object_null_message);
		String s1 = text.substring(0, 1).toUpperCase();
		String textCapitalized = s1 + text.substring(1);
		return textCapitalized;
	}
	
	/**
	 * lower first letter in text
	 * 
	 * @param text
	 * @return first capital text
	 * @tested true
	 */
	public static String lowerFirst(String text) {
		Assertion.notNull(text, AssertMessage.arg_object_null_message);
		String s1 = text.substring(0, 1).toLowerCase();
		String textCapitalized = s1 + text.substring(1);
		return textCapitalized;
	}

	/**
	 * Capital first letter of word in text
	 * 
	 * @param _textline
	 * 
	 * @return capital test
	 * @tested true
	 */
	public static String capitalFirstLetterText(String _textline) {
		Assertion.notNull(_textline, AssertMessage.arg_object_null_message);
		String returnLine = "";
		for (String str : _textline.split(" ")) {
			String s1 = str.substring(0, 1).toUpperCase();
			String strCapitalized = s1 + str.substring(1);
			returnLine += strCapitalized + " ";
		}
		return returnLine;
	}

	/**
	 * Justify text given text
	 * 
	 * @param _source
	 * @param length
	 * @param isLeftJustify
	 * @param filled
	 * @return justify text
	 * @tested true
	 */
	public static String justifyText(String _source, int length, boolean isLeftJustify, char filled) {
		Assertion.notNull(_source, AssertMessage.arg_object_null_message);
		char[] returnArray = new char[length];
		for (int i = 0; i < length; i++) {
			returnArray[i] = filled;
		}
		if (_source == null) {
			return String.valueOf(returnArray);
		}

		if (_source.length() <= length) {
			if (isLeftJustify) {
				for (int i = 0; i < _source.length(); i++) {
					returnArray[i] = _source.charAt(i);
				}
			} else {
				for (int i = 0; i < _source.length(); i++) {
					returnArray[length - 1 - i] = _source.charAt(_source.length() - 1 - i);
				}
			}
		} else {
			if (isLeftJustify) {
				for (int i = 0; i < length; i++) {
					returnArray[i] = _source.charAt(i);
				}
			} else {
				for (int i = 0; i < length; i++) {
					returnArray[length - 1 - i] = _source.charAt(_source.length() - 1 - i);
				}
			}
		}
		return String.valueOf(returnArray);

	}

	public static String[] split(String baseString, String splitString) {
		StringTokenizer tokenizer = new StringTokenizer(baseString, splitString);
		String[] tokens = new String[tokenizer.countTokens()];
		for (int i = 0; tokenizer.hasMoreTokens(); i++) {
			tokens[i] = tokenizer.nextToken();
		}
		return tokens;
	}

	public static int charCountInText(String _sources, char ch) {
		int count = 0;
		char[] charArray = _sources.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == ch) {
				count++;
			}
		}
		return count;
	}

	public static String arrayToTildeText(Object[] array) {
		String text = "";
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				text += "" + array[i];
				if (array.length - 1 != i) {
					text += "~";
				}
			}
		}
		return text;
	}

	public static String getCharNTimes(char c, int n) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < n; i++) {
			strBuf.append(c);
		}
		return strBuf.toString();
	}

	public static String arrayToCommaText(Object[] array) {
		String text = "";
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				text += "" + array[i];
				if (array.length - 1 != i) {
					text += ",";
				}
			}
		}
		return text;
	}

	public static String getDelimString(Iterable<?> iterable, String sepStr) {
		StringBuffer strBuf = new StringBuffer();
		Iterator<?> itr = iterable.iterator();
		while (itr.hasNext()) {
			strBuf.append(itr.next());
			if (itr.hasNext()) {
				strBuf.append(sepStr);
			}
		}
		return strBuf.toString();
	}

	public static String toCamelCase(final String str) {
		if (str == null || str.trim().equals("")) {
			return str;
		}
		String strNew = str.trim().toLowerCase();
		return strNew.length() >= 1 ? Character.toUpperCase(strNew.charAt(0)) + strNew.substring(1) : str;
	}

	public static String stringValue(Object obj) {
		return String.valueOf(obj);
	}

	/**
	 * Returns Wrapper Class corresponding to string
	 * 
	 * @param type
	 *            {@link String} name of primitive
	 * @return {@link Class} wrapper class for string
	 */

	/**
	 * Returns Class corresponding to string primitive type
	 * 
	 * @param type
	 *            {@link String} name of primitive
	 * @return {@link Class} class for string
	 */

	/**
	 * Returns Class corresponding to array
	 * 
	 * @param type
	 *            {@link String} name of array like
	 *            <code>String [], double [], int []</code>
	 * @return {@link Class} class for array like
	 *         <code>String [].class, double [].class, int [].class</code>
	 */

	/**
	 * converts string format put UnderScore '_' before first Cap char and
	 * converts to UPPER case eg. if value of str is 'BrijSoftware' then it will
	 * converted to 'BRIJ_SOFTWARE'
	 * 
	 * @param str
	 *            {@link String} need to modify
	 * @return modified {@link String}
	 */
	public static String addUnderScoreBeforeUpperCaseLetter(String str) {
		if (str == null || str.trim().length() == 0) {
			return str;
		}
		StringBuilder result = new StringBuilder();
		result.append(str.charAt(0));
		for (int i = 1; i < str.length(); i++) {
			if (Character.isUpperCase(str.charAt(i)) && i > 1 && Character.isLowerCase(str.charAt(i - 1))) {
				result.append("_");
			}
			result.append(str.charAt(i));
		}
		return result.toString().toUpperCase();
	}

	/**
	 * 
	 * <p>
	 * method is used to get the title case format for the string passed. for
	 * e.g. variableName the title case String would be Variable Name
	 * </p>
	 * <h4>Example</h4>
	 * <p>
	 * FNUtil.getTitleCaseString("variableName"); // result would be Variable
	 * Name
	 * </p>
	 * 
	 * @param string
	 *            containing the text to be converted into title case format
	 * @return String object having passed string in title case
	 * @see {@link FNUtil#addUnderScoreBeforeUpperCaseLetter(String)}
	 *      addUnderScoreBeforeCaseLetter
	 * @since Foundation 1.0
	 * 
	 */
	public static String friendlyNameForJavaVar(String string) {
		if (string.charAt(0) == '_') {
			return string;
		}
		String tempString = addUnderScoreBeforeUpperCaseLetter(string);
		StringBuilder builder = new StringBuilder();
		for (int counter = 0; counter < tempString.length(); counter++) {
			if (counter == 0) {
				builder.append(tempString.charAt(counter));
			} else if (tempString.charAt(counter) == '_') {
				builder.append((char) 32);
				builder.append(tempString.charAt(counter + 1));
				counter++;
			} else {
				builder.append(Character.toLowerCase(tempString.charAt(counter)));
			}
		}
		return builder.toString();
	}

	public static String maskString(String rawStr, char c, String format) {
		StringBuffer buffer = new StringBuffer();
		while (rawStr.length() + buffer.length() < 10) {
			buffer.append(c);
		}
		buffer.append(rawStr);
		try {
			MaskFormatter maskFormatter = new MaskFormatter(format);
			maskFormatter.setValueContainsLiteralCharacters(false);
			return maskFormatter.valueToString(buffer);
		} catch (ParseException e) {
		}
		return buffer.toString();
	}
	
	public static int charIndex(String sources, int start, char ch) {
		char[] chars = sources.toCharArray();
		for (int index = start; index < chars.length; index++) {
			if (chars[index] == ch) {
				return index;
			}
		}
		return -1;
	}

	public static String getOfter(StringBuffer sources, String match) {
		return getOfter(sources.toString(), match);
	}

	public static String getOfter(String original, String match) {
		String str[]=original.split(match);
		return str.length>1?str[1]:"";
	}

	public static String getBefore(StringBuffer sources, String match) {
		return getOfter(sources.toString(), match);
	}

	public static String getBefore(String original, String match) {
		String str[]=original.split(match);
		return str.length>0?str[0]:"";
	}
	
	public static String between(String original,String start,String end){
		return getBefore(getOfter(original, start), end);
	}

	public static void main(String[] args) {
		System.out.print(between("/media/ramkishor/DATA3/doveloper/Workbook/abs-brijframework-web-core/target/classes/META-INF/web/context/WebContext.xml", "META-INF/","WebContext.xml"));
	}

	public static String[] parseTilde(String objectKeys) {
		return objectKeys.split("~");
	}

}
