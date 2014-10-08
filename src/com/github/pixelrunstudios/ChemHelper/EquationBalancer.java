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

		/*Map<ChemistryUnit, Integer> map1 = new LinkedHashMap<ChemistryUnit, Integer>();
		map1.put(ChemistryUnit.mk("Fe"), 1);
		map1.put(ChemistryUnit.mk("Br"), 3);
		ChemistryUnit c1 = ChemistryUnit.mk(map1);
		Map<ChemistryUnit, Integer> map2 = new LinkedHashMap<ChemistryUnit, Integer>();
		map2.put(ChemistryUnit.mk("H"), 2);
		map2.put(ChemistryUnit.mk("S"), 1);
		map2.put(ChemistryUnit.mk("O"), 4);
		ChemistryUnit c2 = ChemistryUnit.mk(map2);
		ChemistryUnit in = ChemistryUnit.mk(Pair.make(c1, 1), Pair.make(c2, 1));

		System.out.println(in);

		Map<ChemistryUnit, Integer> map3 = new LinkedHashMap<ChemistryUnit, Integer>();
		map3.put(ChemistryUnit.mk("Fe"), 2);
		map3.put(ChemistryUnit.mk("S"), 3);
		map3.put(ChemistryUnit.mk("O"), 12);
		ChemistryUnit c3 = ChemistryUnit.mk(map3);
		Map<ChemistryUnit, Integer> map4 = new LinkedHashMap<ChemistryUnit, Integer>();
		map4.put(ChemistryUnit.mk("H"), 1);
		map4.put(ChemistryUnit.mk("Br"), 1);
		ChemistryUnit c4 = ChemistryUnit.mk(map4);
		ChemistryUnit out = ChemistryUnit.mk(Pair.make(c3, 1), Pair.make(c4, 1));
		System.out.println(out);*/

		Map<ChemistryUnit, Integer> map1 = new LinkedHashMap<ChemistryUnit, Integer>();
		map1.put(ChemistryUnit.mk("C"), 8);
		map1.put(ChemistryUnit.mk("H"), 18);
		ChemistryUnit c1 = ChemistryUnit.mk(map1);
		Map<ChemistryUnit, Integer> map2 = new LinkedHashMap<ChemistryUnit, Integer>();
		map2.put(ChemistryUnit.mk("O"), 2);
		ChemistryUnit c2 = ChemistryUnit.mk(map2);
		ChemistryUnit in = ChemistryUnit.mk(Pair.make(c1, 1), Pair.make(c2, 1));

		System.out.println(in);

		Map<ChemistryUnit, Integer> map3 = new LinkedHashMap<ChemistryUnit, Integer>();
		map3.put(ChemistryUnit.mk("H"), 2);
		map3.put(ChemistryUnit.mk("O"), 1);
		ChemistryUnit c3 = ChemistryUnit.mk(map3);
		Map<ChemistryUnit, Integer> map4 = new LinkedHashMap<ChemistryUnit, Integer>();
		map4.put(ChemistryUnit.mk("C"), 1);
		map4.put(ChemistryUnit.mk("O"), 2);
		ChemistryUnit c4 = ChemistryUnit.mk(map4);
		ChemistryUnit out = ChemistryUnit.mk(Pair.make(c3, 1), Pair.make(c4, 1));
		System.out.println(out);

		balance(in,out);
	}

	public static Pair<ChemistryUnit, ChemistryUnit>
	balance(ChemistryUnit inX, ChemistryUnit outX){

		BigFraction.setAutoSimplify(true);
		/*
		 * Complexity of thing
		 *
		 */
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

		ArrayList<ChemistryUnit> elements = new ArrayList<ChemistryUnit>();
		int mapNum = 0;
		int numOfEle = 0;
		for(Map.Entry<ChemistryUnit, Integer> map : inX.getSubUnits().entrySet()){
			for(Map.Entry<ChemistryUnit, Integer> entry : map.getKey().getSubUnits().entrySet()){
				boolean yesEle = false;
				for(int i = 0; i<elements.size();i++){
					if(elements.get(i).equals(entry.getKey())){
						yesEle = true;
						break;
					}
				}
				if(!yesEle){
					System.out.println("hi");
					elements.add(entry.getKey());
				}
			}
		}
		for(int i = 0; i<elements.size();i++){
			System.out.println(elements.get(i));
		}
		System.out.println();
		mapNum = inX.getSubUnits().size() + outX.getSubUnits().size();
		numOfEle = elements.size();
		System.out.println(numOfEle + " " + mapNum);
		int[][] system = new int[numOfEle][mapNum];
		int newArrayRow = numOfEle-(numOfEle-mapNum+1);


		int counter = add(true, inX, elements, system, 0);
		add(false, outX, elements, system, counter);
		System.out.println("newArrayRow: "+newArrayRow);

		BigFraction[] finale = new BigFraction[mapNum];
		//boolean[] finalePut = new boolean[mapNum];
		finale[0] = new BigFraction(1, 1);
		//finalePut[0] = true;
		//boolean finalePutFull = false;

		Set<Integer> set = new HashSet<Integer>();

		for(int i = 0; i < elements.size(); i++){
			set.add(i);
		}

		Set<Set<Integer>> is = SubsetHelper.subsets(set);
		Set<Set<Integer>> ins = new HashSet<Set<Integer>>();
		for(Set<Integer> s : is){
			if(!(!(s.size() == newArrayRow) || !s.contains(1))){
				System.out.println(s);
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
			for(BigFraction[] sa : systemX){
				finale[i+1] = sa[0];
				i++;
			}
			/*Integer[] mX = pair.getValueOne();
			BigFraction[][] systemX = pair.getValueTwo();
			int i = 0;
			BigFraction multiplicant = new BigFraction(1, 1);
			boolean mset = false;
			for(BigFraction[] sa : systemX){

				if(sa[0].compareTo(new BigFraction(0, 1)) < 0){
					i++;
					continue;
				}
				System.out.println("hax: " + mX[i]);
				if(finalePut[mX[i]] == true && !mset){
					mset = true;
					//multiplicant = finale[mX[i]].divide(sa[0]);
				}
				//else{
				finale[mX[i]] = sa[0].multiply(multiplicant);
				finalePut[mX[i]] = true;
				//}
				i++;
			}
			finalePutFull = true;
			int finaleCounter = 0;
			for(boolean b : finalePut){
				if(!b){
					finalePutFull = false;
				}
				else{
					finaleCounter++;
				}
			}

			System.out.println("WHoos");

			System.out.print("HO ");
			for(BigFraction bf : finale){
				System.out.print(bf + " ");
			}
			System.out.println();
			System.out.println(finaleCounter);*/
		}
		for(BigFraction bf : finale){
			System.out.println("----" + bf);
		}
		int finDenProd = 1;
		for(BigFraction bf : finale){
			finDenProd *= bf.getDenominator();
		}
		for(int i = 0; i < finale.length; i++){
			finale[i] = finale[i].multiply(new BigFraction(finDenProd, 1));
		}
		for(BigFraction bf : finale){
			System.out.println("----" + bf);
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
			System.out.println("------  " + i);
		}
		int finalOutX = 0;
		Map<ChemistryUnit, Integer> mapOfIn = new HashMap<ChemistryUnit, Integer>();
		for(Map.Entry<ChemistryUnit, Integer> map : inX.getSubUnits().entrySet()){
			mapOfIn.put(map.getKey(), finalOutOne[finalOutX]);
			finalOutX++;
		}
		Map<ChemistryUnit, Integer> mapOfOut = new HashMap<ChemistryUnit, Integer>();
		for(Map.Entry<ChemistryUnit, Integer> map : outX.getSubUnits().entrySet()){
			mapOfOut.put(map.getKey(), finalOutOne[finalOutX]);
			finalOutX++;
		}
		ChemistryUnit inRet = ChemistryUnit.mk(mapOfIn);
		ChemistryUnit outRet = ChemistryUnit.mk(mapOfOut);
		return new Pair<ChemistryUnit,
				ChemistryUnit>(inRet, outRet);
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

		/*int[][] numOfNonZero = new int[mapNum-1][2];
		for(int i = 0; i<newArrayRow;i++){
			for(int j = 0; j<mapNum-1;j++){
				if(system3[i][j] != 0){
					numOfNonZero[i][0] = i;
					numOfNonZero[i][1]++;
				}
			}
		}

		List<int[]> list = Arrays.asList(numOfNonZero);
		list.sort(new EquationComparator());
		numOfNonZero = list.toArray(numOfNonZero);*/

		int[] target = new int[mapNum-1];
		Integer[] ia = new Integer[newArrayRow];
		for(int i = 0; i < ia.length; i++){
			ia[i] = i;
		}
		Set<List<Integer>> sli = CombinatoricHelper.permutations(
				Arrays.asList(ia), ia.length);
		boolean broken = false;
		printArray(system3);

		for(List<Integer> li : sli){
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
			if(!fail){
				broken = true;
				break;
			}
		}
		if(!broken){
			return null;
		}

		/*
		for(List<Integer> li : sli){
			System.out.println("SLI" + li);
			for(int i = 0; i < target.length; i++){
				target[i] = -1;
			}

			superFor: for(int i : li){
				outerFor: for(int j = 0; j<mapNum-1;j++){
					if(system3[i][j] != 0){
						//System.out.println("Ar:" + numOfNonZero[i][0]);
						for(int x = 0; x < system3[i].length; x++){
							//System.out.print("o:" + system3[numOfNonZero[i][0]][x] + " ");
						}
						//System.out.println("I:" + i + "J:" + j + "---" + system3[numOfNonZero[i][0]][j]);
						for(int k = 0; k<mapNum-1;k++){
							if(target[k] == i){
								continue outerFor;
							}
						}
						target[j] = i;
						continue superFor;
					}
				}
			}

			for(int i : target){
				System.out.print("aa" + i + " ");
			}
			System.out.println();
			boolean fail = false;
			for(int i = 0; i < target.length; i++){
				if(target[i] == -1){
					fail = true;
				}
			}
			if(!fail){
				broken = true;
				break;
			}
		}
		if(!broken){
			return null;
		}
		 */


		BigFraction[][] system4 = new BigFraction[newArrayRow][mapNum];
		for(int i = 0; i<newArrayRow;i++){
			for(int j = 0; j<mapNum;j++){
				System.out.print(system2[i][j] + " ");
				system4[i][j] = new BigFraction(system2[target[i]][j], 1);
			}
			System.out.println();
		}
		for(int i = 0;i<newArrayRow;i++){
			for(int j = 0; j<mapNum;j++){
				System.out.print(system4[i][j] + " ");
			}
			System.out.println();
		}

		for(int i = 0; i<newArrayRow;i++){
			for(int j = i+1; j<newArrayRow; j++){
				BigFraction temp = system4[j][i+1];
				for(int k = 0; k<mapNum;k++){
					//System.out.println("i:" + i + "j:" + j + "---" + system4[i][i+1]);
					system4[j][k] = system4[j][k].subtract(temp.multiply(
							system4[i][k].divide(system4[i][i+1])));
				}
			}
		}
		System.out.println();
		for(int i = 0;i<newArrayRow;i++){
			for(int j = 0; j<mapNum;j++){
				System.out.print(system4[i][j] + " ");
			}
			System.out.println();
		}

		System.out.println();
		int counter2 = 0;
		for(int i = newArrayRow-1; i>=0;i--){
			system4[i][0] = system4[i][0].divide(system4[i][mapNum-1-counter2]);
			system4[i][mapNum-1-counter2] = system4[i][mapNum-1-counter2].divide(system4[i][mapNum-1-counter2]);
			for(int j = i-1; j>=0; j--){
				system4[j][0] = system4[j][0].subtract(system4[j][mapNum-1-counter2].multiply(system4[i][0]));
				system4[j][mapNum-1-counter2] = system4[j][mapNum-1-counter2].subtract(system4[j][mapNum-1-counter2].multiply(system4[i][mapNum-1-counter2]));
			}
			counter2++;

		}

		System.out.println();
		for(int i = 0;i<newArrayRow;i++){
			for(int j = 0; j<mapNum;j++){
				System.out.print(system4[i][j] + " ");
			}
			System.out.println();
		}

		System.out.println();

		check(newArrayRow, mapNum, target, system3);

		Integer[] iax = px.getValueOne();

		Integer[] outIax = new Integer[iax.length];
		int i = 0;
		for(Integer inx : iax){
			outIax[target[i]] = inx;
			System.out.println(inx);
			i++;
		}
		for(Integer inx : outIax){

			System.out.println(inx);
		}

		/*for(int i = 0;i<numOfEle;i++){
			System.out.print(elements.get(i)+" ");
			for(int j = 0; j<mapNum;j++){
				System.out.print(system[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(int i = 0;i<newArrayRow;i++){
			for(int j = 0; j<mapNum;j++){
				System.out.print(system2[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(int i = 0;i<newArrayRow;i++){
			for(int j = 0; j<mapNum-1;j++){
				System.out.print(system3[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(int i = 0;i<mapNum-1;i++){
			for(int j = 0; j<2;j++){
				System.out.print(numOfNonZero[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for(int i = 0; i<mapNum-1;i++){
			System.out.print(target[i] + " ");
		}
		System.out.println();*/
		//TODO
		return new Pair<Integer[], BigFraction[][]>(outIax, system4);
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
		System.out.println(Arrays.asList(randomY));
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
		for(Map.Entry<ChemistryUnit, Integer> map : inX.getSubUnits().entrySet()){
			for(Map.Entry<ChemistryUnit, Integer> entry : map.getKey().getSubUnits().entrySet()){
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
				System.out.print(i + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static class EquationComparator implements Comparator<int[]>{

		@Override
		public int compare(int[] o1, int[] o2){
			return Integer.compare(o1[1], o2[1]);
		}

	}
}
