package org.brijframework.util.runtime;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

public class SystemUtil {
   public static void systemInfo(String[] args) {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File[] drives = File.listRoots();
		if (drives != null && drives.length > 0) {
			for (File aDrive : drives) {
				System.out.println("Drive Letter: " + aDrive);
				System.out.println("\tType: " + fsv.getSystemTypeDescription(aDrive));
				System.out.println("\tTotal space: " + aDrive.getTotalSpace());
				System.out.println("\tFree space: " + aDrive.getFreeSpace());
				System.out.println();
			}
		}
	}
	public static Long totalSpace(File disk) {
		return disk.getTotalSpace();
	}

	public static Long usedSpace(File disk) {
		return totalSpace(disk) - disk.getUsableSpace();
	}

	public static Long freeSpace(File disk) {
		return disk.getFreeSpace();
	}
	public static void printSystemMemStates() {
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();
		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		sb.append("Current Free memory: " + format.format(freeMemory / 1024) + "\r\n");
		sb.append("Allocated memory: " + format.format(allocatedMemory / 1024) + "\r\n");
		sb.append("Max memory: " + format.format(maxMemory / 1024) + "\r\n");
		sb.append("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\r\n");
		System.out.println(sb);
	}

	public static Map<String,Object> diskPaths() {
		Map<String,Object> map=new HashMap<>();
		Iterable<Path> iterable = FileSystems.getDefault().getRootDirectories();
		for (Path path : iterable) {
			Map<String,Object> fileMap=new HashMap<>();
			fileMap.put("AbsolutePath", path.toAbsolutePath());
			fileMap.put("Uri", path.toUri());
			fileMap.put("RootPath", path.getRoot());
			fileMap.put("NameCount", path.getNameCount());
			System.out.println(fileMap);
		}
		return map;
	}
	public static void diskStoreDefault() {
		Iterable<FileStore> iterable1= FileSystems.getDefault().getFileStores();
	    for (FileStore fileStore : iterable1) {
	    	try {
	    		Map<String,Object> fileStoreMap=new HashMap<>();
	    		fileStoreMap.put("TotalSpace",fileStore);
	    		fileStoreMap.put("TotalSpace",fileStore.getTotalSpace());
	    		fileStoreMap.put("Name",fileStore.name());
	    		fileStoreMap.put("UnallocatedSpace",fileStore.getUnallocatedSpace());
	    		fileStoreMap.put("UsableSpace",fileStore.getUsableSpace());
	    		fileStoreMap.put("isReadOnly", fileStore.isReadOnly());
	    		fileStoreMap.put("type", fileStore.type());
	    		System.out.println(fileStoreMap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static Map<String, Object> diskHistory(URI uri) {
		Iterable<FileStore> iterable= FileSystems.getFileSystem(uri).getFileStores();
		Map<String,Object> fileMap=new HashMap<>();
	    for (FileStore fileStore : iterable) {
	    	try {
	    		Map<String,Object> fileStoreMap=new HashMap<>();
	    		fileStoreMap.put("TotalSpace",fileStore);
	    		fileStoreMap.put("TotalSpace",fileStore.getTotalSpace());
	    		fileStoreMap.put("Name",fileStore.name());
	    		fileStoreMap.put("UnallocatedSpace",fileStore.getUnallocatedSpace());
	    		fileStoreMap.put("UsableSpace",fileStore.getUsableSpace());
	    		fileStoreMap.put("isReadOnly", fileStore.isReadOnly());
	    		fileStoreMap.put("type", fileStore.type());
	    		fileMap.put(fileStore.name(), fileStoreMap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	 return fileMap;
	}
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws URISyntaxException {
		diskPaths();
		System.out.println(FileSystems.getDefault().provider().installedProviders().get(0));
		
	}
}
