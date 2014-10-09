package com.github.pixelrunstudios.ChemHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EquationBalancer{

	private EquationBalancer(){
		//Do nothing
	}

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


		Map<ChemistryUnit, Integer> map3 = new LinkedHashMap<ChemistryUnit, Integer>();
		map3.put(ChemistryUnit.mk("H"), 2);
		map3.put(ChemistryUnit.mk("O"), 1);
		ChemistryUnit c3 = ChemistryUnit.mk(map3);
		Map<ChemistryUnit, Integer> map4 = new LinkedHashMap<ChemistryUnit, Integer>();
		map4.put(ChemistryUnit.mk("C"), 1);
		map4.put(ChemistryUnit.mk("O"), 2);
		ChemistryUnit c4 = ChemistryUnit.mk(map4);
		ChemistryUnit out = ChemistryUnit.mk(Pair.make(c3, 1), Pair.make(c4, 1));


		balance(in,out);
	}

	public static Pair<ChemistryUnit, ChemistryUnit>
	balance(ChemistryUnit inX, ChemistryUnit outX){
		return balance(inX, outX, false);
	}

	private static Pair<ChemistryUnit, ChemistryUnit>
	balance(ChemistryUnit inExpression, ChemistryUnit outExpression, boolean apartMode){

		Debug.println();
		Debug.println("----Begin Balancing Equation----");
		Debug.println();

		Debug.println(inExpression);
		Debug.println(outExpression);

		/*
		 * Nomenclature:
		 *
		 *
		 * Equation: 2H2 + O2 -> 2H2O
		 * Expression: 2H2 + O2
		 * Unit: 2H2
		 * UnitType: H
		 */

		regular: if(true){
			BigFraction.setAutoSimplify(true);

			if(apartMode){
				ChemistryUnit apartUnitInExpression = new ChemistryUnit();
				for(Map.Entry<ChemistryUnit, Integer> inUnit : inExpression.getUnitEntrySet()){
					ChemistryUnit currentInUnit = apart(inUnit.getKey());
					apartUnitInExpression.putUnit(currentInUnit, inUnit.getValue());
				}
				inExpression = apartUnitInExpression;
				ChemistryUnit apartUnitOutExpression = new ChemistryUnit();
				for(Map.Entry<ChemistryUnit, Integer> outUnit : outExpression.getUnitEntrySet()){
					ChemistryUnit currentOutUnit = apart(outUnit.getKey());
					apartUnitOutExpression.putUnit(currentOutUnit, outUnit.getValue());
				}
				outExpression = apartUnitOutExpression;
			}

			//Previously known as elements
			ArrayList<ChemistryUnit> uniqueUnitTypes = new ArrayList<ChemistryUnit>();

			for(Map.Entry<ChemistryUnit, Integer> inExpressionEntry : inExpression.getUnitEntrySet()){
				for(Map.Entry<ChemistryUnit, Integer> inExpressionUnitEntry : inExpressionEntry.getKey().getUnitEntrySet()){
					boolean yesEle = false;
					for(int i = 0; i < uniqueUnitTypes.size();i++){
						if(uniqueUnitTypes.get(i).equals(inExpressionUnitEntry.getKey())){
							yesEle = true;
							break;
						}
					}
					if(!yesEle){
						Debug.printlnDeep("hi");
						uniqueUnitTypes.add(inExpressionUnitEntry.getKey());
					}
				}
			}
			for(Map.Entry<ChemistryUnit, Integer> map : outExpression.getUnitEntrySet()){
				for(Map.Entry<ChemistryUnit, Integer> outEntry : map.getKey().getUnitEntrySet()){
					if(!uniqueUnitTypes.contains(outEntry.getKey())){
						break regular;
					}
				}
			}

			Debug.println();
			Debug.print("Unique Unit Types: ");
			ArrayPrinter.printArray(uniqueUnitTypes);

			//Previously known as mapNum
			int numOfUnits = inExpression.unitSize() + outExpression.unitSize();
			//Previously known as numOfEle
			int numOfUniqueUnitTypes = uniqueUnitTypes.size();

			Debug.println("Unique Unit Types #: " + numOfUniqueUnitTypes);
			Debug.println("Units #: " + numOfUnits);

			//Previously known as system
			int[][] originalMatrix = new int[numOfUniqueUnitTypes][numOfUnits];
			//Replaced newArrayRow with numOfUnits - 1
			//Previously known as newArrayRow
			//int newMatrixRow = numOfUnits - 1;

			int originalMatrixCounter = add(true, inExpression, uniqueUnitTypes, originalMatrix, 0);
			add(false, outExpression, uniqueUnitTypes, originalMatrix, originalMatrixCounter);

			//Previously known as finale
			BigFraction[] balancedResult = new BigFraction[numOfUnits];
			balancedResult[0] = new BigFraction(1, 1);

			Set<Integer> rows = new HashSet<Integer>();

			for(int i = 0; i < uniqueUnitTypes.size(); i++){
				rows.add(i);
			}


			Set<Set<Integer>> rowsSubset = SubsetHelper.subsets(rows);
			Set<Set<Integer>> pickedRowCombinations = new HashSet<Set<Integer>>();
			for(Set<Integer> s : rowsSubset){
				if(!(!(s.size() == numOfUnits - 1) || !s.contains(1))){
					Debug.println("Testing Subset: " + s);
					pickedRowCombinations.add(s);
				}
			}
			for(Set<Integer> pickedRowCombination : pickedRowCombinations){
				Integer[] rowsPicked = new Integer[numOfUnits - 1];
				int pickedRowCombinationI = 0;
				for(Integer rowPicked : pickedRowCombination){
					rowsPicked[pickedRowCombinationI] = rowPicked;
					pickedRowCombinationI++;
				}
				Pair<Integer[], BigFraction[][]> solution = solve(rowsPicked, numOfUnits, originalMatrix, numOfUniqueUnitTypes);
				if(solution == null){
					continue;
				}
				BigFraction[][] systemX = solution.getValueTwo();
				int systemXI = 0;
				boolean noProblems = true;
				for(BigFraction[] sa : systemX){
					if(sa[0] == null || sa[0].compareTo(new BigFraction(0, 1)) <= 0){
						noProblems = false;
					}
					balancedResult[systemXI + 1] = sa[0];
					systemXI++;
				}
				if(noProblems){
					break;
				}
			}

			Debug.print("Balanced Result: ");
			ArrayPrinter.printArray(balancedResult);

			int resultDenominatorProduct = 1;
			for(BigFraction bf : balancedResult){
				if(bf == null){
					break regular;
				}
				resultDenominatorProduct *= bf.getDenominator();
			}
			for(int i = 0; i < balancedResult.length; i++){
				balancedResult[i] = balancedResult[i].multiply(new BigFraction(resultDenominatorProduct, 1));
			}

			Debug.print("Result Product:  ");
			ArrayPrinter.printArray(balancedResult);

			int[] finalResult = new int[balancedResult.length];
			for(int i = 0; i < balancedResult.length; i++){
				finalResult[i] = (int) balancedResult[i].getNumerator();
			}
			int gcdX = MathHelper.gcd(finalResult);
			for(int i = 0; i < balancedResult.length; i++){
				finalResult[i] = finalResult[i] / gcdX;
			}

			Debug.print("Final Result:  ");
			ArrayPrinter.printArray(finalResult);

			Debug.println();
			int finalResultI = 0;
			Map<ChemistryUnit, Integer> mapOfIn = new HashMap<ChemistryUnit, Integer>();
			for(Map.Entry<ChemistryUnit, Integer> map : inExpression.getUnitEntrySet()){
				int finalResultOut = finalResult[finalResultI];
				if(finalResultOut == 0){
					break regular;
				}
				mapOfIn.put(map.getKey(), finalResultOut);
				finalResultI++;
			}
			Map<ChemistryUnit, Integer> mapOfOut = new HashMap<ChemistryUnit, Integer>();
			for(Map.Entry<ChemistryUnit, Integer> map : outExpression.getUnitEntrySet()){
				int finalResultOut = finalResult[finalResultI];
				if(finalResultOut == 0){
					break regular;
				}
				mapOfOut.put(map.getKey(), finalResultOut);
				finalResultI++;
			}
			ChemistryUnit inResult = ChemistryUnit.mk(mapOfIn);
			ChemistryUnit outResult = ChemistryUnit.mk(mapOfOut);
			if(!balanced(inResult, outResult)){
				Debug.println("Balance failure");
				break regular;
			}
			Debug.println(inResult);
			Debug.println(outResult);
			Debug.println();
			Debug.println("----End Balancing Equation----");
			return Pair.make(inResult, outResult);
		}
		{
			if(apartMode){
				return null;
			}
			else{
				Debug.println("ModeSwitch");
				return balance(inExpression, outExpression, true);
			}
		}}

	private static ChemistryUnit apart(ChemistryUnit unit){
		if(unit.getType() == ChemistryUnit.TYPE_BASE){
			return unit;
		}
		ChemistryUnit unitValue = new ChemistryUnit();
		for(Map.Entry<ChemistryUnit, Integer> unitPart : unit.getUnitEntrySet()){
			ChemistryUnit unitPartValue = new ChemistryUnit();
			ChemistryUnit unitPartApartValue = apart(unitPart.getKey());
			if(unitPartApartValue.getType() == ChemistryUnit.TYPE_BASE){
				unitPartValue.putUnit(unitPartApartValue, 1);
			}
			else{
				for(Map.Entry<ChemistryUnit, Integer> xZero : unitPartApartValue.getUnitEntrySet()){
					ChemistryUnit xKey = xZero.getKey();
					if(unitPartValue.containsUnitKey(xKey)){
						unitPartValue.putUnit(xKey, unitPartApartValue.getUnit(xKey) + xZero.getValue());
					}
					else{
						unitPartValue.putUnit(xKey, xZero.getValue());
					}
				}
			}
			for(Map.Entry<ChemistryUnit, Integer> un : unitPartValue.getUnitEntrySet()){
				ChemistryUnit unKey = un.getKey();
				if(unitValue.containsUnitKey(unKey)){
					unitValue.putUnit(unKey, unitValue.getUnit(unKey) + unitPartValue.getUnit(unKey) * unitPart.getValue());
				}
				else{
					unitValue.putUnit(unKey, unitPartValue.getUnit(unKey) * unitPart.getValue());
				}
			}
		}
		return unitValue;
	}

	private static Pair<Integer[], BigFraction[][]> solve(Integer[] rowsPicked, int numOfUnits, int[][] originalMatrix, int numOfEle){

		//Previously known as system2
		int[][] workingMatrix = new int[numOfUnits - 1][numOfUnits];
		//Previously known as system2
		int[][] workingSquareMatrix = new int[numOfUnits - 1][numOfUnits - 1];

		workingMatrix = pick(rowsPicked, originalMatrix, numOfUnits);

		for(int i = 0; i<numOfUnits - 1;i++){
			for(int j = 0; j<numOfUnits-1;j++){
				workingSquareMatrix[i][j] = workingMatrix[i][j+1];
			}
		}

		//Previously known as target
		int[] targetMatrixRows = new int[numOfUnits-1];
		Integer[] rowList = new Integer[numOfUnits - 1];
		for(int i = 0; i < rowList.length; i++){
			rowList[i] = i;
		}
		Set<List<Integer>> rowOrderPermutations = CombinatoricHelper.permutations(
				Arrays.asList(rowList), rowList.length);

		rowOrderFor: for(List<Integer> rowOrder : rowOrderPermutations){
			boolean targetFailed = false;
			for(int i = 0; i < targetMatrixRows.length; i++){
				targetMatrixRows[i] = -1;
			}
			columnFor: for(int column = 0; column < numOfUnits - 1; column++){
				rowFor: for(int row : rowOrder){
					if(workingSquareMatrix[row][column] != 0){
						for(int k = 0; k<numOfUnits-1;k++){
							if(targetMatrixRows[k] == row){
								continue rowFor;
							}
						}
						targetMatrixRows[column] = row;
						continue columnFor;
					}
				}
			{
				if(targetMatrixRows[column] == -1){
					targetFailed = true;
					break;
				}
			}}
			if(targetFailed){
				continue;
			}

			Debug.print("Target Matrix Rows: ");
			ArrayPrinter.printArray(targetMatrixRows);

			Debug.println();

			//Previously known as system4
			BigFraction[][] operationMatrix = new BigFraction[numOfUnits - 1][numOfUnits];
			Debug.println("Working Matrix: ");
			for(int i = 0; i < numOfUnits - 1; i++){
				for(int j = 0; j < numOfUnits; j++){
					Debug.print(workingMatrix[i][j] + " ");
					operationMatrix[i][j] = new BigFraction(workingMatrix[targetMatrixRows[i]][j], 1);
				}
				Debug.println();
			}
			Debug.println();

			Debug.println("Operation Matrix: ");
			ArrayPrinter.printArray(operationMatrix);

			for(int i = 0; i < numOfUnits - 1; i++){
				for(int j = i + 1; j < numOfUnits - 1; j++){
					BigFraction temp = operationMatrix[j][i+1];
					for(int k = 0; k < numOfUnits; k++){
						Debug.printlnDeep("i:" + i + "j:" + j + "---" + operationMatrix[i][i+1]);
						try{
							operationMatrix[j][k] = operationMatrix[j][k].subtract(temp.multiply(
									operationMatrix[i][k].divide(operationMatrix[i][i+1])));
						}
						catch(ArithmeticException e){
							continue rowOrderFor;
						}
					}
				}
			}

			Debug.printlnDeep();

			Debug.println("Operation Matrix Pyramid: ");
			ArrayPrinter.printArray(operationMatrix);

			for(int i = 0; i < numOfUnits - 1;i++){
				int numOfUnitsI = numOfUnits - 2 - i;
				try{
					operationMatrix[numOfUnitsI][0] = operationMatrix[numOfUnitsI][0].divide(operationMatrix[numOfUnitsI][numOfUnitsI + 1]);
				}
				catch(ArithmeticException e){
					continue rowOrderFor;
				}
				operationMatrix[numOfUnitsI][numOfUnitsI + 1] = operationMatrix[numOfUnitsI][numOfUnitsI + 1].divide(operationMatrix[numOfUnitsI][numOfUnitsI + 1]);
				for(int j = numOfUnitsI-1; j>=0; j--){
					operationMatrix[j][0] = operationMatrix[j][0].subtract(operationMatrix[j][numOfUnitsI + 1].multiply(operationMatrix[numOfUnitsI][0]));
					operationMatrix[j][numOfUnitsI + 1] = operationMatrix[j][numOfUnitsI + 1].subtract(operationMatrix[j][numOfUnitsI + 1].multiply(operationMatrix[numOfUnitsI][numOfUnitsI + 1]));
				}

			}

			Debug.println("Operation Matrix Identity: ");
			ArrayPrinter.printArray(operationMatrix);

			Integer[] outRowsPicked = new Integer[rowsPicked.length];
			int rowsPickedI = 0;
			Debug.print("Rows picked: ");
			for(Integer rowPicked : rowsPicked){
				outRowsPicked[targetMatrixRows[rowsPickedI]] = rowPicked;
				Debug.print(rowPicked + " ");
				rowsPickedI++;
			}
			Debug.println();

			Debug.print("Row outcome: ");
			ArrayPrinter.printArray(outRowsPicked);

			Debug.println();

			return Pair.make(outRowsPicked, operationMatrix);
		}
		return null;
	}

	private static int[][] pick(Integer[] pickOrder, int[][] originalMatrix, int numOfUnits){
		Debug.println("Testing Permutation: " + Arrays.asList(pickOrder));
		int[][] workingMatrix = new int[numOfUnits - 1][numOfUnits];
		for(int i = 0; i<numOfUnits - 1;i++){
			workingMatrix[i] = originalMatrix[pickOrder[i]];
		}
		return workingMatrix;
	}

	private static int add(boolean b, ChemistryUnit inX, ArrayList<ChemistryUnit> uniqueUnitTypes, int[][] originalMatrix, int counterInitialValue){
		int originalMatrixCounter = counterInitialValue;
		for(Map.Entry<ChemistryUnit, Integer> map : inX.getUnitEntrySet()){
			for(Map.Entry<ChemistryUnit, Integer> entry : map.getKey().getUnitEntrySet()){
				int index = 0;
				for(int i = 0; i<uniqueUnitTypes.size();i++){
					if(uniqueUnitTypes.get(i).equals(entry.getKey())){
						index = i;
					}
				}
				originalMatrix[index][originalMatrixCounter] = originalMatrixCounter != 0 && b ? -entry.getValue() : entry.getValue();
			}

			originalMatrixCounter++;
		}
		return originalMatrixCounter;
	}

	public static boolean balanced(ChemistryUnit inExpression, ChemistryUnit outExpression){
		ChemistryUnit apartInExpression = apart(inExpression);
		ChemistryUnit apartOutExpression = apart(outExpression);
		for(Map.Entry<ChemistryUnit, Integer> outEntry : apartOutExpression.getUnitEntrySet()){
			Debug.print(outEntry + ";");
		}
		Debug.println();
		for(Map.Entry<ChemistryUnit, Integer> inEntry : apartInExpression.getUnitEntrySet()){
			Debug.print(inEntry + ";");
			ChemistryUnit inKey = inEntry.getKey();
			if(inKey.getType() == ChemistryUnit.TYPE_NEST){
				Debug.println();
				throw new IllegalArgumentException("Illegal in - separation!");
			}
			if(!apartOutExpression.containsUnitKey(inKey) || !apartOutExpression.getUnit(inKey).equals(inEntry.getValue())){
				Debug.println();
				Debug.println(inKey + ":" + apartOutExpression.getUnit(inKey) + ":" + inEntry.getValue() + ";");
				Debug.println("Not Balanced!");
				Debug.println();
				return false;
			}
		}
		for(Map.Entry<ChemistryUnit, Integer> outEntry : apartOutExpression.getUnitEntrySet()){
			ChemistryUnit inKey = outEntry.getKey();
			if(!apartOutExpression.containsUnitKey(inKey)){
				Debug.println();
				Debug.println("Not Balanced!");
				Debug.println();
				return false;
			}
		}
		Debug.println();
		Debug.println("Balanced!");
		Debug.println();
		return true;
	}
}
