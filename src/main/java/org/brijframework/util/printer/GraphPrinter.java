package org.brijframework.util.printer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.support.Access;
import org.brijframework.util.validator.ValidationUtil;

public class GraphPrinter {
	
	private StringBuffer stringToCopy = new StringBuffer("");

	static GraphPrinter printer = null;
	Object object;
	/***
	 * initialize object and get instance of this
	 * 
	 * @param object
	 */
	public static GraphPrinter getPrinter(Object object) {
		printer = (GraphPrinter) InstanceUtil.getSingletonInstance(GraphPrinter.class);
		printer.object = object;
		return printer;
	}
	

	/**
	 * Prints an Object to screen
	 * 
	 * @param a
	 * @return
	 */
	public String printToScreen() {
		printObject(object, 0);
		System.out.println(stringToCopy.toString());
		return stringToCopy.toString();
	}

	/***
	 * Prints an Object to a file whose filename is passed to <BR>
	 * its containing object while instantiating it.
	 * 
	 * @return
	 */
	public String printObject() {
		printObject(object, 0);
		return stringToCopy.toString();
	}

	/***
	 * Prints an Object to a file whose filename is passed to<BR>
	 * its containing object while instantiating it.
	 * 
	 * @param a
	 * @return
	 */
	public String printObject(Object a) {
		printObject(a, 0);
		return stringToCopy.toString();
	}

	/***
	 * The following three printObject methods keep looping among themselves<BR>
	 * until the entire Object or Map is printed
	 * 
	 * @param t
	 * @param tab
	 */
	@SuppressWarnings("rawtypes")
	private void printObject(Object object, int tab) {
		if(object==null){
			return;
		}
		Object t=object;
		if(ValidationUtil.isBrijClass(object)) {
			t=PropertyAccessorUtil.getProperties(object,Access.PRIVATE_NO_STATIC_FINAL);
		}
		if (t instanceof Map && tab == 0) {
			tab = 1;
		}
		if (t instanceof Map) {
			debugln("{");
			printObject((Map) t, ++tab);
			printTab(tab - 1);
			debug("}");
		} else if (t instanceof Collection) {
			debugln("[");
			printObject((Collection) t, ++tab);
			debugln("");
			printTab(tab - 1);
			debug("]");
		}else if (t instanceof Character) {
			debug("\'" + t + "\'");
		} else if (t instanceof String) {
			debug("\"" + t + "\"");
		} else if ( t instanceof Date || t instanceof java.sql.Date
				|| t instanceof Time || t instanceof Timestamp) {
			debug(formatDate(t));
		} else if (t instanceof Double|| t instanceof Float) {
			String value = t.toString();
			debug("" + value + "");
		}else if (t instanceof Long) {
			Long value = (Long) t;
			debug("" + value + "");
		} else if (t instanceof Integer) {
			Integer value = (Integer) t;
			debug("" + value + "");
		} else if (t instanceof Boolean) {
			Boolean value = (Boolean) t;
			debug("" + (value.booleanValue() ? true : false) + "");
		} else {
			debug("\"" + t.toString() + "\"");
		}
	}

	public String formatDate(Object date) {
		return date.toString();
	}

	/**
	 * Prints a Collection
	 * 
	 * @param t
	 * @param tab
	 */
	@SuppressWarnings("rawtypes")
	private void printObject(Collection<?> t, int tab) {
		for (Iterator e = t.iterator(); e.hasNext();) {
			printTab(tab + 1);
			printObject(e.next(), tab + 1);
			if (e.hasNext())
				debugln(",");
		}
	}

	/**
	 * Prints a Map
	 * 
	 * @param t
	 * @param tab
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void printObject(Map t, int tab) {
		Set<Entry<String, Object>> entries = t.entrySet();
		for (Entry<String, Object> entry : entries) {
			Object key = (Object) entry.getKey();
			printTab(tab);
			debug(/* "\""+ */key.toString()/* +"\"" */ + " = ");
			printObject(t.get(key), tab);
			debugln(";");
		}
	}

	/**
	 * Prints a string to a file along with a newline character
	 * 
	 * @param t
	 */
	private void debugln(String t) {
		debug(t + "\r\n");
	}

	/**
	 * Appends the argument to the Stringbuffer
	 * 
	 * @param t
	 */
	private void debug(String t) {
		stringToCopy.append(t);
	}

	/***
	 * Prints the required number of tabs
	 * 
	 * @param no
	 */
	private void printTab(int no) {
		for (int i = 0; i < no - 1; i++)
			debug("   ");
	}

	/**
	 * Verifies the string to check if it contains only valid chararacters As of
	 * now Validate Characters are : Any alphabet, '(', ')', ' '
	 * 
	 * @param aString
	 * @return true | false
	 */
	public boolean validateString(String aString) {
		for (int i = 0; i < aString.length(); i++) {
			char c = aString.toUpperCase().charAt(i);
			if (((c < 65) || (c > 90)) && c != 32 && c != 40 && c != 41) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Object map write to file
	 * 
	 * @param file
	 * @return text
	 * @throws Exception
	 */
	public Object printToFile(File file) {
		if (!file.exists()) {
              return null;
		}
		this.printObject();
		try {
			FileWriter fileToWrite= new FileWriter(file);
			fileToWrite.write(stringToCopy.toString());
			fileToWrite.flush();
			fileToWrite.close();
		} catch (IOException e) {
		}
		return stringToCopy.toString();
	}

	/**
	 * Object map write to file
	 * 
	 * @param file
	 * @return text
	 * @throws Exception
	 */
	public Object printToFile(String filePath) {
		File file = new File(filePath);
		this.printToFile(file);
		return stringToCopy.toString();
	}
}
