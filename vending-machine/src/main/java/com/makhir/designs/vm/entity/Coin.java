package com.makhir.designs.vm.entity;

/**
 * This enum has coins which supported by the vending machine.
 * 
 * */
public enum Coin {
	PENNY(1), NICKLE(5), DIME(10), QUARTER(25), HALF(50);
	
	private int value;
	
	private Coin(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
}
