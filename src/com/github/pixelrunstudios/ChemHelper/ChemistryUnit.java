package com.github.pixelrunstudios.ChemHelper;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ChemistryUnit{

	protected Map<ChemistryUnit, Integer> units =
			new TreeMap<ChemistryUnit, Integer>();

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

	public ChemistryUnit(Pair<ChemistryUnit, Integer>[] data){
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

	public boolean hasSubUnits(){
		return getSubUnits().size() > 0;
	}

	public static ChemistryUnit mk(String name){
		return new ChemistryUnit(name);
	}

	public static ChemistryUnit mk(Map<ChemistryUnit, Integer> data){
		return new ChemistryUnit(data);
	}

	//Probably safe...
	@SafeVarargs
	public static ChemistryUnit mk(Pair<ChemistryUnit, Integer>... data){
		return new ChemistryUnit(data);
	}
}
