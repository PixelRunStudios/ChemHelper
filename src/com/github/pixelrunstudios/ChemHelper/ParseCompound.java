package com.github.pixelrunstudios.ChemHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParseCompound{
	public static LinkedHashMap<String, Integer> parseCompound(String compound){
		boolean inParen = false, inNumber = false, firstEleStart = false,
				afterParen = false, firstInParen = false, firstAfterParen = false;
		String tempElement = "";
		String tempNumber = "";
		LinkedHashMap<String,Integer> elements = new LinkedHashMap<String,Integer>();
		LinkedHashMap<String,Integer> elementsInParenth = new LinkedHashMap<String,Integer>();
		for(int i = 0; i<compound.length();i++){
			char cati = compound.charAt(i);
			boolean parenEnd = cati == ')';
			if(cati >= 'A' && cati <= 'Z' || parenEnd){
				if(!firstEleStart){
					firstEleStart = true;
					firstInParen = false;
				}
				else{
					boolean inf = inParen && !firstInParen;
					if(parenEnd){
						inParen = false;
						afterParen = true;
						firstAfterParen = true;
					}
					inNumber = false;
					tempNumber = tempNumber == "" ? "1" : tempNumber;
					if(parenEnd || inf || !firstAfterParen){
						LinkedHashMap<String, Integer> eleTemp;
						if(parenEnd || inf){
							eleTemp = elementsInParenth;
						}
						else{
							eleTemp = elements;
						}
						if(eleTemp.containsKey(tempElement)){
							tempNumber = Integer.toString(eleTemp.get(tempElement)+Integer.parseInt(tempNumber));
						}
						eleTemp.put(tempElement, Integer.parseInt(tempNumber));
						firstInParen = false;
					}
					else{
						inParenToElements(elements, elementsInParenth, tempNumber);
						firstAfterParen = false;
						afterParen = false;
					}
					tempElement = "";
					tempNumber = "";
				}
				if(!parenEnd){
					tempElement += cati;
				}
			}
			else if(cati >= 'a' && cati <= 'z'){
				tempElement += cati;
			}
			else if(cati >= '0'&& cati <= '9'){
				if(!inNumber || afterParen){
					inNumber = true;
				}
				tempNumber += cati;
			}
			else if(cati == '('){
				inParen = true;
				firstInParen = true;
			}
		}
		tempNumber = tempNumber == "" ? "1" : tempNumber;
		if(afterParen){
			inParenToElements(elements, elementsInParenth, tempNumber);
		}
		if(elements.containsKey(tempElement)){
			tempNumber = Integer.toString(elements.get(tempElement)+Integer.parseInt(tempNumber));
		}
		if(tempElement != ""){
			elements.put(tempElement, Integer.parseInt(tempNumber));
		}
		return elements;
	}

	private static void inParenToElements(LinkedHashMap<String, Integer> elements,
			LinkedHashMap<String, Integer> elementsInParenth, String tempNumber){
		for(Map.Entry<String, Integer> entry : elementsInParenth.entrySet()){
			String key = entry.getKey();
			int value = entry.getValue() * Integer.parseInt(tempNumber);
			elementsInParenth.put(key, value);
			if(elements.containsKey(key)){
				value += elements.get(key);
			}
			elements.put(key, value);
		}
		elementsInParenth.clear();
	}
}
