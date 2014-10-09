package com.github.pixelrunstudios.ChemHelper;

import java.util.Map;


public class Main{

	public static final String COMPOUND = "(H(OH))2";
	public static void main(String[] args){
		// TODO Auto-generated method stub
		ChemistryUnit elements = ChemistryParser.parseCompound(COMPOUND);

		printU(elements);

		/*for(String name: elements.keySet()){
			String key = name.toString();
			String value = elements.get(name).toString();
			System.out.println(key + " " + value);
		}

		try{
			System.out.println(MolarMass.calculate(elements,
					FileToMap.readMapFromFile(new File("elements.txt"))));
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public static void printU(ChemistryUnit unit){
		if(unit.getType() == ChemistryUnit.TYPE_BASE){
			System.out.print(unit.getName());
		}
		else if(unit.getType() == ChemistryUnit.TYPE_NEST){
			for(Map.Entry<ChemistryUnit, Integer> unitx : unit.getSubUnits().entrySet()){
				System.out.print("(");
				printU(unitx.getKey());
				System.out.print("|" + unitx.getValue() + ")");
			}
		}
		else{
			throw new IllegalStateException("Illegal state");
		}
	}

}
