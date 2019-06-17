package org.brijframework.util.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class PropertiesUtil {
	
	public static Properties getProperties(File file){
		try {
			return getProperties(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Properties getProperties(InputStream inputStream ){
		Properties properties=new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static Properties getProperties(Map<String, Object> map){
		Properties properties=new Properties();
		if(map==null) {
			return properties;
		}
		String parentKey="";
		fillMap(properties, parentKey, map);
		return properties;
	}

	@SuppressWarnings("unchecked")
	public static void fillMap(Properties properties,String parentKey,Map<String, Object> map) {
		for(Entry<String, Object> entry:map.entrySet()) {
			if(entry.getValue() instanceof Map) {
				fillMap(properties,parentKey.isEmpty()?entry.getKey(): parentKey+"."+entry.getKey(), (Map<String, Object>)entry.getValue()); 
			}else if(entry.getValue() instanceof List<?>){
				fillList(properties,parentKey.isEmpty()?entry.getKey(): parentKey+"."+entry.getKey(), (List<Object>)entry.getValue()); 
			}else {
				properties.put(parentKey.isEmpty()?entry.getKey():parentKey+"."+entry.getKey(), entry.getValue());
			}
		}
		if(!parentKey.isEmpty())
		properties.put(parentKey, map);
	}
	
	@SuppressWarnings("unchecked")
	public static void fillList(Properties properties,String parentKey,List<Object> list) {
		int index=0;
		for(Object object:list) {
			if(object instanceof Map) {
				fillMap(properties, parentKey+"["+index+"]", (Map<String, Object>)object); 
			}else if(object instanceof List<?>){
				fillList(properties, parentKey+"["+index+"]", (List<Object>)object); 
			}else {
				properties.put(parentKey+"["+index+"]", object);
			}
			index++;
		}
		if(!parentKey.isEmpty())
		properties.put(parentKey, list);
	}
}
