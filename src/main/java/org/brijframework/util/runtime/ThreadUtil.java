package org.brijframework.util.runtime;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ThreadUtil {
	/**
	 * <p>
	 * To sleep for specified seconds.
	 * </p>
	 * 
	 * @param seconds
	 *            till which you want program to sleep.
	 * @throws InterruptedException
	 * @since Foundation 1.0
	 * 
	 */
	public static void sleepForSeconds(int seconds) throws InterruptedException {
		Thread.sleep(seconds * 1000L);
	}

	public static boolean intrrupted(long id) throws InterruptedException {
		Set<Thread> set = Thread.getAllStackTraces().keySet();
		for (Thread thread : set) {
			if (thread.getId() == id) {
				Thread.interrupted();
				return true;
			}
		}
		return false;
	}
	
	
	public static List<Thread> getThreadGroup(String name) throws InterruptedException {
		List<Thread> threads=new ArrayList<>();
		Set<Thread> set = Thread.getAllStackTraces().keySet();
		for (Thread thread : set) {
			if (thread.getThreadGroup().getName().contentEquals(name)) {
				threads.add(thread);
			}
		}
		return threads;
	}
	
	
	public static List<Thread> getAliveThreadGroup(String name) throws InterruptedException {
		List<Thread> threads=new ArrayList<>();
		Set<Thread> set = Thread.getAllStackTraces().keySet();
		for (Thread thread : set) {
			if (thread.getThreadGroup().getName().contentEquals(name)) {
				if(thread.isAlive())
				threads.add(thread);
			}
		}
		return threads;
	}
	
	
	public static List<Thread> getALiveThreadGroup(String name,State state ) throws InterruptedException {
		List<Thread> threads=new ArrayList<>();
		Set<Thread> set = Thread.getAllStackTraces().keySet();
		for (Thread thread : set) {
			if (thread.getThreadGroup().getName().contentEquals(name)) {
				if(thread.isAlive()&& thread.getState().equals(state))
				threads.add(thread);
			}
		}
		return threads;
	}
	
	public static void sleepForSoManySeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000L);
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
	}

	public static void sleepForSoManyMilliSeconds(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}
	}

	public static void runGc(int n) {
		for (int i = 0; i < n; i++) {
			Runtime.getRuntime().gc();
			sleepForSoManySeconds(1);
		}
	}

	
}
