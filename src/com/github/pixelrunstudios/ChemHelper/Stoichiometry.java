package com.github.pixelrunstudios.ChemHelper;

import java.util.Map;

public class Stoichiometry{

	public static double convert(String inEq, String outEq, double amt,
			String inUnit, String inSub, String outUnit, String outSub,
			Map<String, String> data){
		// TODO Auto-generated method stub
		Pair<Map<Map<String, Integer>, Integer>,
		Map<Map<String, Integer>, Integer>> iao = ParseCompound.
		parseEquation(inEq, outEq);
		Map<Map<String, Integer>, Integer> in = iao.getValueOne();
		Map<Map<String, Integer>, Integer> out = iao.getValueTwo();
		Map<String, Integer> inZ = ParseCompound.parseCompound(inSub);
		Map<String, Integer> outZ = ParseCompound.parseCompound(outSub);
		int inNum = 0;
		int outNum = 0;
		for(Map.Entry<Map<String, Integer>, Integer> z : in.entrySet()){
			if(z.getKey().equals(inZ)){
				inNum = z.getValue();
			}
			if(z.getKey().equals(outZ)){
				outNum = z.getValue();
			}
		}
		for(Map.Entry<Map<String, Integer>, Integer> z : out.entrySet()){
			if(z.getKey().equals(inZ)){
				inNum = z.getValue();
			}
			if(z.getKey().equals(outZ)){
				outNum = z.getValue();
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

}
