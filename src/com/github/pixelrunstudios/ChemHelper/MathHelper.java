package com.github.pixelrunstudios.ChemHelper;

public class MathHelper{

	public static int gcd(int a, int b){
		while(b > 0){
			int tmp = b;
			b = a % b;
			a = tmp;
		}
		return a;
	}

	public static int gcd(int[] ia){
		int r = ia[0];
		for(int i = 1; i < ia.length; i++){
			r = gcd(r, ia[i]);
		}
		return r;
	}

}
