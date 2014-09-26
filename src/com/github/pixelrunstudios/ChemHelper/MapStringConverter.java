package com.github.pixelrunstudios.ChemHelper;

import java.util.HashMap;
import java.util.Map;

public class MapStringConverter{

	public static Map<String, String> stringToMap(String s){
		Map<String, String> hm = new HashMap<String, String>();
		if(s.length() == 0){
			return hm;
		}
		//String[] sa = s.split("(?<!\\\\)\n");
		String[] sa = s.split("(?<=([^\\\\])(\\\\\\\\){0," + s.length()/2 + "})\n");
		for(String sn : sa){
			if(sn.length() == 0){
				continue;
			}
			//String[] sna = sn.split("(?<!\\\\)=");
			String[] sna = sn.split("(?<=([^\\\\])(\\\\\\\\){0," + sn.length()/2 + "})=");
			String object;
			if(sna.length > 1){
				object = MapStringConverter.mapUnescape(sna[1]);
			}
			else{
				object = "";
			}
			String meta = MapStringConverter.mapUnescape(sna[0]);
			hm.put(meta, object);
		}
		return hm;
	}

	private static String mapUnescape(String in){
		in = in.replaceAll("(?<=(\\\\\\\\){0," + in.length()/2 + "})\\\\\n", "\n");
		in = in.replaceAll("(?<=(\\\\\\\\){0," + in.length()/2 + "})\\\\=", "=");
		in = in.replace("\\\\", "\\");
		return in;
	}

	public static <K, V> String mapToString(Map<K, V> map){
		String s = "";
		boolean first = true;
		for(Map.Entry<K, V> entry : map.entrySet()){
			if(first == true){
				first = false;
			}
			else{
				s += "\n";
			}
			s += MapStringConverter.mapEscape(entry.getKey().toString()) + "=" + MapStringConverter.mapEscape(entry.getValue().toString());
		}
		return s;
	}

	private static String mapEscape(String in){
		in = in.replace("\\", "\\\\");
		in = in.replace("=", "\\=");
		in = in.replace("\n", "\\\n");
		return in;
	}


}
