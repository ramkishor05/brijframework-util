package org.brijframework.util.runtime;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * This program demonstrates how to use the Preferences class to store and
 * retrieve preference data.
 * @author ram
 *
 */
public class PreferencesUtil {
	public static void main(String[] args) {
		Preferences ssPrefs= Preferences.systemNodeForPackage(PreferencesUtil.class);
		try {
			String[] keys = ssPrefs.keys();
			
			if (keys == null || keys.length == 0) {
				ssPrefs.put("hostname", "www.codejava.net");
				ssPrefs.putInt("port", 12345);
				ssPrefs.putBoolean("authentication", true);
				ssPrefs.putLong("timeout", 90000);
			} else {
				String hostname = ssPrefs.get("hostname", null);
				int port = ssPrefs.getInt("port", 80);
				boolean authentication = ssPrefs.getBoolean("authentication", false);
				long timeout = ssPrefs.getLong("timeout", 20000);

				String username = ssPrefs.get("username", "tom");
				
				System.out.println(hostname);
				System.out.println(port);
				System.out.println(authentication);
				System.out.println(timeout);
				System.out.println(username);
			}
		} catch (BackingStoreException ex) {
			System.err.println(ex);
		}
		Preferences userPrefs = Preferences.userNodeForPackage(PreferencesUtil.class);

		try {
			String[] keys = userPrefs.keys();
			
			if (keys == null || keys.length == 0) {
				userPrefs.put("hostname", "www.codejava.net");
				userPrefs.putInt("port", 12345);
				userPrefs.putBoolean("authentication", true);
				userPrefs.putLong("timeout", 90000);
			} else {
				String hostname = userPrefs.get("hostname", null);
				int port = userPrefs.getInt("port", 80);
				boolean authentication = userPrefs.getBoolean("authentication", false);
				long timeout = userPrefs.getLong("timeout", 20000);

				String username = userPrefs.get("username", "tom");
				
				System.out.println(hostname);
				System.out.println(port);
				System.out.println(authentication);
				System.out.println(timeout);
				System.out.println(username);
			}
		} catch (BackingStoreException ex) {
			System.err.println(ex);
		}
	}
}
