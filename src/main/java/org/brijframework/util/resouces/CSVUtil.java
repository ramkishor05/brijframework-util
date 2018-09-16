package org.brijframework.util.resouces;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSVUtil {
	
	public static String  listToCSV(List<Map<String, Object>> dataArray) {
		String data = "";
		if (dataArray.size() > 0) {
			int hi = 0;
			LinkedHashMap<String, Object> headerMap = headerMap(dataArray);
			for (String key : headerMap.keySet()) {
				data += replaceComma(key);
				if (hi < headerMap.keySet().size() - 1) {
					data += ",";
				} else {
					data += "\n";
				}
				hi++;
			}
			for (Map<String, Object> map : dataArray) {
				int index = 0;
				for (String key : headerMap.keySet()) {
					if (map.get(key) != null) {
						data +=replaceComma(map.get(key).toString());
					} else {
						data += " ";
					}
					if (index < headerMap.keySet().size() - 1) {
						data += ",";
					} else {
						data += "\n";
					}
					index++;
				}
			}
		}
		return data;
	}
	
	private static String replaceComma(String description) {
		if (description != null) {
			return description.replaceAll("\\,", "^").replaceAll("\"", "'");
		}
		return " ";
	}
	private static LinkedHashMap<String, Object> headerMap(List<Map<String, Object>> dataArray) {
		int max = 0;
		int index = 0;
		int i = 0;
		for (Map<String, Object> map : dataArray) {
			if (max < map.keySet().size()) {
				index = i;
				max = map.keySet().size();
			}
			i++;
		}
		return new LinkedHashMap<>(dataArray.get(index));
	}
	
	public static void main(String[] args) {
		List<Map<String, Object>> dataArray=new ArrayList<>();
		LinkedHashMap<String, Object> map=new LinkedHashMap<>();
		map.put("id","1");
		map.put("name","ram kishor");
		map.put("email","ram@mail.com");
		map.put("addres","noida");
		for(int i=0;i<=10;i++){
			dataArray.add(map);
		}
		
		System.out.println(listToCSV(dataArray));
	}
}
