package org.brijframework.util;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.brijframework.util.location.DirUtil;

public class WatchFactory {

	private static WatchFactory factory;
	private LinkedHashMap<File, WatchConfig> cache = new LinkedHashMap<>();
	
	public static WatchFactory getFactory() {
		if(factory==null) {
			factory=new WatchFactory();
		}
		return factory;
	}
	
	public void register(File dir,Object object,String method) {
		WatchConfig config=new WatchConfig();
		config.setDir(dir);
		config.setMethod(method);
		config.setObject(object);
		this.register(config);
		for(File subDir:DirUtil.getSubDirs(dir)) {
			this.register(subDir,object,method);
		}
	}
	
	public void register(WatchConfig config) {
		this.getCache().put(config.getDir(), config);
		this.doService(config);
	}
	
	private void doService(WatchConfig config) {
		try {
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.submit(new WatchRunable(config));
		}catch (Exception e) {
		}
	}
	
	public WatchConfig fetch(File key) {
		return getCache().get(key);
	}
	
	public LinkedHashMap<File, WatchConfig> getCache() {
		return cache;
	}
	
}


