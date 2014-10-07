package com.github.pixelrunstudios.ChemHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParseCompound{
	public static ChemistryUnit parseCompound(String compound){
		if(findNest(compound, Integer.MIN_VALUE, false).getValueOne() != 0){
			throw new IllegalArgumentException("For input string: " + compound);
		}
		boolean inParen = false, inNumber = false, firstEleStart = false,
				afterParen = false, firstInParen = false, firstAfterParen = false;
		String tempElement = "";
		String tempNumber = "";
		ChemistryUnit elements = new ChemistryUnit();
		ChemistryUnit elementsInParen = new ChemistryUnit();
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
						ChemistryUnit eleTemp;
						if(parenEnd || inf){
							eleTemp = elementsInParen;
						}
						else{
							eleTemp = elements;
						}
						if(eleTemp.containsUnitKey(ChemistryUnit.mk(tempElement))){
							tempNumber = Integer.toString(eleTemp.getUnit(ChemistryUnit.mk(tempElement))+Integer.parseInt(tempNumber));
						}
						if(!tempElement.equals("")){
							eleTemp.putUnit(ChemistryUnit.mk(tempElement), Integer.parseInt(tempNumber));
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
		if(elements.containsUnitKey(ChemistryUnit.mk(tempElement))){
			tempNumber = Integer.toString(elements.getUnit(ChemistryUnit.mk(tempElement))+Integer.parseInt(tempNumber));
		}
		if(!tempElement.equals("")){
			elements.putUnit(ChemistryUnit.mk(tempElement), Integer.parseInt(tempNumber));
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

	private static void add(ChemistryUnit elementsInParen,
			ChemistryUnit parseCompound){
		for(Map.Entry<ChemistryUnit, Integer> entry : parseCompound.getSubUnits().entrySet()){
			if(elementsInParen.containsUnitKey(entry.getKey())){
				elementsInParen.putUnit(entry.getKey(), elementsInParen.getUnit(entry.getKey()) + entry.getValue());
			}
			else{
				elementsInParen.putUnit(entry.getKey(), entry.getValue());
			}
		}
	}

	private static void inParenToElements(ChemistryUnit elements,
			ChemistryUnit elementsInParen, String tempNumber){
		Integer i = Integer.parseInt(tempNumber);
		/*for(Map.Entry<ChemistryUnit, Integer> ix : elementsInParen.getSubUnits().entrySet()){
			elementsInParen.putUnit(ix.getKey(), ix.getValue() * i);
		}
		elements.putAllUnits(elementsInParen.getSubUnits());*/
		if(elementsInParen.unitSize() == 0 && elementsInParen.getName() == null){
			System.out.println("po");

			return;
		}
		elements.putUnit(elementsInParen, i);
		//elementsInParen.clearUnit();
	}

	public static ChemistryUnit
	parseExpression(String in){
		ChemistryUnit unit = new ChemistryUnit();
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
				unit.putUnit(parseCompound(s.substring(counter)), n);
			}
		}
		return unit;
	}

	public static Pair<ChemistryUnit, ChemistryUnit>
	parseEquation(String in, String out){
		ChemistryUnit inX = parseExpression(in);
		ChemistryUnit outX = parseExpression(out);
		if(balanced(inX, outX)){
			return new Pair<ChemistryUnit,
					ChemistryUnit>(inX, outX);
		}
		else{
			return new Pair<ChemistryUnit,
					ChemistryUnit>(inX, outX);
			//return balance(inX, outX);
		}
	}

	private static Pair<Map<Map<String, Integer>, Integer>,
	Map<Map<String, Integer>, Integer>> balance(
			Map<Map<String, Integer>, Integer> inX,
			Map<Map<String, Integer>, Integer> outX){
		List<Map<String, Integer>> ino = new LinkedList<Map<String, Integer>>();
		List<Map<String, Integer>> outo = new LinkedList<Map<String, Integer>>();
		ino.addAll(inX.keySet());
		System.out.println(ino);
		outo.addAll(outX.keySet());
		System.out.println(outo);
		return EquationBalancer.balance(ino, outo);
	}

	private static boolean balanced(ChemistryUnit inX,
			ChemistryUnit outX){
		// TODO Auto-generated method stub
		return true;
	}
}
