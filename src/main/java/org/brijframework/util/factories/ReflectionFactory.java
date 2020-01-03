package org.brijframework.util.factories;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class ReflectionFactory {
	
	private static final String DOT_STR_SEPARATOR = ".";
	private static final String DIR_STR_SEPARATOR = "/";
	private static final char DOT_CHAR_SEPARATOR='.';
	private static final String CLASS_FILE_SUFFIX = ".class";
	private static final String JAR_FILE_SUFFIX = ".jar";
	private static final String ST_DOT="\\.";
	private static final String INTERNAL_CLASS="INTERNAL_CLASS";
	private static final String EXTERNAL_CLASS="EXTERNAL_CLASS";
	
	private ConcurrentHashMap<String, Set<Class<?>>> cache=new ConcurrentHashMap<>();
	
	{
		getCache().put(INTERNAL_CLASS,new HashSet<>());
		getCache().put(EXTERNAL_CLASS,new HashSet<>());
	}
	
	private static  ReflectionFactory factory;
	
	public static ReflectionFactory getFactory() {
		if(factory==null) {
			factory=new ReflectionFactory();
			factory.loadFactory();
		}
		return factory;
	}
	
	private void loadFactory() {
		try {
			this.getClassListFromInternal().forEach(cls->{
				getCache().get(INTERNAL_CLASS).add(cls);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			getClassListFromExternal().forEach(cls->{
				getCache().get(EXTERNAL_CLASS).add(cls);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ConcurrentHashMap<String, Set<Class<?>>> getCache() {
		return cache;
	}
	
	public Set<Class<?>> getInternalClassList(){
		return getCache().get(INTERNAL_CLASS);
	}
	
	public Set<Class<?>> getExternalClassList(){
		return getCache().get(EXTERNAL_CLASS);
	}

	public  ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public  Set<String> getJarPaths() {
		URL[] urls = ((URLClassLoader) getContextClassLoader()).getURLs();
		Set<String> jars = new HashSet<>();
		for (URL url : urls) {
			jars.add(url.getFile());
		}
		return jars;
	}

	public  Set<File> getJarFiles() {
		URL[] urls = ((URLClassLoader) getContextClassLoader()).getURLs();
		Set<File> jars = new HashSet<>();
		for (URL url : urls) {
			jars.add(new File(url.getFile()));
		}
		return jars;
	}
	
	public  Set<File> getJarFiles(Consumer<File> action) {
		URL[] urls = ((URLClassLoader) getContextClassLoader()).getURLs();
		Set<File> jars = new HashSet<>();
		for (URL url : urls) {
			action.accept(new File(url.getFile()));
		}
		return jars;
	}

	public  Set<String> getResourcePaths() throws IOException {
		Enumeration<URL> urls = getContextClassLoader().getResources("");
		Set<String> jars = new HashSet<>();
		while (urls.hasMoreElements()) {
			URL url = (URL) urls.nextElement();
			jars.add(url.getFile());
		}
		return jars;
	}

	public  Set<File> getResourceFiles() throws IOException {
		Enumeration<URL> urls = getContextClassLoader().getResources("");
		Set<File> jars = new HashSet<>();
		while (urls.hasMoreElements()) {
			URL url = (URL) urls.nextElement();
			jars.add(new File(url.getFile()));
		}
		return jars;
	}

	public  Set<URL> getResourceURLs() throws IOException {
		Enumeration<URL> urls = getContextClassLoader().getResources("");
		Set<URL> jars = new HashSet<>();
		while (urls.hasMoreElements()) {
			URL url = (URL) urls.nextElement();
			jars.add(url);
		}
		return jars;
	}


	private  Set<Class<?>> getClassListFromInternal() {
		Set<Class<?>> classes = new HashSet<>();
		try {
			getNameListFromInternal().forEach(className -> {
				Class<?> cls=getSafeClass(className);
				if(cls!=null) {
					classes.add(cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}
	
	public  Set<String> getNameListFromInternal() throws Exception {
		Set<String> classes = new HashSet<>();
		getResourceFiles().forEach(root -> {
			if (null != root.listFiles())
				for (File file : root.listFiles()) {
					if (file.isDirectory()) {
						try {
							classes.addAll(getNameListForInternal(file.getName()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
		});
		return classes;
	}

	
	/**
	 * Scans all classes accessible from the context class loader which belong to
	 * the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public  Set<String> getNameListForInternal(String packageName) throws Exception {
		ClassLoader classLoader = getContextClassLoader();
		return getNameListForInternal(packageName, classLoader);
	}
	
	/**
	 * Scans all classes accessible from the context class loader which belong to
	 * the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public  Set<String> getNameListForInternal(String packageName,ClassLoader classLoader) throws Exception {
		String path = packageName.replace(DOT_STR_SEPARATOR, DIR_STR_SEPARATOR);
		Enumeration<URL> resources = classLoader.getResources(path);
		Set<File> dirs = new HashSet<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		Set<String> classes = new HashSet<>();
		for (File directory : dirs) {
			classes.addAll(getNameListForInternal(directory, packageName));
		}
		return classes;
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base
	 *                    directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private Set<String> getNameListForInternal(File directory, String packageName) throws Exception {
		Set<String> classes = new HashSet<>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			classes.addAll(getNameListForInternalClass(file, packageName));
		}
		return classes;
	}
	
	private Set<String> getNameListForInternalClass(File file, String packageName) throws Exception {
		Set<String> classes = new HashSet<>();
		if (file.isDirectory()) {
			assert !file.getName().contains(ST_DOT);
			classes.addAll(getNameListForInternal(file, packageName + DOT_STR_SEPARATOR + file.getName()));
		} else if (file.getName().endsWith(CLASS_FILE_SUFFIX)) {
			classes.add(packageName + DOT_STR_SEPARATOR + file.getName().substring(0, file.getName().length() - 6));
		}
		return classes;
	}
	
	public boolean isJarClass(Class<?> clas) {
		return getCache().get(EXTERNAL_CLASS).stream().anyMatch(cls->cls.getName().equals(clas.getName()));
	}

	public boolean isProjectClass(Class<?> clas) {
		return getCache().get(INTERNAL_CLASS).stream().anyMatch(cls->cls.getName().equals(clas.getName()));
	}
	
	public void getNameListFromExternal(File jarFile,Consumer<String> action) throws Exception {
		if (jarFile.isDirectory() || !jarFile.getName().endsWith(JAR_FILE_SUFFIX)) {
			return ;
		}
		try (JarInputStream jar = new JarInputStream(new FileInputStream(jarFile))) {
			JarEntry entry;
			while (true) {
				entry = jar.getNextJarEntry();
				if (entry == null) {
					break;
				}
				if ((entry.getName().endsWith(CLASS_FILE_SUFFIX))) {
					String className = entry.getName().replace(DIR_STR_SEPARATOR, DOT_STR_SEPARATOR);
					className=className.substring(0, className.lastIndexOf(DOT_CHAR_SEPARATOR));
					action.accept(className);
				}
			}
		}
	}
	
	public  Set<String> getNameListFromExternal() throws Exception {
		Set<String> classes = new HashSet<>();
		getJarFiles(root -> {
			try {
				getNameListFromExternal(root,className->{
					classes.add(className);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return classes;
	}
	
	private Set<Class<?>> getClassListFromExternal() throws Exception {
		Set<Class<?>> classes = new HashSet<>();
		getNameListFromExternal().forEach(className -> {
			Class<?> cls=getSafeClass(className);
			if(cls!=null) {
				classes.add(cls);
			}
		});
		return classes;
	}
	
	
	public Class<?> getSafeClass(String className, ClassLoader classLoader) {
		try {
			return Class.forName(className,false,classLoader);
		} catch (Throwable e) {
			return null;
		}
	}
	
	public Class<?> getSafeClass(String className) {
		try {
			return getSafeClass(className,getContextClassLoader());
		} catch (Exception|NoClassDefFoundError e) {
			return null;
		}
	}

}
