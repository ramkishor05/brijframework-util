package org.brijframework.util.resouces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.util.validator.ValidationUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class JSONUtil {
	public static <T> boolean isJDKClass(T t) {
		return t.getClass().getPackage().getName().startsWith("java");
	}

	private static JSONObject getJSONObject(Object object) {
		Map<?, ?> map = (Map<?, ?>) object;
		return new JSONObject(map);
	}

	private static JSONArray getJSONArray(Object object) {
		JSONArray json = new JSONArray();
		for (Object value : ((Iterable<?>) object)) {
			json.put(getJSONObject(value));
		}
		return json;
	}

	public static Object getJSONObjectFromObject(Object object) {
		Object obj = null;
		if (object instanceof Map) {
			obj = getJSONObject(object);
		} else if (object instanceof Iterable) {
			obj = getJSONArray(object);
		}
		return obj;
	}

	public static HashMap<String, Object> getMapFromJSONObject(JSONObject object, String key) throws JSONException {
		return getMapFromJSONObject(object.getJSONObject(key));
	}

	public static HashMap<String, Object> getMapFromJSONObject(JSONObject object) throws JSONException {
		HashMap<String, Object> map = new HashMap<>();
		Iterator<?> keys = object.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			map.put(key, getObjectFromJSON(object.get(key)));
		}
		return map;
	}

	public static ArrayList<Object> getListFromJSONArray(JSONArray array) throws JSONException {
		ArrayList<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			list.add(getObjectFromJSON(array.get(i)));
		}
		return list;
	}

	public static Object getObjectFromJSON(Object json) throws JSONException {
		if (json == JSONObject.NULL) {
			return null;
		} else if (json instanceof JSONObject) {
			return getMapFromJSONObject((JSONObject) json);
		} else if (json instanceof JSONArray) {
			return getListFromJSONArray((JSONArray) json);
		} else {
			return json;
		}
	}

	public static JSONObject getJSONFromXML(String xml) {
		JSONObject jsonObj = null;
		try {
			jsonObj = XML.toJSONObject(xml);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	public static String getXMLFromJSON(Object obj) {
		try {
			return XML.toString(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject getJSONObjectFromJSONString(String json) {
		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONArray getJSONArrayFromJSONString(String json) {
		try {
			return new JSONArray(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getMapFromJSONString(String json) {
		return getMap(getJSONObjectFromJSONString(json));
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(JSONObject jsonObject) {
		Map<String, Object> map = new HashMap<>();
		Iterator<String> iterator = jsonObject.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = null;
			try {
				value = jsonObject.get(key);
			} catch (JSONException e) {

			}
			map.put(key, value instanceof JSONObject ? getMap((JSONObject) value)
					: value instanceof JSONArray ? getList((JSONArray) value) : value);
		}
		return map;
	}

	public static List<Object> getList(JSONArray array) {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			try {
				if (array.get(i) instanceof JSONObject) {
					list.add(getMap(array.getJSONObject(i)));
				} else {
					list.add(array.get(i));
				}
			} catch (JSONException e) {
			}
		}
		return list;
	}

	public static List<Object> getListFromJSONArray(String json) {
		json = json.trim();
		if (json.startsWith("[")) {
			JSONArray array = getJSONArrayFromJSONString(json);
			return getList(array);
		}
		return null;
	}

	public static String getJsonStringFromObject(Object cache) {
		StringBuilder builder = new StringBuilder();
		if (cache instanceof Map) {
			builder.append(getJsonStringFromMap((Map<?, ?>) cache));
		}
		if (cache instanceof Collection) {
			builder.append(getJsonStringFromCollection((Collection<?>) cache));
		}
		if (ValidationUtil.isPrimative(cache)) {
			builder.append(cache);
		} else {
			System.out.println(cache);
			//builder.append(getJsonStringFromObject(cache));
		}
		return builder.toString();
	}

	public static String getJsonStringFromCollection(Collection<? extends Object> cache) {
		StringBuilder builder = new StringBuilder();
		AtomicInteger count = new AtomicInteger(0);
		builder.append("[");
		cache.forEach(object -> {
			builder.append(getJsonStringFromObject(cache));
			if (count.incrementAndGet() < (cache.size())) {
				builder.append(",");
			}
		});
		builder.append("]");
		return builder.toString();
	}

	public static String getJsonStringFromMap(Map<?, ?> cache) {
		StringBuilder builder = new StringBuilder();
		AtomicInteger count = new AtomicInteger(0);
		builder.append("{");
		cache.entrySet().forEach(node -> {
			builder.append(node.getKey() + ":" + node.getValue());
			if (count.incrementAndGet() < (cache.size())) {
				builder.append(",");
			}
		});
		builder.append("}");
		return builder.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String json) {
		JSONObject object = null;
		try {
			object = new JSONObject(json);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Map<String, Object> source = new HashMap<>();
		Iterator<Object> keys = object.keys();
		while (keys.hasNext()) {
			Object key = keys.next();
			try {
				Object value = object.get(key.toString());
				if (value instanceof JSONArray) {
					source.put(key.toString(), JSONUtil.toList((JSONArray) value));
				} else if (value instanceof JSONObject) {
					source.put(key.toString(), JSONUtil.toMap((JSONObject) value));
				} else {
					source.put(key.toString(), value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return source;
	}

	public static List<Object> toList(JSONArray array) {
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < array.length(); i++) {
			try {
				Object value = array.get(i);
				if (value instanceof JSONArray) {
					list.add(JSONUtil.toList((JSONArray) value));
				} else if (value instanceof JSONObject) {
					list.add(JSONUtil.toMap((JSONObject) value));
				} else {
				   list.add(value);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static Map<?, ?> toMap(JSONObject object) {
		Map<String, Object> map = new HashMap<>();
		object.keys().forEachRemaining(key -> {
			Object value = null;
			try {
				value = object.get(key.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (value instanceof JSONArray) {
				map.put(key.toString(), JSONUtil.toList((JSONArray) value));
			} else if (value instanceof JSONObject) {
				map.put(key.toString(), JSONUtil.toMap((JSONObject) value));
			} else {
				map.put(key.toString(), value);
			}
		});
		return map;
	}
}
