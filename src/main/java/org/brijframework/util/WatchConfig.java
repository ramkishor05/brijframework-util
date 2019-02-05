package org.brijframework.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;

public class WatchConfig {

	private volatile File file;
	private boolean isReload;
	private Method method;
	private Object object;
	private Object[] parameters;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isReload() {
		return isReload;
	}

	public void setReload(boolean isReload) {
		this.isReload = isReload;
	}

	public URI getDir() {
		if(getFile().isDirectory()) {
			return getFile().toURI();
		}
		return getFile().getParentFile().toURI();
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object... parameters) {
		this.parameters = parameters;
	}
}
