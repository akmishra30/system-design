package com.mahkir.designs.vm.exception;

/**
 * This exception will be thrown when there is no product present in vending machine.
 * 
 * */
public class ProductSoldOutException extends RuntimeException {
	private String message;
	
	public ProductSoldOutException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
}
