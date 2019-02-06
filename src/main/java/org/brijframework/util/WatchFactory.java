package org.brijframework.util;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.util.LinkedHashMap;

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
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(config.getDir().toURI());
			dir.register(watcher, ENTRY_MODIFY,OVERFLOW,ENTRY_CREATE);
			Thread thread=new Thread(new WatchRunable(config,watcher));
			thread.start();
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


