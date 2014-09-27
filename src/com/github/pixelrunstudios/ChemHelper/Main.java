package com.github.pixelrunstudios.ChemHelper;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class Main{

	public static final String COMPOUND = "H2O";
	public static void main(String[] args){
		// TODO Auto-generated method stub
		Map<String, Integer> elements = ParseCompound.parseCompound(COMPOUND);

		for(String name: elements.keySet()){
			String key = name.toString();
			String value = elements.get(name).toString();
			System.out.println(key + " " + value);
		}

		try{
			System.out.println(MolarMass.calculate(elements,
					FileToMap.readMapFromFile(new File("elements.txt")), COMPOUND));
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
