package org.brijframework.util.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.brijframework.util.support.Constants;

public class HashMapUtil {

	public static HashMap<String, Object> getHashMap(Properties properties) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : properties.entrySet()) {

			if (entry.getKey() instanceof String) {
				fillMap(hashMap, (String) entry.getKey(), entry.getValue());
			} else {
				fillMap(hashMap, (String) entry.getKey(), entry.getValue());
			}

		}
		return hashMap;
	}

	private static void fillMap(HashMap<String, Object> hashMap, String key, Object value) {
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void fillMap(Properties properties) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			String[] keyArray = entry.getKey().toString().split(Constants.SPLIT_DOT);
			String keyPath = "";
			for (int index = 0; index < keyArray.length; index++) {
				String key = keyArray[index];
				if (hashMap.containsKey(key)) {
					hashMap.put(key, new HashMap<>());
				}
				Object value = hashMap.get(keyPath);
				if (value instanceof Map) {
					((Map) hashMap.get(keyPath)).put(key, null);
				} else {
					hashMap.put(key, value);
				}
				keyPath = key;
			}
		}
		System.out.println(hashMap);
	}
}
