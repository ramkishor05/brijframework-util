package org.brijframework.util.location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.casting.DateUtil;
import org.brijframework.util.text.StringUtil;

public abstract class DirUtil {

	static List<File> folderList = new ArrayList<>();
	static List<File> filelist = new ArrayList<>();
	
	public static List<File> getSubDirs(File file) {
		List<File>subDirs=new ArrayList<>();
		if(file.isDirectory()) {
			for(File sub:file.listFiles()) {
				getSubDirs(subDirs,sub);
			}
		}
		return subDirs;
	}
	
	public static List<File> getSubDirs(List<File>subDirs,File file) {
		if(file.isDirectory()) {
			subDirs.add(file);
			for(File sub:file.listFiles()) {
				getSubDirs(subDirs,sub);
			}
		}
		return subDirs;
	}
	
	public static boolean ensureDirExists(File dir) {
		if (!dir.exists()) {
			boolean dirCreatedSuccessfully = dir.mkdirs();
			if (!dirCreatedSuccessfully) {
				String message = dir.getAbsolutePath() + " neither exists nor could it be created successfully";
				System.out.println(StringUtil.getCharNTimes('*', message.length() + 4));
				System.out.println("* " + message + " *");
				System.out.println(StringUtil.getCharNTimes('*', message.length() + 4));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * check is file or not
	 * 
	 * @param file
	 * @return boolean
	 */
	public static boolean isFile(File file) {
		return file.isFile();
	}

	/**
	 * check is folder or not
	 * 
	 * @param folder
	 * @return boolean
	 */
	public static boolean isFolder(File folder) {
		return folder.isDirectory();
	}
	
	public static File getVerifiedDir(String _dirPath) {
		Assertion.notNull(_dirPath, AssertMessage.arg_null_message);
		File dir = new File(_dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}
	/**
	 * create new folder in given path
	 * 
	 * @param folder
	 * @return boolean
	 */
	public static boolean createFolder(String folder) {
		Assertion.notNull(folder, AssertMessage.arg_null_message);
		File logDir = new File(folder);
		if (!logDir.exists()) {
			logDir.mkdirs();
			return false;
		}
		return true;
	}

	/**
	 * create new folder in given path
	 * 
	 * @param folder
	 * @return boolean
	 */
	public static boolean createFolder(File logDir) {
		Assertion.notNull(logDir, AssertMessage.arg_null_message);
		if (!logDir.exists()) {
			logDir.mkdirs();
			return false;
		}
		return true;
	}

	/**
	 * create new file in given path
	 * 
	 * @param file
	 * @return boolean
	 */
	public static File getDir(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		File logDir = new File(dirPath);
		if (!logDir.exists()) {
			logDir.mkdirs();
		}
		return logDir;
	}

	public static boolean createFile(String file) {
		Assertion.notNull(file, AssertMessage.arg_null_message);
		String path = "";
		String fileName = "";
		if (file.contains("/")) {
			path = file.substring(0, file.lastIndexOf("/"));
			fileName = file.substring(file.lastIndexOf("/") + 1, file.length());
		} else {
			fileName = file;
		}
		boolean status = false;
		try {
			File logFile = new File(getDir(path), fileName);
			fileName = logFile.getAbsolutePath();
			@SuppressWarnings("resource")
			FileWriter logWriter = new FileWriter(fileName, true);
			logWriter.write("");
			logWriter.flush();
		} catch (IOException e) {
		}
		return status;
	}

	/**
	 * remove file form directory
	 * 
	 * @param file
	 * @return boolean
	 */
	public static boolean removeFile(File file) {
		Assertion.notNull(file, AssertMessage.arg_null_message);
		return file.delete();
	}

	/**
	 * remove directory form path
	 * 
	 * @param foler
	 * @return boolean
	 */
	public static boolean removeFolder(File folder) {
		Assertion.notNull(folder, AssertMessage.arg_null_message);
		return folder.delete();
	}

	public static String getFilePath(String dir, String fileName) throws FileNotFoundException {
		Assertion.notNull(dir, AssertMessage.arg_null_message);
		Assertion.notNull(fileName, AssertMessage.arg_null_message);
		return getFileExists(dir, fileName).getAbsolutePath();
	}

	public static File getFileExists(String dir, String fileName) throws FileNotFoundException {
		Assertion.notNull(dir, AssertMessage.arg_null_message);
		Assertion.notNull(fileName, AssertMessage.arg_null_message);
		File direc = getDirPath(dir);
		File file = new File(direc + "/" + fileName);
		if (!file.exists()) {
			throw new FileNotFoundException(fileName + " not exists");
		}
		return file;
	}

	public static boolean getFileExists(String path) {
		Assertion.notNull(path, AssertMessage.arg_null_message);
		File file=getPath(path);
		if (file.exists()) {
			return true;
		};
		return false;
	}
	/**
	 * remove directory form path
	 * 
	 * @param dir
	 * @return boolean
	 */

	public static void renameDir(File file) {
		Assertion.notNull(file, AssertMessage.arg_null_message);
		String list[] = file.list();
		for (int i = 0; i < list.length; i++) {
			try {
				File delfile = new File(file, list[i]);
				Calendar fileMod = Calendar.getInstance();
				fileMod.setTime(new Date(delfile.lastModified()));
				String fileName = list[i].substring(0, list[i].lastIndexOf("."));
				String extensionName = list[i].substring(list[i].lastIndexOf(".") + 1, list[i].length());
				delfile.renameTo(
						new File(file, fileName + "_" + DateUtil.dateTimeString(fileMod) + "." + extensionName));
			} catch (Exception e) {
			}
		}
	}

	public static void deleteDir(File file) {
		Assertion.notNull(file, AssertMessage.arg_null_message);
		String list[] = file.list();
		for (int i = 0; i < list.length; i++) {
			try {
				File delfile = new File(file, list[i]);
				delfile.delete();
			} catch (Exception e) {
			}
		}
	}

	public static void deleteAllFile(File file, int offSet) {
		Assertion.notNull(file, AssertMessage.arg_null_message);
		Calendar previousDate = DateUtil.currentDate(offSet);
		System.out.println("Delete from " + file.getAbsolutePath() + " File modified before " + DateUtil.dateString(previousDate));
		String list[] = file.list();
		for (int i = 0; i < list.length; i++) {
			try {
				File delfile = new File(file, list[i]);
				Calendar fileMod = Calendar.getInstance();
				fileMod.setTime(new Date(delfile.lastModified()));
				if (fileMod.before(previousDate)) {
					delfile.delete();
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * clean directory form path
	 * 
	 * @param dir
	 * @return boolean
	 */
	public static boolean cleanDir(File dir) {
		Assertion.notNull(dir, AssertMessage.arg_null_message);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File aFile : files) {
					deleteDir(aFile);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * get total file in dir
	 * 
	 * @param dirPath
	 * @return File List
	 */
	public static List<File> getDirFiles(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		File dir = new File(dirPath);
		if (!dir.exists()) {
			throw new IllegalArgumentException("The given path does not exist.");
		}
		if (isFile(dir)) {
			throw new IllegalArgumentException("The given path is a file. A directory is expected.");
		}
		File[] subFiles = dir.listFiles();
		if (subFiles != null && subFiles.length > 0) {
			for (File aFile : subFiles) {
				if (aFile.isFile()) {
					filelist.add(aFile);
				}
			}
		}
		return filelist;
	}

	/**
	 * get total folder in dir
	 * 
	 * @param dirPath
	 * @return List<File>
	 */
	public static List<File> getDirFolders(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		File dir = new File(dirPath);
		if (!dir.exists()) {
			throw new IllegalArgumentException("The given path does not exist.");
		}
		if (!isFile(dir)) {
		}
		File[] subFiles = dir.listFiles();
		if (subFiles != null && subFiles.length > 0) {
			for (File aFile : subFiles) {
				if (aFile.isDirectory()) {
					folderList.add(aFile);
				}
			}
		}
		return folderList;
	}

	public static String[] getDirFolderList(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		String data = "";
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				data += file.getName() + " ";
			}
		}
		return data.split(" ");
	}

	/**
	 * Lists files and directories using String[] list() method
	 */
	public static String[] getFileList(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		String data = "";
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files.length == 0) {
			System.err.println("The directory is empty");
		}
		for (File file : files) {
			if (file.isFile()) {
				data += file.getName() + " ";
			}
		}
		return data.split(" ");
	}

	public static File getDirPath(String dirPath) throws FileNotFoundException {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		File file = new File(dirPath);
		if (!file.isDirectory()) {
			throw new FileNotFoundException("Directory Not found");
		}
		return file;
	}
	
	public static File getPath(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		return  new File(dirPath);
	}

	public static File[] dirFilesWithSubDir(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		File directory = new File(dirPath);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				filelist.add(file);
			} else if (file.isDirectory()) {
				dirFilesWithSubDir(file.getAbsolutePath());
			}
		}
		File[] files = new File[filelist.size()];
		int i = 0;
		for (File file : filelist) {
			files[i++] = file;
		}
		return files;
	}

	public static List<File> filesWithSubDir(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		File directory = new File(dirPath);
		List<File> list = new ArrayList<>();
		list.add(directory);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				list.add(file);
			} else if (file.isDirectory()) {
				list.addAll(filesWithSubDir(file.getAbsolutePath()));
			}
		}
		return list;
	}

	public static void deletePosDataFile(File file, int offSet) {
		Assertion.notNull(file, AssertMessage.arg_null_message);
		String list[] = file.list();
		for (int i = 0; i < list.length; i++) {
			try {
				String date = list[i].substring(list[i].length() - 14, list[i].length() - 4);
				Calendar cal = Calendar.getInstance(TimeZone.getDefault());
				Calendar dateCalOffset = DateUtil.offsetDate(cal, -offSet);
				Calendar fileDate = DateUtil.calenderForDate(date, "_", "yymmdd");
				if (fileDate.getTime().getTime() < dateCalOffset.getTime().getTime()) {
					File delfile = new File(file, list[i]);
					delfile.delete();
				}
			} catch (Exception e) {
				System.out.println("Unable to delete Log Files  \r\n" + list[i]);
			}
		}

	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getSubDirectories(File dir, boolean isImmediate) {
		Assertion.notNull(dir, AssertMessage.arg_null_message);
		if (dir.exists()) {
			File[] dirs = dir.listFiles();
			Vector subDirs = new Vector();
			for (int i = 0; i < dirs.length; i++) {
				if (dirs[i].isDirectory()) {
					subDirs.add(dirs[i]);
					if (!isImmediate) {
						subDirs.addAll(getSubDirectories(dirs[i], isImmediate));
					}
				}
			}
			return subDirs;
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static Vector getSubDirectories(File dir) {
		Assertion.notNull(dir, AssertMessage.arg_null_message);
		return getSubDirectories(dir, false);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getMatchingDirectoriesForAPattern(String pattern) {
		if (pattern.indexOf('*') != -1 && pattern.indexOf('?') != -1) {
			throw new RuntimeException("* and ? cannot co-exit in same pattern string: " + pattern);
		}
		pattern = pattern.replace('\\', '/');
		if (pattern.indexOf('?') != -1) {
			if (pattern.indexOf('?') < pattern.length() - 1) {
				throw new RuntimeException("? should be the last character in pattern string: " + pattern);
			} else {
				return getSubDirectories(new File(pattern.substring(0, pattern.indexOf('?'))));
			}
		}
		Vector matchingDirs = new Vector();
		if (pattern.indexOf('*') != -1) {
			File parentDir = new File(pattern.substring(0, pattern.indexOf('*')));
			if (parentDir.exists()) {
				Vector subDirs = getSubDirectories(parentDir);
				Vector baseDirTokens = getDirTokens(pattern);
				Enumeration enm = subDirs.elements();
				while (enm.hasMoreElements()) {
					File dir = (File) enm.nextElement();
					if (isDirPatternMatch(baseDirTokens, getDirTokens(dir))) {
						matchingDirs.addElement(dir);
					}
				}
			}
		} else {
			matchingDirs.addElement(new File(pattern));
		}
		return matchingDirs;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getMatchingDirectoriesForAPatternAlternateWay(String pattern) {
		pattern = pattern.replace('\\', '/');
		pattern = pattern.endsWith("/") ? pattern.substring(0, pattern.length() - 1) : pattern;

		Vector matchingParentDirs = new Vector();
		StringTokenizer tokenizer = new StringTokenizer(pattern, "*");
		if (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			File dir = new File(token);
			if (dir.exists()) {
				if (token.endsWith("/")) {
					matchingParentDirs.addAll(getSubDirectories(dir, true));
				} else {
					matchingParentDirs.add(dir);
				}
			}
		}
		if (matchingParentDirs.size() > 0) {
			while (tokenizer.hasMoreTokens()) {
				String newToken = tokenizer.nextToken();
				Vector tempMatchingParentDirs = new Vector();
				Enumeration enm = matchingParentDirs.elements();
				while (enm.hasMoreElements()) {
					File dir = (File) enm.nextElement();
					String path = dir.getAbsolutePath().replace('\\', '/');
					path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
					File newDir = new File(path + newToken);
					if (newDir.exists()) {
						if (newToken.endsWith("/")) {
							tempMatchingParentDirs.addAll(getSubDirectories(newDir, true));
						} else {
							tempMatchingParentDirs.add(newDir);
						}
					}
				}
				matchingParentDirs.clear();
				matchingParentDirs.addAll(tempMatchingParentDirs);
			}
		}
		return matchingParentDirs;
	}

	@SuppressWarnings("rawtypes")
	public static Vector getDirTokens(File dir) {
		Assertion.notNull(dir, AssertMessage.arg_null_message);
		return getDirTokens(dir.getAbsolutePath());
	}

	@SuppressWarnings("rawtypes")
	public static File getDestinationDirectory(File sourceDir, String sourcePattern, String destPattern) {
		Assertion.notNull(sourceDir, AssertMessage.arg_null_message);
		if (getDirTokens(sourcePattern, "*").size() != getDirTokens(destPattern, "*").size()) {
			throw new RuntimeException("Source pattern and destination pattern do not have equal number of *: " + sourcePattern + "    " + destPattern);
		}
		Vector sourcePatternTokens = getDirTokens(sourcePattern);
		Vector sourceTokens = getDirTokens(sourceDir);
		StringBuffer destBuf = new StringBuffer(destPattern);
		for (int i = 0; i < sourcePatternTokens.size(); i++) {
			if (((String) sourcePatternTokens.elementAt(i)).equals("*")) {
				String correspondingSourceDir = (String) sourceTokens.elementAt(i);
				destBuf = destBuf.replace(destBuf.indexOf("*"), destBuf.indexOf("*") + 1, correspondingSourceDir);
			}
		}
		return new File(destBuf.toString());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getDirTokens(String dirPath, String delim) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		StringTokenizer stringTokenizer = new StringTokenizer(dirPath, delim);
		Vector dirTokens = new Vector();
		while (stringTokenizer.hasMoreTokens()) {
			dirTokens.addElement(stringTokenizer.nextToken());
		}
		return dirTokens;
	}

	@SuppressWarnings("rawtypes")
	public static Vector getDirTokens(String dirPath) {
		Assertion.notNull(dirPath, AssertMessage.arg_null_message);
		return getDirTokens(dirPath, "\\/");
	}

	@SuppressWarnings("rawtypes")
	public static boolean isDirPatternMatch(Vector baseDirTokens, Vector dirTokens) {
		Assertion.notNull(baseDirTokens, AssertMessage.arg_null_message);
		boolean matchesOK = true;
		Enumeration baseEnum = baseDirTokens.elements();
		Enumeration enm = dirTokens.elements();
		while (baseEnum.hasMoreElements()) {
			String baseToken = (String) baseEnum.nextElement();
			String token = (String) enm.nextElement();
			if (!baseToken.equals("*") && !baseToken.equalsIgnoreCase(token)) {
				matchesOK = false;
				break;
			}
		}
		return matchesOK;
	}


	public static boolean removeDir(File dir) {
		if (dir != null && dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					boolean isSubDirDeletedSuccessfully = removeDir(files[i]);
					if (!isSubDirDeletedSuccessfully) {
						return false;
					}
				} else {
					boolean isFileDeletedSuccessfully = files[i].delete();
					if (!isFileDeletedSuccessfully) {
						return false;
					}
				}
			}
			return dir.delete();
		}
		return true;
	}

	

}
