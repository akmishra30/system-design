package com.mahkir.designs.vm.exception;

/**
 * This exception will be thrown when there is less change to revert to customer.
 * 
 * */
public class NotSufficientChangeException extends RuntimeException{
	private String message;
	
	public NotSufficientChangeException(String message){
		this.message = message;
	}
	
	@Override
	public String getMessage(){
		return message;
	}
}
