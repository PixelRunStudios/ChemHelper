package com.github.pixelrunstudios.ChemHelper;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

public final class SubsetHelper{
	private SubsetHelper(){
		//Do nothing
	}

	public static <T> Set<Set<T>> subsets(Set<T> set){
		return new PowerSet<T>(set);
	}

	public static class PowerSet<T> extends AbstractSet<Set<T>>{

		protected Set<T> wrapped;
		protected boolean isTimeEfficient;
		protected boolean isThreadSafe;

		protected PowerSet(){
			wrapped = new HashSet<T>();
		}

		//Immutable collection view of all subsets
		public PowerSet(Set<T> set){
			this();
			wrapped = set;
		}

		//Returned iterator uses Set's elements at creation time, does not update
		//Request new iterator to use updated elements
		@Override
		public Iterator<Set<T>> iterator(){
			return new SubsetSetIterator<T>(wrapped);
		}

		//The number of subsets = Math.pow(2, x), using ints instead of doubles
		@Override
		public int size(){
			int n = 1;
			for(int i = 0; i < wrapped.size(); i++){
				n *= 2;
			}
			return n;
		}

		@Override
		public boolean contains(Object o){
			if(o instanceof Set<?>){
				Set<?> set = (Set<?>) o;
				if(wrapped.containsAll(set)){
					return true;
				}
			}
			return false;
		}


		public static class SubsetSetIterator<T> implements Iterator<Set<T>>{

			protected TreeSet<Integer> current;
			protected ArrayList<T> array;

			public SubsetSetIterator(Set<T> set){
				array = new ArrayList<T>(set.size());
				array.addAll(set);
			}

			@Override
			public boolean hasNext(){
				if(current == null){
					return true;
				}
				if(current.size() >= array.size()){
					return false;
				}
				return true;
			}

			@Override
			public Set<T> next(){
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				if(current == null){
					current = new TreeSet<Integer>();
				}
				else{
					if(current.size() == 0){
						current.add(1);
					}
					else{
						part:{{
							int iterations = 0;
							int tempIndex = current.last();
							while(tempIndex == array.size() - iterations){
								if(tempIndex == current.first()){
									current.clear();
									for(int i = 1; i < iterations + 3; i++){
										current.add(i);
									}
									break part;
								}
								current.remove(tempIndex);
								tempIndex = current.lower(tempIndex);
								iterations++;
							}
							current.remove(tempIndex);
							for(int i = 0; i < iterations + 1; i++){
								current.add(tempIndex + 1 + i);
							}
						}}
					}
				}
				return subsetWith(current);
			}

			private Set<T> subsetWith(TreeSet<Integer> indicies){
				HashSet<T> set = new HashSet<T>(indicies.size());
				for(Integer i : indicies){
					set.add(array.get(i-1));
				}
				return Collections.unmodifiableSet(set);
			}

			//Remove is not supported since collection is immutable
			@Override
			public void remove(){
				throw new UnsupportedOperationException();
			}
		}
	}
}
