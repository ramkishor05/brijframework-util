package org.brijframework.util;

import java.io.File;
import java.util.LinkedHashMap;

import org.brijframework.util.accessor.LogicAccessorUtil;

public class WatchConfig {

	private volatile File dir;
	
	private String method;
	private Object object;
	private Object[] parameters;
	
	private volatile LinkedHashMap<String, File> subDirs=new LinkedHashMap<>();

	public void setSubDirs(LinkedHashMap<String, File> subDirs) {
		this.subDirs = subDirs;
	}
	
	public LinkedHashMap<String, File> getSubDirs() {
		return subDirs;
	}
	
	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
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
	

	public void recall(File path) {
		try {
			LogicAccessorUtil.callLogic(this.getObject(), this.getMethod(), path);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
