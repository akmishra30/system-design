package com.mahkir.designs.vm.data;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an adapter class which holds the data related to coins and products inside 
 * the vending machine.
 * 
 * */
public class Inventory<T> {
	private Map<T, Integer> inventory = new HashMap<T, Integer>();
	
	public int getQuantity(T item){
		Integer value = inventory.get(item);
		return value == null ? 0 : value;
	}
	
	public void add(T item){
		int count = inventory.get(item);
		inventory.put(item, count + 1);
	}
	
	public void deduct(T item){
		if(hasItem(item)){
			int count = inventory.get(item);
			inventory.put(item, count - 1);
		}
	}
	
	public boolean hasItem(T item){
		return getQuantity(item) > 0;
	}
	
	public void clear(){
		inventory.clear();
	}
	
	public void put(T item, int quantity){
		inventory.put(item, quantity);
	}
}