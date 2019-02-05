package org.brijframework.util;

import java.io.File;
import java.util.LinkedHashMap;

public class WatchFactory {

	private static WatchFactory factory;
	private LinkedHashMap<File, WatchConfig> cache = new LinkedHashMap<>();
	
	public static WatchFactory getFactory() {
		if(factory==null) {
			factory=new WatchFactory();
		}
		return factory;
	}
	
	public void register(WatchConfig config) {
		getCache().put(config.getFile(), config);
		Thread thread=new Thread(new WatchRunable(config));
		thread.start();
	}
	
	public WatchConfig fetch(File key) {
		return getCache().get(key);
	}
	
	public LinkedHashMap<File, WatchConfig> getCache() {
		return cache;
	}
	
}


