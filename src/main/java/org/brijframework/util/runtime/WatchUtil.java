package org.brijframework.util.runtime;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class WatchUtil {
	public static void watchService(URI paths) {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(paths);
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY,OVERFLOW);
			
			System.out.println("Watch Service registered for dir: " + dir.getFileName());
			
			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					return;
				}
				
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();
					
					System.out.println(kind.name() + ": " + fileName);
					if (kind == ENTRY_MODIFY && 
							fileName.toString().equals("FileWatchUtil.java")) {
						System.out.println("My source file has changed!!!");
					}
				}
				
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
			
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	
	public static void watchFile(File file) throws IOException {
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path dir = Paths.get(file.getParent());
		dir.register(watcher, ENTRY_MODIFY,OVERFLOW);
		
		System.out.println("Watch Service registered for dir: " + dir.getFileName());
		
		while (true) {
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException ex) {
				return;
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind<?> kind = event.kind();
				if (kind == ENTRY_MODIFY ) {
					System.out.println("My source file has changed!!!");
				}
			}
			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}
}
