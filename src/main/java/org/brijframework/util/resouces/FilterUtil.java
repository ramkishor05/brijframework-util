package org.brijframework.util.resouces;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FilterUtil {
	public static List<File> folderList = new ArrayList<>();
	public static List<File> filelist = new ArrayList<>();

	/**
	 * Lists files and directories using File[] listFiles(FilenameFilter) method
	 */
	public static File[] getFilesListByExtension(String dirPath, final String extension) {
		FilenameFilter extensionFilter = new FilenameFilter() {
			public boolean accept(File file, String name) {
				if (name.endsWith(extension)) {
					return true;
				} else {
					return false;
				}
			}
		};
		File dir = new File(dirPath);
		File[] files = dir.listFiles(extensionFilter);
		return files;
	}

	/**
	 * Lists files and directories using File[] listFiles(FileFilter) method
	 */
	public static File[] getFileListBySize(String dirPath, final int size) {
		FileFilter sizeFilter = new FileFilter() {
			public boolean accept(File file) {
				if (file.isFile() && file.length() > size * 1024 * 1024) {
					return true;
				} else {
					return false;
				}
			}
		};
		File dir = new File(dirPath);
		File[] files = dir.listFiles(sizeFilter);
		return files;
	}

	public static File[] dirFilesWithSubDir(String dir, final String extension) {
		File directory = new File(dir);
		FilenameFilter extensionFilter = new FilenameFilter() {
			public boolean accept(File file, String name) {
				if (name.endsWith(extension) || file.isDirectory()) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] fList = directory.listFiles(extensionFilter);
		for (File file : fList) {
			if (file.isDirectory()) {
				dirFilesWithSubDir(file.getAbsolutePath(), extension);
			} else {
				filelist.add(file);
			}
		}
		File[] files = new File[filelist.size()];
		int i = 0;
		for (File file : filelist) {
			files[i++] = file;
		}
		return files;
	}

	public static File[] dirFilesWithSubDir(String dir, final int size) {
		File directory = new File(dir);
		FileFilter sizeFilter = new FileFilter() {
			public boolean accept(File file) {
				if (file.isFile() && file.length() > size * 1024 * 1024) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] fList = directory.listFiles(sizeFilter);
		for (File file : fList) {
			if (file.isFile()) {
				filelist.add(file);
			} else if (file.isDirectory()) {
				dirFilesWithSubDir(file.getAbsolutePath(), size);
			}
		}
		File[] files = new File[filelist.size()];
		int i = 0;
		for (File file : filelist) {
			files[i++] = file;
		}
		return files;
	}

}
