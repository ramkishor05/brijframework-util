package org.brijframework.util.reflect;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.text.StringUtil;

public class PackUtil {
	private static final char PKG_SEPARATOR = '.';
	private static final char DIR_SEPARATOR = '/';
	private static final String CLASS_FILE_SUFFIX = ".class";

	private static final ConcurrentHashMap<Object, Class<?>> jarClassHash = new ConcurrentHashMap<>();
	
	private static final ConcurrentHashMap<Object, Class<?>> proClassHash = new ConcurrentHashMap<>();

	private static PackUtil util;

	public static PackUtil getInstanse() {
		if (util == null) {
			synchronized (PackUtil.class) {
				util = InstanceUtil.getSingletonInstance(PackUtil.class);
			}
		}
		return util;
	}

	static {
		try {
			getJarClassList().forEach(clazz -> {
				jarClassHash.put(clazz.getName(), clazz);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			getProjectClassList().forEach(clazz->{
				proClassHash.put(clazz.getName(), clazz);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static String[] getClassPath() {
		String classpath = System.getProperty("java.class.path");
		return classpath.split(File.pathSeparator);
	}
	
	private synchronized static Set<Class<?>> getJarClassList() throws Exception{
		Set<Class<?>> classes = new HashSet<>();
		for (String element : getClassPath()) {
			if (element.endsWith("jar")) {
				classes.addAll(getJarClassList(element));
			}
		}
		return classes;
	}
	
	private synchronized static Set<Class<?>> getProjectClassList() throws Exception{
		Set<Class<?>> classes = new HashSet<>();
		FileSystemManager fileSystemManager = VFS.getManager();
		for (String element : getClassPath()) {
			if (!element.endsWith("jar")) {
				FileObject fileObject = (FileObject) fileSystemManager.resolveFile(element);
				String localFilePath = fileObject.getName().getPath();
				for (String scannedPackage : getProjectPackages(fileObject, localFilePath)) {
					classes.addAll(getClassList(scannedPackage));
				}
			}
		}
		return classes;
	}

	private synchronized static Set<Class<?>> getJarClassList(String path) throws Exception {
		Set<Class<?>> classes = new HashSet<>();
		try (ZipInputStream zip = new ZipInputStream(new FileInputStream(path))) {
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					String className = entry.getName().replace('/', '.'); // including ".class"
					if (className.startsWith("java") || className.startsWith("javax") || className.startsWith("com.sun")
							|| className.startsWith("sun")) {
						continue;
					}
					try {
						classes.add(Class.forName(className));
					} catch (Exception ignore) {
					}
				}
			}
		}
		return classes;
	}
	
	
	public synchronized static Set<Class<?>> getClassList() {
		Set<Class<?>> classes = new HashSet<>();
		try {
			classes.addAll(jarClassHash.values());
			classes.addAll(proClassHash.values());
		} catch (Exception e) {
		}
		return classes;
	}

	public synchronized static Collection<Class<?>> getProjectClasses(){
		return proClassHash.values();
	}

	public synchronized static Set<String> getJarClassNames(String path) throws Exception {
		Set<String> projectPackages = new HashSet<>();
		try (ZipInputStream zip = new ZipInputStream(new FileInputStream(path))) {
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					String className = entry.getName().replace('/', '.'); // including ".class"
					if (className.startsWith("java") || className.startsWith("javax") || className.startsWith("com.sun")
							|| className.startsWith("sun")) {
						continue;
					}
					projectPackages.add(className.substring(0, className.length() - ".class".length()));
				}
			}
		}
		return projectPackages;
	}

	public synchronized static Set<String> getProjectPackages() {
		Set<String> projectPackages = new HashSet<>();
		try {
			FileSystemManager fileSystemManager = VFS.getManager();
			for (String element : PathUtil.getClassPathURL()) {
				if (element.endsWith("jar")) {
					continue;
				} else {
					FileObject fileObject = (FileObject) fileSystemManager.resolveFile(element);
					String localFilePath = fileObject.getName().getPath();
					projectPackages.addAll(getProjectPackages(fileObject, localFilePath));
				}
			}
		} catch (FileSystemException | org.apache.commons.vfs2.FileSystemException e) {
		}
		return projectPackages;
	}

	private static Set<String> getProjectPackages(final FileObject fileObject, String localFilePath)
			throws FileSystemException, org.apache.commons.vfs2.FileSystemException {
		Set<String> projectPackages = new HashSet<>();
		FileObject[] children = fileObject.getChildren();
		for (FileObject child : children) {
			if (!child.getName().getBaseName().equals("META-INF")) {
				if (child.getType() == FileType.FOLDER) {
					projectPackages.addAll(getProjectPackages(child, localFilePath));
				} else if (child.getName().getExtension().equals("class")) {
					String parentPath = child.getParent().getName().getPath();
					parentPath = StringUtil.replaceText(parentPath, localFilePath, "");
					parentPath = parentPath.substring(parentPath.indexOf("/") + 1).replaceAll("/", ".");
					projectPackages.add(parentPath);
				}
			}
		}
		return projectPackages;
	}

	private static List<Class<?>> getClassList(File file, String scannedPackage) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String resource = scannedPackage + PKG_SEPARATOR + file.getName();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				classes.addAll(getClassList(child, resource));
			}
		} else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
			int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
			String className = resource.substring(0, endIndex);
			try {
				classes.add(Class.forName(className));
			} catch (Exception ignore) {
			}
		}
		return classes;
	}

	public static List<Class<?>> getClassList(String scannedPackage) {
		String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
		URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
		if (scannedUrl == null) {
		}
		File scannedDir = new File(scannedUrl.getFile());
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (scannedDir == null || scannedDir.listFiles() == null) {
			return classes;
		}
		for (File file : scannedDir.listFiles()) {
			classes.addAll(getClassList(file, scannedPackage));
		}
		return classes;
	}

	public static boolean isProjectClass(Class<? extends Object> clazz) {
		return proClassHash.containsKey(clazz.getName());
	}

	public static boolean isJarClass(Class<? extends Object> clazz) {
		return jarClassHash.containsKey(clazz.getName());
	}

	
	public static void main(String[] args) throws ClassNotFoundException {
		/*
		 * System.out.println(getAllClasses().size()); for(Class<?> clz:getAllClasses())
		 * { System.out.println(clz);
		 * if(clz.getName().equals(PackageUtil.class.getName())) {
		 * System.out.println(clz); } }
		 */
		System.out.println(isProjectClass(MethodUtil.class));
	}

	
}
