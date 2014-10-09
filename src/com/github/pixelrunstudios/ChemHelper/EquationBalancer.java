package com.github.pixelrunstudios.ChemHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EquationBalancer{

	public static void main(String[] args){

		//Examples:

		//{Fe=1,Br=3}, {H=2,S=1,O=4} -> {Fe=2,S=3,O=12} + {H=1,Br=1}
		//
		//    Map1    Map2     Map3        Map4
		// Br   3       0        0           1
		// Fe   1       2        2           0
		// S    0       1        3           0
		// H    0       2        0           1
		// O    0       4        12          0
		//
		// Final: 2, 3, 1, 6

		//{C=8,H=18}, {O=2} -> {H=2,O=1} + {C=1,O=2}
		//
		//    Map1    Map2       Map3        Map4
		// C    8       0          0           1
		// H    18      0          2           0
		// O    0       2          1           2
		//
		// Final: 2, 25, 18, 16

		Map<ChemistryUnit, Integer> map1 = new LinkedHashMap<ChemistryUnit, Integer>();
		map1.put(ChemistryUnit.mk("C"), 8);
		map1.put(ChemistryUnit.mk("H"), 18);
		ChemistryUnit c1 = ChemistryUnit.mk(map1);
		Map<ChemistryUnit, Integer> map2 = new LinkedHashMap<ChemistryUnit, Integer>();
		map2.put(ChemistryUnit.mk("O"), 2);
		ChemistryUnit c2 = ChemistryUnit.mk(map2);
		ChemistryUnit in = ChemistryUnit.mk(Pair.make(c1, 1), Pair.make(c2, 1));
		Debug.println(in);

		Map<ChemistryUnit, Integer> map3 = new LinkedHashMap<ChemistryUnit, Integer>();
		map3.put(ChemistryUnit.mk("H"), 2);
		map3.put(ChemistryUnit.mk("O"), 1);
		ChemistryUnit c3 = ChemistryUnit.mk(map3);
		Map<ChemistryUnit, Integer> map4 = new LinkedHashMap<ChemistryUnit, Integer>();
		map4.put(ChemistryUnit.mk("C"), 1);
		map4.put(ChemistryUnit.mk("O"), 2);
		ChemistryUnit c4 = ChemistryUnit.mk(map4);
		ChemistryUnit out = ChemistryUnit.mk(Pair.make(c3, 1), Pair.make(c4, 1));
		Debug.println(out);

		balance(in,out);
	}

	public static Pair<ChemistryUnit, ChemistryUnit>
	balance(ChemistryUnit inX, ChemistryUnit outX){
		return balance(inX, outX, false);
	}

	public static Pair<ChemistryUnit, ChemistryUnit>
	balance(ChemistryUnit inX, ChemistryUnit outX, boolean apart){

		BigFraction.setAutoSimplify(true);

		if(apart){
			ChemistryUnit inNewX = new ChemistryUnit();
			for(Map.Entry<ChemistryUnit, Integer> cur : inX.getUnits().entrySet()){

				ChemistryUnit cnr = apart(cur.getKey());
				inNewX.putUnit(cnr, cur.getValue());
			}
			inX = inNewX;
			ChemistryUnit outNewX = new ChemistryUnit();
			for(Map.Entry<ChemistryUnit, Integer> cur : outX.getUnits().entrySet()){

				ChemistryUnit cnr = apart(cur.getKey());
				outNewX.putUnit(cnr, cur.getValue());
			}
			outX = outNewX;
		}

		ArrayList<ChemistryUnit> elements = new ArrayList<ChemistryUnit>();
		int mapNum = 0;
		int numOfEle = 0;
		for(Map.Entry<ChemistryUnit, Integer> map : inX.getUnits().entrySet()){
			for(Map.Entry<ChemistryUnit, Integer> entry : map.getKey().getUnits().entrySet()){
				boolean yesEle = false;
				for(int i = 0; i<elements.size();i++){
					if(elements.get(i).equals(entry.getKey())){
						yesEle = true;
						break;
					}
				}
				if(!yesEle){
					Debug.printlnDeep("hi");
					elements.add(entry.getKey());
				}
			}
		}
		for(Map.Entry<ChemistryUnit, Integer> map : outX.getUnits().entrySet()){
			for(Map.Entry<ChemistryUnit, Integer> outEntry : map.getKey().getUnits().entrySet()){
				if(!elements.contains(outEntry.getKey())){
					if(apart){
						return null;
					}
					else{
						Debug.println("ModeSwitch");
						return balance(inX, outX, true);
					}
				}
			}
		}
		for(int i = 0; i<elements.size();i++){
			Debug.print(elements.get(i) + " ");
		}
		Debug.println();
		mapNum = inX.getSubUnits().size() + outX.getSubUnits().size();
		numOfEle = elements.size();
		Debug.println(numOfEle + " " + mapNum);
		int[][] system = new int[numOfEle][mapNum];
		int newArrayRow = numOfEle-(numOfEle-mapNum+1);


		int counter = add(true, inX, elements, system, 0);
		add(false, outX, elements, system, counter);
		Debug.println("newArrayRow: "+newArrayRow);

		BigFraction[] finale = new BigFraction[mapNum];
		finale[0] = new BigFraction(1, 1);

		Set<Integer> set = new HashSet<Integer>();

		for(int i = 0; i < elements.size(); i++){
			set.add(i);
		}

		Set<Set<Integer>> is = SubsetHelper.subsets(set);
		Set<Set<Integer>> ins = new HashSet<Set<Integer>>();
		for(Set<Integer> s : is){
			if(!(!(s.size() == newArrayRow) || !s.contains(1))){
				Debug.println(s);
				ins.add(s);
			}
		}
		for(Set<Integer> s : ins){
			Integer[] ia = new Integer[newArrayRow];
			int ooo = 0;
			for(Integer i : s){
				ia[ooo] = i;
				ooo++;
			}
			Pair<Integer[], BigFraction[][]> pair = solve(ia, newArrayRow, mapNum, system, numOfEle);
			if(pair == null){
				continue;
			}
			BigFraction[][] systemX = pair.getValueTwo();
			int i = 0;
			boolean win = true;
			for(BigFraction[] sa : systemX){
				if(sa[0] == null || sa[0].compareTo(new BigFraction(0, 1)) <= 0){
					win = false;
				}
				finale[i+1] = sa[0];
				i++;
			}
			if(win){
				break;
			}
		}
		for(BigFraction bf : finale){
			Debug.println("----" + bf);
		}
		int finDenProd = 1;
		for(BigFraction bf : finale){
			if(bf == null){
				if(apart){
					return null;
				}
				else{
					Debug.println("ModeSwitch");
					return balance(inX, outX, true);
				}
			}
			finDenProd *= bf.getDenominator();
		}
		for(int i = 0; i < finale.length; i++){
			finale[i] = finale[i].multiply(new BigFraction(finDenProd, 1));
		}
		for(BigFraction bf : finale){
			Debug.println("----" + bf);
		}
		int[] finalOutOne = new int[finale.length];
		for(int i = 0; i < finale.length; i++){
			finalOutOne[i] = (int) finale[i].getNumerator();
		}
		int gcdX = gcd(finalOutOne);
		for(int i = 0; i < finale.length; i++){
			finalOutOne[i] = finalOutOne[i] / gcdX;
		}
		for(int i : finalOutOne){
			Debug.println("------  " + i);
		}
		int finalOutX = 0;
		Map<ChemistryUnit, Integer> mapOfIn = new HashMap<ChemistryUnit, Integer>();
		for(Map.Entry<ChemistryUnit, Integer> map : inX.getUnits().entrySet()){
			int fOut = finalOutOne[finalOutX];
			if(fOut == 0){
				if(apart){
					return null;
				}
				else{
					Debug.println("ModeSwitch");
					return balance(inX, outX, true);
				}
			}
			mapOfIn.put(map.getKey(), fOut);
			finalOutX++;
		}
		Map<ChemistryUnit, Integer> mapOfOut = new HashMap<ChemistryUnit, Integer>();
		for(Map.Entry<ChemistryUnit, Integer> map : outX.getUnits().entrySet()){
			int fOut = finalOutOne[finalOutX];
			if(fOut == 0){
				if(apart){
					return null;
				}
				else{
					Debug.println("ModeSwitch");
					return balance(inX, outX, true);
				}
			}
			mapOfOut.put(map.getKey(), fOut);
			finalOutX++;
		}
		ChemistryUnit inRet = ChemistryUnit.mk(mapOfIn);
		ChemistryUnit outRet = ChemistryUnit.mk(mapOfOut);
		if(!balanced(inRet, outRet)){
			Debug.println("Balance failure");
			if(apart){
				return null;
			}
			else{
				Debug.println("ModeSwitch");
				return balance(inX, outX, true);
			}
		}
		Debug.println(inRet);
		Debug.println(outRet);
		return new Pair<ChemistryUnit,
				ChemistryUnit>(inRet, outRet);
	}

	public static ChemistryUnit apart(ChemistryUnit inZ){
		if(inZ.getType() == ChemistryUnit.TYPE_BASE){
			return inZ;
		}
		ChemistryUnit valOut = new ChemistryUnit();
		for(Map.Entry<ChemistryUnit, Integer> unit : inZ.getUnits().entrySet()){
			ChemistryUnit val = new ChemistryUnit();
			ChemistryUnit x = apart(unit.getKey());
			if(x.getType() == ChemistryUnit.TYPE_BASE){
				val.putUnit(x, 1);
			}
			else{
				for(Map.Entry<ChemistryUnit, Integer> xZero : x.getUnits().entrySet()){
					ChemistryUnit xKey = xZero.getKey();
					if(val.containsUnitKey(xKey)){
						val.putUnit(xKey, x.getUnit(xKey) + xZero.getValue());
					}
					else{
						val.putUnit(xKey, xZero.getValue());
					}
				}
			}
			for(Map.Entry<ChemistryUnit, Integer> un : val.getUnits().entrySet()){
				ChemistryUnit unKey = un.getKey();
				if(valOut.containsUnitKey(unKey)){
					valOut.putUnit(unKey, valOut.getUnit(unKey) + val.getUnit(unKey) * unit.getValue());
				}
				else{
					valOut.putUnit(unKey, val.getUnit(unKey) * unit.getValue());
				}
			}
		}
		return valOut;
	}

	private static int gcd(int a, int b){
		while(b > 0){
			int tmp = b;
			b = a % b;
			a = tmp;
		}
		return a;
	}

	private static int gcd(int[] ia){
		int r = ia[0];
		for(int i = 1; i < ia.length; i++){
			r = gcd(r, ia[i]);
		}
		return r;
	}

	public static Pair<Integer[], BigFraction[][]> solve(Integer[] randomY, int newArrayRow, int mapNum, int[][] system, int numOfEle){

		int[][] system2 = new int[newArrayRow][mapNum];
		int[][] system3 = new int[newArrayRow][mapNum-1];

		Pair<Integer[], int[][]> px = pickRandom(randomY, system, newArrayRow, mapNum);
		system2 = px.getValueTwo();

		for(int i = 0; i<newArrayRow;i++){
			for(int j = 0; j<mapNum-1;j++){
				system3[i][j] = system2[i][j+1];
			}
		}

		int[] target = new int[mapNum-1];
		Integer[] ia = new Integer[newArrayRow];
		for(int i = 0; i < ia.length; i++){
			ia[i] = i;
		}
		Set<List<Integer>> sli = CombinatoricHelper.permutations(
				Arrays.asList(ia), ia.length);
		printArray(system3);

		megaFor: for(List<Integer> li : sli){
			boolean fail = false;
			for(int i = 0; i < target.length; i++){
				target[i] = -1;
			}
			superFor: for(int i = 0; i < mapNum - 1; i++){
				outerFor: for(int lx : li){
					if(system3[lx][i] != 0){
						for(int k = 0; k<mapNum-1;k++){
							if(target[k] == lx){
								continue outerFor;
							}
						}
						target[i] = lx;
						continue superFor;
					}
				}
			if(target[i] == -1){
				fail = true;
				break;
			}
			}
			if(fail){
				continue;
			}

			for(int i : target){
				Debug.print("aa" + i + " ");
			}
			Debug.println();

			BigFraction[][] system4 = new BigFraction[newArrayRow][mapNum];
			for(int i = 0; i<newArrayRow;i++){
				for(int j = 0; j<mapNum;j++){
					Debug.print(system2[i][j] + " ");
					system4[i][j] = new BigFraction(system2[target[i]][j], 1);
				}
				Debug.println();
			}

			printArray(system4);

			for(int i = 0; i<newArrayRow;i++){
				for(int j = i+1; j<newArrayRow; j++){
					BigFraction temp = system4[j][i+1];
					for(int k = 0; k<mapNum;k++){
						Debug.printlnDeep("i:" + i + "j:" + j + "---" + system4[i][i+1]);
						try{
							system4[j][k] = system4[j][k].subtract(temp.multiply(
									system4[i][k].divide(system4[i][i+1])));
						}
						catch(ArithmeticException e){
							continue megaFor;
						}
					}
				}
			}

			Debug.println();

			printArray(system4);

			int counter2 = 0;
			for(int i = newArrayRow-1; i>=0;i--){
				try{
					system4[i][0] = system4[i][0].divide(system4[i][mapNum-1-counter2]);
				}
				catch(ArithmeticException e){
					continue megaFor;
				}
				system4[i][mapNum-1-counter2] = system4[i][mapNum-1-counter2].divide(system4[i][mapNum-1-counter2]);
				for(int j = i-1; j>=0; j--){
					system4[j][0] = system4[j][0].subtract(system4[j][mapNum-1-counter2].multiply(system4[i][0]));
					system4[j][mapNum-1-counter2] = system4[j][mapNum-1-counter2].subtract(system4[j][mapNum-1-counter2].multiply(system4[i][mapNum-1-counter2]));
				}
				counter2++;

			}

			printArray(system4);

			check(newArrayRow, mapNum, target, system3);

			Integer[] iax = px.getValueOne();

			Integer[] outIax = new Integer[iax.length];
			int i = 0;
			for(Integer inx : iax){
				outIax[target[i]] = inx;
				Debug.println(inx);
				i++;
			}

			printArray(outIax);

			return new Pair<Integer[], BigFraction[][]>(outIax, system4);
		}
		return null;
	}

	private static Pair<Integer[], int[][]> pickRandom(Integer[] randomY, int[][] system, int newArrayRow, int mapNum){
		if(randomY == null){
			Integer[] randomX = new Integer[newArrayRow - 1];
			for(int i = 0; i < randomX.length; i++){
				randomX[i] = i + 2;
			}
			List<Integer> ia = Arrays.<Integer>asList(randomX);
			Collections.shuffle(ia);
			randomX = ia.toArray(randomX);
			randomY = new Integer[newArrayRow];
			System.arraycopy(randomX, 0, randomY, 1, randomX.length);
			randomY[0] = 1;
		}
		Debug.println(Arrays.asList(randomY));
		int[][] system2 = new int[newArrayRow][mapNum];
		for(int i = 0; i<newArrayRow;i++){
			system2[i] = system[randomY[i]];
		}
		Integer[] outX = new Integer[newArrayRow];
		System.arraycopy(randomY, 0, outX, 0, newArrayRow);
		return new Pair<Integer[], int[][]>(outX, system2);
	}

	public static int add(boolean b, ChemistryUnit inX, ArrayList<ChemistryUnit> elements, int[][] system, int initCounter){
		int counter = initCounter;
		for(Map.Entry<ChemistryUnit, Integer> map : inX.getUnits().entrySet()){
			for(Map.Entry<ChemistryUnit, Integer> entry : map.getKey().getUnits().entrySet()){
				int temp = 0;
				for(int i = 0; i<elements.size();i++){
					if(elements.get(i).equals(entry.getKey())){
						temp = i;
					}
				}
				system[temp][counter] = counter != 0 && b ? -entry.getValue() : entry.getValue();
			}

			counter++;
		}
		return counter;
	}

	public static boolean check(int newArrayRow, int mapNum, int[] target, int[][] system3){
		boolean done = false;
		boolean onTarget = false;
		int zeroes = 0;
		int rowDone = 0;
		outerFor: for(int i = 0; i<newArrayRow;i++){
			onTarget = false;
			zeroes = 0;
			for(int j = 0; j<mapNum-1;j++){
				if(j != target[i]){
					if(system3[i][j] !=0){
						break outerFor;
					}else{
						zeroes++;
					}
				}else{
					if(target[i] !=1){
						break outerFor;
					}else{
						onTarget = true;
					}
				}
			}
			if(zeroes == mapNum-2 && onTarget){
				rowDone++;
			}
		}
		if(rowDone == newArrayRow){
			done = true;
		}else{
			done = false;
		}
		return done;
	}

	private static void printArray(int[][] arr){
		for(int[] ia : arr){
			for(int i : ia){
				Debug.print(i + " ");
			}
			Debug.println();
		}
		Debug.println();
	}

	private static <T> void printArray(T[][] arr){
		for(T[] ia : arr){
			for(T i : ia){
				Debug.print(i + " ");
			}
			Debug.println();
		}
		Debug.println();
	}

	private static <T> void printArray(T[] outIax){
		for(T i : outIax){
			Debug.print(i + " ");
		}
		Debug.println();
	}

	public static class EquationComparator implements Comparator<int[]>{

		@Override
		public int compare(int[] o1, int[] o2){
			return Integer.compare(o1[1], o2[1]);
		}

	}

	public static boolean balanced(ChemistryUnit inX, ChemistryUnit outX){
		ChemistryUnit inZ = apart(inX);
		ChemistryUnit outZ = apart(outX);
		for(Map.Entry<ChemistryUnit, Integer> outZero : outZ.getUnits().entrySet()){
			Debug.print(outZero + ";");
		}
		Debug.println();
		for(Map.Entry<ChemistryUnit, Integer> inZero : inZ.getUnits().entrySet()){
			Debug.print(inZero + ";");
			ChemistryUnit inKey = inZero.getKey();
			if(inKey.getType() == ChemistryUnit.TYPE_NEST){
				throw new IllegalArgumentException("Illegal in - separation!");
			}
			if(!outZ.containsUnitKey(inKey) || !outZ.getUnit(inKey).equals(inZero.getValue())){
				Debug.println(inKey + ":" + outZ.getUnit(inKey) + ":" + inZero.getValue() + ";");
				Debug.println("Not Balanced! 01");
				return false;
			}
		}
		for(Map.Entry<ChemistryUnit, Integer> outZero : outZ.getUnits().entrySet()){
			ChemistryUnit inKey = outZero.getKey();
			if(!outZ.containsUnitKey(inKey)){
				Debug.println("Not Balanced! 02");
				return false;
			}
		}
		Debug.println("Balanced!");
		return true;
	}
}
