package com.github.pixelrunstudios.ChemHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main{

	public static void main(String[] args){
		// TODO Auto-generated method stub
		parseCompound("(O)3H");
	}

	public static void parseCompound(String compound){
		boolean inParenth = false;
		boolean inNumber = false;
		boolean firstEle = false;
		boolean afterParenth =false;
		boolean firstInParenth = false;
		boolean firstAfterParenth = false;
		String tempElement = "";
		String tempNumber = "";
		HashMap<String,Integer> elements = new LinkedHashMap<String,Integer>();
		HashMap<String,Integer> elementsInParenth = new LinkedHashMap<String,Integer>();

		for(int i = 0; i<compound.length();i++){
			if(compound.charAt(i) >= 'A' && compound.charAt(i) <= 'Z' && !firstEle){
				System.out.println("hi1");
				firstEle = true;
				tempElement +=compound.charAt(i);
				firstInParenth = false;
			}else if(compound.charAt(i) >= 'A' && compound.charAt(i) <= 'Z' && firstEle && !inParenth){
				System.out.println("hi2");
				inNumber = false;
				if(tempNumber == ""){
					tempNumber = "1";
				}
				//System.out.println(tempElement);
				if(elements.containsKey(tempElement)){
					tempNumber = Integer.toString(elements.get(tempElement)+Integer.parseInt(tempNumber));
				}
				System.out.println("put1:" + tempElement);

				elements.put(tempElement, Integer.parseInt(tempNumber));
				tempElement = "";
				tempNumber = "";
				tempElement +=compound.charAt(i);

			}
			else if(compound.charAt(i) >= 'A' && compound.charAt(i) <= 'Z' && inParenth && firstInParenth && firstEle){
				System.out.println("hi3");
				inNumber = false;
				if(tempNumber == ""){
					tempNumber = "1";
				}
				if(elements.containsKey(tempElement)){
					tempNumber = Integer.toString(elements.get(tempElement)+Integer.parseInt(tempNumber));
				}
				System.out.println("put2:" + tempElement);

				elements.put(tempElement, Integer.parseInt(tempNumber));
				tempElement = "";
				tempNumber = "";
				tempElement +=compound.charAt(i);
				firstInParenth = false;
			}else if(compound.charAt(i) >= 'A' && compound.charAt(i) <= 'Z' && inParenth && !firstInParenth && firstEle){
				inNumber = false;
				System.out.println("hi4");
				if(tempNumber == ""){
					tempNumber = "1";
				}
				if(elementsInParenth.containsKey(tempElement)){
					tempNumber = Integer.toString(elementsInParenth.get(tempElement)+Integer.parseInt(tempNumber));
				}
				System.out.println("putP1:" + tempElement);
				System.out.println("putP1:" + tempNumber);

				elementsInParenth.put(tempElement, Integer.parseInt(tempNumber));
				tempElement = "";
				tempNumber = "";
				tempElement +=compound.charAt(i);
			}
			else if(compound.charAt(i) >= 'A' && compound.charAt(i) <= 'Z' && !inParenth && firstAfterParenth && firstEle){
				inNumber = false;
				if(tempNumber == ""){
					tempNumber = "1";
				}
				System.out.println("hi5");
				for(Map.Entry<String, Integer> entry : elementsInParenth.entrySet()){
					String key = entry.getKey();
					int value = entry.getValue() * Integer.parseInt(tempNumber);

					//System.out.println("putPM:" + tempElement);

					elementsInParenth.put(key, value);
					if(elements.containsKey(key)){
						value += elements.get(key);
					}
					System.out.println("Akey: "+key);
					System.out.println("Avalue: "+value);
					System.out.println("Avalue2: "+entry.getValue());

					//System.out.println("putM:" + tempElement);

					elements.put(key, value);
				}
				tempElement = "";
				tempNumber = "";
				tempElement +=compound.charAt(i);
				firstAfterParenth = false;
				afterParenth = false;
			}else{
				System.out.println("died");
				System.out.println("firstEle: "firstEle);
			}

			if(compound.charAt(i)>='a' && compound.charAt(i)<='z'){
				tempElement +=compound.charAt(i);
			}
			if(compound.charAt(i)>='0'&& compound.charAt(i) <= '9' && !inNumber && !afterParenth){
				inNumber = true;
				tempNumber +=compound.charAt(i);
			}else if(compound.charAt(i)>='0'&& compound.charAt(i) <= '9' && inNumber && !afterParenth){
				tempNumber +=compound.charAt(i);
			}
			else if(compound.charAt(i)>='0'&& compound.charAt(i) <= '9' && afterParenth){
				inNumber = true;
				tempNumber +=compound.charAt(i);
			}
			if(compound.charAt(i) == '('){
				inParenth = true;
				firstInParenth = true;
			}
			if(compound.charAt(i) == ')'){
				inParenth = false;
				afterParenth = true;
				inNumber = false;
				if(tempNumber == ""){
					tempNumber = "1";
				}
				if(elementsInParenth.containsKey(tempElement)){
					tempNumber = Integer.toString(elementsInParenth.get(tempElement)+Integer.parseInt(tempNumber));
				}
				System.out.println("putP:" + tempElement);

				elementsInParenth.put(tempElement, Integer.parseInt(tempNumber));
				tempElement = "";
				tempNumber = "";
				firstAfterParenth = true;
			}
		}

		if(tempNumber == ""){
			tempNumber = "1";
		}
		if(afterParenth){
			for(Map.Entry<String, Integer> entry : elementsInParenth.entrySet()){
				String key = entry.getKey();
				System.out.println("tempNumber: "+tempNumber);
				System.out.println("entry.getvalue: "+entry.getValue());

				int value = entry.getValue() * Integer.parseInt(tempNumber);
				//System.out.println(entry.getValue());
				//System.out.println(Integer.parseInt(tempNumber));

				//System.out.println("beforeV"+value);

				//System.out.println("putPM2:" + tempElement);

				elementsInParenth.put(key, value);
				if(elements.containsKey(key)){
					value += elements.get(key);
				}
				System.out.println("key: "+key);
				System.out.println("value: "+value);
				System.out.println("value2: "+entry.getValue());

				//System.out.println("value"+ value);
				//System.out.println("putM2:" + tempElement);

				elements.put(key, value);
			}
		}

		//System.out.println(tempElement);
		if(elements.containsKey(tempElement)){
			tempNumber = Integer.toString(elements.get(tempElement)+Integer.parseInt(tempNumber));
		}
		if(tempElement != ""){
			System.out.println("put3:" + tempElement);
			elements.put(tempElement, Integer.parseInt(tempNumber));

		}
		inNumber = false;
		inParenth = false;
		for(String name: elements.keySet()){
			String key = name.toString();
			String value = elements.get(name).toString();
			System.out.println(key + " " + value);
		}
		//System.out.println(elements.size());
	}
}
