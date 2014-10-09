package com.github.pixelrunstudios.ChemHelper;


public class ArrayPrinter{

	public static void printArray(int[][] arr){
		if(!Debug.DEBUG){
			return;
		}
		for(int[] ia : arr){
			for(int i : ia){
				Debug.print(i + " ");
			}
			Debug.println();
		}
		Debug.println();
	}

	public static <T> void printArray(T[][] arr){
		if(!Debug.DEBUG){
			return;
		}
		for(T[] ia : arr){
			for(T i : ia){
				Debug.print(i + " ");
			}
			Debug.println();
		}
		Debug.println();
	}

	public static <T> void printArray(T[] outIax){
		if(!Debug.DEBUG){
			return;
		}
		for(T i : outIax){
			Debug.print(i + " ");
		}
		Debug.println();
	}

	public static void printArray(int[] finalResult){
		if(!Debug.DEBUG){
			return;
		}
		for(int i : finalResult){
			Debug.print(i + " ");
		}
		Debug.println();
	}

	public static <T> void printArray(Iterable<T> x){
		if(!Debug.DEBUG){
			return;
		}
		for(T t : x){
			Debug.print(t + " ");
		}
		Debug.println();
	}

}
