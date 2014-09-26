package com.github.pixelrunstudios.ChemHelper;

import java.util.Map;


public class Main{

	public static void main(String[] args){
		// TODO Auto-generated method stub
		Map<String, Integer> elements = ParseCompound.parseCompound("H2O");

		for(String name: elements.keySet()){
			String key = name.toString();
			String value = elements.get(name).toString();
			System.out.println(key + " " + value);
		}
	}


}
