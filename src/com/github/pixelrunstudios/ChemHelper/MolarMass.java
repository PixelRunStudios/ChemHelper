package com.github.pixelrunstudios.ChemHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class MolarMass{

	public static void calculate(LinkedHashMap<String,Integer> elements,
			LinkedHashMap<String,String> data, String input){
		double mass = 0.0;
		for(Map.Entry<String, Integer> entry : elements.entrySet()){
			mass += Double.parseDouble(data.get(entry.getKey().toLowerCase()+"_atomic-mass"))*entry.getValue();
		}
		System.out.println("molar mass of " + input+ " : "+mass);
	}
}
