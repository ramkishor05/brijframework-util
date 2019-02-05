package org.brijframework.util;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.brijframework.logger.LogTracker;
import org.brijframework.logger.constant.LogAccess;
import org.brijframework.util.accessor.LogicAccessorUtil;

public class WatchRunable implements Runnable{

	private WatchConfig config;
	
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
			Path dir = Paths.get(config.getDir());
			dir.register(watcher, ENTRY_MODIFY,OVERFLOW);
			watching(watcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void watching(WatchService watcher) {
		while (true) {
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException ex) {
				return;
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
				Path fileName = ev.context();
				if (kind == ENTRY_MODIFY ) {
					System.out.println("My source file has changed!!!");
					LogTracker.info("WatchRunable ",LogAccess.DEVELOPER, kind.name().toLowerCase() + ": " + ev.context().toUri());
					this.config.setParameters(new File(this.config.getDir().getPath(),fileName.toFile().getName()));
					recall(config);
				}
			}
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}

	private void recall(WatchConfig config) {
		if(config==null) {
			return;
		}
		LogicAccessorUtil.callLogic(config.getObject(), config.getMethod(), config.getParameters());
	}
	
	
}
