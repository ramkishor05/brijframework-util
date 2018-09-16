package org.brijframework.util.runtime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import org.brijframework.util.casting.DateUtil;

public class LogUtil {

	public static void printStackTrace(String message) {
		message = message != null ? message : "";
		try {
			throw new RuntimeException(message);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public static StackTraceElement[] getStackTrace(String message) {
		message = message != null ? message : "";
		StackTraceElement[] stackTrace = null;
		try {
			throw new RuntimeException(message);
		} catch (RuntimeException e) {
			stackTrace = e.getStackTrace();
		}
		return stackTrace;
	}

	public static void printException(String errorMessage) {
		printException(errorMessage, null, false);
	}

	public static void printException(String errorMessage, Throwable e) {
		printException(errorMessage, e, true);
	}

	public static void printException(Throwable e) {
		printException(null, e, true);
	}

	public static void printException(String errorMessage, Throwable e, boolean verbose) {
		printException(errorMessage, e, verbose, null);
	}

	public synchronized static void printException(String errorMessage, Throwable e, boolean verbose, String logDir) {
		PrintWriter pw = getLogFile(logDir);
		if (errorMessage != null && !errorMessage.equals("")) {
			pw.println(errorMessage);
		}
		if (e != null) {
			if (verbose) {
				e.printStackTrace(pw);
			} else {
				pw.println(e.getMessage());
			}
		}
		pw.println("\r\n");
		pw.close();
	}

	public static void printError(String fileName, Throwable e) {
		Hashtable<Object, Object> hash = new Hashtable<>();
		if (e.getMessage() != null) {
			hash.put("message", e.getMessage());
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		hash.put("stackTrace", sw.getBuffer().toString());
		try {
			FileOutputStream file = new FileOutputStream("../error/" + fileName + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(hash);
			out.flush();
			file.close();
		} catch (IOException exp) {
			exp.printStackTrace();
		}
	}

	private static PrintWriter getLogFile(String logDir) {
		try {
			String fileName = "";
			String fullcomputername = null;
			try {
				InetAddress hostname[] = InetAddress.getAllByName(InetAddress.getLocalHost().getHostAddress());
				fullcomputername = hostname[0].getHostName() + "-";
			} catch (Exception e) {
				fullcomputername = "";
			}

			if (logDir == null) {
				File logDirCheck = new File("log");
				if (!logDirCheck.exists()) {
					logDirCheck.mkdirs();
				}
				fileName = "log/exception-log-of-" + fullcomputername + DateUtil.getyyyyMMMddDateString() + ".txt";
			} else {
				fileName = logDir + "/exception-log-of-" + fullcomputername +  DateUtil.getyyyyMMMddDateString() + ".txt";
			}
			PrintWriter pw = new PrintWriter(new FileOutputStream(fileName, true));

			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM d, yyyy 'at' hh:mm:ss a");
			pw.println("An Exception occurred on " + dateFormat2.format(gc.getTime()));
			return pw;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int getCharCount(String str, char c) {
		char[] charArray = str.toCharArray();
		int count = 0;
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == c) {
				count++;
			}
		}
		return count;
	}

	/*
	 * public static Vector getMatchingDirectoriesForAPatternAlternateWay(String
	 * pattern) { pattern = pattern.replace('\\', '/'); pattern =
	 * pattern.endsWith("/") ? pattern.substring(0, pattern.length()-1) :
	 * pattern;
	 *
	 * Vector asterikIndices = new Vector(); asterikIndices.add(new
	 * Integer(-1)); for (int i = 0; i < pattern.length(); i++) { if
	 * (pattern.charAt(i) == '*') { asterikIndices.add(new Integer(i)); } }
	 * asterikIndices.add(new Integer(pattern.length()-1));
	 *
	 * Vector matchingDirs = new Vector(); if (asterikIndices.size() == 2) {
	 * File dir = new File(pattern); if (dir.exists()) {
	 * matchingDirs.addElement(dir); } return matchingDirs; } Vector
	 * parentDirMatches = new Vector(); for (int i = 0; i <
	 * asterikIndices.size(); i++) { int delim1 =
	 * ((Integer)asterikIndices.elementAt(i)).intValue()+1; int delim2 =
	 * ((Integer)asterikIndices.elementAt(i+1)).intValue(); String
	 * dirStaticToken = pattern.substring(delim1, delim2); Vector
	 * tempParentDirMatches = new Vector(); if (parentDirMatches.size() > 0) {
	 * Enumeration enm = parentDirMatches.elements(); while
	 * (enm.hasMoreElements()) { File dir = (File)enm.nextElement(); String
	 * basePath = dir.getAbsolutePath().replace('\\', '/'); basePath =
	 * basePath.endsWith("/") ? basePath.substring(0, basePath.length()-1) :
	 * basePath; File newDir = new File(basePath + dirStaticToken); if
	 * (newDir.exists()) { tempParentDirMatches.addElement(newDir); } } }
	 * parentDirMatches.clear(); Enumeration enm =
	 * tempParentDirMatches.elements(); while (enm.hasMoreElements()) { File dir
	 * = (File)enm.nextElement(); parentDirMatches.addAll(getSubDirectories(dir,
	 * true)); } }
	 *
	 *
	 * File parentDir = new File(pattern.substring(0, pattern.indexOf('?')));
	 * String remainderPattern = pattern.substring(pattern.indexOf('?')+1); if
	 * (parentDir.exists()) { Vector subDirs = getSubDirectories(parentDir,
	 * true); Enumeration enm = subDirs.elements(); while
	 * (enm.hasMoreElements()) { File dir = (File)enm.nextElement(); String
	 * basePath = dir.getAbsolutePath().replace('\\', '/'); basePath =
	 * basePath.endsWith("/") ? basePath.substring(0, basePath.length()-1) :
	 * basePath; File totalPathDir = new File(basePath + remainderPattern); if
	 * (totalPathDir.exists()) { matchingDirs.addElement(totalPathDir); } } }
	 * return matchingDirs; }
	 */


	

	
	/*public static void log(String str, AppObject appObject) {
		log(str, false, appObject);
	}

	public static void log(String str, boolean isLoggingOn, AppObject appObject) {
		if (isLoggingOn) {
			if (appObject == null) {
				System.out.println(str);
			} else {
				appObject.log(str);
			}
		}
	}

	public static void logException(String str, Exception e, AppObject appObject) {
		if (appObject == null) {
			System.out.println(str);
			e.printStackTrace();
		} else {
			appObject.logException(str, e);
		}
	}

	public static boolean isCompatibleJvmVersion(AppObject appObject) {
		try {
			float jvmVersion = Float.parseFloat(System.getProperties().getProperty("java.vm.version").substring(0, 3));
			return jvmVersion > 1.4f;
		} catch (Exception e) {
			logException("Exception in getting JVM version", e, appObject);
		}
		return false;
	}

	public static StringBuffer getResponseFromUrls(Vector urls, String friendlyNameForResponse, boolean isLoggingOn, AppObject appObject) {
		if (urls == null) {
			throw new NullPointerException("urls is null");
		}
		StringBuffer retVal = null;
		if (urls.size() == 1) {
			String urlString = (String) urls.get(0);
			log("Attempting to " + friendlyNameForResponse + " on/from " + urlString, isLoggingOn, appObject);
			retVal = getUrlContent(urlString, false, appObject);
		} else {
			Random random = new Random();
			String firstUrlString = (String) urls.get(random.nextInt(urls.size() - 1));
			log("Attempting to " + friendlyNameForResponse + " on/from " + firstUrlString, isLoggingOn, appObject);
			retVal = getUrlContent(firstUrlString, false, appObject);
			if (retVal == null && urls.size() > 2) {
				for (int i = 0; i < urls.size() - 1; i++) {
					String urlString = (String) urls.get(i);
					if (!urlString.equalsIgnoreCase(firstUrlString)) {
						log("Attempting to " + friendlyNameForResponse + " on/from " + urlString, isLoggingOn, appObject);
						retVal = getUrlContent(urlString, false, appObject);
						if (retVal != null) {
							break;
						}
					}
				}
			}
		}
		if (retVal == null && urls.size() > 1) {
			String urlString = (String) urls.get(urls.size() - 1);
			log("Attempting to " + friendlyNameForResponse + " on/from fail-over url " + urlString, isLoggingOn, appObject);
			retVal = getUrlContent(urlString, false, appObject);
		}
		if (retVal == null) {
			log("Could Not " + friendlyNameForResponse + " on/from any of the configured urls", true, appObject);
		}
		return retVal;
	}

	public static StringBuffer getUrlContent(String urlString, AppObject appObject) {
		return getUrlContent(urlString, true, appObject);
	}

	public static StringBuffer getUrlContent(String urlString, boolean isThrowException, AppObject appObject) {
		URL url = null;
		HttpURLConnection urlConnection = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			url = new URL(urlString);
			urlConnection = (HttpURLConnection) (url.openConnection());
			if (isCompatibleJvmVersion(appObject)) {
				urlConnection.setConnectTimeout(60000);
				urlConnection.setReadTimeout(60000);
			}
			inputStream = urlConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer strBuf = new StringBuffer();
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				strBuf.append(line + " ");
			}
			return strBuf;
		} catch (Exception e) {
			if (e instanceof ConnectException || e instanceof UnknownHostException || (e.getMessage() != null && e.getMessage().toLowerCase().indexOf("connection reset") != -1)) {
				logException("Could not connect to URL " + url, e, appObject);
			} else {
				if (isThrowException) {
					throw new RuntimeException(e);
				} else {
					logException("Exception in connecting to URL " + url + " : Check logs", e, appObject);
				}
			}
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception ignored) {
			}
			try {
				inputStreamReader.close();
			} catch (Exception ignored) {
			}
			try {
				inputStream.close();
			} catch (Exception ignored) {
			}
			try {
				urlConnection.disconnect();
			} catch (Exception ignored) {
			}
		}
		return null;
	}*/

	


	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getHashForList(List list, String keyPath) {
		HashMap map = new HashMap();
		if (list != null) {
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				EntityObject entityObject = (EntityObject) itr.next();
				if (entityObject != null) {
					Object object = entityObject.valueForKeyPath(keyPath);
					if (object != null) {
						map.put(object, entityObject);
					}
				}
			}
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static CaseInsensitiveMap getCaseInsensitiveMapForList(Iterable iterable, String keyPath) {
		CaseInsensitiveMap map = new CaseInsensitiveMap();
		if (iterable != null) {
			Iterator itr = iterable.iterator();
			while (itr.hasNext()) {
				EntityObject entityObject = (EntityObject) itr.next();
				if (entityObject != null) {
					Object object = entityObject.valueForKeyPath(keyPath);
					if (object != null) {
						map.put((String) object, entityObject);
					}
				}
			}
		}
		return map;
	}*/

	
	/*@SuppressWarnings("unchecked")
	
*/
	

	/*public static String getCsl(Iterable iterable, String keyPath) {
		return getDelimiterString(iterable, keyPath, ", ");
	}
*/
	/*// For entityObject
	public static String getDelimiterString(Iterable iterable, String keyPath, String sepStr) {
		StringBuffer strBuf = new StringBuffer();
		Iterator itr = iterable.iterator();
		while (itr.hasNext()) {
			EntityObject entityObject = (EntityObject) itr.next();
			strBuf.append(entityObject.valueForKeyPath(keyPath));
			if (itr.hasNext()) {
				strBuf.append(sepStr);
			}
		}
		return strBuf.toString();
	}
*/
	
	
}
