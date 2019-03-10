package com.makhir.designs.vm.entity;

/**
 * This enum holds the products which supposed to sell.
 * 
 * */
public enum Product {
	PEPSI("Pepsi", 15), COKE("Coke", 20), FENTA("Fenta", 25), SODA("Soda", 30), SEVENUP("SevenUp", 35);
	
	private String name;
	private int price;
	
	private Product(String name, int price){
		this.name = name;
		this.price = price;
	}
	
	public String getName(){
		return name;
	}
	
	public int getPrice(){
		return price;
	}
}
