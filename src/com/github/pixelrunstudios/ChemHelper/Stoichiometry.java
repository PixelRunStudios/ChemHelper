package com.github.pixelrunstudios.ChemHelper;

import java.util.Map;

public class Stoichiometry{

	public static double convert(String inEq, String outEq, double amt,
			String inUnit, String inSub, String outUnit, String outSub,
			Map<String, String> data){
		// TODO Auto-generated method stub
		Pair<ChemistryUnit, ChemistryUnit> iao = ParseCompound.
				parseEquation(inEq, outEq);
		if(iao == null){
			//System.out.println("N/A");
			throw new IllegalArgumentException("Cannot be converted!");
		}
		ChemistryUnit in = iao.getValueOne();
		ChemistryUnit out = iao.getValueTwo();
		ChemistryUnit inZ = ParseCompound.parseCompound(inSub);
		ChemistryUnit outZ = ParseCompound.parseCompound(outSub);
		int inNum = 0;
		int outNum = 0;
		Pair<Boolean, Integer> inp = huntDown(in, inZ, outZ);
		if(inp.getValueTwo() < 0){
			//System.out.println("N/A");
			throw new IllegalArgumentException("Cannot be converted!");
		}
		else{
			if(inp.getValueOne()){
				inNum = inp.getValueTwo();
			}
			else{
				outNum = inp.getValueTwo();
			}
		}
		Pair<Boolean, Integer> oup = huntDown(out, inZ, outZ);
		if(oup.getValueTwo() < 0){
			System.out.println("N/A");
			return 0;
		}
		else{
			if(oup.getValueOne()){
				inNum = oup.getValueTwo();
			}
			else{
				outNum = oup.getValueTwo();
			}
		}
		double outOverIn = (double) outNum / (double) inNum;
		double inToMole = 0;
		switch(inUnit){
			case "g":
				inToMole = 1 / MolarMass.calculate(inZ, data);
				break;
			case "mol":
				inToMole = 1;
				break;
		}
		double outToMole = 0;
		switch(outUnit){
			case "g":
				outToMole = MolarMass.calculate(outZ, data);
				break;
			case "mol":
				outToMole = 1;
				break;
		}
		return amt * inToMole * outOverIn * outToMole;
	}

	//True if inz
	private static Pair<Boolean, Integer> huntDown(ChemistryUnit in, ChemistryUnit inZ,
			ChemistryUnit outZ){
		if(in.equals(inZ)){
			return Pair.make(true, 1);
		}
		else if(in.equals(outZ)){
			return Pair.make(false, 1);
		}
		else if(in.getType() == ChemistryUnit.TYPE_BASE){
			return Pair.make(false, -1);
		}
		for(Map.Entry<ChemistryUnit, Integer> cu : in.getSubUnits().entrySet()){
			Pair<Boolean, Integer> pair = huntDown(cu.getKey(), inZ, outZ);
			if(pair.getValueTwo().equals(-1)){
				continue;
			}
			else{
				return Pair.make(pair.getValueOne(), pair.getValueTwo() * cu.getValue());
			}
		}
		return Pair.make(false, -1);
	}

}
