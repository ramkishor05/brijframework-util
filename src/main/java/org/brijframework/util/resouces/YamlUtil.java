package org.brijframework.util.resouces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

public class YamlUtil {
	public static HashMap<String, Object> getHashMap(File file ){
	    try {
			return getHashMap(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Properties getEnvProperties(File file ){
		Properties envproperties=new Properties();
	    HashMap<String, Object> properties=getHashMap(file );
		buildProps(envproperties, properties);
		return envproperties;
	}
	
	public static Properties getEnvProperties(String yml ){
		Properties envproperties=new Properties();
	    HashMap<String, Object> properties=getHashMap(yml );
		buildProps(envproperties, properties);
		return envproperties;
	}
	
	@SuppressWarnings("unchecked")
	private static void buildProps(Properties envproperties , Map<String, Object> properties) {
		properties.forEach((parentkey,value)->{
			if(value instanceof Map) {
				buildProps(parentkey, envproperties,( Map<String, Object>) value);
			}else if(value instanceof List) {
				buildProps(parentkey, envproperties, (List<Object>) value);
			}else {
				envproperties.put(parentkey, value);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private static void buildProps(String parentkey,Properties envproperties , List<Object> array) {
		envproperties.put(parentkey, array);
		for (int index = 0; index < array.size(); index++) {
			Object value=array.get(index);
			if(value instanceof Map) {
				buildProps(parentkey+"["+index+"]", envproperties,( Map<String, Object>) value);
			}else if(value instanceof List) {
				buildProps(parentkey+"["+index+"]", envproperties, (List<Object>) value);
			}else {
				envproperties.put(parentkey+"["+index+"]", value);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void buildProps(String parentkey,Properties envproperties , Map<String, Object> properties) {
		envproperties.put(parentkey, properties);
		properties.forEach((key,value)->{
			if(value instanceof Map) {
				buildProps(parentkey+"."+key, envproperties,( Map<String, Object>) value);
			}else if(value instanceof List) {
				buildProps(parentkey+"."+key, envproperties, (List<Object>) value);
			}else {
				envproperties.put(parentkey+"."+key, value);
			}
		});
	}
	
	public static Properties getProperties(File file ){
	    try {
			return getProperties(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Properties getProperties(InputStream in){
	    return getObjectMap(  Properties.class ,in);
	}

	public static HashMap<String, Object> getHashMap(InputStream in){
	    return getObjectMap(  HashMap.class ,in);
	}
	
	public static HashMap<String, Object> getHashMap(String yml){
	    return getObjectMap(  HashMap.class ,yml);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getObjectMap(Class<?> cls,String yml){
		Yaml yaml = new Yaml();  
	    return (T) yaml.loadAs( yml, cls);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getObjectMap(Class<?> cls,InputStream in){
		Yaml yaml = new Yaml();  
	    return (T) yaml.loadAs( in, cls);
	}
}
