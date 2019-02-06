package org.brijframework.util;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.brijframework.logger.LogTracker;
import org.brijframework.logger.constant.LogAccess;
import org.brijframework.util.accessor.LogicAccessorUtil;

public class WatchRunable implements Runnable{

	private WatchConfig config;
	
	private WatchService watcher;
	
	public WatchRunable(WatchConfig config,WatchService watcher) {
		this.config=config;
		this.watcher=watcher;
	}
	
	@Override
	public void run() {
		if(this.config==null || this.watcher==null) {
			return ;
		}
		watching(this.config,this.watcher);
	}

	@SuppressWarnings("unchecked")
	private void watching(WatchConfig config,WatchService watcher) {
		for (;;) {
			try {
				WatchKey key = watcher.take();
				List<WatchEvent<?>> pollEvents = key.pollEvents();
				Set<String> files=new HashSet<>();
				for (WatchEvent<?> event : pollEvents) {
					WatchEvent.Kind<?> kind = event.kind();
					if (kind == OVERFLOW) {
			            continue;
			        }
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();
					File file=fileName.toFile();
					if ((kind == ENTRY_MODIFY ||kind ==  ENTRY_CREATE ) && !files.contains(file.getName()) ) {
						files.add(file.getName());
						File path=new File(config.getDir().getPath(),file.getName());
						LogTracker.info("WatchRunable ",LogAccess.DEVELOPER, "Resource file has changed!!!" + ": " + path);
						recall(config,path);
					}
				}
				boolean valid = key.reset();
				files.clear();
				if (!valid) {
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
		}
	}

	private void recall(WatchConfig config,File path) {
		if(config==null) {
			return;
		}
		LogicAccessorUtil.callLogic(config.getObject(), config.getMethod(), path);
	}
	
	
}
