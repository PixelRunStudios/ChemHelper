package com.github.pixelrunstudios.ChemHelper;

public class Debug{

	public static final boolean DEEP_DEBUG = true;
	public static final boolean DEBUG = true;

	public static void print(Object o){
		if(DEBUG){
			System.out.print(o);
		}
	}

	public static void println(Object o){
		if(DEBUG){
			System.out.println(o);
		}
	}

	public static void println(){
		if(DEBUG){
			System.out.println();
		}
	}

	public static void printlnDeep(String string){
		if(DEEP_DEBUG){
			println(string);
		}
	}

	public static void printlnDeep(){
		if(DEEP_DEBUG){
			println();
		}
	}

	public static void printDeep(String string){
		if(DEEP_DEBUG){
			println(string);
		}
	}

}
