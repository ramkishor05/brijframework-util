package org.brijframework.util.resouces;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.brijframework.util.location.DirUtil;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.location.StreamUtil;
import org.brijframework.util.validator.ValidationUtil;

public class ResourcesUtil {
	private static final String META_INF = "META-INF";

	public static Collection<? extends File> getCurrentResources(String path, String extesions) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return getCurrentResources(classLoader, path, extesions);
	}

	public static Collection<? extends File> getCurrentResources(ClassLoader classLoader, String path, String extesions)
			throws IOException {
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<File> files = new ArrayList<File>();
		for (File directory : dirs) {
			files.addAll(findCurrentFiles(directory, extesions));
		}
		return files;
	}

	public static Collection<? extends File> findCurrentFiles(File directory, String... extesions) {
		List<File> files = new ArrayList<>();
		if (!directory.exists()) {
			return files;
		}
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				files.add(file);
			} else {
				if (isValid(file, extesions)) {
					files.add(file);
				}
			}
		}
		return files;
	}

	public static List<File> getResources(ClassLoader classLoader, String path, String... extesions)
			throws IOException {
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		List<File> files = new ArrayList<File>();
		for (File file : dirs) {
			if (file.isDirectory()) {
				files.addAll(findFiles(file, extesions));
			} else {
				files.add(file);
			}
		}
		return files;
	}

	public static List<File> getResources(String path, String... extesions) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return getResources(classLoader, path, extesions);
	}

	public static URL getResource(String path) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResource(path);
	}

	public static Collection<? extends File> findFiles(File directory, String... extesions) {
		List<File> files = new ArrayList<>();
		if (!directory.exists()) {
			return files;
		}
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				files.addAll(findFiles(file, extesions));
			} else {
				if (isValid(file, extesions)) {
					files.add(file);
				}
			}
		}
		return files;
	}

	private static boolean isValid(File file, String... extesions) {
		if (extesions == null || extesions.length == 0) {
			return true;
		}
		for (String extesion : extesions) {
			if (extesion != null && file.getName().endsWith(extesion)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong to
	 * the given package and subpackages.
	 *
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				String cls = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
				Class<?> _cls = null;
				try {
					_cls = Class.forName(cls);
				} catch (ClassNotFoundException | NoClassDefFoundError | ExceptionInInitializerError e) {
					// my class isn't there!
				}
				if (_cls != null)
					classes.add(_cls);
			}
		}
		return classes;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong to
	 * the given package and subpackages.
	 *
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class<?>[] getClasses(ClassLoader classLoader) {
		assert classLoader != null;
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (String path : System.getProperty("java.class.path").split(":")) {
			File rootPath = new File(path);
			if (rootPath == null || rootPath.listFiles() == null) {
				continue;
			}
			for (File directory : rootPath.listFiles()) {
				if (directory.isDirectory()) {
					String packageName = directory.getName();
					if (!META_INF.equals(packageName)) {
						try {
							classes.addAll(findClasses(directory, packageName));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return classes.toArray(new Class[classes.size()]);
	}

	public static Class<?>[] getClasses() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return getClasses(classLoader);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList loadFilesFromJarOrDirectly(String dirPath, URL dirUrl) {
		ArrayList inputStreamArray = new ArrayList();
		try {
			URLConnection urlConnection = dirUrl.openConnection();
			if (urlConnection instanceof JarURLConnection) { // if the file is
																// inside jar
				JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
				JarFile jarFile = jarURLConnection.getJarFile();
				String alternateDirPath = dirPath;
				if (dirPath.length() > 1 && dirPath.startsWith("/")) {
					alternateDirPath = dirPath.substring(1);
				}
				Enumeration enm = jarFile.entries();
				while (enm.hasMoreElements()) {
					JarEntry jarEntry = (JarEntry) enm.nextElement();
					if (!jarEntry.isDirectory() && (jarEntry.getName().startsWith(dirPath)
							|| jarEntry.getName().startsWith(alternateDirPath))) {
						inputStreamArray.add(jarFile.getInputStream(jarEntry));
					}
				}
			} else { // if the file can be accessed directly
				File dirOrFile = new File(dirUrl.getPath());
				if (dirOrFile.isDirectory()) {
					File[] files = dirOrFile.listFiles();
					for (int i = 0; i < files.length; i++) {
						FileInputStream fileInputStream = new FileInputStream(files[i]);
						inputStreamArray.add(fileInputStream);
					}
				} else {
					FileInputStream fileInputStream = new FileInputStream(dirOrFile);
					inputStreamArray.add(fileInputStream);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could Not load directory from directory " + dirPath, e);
		}
		return inputStreamArray;
	}

	public static boolean dirCreate(String dir) {
		String[] dirs = dir.split("~");
		for (String path : dirs) {
			File file = new File(PathUtil.locateURIConfig(path));
			if (!DirUtil.createFolder(file)) {
			}
		}
		return true;
	}

	public static List<Object> getLisFromJsonFile(String dir) {
		List<Object> loadtable = new ArrayList<>();
		File file = new File(PathUtil.locateURIConfig(dir));
		if (file != null) {
			if (file.isDirectory()) {
				File[] files = StreamUtil.getAllResurces(dir, ".json");
				for (File filez : files) {
					String json = "";
					try {
						json = new String(FileUtil.loadCharFile(filez));
					} catch (Exception e) {
						continue;
					}
					if (!json.isEmpty()) {
						List<Object> list = JSONUtil.getListFromJSONArray(json);
						if (ValidationUtil.isValidObject(list)) {
							loadtable.addAll(list);
						}
					}
				}
			} else if (file.isFile()) {
				String json = "";
				try {
					json = new String(FileUtil.loadCharFile(file));
				} catch (Exception e) {
					return null;
				}
				if (!json.isEmpty()) {
					List<Object> list = JSONUtil.getListFromJSONArray(json);
					if (!ValidationUtil.isEmptyObject(list)) {
						loadtable.addAll(list);
					}
				}
			}
		}
		return loadtable;
	}

	public static Map<String, Object> getMapFromJsonFile(String filePath) {
		if (!ValidationUtil.isEmptyObject(filePath)) {
			return new HashMap<>();
		}
		File file = new File(PathUtil.locateURIConfig(filePath));
		String json = "";
		try {
			json = new String(FileUtil.loadCharFile(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!json.isEmpty()) {
			return JSONUtil.getMapFromJSONString(json);
		}
		return null;
	}

	public static List<Object> getListFromXMLFile(String dir) {
		List<Object> loadtable = new ArrayList<>();
		if (!ValidationUtil.isEmptyObject(dir)) {
			return loadtable;
		}
		File file = new File(PathUtil.locateURIConfig(dir));
		if (file != null) {
			if (file.isDirectory()) {
				File[] files = StreamUtil.getAllResurces(dir, ".xml");
				for (File filez : files) {
					String xml = "";
					try {
						xml = new String(FileUtil.loadCharFile(filez));
					} catch (Exception e) {
						continue;
					}
					if (!xml.isEmpty()) {
						Map<String, Object> map = XMLUtil.rootMapFromXML(xml);
						if (map != null)
							loadtable.add(map);
					}
				}
			} else if (file.isFile()) {
				String xml = "";
				try {
					xml = new String(FileUtil.loadCharFile(file));
				} catch (Exception e) {
			       return null;
				}
				if (!ValidationUtil.isEmptyObject(xml)) {
					return null;
				}
				Map<?, ?> map = XMLUtil.rootMapFromXML(xml);
				if (map != null)
					loadtable.add(map);
			}
		}
		return loadtable;
	}

	public static Map<String, Object> getMapFromXMLFile(String filePath) {
		if (!ValidationUtil.isEmptyObject(filePath)) {
			return new HashMap<String, Object>();
		}
		File file = new File(PathUtil.locateURIConfig(filePath));
		String xml = "";
		try {
			xml = new String(FileUtil.loadCharFile(file));
		} catch (Exception e) {
			return null;
		}
		if (!ValidationUtil.isEmptyObject(xml)) {
			return null;
		}
		return (Map<String, Object>) XMLUtil.rootMapFromXML(xml);

	}

	public static List<Object> getListFromOBJFile(String beans) {
		return null;
	}

	public static List<Object> getMapFromOBJFile(String beans) {
		if (!ValidationUtil.isEmptyObject(beans)) {
			return new ArrayList<Object>();
		}
		File file = new File(PathUtil.locateURIConfig(beans));
		String json = "";
		try {
			json = new String(FileUtil.loadCharFile(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONUtil.getListFromJSONArray(json);
	}

	public static Object getObjectFromPersistFile(String beans) {
		if (!ValidationUtil.isEmptyObject(beans)) {
			return new ArrayList<Object>();
		}
		File file = new File(PathUtil.locateURIConfig(beans));
		return FileUtil.readObject(file);
	}

	public static List<Object> getListFromPersistFile(String dir) {
		List<Object> loadtable = new ArrayList<>();
		if (!ValidationUtil.isEmptyObject(dir)) {
			return loadtable;
		}
		File file = new File(PathUtil.locateURIConfig(dir));
		if (file != null) {
			if (file.isDirectory()) {
				File[] files = StreamUtil.getAllResurces(dir, ".ser");
				for (File filez : files) {
					Object obj = FileUtil.readObject(filez);
					loadtable.add(obj);
				}
			} else if (file.isFile()) {

				Object obj = FileUtil.readObject(file);
				loadtable.add(obj);
			}
		}
		return loadtable;
	}
}
