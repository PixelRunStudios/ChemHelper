package com.github.pixelrunstudios.ChemHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdealGas {

	public static final Map<String, Double> conversion = new HashMap<String, Double>();
	//pressure
	public static final double kPa = 1;
	public static final double Pa = 1000;
	public static final double atm = 0.0098692;
	public static final double mmHg = 7.5006;
	public static final double torr = 7.500592;
	public static final double psi = 0.1450377;
	public static final double inHg = 0.2952998;
	public static final double bar = 0.01;

	//volume
	public static final double L = 1;
	public static final double mL = 1000;
	public static final double m3 = 0.001;
	public static final double floz = 35.1950797;
	public static final double qt = 0.879876992;
	public static final double gal = 0.219969248;
	public static final double ft3 = 0.0353147;

	//ideal gas constant
	public static final double R = 8.3144621;

	//Temperature
	//C -> K = +273.15
	//K -> C = -273.15
	//F -> K = (x + 459.67) * 5/9.0
	//K -> F = (x * 9.0/5) - 459.67
	static{
		conversion.put("kPa", kPa);
		conversion.put("Pa", Pa);
		conversion.put("atm", atm);
		conversion.put("mmHg", mmHg);
		conversion.put("torr", torr);
		conversion.put("psi", psi);
		conversion.put("inHg", inHg);
		conversion.put("bar", bar);
		conversion.put("L", L);
		conversion.put("mL", mL);
		conversion.put("m3", m3);
		conversion.put("floz", floz);
		conversion.put("qt", qt);
		conversion.put("gal", gal);
		conversion.put("ft3", ft3);
	}
	public static List<Double> calc(String p1S, String p1US, String v1S, String v1US, String n1S, String t1S, String t1US,
			String p2S, String p2US, String v2S, String v2US, String n2S, String t2S, String t2US){
		ArrayList<Pair<Boolean, Pair<Double, String>>> input = new ArrayList<Pair<Boolean, Pair<Double, String>>>(8);
		input.add(isFilled(p1S, p1US));
		input.add(isFilled(v1S, v1US));
		input.add(isFilled(n1S, "mol"));
		input.add(isFilled(t1S, t1US));
		input.add(isFilled(p2S, p2US));
		input.add(isFilled(v2S, v2US));
		input.add(isFilled(n2S, "mol"));
		input.add(isFilled(t2S, t2US));

		int firstFilled = 0;
		int secondFilled = 0;
		int pCount = 0;
		Map<Integer, Pair<Double, String>> first = new HashMap<Integer, Pair<Double, String>>();
		Map<Integer, Pair<Double, String>> firstAfter = new HashMap<Integer, Pair<Double, String>>();
		Map<Integer, Pair<Double, String>> secondAfter = new HashMap<Integer, Pair<Double, String>>();
		Map<Integer, Pair<Double, String>> second = new HashMap<Integer, Pair<Double, String>>();
		Map<Integer, Pair<Double, String>> secondAfterAfter = new HashMap<Integer, Pair<Double, String>>();
		Map<Integer, Pair<Double, String>> firstAfterAfter = new HashMap<Integer, Pair<Double, String>>();


		for(Pair<Boolean, Pair<Double, String>> p : input){
			if(pCount < 4 && p.getValueOne()){
				firstFilled++;
				first.put(pCount, p.getValueTwo());
			}
			else if(pCount >= 4 && p.getValueOne()){
				secondFilled++;
				second.put(pCount-4, p.getValueTwo());
			}
			pCount++;
		}
		for(Map.Entry<Integer, Pair<Double, String>> p : first.entrySet()){
			if(p.getKey() == 0){
				firstAfter.put(p.getKey(), Pair.make(convert(p.getValue().getValueOne(), p.getValue().getValueTwo(), "kPa"), "kPa"));
			}
			if(p.getKey() == 1){
				firstAfter.put(p.getKey(), Pair.make(convert(p.getValue().getValueOne(), p.getValue().getValueTwo(), "L"), "L"));
			}
			if(p.getKey() == 2){
				firstAfter.put(p.getKey(), p.getValue());
			}
			if(p.getKey() == 3){
				firstAfter.put(p.getKey(), Pair.make(convert(p.getValue().getValueOne(), p.getValue().getValueTwo(), "K"), "K"));
			}
		}

		System.out.println(firstAfter);

		for(Map.Entry<Integer, Pair<Double, String>> p : second.entrySet()){
			if(p.getKey() == 0){
				secondAfter.put(p.getKey(), Pair.make(convert(p.getValue().getValueOne(), p.getValue().getValueTwo(), "kPa"), "kPa"));
			}
			if(p.getKey() == 1){
				secondAfter.put(p.getKey(), Pair.make(convert(p.getValue().getValueOne(), p.getValue().getValueTwo(), "L"), "L"));
			}
			if(p.getKey() == 2){
				secondAfter.put(p.getKey(), p.getValue());
			}
			if(p.getKey() == 3){
				secondAfter.put(p.getKey(), Pair.make(convert(p.getValue().getValueOne(), p.getValue().getValueTwo(), "K"), "K"));
			}
		}

		double lastVal = 0;

		if(firstFilled == 3){
			int missing = -1;
			for(int i = 0; i <= 3; i++){
				if(!firstAfter.containsKey(i)){
					missing = i;
				}

			}

			lastVal = all4(firstAfter, missing);
			if(missing == 0){
				lastVal = convert(lastVal, "kPa", input.get(0).getValueTwo().getValueTwo());
			}
			if(missing == 1){
				lastVal = convert(lastVal, "L", input.get(1).getValueTwo().getValueTwo());
			}
			if(missing == 3){
				lastVal = convert(lastVal, "K", input.get(3).getValueTwo().getValueTwo());
			}
			firstAfterAfter.putAll(first);
			firstAfterAfter.put(missing, Pair.make(lastVal, "std"));

		}


		if(firstFilled == 2 && secondFilled == 1){
			if(check(firstAfter, secondAfter).getValueOne()){
				int sameField = check(firstAfter, secondAfter).getValueTwo().getValueOne();
				int diffField = check(firstAfter, secondAfter).getValueTwo().getValueTwo();
				double middle = two(sameField, firstAfter.get(sameField).getValueOne(), secondAfter.get(sameField).getValueOne(), diffField, firstAfter.get(diffField).getValueOne());

				double end = 0.0;
				if(diffField == 0){
					end = convert(middle, "kPa", input.get(4).getValueTwo().getValueTwo());
				}
				else if(diffField == 1){
					end = convert(middle, "L", input.get(5).getValueTwo().getValueTwo());
				}
				else if(diffField == 2){
					end = middle;
				}
				else if(diffField == 3){
					end = convert(middle, "K", input.get(7).getValueTwo().getValueTwo());
				}
				System.out.println(end);

				secondAfterAfter.putAll(second);
				secondAfterAfter.put(diffField, Pair.make(end, "std"));
			}

		}

		Double[] total = new Double[8];
		for(Map.Entry<Integer, Pair<Double, String>> p : firstAfterAfter.entrySet()){
			total[p.getKey()] = p.getValue().getValueOne();
		}

		for(Map.Entry<Integer, Pair<Double, String>> p : secondAfterAfter.entrySet()){
			total[p.getKey() + 4] = p.getValue().getValueOne();
		}

		return Arrays.asList(total);

	}


	public static Pair<Boolean, Pair<Double, String>> isFilled(String s, String u){
		if(!s.equals("")){
			return Pair.make(true, Pair.make(Double.parseDouble(s), u));
		}else{
			return Pair.make(false, Pair.make(0.0,u));
		}

	}

	public static double all4(Map<Integer, Pair<Double,String>> first, int missing){
		double val = 0;
		if(missing == 0){
			val = 1.0/first.get(1).getValueOne()*R*first.get(2).getValueOne()*first.get(3).getValueOne();
		}
		else if(missing == 1){
			val = 1.0/first.get(0).getValueOne()*R*first.get(2).getValueOne()*first.get(3).getValueOne();
		}
		else if(missing == 2){
			val = 1.0*first.get(0).getValueOne()*first.get(1).getValueOne()/R/first.get(3).getValueOne();
		}
		else if(missing == 3){
			val = 1.0*first.get(0).getValueOne()*first.get(1).getValueOne()/first.get(2).getValueOne()/R;
		}
		return val;
	}

	public static double two(int sameField, double field1SVal, double field2SVal, int diffField, double field1DVal){


		double a = field1SVal;
		double b = field1DVal;
		double c = field2SVal;
		System.out.println("a:" + a);
		System.out.println("b:" + b);
		System.out.println("c:" + c);

		if(sameField == 2 || sameField == 3){
			a = 1.0/a;
			c = 1.0/c;
		}
		if(diffField == 2 || diffField == 3){
			b = 1.0/b;
		}

		double d = a*b*1.0/c;


		if(diffField == 2 || diffField == 3){
			d = 1.0/d;
		}
		return d;
	}
	public static double convert(double n, String first, String second){
		if(!first.equals("K") && !second.equals("K")){
			System.out.println(second);
			System.out.println(first);
			return n*(conversion.get(second)/conversion.get(first));
		}
		else if(first.equals("C") && second.equals("K")){
			return n+273.15;
		}else if(first.equals("K") && second.equals("C")){
			return n-273.15;
		}else if(first.equals("K") && second.equals("K")){
			return n;
		}else if(first.equals("F") && second.equals("K")){
			return (n + 459.67) * (5.0/9);
		}else if(first.equals("K") && second.equals("F")){
			return n * 9.0/5 - 459.67;
		}else{
			return -1;
		}
	}

	public static Pair<Boolean, Pair<Integer,Integer>> check(Map<Integer, Pair<Double,String>> first, Map<Integer, Pair<Double,String>> second){
		int firstVal = -1;
		int secondVal = -1;
		int counter = 0;
		for(Map.Entry<Integer, Pair<Double,String>> f : first.entrySet()){
			if(counter == 0){
				firstVal = f.getKey();
			}
			if(counter == 1){
				secondVal = f.getKey();
			}
			counter++;
		}
		for(Map.Entry<Integer, Pair<Double,String>> s : second.entrySet()){
			if(s.getKey() == firstVal){
				return Pair.make(true, Pair.make(s.getKey(), secondVal));
			}
			else if(s.getKey() == secondVal){
				return Pair.make(true, Pair.make(s.getKey(), firstVal));
			}
		}
		return Pair.make(false, Pair.make(-1,-1));

	}

}
