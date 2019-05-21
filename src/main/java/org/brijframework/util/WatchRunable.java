package org.brijframework.util;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WatchRunable implements Runnable{

	private WatchConfig config;
	private ConcurrentHashMap<String, Long> updated=new ConcurrentHashMap<String, Long>();
	
	public WatchRunable(WatchConfig config) {
		this.config=config;
	}
	
	@Override
	public void run() {
		if(this.config==null) {
			return ;
		}
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(config.getDir().toURI());
			dir.register(watcher, ENTRY_MODIFY,OVERFLOW,ENTRY_CREATE);
			watching(this.config,watcher);
		}catch (Exception e) {
		}
	}

	@SuppressWarnings("unchecked")
	private void watching(WatchConfig config,WatchService watcher) {
			try {
				WatchKey key = watcher.take();
				List<WatchEvent<?>> pollEvents = key.pollEvents();
				for (WatchEvent<?> event : pollEvents) {
					WatchEvent.Kind<?> kind = event.kind();
					if (kind == OVERFLOW) {
			            continue;
			        }
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();
					File file=fileName.toFile();
					File ctPath=new File(config.getDir().getPath(),file.getName());
					if (kind == ENTRY_MODIFY ||kind ==  ENTRY_CREATE ) {
						if(!ctPath.exists()) {
							continue;
						}
						Long lastModified=updated.get(ctPath.getAbsolutePath());
						if(lastModified!=null && lastModified==ctPath.lastModified()) {
							continue;
						}else {
							updated.put(ctPath.getAbsolutePath(),ctPath.lastModified());
						}
						config.recall(ctPath);
					}
				}
				pollEvents.clear();
				boolean valid = key.reset();
				if (valid) {
					watching(config, watcher);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
	}
}
