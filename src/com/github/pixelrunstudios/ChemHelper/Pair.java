package com.github.pixelrunstudios.ChemHelper;

import java.io.Serializable;

public class Pair<T, S> implements Serializable{

	private static final long serialVersionUID = -8076809629855557338L;
	//private static final int LOW_MASK = 0b0000000011111111;
	//private static final int HIGH_MASK = 0b1111111100000000;

	protected T valueOne;
	protected S valueTwo;

	protected Pair(){

	}

	public Pair(Pair<T, S> original){
		this(original.getValueOne(), original.getValueTwo());
	}

	public Pair(T valueOne, S valueTwo){
		this.valueOne = valueOne;
		this.valueTwo = valueTwo;
	}

	public T getValueOne(){
		return valueOne;
	}

	public S getValueTwo(){
		return valueTwo;
	}

	@Override
	public String toString(){
		String valueOneString = getValueOne() == null ? "null" : getValueOne().toString();
		String valueTwoString = getValueTwo() == null ? "null" : getValueTwo().toString();
		return "Pair: [" + valueOneString + "," + valueTwoString + "]";
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof Pair){
			Pair<?, ?> p = (Pair<?, ?>) o;
			if(getValueOne().equals(p.getValueOne()) && getValueTwo().equals(p.getValueTwo())){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode(){
		return getValueOne().hashCode() ^ getValueTwo().hashCode(); //<< 8 ^ HIGH_MASK ^ getValueTwo().hashCode() >> 8 ^ LOW_MASK;
	}

	/**
	 * Creates a new Pair with the given values
	 * @param v1 the value of the first item
	 * @param v2 the value of the second item
	 * @return a new Pair with the given values
	 */
	public static <T, S> Pair<T, S> make(T v1, S v2){
		return new Pair<T, S>(v1, v2);
	}

	/**
	 * Creates a new Pair with the same values as the given Pair
	 * @param original the Pair to obtain values from
	 * @return a new Pair with the same values as the given Pair
	 */
	public static <T, S> Pair<T, S> make(Pair<T, S> pair){
		return new Pair<T, S>(pair);
	}
}
