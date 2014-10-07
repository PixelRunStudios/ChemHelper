package com.github.pixelrunstudios.ChemHelper;

import java.util.Map;

public class MolarMass{

	public static double calculate(ChemistryUnit inZ,
			Map<String,String> data){
		double mass = 0.0;
		/*for(Map.Entry<String, Integer> entry : inZ.entrySet()){
			mass += Double.parseDouble(data.get(entry.getKey().toLowerCase()+"_atomic-mass"))*entry.getValue();
		}*/
		if(inZ.getType() == ChemistryUnit.TYPE_BASE){
			return Double.parseDouble(data.get(inZ.getName().toLowerCase()+"_atomic-mass"));
		}
		for(Map.Entry<ChemistryUnit, Integer> unit : inZ.getSubUnits().entrySet()){
			mass += calculate(unit.getKey(), data) * unit.getValue();
		}
		return mass;
	}

	/*public static double calculateExpression(
			Map<Map<String, Integer>, Integer> elements,
			Map<String, String> data){
		double mass = 0.0;
		for(Map.Entry<Map<String, Integer>, Integer> ex : elements.entrySet()){
			mass += ex.getValue() * calculate(ex.getKey(), data);
		}
		return mass;
	}*/
}
