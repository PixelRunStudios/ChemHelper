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
		LinkedHashMap<String,Integer> elementsInParen = new LinkedHashMap<String,Integer>();
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
							eleTemp = elementsInParen;
						}
						else{
							eleTemp = elements;
						}
						if(eleTemp.containsKey(tempElement)){
							tempNumber = Integer.toString(eleTemp.get(tempElement)+Integer.parseInt(tempNumber));
						}
						if(!tempElement.equals("")){
							eleTemp.put(tempElement, Integer.parseInt(tempNumber));
						}
						firstInParen = false;
					}
					else{
						inParenToElements(elements, elementsInParen, tempNumber);
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
				if(inParen){
					firstEleStart = true;
					tempNumber = tempNumber == "" ? "1" : tempNumber;
					firstAfterParen = false;
					int z = findCloseIndex(compound, i);
					String tp = compound.substring(i, z);
					add(elementsInParen,parseCompound(tp));
					i = z - 1;
				}
				else{
					inParen = true;
					firstInParen = true;
				}
			}
		}
		tempNumber = tempNumber == "" ? "1" : tempNumber;
		if(afterParen){
			inParenToElements(elements, elementsInParen, tempNumber);
		}
		if(elements.containsKey(tempElement)){
			tempNumber = Integer.toString(elements.get(tempElement)+Integer.parseInt(tempNumber));
		}
		if(!tempElement.equals("")){
			elements.put(tempElement, Integer.parseInt(tempNumber));
		}
		return elements;
	}

	private static int findCloseIndex(String compound, int n){
		int nest = 0;
		for(int i = 0; i <= n; i++){
			if(compound.charAt(i) == '('){
				nest++;
			}
			else if(compound.charAt(i) == ')'){
				nest--;
			}
		}
		int newNest = 0;
		int lastFound = 0;
		for(int i = compound.length() - 1; i > n; i--){
			if(compound.charAt(i) == '('){
				newNest--;
			}
			else if(compound.charAt(i) == ')'){
				newNest++;
				if(newNest == nest){
					lastFound = i;
				}
			}
		}
		char cati = compound.charAt(lastFound + 1);
		if(cati >= '0'&& cati <= '9'){
			return lastFound + 2;
		}
		return lastFound + 1;
	}

	private static void add(LinkedHashMap<String, Integer> elementsInParen,
			LinkedHashMap<String, Integer> parseCompound){
		for(Map.Entry<String, Integer> entry : parseCompound.entrySet()){
			if(elementsInParen.containsKey(entry.getKey())){
				elementsInParen.put(entry.getKey(), elementsInParen.get(entry.getKey()) + entry.getValue());
			}
			else{
				elementsInParen.put(entry.getKey(), entry.getValue());
			}
		}
	}

	private static void inParenToElements(LinkedHashMap<String, Integer> elements,
			LinkedHashMap<String, Integer> elementsInParen, String tempNumber){
		for(Map.Entry<String, Integer> entry : elementsInParen.entrySet()){
			String key = entry.getKey();
			int value = entry.getValue() * Integer.parseInt(tempNumber);
			if(elements.containsKey(key)){
				value += elements.get(key);
			}
			elements.put(key, value);
		}
		elementsInParen.clear();
	}
}
