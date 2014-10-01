package com.github.pixelrunstudios.ChemHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParseCompound{
	public static LinkedHashMap<String, Integer> parseCompound(String compound){
		if(findNest(compound, Integer.MIN_VALUE, false).getValueOne() != 0){
			throw new IllegalArgumentException("For input string: " + compound);
		}
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
				if(parenEnd){
					inParen = false;
					firstInParen = false;
				}
				if(!firstEleStart){
					firstEleStart = true;
					firstInParen = false;
				}
				else{
					boolean inf = inParen && !firstInParen;
					if(parenEnd){
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
		int nest = findNest(compound.substring(0, n + 1), Integer.MIN_VALUE, false).getValueOne();
		int lastFound = n + 1 + findNest(compound.substring(
				n + 1, compound.length()), -nest, true).getValueTwo();
		char cati = compound.charAt(lastFound + 1);
		if(cati >= '0'&& cati <= '9'){
			return lastFound + 2;
		}
		return lastFound + 1;
	}

	private static Pair<Integer, Integer> findNest(String value, int target, boolean fromEnd){
		int lastFound = Integer.MIN_VALUE;
		int nest = 0;
		for(int i = 0; i < value.length(); i++){
			int search = fromEnd ? value.length() - 1 - i : i;
			if(value.charAt(search) == '('){
				nest++;
			}
			else if(value.charAt(search) == ')'){
				nest--;
				if(target == nest){
					lastFound = search;
				}
			}
		}
		return new Pair<Integer, Integer>(nest, lastFound);
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

	public static Map<Map<String, Integer>, Integer>
	parseExpression(String in){
		Map<Map<String, Integer>, Integer> map =
				new HashMap<Map<String, Integer>, Integer>();
		in = in.replace(" ", "");
		String[] sa = in.split("\\+");
		for(String s : sa){
			String tempNumber = "";
			int counter = 0;
			char cati = s.charAt(counter);
			while(cati >= '0'&& cati <= '9'){
				tempNumber += cati;
				cati = s.charAt(++counter);
			}
			if(tempNumber.equals("")){
				tempNumber = "1";
			}
			int n = Integer.parseInt(tempNumber);
			if(n > 0){
				map.put(parseCompound(s.substring(counter)), n);
			}
		}
		return map;
	}

	public static Pair<Map<Map<String, Integer>, Integer>, Map<Map<String, Integer>, Integer>>
	parseEquation(String in, String out){
		Map<Map<String, Integer>, Integer> inX = parseExpression(in);
		Map<Map<String, Integer>, Integer> outX = parseExpression(out);
		if(balanced(inX, outX)){
			return new Pair<Map<Map<String, Integer>, Integer>,
					Map<Map<String, Integer>, Integer>>(inX, outX);
		}
		else{
			return balance(inX, outX);
		}
	}

	private static Pair<Map<Map<String, Integer>, Integer>,
	Map<Map<String, Integer>, Integer>> balance(
			Map<Map<String, Integer>, Integer> inX,
			Map<Map<String, Integer>, Integer> outX){
		// TODO Auto-generated method stub
		return null;
	}

	private static boolean balanced(Map<Map<String, Integer>, Integer> inX,
			Map<Map<String, Integer>, Integer> outX){
		// TODO Auto-generated method stub
		return true;
	}
}
