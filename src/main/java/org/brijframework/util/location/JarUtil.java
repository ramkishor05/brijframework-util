package org.brijframework.util.location;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarUtil {

	public static List<Class<?>> getClasses(String packageName, String jarName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		packageName = packageName.replaceAll("\\.", "/");
		File f = new File(jarName);
		if (f.exists()) {
			try {
				@SuppressWarnings("resource")
				JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
				JarEntry jarEntry;
				while (true) {
					jarEntry = jarFile.getNextJarEntry();
					if (jarEntry == null) {
						break;
					}
					System.out.println(jarEntry.getName());
					if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {
						classes.add(Class.forName(jarEntry.getName().replaceAll("/", "\\.").substring(0, jarEntry.getName().length() - 6)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return classes;
		} else {
			return null;
		}
	}

	@SuppressWarnings("resource")
	public static List<Class<?>> getClasses(File jarName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		File f = jarName;
		if (f.exists()) {
			try {
				JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
				JarEntry jarEntry;
				while (true) {
					jarEntry = jarFile.getNextJarEntry();
					if (jarEntry == null) {
						break;
					}
					System.out.println(jarEntry.getName());
					if ((jarEntry.getName().endsWith(".class"))) {
						classes.add(Class.forName(jarEntry.getName().replaceAll("/", "\\.").substring(0, jarEntry.getName().length() - 6)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return classes;
		} else {
			return null;
		}
	}

	public static List<Class<?>> getClasses(String jarName) throws ClassNotFoundException {
		File f = new File(jarName);
		return getClasses(f);
	}

	@SuppressWarnings("resource")
	public static List<File> getFiles(String jarName) throws ClassNotFoundException {
		ArrayList<File> files = new ArrayList<File>();
		File f = new File(jarName);
		if (f.exists()) {
			try {
				JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
				JarEntry jarEntry;
				while (true) {
					jarEntry = jarFile.getNextJarEntry();
					if (jarEntry == null) {
						break;
					}
					System.out.println(jarEntry.getName());
					files.add(new File(jarEntry.getName()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return files;
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		JarUtil.getClasses("D:\\doveloper\\modelframworks\\org.brijframework-util\\target\\org.brijframework-util-0.0.1.jar");
	}

}
