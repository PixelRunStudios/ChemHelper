package com.github.pixelrunstudios.ChemHelper;

import java.util.Map;

public class MolarMass{

	public static double calculate(Map<String,Integer> elements,
			Map<String,String> data){
		double mass = 0.0;
		for(Map.Entry<String, Integer> entry : elements.entrySet()){
			mass += Double.parseDouble(data.get(entry.getKey().toLowerCase()+"_atomic-mass"))*entry.getValue();
		}
		return mass;
	}

	public static double calculateExpression(
			Map<Map<String, Integer>, Integer> elements,
			Map<String, String> data){
		double mass = 0.0;
		for(Map.Entry<Map<String, Integer>, Integer> ex : elements.entrySet()){
			mass += ex.getValue() * calculate(ex.getKey(), data);
		}
		return mass;
	}
}
