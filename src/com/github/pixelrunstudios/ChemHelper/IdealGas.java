package com.github.pixelrunstudios.ChemHelper;

import java.util.ArrayList;
import java.util.HashMap;
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
	public static ArrayList<Double> calc(String p1S, String p1US, String v1S, String v1US, String n1S, String t1S, String t1US,
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
		ArrayList<Pair<Integer, Pair<Double, String>>> first = new ArrayList<Pair<Integer, Pair<Double, String>>>();
		ArrayList<Pair<Integer, Pair<Double, String>>> second = new ArrayList<Pair<Integer, Pair<Double, String>>>();

		for(Pair<Boolean, Pair<Double, String>> p : input){
			if(pCount < 4 && p.getValueOne()){
				firstFilled++;
				first.add(Pair.make(pCount, p.getValueTwo()));
			}
			else if(pCount >= 4 && p.getValueOne()){
				secondFilled++;
				second.add(Pair.make(pCount, p.getValueTwo()));
			}
			pCount++;
		}
		pCount = 0;
		for(Pair<Integer, Pair<Double, String>> p : first){
			if(p.getValueOne() == 0){
				first.add(pCount, Pair.make(p.getValueOne(), Pair.make(convert(p.getValueTwo().getValueOne(), p.getValueTwo().getValueTwo(), "kPa"), "kPa")));
			}
			if(p.getValueOne() == 1){
				first.add(pCount, Pair.make(p.getValueOne(), Pair.make(convert(p.getValueTwo().getValueOne(), p.getValueTwo().getValueTwo(), "L"), "L")));
			}
			if(p.getValueOne() == 3){
				first.add(pCount, Pair.make(p.getValueOne(), Pair.make(convert(p.getValueTwo().getValueOne(), p.getValueTwo().getValueTwo(), "K"), "K")));
			}
			pCount++;
		}

		pCount = 0;
		for(Pair<Integer, Pair<Double, String>> p : second){
			if(p.getValueOne() == 0){
				second.add(pCount, Pair.make(p.getValueOne(), Pair.make(convert(p.getValueTwo().getValueOne(), p.getValueTwo().getValueTwo(), "kPa"), "kPa")));
			}
			if(p.getValueOne() == 1){
				second.add(pCount, Pair.make(p.getValueOne(), Pair.make(convert(p.getValueTwo().getValueOne(), p.getValueTwo().getValueTwo(), "L"), "L")));
			}
			if(p.getValueOne() == 3){
				second.add(pCount, Pair.make(p.getValueOne(), Pair.make(convert(p.getValueTwo().getValueOne(), p.getValueTwo().getValueTwo(), "K"), "K")));
			}
			pCount++;
		}


		double lastVal = 0;

		if(firstFilled == 3){
			int missing = -1;
			boolean first0 = false;
			for(int i = 0; i < first.size(); i++){
				if(first0){
					if(first.get(i).getValueOne() != first.get(i-1).getValueOne() + 1){
						missing = first.get(i).getValueOne() - 1;
					}
				}
				if(i == 0 && first.get(0).getValueOne() == 0){
					first0 = true;
				}else{
					missing = 0;
				}
			}
			lastVal = all4(first, missing);
			if(missing == 0){
				lastVal = convert(lastVal, "kPa", input.get(0).getValueTwo().getValueTwo());
			}
			if(missing == 1){
				lastVal = convert(lastVal, "L", input.get(1).getValueTwo().getValueTwo());
			}
			if(missing == 3){
				lastVal = convert(lastVal, "K", input.get(3).getValueTwo().getValueTwo());
			}
			first.set(missing, Pair.make(missing, Pair.make(lastVal, "std")));

		}

		if(true/*stuff about first and second lines*/){
			//blah blah blah
			//to be added
		}

		ArrayList<Double> total = new ArrayList<Double>();
		for(Pair<Integer, Pair<Double, String>> p : first){
			total.add(p.getValueTwo().getValueOne());
		}

		for(Pair<Integer, Pair<Double, String>> p : second){
			total.add(p.getValueTwo().getValueOne());
		}

		return total;

	}


	public static Pair<Boolean, Pair<Double, String>> isFilled(String s, String u){
		if(!s.equals("")){
			return Pair.make(true, Pair.make(Double.parseDouble(s), u));
		}else{
			return Pair.make(false, Pair.make(0.0,u));
		}

	}

	public static double all4(ArrayList<Pair<Integer, Pair<Double,String>>> first, int missing){
		double val = 0;
		if(missing == 0){
			val = first.get(1).getValueTwo().getValueOne()*R*first.get(2).getValueTwo().getValueOne()*1.0/first.get(0).getValueTwo().getValueOne();
		}
		else if(missing == 1){
			val = first.get(1).getValueTwo().getValueOne()*R*first.get(2).getValueTwo().getValueOne()*1.0/first.get(0).getValueTwo().getValueOne();
		}
		else if(missing == 2){
			val = first.get(0).getValueTwo().getValueOne()*first.get(1).getValueTwo().getValueOne()*1.0/R*first.get(2).getValueTwo().getValueOne();
		}
		else if(missing == 3){
			val = first.get(0).getValueTwo().getValueOne()*first.get(1).getValueTwo().getValueOne()*1.0/first.get(2).getValueTwo().getValueOne()*R;
		}
		return val;
	}

	public static double convert(double n, String first, String second){
		if(!first.equals("K") || !second.equals("K")){
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


}
