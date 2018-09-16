package org.brijframework.util.location;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;

public abstract class PathUtil {
	private static final String META_INF = "META-INF";
	public static ClassLoader overridenClassLoader;

	public static ClassLoader getContextClassLoader() {
		return overridenClassLoader != null ? overridenClassLoader : Thread.currentThread().getContextClassLoader();
	}

	public static URL[] getClassPath() {
		ClassLoader cl = getContextClassLoader();
		URL[] urls = ((URLClassLoader) cl).getURLs();
		return urls;
	}

	@SuppressWarnings("deprecation")
	public static List<URL> getResoucePath() throws URISyntaxException {
		ClassLoader cl = getContextClassLoader();
		List<URL> list = new ArrayList<>();
		Enumeration<URL> enumeration;
		try {
			enumeration = cl.getResources(META_INF);
			while (enumeration.hasMoreElements()) {
				File file = new File(enumeration.nextElement().getPath());
				if (file.isDirectory()) {
					for (File _file : DirUtil.getDirFiles(file.getAbsolutePath())) {
						list.add(_file.toURL());
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<String> getClassPathURL() {
		ArrayList<String> LIST = new ArrayList<>();
		for (URL url : getClassPath()) {
			LIST.add(url.getPath());
		}
		return LIST;
	}

	@SuppressWarnings("deprecation")
	public static URL findAsResource(final String _path) {
		Assertion.notNull(_path, AssertMessage.arg_null_message);
		URL url = null;
		ClassLoader contextClassLoader = getContextClassLoader();
		if (contextClassLoader != null) {
			url = contextClassLoader.getResource(_path);
		}

		url = PathUtil.class.getClassLoader().getResource(_path);
		if (url != null) {
			return url;
		}

		url = ClassLoader.getSystemClassLoader().getResource(_path);

		if (url == null) {
			try {
				return new File(_path).toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	public static URL locateURLConfig(final String path) {
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			return PathUtil.findAsResource(path);
		}
	}

	public static boolean isValidPath(final String _path) {
		Assertion.notNull(_path, AssertMessage.arg_null_message);
		try {
			URL url = new URL(_path);
			if (url != null) {
				try {
					url.toURI();
				} catch (URISyntaxException e) {
					return false;
				}
			}
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	public static URI locateURIConfig(final String path) {
		Assertion.notNull(path, AssertMessage.arg_null_message);
		try {
			try {
				return new URL(path).toURI();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			try {
				return PathUtil.findAsResource(path).toURI();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public static String getResoucesPath(String path) {
		Assertion.notNull(path, AssertMessage.arg_null_message);
		URL url = ClassLoader.getSystemClassLoader().getResource(path);
		return url.getPath();
	}

	public static URL getResoucesURL(String _path) {
		Assertion.notNull(_path, AssertMessage.arg_null_message);
		URL url = ClassLoader.getSystemClassLoader().getResource(_path);
		return url;
	}

	public static URI getResoucesURI(String _path) {
		Assertion.notNull(_path, AssertMessage.arg_null_message);
		URL url = ClassLoader.getSystemClassLoader().getResource(_path);
		URI uri = null;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			try {
				return PathUtil.findAsResource(_path).toURI();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		return uri;
	}

	public static String getContextNameFromURLString(String _path) {
		Assertion.notNull(_path, AssertMessage.arg_null_message);
		StringTokenizer tokenizer = new StringTokenizer(_path, "/");
		String lastToken = null;
		while (tokenizer.hasMoreTokens()) {
			lastToken = tokenizer.nextToken();
		}
		return lastToken;
	}

	public static File resources(String suffix) {
		for(File file:DirUtil.filesWithSubDir(System.getProperty("user.dir"))){
			if (file.getAbsolutePath().endsWith(suffix)) {
				return file;
			}
		};
		return null;
	}
	
	
}
