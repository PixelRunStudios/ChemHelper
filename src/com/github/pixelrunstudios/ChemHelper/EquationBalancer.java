package com.github.pixelrunstudios.ChemHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EquationBalancer{
	public static void main(String[] args){
		Set<Map<String, Integer>> in = new HashSet<Map<String, Integer>>();
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		map1.put("Fe", 1);
		map1.put("Br", 3);
		in.add(map1);
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		map2.put("H", 2);
		map2.put("S", 1);
		map2.put("O", 4);
		in.add(map2);

		Set<Map<String, Integer>> out = new HashSet<Map<String, Integer>>();
		Map<String, Integer> map3 = new HashMap<String, Integer>();
		map3.put("Fe", 2);
		map3.put("S", 3);
		map3.put("O", 12);
		out.add(map3);
		Map<String, Integer> map4 = new HashMap<String, Integer>();
		map4.put("H", 1);
		map4.put("Br", 1);
		out.add(map4);
		balance(in,out);
	}

	public static Pair<Map<Map<String, Integer>, Integer>, Map<Map<String, Integer>, Integer>>
	balance(Set<Map<String, Integer>> in, Set<Map<String, Integer>> out){

		BigFraction.setAutoSimplify(true);
		/*
		 * Complexity of thing
		 *
		 */
		//{Fe=1,Br=3}, {H=2,S=1,O=4} -> {Fe=2,S=3,O=12} + {H=2,Br=1}
		//
		//    Map1    Map2     Map3        Map4
		// Br   3       0        0           1
		// Fe   1       2        2           0
		// S    0       1        3           0
		// H    0       2        0           1
		// O    0       4        12          0
		//
		// Final: 2, 3, 1, 6

		ArrayList<String> elements = new ArrayList<String>();
		int mapNum = 0;
		int numOfEle = 0;
		for(Map<String, Integer> map : in){
			for(Map.Entry<String, Integer> entry : map.entrySet()){
				boolean yesEle = false;
				for(int i = 0; i<elements.size();i++){
					if(elements.get(i) == entry.getKey()){
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
		mapNum = in.size() + out.size();
		numOfEle = elements.size();
		System.out.println(numOfEle + " " + mapNum);
		int[][] system = new int[numOfEle][mapNum];
		int newArrayRow = numOfEle-(numOfEle-mapNum+1);


		int counter = add(in, elements, system, 0);
		add(out, elements, system, counter);
		System.out.println("newArrayRow: "+newArrayRow);

		BigFraction[] finale = new BigFraction[mapNum];
		boolean[] finalePut = new boolean[mapNum];
		finale[0] = new BigFraction(1, 1);
		finalePut[0] = true;
		boolean finalePutFull = false;
		while(!finalePutFull){
			Pair<Integer[], BigFraction[][]> pair = solve(newArrayRow, mapNum, system, numOfEle);
			Integer[] mX = pair.getValueOne();
			BigFraction[][] systemX = pair.getValueTwo();
			int i = 0;
			BigFraction multiplicant = new BigFraction(1, 1);
			boolean mset = false;
			for(BigFraction[] sa : systemX){

				if(sa[0].compareTo(new BigFraction(0, 1)) < 0){
					i++;
					continue;
				}
				if(finalePut[mX[i]] == true && !mset){
					mset = true;
					multiplicant = finale[mX[i]].divide(sa[0]);
				}
				finale[mX[i]] = sa[0].multiply(multiplicant);
				finalePut[mX[i]] = true;

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
			System.out.println(finaleCounter);
		}
		for(BigFraction bf : finale){
			System.out.println(bf);
		}
		//TODO
		return null;
	}

	public static Pair<Integer[], BigFraction[][]> solve(int newArrayRow, int mapNum, int[][] system, int numOfEle){

		int[][] system2 = new int[newArrayRow][mapNum];
		int[][] system3 = new int[newArrayRow][mapNum-1];

		Pair<Integer[], int[][]> px = pickRandom(system, newArrayRow, mapNum);
		system2 = px.getValueTwo();

		for(int i = 0; i<newArrayRow;i++){
			for(int j = 0; j<mapNum-1;j++){
				system3[i][j] = system2[i][j+1];
			}
		}

		int[][] numOfNonZero = new int[mapNum-1][2];
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
		numOfNonZero = list.toArray(numOfNonZero);

		int[] target = new int[mapNum-1];
		for(int i = 0; i<newArrayRow;i++){
			outerFor: for(int j = 0; j<mapNum-1;j++){
				if(system3[numOfNonZero[i][0]][j] != 0){
					for(int k = 0; k<mapNum-1;k++){
						if(target[k] == j){
							continue outerFor;
						}
					}
					target[i] = j;
				}
			}
		}

		BigFraction[][] system4 = new BigFraction[newArrayRow][mapNum];
		for(int i = 0; i<newArrayRow;i++){
			for(int j = 0; j<mapNum;j++){
				system4[target[i]][j] = new BigFraction(system2[i][j], 1);
			}
		}
		for(int i = 0;i<newArrayRow;i++){
			for(int j = 0; j<mapNum;j++){
				System.out.print(system4[i][j] + " ");
			}
			System.out.println();
		}

		for(int i = 0; i<newArrayRow;i++){
			for(int j = i; j<newArrayRow-i-1; j++){
				for(int k = 0; k<mapNum;k++){
					system4[i+j+1][k] = system4[i+j+1][k].subtract(system4[i][k].multiply(
							system4[i+j+1][j+1].divide(system4[i][j+1])));
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
		return new Pair<Integer[], BigFraction[][]>(px.getValueOne(), system4);
	}

	private static Pair<Integer[], int[][]> pickRandom(int[][] system, int newArrayRow, int mapNum){
		Integer[] randomX = new Integer[newArrayRow - 1];
		for(int i = 0; i < randomX.length; i++){
			randomX[i] = i + 2;
		}
		List<Integer> ia = Arrays.<Integer>asList(randomX);
		Collections.shuffle(ia);
		randomX = ia.toArray(randomX);
		Integer[] randomY = new Integer[newArrayRow];
		System.arraycopy(randomX, 0, randomY, 1, randomX.length);
		randomY[0] = 1;
		int[][] system2 = new int[newArrayRow][mapNum];
		for(int i = 0; i<newArrayRow;i++){
			system2[i] = system[randomY[i]];
		}
		Integer[] outX = new Integer[newArrayRow];
		System.arraycopy(randomY, 0, outX, 0, newArrayRow);
		return new Pair<Integer[], int[][]>(outX, system2);
	}

	public static int add(Set<Map<String, Integer>> mapSet, ArrayList<String> elements, int[][] system, int initCounter){
		int counter = initCounter;
		for(Map<String, Integer> map : mapSet){
			for(Map.Entry<String, Integer> entry : map.entrySet()){
				int temp = 0;
				for(int i = 0; i<elements.size();i++){
					if(elements.get(i) == entry.getKey()){
						temp = i;
					}
				}
				system[temp][counter] = entry.getValue();
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

	public static class EquationComparator implements Comparator<int[]>{

		@Override
		public int compare(int[] o1, int[] o2){
			return Integer.compare(o1[1], o2[1]);
		}

	}
}
