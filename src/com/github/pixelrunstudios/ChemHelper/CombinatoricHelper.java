package com.github.pixelrunstudios.ChemHelper;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

public final class CombinatoricHelper{

	private CombinatoricHelper(){
		//Do nothing
	}

	public static void main(String[] args){
		List<Integer> l = Arrays.asList(1, 2, 3, 3, 3);
		Set<List<Integer>> psi = permutations(l, 3);
		int i = 0;
		for(List<Integer> li : psi){
			i++;
			System.out.println(li);
		}
		System.out.println(psi.size() + ":" + i);
	}

	public static <T> Set<List<T>> permutations(List<T> list, int outLength){
		return new PermutationSet<T>(list, outLength);
	}

	public static class PermutationSet<T> extends AbstractSet<List<T>>{

		protected List<T> wrapped;
		protected int outLength;

		protected PermutationSet(){
			wrapped = new ArrayList<T>();
		}

		//Immutable collection view of all perms/combs
		//outlength = length of output list
		public PermutationSet(List<T> list, int outLength){
			this();
			wrapped = list;
			this.outLength = outLength;
			if(outLength < 0){
				throw new ArrayIndexOutOfBoundsException();
			}
		}

		//Returned iterator uses List's elements at creation time, does not update
		//Request new iterator to use updated elements
		@Override
		public Iterator<List<T>> iterator(){
			return new PermutationIterator<T>(wrapped, outLength);
		}

		//The number of permutations
		//TODO
		@Override
		public int size(){
			if(wrapped.size() < outLength){
				return 0;
			}
			int n = 0;
			for(@SuppressWarnings("unused") List<T> t : this){
				n++;
			}
			/*HashMap<T, Integer> hashMap = new HashMap<T, Integer>();
			for(T t : wrapped){
				if(hashMap.containsKey(t)){
					hashMap.put(t, hashMap.get(t) + 1);
				}
				else{
					hashMap.put(t, 1);
				}
			}
			int n = 0;
			n = factorial(wrapped.size());
			n /= factorial(wrapped.size() - outLength);
			for(int i : hashMap.values()){
				n /= factorial(i);
			}*/
			return n;
		}

		/*
		private static int factorial(int input){
			int n = 1;
			for(int i = 0; i < input; i++){
				n *= i + 1;
			}
			return n;
		}
		 */

		public static class PermutationIterator<T> implements Iterator<List<T>>{

			protected boolean next = true;
			protected int outSize;
			protected TreeMap<Integer, Integer> map;
			protected int totalSize;
			protected ArrayList<T> index;

			protected ArrayList<Integer> current;
			protected ArrayList<Integer> nextCache;

			//Iterates through all the PERMUTATIONS
			public PermutationIterator(List<T> list, int size){
				outSize = size;
				totalSize = list.size();
				index = new ArrayList<T>(totalSize);
				current = new ArrayList<Integer>(outSize);
				for(int i = 0; i < outSize; i++){
					current.add(0);
				}
				map = new TreeMap<Integer, Integer>();
				int i = 0;
				for(T t : list){
					if(index.contains(t)){
						int n = index.lastIndexOf(t);
						map.put(n + 1, map.get(n + 1) + 1);
					}
					else{
						index.add(t);
						map.put(++i, 1);
					}
				}
				if(map.size() == 0){
					next = false;
				}
				else if(totalSize < outSize){
					next = false;
				}
				else{
					fill(0);
					ArrayList<Integer> nextCacheValue = new ArrayList<Integer>(outSize);
					nextCacheValue.addAll(current);
					nextCache = nextCacheValue;
					if(nextCache == null){
						next = false;
					}
					else{
						next = true;
					}
				}
			}

			private void fill(int i){
				for(;i < outSize; i++){
					int n = nextElement(Integer.MIN_VALUE, i);
					if(n == Integer.MIN_VALUE){
						return;
					}
					current.set(i, n);
				}
			}

			private ArrayList<Integer> safeNext(){
				boolean done = false;
				int currentIndex = outSize - 1;
				while(!done){
					int element = nextElement(current.get(currentIndex), currentIndex);
					if(element == Integer.MIN_VALUE){
						currentIndex--;
						if(currentIndex < 0){
							return null;
						}
						continue;
					}
					else{
						current.set(currentIndex, element);
						fill(currentIndex + 1);
						done = true;
					}
				}
				ArrayList<Integer> returnValue = new ArrayList<Integer>(outSize);
				returnValue.addAll(current);
				return returnValue;
			}

			private int nextElement(int above, int index){
				boolean hasNext = true;
				int n = above;
				while(hasNext){
					if(n == map.lastKey()){
						return Integer.MIN_VALUE;
					}
					n = map.higherKey(n);
					int currentN = 0;
					boolean good = true;
					int maxN = map.get(n);
					for(int i = 0; i < index; i++){
						if(current.get(i) == n){
							currentN++;
							if(currentN >= maxN){
								good = false;
								break;
							}
						}
					}
					hasNext = !good;
				}
				return n;
			}

			@Override
			public boolean hasNext(){
				return next;
			}

			@Override
			public List<T> next(){
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				ArrayList<Integer> nextItem = nextCache;
				nextCache = safeNext();
				if(nextCache == null){
					next = false;
				}
				return listWith(nextItem);
			}

			private List<T> listWith(ArrayList<Integer> indicies){
				ArrayList<T> list = new ArrayList<T>(indicies.size());
				for(Integer i : indicies){
					list.add(index.get(i-1));
				}
				return Collections.unmodifiableList(list);
			}

			//Remove is not supported since collection is immutable
			@Override
			public void remove(){
				throw new UnsupportedOperationException();
			}
		}
	}

	/*
	public static class CombinationSet<T> extends AbstractSet<List<T>>{

		protected List<T> wrapped;
		protected int outLength;

		protected CombinationSet(){
			wrapped = new ArrayList<T>();
		}

		//Immutable collection view of all perms/combs
		//outlength = length of output list
		public CombinationSet(List<T> list, int outLength){
			this();
			wrapped = list;
			this.outLength = outLength;
			if(outLength < 0){
				throw new ArrayIndexOutOfBoundsException();
			}
		}

		//Returned iterator uses List's elements at creation time, does not update
		//Request new iterator to use updated elements
		@Override
		public Iterator<List<T>> iterator(){
			return new CombinationIterator<T>(wrapped, outLength);
		}

		//The number of permutations
		//TODO
		@Override
		public int size(){
			if(wrapped.size() < outLength){
				return 0;
			}
			HashMap<T, Integer> hashMap = new HashMap<T, Integer>();
			for(T t : wrapped){
				if(hashMap.containsKey(t)){
					hashMap.put(t, hashMap.get(t) + 1);
				}
				else{
					hashMap.put(t, 1);
				}
			}
			int n = 0;
			n = factorial(wrapped.size());
			n /= factorial(wrapped.size() - outLength);
			for(int i : hashMap.values()){
				n /= factorial(i);
			}
			return n;
		}


		private static int factorial(int input){
			int n = 1;
			for(int i = 0; i < input; i++){
				n *= i + 1;
			}
			return n;
		}

		public static class CombinationIterator<T> implements Iterator<List<T>>{

			protected boolean next = true;
			protected int outSize;
			protected TreeMap<Integer, Integer> map;
			protected int totalSize;
			protected ArrayList<T> index;

			protected ArrayList<Integer> current;
			protected ArrayList<Integer> nextCache;

			//Iterates through all the PERMUTATIONS
			public CombinationIterator(List<T> list, int size){
				outSize = size;
				totalSize = list.size();
				index = new ArrayList<T>(totalSize);
				current = new ArrayList<Integer>(outSize);
				for(int i = 0; i < outSize; i++){
					current.add(0);
				}
				map = new TreeMap<Integer, Integer>();
				int i = 0;
				for(T t : list){
					if(index.contains(t)){
						int n = index.lastIndexOf(t);
						map.put(n + 1, map.get(n + 1) + 1);
					}
					else{
						index.add(t);
						map.put(++i, 1);
					}
				}
				if(map.size() == 0){
					next = false;
				}
				else if(totalSize < outSize){
					next = false;
				}
				else{
					fill(0);
					ArrayList<Integer> nextCacheValue = new ArrayList<Integer>(outSize);
					nextCacheValue.addAll(current);
					nextCache = nextCacheValue;
					if(nextCache == null){
						next = false;
					}
					else{
						next = true;
					}
				}
			}

			private void fill(int i){
				for(;i < outSize; i++){
					int n = nextElement(Integer.MIN_VALUE, i);
					if(n == Integer.MIN_VALUE){
						return;
					}
					current.set(i, n);
				}
			}

			private ArrayList<Integer> safeNext(){
				boolean done = false;
				int currentIndex = outSize - 1;
				while(!done){
					int element = nextElement(current.get(currentIndex), currentIndex);
					if(element == Integer.MIN_VALUE){
						currentIndex--;
						if(currentIndex < 0){
							return null;
						}
						continue;
					}
					else{
						current.set(currentIndex, element);
						fill(currentIndex + 1);
						done = true;
					}
				}
				ArrayList<Integer> returnValue = new ArrayList<Integer>(outSize);
				returnValue.addAll(current);
				return returnValue;
			}

			private int nextElement(int above, int index){
				boolean hasNext = true;
				int n = above;
				while(hasNext){
					if(n == map.lastKey()){
						return Integer.MIN_VALUE;
					}
					n = map.higherKey(n);
					int currentN = 0;
					boolean good = true;
					int maxN = map.get(n);
					for(int i = 0; i < index; i++){
						if(current.get(i) == n){
							currentN++;
							if(currentN >= maxN){
								good = false;
								break;
							}
						}
					}
					hasNext = !good;
				}
				return n;
			}

			@Override
			public boolean hasNext(){
				return next;
			}

			@Override
			public List<T> next(){
				if(!hasNext()){
					throw new NoSuchElementException();
				}
				ArrayList<Integer> nextItem = nextCache;
				nextCache = safeNext();
				if(nextCache == null){
					next = false;
				}
				return listWith(nextItem);
			}

			private List<T> listWith(ArrayList<Integer> indicies){
				ArrayList<T> list = new ArrayList<T>(indicies.size());
				for(Integer i : indicies){
					list.add(index.get(i-1));
				}
				return Collections.unmodifiableList(list);
			}

			//Remove is not supported since collection is immutable
			@Override
			public void remove(){
				throw new UnsupportedOperationException();
			}
		}
	}*/
}
