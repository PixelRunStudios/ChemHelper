package com.github.pixelrunstudios.ChemHelper;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ChemistryUnit implements Comparable<ChemistryUnit>{

	private Map<ChemistryUnit, Integer> units =
			new LinkedHashMap<ChemistryUnit, Integer>();

	//For base
	protected String name;

	protected ChemistryUnit(Map<ChemistryUnit, Integer> map){
		units.putAll(map);
	}

	protected ChemistryUnit(String name){
		this.name = name;
	}

	protected ChemistryUnit(){

	}

	protected boolean containsUnitKey(ChemistryUnit key){
		return units.containsKey(key);
	}

	protected void putUnit(ChemistryUnit key, Integer value){
		if(value == 0){
			System.out.println("a");
			return;
		}
		units.put(key, value);
	}

	protected Integer getUnit(ChemistryUnit key){
		return units.get(key);
	}

	protected void clearUnit(){
		units.clear();
	}

	private ChemistryUnit(Pair<ChemistryUnit, Integer>[] data){
		for(Pair<ChemistryUnit, Integer> p : data){
			units.put(p.getValueOne(), p.getValueTwo());
		}
	}

	public static final int TYPE_BASE = 0;
	public static final int TYPE_NEST = 1;

	public int getType(){
		if(!hasSubUnits()){
			return TYPE_BASE;
		}
		else{
			return TYPE_NEST;
		}
	}

	public String getName(){
		if(getType() == TYPE_BASE){
			return name;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Reutrns a map with size = 0 on no sub-units
	 * @return
	 */
	public Map<ChemistryUnit, Integer> getSubUnits(){
		return Collections.unmodifiableMap(units);
	}

	protected Map<ChemistryUnit, Integer> getUnits(){
		return units;
	}

	protected Set<Map.Entry<ChemistryUnit, Integer>>
	getUnitEntrySet(){
		return getUnits().entrySet();
	}

	public Set<Map.Entry<ChemistryUnit, Integer>>
	getSubUnitEntrySet(){
		return Collections.unmodifiableSet(getUnits().entrySet());
	}

	public boolean hasSubUnits(){
		return getSubUnits().size() > 0;
	}

	public static ChemistryUnit mk(String name){
		if(name == null){
			Debug.printlnDeep("aaaar");
			return null;
		}
		return new ChemistryUnit(name);
	}

	public static ChemistryUnit mk(Map<ChemistryUnit, Integer> data){
		if(data == null || data.size() == 0){
			return null;
		}
		return new ChemistryUnit(data);
	}

	//Probably safe...
	@SafeVarargs
	public static ChemistryUnit mk(Pair<ChemistryUnit, Integer>... data){
		if(data == null || data.length == 0){
			return null;
		}
		return new ChemistryUnit(data);
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof ChemistryUnit){
			ChemistryUnit cu = (ChemistryUnit) o;
			if(getType() == TYPE_NEST && cu.getType() == TYPE_NEST && cu.getSubUnits().equals(getSubUnits())){
				return true;
			}
			if(getType() == TYPE_BASE && cu.getType() == TYPE_BASE && cu.getName().equals(getName())){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode(){
		if(getType() == TYPE_NEST){
			return getSubUnits().hashCode();
		}
		if(getType() == TYPE_BASE){
			return getName().hashCode();
		}
		throw new IllegalStateException("Illegal type");
	}

	protected void putAllUnits(Map<ChemistryUnit, Integer> subUnits){
		for(Map.Entry<ChemistryUnit, Integer> unit : subUnits.entrySet()){
			putUnit(unit.getKey(), unit.getValue());
		}
	}

	@Override
	public int compareTo(ChemistryUnit o){
		return new Integer(hashCode()).compareTo(o.hashCode());
	}

	public int unitSize(){
		return units.size();
	}

	@Override
	public String toString(){
		if(getType() == ChemistryUnit.TYPE_BASE){
			return getName();
		}
		else{
			String build = "";
			for(Map.Entry<ChemistryUnit, Integer> x : units.entrySet()){
				build += x.getKey().toString() + (x.getValue() <= 1 ? "" : x.getValue());
			}
			build = "(" + build + ")";
			return build;
		}
	}
}
